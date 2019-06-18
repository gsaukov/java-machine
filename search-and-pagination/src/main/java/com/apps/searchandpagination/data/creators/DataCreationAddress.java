package com.apps.searchandpagination.data.creators;

import com.apps.reflection.RandomObjectFiller;
import com.apps.searchandpagination.cassandra.entity.AddressData;
import com.apps.searchandpagination.cassandra.repository.AddressDataRepository;
import com.apps.searchandpagination.persistance.converters.AccountAddressConverter;
import com.apps.searchandpagination.persistance.converters.JsonUtils;
import com.apps.searchandpagination.persistance.entity.AccountAddress;
import com.apps.searchandpagination.service.account.json.AddressJson;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


@Component
@ConfigurationProperties(prefix = "data-creation")
public class DataCreationAddress {

    private Resource addressStore;

    private RandomObjectFiller filler = new RandomObjectFiller();

    @Autowired
    private AddressDataRepository addressDataRepository;

    public void createData() throws IllegalAccessException, InstantiationException {
        for(int i = 0; i < 1000; i++){
            AddressData addressData = filler.createAndFill(AddressData.class);
            addressDataRepository.save(addressData);
        }
    }

    public List<AccountAddress> resolveAddressFromFile(){
        List<AccountAddress> accountAddresses = new ArrayList<>();
        AccountAddressConverter converter = new AccountAddressConverter();
        List<AddressJson> addressJsons = resolveJsonAddressFromFile();
        for(AddressJson addressJson : addressJsons){
            accountAddresses.add(converter.convert(addressJson));
        }
        return accountAddresses;
    }

    public List<AddressJson> resolveJsonAddressFromFile(){
        List<AddressJson> addressJsons = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(addressStore.getFile().toPath()));
            addressJsons = JsonUtils.fromJson(content, new TypeReference<List<AddressJson>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressJsons;
    }

    public void setAddressStore(String addressStore) {
        this.addressStore = new PathMatchingResourcePatternResolver().getResource(addressStore);
    }

}
