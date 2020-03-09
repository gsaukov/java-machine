package com.apps.cloud.justitia.rest.controller.user;

import com.apps.cloud.common.rest.ErrorResponse;
import com.apps.cloud.common.rest.RestResponse;
import com.apps.cloud.common.rest.VoidResponse;
import com.apps.cloud.justitia.data.entity.User;
import com.apps.cloud.justitia.data.repository.UserRepository;
import com.apps.cloud.justitia.rest.controller.user.request.RestCreateUserRequest;
import com.apps.cloud.justitia.rest.controller.user.request.RestRemoveUserRequest;
import com.apps.cloud.justitia.rest.controller.user.response.RestListUsersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/justitia-api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/create")
    public ResponseEntity<RestResponse> create(@RequestBody RestCreateUserRequest restCreateUserRequest) {
        if (userRepository.existsByUsername(restCreateUserRequest.getUsername())) {
            return ErrorResponse.badRequest("USERNAME_ALREADY_EXISTS");
        }

        userRepository.save(new User.Builder()
                .withUsername(restCreateUserRequest.getUsername())
                .withPassword(passwordEncoder.encode(restCreateUserRequest.getPassword()))
                .build());

        return VoidResponse.created();
    }

    @PostMapping("/remove")
    public ResponseEntity<RestResponse> remove(@RequestBody RestRemoveUserRequest restRemoveUserRequest) {
        if (userRepository.removeByUsername(restRemoveUserRequest.getUsername()) > 0L) {
            return VoidResponse.ok();
        }

        return ErrorResponse.badRequest("USERNAME_NOT_FOUND");
    }

    @GetMapping("/list")
    public ResponseEntity<RestResponse> list() {
        List<String> usernames = userRepository.findAll().stream().map(User::getUsername).collect(toList());

        return new ResponseEntity<>(new RestListUsersResponse.Builder().withUsernames(usernames).build(), HttpStatus.OK);
    }

    @GetMapping("/login")
    public String login(HttpServletRequest req, Model model) {
        enrichModel(req, model);
        return "loginpage";
    }

    private void enrichModel(HttpServletRequest req, Model model){
        Object objRequest = req.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
        if(objRequest != null && SavedRequest.class.isAssignableFrom(objRequest.getClass())){
            SavedRequest savedRequest = (SavedRequest) objRequest;
            String clientId = getParameterValues(savedRequest, "client_id");
            if(clientId != null){
                model.addAttribute("clientName", clientId);
            } else {
                model.addAttribute("clientName", "Justitia");
            }
            String scope = getParameterValues(savedRequest, "scope");
            if(scope != null) {
                model.addAttribute("scope", scope);
            }
        }
    }

    private String getParameterValues(SavedRequest savedRequest, String parameterName) {
        String[] params = savedRequest.getParameterValues(parameterName);
        if(params != null && params.length > 0){
            return params[0];
        }
        return null;
    }

}
