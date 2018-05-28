package org.blah.listener;


import org.springframework.kafka.annotation.KafkaListener;

public class AllMessagesKafkaListener {

    @KafkaListener(topics = "test-topic", groupId = "kafkaServiceConsumerGroup")
    public void listen(String message) {
        System.out.println("Received Messasge in group foo: " + message);
    }

}
