package com.apps.searchandpagination.security.method;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class AppMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private AppAuthorityService appAuthorityService;

    public AppMethodSecurityExpressionRoot(Authentication authentication, AppAuthorityService appAuthorityService) {
        super(authentication);
        this.appAuthorityService = appAuthorityService;
    }

    public boolean hasDomain(String domain) {
        return appAuthorityService.getAvailableDomains(getAuthentication()).contains(domain);
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }

    @Override
    public void setFilterObject(Object obj) {
        this.filterObject = obj;
    }

    @Override
    public void setReturnObject(Object obj) {
        this.returnObject = obj;
    }

}
