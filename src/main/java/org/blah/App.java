package org.blah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.jms.ConnectionFactory;

/**
 * Created by ubuntu on 4/20/18.
 */
@EnableJms
@EnableScheduling
@SpringBootApplication
public class App {


    @Bean
    public JmsListenerContainerFactory<?> jmsFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // This provides all boot's default to this factory, including the message converter

        factory.setPubSubDomain(true);
        configurer.configure(factory, connectionFactory);
        // You could still override some of Boot's default if necessary.
        return factory;
    }

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }


    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);

        Config config = context.getBean(Config.class);
        System.out.println("Starting application with id: " + config.getId());

        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.convertAndSend("broadcast", new Message(config.getId().toString(), "register"));
    }



}
