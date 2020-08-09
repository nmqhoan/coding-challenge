package com.genesys.challenge.FiveInRow.configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genesys.challenge.FiveInRow.domain.Message;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SocketHandler extends AbstractWebSocketHandler {
    private final Logger log = LoggerFactory.getLogger(SocketHandler.class);
    List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws InterruptedException, IOException {

        for(WebSocketSession webSocketSession : sessions) {
//            if(webSocketSession.equals(session))
//                continue;
            log.info("Receive " + message.getPayload());
            Message mss = new ObjectMapper().readValue(message.getPayload(),Message.class);
//            log.info(mss.toString());
//            Map value = new Gson().fromJson(message.getPayload(), Map.class);
//            log.info(String.valueOf(value));
            if(webSocketSession.isOpen())
                webSocketSession.sendMessage(message);
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws IOException {
        System.out.println("New Binary Message Received");
        session.sendMessage(message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //the messages will be broadcasted to all users.
        sessions.add(session);
    }
}
