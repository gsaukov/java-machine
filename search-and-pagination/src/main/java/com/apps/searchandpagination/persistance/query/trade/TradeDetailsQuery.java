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

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@Service
public class TradeDetailsQuery {

    @Autowired
    private TradeDetailsRepository tradeDetailsRepository;

    @Autowired
    private EntityManager entityManager;


    public void queryTradeDetails() {
        TradeDetailsSpecification spec1 =
                new TradeDetailsSpecification(new SearchCriteria(TradeDetails_.firstName, ":", "john"));
        TradeDetailsSpecification spec2 =
                new TradeDetailsSpecification(new SearchCriteria(TradeDetails_.lastName, ":", "doe"));

        List<TradeDetails> results = tradeDetailsRepository.findAll(Specification.where(spec1).and(spec2));
    }

    public void queryTrade(TradeDetailsCriteria tradeDetailsCriteria) {
        TradeDetailsCriteria cr = new TradeDetailsCriteria();
        List<String> accs = new ArrayList<>();
        accs.add("QJVQZTGVBOFMRP6OQHI");
        accs.add("BTGUW");
        accs.add("Z9KUI73CSM");
        accs.add("V2RQ6ZG0WAGVC0");

        List<String> symbs = new ArrayList<>();
        symbs.add("9PWKA2Y2VPBBOHHHHK6");
        symbs.add("HOVJXTUM2NWJZ3RKMX");
        symbs.add("RD4SNZBM3MCIIV");
        symbs.add("KGK0B");
        cr.setAccounts(accs);

        cr.setSymbols(symbs);

        cr.setDateAfter(LocalDateTime.now());

        createTransactionCriteriaQuery(cr);
        List<TradeDetails> results = tradeDetailsRepository.findAll(returnCandidates(tradeDetailsCriteria));
    }

    private Specification<TradeDetails> returnCandidates(TradeDetailsCriteria tradeDetailsCriteria) {
// rewrite with query and null checks.

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

    public List<TradeDetails> createTransactionCriteriaQuery(TradeDetailsCriteria tradeDetailsCriteria){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<TradeDetails> queryTradeDetails = builder.createQuery(TradeDetails.class);

        Root<TradeDetails> rootTradeDetails = queryTradeDetails.from(TradeDetails.class);
        Join<TradeDetails, TradeData> joinTradeData = rootTradeDetails.join(TradeDetails_.tradeData);

        List<Predicate> predicates = new ArrayList<>();

        if(tradeDetailsCriteria.getIds()!=null && !tradeDetailsCriteria.getIds().isEmpty()){
            predicates.add(creatIn(builder, joinTradeData.get(TradeData_.id), tradeDetailsCriteria.getIds()));
        }

        if(tradeDetailsCriteria.getAccounts()!=null && !tradeDetailsCriteria.getAccounts().isEmpty()){
            predicates.add(creatIn(builder, joinTradeData.get(TradeData_.account), tradeDetailsCriteria.getAccounts()));
        }

        if(tradeDetailsCriteria.getSymbols()!=null && !tradeDetailsCriteria.getSymbols().isEmpty()){
            predicates.add(creatIn(builder, joinTradeData.get(TradeData_.symbol), tradeDetailsCriteria.getSymbols()));
        }

        if(tradeDetailsCriteria.getRoute()!=null){
            predicates.add(builder.equal(joinTradeData.get(TradeData_.route), tradeDetailsCriteria.getRoute()));
        }

        if(tradeDetailsCriteria.getDateAfter()!=null){
            predicates.add(builder.greaterThanOrEqualTo(joinTradeData.get(TradeData_.date), tradeDetailsCriteria.getDateAfter()));
        }

        if(tradeDetailsCriteria.getDateBefore()!=null){
            predicates.add(builder.lessThanOrEqualTo(joinTradeData.get(TradeData_.date), tradeDetailsCriteria.getDateBefore()));
        }

        if(tradeDetailsCriteria.getIban()!=null){
            predicates.add(builder.equal(rootTradeDetails.get(TradeDetails_.iban), tradeDetailsCriteria.getIban()));
        }

        if(tradeDetailsCriteria.getFirstName()!=null){
            predicates.add(builder.like(builder.lower(rootTradeDetails.get(TradeDetails_.firstName)),
                    "%" + tradeDetailsCriteria.getFirstName().toLowerCase() + "%"));
        }

        if(tradeDetailsCriteria.getLastName()!=null){
            predicates.add(builder.like(builder.lower(rootTradeDetails.get(TradeDetails_.lastName)),
                    "%" + tradeDetailsCriteria.getLastName().toLowerCase() + "%"));
        }

        if(tradeDetailsCriteria.getAmountGreater()!=null){
            predicates.add(builder.greaterThanOrEqualTo(joinTradeData.get(TradeData_.amount), tradeDetailsCriteria.getAmountGreater()));
        }

        if(tradeDetailsCriteria.getAmountLess()!=null){
            predicates.add(builder.lessThanOrEqualTo(joinTradeData.get(TradeData_.amount), tradeDetailsCriteria.getAmountLess()));
        }

        queryTradeDetails.where(predicates.toArray(new Predicate[]{}));

        return entityManager.createQuery(queryTradeDetails).getResultList();
//        Predicate hasBirthday = builder.equal(root.get(TradeDetails_.firstName), "dfsf");

    }

    private Predicate creatIn(CriteriaBuilder builder, Path path, List list){
        CriteriaBuilder.In in = builder.in(path);
        list.forEach(i -> in.value(i));
        return in;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
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