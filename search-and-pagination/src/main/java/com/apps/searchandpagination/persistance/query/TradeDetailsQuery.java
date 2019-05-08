package com.apps.searchandpagination.persistance.query;

import com.apps.searchandpagination.persistance.entity.TradeData;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TradeDetailsQuery {

//    public void givenFirstAndLastName_whenGettingListOfUsers_thenCorrect() {
//        UserSpecification spec1 =
//                new UserSpecification(new SearchCriteria("firstName", ":", "john"));
//        UserSpecification spec2 =
//                new UserSpecification(new SearchCriteria("lastName", ":", "doe"));
//
//        List<User> results = repository.findAll(Specification.where(spec1).and(spec2));
//
//        assertThat(userJohn, isIn(results));
//        assertThat(userTom, not(isIn(results)));
//    }

}
