package com.apps.cloud.zuul.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.session.SessionManagementFilter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfigurer
        extends WebSecurityConfigurerAdapter {

    @Value("${security.oauth2.jwt.signing-key}")
    private String signingKey;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
            .anyRequest().authenticated()
            .and().addFilterBefore(appAuthenticationFilter(), SessionManagementFilter.class)
            .formLogin().loginPage("https://localhost:8002/oauth/authorize?response_type=code&client_id=sdapplication&scope=read")
            .and().httpBasic().disable();
    }

    @Bean
    public AppAuthenticationFilter appAuthenticationFilter() throws Exception {
        AppAuthenticationFilter appAuthenticationFilter = new AppAuthenticationFilter();
        appAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return appAuthenticationFilter;
    }

    @Bean
    @Override
    public OAuth2AuthenticationManager authenticationManagerBean() throws Exception {
        OAuth2AuthenticationManager manager = new OAuth2AuthenticationManager();
        manager.setTokenServices(defaultTokenServices());
        return manager;
    }

    @Bean
    @Primary
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signingKey);
        converter.setVerifierKey(signingKey);
        return converter;
    }

}
