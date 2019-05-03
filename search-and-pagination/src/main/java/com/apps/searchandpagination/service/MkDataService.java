package com.apps.searchandpagination.service;

import com.apps.reflection.RandomObjectFiller;
import com.apps.searchandpagination.persistance.entity.MkData;
import com.apps.searchandpagination.persistance.repository.MkDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MkDataService {

    private RandomObjectFiller filler = new RandomObjectFiller();
    private List<MkData> dataObjects = new ArrayList<>();

    @Autowired
    public void setMkDataRepository(MkDataRepository mkDataRepository) {
        this.mkDataRepository = mkDataRepository;
    }

    private MkDataRepository mkDataRepository;

    public MkDataService() {
        fillDataObjects();
    }

    public Page<MkData> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<MkData> list;

        if (dataObjects.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, dataObjects.size());
            list = dataObjects.subList(startItem, toIndex);
        }

        Page<MkData> dataPage= new PageImpl<>(list, PageRequest.of(currentPage, pageSize), dataObjects.size());

        return dataPage;
    }


    private void fillDataObjects() {
        for(int i = 0; i < 1000; i++){
            try {
                MkData mkData = filler.createAndFill(MkData.class);
                dataObjects.add(mkData);
//                mkDataRepository.save(mkData);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
