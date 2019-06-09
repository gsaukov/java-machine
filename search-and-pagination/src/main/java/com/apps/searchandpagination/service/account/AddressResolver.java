package com.apps.searchandpagination.service.account;

import com.apps.reflection.RandomObjectFiller;
import com.apps.searchandpagination.cassandra.entity.AddressData;
import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.client.request.NominatimSearchRequest;
import fr.dudie.nominatim.model.Address;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


//https://nominatim.openstreetmap.org/search?q=pilkington+avenue,+birmingham&format=json
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
        List<Address> addresses = jsonNominatimClient.search(req);
        return;
    }
}
