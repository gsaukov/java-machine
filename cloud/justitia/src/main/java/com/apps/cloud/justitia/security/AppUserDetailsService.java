package com.apps.cloud.justitia.security;

import com.apps.cloud.justitia.data.entity.Authority;
import com.apps.cloud.justitia.data.entity.User;
import com.apps.cloud.justitia.data.entity.UserDomainAccess;
import com.apps.cloud.justitia.data.entity.UserRights;
import com.apps.cloud.justitia.data.repository.AuthorityRepository;
import com.apps.cloud.justitia.data.repository.UserDomainAccessRepository;
import com.apps.cloud.justitia.data.repository.UserRepository;
import com.apps.cloud.justitia.data.repository.UserRightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.text.MessageFormat.format;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserDomainAccessRepository userDomainAccessRepository;

    @Autowired
    private UserRightsRepository userRightsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException(format("Unknown user with username [{0}].", username));
        }

        List<Authority> authorities = authorityRepository.findByUser(user.get());
        List<String> domainAccesses = getUserDomainAccess(user.get());
        Map<String, String> userRights = getUserRights(user.get());

        return toAppUser(user.get(), authorities, domainAccesses, userRights);
    }

    private Map<String, String> getUserRights(User user) {
        Map<String, String> userRights = new HashMap<>();
        for(UserRights userRight : userRightsRepository.findByUser(user)) {
            userRights.put(userRight.getRightName(), userRight.getRightValue());
        }
        return userRights;
    }

    private List<String> getUserDomainAccess(User user) {
        List<String> domainAccesses = new ArrayList<>();
        for(UserDomainAccess userDomainAccess : userDomainAccessRepository.findByUser(user)) {
            domainAccesses.add(userDomainAccess.getDomain());
        }
        return domainAccesses;
    }

    private AppUser toAppUser(User user, List<Authority> authorities, List<String> domains, Map<String, String> rights) {
        return new AppUser(user.getId(), user.getUsername(), user.getPassword(), authorities, domains, rights);
    }

}
