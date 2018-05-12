package org.blah;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ubuntu on 5/10/18.
 */
@Component
public class ListenerRabbit {

//    @Autowired
//    Config config;

//    final static String queue = "queue" + config.getId();

    @RabbitListener(queues = "#{queue.name}")
    public void receiveMessage(Message content) {
        System.out.println(content);
    }

//    @Override
//    public void onMessage(org.springframework.amqp.core.Message message, Channel channel) throws Exception {
//        System.out.println(message);
//    }
}

