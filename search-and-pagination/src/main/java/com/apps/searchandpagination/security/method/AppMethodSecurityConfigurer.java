package com.apps.searchandpagination.security.method;

import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppMethodSecurityConfigurer extends GlobalMethodSecurityConfiguration {

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        AppMethodSecurityExpressionHandler handler = new AppMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(new AppPermissionEvaluator());
        return handler;
    }

}
