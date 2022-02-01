package com.p.chat;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class ChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

//    @Autowired
//    private Sender sender;
//
//    @Scheduled(initialDelay = 5000, fixedDelay = 5000)
//    public void xxx() {
//        System.out.println("send scheduler " + LocalDateTime.now());
//        sender.send();
//    }

}
