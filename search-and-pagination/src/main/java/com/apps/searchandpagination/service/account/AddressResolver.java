package com.apps.searchandpagination.service.account;

import com.apps.searchandpagination.data.creators.DataCreationAddress;
import com.apps.searchandpagination.service.account.json.AddressJson;
import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.client.request.NominatimSearchRequest;
import fr.dudie.nominatim.model.Address;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//https://nominatim.openstreetmap.org/search?q=pilkington+avenue,+birmingham&format=json
@Service
public class AddressResolver {
    private NominatimSearchRequest req;

    public static void main(String args[]) throws IOException {
        AddressResolver resolver = new AddressResolver();
        DataCreationAddress dataCreation = new DataCreationAddress();
        List<AddressJson> addressJsons = dataCreation.resolveAddressFromFile();
        resolver.query(addressJsons);
    }

    public void simpleQuery() throws IOException {
        req = new NominatimSearchRequest();
        req.setQuery("vitré, france");
        JsonNominatimClient jsonNominatimClient = new JsonNominatimClient(HttpClientBuilder.create().build(), "");
        List<Address> addresses = jsonNominatimClient.search(req);
    }

    public List<List<Address>> query(List<AddressJson> addressesSource) throws IOException {
        List<List<Address>> addresses = new ArrayList();
        JsonNominatimClient jsonNominatimClient = new JsonNominatimClient(HttpClientBuilder.create().build(), "");
        for(int i = 0; i < 100; i++){
            req = new NominatimSearchRequest();
            req.setQuery(addressesSource.get(i).toString());
            addresses.add(jsonNominatimClient.search(req));
        }
        return addresses;
    }
}
