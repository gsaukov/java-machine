package com.apps.searchandpagination.service;

import com.apps.reflection.RandomObjectFiller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataService {

    private RandomObjectFiller filler = new RandomObjectFiller();
    private List<DataObject> dataObjects = new ArrayList<>();

    public DataService() {
        fillDataObjects();
    }

    public Page<DataObject> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<DataObject> list;

        if (dataObjects.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, dataObjects.size());
            list = dataObjects.subList(startItem, toIndex);
        }

        Page<DataObject> dataPage
                = new PageImpl<DataObject>(list, PageRequest.of(currentPage, pageSize), dataObjects.size());

        return dataPage;
    }


    private void fillDataObjects() {
        for(int i = 0; i < 1000; i++){
            try {
                dataObjects.add(filler.createAndFill(DataObject.class));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
