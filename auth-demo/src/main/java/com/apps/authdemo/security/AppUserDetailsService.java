package com.apps.authdemo.security;

import com.apps.authdemo.data.entity.User;
import com.apps.authdemo.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.text.MessageFormat.format;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException(format("Unknown user with username [{0}].", username));
        }

        return toAppUser(user.get());
    }

    private org.springframework.security.core.userdetails.User toAppUser(User user) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), null);
    }

}
