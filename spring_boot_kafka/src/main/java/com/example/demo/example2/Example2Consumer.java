package com.example.demo.example2;

import com.example.demo.Conf;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
class Example2Consumer {

    @KafkaListener(topics = Conf.TOPIC_SECOND_NAME)
    public void consume(String msgStr) {
        System.out.println("start consume MSG: " + msgStr);
        for (int i = 0; i < 10; i++) {
            mySleep(1000);
            System.out.println("sec " + i);
        }
        System.out.println("finish");
    }

    private void mySleep(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
