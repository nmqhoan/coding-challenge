package com.genesys.challenge.client.socket;

import com.genesys.challenge.client.configuration.ServerConfiguration;
import com.genesys.challenge.client.ui.GamePanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.websocket.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

/**
 * WebSocketClient.
 *
 */
@Configuration
@PropertySource("file:application.properties")
@ClientEndpoint
public class SocketClient {
    private final Logger log = LoggerFactory.getLogger(SocketClient.class);

    private String socketServierURI;
    private Session session;
    private GamePanel gameJpanel;
    private WebSocketContainer container;


    public SocketClient(GamePanel gameJpanel){
        socketServierURI="ws://" + ServerConfiguration.prop.getProperty("server.ip") + ":"
                        + ServerConfiguration.prop.getProperty("server.port")
                        +"/"
                        + ServerConfiguration.prop.getProperty("socket.channel");
        this.gameJpanel = gameJpanel;
        try{
            container= ContainerProvider.
                    getWebSocketContainer();
            container.connectToServer(this, new URI(socketServierURI));

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    private void openConnection (){
        try{
            container.connectToServer(this, new URI(socketServierURI));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session){
        log.info("socket connect");
        this.session=session;
    }

    @OnClose
    public void onClose(Session session){
        log.info("socket closes");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.debug("Receive message: "+message);
        gameJpanel.onMessage(message);
    }

    public void sendMessage(String message) {
        try {
            if(!session.isOpen())
                openConnection();
            log.debug("Send message: " + message);
            session.getBasicRemote().sendText(message);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
