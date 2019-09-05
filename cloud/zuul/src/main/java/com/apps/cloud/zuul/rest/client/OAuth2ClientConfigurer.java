package com.apps.cloud.zuul.rest.client;

import feign.auth.BasicAuthRequestInterceptor;
import feign.form.FormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuth2ClientConfigurer {

    @Value("${feign.oauth2.client-id}")
    private String clientId;

    @Value("${feign.oauth2.client-secret}")
    private String clientSecret;

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public FormEncoder feignFormEncoder() {
        return new FormEncoder(new SpringEncoder(this.messageConverters));
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(clientId, clientSecret);
    }

}
