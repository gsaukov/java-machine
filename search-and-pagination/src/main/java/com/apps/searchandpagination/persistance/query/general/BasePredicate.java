package com.apps.searchandpagination.persistance.query.general;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Collection;

public class BasePredicate <T> implements Specification<T> {

    private SearchCriteria criteria;

    public BasePredicate(SearchCriteria criteria){
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (is("=")) {
            if(isType(root, Collection.class)){
                CriteriaBuilder.In in = builder.in(root.get(criteria.getKey()));
                return in.in((Collection) criteria.getValue());
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        } else if (is(">")) {
            return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), (Comparable) criteria.getValue());
        }
        else if (is("<")) {
            return builder.lessThanOrEqualTo(root.get(criteria.getKey()), (Comparable) criteria.getValue());
        }
        else if (is(":")) {
            if (isType(root, String.class)) {
                return builder.like(root.<String> get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }

    private boolean is(String operator) {
        return criteria.getOperation().equalsIgnoreCase(operator);
    }

    private boolean isType(Root<T> root, Class clazz){
        return root.get(criteria.getKey()).getJavaType().isAssignableFrom(clazz);
    }
}
