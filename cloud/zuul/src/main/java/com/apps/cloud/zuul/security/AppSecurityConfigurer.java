package com.apps.cloud.zuul.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import redis.clients.jedis.Jedis;

@Configuration
@EnableWebSecurity
public class AppSecurityConfigurer
        extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Autowired
//    private AppAuthenticationSuccessHandler appAuthenticationSuccessHandler;

//{WebAsyncManagerIntegrationFilter@12705}
//{SecurityContextPersistenceFilter@12704}
//{HeaderWriterFilter@12703}
//{CorsFilter@12702}
//{LogoutFilter@12701}
//{RequestCacheAwareFilter@12700}
//{SecurityContextHolderAwareRequestFilter@12699}
//{AnonymousAuthenticationFilter@12698}
//{SessionManagementFilter@12697}
//{ExceptionTranslationFilter@12695}
//{FilterSecurityInterceptor@13093}
//
//https://spring.io/guides/tutorials/spring-boot-oauth2/
//https://shout.setfive.com/2015/11/02/spring-boot-authentication-with-custom-http-header/
//OAuth2Authentication
//AbstractAuthenticationToken
//
//SecurityContextHolderAwareRequestFilter s;
//UsernamePasswordAuthenticationFilter u;
//AbstractAuthenticationProcessingFilter a;
//OAuth2ClientAuthenticationProcessingFilter o;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
            .anyRequest().authenticated()
            .and().addFilterBefore(appAuthenticationFilter(), SessionManagementFilter.class)
            .formLogin().loginPage("https://localhost:8002/oauth/authorize?response_type=code&client_id=sdapplication&scope=read")
            .and().httpBasic().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AppAuthenticationFilter appAuthenticationFilter() throws Exception {
        AppAuthenticationFilter appAuthenticationFilter = new AppAuthenticationFilter();
        appAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return appAuthenticationFilter;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AppAuthenticationProvider();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
