package com.apps.searchandpagination.persistance.converters;

import com.apps.searchandpagination.persistance.entity.AccountAddress;
import com.apps.searchandpagination.service.account.json.AddressJson;

public class AccountAddressConverter {

    public AccountAddress convert(AddressJson addressJson){
        AccountAddress address = new AccountAddress();
        address.setStreet(addressJson.getStreet());
        address.setCity(addressJson.getCity());
        address.setState(addressJson.getState());
        address.setPostalCode(addressJson.getPostalCode());
        address.setLatitude(addressJson.getLatitude());
        address.setLongitude(addressJson.getLongitude());
        return address;
    }

}
