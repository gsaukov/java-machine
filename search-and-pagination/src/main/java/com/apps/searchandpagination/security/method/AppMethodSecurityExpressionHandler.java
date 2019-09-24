package com.apps.searchandpagination.security.method;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;

@Configuration
public class AppMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    @Autowired
    private AppJsonAuthorityService appJsonAuthorityService;

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
            Authentication authentication, MethodInvocation invocation) {
        AppMethodSecurityExpressionRoot root = new AppMethodSecurityExpressionRoot(authentication, appJsonAuthorityService);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(this.trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        return root;
    }

}
