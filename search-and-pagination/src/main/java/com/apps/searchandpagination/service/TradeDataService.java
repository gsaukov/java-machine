package com.apps.searchandpagination.service;

import com.apps.reflection.RandomObjectFiller;
import com.apps.searchandpagination.persistance.entity.TradeData;
import com.apps.searchandpagination.persistance.repository.TradeDataRepository;
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
public class TradeDataService {

    private RandomObjectFiller filler = new RandomObjectFiller();
    private List<TradeData> dataObjects = new ArrayList<>();

    @Autowired
    private TradeDataRepository tradeDataRepository;

    public TradeDataService(TradeDataRepository tradeDataRepository) {
        this.tradeDataRepository = tradeDataRepository;
        fillDataObjects();
    }

    public Page<TradeData> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<TradeData> list;

        if (dataObjects.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, dataObjects.size());
            list = dataObjects.subList(startItem, toIndex);
        }

        Page<TradeData> dataPage= new PageImpl<>(list, PageRequest.of(currentPage, pageSize), dataObjects.size());

        return dataPage;
    }

    private void fillDataObjects() {
        for(int i = 0; i < 1000; i++){
            try {
                TradeData tradeData = filler.createAndFill(TradeData.class);
                dataObjects.add(tradeData);
                tradeDataRepository.save(tradeData);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
