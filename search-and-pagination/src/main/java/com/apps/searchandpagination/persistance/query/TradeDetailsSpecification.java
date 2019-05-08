package com.apps.searchandpagination.persistance.query;

import com.apps.searchandpagination.persistance.entity.TradeDetails;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TradeDetailsSpecification extends BasePredicate<TradeDetails> {

    protected TradeDetailsSpecification(SearchCriteria criteria) {
//        super(criteria);
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
