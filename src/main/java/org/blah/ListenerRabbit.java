package org.blah;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.xml.bind.SchemaOutputResolver;

/**
 * Created by ubuntu on 5/10/18.
 */
@Slf4j
@Component
public class ListenerRabbit {

    private final Config config;


    final private ApplicationEphemeralState state;

    final private MessageSender sender;

    @Autowired
    public ListenerRabbit(ApplicationEphemeralState state, MessageSender sender, Config config) {
        this.state = state;
        this.sender = sender;
        this.config = config;
    }

    @RabbitListener(queues = "#{queue.name}")
    public void receiveMessage(Message message) {
        state.updateLastServerMessageTime(message);
        log.debug("topic listner: {}", message);
    }

    @RabbitListener(queues = "#{rpcQueue.name}")
    public void receiveRpcMessage(Message message) {
        log.debug("rpc listener: {}", message);
    }

}

