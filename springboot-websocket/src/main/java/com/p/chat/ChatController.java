package com.p.chat;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final MessageSender messageSender;
    private final SimpUserRegistry simpUserRegistry;


    public ChatController(MessageSender messageSender, SimpUserRegistry simpUserRegistry) {
        this.messageSender = messageSender;
        this.simpUserRegistry = simpUserRegistry;
    }

    @MessageMapping("/chat")
    public void receiveMessage(
        @Payload ChatMessage message,
        Principal user,
        @Header("simpSessionId") String sessionId
    ) {
        System.out.println("XXXX request headers");
        System.out.println("user: " + user);
        System.out.println("sessionId: " + sessionId);
        System.out.println("receive msg: " + message);

        System.out.println("XXXX sim user registry");
        System.out.println("user count " + simpUserRegistry.getUserCount());
        for (SimpUser simpUserRegistryUser : simpUserRegistry.getUsers()) {
            System.out.println("name: " + simpUserRegistryUser.getName() + ", " + simpUserRegistryUser.getPrincipal() + ", sessions: " + simpUserRegistryUser.getSessions());
        }

        messageSender.send(message);
    }

}
