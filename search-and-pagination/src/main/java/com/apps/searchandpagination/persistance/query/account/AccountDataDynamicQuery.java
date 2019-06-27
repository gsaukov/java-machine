package com.apps.searchandpagination.persistance.query.account;

import com.apps.searchandpagination.persistance.entity.AccountAddress;
import com.apps.searchandpagination.persistance.entity.AccountAddress_;
import com.apps.searchandpagination.persistance.entity.AccountData;
import com.apps.searchandpagination.persistance.entity.AccountData_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.transaction.Transactional.TxType.REQUIRED;

@Service
@Transactional(REQUIRED)
public class AccountDataDynamicQuery {

    @Autowired
    private EntityManager entityManager;

    public Page<AccountData> queryAccounts(Pageable page, AccountDataCriteria accountDataCriteria) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<AccountData> queryAccountData = builder.createQuery(AccountData.class);
        queryAccountData.distinct(true);

        Root<AccountData> rootAccountData = queryAccountData.from(AccountData.class);
        Join<AccountData, AccountAddress> joinAccountAddress = rootAccountData.join(AccountData_.addresses);

        List<Predicate> predicates = createQueryAccountsPredicates(accountDataCriteria, builder, rootAccountData, joinAccountAddress);

        queryAccountData.where(predicates.toArray(new Predicate[]{}));

        addOrderBy(accountDataCriteria, builder, queryAccountData, rootAccountData, joinAccountAddress);

        TypedQuery<AccountData> query = entityManager.createQuery(queryAccountData);
        query.setFirstResult(page.getPageNumber() * page.getPageSize());
        query.setMaxResults(page.getPageSize());

        List<AccountData> res = query.getResultList();
        long count = countQueryAccounts(accountDataCriteria, builder);

        return new PageImpl<>(res, page, count);
    }

    private List<Predicate> createQueryAccountsPredicates(AccountDataCriteria accountDataCriteria, CriteriaBuilder builder,
                                                          Root<AccountData> rootAccountData,
                                                          Join<AccountData, AccountAddress> joinAccountAddress) {
        List<Predicate> predicates = new ArrayList<>();

        if (isNotEmpty(accountDataCriteria.getAccounts())) {
            predicates.add(creatIn(builder, rootAccountData.get(AccountData_.accountId), accountDataCriteria.getAccounts()));
        }

        if (isNotEmpty(accountDataCriteria.getAddresses())) {
            predicates.add(creatIn(builder, joinAccountAddress.get(AccountAddress_.addressId), accountDataCriteria.getAddresses()));
        }

        if (isNotEmpty(accountDataCriteria.getEmail())) {
            predicates.add(builder.equal(rootAccountData.get(AccountData_.email), accountDataCriteria.getEmail()));
        }

        if (isNotEmpty(accountDataCriteria.getFirstName())) {
            if(AccountDataCriteria.ComparisonType.LIKE.equals(accountDataCriteria.getFirstNameComparisonType())){
                predicates.add(builder.like(builder.lower(rootAccountData.get(AccountData_.firstName)),
                        accountDataCriteria.getFirstName().toLowerCase()));
            } else {
                predicates.add(builder.equal(rootAccountData.get(AccountData_.firstName),
                        accountDataCriteria.getFirstName()));
            }
        }

        if (isNotEmpty(accountDataCriteria.getLastName())) {
            if(AccountDataCriteria.ComparisonType.LIKE.equals(accountDataCriteria.getLastNameComparisonType())){
                predicates.add(builder.like(builder.lower(rootAccountData.get(AccountData_.lastName)),
                        accountDataCriteria.getLastName().toLowerCase()));
            } else {
                predicates.add(builder.equal(rootAccountData.get(AccountData_.lastName),
                        accountDataCriteria.getLastName()));
            }
        }

        if (isNotEmpty(accountDataCriteria.getCity())) {
            predicates.add(builder.equal(joinAccountAddress.get(AccountAddress_.city), accountDataCriteria.getCity()));
        }

        if (isNotEmpty(accountDataCriteria.getState())) {
            predicates.add(builder.equal(joinAccountAddress.get(AccountAddress_.state), accountDataCriteria.getState()));
        }

        if (isNotEmpty(accountDataCriteria.getPostalCode())) {
            predicates.add(builder.equal(joinAccountAddress.get(AccountAddress_.postalCode), accountDataCriteria.getPostalCode()));
        }

        return predicates;
    }

    private Predicate creatIn(CriteriaBuilder builder, Path path, List list){
        CriteriaBuilder.In in = builder.in(path);
        list.forEach(i -> in.value(i));
        return in;
    }

    private void addOrderBy(AccountDataCriteria accountDataCriteria, CriteriaBuilder builder,
                            CriteriaQuery<AccountData> queryAccountData,
                            Root<AccountData> rootAccountData,
                            Join<AccountData, AccountAddress> joinAccountAddress){
        if(accountDataCriteria.getOrder() == null){
            queryAccountData.orderBy(builder.desc(rootAccountData.get(AccountData_.accountId)));
            return;
        }
        switch (accountDataCriteria.getOrder()){
            case ACCOUNT:
                queryAccountData.orderBy(builder.desc(rootAccountData.get(AccountData_.accountId)));
                break;
            case LAST_NAME:
            default:
                queryAccountData.orderBy(builder.desc(rootAccountData.get(AccountData_.lastName)));
                break;
        }
    }

    private Long countQueryAccounts(AccountDataCriteria accountDataCriteria, CriteriaBuilder builder) {
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<AccountData> rootAccountData = countQuery.from(AccountData.class);
        Join<AccountData, AccountAddress> joinAccountAddress = rootAccountData.join(AccountData_.addresses);
        countQuery.select(builder.countDistinct(rootAccountData));
        List<Predicate> predicates = createQueryAccountsPredicates(accountDataCriteria, builder, rootAccountData, joinAccountAddress);
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