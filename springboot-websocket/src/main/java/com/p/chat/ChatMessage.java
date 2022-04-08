package com.p.chat;

public class ChatMessage {
    private UserName from;
    private String body;

    public ChatMessage() {
    }

    public ChatMessage(UserName from, String body) {
        this.from = from;
        this.body = body;
    }

    public UserName getFrom() {
        return from;
    }

    public void setFrom(UserName from) {
        this.from = from;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
            "from=" + from +
            ", body='" + body + '\'' +
            '}';
    }
}
