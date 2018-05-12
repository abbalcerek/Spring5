package org.blah;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.SocketUtils;

import javax.naming.Context;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Properties;


/**
 * Created by ubuntu on 4/20/18.
 */
//@EnableJms
@EnableRabbit
@EnableScheduling
@SpringBootApplication
public class App {

    public static String queue = "queue";
    public static String topic = "jms-topic";
    public static String host = "rabbitmq";

//    @Bean
//    public JmsListenerContainerFactory<?> jmsFactory(ConnectionFactory connectionFactory,
//                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        // This provides all boot's default to this factory, including the message converter
//
//        factory.setPubSubDomain(true);
//        configurer.configure(factory, connectionFactory);
//        // You could still override some of Boot's default if necessary.
//        return factory;
//    }

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter converter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        return converter;
    }

//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory =
//                new CachingConnectionFactory(host);
//        connectionFactory.setUsername("guest");
//        connectionFactory.setPassword("guest");
//        return connectionFactory;
//    }

    @Bean
    public SimpleRabbitListenerContainerFactory myFactory(ConnectionFactory connectionFactory, SimpleRabbitListenerContainerFactoryConfigurer configurer) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        factory.setMessageConverter(converter());
        return factory;
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, Config config, ListenerRabbit listenerRabbit) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queue + config.getId());
        container.setMessageListener(listenerRabbit);
        return container;
    }

    @Bean
    public Queue queue(Config config) {
        return new Queue(queue + config.getId(), false);
    }

    @Bean
    public Exchange exchange() {
        return new FanoutExchange(topic);
    }

    @Bean
    public Binding binding(Queue queue, FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
        //return BindingBuilder.bind(queue).to(exchange).with("#");
    }


    public static void main(String[] args) {

        try {
            boolean reachable = InetAddress.getByName(host).isReachable(1000);
            if (!reachable) throw new RuntimeException("Can not connect to host : " + host);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Properties props = new Properties();
        props.put("server.port", SocketUtils.findAvailableTcpPort());
        ConfigurableApplicationContext context = new SpringApplicationBuilder().sources(App.class).properties(props).run(args);

//        ApplicationContext context = SpringApplication.run(App.class, args);

        Config config = context.getBean(Config.class);
        System.out.println("Starting application with id: " + config.getId());

        RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);

        rabbitTemplate.convertAndSend(topic, "key", new Message(config.getId().toString(), "register"));

//        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
//
//        jmsTemplate.setPubSubDomain(true);
//        jmsTemplate.convertAndSend("broadcast", new Message(config.getId().toString(), "register"));
    }



}
