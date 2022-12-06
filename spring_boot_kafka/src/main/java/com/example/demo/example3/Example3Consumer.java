package com.example.demo.example3;

import com.example.demo.Conf;
import kafka.example3.avro.UserEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
class Example3Consumer {

    @KafkaListener(topics = Conf.TOPIC_AVRO_EXAMPLE)
    public void consume(ConsumerRecord<String, UserEvent> record) {
        UserEvent userEvent = record.value();
        System.out.println("start consume MSG: " + userEvent);
    }
}
