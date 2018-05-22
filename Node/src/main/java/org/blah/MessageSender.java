package org.blah;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class MessageSender {

    final private RabbitTemplate template;

    final private Random random = new Random();

    final private Config config;

    final private ScheduledExecutorService executorService;

    @Autowired
    public MessageSender(RabbitTemplate template, Config config, ScheduledExecutorService executorService) {
        this.template = template;
        this.config = config;
        this.executorService = executorService;
    }

    public void sendMessage(Message message, String exchange, String key, Long delayInMillis) {
        sendMessage(message, exchange, key, delayInMillis, false);
    }

    public void sendMessage(Message message, String exchange, String key, Long delayInMillis, Boolean random) {
        executorService.schedule(() -> { template.convertAndSend(exchange, key, message); }, 15, TimeUnit.MILLISECONDS);
    }

    public void sendMessage(Message message, String exchange, String key) {
        sendMessage(message, exchange, key, 0L, false);
    }

    public void sendMessageWithRandDelay(Message message, String exchange, String key, Long delayInMillis) {
        sendMessage(message, exchange, key, Math.abs(random.nextLong()) % delayInMillis, true);
    }

    public void sendMessageWithRandDelay(Message message, String exchange, String key) {
        sendMessage(message, exchange, key, Math.abs(random.nextLong()) % config.getMessageDelay(), true);
    }

}
