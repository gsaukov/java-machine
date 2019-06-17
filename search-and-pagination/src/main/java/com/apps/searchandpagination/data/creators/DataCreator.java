package com.apps.searchandpagination.data.creators;

import com.apps.reflection.RandomObjectFiller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataCreator {

    private RandomObjectFiller filler = new RandomObjectFiller();

    @Autowired
    private DataCreationAccount dataCreationAccount;
    @Autowired
    private DataCreationAddress dataCreationAddress;
    @Autowired
    private DataCreationTrade dataCreationTrade;

    public void createData(){
        try {
            //Order matters
            dataCreationAccount.createData();
            dataCreationTrade.createData();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
