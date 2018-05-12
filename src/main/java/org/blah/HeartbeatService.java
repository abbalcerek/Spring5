package org.blah;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by ubuntu on 5/9/18.
 */

@Component
public class HeartbeatService {

    @Autowired
    RabbitTemplate template;

    @Autowired
    Config config;

    @Scheduled(fixedRate = 10000)
    public void broadcastHeartbeat() {
        System.out.println("Sending heartbeat message");
        template.convertAndSend(App.topic, "", new Message(config.getId().toString(), "heartbeat"));
    }

}
