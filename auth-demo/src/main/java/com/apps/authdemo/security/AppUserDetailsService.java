package com.apps.authdemo.security;

import com.apps.authdemo.data.entity.Authority;
import com.apps.authdemo.data.entity.User;
import com.apps.authdemo.data.repository.AuthorityRepository;
import com.apps.authdemo.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.text.MessageFormat.format;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException(format("Unknown user with username [{0}].", username));
        }

        List<Authority> authorities = authorityRepository.findByUser(user.get());
        return toAppUser(user.get(), authorities);
    }

    private org.springframework.security.core.userdetails.User toAppUser(User user, List<Authority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), toGrantedAuthorities(authorities));
    }

    private List<GrantedAuthority> toGrantedAuthorities(List<Authority> authorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for(Authority authority : authorities){
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
        return grantedAuthorities;
    }
}
