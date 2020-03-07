package com.apps.cloud.zuul.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.session.SessionManagementFilter;

import java.security.KeyStore;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity

//Could be rewritten with https://www.baeldung.com/spring-security-oauth2-enable-resource-server-vs-enable-oauth2-sso
public class AppSecurityConfigurer
        extends WebSecurityConfigurerAdapter {

    @Value("${login-page-url}")
    private String  loginPageUrl;

    @Value("${jwt.certificate.store.trust-store}")
    private Resource truststore;

    @Value("${jwt.certificate.store.trust-store-password}")
    private String truststorePassword;

    @Value("${jwt.certificate.key.key-alias}")
    private String keyAlias;

    @Value("${jwt.certificate.key.key-password}")
    private String keyPassword;

//    RedisOperationsSessionRepository
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
            .antMatchers("/logedout").permitAll()
            .anyRequest().authenticated()
            .and().addFilterBefore(appAuthenticationFilter(), SessionManagementFilter.class)
            .formLogin().loginPage(loginPageUrl)
            .and().logout()
            .logoutUrl("/performlogout")
            .logoutSuccessUrl("/logedout")
            .invalidateHttpSession(true)
            .deleteCookies("SESSION")
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
    public DefaultTokenServices defaultTokenServices() throws Exception {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    @Bean
    public JwtTokenStore tokenStore() throws Exception {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() throws Exception {
        KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());
        store.load(truststore.getInputStream(), truststorePassword.toCharArray());
        PublicKey publicKey = store.getCertificate(keyAlias).getPublicKey();

        DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
        tokenConverter.setUserTokenConverter(new AppUserAuthenticationConverter());

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setAccessTokenConverter(tokenConverter);
        converter.setVerifier(new RsaVerifier((RSAPublicKey)publicKey));
        return converter;
    }

}
