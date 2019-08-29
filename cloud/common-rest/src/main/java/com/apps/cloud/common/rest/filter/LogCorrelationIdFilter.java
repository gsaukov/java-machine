package com.apps.cloud.common.rest.filter;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogCorrelationIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
//        String logCorrelationId = request.getHeader(LOG_CORRELATION_ID);
//        MDC.put(LOG_CORRELATION_ID, useExistingOrCreateNew(logCorrelationId));

        filterChain.doFilter(request, response);
    }

}