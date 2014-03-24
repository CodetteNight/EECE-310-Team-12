package org.jpacman.framework.social;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.EnableInMemoryConnectionRepository;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.twitter.config.annotation.EnableTwitter;

/**
 * Spring application to obtain tweets "#springframework".
 * From http://spring.io/guides/gs/accessing-twitter/
 */
@EnableTwitter(appId="qaSMjjxc4sEiD07Fxj8PBQ", appSecret="iAPROs4PbCzyq5OXwh8m53Lej9h7AW0uj7hLgClLb4")
@EnableInMemoryConnectionRepository
public class TwitterConfig {

    @Bean
    public UserIdSource userIdSource() {
        return new UserIdSource() {         
            @Override
            public String getUserId() {
                return "testuser";
            }
        };
    }

    @Bean
    public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
        return new ConnectController(connectionFactoryLocator, connectionRepository);
    }
    
}