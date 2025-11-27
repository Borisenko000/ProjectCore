package edu.polina.websocket;

import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatService {
    public Set<WebSocketChat> webSockets = Collections.newSetFromMap(new ConcurrentHashMap<>());

     public void addSocket(WebSocketChat socket) {
         webSockets.add(socket);
     }

     public void sendMessage(String data) {
         for (WebSocketChat u : webSockets) {
             u.sendString(data);
         }
     }

     public void remove(WebSocketChat socket) {
         webSockets.remove(socket);
     }
}
