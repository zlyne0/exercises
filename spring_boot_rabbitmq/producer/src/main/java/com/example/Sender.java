package com.example;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Sender {

    private final RabbitTemplate template;
//    private final Queue queue;

    void send() {
        String message = "Hello World!";
        template.convertAndSend("q.auth-process", message);
        System.out.println(" [x] Sent '" + message + "'");
    }

    @RabbitListener(queues = "q.auth-process-results.client1")
    void resultsClient1(String msg) {
        System.out.println("[client1 listener got message: " + msg);
    }

    @RabbitListener(queues = "q.auth-process-results.client2")
    void resultsClient2(String msg) {
        System.out.println("[client2 listener got message: " + msg);
    }

}
