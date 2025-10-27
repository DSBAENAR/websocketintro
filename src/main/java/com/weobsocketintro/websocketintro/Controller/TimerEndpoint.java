package com.weobsocketintro.websocketintro.Controller;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
@Component
@ServerEndpoint("/timer")
public class TimerEndpoint {
    private static final Logger logger = Logger.getLogger("ETFEndpoint");
    
    static Queue<Session> queue = new ConcurrentLinkedQueue<>();

    public static void send (String msg){
        try {
            for (Session s: queue){
                s.getBasicRemote().sendText(msg);
                logger.log(Level.INFO, "Sent: {0}",msg);
            }
        } catch (IOException e) {
            logger.log(Level.INFO ,e.toString());
        }
    }

    @OnOpen
        public void openConnection(Session session) {
        queue.add(session);
        logger.log(Level.INFO, "Connection opened.");
        try {
        session.getBasicRemote().sendText("Connection established.");
        } catch (IOException ex) {
        Logger.getLogger(TimerEndpoint.class.getName()).log(Level.SEVERE,
        null, ex);
        }
    }

    @OnClose
    public void closedConnection(Session session) {
        queue.remove(session);
        logger.log(Level.INFO, "Connection closed.");
    }

    @OnError
    public void error(Session session, Throwable t) {
        /* Remove this connection from the queue */
        queue.remove(session);
        logger.log(Level.INFO, t.toString());
        logger.log(Level.INFO, "Connection error.");
    }

    
}
