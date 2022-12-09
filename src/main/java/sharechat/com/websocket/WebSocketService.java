package sharechat.com.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value="/websocket/{sender}/2/{receiver}", configurator = EndpointConfigure.class)
public class WebSocketService {
    private static int onlineCount = 0;

    private static final ConcurrentHashMap<String, Session> webSocketServices
            = new ConcurrentHashMap<>();

    String str = "";

    @OnOpen
    public void onOpen(@PathParam("sender") String sender,
                       @PathParam("receiver") String receiver,
                       Session session,
                       EndpointConfig config) {
        webSocketServices.put(sender+'|'+receiver, session);
        addOnlineCount();
        System.out.println("新连接: " + sender + ", 当前在线人数为:" + getOnlineCount());
    }

    @OnClose
    public void onClose(@PathParam("sender") String sender,
                        @PathParam("receiver") String receiver) {
        webSocketServices.remove(sender+'|'+receiver);
        subOnlineCount();
        System.out.println("一个链接关闭！"+sender+", 当前在线人数为:" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(@PathParam("sender") String sender,
                          @PathParam("receiver") String receiver,
                          String message,
                          Session session) {
        System.out.println("来自客户端" + sender + "的消息:" + message);

        // 数据库查找
        // 向数据库保存信息



        try{
            sendMessage(message, sender, receiver);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 转发消息
    }

    @OnError
    public void onError(Session session, Throwable error) throws IOException {
        sendInfo("响应超时");
    }

    /**
     * 发送消息
     * @param message 消息本体
     * @param sender 消息发送者
     * @param receiver 消息接收对象
     * */
    public void sendMessage(String message, String sender, String receiver) throws IOException {
        webSocketServices.get(receiver+'|'+sender)
                .getBasicRemote().sendText("来自"+sender+"的信息:" + message);
    }

    /**
     * 群发消息
     * */
    public void sendInfo(String message) throws IOException {
        Collections.list(webSocketServices.keys()).forEach( item -> {
            String[] users = item.split("\\|");
            try {
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
