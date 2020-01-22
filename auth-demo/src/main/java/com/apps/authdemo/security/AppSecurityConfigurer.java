package com.apps.authdemo.security;

import com.apps.authdemo.security.filters.BasicAuthenticationFilterEnriched;
import com.apps.authdemo.security.filters.UsernamePasswordAuthenticationFilterEnriched;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SessionManagementFilter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfigurer
        extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override

    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
        http.cors().and().csrf().disable().authorizeRequests()
            .antMatchers("/static/**").permitAll()
            .antMatchers("/logedout").permitAll()
            .antMatchers("/partone").permitAll()
            .anyRequest().authenticated()
            .and()
                .addFilterBefore(usernamePasswordAuthenticationFilterEnriched(), SessionManagementFilter.class)
                .addFilterBefore(basicAuthenticationFilterEnriched(), UsernamePasswordAuthenticationFilterEnriched.class)
            .logout()
            .logoutUrl("/performlogout")
            .logoutSuccessUrl("/logedout")
            .invalidateHttpSession(true)
            .deleteCookies("SESSION")
            .and().httpBasic().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public BasicAuthenticationFilterEnriched basicAuthenticationFilterEnriched() throws Exception {
        return new BasicAuthenticationFilterEnriched(authenticationManager());
    }

    @Bean
    public UsernamePasswordAuthenticationFilterEnriched usernamePasswordAuthenticationFilterEnriched() throws Exception {
        UsernamePasswordAuthenticationFilterEnriched filter = new UsernamePasswordAuthenticationFilterEnriched();
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }
}
