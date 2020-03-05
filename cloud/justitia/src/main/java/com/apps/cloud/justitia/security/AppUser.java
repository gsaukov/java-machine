package com.apps.cloud.justitia.security;

import com.apps.cloud.justitia.data.entity.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class AppUser extends User {

    private final List<String> domains;

    private final Map<String, String> rights;

    public AppUser(String username, String password, List<Authority> authorities, List<String> domains, Map<String, String> rights) {
        super(username, password, toGrantedAuthorities(authorities));
        this.domains = domains;
        this.rights = rights;
    }

    private static List<GrantedAuthority> toGrantedAuthorities(List<Authority> authorities) {
        return authorities.stream().map(AppUser::toGrantedAuthority).collect(toList());
    }

    private static GrantedAuthority toGrantedAuthority(Authority authority) {
        return new SimpleGrantedAuthority(authority.getAuthority());
    }

    public List<String> getDomains() {
        return domains;
    }

    public Map<String, String> getRights() {
        return rights;
    }

}
