package com.interzonedev.springbootdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the application from the command line via the {@link #main(String[])} method.
 */
@SpringBootApplication()
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    /**
     * Entry point of the application from the command line.
     *
     * @param args An array of arguments passed in from the command line.
     */
    public static void main(String[] args) {
        log.debug("main: Start");
        SpringApplication.run(Application.class, args);
        log.debug("main: End");
    }

}
