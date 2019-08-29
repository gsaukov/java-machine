package com.apps.cloud.justitia.security;

import com.apps.cloud.justitia.data.entity.Authority;
import com.apps.cloud.justitia.data.entity.User;
import com.apps.cloud.justitia.data.repository.AuthorityRepository;
import com.apps.cloud.justitia.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    private AppUser toAppUser(User user, List<Authority> authorities) {
        return new AppUser(user.getUsername(), user.getPassword(), authorities);
    }

}
