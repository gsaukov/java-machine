package com.apps.cloud.common.rest.filter;

import org.slf4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static java.lang.System.lineSeparator;
import static java.util.Collections.list;
import static org.slf4j.LoggerFactory.getLogger;

public class RequestAndResponseFilter extends OncePerRequestFilter {

    private static final Logger logger = getLogger(RequestAndResponseFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        logRequestHeaders(request);

        filterChain.doFilter(requestWrapper, responseWrapper);

        logRequestAndResponse(request, response, requestWrapper, responseWrapper);

        responseWrapper.copyBodyToResponse();
    }

    private void logRequestHeaders(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();

        builder.append("Incoming Request Headers - ")
                .append(request.getMethod()).append(" ")
                .append(request.getRequestURL().toString())
                .append(lineSeparator());

        logRequestHeaders(request, builder);

        logger.debug(builder.toString());
    }

    private void logRequestHeaders(HttpServletRequest request, StringBuilder builder) {
        list(request.getHeaderNames()).forEach(header -> logHeader(header, request, builder));
    }

    private void logHeader(String headerName, HttpServletRequest request, StringBuilder builder) {
        builder.append(headerName).append(" = ").append(request.getHeader(headerName)).append(lineSeparator());
    }

    private void logRequestAndResponse(HttpServletRequest request, HttpServletResponse response,
                                       ContentCachingRequestWrapper requestWrapper,
                                       ContentCachingResponseWrapper responseWrapper) {
        StringBuilder builder = new StringBuilder();

        builder.append("Incoming Request")
                .append(lineSeparator());

        logContent(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding(), builder);

        builder.append("Outgoing Response - ")
                .append(response.getStatus())
                .append(lineSeparator());

        logResponseHeaders(response, builder);
        logContent(responseWrapper.getContentAsByteArray(), response.getCharacterEncoding(), builder);

        logger.debug(builder.toString());
    }

    private void logResponseHeaders(HttpServletResponse response, StringBuilder builder) {
        response.getHeaderNames().forEach(header -> logHeader(header, response, builder));
    }

    private void logHeader(String headerName, HttpServletResponse response, StringBuilder builder) {
        builder.append(headerName).append(" = ").append(response.getHeader(headerName)).append(lineSeparator());
    }

    private void logContent(byte[] content, String encoding, StringBuilder builder) {
        String requestBody = getContentAsString(content, encoding);

        if (!requestBody.isEmpty()) {
            builder.append(requestBody)
                    .append(lineSeparator())
                    .append(lineSeparator());
        }
    }

    private String getContentAsString(byte[] buffer, String encoding) {
        if (buffer == null || buffer.length == 0) {
            return "";
        }

        if (encoding == null) {
            return "Unspecified Encoding";
        }

        try {
            return new String(buffer, 0, buffer.length, encoding);
        } catch (final UnsupportedEncodingException exc) {
            return "Unsupported Encoding";
        }
    }

}
