package com.apps.searchandpagination.security.method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class AppPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private AppJsonAuthorityService appJsonAuthorityService;

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }
        final String targetType = targetDomainObject.getClass().getSimpleName().toLowerCase();
        return appJsonAuthorityService.hasPrivilege(auth, targetType, permission.toString().toLowerCase());
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        //targetId is not used but implementation could be expanded to include it, targetId represent usually method parameter.
        return appJsonAuthorityService.hasPrivilege(auth, targetType.toLowerCase(), permission.toString().toLowerCase());
    }

}
