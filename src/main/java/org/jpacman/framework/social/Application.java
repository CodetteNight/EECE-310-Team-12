package org.jpacman.framework.social;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Based on Spring Social guide
 * http://spring.io/guides/gs
 *
 */
@Configuration
@EnableAutoConfiguration
@Import(TwitterConfig.class)
@ComponentScan
public class Application {

    /*
     * SPRING BOOTSTRAP MAIN
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

} 