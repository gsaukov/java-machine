package com.apps.cloud.zuul.security;

//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AppAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String xAuth = request.getHeader("X-Authorization");


//        // The token is 'valid' so magically get a user id from it
//        Long id = getUserIdFromToken(xAuth);
//
//        // Create our Authentication and let Spring know about it
//        Authentication auth = new DemoAuthenticationToken(id);
//        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

}

