package com.apps.cloud.justitia.rest.controller.authority;

import com.apps.cloud.common.rest.ErrorResponse;
import com.apps.cloud.common.rest.RestResponse;
import com.apps.cloud.common.rest.VoidResponse;
import com.apps.cloud.justitia.data.entity.Authority;
import com.apps.cloud.justitia.data.entity.User;
import com.apps.cloud.justitia.data.repository.AuthorityRepository;
import com.apps.cloud.justitia.data.repository.UserRepository;
import com.apps.cloud.justitia.rest.controller.authority.request.RestCreateAuthorityRequest;
import com.apps.cloud.justitia.rest.controller.authority.request.RestListAuthoritiesRequest;
import com.apps.cloud.justitia.rest.controller.authority.request.RestRemoveAuthorityRequest;
import com.apps.cloud.justitia.rest.controller.authority.response.RestListAuthoritiesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/justitia-api/authority")
public class AuthorityController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @PostMapping("/create")
    public ResponseEntity<RestResponse> create(@RequestBody RestCreateAuthorityRequest restCreateAuthorityRequest) {
        Optional<User> user = userRepository.findByUsername(restCreateAuthorityRequest.getUsername());

        if (!user.isPresent()) {
            return ErrorResponse.badRequest("USERNAME_NOT_FOUND");
        }

        if (authorityRepository.existsByUserAndAuthority(user.get(), restCreateAuthorityRequest.getAuthority())) {
            return ErrorResponse.badRequest("AUTHORITY_ALREADY_EXISTS");
        }

        authorityRepository.save(new Authority.Builder()
                .withUser(user.get())
                .withAuthority(restCreateAuthorityRequest.getAuthority())
                .build());

        return VoidResponse.created();
    }

    @PostMapping("/remove")
    public ResponseEntity<RestResponse> remove(@RequestBody RestRemoveAuthorityRequest restRemoveAuthorityRequest) {
        Optional<User> user = userRepository.findByUsername(restRemoveAuthorityRequest.getUsername());

        if (!user.isPresent()) {
            return ErrorResponse.badRequest("USERNAME_NOT_FOUND");
        }

        if (authorityRepository.removeByUserAndAuthority(user.get(), restRemoveAuthorityRequest.getAuthority()) > 0L) {
            return VoidResponse.ok();
        }

        return ErrorResponse.badRequest("AUTHORITY_NOT_FOUND");
    }

    @PostMapping("/list")
    public ResponseEntity<RestResponse> list(@RequestBody RestListAuthoritiesRequest restListAuthoritiesRequest) {
        Optional<User> user = userRepository.findByUsername(restListAuthoritiesRequest.getUsername());

        if (!user.isPresent()) {
            return ErrorResponse.badRequest("USERNAME_NOT_FOUND");
        }

        List<String> authorities = authorityRepository.findByUser(user.get()).stream().map(Authority::getAuthority).collect(toList());

        return new ResponseEntity<>(new RestListAuthoritiesResponse.Builder().withAuthorities(authorities).build(), HttpStatus.OK);
    }

}
