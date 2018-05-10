package org.blah;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Created by ubuntu on 5/9/18.
 */

@Component
public class Config {

    private Integer id = new Random().nextInt();

    public Integer getId() {
        return id;
    }
}
