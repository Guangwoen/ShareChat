package sharechat.com.websocket;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import sharechat.com.entity.Message;
import sharechat.com.repository.MessageRepository;
import sharechat.com.repository.ShareMessageRepository;
import sharechat.com.util.SnowflakeHelper;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.*;
import java.util.Collections;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

@Component
@ServerEndpoint(value="/websocket/{sender}/2/{receiver}/{type}", configurator = EndpointConfigure.class)
public class WebSocketService {
    private static int onlineCount = 0;

    private final MessageRepository messageRepository;

    private final ShareMessageRepository shareMessageRepository;

    private final RedisTemplate<String, ?> redisTemplate;

    private final SnowflakeHelper idGenerator;

    public WebSocketService(MessageRepository messageRepository,
                            ShareMessageRepository shareMessageRepository,
                            RedisTemplate<String, ?> redisTemplate,
                            SnowflakeHelper snowflakeHelper) {
        this.messageRepository = messageRepository;
        this.shareMessageRepository = shareMessageRepository;
        this.redisTemplate = redisTemplate;
        this.idGenerator = snowflakeHelper;
    }

    /**
     * 临时存储用户与服务器的每个对话
     */
    private static final ConcurrentHashMap<String, Session> webSocketServices
            = new ConcurrentHashMap<>();

    private String makeKey(String a, String b) { return a+"_"+b; }

    @OnOpen
    public void onOpen(@PathParam("sender") String sender,
                       @PathParam("receiver") String receiver,
                       Session session,
                       EndpointConfig config) throws IOException {
        webSocketServices.put(makeKey(sender, receiver), session);
        addOnlineCount();
        ListOperations<String, Message> listOps = (ListOperations<String, Message>) redisTemplate.opsForList();

        /* 首先检查Redis中是否存在离线消息，之后再查询数据库 */

        /* 因为缓存中的信息比数据库中的信息更新，所以先获取缓存中的信息 */
        List<Message> msgList = listOps.range(makeKey(receiver, sender), 0, -1);
        if(msgList != null && msgList.size() != 0) {
            for(Message m: msgList) {
                sendMessage(m.getMsgBody(), receiver, sender);
                m.setIsReceived(true);
                messageRepository.save(m);
            }
            listOps.trim(makeKey(receiver, sender), 0, 0); // 从缓存中删除
            listOps.leftPop(makeKey(receiver, sender));
        }

        /* 之后检查数据库中的信息 */
        List<Message> dbMsgList = messageRepository.findOffLineMessage(makeKey(receiver, sender));
        if(dbMsgList != null && dbMsgList.size() != 0) {
            for(Message m: dbMsgList) {
                sendMessage(m.getMsgBody(), receiver, sender);
                messageRepository.updateIsReceived(m.getMsgId(), m.getMsgsendTime(), m.getSenderId(), true); // 重置字段
            }
        }
        System.out.println("新连接: " + sender + ", 当前在线会话为:" + getOnlineCount());
    }

    @OnClose
    public void onClose(@PathParam("sender") String sender,
                        @PathParam("receiver") String receiver) {
        webSocketServices.remove(makeKey(sender, receiver));
        subOnlineCount();
        ListOperations<String, Message> listOps = (ListOperations<String, Message>) redisTemplate.opsForList();

        /* 将Redis内的离线消息持久化 */
        List<Message> msgList = listOps.range(makeKey(sender, receiver), 0, -1);
        if(msgList != null && msgList.size() != 0) {
            messageRepository.saveAll(msgList);
        }
        listOps.trim(makeKey(sender, receiver), 0, 0);
        listOps.leftPop(makeKey(sender, receiver));
        System.out.println("一个链接关闭！"+sender+", 当前在线会话为:" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(@PathParam("sender") String sender,
                          @PathParam("receiver") String receiver,
                          @PathParam("type") String type,
                          String message,
                          Session session) {
        System.out.println("来自客户端" + sender + "的消息:" + message);

        if(type.equals("only")) {
            onlyMessage(sender, receiver, message); // 一般消息
        }
        else if(type.equals("shared")) {
            sharedMessage(sender, receiver, message); // 共享消息
        }
        else System.out.println("类型错误");
    }

    private void onlyMessage(String sender, String receiver, String message) {
        ListOperations<String, Message> listOps = (ListOperations<String, Message>) redisTemplate.opsForList();
        boolean isReceiverOnFace = webSocketServices.containsKey(makeKey(receiver, sender));
        System.out.println("boolean: " + isReceiverOnFace);
        // 如果在对方离线的时候发送消息
        Message newMsg = new Message(
                String.valueOf(idGenerator.snowflakeId()),
                makeKey(sender, receiver),
                sender,
                Instant.now().toString(),
                message,
                null
        );
        if(!isReceiverOnFace) {
            System.out.println("is offline");
            newMsg.setIsReceived(false);
            listOps.rightPush(makeKey(sender, receiver), newMsg);
        }
        else {
            newMsg.setIsReceived(true);
            messageRepository.save(newMsg);
            try{
                sendMessage(message, sender, receiver);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sharedMessage(String sender, String receiver, String message) {
        Message newMsg = new Message(
                String.valueOf(idGenerator.snowflakeId()),
                makeKey(sender, receiver),
                sender,
                Instant.now().toString(),
                message,
                null
        );
        shareMessageRepository.save(newMsg);
        try {
            sendMessage(message, sender, receiver);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) throws IOException {
        error.printStackTrace();
    }

    /**
     * 发送消息
     * @param message 消息本体
     * @param sender 消息发送者
     * @param receiver 消息接收对象
     * */
    public void sendMessage(String message, String sender, String receiver) throws IOException {
        webSocketServices.get(makeKey(receiver, sender))
                .getBasicRemote().sendText(message);
    }

    /**
     * 群发消息
     * */
    public void sendInfo(String message) throws IOException {
        Collections.list(webSocketServices.keys()).forEach(item -> {
            String[] users = item.split("_");
            try {
                System.out.println("INFO:: " + users[0] + " " + users[1]);
                sendMessage(message, users[0], users[1]);
            }
            catch (IOException e) {
                return;
            }
        });
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketService.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketService.onlineCount--;
    }
}
