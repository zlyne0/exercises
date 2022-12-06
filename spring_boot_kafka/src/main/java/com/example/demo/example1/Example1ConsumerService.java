package com.example.demo.example1;

import com.example.demo.Conf;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
class Example1ConsumerService {

    @KafkaListener(topics = Conf.TOPIC_NAME)
    public void consume(String msgStr) {
        System.out.println("consume MSG: " + msgStr);
    }


}
