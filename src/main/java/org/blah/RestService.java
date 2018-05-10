package org.blah;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * Created by ubuntu on 5/9/18.
 */
@RestController
public class RestService {

    @Autowired
    private Config config;

    @RequestMapping("/")
    public String hello() {
        return "Hello world " + config.getId();
    }

}
