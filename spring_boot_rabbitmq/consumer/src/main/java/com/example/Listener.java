package com.example;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Listener {

    private final RabbitTemplate template;

    @RabbitListener(queues = Config.QUEUE_NAME)
    void initProcess(String msg) {
        System.out.println("init process consumer");
        System.out.println("msg = " + msg);

        //throw new AmqpRejectAndDontRequeueException("test exception");
        //throw new RuntimeException("test runtime exception");

        template.convertAndSend(Config.EXCHANGE_AUTH_PROCESS, "C1", "result for client C1");
        template.convertAndSend(Config.EXCHANGE_AUTH_PROCESS, "C2", "result for client C2");
    }
}
