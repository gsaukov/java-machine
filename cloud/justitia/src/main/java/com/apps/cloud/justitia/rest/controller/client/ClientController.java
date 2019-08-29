package com.apps.cloud.justitia.rest.controller.client;

import com.apps.cloud.common.rest.ErrorResponse;
import com.apps.cloud.common.rest.RestResponse;
import com.apps.cloud.common.rest.VoidResponse;
import com.apps.cloud.justitia.data.entity.Client;
import com.apps.cloud.justitia.data.repository.ClientRepository;
import com.apps.cloud.justitia.rest.controller.client.request.RestCreateClientRequest;
import com.apps.cloud.justitia.rest.controller.client.request.RestRemoveClientRequest;
import com.apps.cloud.justitia.rest.controller.client.response.RestListClientDetails;
import com.apps.cloud.justitia.rest.controller.client.response.RestListClientsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/justitia-api/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/create")
    public ResponseEntity<RestResponse> create(@RequestBody RestCreateClientRequest restCreateClientRequest) {
        if (clientRepository.existsByClientId(restCreateClientRequest.getClientId())) {
            return ErrorResponse.badRequest("CLIENT_ID_ALREADY_EXISTS");
        }

        clientRepository.save(new Client.Builder()
                .withClientId(restCreateClientRequest.getClientId())
                .withClientSecret(passwordEncoder.encode(restCreateClientRequest.getClientSecret()))
                .withGrantTypes(restCreateClientRequest.getGrantTypes())
                .withScopes(restCreateClientRequest.getScopes())
                .withAccessTokenDuration(restCreateClientRequest.getAccessTokenDuration())
                .withRefreshTokenDuration(restCreateClientRequest.getRefreshTokenDuration())
                .withAutoApprove(restCreateClientRequest.getAutoApprove())
                .withRedirectUri(restCreateClientRequest.getRedirectUri())
                .build());

        return VoidResponse.created();
    }

    @PostMapping("/remove")
    public ResponseEntity<RestResponse> remove(@RequestBody RestRemoveClientRequest restRemoveClientRequest) {
        if (clientRepository.removeByClientId(restRemoveClientRequest.getClientId()) > 0L) {
            return VoidResponse.ok();
        }

        return ErrorResponse.badRequest("CLIENT_ID_NOT_FOUND");
    }

    @GetMapping("/list")
    public ResponseEntity<RestResponse> list() {
        List<Client> clients = clientRepository.findAll();

        return new ResponseEntity<>(new RestListClientsResponse.Builder().withClients(clients.stream()
                .map(this::toRestListClientDetails).collect(toList())).build(), HttpStatus.OK);
    }

    private RestListClientDetails toRestListClientDetails(Client client) {
        return new RestListClientDetails.Builder()
                .withClientId(client.getClientId())
                .withGrantTypes(client.getGrantTypes())
                .withScopes(client.getScopes())
                .withAccessTokenDuration(client.getAccessTokenDuration())
                .withRefreshTokenDuration(client.getRefreshTokenDuration())
                .withAutoApprove(client.getAutoApprove())
                .withRedirectUri(client.getRedirectUri())
                .build();
    }

}
