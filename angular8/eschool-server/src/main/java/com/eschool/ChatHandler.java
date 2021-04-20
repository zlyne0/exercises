package com.eschool;

import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ChatHandler extends TextWebSocketHandler {

    private MsgSender msgSender;
    
    public ChatHandler(MsgSender msgSender) {
        this.msgSender = msgSender;
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println("payload " + message.getPayload());
        msgSender.echo("" + message.getPayload());
    }
    
}
