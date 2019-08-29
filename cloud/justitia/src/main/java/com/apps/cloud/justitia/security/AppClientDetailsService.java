package com.apps.cloud.justitia.security;

import com.apps.cloud.justitia.data.entity.Client;
import com.apps.cloud.justitia.data.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.text.MessageFormat.format;

@Service
public class AppClientDetailsService implements ClientDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        Optional<Client> client = clientRepository.findByClientId(clientId);

        if (!client.isPresent()) {
            throw new NoSuchClientException(format("Unknown client with clientId [{0}].", clientId));
        }

        return toAppClient(client.get());
    }

    private AppClient toAppClient(Client client) {
        return new AppClient.Builder()
                .withClientId(client.getClientId())
                .withClientSecret(client.getClientSecret())
                .withGrantTypes(client.getGrantTypes())
                .withScopes(client.getScopes())
                .withAccessTokenDuration(client.getAccessTokenDuration())
                .withRefreshTokenDuration(client.getRefreshTokenDuration())
                .withAutoApprove(client.getAutoApprove())
                .withRedirectUri(client.getRedirectUri())
                .build();
    }

}
