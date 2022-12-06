package com.example.demo.example3;

import com.example.demo.Conf;
import kafka.example3.avro.UserEvent;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class Example3ProducerService {


    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public void produce() {
        UserEvent userEvent = new UserEvent();
        userEvent.setAge(17);
        userEvent.setFirstname("Siri");
        userEvent.setLastname("Ketton");
        System.out.println("produce message: " + userEvent);
        kafkaTemplate.send(Conf.TOPIC_AVRO_EXAMPLE, userEvent);
    }

}

