package com.apps.searchandpagination.persistance.query.trade;

import com.apps.searchandpagination.persistance.entity.TradeDetails;
import com.apps.searchandpagination.persistance.query.general.BasePredicate;
import com.apps.searchandpagination.persistance.query.general.SearchCriteria;

public class TradeDetailsSpecification extends BasePredicate<TradeDetails> {

    public TradeDetailsSpecification(SearchCriteria criteria) {
        super(criteria);
    }

//    public static Specification<TradeDetails> isLongTermCustomer() {
//
//
//
//
//        return (root, query, cb) ->{
//
//            return cb.equal(root.get(TradeDetails_.createdAt), "");
//
//        };
//
//    }
//
//    public void met(CriteriaBuilder builder){
//
//        CriteriaQuery<TradeDetails> query = builder.createQuery(TradeDetails.class);
//
//        Root<TradeDetails> root = query.from(TradeDetails.class);
//
//        Predicate hasBirthday = builder.equal(root.get(TradeDetails_.firstName), "dfsf");
//
//    }
}
