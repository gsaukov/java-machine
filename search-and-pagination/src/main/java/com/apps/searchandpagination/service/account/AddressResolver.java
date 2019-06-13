package com.apps.searchandpagination.service.account;

import com.apps.reflection.RandomObjectFiller;
import com.apps.searchandpagination.cassandra.entity.AddressData;
import com.apps.searchandpagination.persistance.converters.JsonUtils;
import com.apps.searchandpagination.service.account.json.AddressJson;
import com.fasterxml.jackson.core.type.TypeReference;
import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.client.request.NominatimSearchRequest;
import fr.dudie.nominatim.model.Address;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


//https://nominatim.openstreetmap.org/search?q=pilkington+avenue,+birmingham&format=json
@Service
public class AddressResolver {
    private NominatimSearchRequest req;


    public static void main(String args[]) throws IOException {
        AddressResolver resolver = new AddressResolver();
        List<AddressJson> addressJsons = resolver.resolveAddressFromFile();
        resolver.query(addressJsons);
    }

    public void simpleQuery() throws IOException {
        req = new NominatimSearchRequest();
        req.setQuery("vitré, france");
        JsonNominatimClient jsonNominatimClient = new JsonNominatimClient(HttpClientBuilder.create().build(), "");
        List<Address> addresses = jsonNominatimClient.search(req);
    }

    public List<AddressJson> resolveAddressFromFile(){
        List<AddressJson> addressJsons = new ArrayList<>();
        try {
           String content = new String(Files.readAllBytes(Paths.get(AddressResolver.class.getClassLoader().getResource("db/addresses-us-all.json").toURI())));
           addressJsons = JsonUtils.fromJson(content, new TypeReference<List<AddressJson>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return addressJsons;
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
