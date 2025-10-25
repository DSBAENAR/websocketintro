package com.weobsocketintro.websocketintro.Controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @GetMapping("/status")
    public String status(){
        return "{\"status\":\"Greetings from SpringBoot " 
        + LocalDateTime.now()
        .atZone(ZoneId.of("UTC-5"))
        .format(DateTimeFormatter.ofPattern("dd-MM-YYYY HH:mm")) 
        + " "
        + "The server is Running \"}";
    }
}