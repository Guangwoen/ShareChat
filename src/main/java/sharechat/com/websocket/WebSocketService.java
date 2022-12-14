package sharechat.com.websocket;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import sharechat.com.entity.Message;
import sharechat.com.repository.MessageRepository;
import sharechat.com.util.SnowflakeHelper;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

@Component
@ServerEndpoint(value="/websocket/{sender}/2/{receiver}", configurator = EndpointConfigure.class)
public class WebSocketService {
    private static int onlineCount = 0;

    private final MessageRepository messageRepository;

    private final RedisTemplate redisTemplate;

    private final SnowflakeHelper idGenerator;

    public WebSocketService(MessageRepository messageRepository,
                            RedisTemplate redisTemplate,
                            SnowflakeHelper snowflakeHelper) {
        this.messageRepository = messageRepository;
        this.redisTemplate = redisTemplate;
        this.idGenerator = snowflakeHelper;
    }

    /**
     * 临时存储用户与服务器的每个对话
     */
    private static final ConcurrentHashMap<String, Session> webSocketServices
            = new ConcurrentHashMap<>();

    private String makeKey(String a, String b) { return a+"|"+b; }

    @OnOpen
    public void onOpen(@PathParam("sender") String sender,
                       @PathParam("receiver") String receiver,
                       Session session,
                       EndpointConfig config) throws IOException {
        webSocketServices.put(makeKey(sender, receiver), session);
        addOnlineCount();

        /* 首先检查Redis中是否存在离线消息，之后再查询数据库 */

        /* 因为缓存中的信息比数据库中的信息更新，所以先获取缓存中的信息 */
        List<String> msgList = redisTemplate.opsForList().range(makeKey(receiver, sender), 0, -1);
        if(msgList != null && msgList.size() != 0) {
            for(String m: msgList) {
                sendMessage(m, receiver, sender);
                redisTemplate.opsForList().trim(makeKey(receiver, sender), 0, 0); // 从缓存中删除
                redisTemplate.opsForList().leftPop(makeKey(receiver, sender));
            }
        }

        /* 之后检查数据库中的信息 */
        List<Message> dbMsgList = messageRepository.findOffLineMessage(makeKey(receiver, sender));
        if(dbMsgList != null && dbMsgList.size() != 0) {
            for(Message m: dbMsgList) {
                sendMessage(m.getMsgBody(), receiver, sender);
                messageRepository.updateIsReceived(m.getMsgId(), true); // 重置字段
            }
        }
        System.out.println("新连接: " + sender + ", 当前在线会话为:" + getOnlineCount());
    }

    @OnClose
    public void onClose(@PathParam("sender") String sender,
                        @PathParam("receiver") String receiver) {
        webSocketServices.remove(makeKey(sender, receiver));
        subOnlineCount();

        /* 将Redis内的离线消息持久化 */
        List<String> msgList = redisTemplate.opsForList().range(makeKey(sender, receiver), 0, -1);
        if(msgList != null && msgList.size() != 0) {
            for(String m: msgList) {
                messageRepository.save(new Message(
                        String.valueOf(idGenerator.snowflakeId()),
                        makeKey(sender, receiver),
                        sender,
                        Instant.now(),
                        m,
                        false
                ));
            }
        }
        redisTemplate.opsForList().trim(makeKey(sender, receiver), 0, 0);
        redisTemplate.opsForList().leftPop(makeKey(sender, receiver));
        System.out.println("一个链接关闭！"+sender+", 当前在线会话为:" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(@PathParam("sender") String sender,
                          @PathParam("receiver") String receiver,
                          String message,
                          Session session) {
        System.out.println("来自客户端" + sender + "的消息:" + message);

        boolean isReceiverOnFace = webSocketServices.containsKey(makeKey(receiver, sender));
        System.out.println("boolean: " + isReceiverOnFace);
        // 如果在对方离线的时候发送消息
        if(!isReceiverOnFace) {
            System.out.println("is offline");
            redisTemplate.opsForList().rightPush(makeKey(sender, receiver), message);
        }
        else {
            messageRepository.save(new Message(
                    String.valueOf(idGenerator.snowflakeId()),
                    makeKey(sender, receiver),
                    sender,
                    Instant.now(),
                    message,
                    true
            ));
            try{
                sendMessage(message, sender, receiver);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 转发消息
    }

    @OnError
    public void onError(Session session, Throwable error) throws IOException {
        // sendInfo("响应超时");
    }

    /**
     * 发送消息
     * @param message 消息本体
     * @param sender 消息发送者
     * @param receiver 消息接收对象
     * */
    public void sendMessage(String message, String sender, String receiver) throws IOException {
        webSocketServices.get(makeKey(receiver, sender))
                .getBasicRemote().sendText("来自"+sender+"的信息:" + message);
    }

    /**
     * 群发消息
     * */
    public void sendInfo(String message) throws IOException {
        Collections.list(webSocketServices.keys()).forEach(item -> {
            String[] users = item.split("\\|");
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
