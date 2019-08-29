package com.apps.cloud.justitia.security;

import com.apps.cloud.justitia.data.entity.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class AppUser extends User {

    public AppUser(String username, String password, List<Authority> authorities) {
        super(username, password, toGrantedAuthorities(authorities));
    }

    private static List<GrantedAuthority> toGrantedAuthorities(List<Authority> authorities) {
        return authorities.stream().map(AppUser::toGrantedAuthority).collect(toList());
    }

    private static GrantedAuthority toGrantedAuthority(Authority authority) {
        return new SimpleGrantedAuthority(authority.getAuthority());
    }

}
