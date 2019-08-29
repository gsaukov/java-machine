package com.apps.cloud.common.rest.filter;

import org.slf4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class LogAuditContextFilter extends OncePerRequestFilter {

//    private static final Logger logger = getLogger(AuditContext.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
//        AuditContext.remove();
//        try {
//            filterChain.doFilter(request, response);
//        } finally {
//            logAuditContext(request, response);
//            AuditContext.remove();
//        }
    }

    private void logAuditContext(HttpServletRequest request, HttpServletResponse response) {
//        AuditContext context = AuditContext.get();
//
//        logger.debug("Audit (web) - endpointUri {}, httpStatus {}, dbQueriesCount {}, dbTimeMillis {}",
//                request.getRequestURI(),
//                response.getStatus(),
//                context.getDbQueriesCount(),
//                context.getDbTimeMillis());
    }

}