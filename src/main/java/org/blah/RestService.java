package org.blah;

import org.blah.dto.Node;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * Created by ubuntu on 5/9/18.
 */
@RestController
public class RestService {

    private final Config config;

    private final ApplicationEphemeralState state;

    @Autowired
    public RestService(ApplicationEphemeralState state, Config config) {
        this.state = state;
        this.config = config;
    }

    @RequestMapping("/")
    public String hello() {
        return "Server id: " + config.getId();
    }

    @RequestMapping("/servers")
    public Map<String, Node> servers() {
        return state.getAllServers();
    }

}
