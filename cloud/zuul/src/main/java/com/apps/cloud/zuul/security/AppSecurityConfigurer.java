package com.apps.cloud.zuul.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

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
//OAuth2Authentication
//AbstractAuthenticationToken
//
//SecurityContextHolderAwareRequestFilter
//UsernamePasswordAuthenticationFilter
//AbstractAuthenticationProcessingFilter
//OAuth2ClientAuthenticationProcessingFilter

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable().authorizeRequests()
                .anyRequest().authenticated().and().httpBasic().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
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
