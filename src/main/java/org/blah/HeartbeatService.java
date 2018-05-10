package org.blah;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by ubuntu on 5/9/18.
 */

@Component
public class HeartbeatService {

    @Autowired
    JmsTemplate template;

    @Autowired
    Config config;

    @Scheduled(fixedRate = 10000)
    public void broadcastHeartbeat() {
        System.out.println("dupa");
        template.setPubSubDomain(true);
        template.convertAndSend("broadcast", new Message(config.getId().toString(), "heartbeat"));
    }

}
