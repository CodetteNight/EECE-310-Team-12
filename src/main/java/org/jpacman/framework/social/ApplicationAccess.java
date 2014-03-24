package org.jpacman.framework.social;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import(TwitterConfig.class)
@ComponentScan


/**
 * Spring application to obtain tweets "#springframework".
 * From http://spring.io/guides/gs/accessing-twitter/
 */
public class ApplicationAccess {

    /*
     * SPRING BOOTSTRAP MAIN
     */
    public static void main(String[] args) {
    	bootstrap(args);
    	
    }
    
    public static void bootstrap(String[] args){
        SpringApplication.run(ApplicationAccess.class, args);
    }

} 