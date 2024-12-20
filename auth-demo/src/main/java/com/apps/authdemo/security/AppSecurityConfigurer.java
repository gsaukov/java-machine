package com.apps.authdemo.security;

import com.apps.authdemo.security.filters.BasicAuthenticationFilterEnriched;
import com.apps.authdemo.security.filters.CsrfFilterEnriched;
import com.apps.authdemo.security.filters.UsernamePasswordAuthenticationFilterEnriched;
import com.corundumstudio.socketio.SocketIOServer;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SessionManagementFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class AppSecurityConfigurer
        extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SocketIOServer server;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().cors().and().authorizeRequests()
            .antMatchers("/static/**").permitAll()
            .antMatchers("/logedout").permitAll()
            .antMatchers("/authdemo/**").permitAll()
            .anyRequest().authenticated()
            .and()
                .addFilterBefore(usernamePasswordAuthenticationFilterEnriched(), SessionManagementFilter.class)
                .addFilterBefore(basicAuthenticationFilterEnriched(), UsernamePasswordAuthenticationFilterEnriched.class)
                .addFilterBefore(csrfFilterEnriched(), BasicAuthenticationFilterEnriched.class)
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
        return new BCryptPasswordEncoderEnriched(server);
    }

    @Bean
    public BasicAuthenticationFilterEnriched basicAuthenticationFilterEnriched() throws Exception {
        return new BasicAuthenticationFilterEnriched(authenticationManager(), server);
    }

    @Bean
    public UsernamePasswordAuthenticationFilterEnriched usernamePasswordAuthenticationFilterEnriched() throws Exception {
        UsernamePasswordAuthenticationFilterEnriched filter = new UsernamePasswordAuthenticationFilterEnriched(server);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                redirectStrategy.sendRedirect(request, response, "/login");
            }
        });
        return filter;
    }

    @Bean
    public CsrfFilterEnriched csrfFilterEnriched() throws Exception {
        return new CsrfFilterEnriched(server);
    }
}
