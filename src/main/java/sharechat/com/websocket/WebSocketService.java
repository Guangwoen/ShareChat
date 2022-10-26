package sharechat.com.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint(value="/websocket", configurator = EndpointConfigure.class)
public class WebSocketService {
    private static int onlineCount = 0;

    private static CopyOnWriteArraySet<WebSocketService> webSocketServices = new CopyOnWriteArraySet<WebSocketService>();

    private Session session;

    String str = "";

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        webSocketServices.add(this);
        addOnlineCount();

        try {
            sendMessage("服务端链接成功");
        }
        catch (IOException e) {
            System.out.println("IO异常");
        }
    }

    @OnClose
    public void onClose() {
        webSocketServices.remove(this);
        subOnlineCount();

        System.out.println("一个链接关闭！当前在线人数为:" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        str = message;
        // 向数据库保存信息

        System.out.println("onMessage sessionId is:" + session.getId());

        // 转发消息
    }

    @OnError
    public void onError(Session session, Throwable error) {
        for (WebSocketService item : webSocketServices) {
            try {
                item.sendMessage("响应超时!");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText("服务端信息:" + message);
    }

    public static void sendInfo(String message) throws IOException {
        for (WebSocketService item : webSocketServices) {
            try {
                item.sendMessage(message);
            }
            catch (IOException e) {
                continue;
            }
        }
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
