package com.apps.searchandpagination.service.account;

import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.client.request.NominatimSearchRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AddressResolver {
    private NominatimSearchRequest req;


    public static void main(String args[]) throws IOException {
        AddressResolver resolver = new AddressResolver();
        resolver.simpleQuery();
    }

    public void simpleQuery() throws IOException {
        req = new NominatimSearchRequest();
        req.setQuery("vitré, france");
        JsonNominatimClient jsonNominatimClient = new JsonNominatimClient(HttpClientBuilder.create().build(), "");
        jsonNominatimClient.search(req);
    }
}
