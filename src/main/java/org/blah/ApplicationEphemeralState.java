package org.blah;

import org.blah.dto.Node;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

@Component
public class ApplicationEphemeralState {

    ConcurrentMap<String, Node> messageCash = new ConcurrentHashMap<>();

    public Node updateLastServerMessageTime(Message message) {
        assert messageCash.size() < 20;
        return messageCash.put(
                message.getSenderAddress(),
                new Node(message.getSenderAddress(), DateTime.now(), true)
        );
    }

    public Map<String, Node> getAllServers() {
        return new HashMap<>(messageCash);
    }


}
