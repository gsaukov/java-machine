package com.apps.searchandpagination.persistance.query.trade;

import com.apps.searchandpagination.persistance.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static javax.transaction.Transactional.TxType.REQUIRED;

@Service
@Transactional(REQUIRED)
public class TradeDetailsDynamicQuery {

    @Autowired
    private EntityManager entityManager;

    public Page<TradeDetails> queryTrades(Pageable page, TradeDetailsCriteria tradeDetailsCriteria) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<TradeDetails> queryTradeDetails = builder.createQuery(TradeDetails.class);

        Root<TradeDetails> rootTradeDetails = queryTradeDetails.from(TradeDetails.class);
        Join<TradeDetails, TradeData> joinTradeData = rootTradeDetails.join(TradeDetails_.tradeData);

        List<Predicate> predicates = createQueryTradesPredicates(tradeDetailsCriteria, builder, rootTradeDetails, joinTradeData);

        queryTradeDetails.where(predicates.toArray(new Predicate[]{}));

        long count = countQueryTrades(builder, predicates);

        addOrderBy(tradeDetailsCriteria, builder, queryTradeDetails,joinTradeData);

        TypedQuery query = entityManager.createQuery(queryTradeDetails);
        query.setFirstResult(page.getPageNumber() * page.getPageSize());
        query.setMaxResults(page.getPageSize());

        return new PageImpl<>(query.getResultList(), page, count);
    }

    private List<Predicate> createQueryTradesPredicates(TradeDetailsCriteria tradeDetailsCriteria, CriteriaBuilder builder,
                                                        Root<TradeDetails> rootTradeDetails,
                                                        Join<TradeDetails, TradeData> joinTradeData) {
        List<Predicate> predicates = new ArrayList<>();

        if (tradeDetailsCriteria.getIds() != null && !tradeDetailsCriteria.getIds().isEmpty()) {
            predicates.add(creatIn(builder, joinTradeData.get(TradeData_.id), tradeDetailsCriteria.getIds()));
        }

        if (tradeDetailsCriteria.getAccounts() != null && !tradeDetailsCriteria.getAccounts().isEmpty()) {
            predicates.add(creatIn(builder, joinTradeData.get(TradeData_.account), tradeDetailsCriteria.getAccounts()));
        }

        if (tradeDetailsCriteria.getSymbols() != null && !tradeDetailsCriteria.getSymbols().isEmpty()) {
            predicates.add(creatIn(builder, joinTradeData.get(TradeData_.symbol), tradeDetailsCriteria.getSymbols()));
        }

        if (tradeDetailsCriteria.getRoute() != null) {
            predicates.add(builder.equal(joinTradeData.get(TradeData_.route), tradeDetailsCriteria.getRoute()));
        }

        if (tradeDetailsCriteria.getDateAfter() != null) {
            predicates.add(builder.greaterThanOrEqualTo(joinTradeData.get(TradeData_.date), tradeDetailsCriteria.getDateAfter()));
        }

        if (tradeDetailsCriteria.getDateBefore() != null) {
            predicates.add(builder.lessThanOrEqualTo(joinTradeData.get(TradeData_.date), tradeDetailsCriteria.getDateBefore()));
        }

        if (tradeDetailsCriteria.getIban() != null) {
            predicates.add(builder.equal(rootTradeDetails.get(TradeDetails_.iban), tradeDetailsCriteria.getIban()));
        }

        if (tradeDetailsCriteria.getFirstName() != null) {
            predicates.add(builder.like(builder.lower(rootTradeDetails.get(TradeDetails_.firstName)),
                    "%" + tradeDetailsCriteria.getFirstName().toLowerCase() + "%"));
        }

        if (tradeDetailsCriteria.getLastName() != null) {
            predicates.add(builder.like(builder.lower(rootTradeDetails.get(TradeDetails_.lastName)),
                    "%" + tradeDetailsCriteria.getLastName().toLowerCase() + "%"));
        }

        if (tradeDetailsCriteria.getAmountGreater() != null) {
            predicates.add(builder.greaterThanOrEqualTo(joinTradeData.get(TradeData_.amount).get(EmbeddableBigMoney_.amount), tradeDetailsCriteria.getAmountGreater().getAmount()));
            predicates.add(builder.equal(joinTradeData.get(TradeData_.amount).get(EmbeddableBigMoney_.currency), tradeDetailsCriteria.getAmountGreater().getCurrencyUnit()));
        }

        if (tradeDetailsCriteria.getAmountLess() != null) {
            predicates.add(builder.lessThanOrEqualTo(joinTradeData.get(TradeData_.amount).get(EmbeddableBigMoney_.amount), tradeDetailsCriteria.getAmountLess().getAmount()));
            predicates.add(builder.equal(joinTradeData.get(TradeData_.amount).get(EmbeddableBigMoney_.currency), tradeDetailsCriteria.getAmountLess().getCurrencyUnit()));
        }

        return predicates;
    }

    private Predicate creatIn(CriteriaBuilder builder, Path path, List list){
        CriteriaBuilder.In in = builder.in(path);
        list.forEach(i -> in.value(i));
        return in;
    }

    private void addOrderBy(TradeDetailsCriteria tradeDetailsCriteria, CriteriaBuilder builder,
                            CriteriaQuery<TradeDetails> queryTradeDetails,
                            Join<TradeDetails, TradeData> joinTradeData){
        if(tradeDetailsCriteria.getOrder() == null){
            queryTradeDetails.orderBy(builder.desc(joinTradeData.get(TradeData_.date)));
            return;
        }
        switch (tradeDetailsCriteria.getOrder()){
            case AMOUNT:
                queryTradeDetails.orderBy(builder.desc(joinTradeData.get(TradeData_.amount)));
                break;
            case SYMBOL:
                queryTradeDetails.orderBy(builder.asc(joinTradeData.get(TradeData_.symbol)));
                break;
            case DATE:
            default:
                queryTradeDetails.orderBy(builder.desc(joinTradeData.get(TradeData_.date)));
                break;
        }
    }

    private Long countQueryTrades(CriteriaBuilder builder, List<Predicate> predicates) {
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        countQuery.select(builder.count(countQuery.from(TradeDetails.class).join(TradeDetails_.tradeData)));
        entityManager.createQuery(countQuery);
        countQuery.where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}