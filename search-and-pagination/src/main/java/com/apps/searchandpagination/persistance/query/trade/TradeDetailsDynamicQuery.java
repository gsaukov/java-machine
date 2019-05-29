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
import java.util.Collection;
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

        addOrderBy(tradeDetailsCriteria, builder, queryTradeDetails, joinTradeData);

        TypedQuery<TradeDetails> query = entityManager.createQuery(queryTradeDetails);
        query.setFirstResult(page.getPageNumber() * page.getPageSize());
        query.setMaxResults(page.getPageSize());

        List<TradeDetails> res = query.getResultList();
        long count = countQueryTrades(tradeDetailsCriteria, builder);

        return new PageImpl<>(res, page, count);
    }

    private List<Predicate> createQueryTradesPredicates(TradeDetailsCriteria tradeDetailsCriteria, CriteriaBuilder builder,
                                                        Root<TradeDetails> rootTradeDetails,
                                                        Join<TradeDetails, TradeData> joinTradeData) {
        List<Predicate> predicates = new ArrayList<>();

        if (isNotEmpty(tradeDetailsCriteria.getIds())) {
            predicates.add(creatIn(builder, joinTradeData.get(TradeData_.id), tradeDetailsCriteria.getIds()));
        }

        if (isNotEmpty(tradeDetailsCriteria.getAccounts())) {
            predicates.add(creatIn(builder, joinTradeData.get(TradeData_.account), tradeDetailsCriteria.getAccounts()));
        }

        if (isNotEmpty(tradeDetailsCriteria.getSymbols())) {
            predicates.add(creatIn(builder, joinTradeData.get(TradeData_.symbol), tradeDetailsCriteria.getSymbols()));
        }

        if (isNotEmpty(tradeDetailsCriteria.getRoute())) {
            predicates.add(builder.equal(joinTradeData.get(TradeData_.route), tradeDetailsCriteria.getRoute()));
        }

        if (isNotEmpty(tradeDetailsCriteria.getDateAfter())) {
            predicates.add(builder.greaterThanOrEqualTo(joinTradeData.get(TradeData_.date), tradeDetailsCriteria.getDateAfter()));
        }

        if (isNotEmpty(tradeDetailsCriteria.getDateBefore())) {
            predicates.add(builder.lessThanOrEqualTo(joinTradeData.get(TradeData_.date), tradeDetailsCriteria.getDateBefore()));
        }

        if (isNotEmpty(tradeDetailsCriteria.getIban())) {
            predicates.add(builder.equal(rootTradeDetails.get(TradeDetails_.iban), tradeDetailsCriteria.getIban()));
        }

        if (isNotEmpty(tradeDetailsCriteria.getFirstName())) {
            if(TradeDetailsCriteria.ComparisonType.LIKE.equals(tradeDetailsCriteria.getFirstNameComparisonType())){
                predicates.add(builder.like(builder.lower(rootTradeDetails.get(TradeDetails_.firstName)),
                    tradeDetailsCriteria.getFirstName().toLowerCase()));
            } else {
                predicates.add(builder.equal(rootTradeDetails.get(TradeDetails_.firstName),
                        tradeDetailsCriteria.getFirstName()));
            }
        }

        if (isNotEmpty(tradeDetailsCriteria.getLastName())) {
            if(TradeDetailsCriteria.ComparisonType.LIKE.equals(tradeDetailsCriteria.getLastNameComparisonType())){
                predicates.add(builder.like(builder.lower(rootTradeDetails.get(TradeDetails_.lastName)),
                        tradeDetailsCriteria.getLastName().toLowerCase()));
            } else {
                predicates.add(builder.equal(rootTradeDetails.get(TradeDetails_.lastName),
                        tradeDetailsCriteria.getLastName()));
            }
        }

        if (isNotEmpty(tradeDetailsCriteria.getAmountGreater())) {
            predicates.add(builder.greaterThanOrEqualTo(joinTradeData.get(TradeData_.amount).get(EmbeddableBigMoney_.amount), tradeDetailsCriteria.getAmountGreater().getAmount()));
            predicates.add(builder.equal(joinTradeData.get(TradeData_.amount).get(EmbeddableBigMoney_.currency), tradeDetailsCriteria.getAmountGreater().getCurrencyUnit()));
        }

        if (isNotEmpty(tradeDetailsCriteria.getAmountLess())) {
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

    private Long countQueryTrades(TradeDetailsCriteria tradeDetailsCriteria, CriteriaBuilder builder) {
        CriteriaQuery<TradeDetails> queryTradeDetails = builder.createQuery(TradeDetails.class);
        Root<TradeDetails> rootTradeDetails = queryTradeDetails.from(TradeDetails.class);
        Join<TradeDetails, TradeData> joinTradeData = rootTradeDetails.join(TradeDetails_.tradeData);
        List<Predicate> predicates = createQueryTradesPredicates(tradeDetailsCriteria, builder, rootTradeDetails, joinTradeData);
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        countQuery.select(builder.count(countQuery.from(TradeDetails.class).join(TradeDetails_.tradeData)));
        entityManager.createQuery(countQuery);
        countQuery.where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    public static boolean isNotEmpty(final Collection< ? > o) {
        return o != null && !o.isEmpty();
    }

    public static boolean isNotEmpty(final String o) {
        return o != null && !o.isEmpty();
    }

    public static boolean isNotEmpty(Object o) {
        return o != null;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}