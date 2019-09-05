package com.apps.cloud.zuul.rest.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZuulFilterFactory {

    @Bean
    public AuthorizationHeaderFilter bearerHeaderFilter() {
        return new AuthorizationHeaderFilter();
    }

}
