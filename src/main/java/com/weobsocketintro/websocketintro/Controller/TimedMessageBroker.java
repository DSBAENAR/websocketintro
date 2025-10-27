package com.weobsocketintro.websocketintro.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class TimedMessageBroker {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    private static final Logger logger = Logger.getLogger(TimedMessageBroker.class.getName());

    @Scheduled(fixedRate = 5000)
    public void broadcast(){
        logger.log(Level.INFO, "broadcasting message");
        TimerEndpoint.send("The current time is " + formatter.format(new Date()));
    }
}
