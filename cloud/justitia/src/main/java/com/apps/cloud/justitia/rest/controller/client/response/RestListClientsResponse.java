package com.apps.cloud.justitia.rest.controller.client.response;

import com.apps.cloud.common.rest.RestResponse;

import java.util.List;

public class RestListClientsResponse implements RestResponse {

    private List<RestListClientDetails> clients;

    private RestListClientsResponse() {
        // REST
    }

    private RestListClientsResponse(Builder builder) {
        clients = builder.clients;
    }

    public List<RestListClientDetails> getClients() {
        return clients;
    }

    public void ListClients(List<RestListClientDetails> clients) {
        this.clients = clients;
    }

    public static final class Builder {

        private List<RestListClientDetails> clients;

        public Builder withClients(List<RestListClientDetails> clients) {
            this.clients = clients;
            return this;
        }

        public RestListClientsResponse build() {
            return new RestListClientsResponse(this);
        }

    }

}
