package com.apps.searchandpagination.persistance.query.trade;

import com.apps.searchandpagination.persistance.entity.TradeData;
import com.apps.searchandpagination.persistance.entity.TradeData_;
import com.apps.searchandpagination.persistance.entity.TradeDetails;
import com.apps.searchandpagination.persistance.entity.TradeDetails_;
import com.apps.searchandpagination.persistance.query.general.BasePredicate;
import com.apps.searchandpagination.persistance.query.general.SearchCriteria;
import com.apps.searchandpagination.persistance.repository.TradeDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
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

    public void queryTrade(TradeDetailsCriteria tradeDetailsCriteria) {
        List<TradeDetails> results = tradeDetailsRepository.findAll(returnCandidates(tradeDetailsCriteria));
    }

    private Specification<TradeDetails> returnCandidates(TradeDetailsCriteria tradeDetailsCriteria) {

        return (root, query, builder) -> builder.and(
                creatIn(builder, root.join(TradeDetails_.tradeData).get(TradeData_.id), tradeDetailsCriteria.getIds()),
                creatIn(builder, root.join(TradeDetails_.tradeData).get(TradeData_.account), tradeDetailsCriteria.getAccounts()),
                creatIn(builder, root.join(TradeDetails_.tradeData).get(TradeData_.symbol), tradeDetailsCriteria.getSymbols()),
//                builder.in(root.join(TradeDetails_.tradeData).get(TradeData_.id)).in(tradeDetailsCriteria.getIds()),
//                builder.in(root.join(TradeDetails_.tradeData).get(TradeData_.account)).in(tradeDetailsCriteria.getAccounts()),
//                builder.in(root.join(TradeDetails_.tradeData).get(TradeData_.symbol)).in(tradeDetailsCriteria.getSymbols()),
                builder.equal(root.join(TradeDetails_.tradeData).get(TradeData_.route), tradeDetailsCriteria.getRoute()),
//                builder.greaterThanOrEqualTo(root.join(TradeDetails_.tradeData).get(TradeData_.amount), tradeDetailsCriteria.getAmountGreater()),
//                builder.lessThanOrEqualTo(root.join(TradeDetails_.tradeData).get(TradeData_.amount), tradeDetailsCriteria.getAmountLess()),
                builder.greaterThanOrEqualTo(root.join(TradeDetails_.tradeData).get(TradeData_.date), tradeDetailsCriteria.getDateAfter()),
                builder.lessThanOrEqualTo(root.join(TradeDetails_.tradeData).get(TradeData_.date), tradeDetailsCriteria.getDateBefore()),
                builder.equal(root.get(TradeDetails_.iban), tradeDetailsCriteria.getIban()),
                builder.like(root.get(TradeDetails_.firstName), tradeDetailsCriteria.getFirstName()),
                builder.like(root.get(TradeDetails_.lastName), tradeDetailsCriteria.getLastName()));
    }

    private Predicate creatIn(CriteriaBuilder builder, Path path, List list){
        CriteriaBuilder.In in = builder.in(path);
        list.forEach(i -> in.value(i));
        return in;
    }

    public void setTradeDetailsRepository(TradeDetailsRepository tradeDetailsRepository) {
        this.tradeDetailsRepository = tradeDetailsRepository;
    }



}

//    TradeDetailsCriteria cr = new TradeDetailsCriteria();
//        cr.setAccounts(new ArrayList<>());
//        cr.setIds(new ArrayList<>());
//        cr.setSymbols(new ArrayList<>());
//        cr.setFirstName("an");
//        tradeDetailsRepository.findAll(returnCandidates(cr));