package com.p.chat;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
class MessageSender {

    private SimpMessagingTemplate simpMessagingTemplate;

    public MessageSender(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void send(ChatMessage message) {
        simpMessagingTemplate.convertAndSend("/topic/chat", message);
    }

    public void send2(ChatMessage message) {
        simpMessagingTemplate.convertAndSendToUser("user", "destination", message);
    }
}
