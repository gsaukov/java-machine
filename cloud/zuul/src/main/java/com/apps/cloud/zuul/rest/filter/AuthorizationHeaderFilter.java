package com.apps.cloud.zuul.rest.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.slf4j.LoggerFactory.getLogger;

public class AuthorizationHeaderFilter extends ZuulFilter {

    private static final Logger logger = getLogger(AuthorizationHeaderFilter.class);

    private static final Pattern UUID_PATTERN =
            compile("^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$");

    private static final String AUTHORIZATION_HEADER = "authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String BASIC_PREFIX = "Basic ";

    @Autowired
    private AuthorizationFilterSupport authorizationFilterSupport;

//    @Value("#{'${feign.oauth2.supported-apis}'.split(',')}")
//    private List<String> supportedApis;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        process(currentContext, request);

        return null; // Not used by the framework.
    }

    private void process(RequestContext currentContext, HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authorizationHeader != null) {
            process(currentContext, authorizationHeader);
        }
    }

    private void process(RequestContext currentContext, String authorizationHeader) {
        String uri = currentContext.getRequest().getRequestURI();
//        if (supportedApis.stream().noneMatch(uri::contains)) {
//            return;
//        }

        String lowerAuthorizationHeader = authorizationHeader.toLowerCase();
        if (lowerAuthorizationHeader.startsWith(BEARER_PREFIX.toLowerCase())) {
            processBearer(currentContext, authorizationHeader);
        } else if (lowerAuthorizationHeader.startsWith(BASIC_PREFIX.toLowerCase())) {
            processBasic(currentContext, authorizationHeader);
        }
    }

    private void processBasic(RequestContext currentContext, String authorizationHeader) {
        String basic = authorizationHeader.substring(BASIC_PREFIX.length()).trim();

        authorizationFilterSupport.getToken(basic).ifPresent(accessToken -> {
            logger.debug("Replacing basic auth {} with encoded jwt value.", basic);
            currentContext.addZuulRequestHeader(AUTHORIZATION_HEADER, BEARER_PREFIX.concat(accessToken));
        });
    }

    private void processBearer(RequestContext currentContext, String authorizationHeader) {
//        String token = authorizationHeader.substring(BEARER_PREFIX.length()).trim();
//
//        if (isUuid(token)) {
//            accessTokenRepository.findByJti(token).ifPresent(accessToken -> {
//                logger.debug("Replacing bearer token jti {} with encoded jwt value.", accessToken.getJti());
//                currentContext.addZuulRequestHeader(AUTHORIZATION_HEADER, BEARER_PREFIX.concat(accessToken.getEncoded()));
//            });
//        }
    }

    private boolean isUuid(String value) {
        return UUID_PATTERN.matcher(value).matches();
    }

}
