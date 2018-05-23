package org.blah;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestMain {

    public static void main(String... args) {
        log.debug("test debug {}", "test");
    }
}
