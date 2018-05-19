package org.blah;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.Random;

/**
 * Created by ubuntu on 5/9/18.
 */

@Component
public class Config {

//    private final Environment environment;

//    @PostConstruct
//    private void init() {
//      id = environment.getProperty("HOSTNAME");
//    }

    @Value("#{environment.HOSTNAME}")
    private String id;// = InetAddress.getLoopbackAddress().getHostName();

    @Value("${maxDelay}")
    private long messageDelay;

    @Value("${spring.rabbitmq.host}")
    public String host;

//    @Autowired
//    public Config(Environment environment) {
//        this.environment = environment;
//    }

    public String getId() {
        return id;
    }

    public long getMessageDelay() {
        return messageDelay;
    }

    public void setMessageDelay(long messageDelay) {
        this.messageDelay = messageDelay;
    }

    public String getHost() {
        return host;
    }
}
