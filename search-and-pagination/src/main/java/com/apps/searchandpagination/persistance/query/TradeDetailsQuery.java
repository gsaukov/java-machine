package com.apps.searchandpagination.persistance.query;

import com.apps.searchandpagination.persistance.entity.TradeData;
import com.apps.searchandpagination.persistance.entity.TradeData_;
import com.apps.searchandpagination.persistance.entity.TradeDetails;
import com.apps.searchandpagination.persistance.entity.TradeDetails_;
import com.apps.searchandpagination.persistance.repository.TradeDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;


@Service
public class TradeDetailsQuery {

    @Autowired
    private TradeDetailsRepository tradeDetailsRepository;


    public void queryTradeDetails() {
        TradeDetailsSpecification spec1 =
                new TradeDetailsSpecification(new SearchCriteria(TradeDetails_.firstName, ":", "john"));
        TradeDetailsSpecification spec2 =
                new TradeDetailsSpecification(new SearchCriteria(TradeDetails_.lastName, ":", "doe"));

        List<TradeDetails> results = tradeDetailsRepository.findAll(Specification.where(spec1).and(spec2));
    }

    public void queryTradeDetailsBank() {
        List<TradeDetails> results = tradeDetailsRepository.findAll(returnCandidates("bank"));
    }

    private Specification<TradeDetails> returnCandidates(String bankName) {

        return (root, query, builder) -> builder.and( //
                builder.equal(root.get(TradeDetails_.bankName), bankName), //
                builder.equal(root.join(TradeDetails_.tradeData).get(TradeData_.route), TradeData.Route.BUY));
    }

    public void setTradeDetailsRepository(TradeDetailsRepository tradeDetailsRepository) {
        this.tradeDetailsRepository = tradeDetailsRepository;
    }

}
