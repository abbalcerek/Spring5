package org.blah.listener;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AllMessagesKafkaListener {

    @KafkaListener(topics = "test-topic", groupId = "kafkaServiceConsumerGroup")
    public void listen(String message) {
        System.out.println("Received Messasge in group foo: " + message);
    }

}
