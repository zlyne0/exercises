package com.example;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    public static final String QUEUE_NAME = "q.auth-process";
    public static final String EXCHANGE_AUTH_PROCESS = "exchange.auth-process-results";

    public static final String OUTPUT_QUEUE_NAME1 = "q.auth-process-results.client1";
    public static final String OUTPUT_QUEUE_NAME2 = "q.auth-process-results.client2";

    @Bean("initConsumerQueue")
    Queue initConsumerQueue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Bean("client1ConsumerQueue")
    Queue client1ConsumerQueue() {
        return new Queue(OUTPUT_QUEUE_NAME1, false);
    }

    @Bean("client2ConsumerQueue")
    Queue client2ConsumerQueue() {
        return new Queue(OUTPUT_QUEUE_NAME2, false);
    }

    @Bean("resultsExchange")
    DirectExchange resultsExchange() {
        return new DirectExchange(EXCHANGE_AUTH_PROCESS);
    }

    @Bean
    Binding bindingOutputClient1(Queue client1ConsumerQueue, DirectExchange resultsExchange) {
        return BindingBuilder.bind(client1ConsumerQueue).to(resultsExchange).with("C1");
    }

    @Bean
    Binding bindingOutputClient2(Queue client2ConsumerQueue, DirectExchange resultsExchange) {
        return BindingBuilder.bind(client2ConsumerQueue).to(resultsExchange).with("C2");
    }
}
