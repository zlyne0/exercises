package com.example.demo.example1;

import com.example.demo.Conf;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
class Example1ProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void produce() {
        String str = "Hello " + LocalDateTime.now();
        System.out.println("produce message: " + str);
        kafkaTemplate.send(Conf.TOPIC_NAME, str);
    }

}

