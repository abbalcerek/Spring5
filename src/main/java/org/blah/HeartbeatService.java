package org.blah;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by ubuntu on 5/9/18.
 */

@Component
public class HeartbeatService {

    private final MessageSender sender;

    private ApplicationEphemeralState state;

    private final Config config;

    @Autowired
    public HeartbeatService(Config config, MessageSender sender, ApplicationEphemeralState state) {
        this.config = config;
        this.sender = sender;
        this.state = state;
    }

    @Scheduled(fixedRate = 3000)
    public void broadcastHeartbeat() {
        System.out.println("Sending heartbeat message");
        sender.sendMessageWithRandDelay(new Message(config.getId(), "heartbeat"), App.topic, "");
    }

    @Scheduled(fixedRate = 10000)
    public void pingUnresponsiveNodes() {
        System.out.println("pinging unresponsive nodes");
        state.getAllServers().keySet().forEach(s -> {
            System.out.println("Sending ping message: " + new Message(config.getId(), "ping") + " to: " + "rpc" + s);
            sender.sendMessage(new Message(config.getId(), "ping"), "rpc", "rpc" + s);
                });
    }

}
