package com.eschool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MsgSender {
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;    
    
    @SendTo("/topic/echo")
    public String echo(String msg) {
        simpMessagingTemplate.convertAndSend("/topic", "echo " + msg);
        return "echo " + msg;
    }
}
