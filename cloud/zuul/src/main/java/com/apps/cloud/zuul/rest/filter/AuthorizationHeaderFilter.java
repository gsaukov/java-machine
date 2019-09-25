package com.apps.cloud.zuul.rest.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.server.Session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.slf4j.LoggerFactory.getLogger;

public class AuthorizationHeaderFilter extends ZuulFilter {

    private static final Logger logger = getLogger(AuthorizationHeaderFilter.class);

    private static final String COOKIE = "cookie";

    @Autowired
    private AuthorizationFilterSupport authorizationFilterSupport;

    @Override
    public String filterType() {
        return "route";
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
        if(isCookiesSet(currentContext)){
            return null;
        }
        process(currentContext, request);
        return null; // Not used by the framework.
    }

    private boolean isCookiesSet(RequestContext currentContext) {
        return currentContext.getZuulRequestHeaders().containsKey(COOKIE);
    }

    private void process(RequestContext currentContext, HttpServletRequest request) {
        Cookie sessionCookie = getSessionCookie(request);
        if(sessionCookie != null){
            currentContext.addZuulRequestHeader(COOKIE, sessionCookie.getName() + "=" + sessionCookie.getValue());
        }
    }

    private Cookie getSessionCookie(HttpServletRequest request){
        Cookie sessionCookie = null;
        for(Cookie cookie : request.getCookies()){
            if(cookie.getName().equals("SESSION")){
                sessionCookie = cookie;
            }
        }
        return sessionCookie;
    }

}
