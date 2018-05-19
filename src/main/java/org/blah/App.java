package org.blah;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.SocketUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.naming.Context;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


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
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, Config config) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
//        container.setMessageListener(listenerRabbit);
        return container;
    }

    @Bean
    public AnonymousQueue queue() {
        return new AnonymousQueue();
    }

    @Bean
    public FanoutExchange exchange() {
        return new FanoutExchange(topic);
    }

    @Bean("topicBinding")
    public Binding binding(FanoutExchange exchange) {
        return BindingBuilder.bind(queue()).to(exchange);
        //return BindingBuilder.bind(queue).to(exchange).with("#");
    }


    @Bean(name = "rpcQueue")
    public Queue rpcQueue(Config config) {
        return new Queue("rpc" + config.getId());
    }

    @Bean
    public DirectExchange rpcExchange() {
        return new DirectExchange("rpc");
    }

    @Bean(name = "rpcBinding")
    public Binding rpcBinding(Config config) {
        Queue q = rpcQueue(config);
//        System.out.println("Binding queue: " + q + " with name: " + q.getName() + " rpc: " + springExp("#rpcQueue.name"));
        return BindingBuilder.bind(rpcQueue(config)).to(rpcExchange()).withQueueName();
    }

    @Bean
    public ScheduledExecutorService scheduler() {
        return Executors.newScheduledThreadPool(4);
    }

    private String springExp(String expression) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(expression);
        return  (String) exp.getValue();
    }


    public static void main(String[] args) {

//        try {
//            boolean reachable = InetAddress.getByName(host).isReachable(1000);
//            if (!reachable) throw new RuntimeException("Can not connect to host : " + host);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

//        Properties props = new Properties();
//        props.put("server.port", SocketUtils.findAvailableTcpPort());
//        ConfigurableApplicationContext context = new SpringApplicationBuilder().sources(App.class).properties(props).run(args);

        System.out.println("Env: " + System.getenv());

        ApplicationContext context = SpringApplication.run(App.class, args);

        Config config = context.getBean(Config.class);
        System.out.println("Starting application with id: " + config.getId());

        MessageSender sender = context.getBean(MessageSender.class);

        sender.sendMessageWithRandDelay(new Message(config.getId(), "register"), topic, "key", config.getMessageDelay());

    }



}
