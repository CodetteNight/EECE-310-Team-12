package org.jpacman.framework.social;

import org.springframework.context.annotation.Bean;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.EnableInMemoryConnectionRepository;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.twitter.config.annotation.EnableTwitter;

/**
 * Based on Spring Social guide
 * http://spring.io/guides/gs
 *
 */

/** TODO: Replace appID and appSecret with a valid one.
 * @author vwjf */
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