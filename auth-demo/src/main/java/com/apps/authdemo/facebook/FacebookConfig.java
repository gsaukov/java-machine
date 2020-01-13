package com.apps.authdemo.facebook;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

@Configuration
public class FacebookConfig implements SocialConfigurer {

    @Bean
    public FacebookConnectionFactory facebookConnectionFactory() {
        return new FacebookConnectionFactory("2930956966914426", "6da884e06396dd927d6f6b6d78fd5dac");
    }

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
        cfConfig.addConnectionFactory(facebookConnectionFactory());
    }

    @Override
    public UserIdSource getUserIdSource() {
        return () -> "anonymous";
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return null;
    }

}