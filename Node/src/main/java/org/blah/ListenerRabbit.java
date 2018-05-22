package org.blah;

import com.rabbitmq.client.Channel;
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
@Component
public class ListenerRabbit {

    private final Config config;

//    final static String queue = "queue" + config.getId();

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
        System.out.println("topic listner : " + message);
//        System.out.println(state.messageCash);
    }

    @RabbitListener(queues = "#{rpcQueue.name}")
    public void receiveRpcMessage(Message message) {
        System.out.println("rpc listener : " + message);

        if (message.getPayload().equals("ping")) {
            System.out.println("sending pong message : " + new Message(config.getId(), "pong") + " to : " + message.getSenderAddress());
            sender.sendMessage(new Message(config.getId(), "pong"), "rpc", "rpc" + message.getSenderAddress());
        }
    }

//    @Override
//    public void onMessage(org.springframework.amqp.core.Message message, Channel channel) throws Exception {
//        System.out.println(message);
//    }
}

