package edu.polina.websocket;

import jakarta.servlet.annotation.WebServlet;
import org.eclipse.jetty.ee10.websocket.server.JettyWebSocketServlet;
import org.eclipse.jetty.ee10.websocket.server.JettyWebSocketServletFactory;

import java.time.Duration;


@WebServlet(name = "WebSocketServlet", urlPatterns = {"/chat"})
public class WebSocketChatServlet extends JettyWebSocketServlet {

     private final ChatService service;
     private final static int LOGOUT_TIME = 10 * 60 * 1000;

     public WebSocketChatServlet() {
         this.service = new ChatService();
     }

     @Override
     public void configure(JettyWebSocketServletFactory factory) {
         factory.setIdleTimeout(Duration.ofMillis(LOGOUT_TIME));
         factory.setCreator((req, resp) -> new WebSocketChat(service));

     }
}
