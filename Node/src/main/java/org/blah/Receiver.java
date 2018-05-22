package org.blah;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by ubuntu on 5/8/18.
 */

//@Component
public class Receiver {

    @JmsListener(destination = "broadcast", containerFactory = "jmsFactory")
    public void receiveMessage(Message message) {
        System.out.println(message);
    }

}
