package edu.polina.websocket;


import org.eclipse.jetty.websocket.api.Callback;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketOpen;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class WebSocketChat {
    public ChatService service;
    private Session session;

    public WebSocketChat(ChatService service) {
        this.service = service;
    }

    @OnWebSocketOpen
    public void onOpen(Session session) {
        service.addSocket(this);
        this.session = session;
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        service.sendMessage(data);
    }

    @OnWebSocketClose
    public void onClose() {
        service.remove(this);
    }

    public void sendString(String data) {
        session.sendText(data, new Callback() {
            @Override
            public void succeed() {
                Callback.super.succeed();
            }
            @Override
            public void fail(Throwable e) {
                System.out.println("Ошибка отправки сообщения" + e.getMessage());
            }
        });
    }
}
