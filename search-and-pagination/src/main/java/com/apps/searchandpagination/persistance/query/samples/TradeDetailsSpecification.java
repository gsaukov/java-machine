package com.apps.searchandpagination.persistance.query.samples;

import com.apps.searchandpagination.persistance.entity.TradeData_;
import com.apps.searchandpagination.persistance.entity.TradeDetails;
import com.apps.searchandpagination.persistance.entity.TradeDetails_;
import com.apps.searchandpagination.persistance.query.trade.TradeDetailsCriteria;
import com.apps.searchandpagination.persistance.query.trade.TradeDetailsDynamicQuery;
import com.apps.searchandpagination.persistance.repository.TradeDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TradeDetailsSpecification extends BasePredicate<TradeDetails> {

    @Autowired
    private TradeDetailsRepository tradeDetailsRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TradeDetailsDynamicQuery tradeDetailsQuery;

    public TradeDetailsSpecification(SearchCriteria criteria) {
        super(criteria);
    }

    public static Specification<TradeDetails> checkLastName() {
        return (root, query, cb) ->{
            return cb.equal(root.get(TradeDetails_.lastName), "");
        };
    }

    public void met(CriteriaBuilder builder){

        CriteriaQuery<TradeDetails> query = builder.createQuery(TradeDetails.class);

        Root<TradeDetails> root = query.from(TradeDetails.class);

        Predicate hasBirthday = builder.equal(root.get(TradeDetails_.firstName), "dfsf");

    }

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
        tradeDetailsQuery.queryTrades(PageRequest.of(0, 100), cr);
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

    private Predicate creatIn(CriteriaBuilder builder, Path path, List list){
        CriteriaBuilder.In in = builder.in(path);
        list.forEach(i -> in.value(i));
        return in;
    }

    public void setTradeDetailsRepository(TradeDetailsRepository tradeDetailsRepository) {
        this.tradeDetailsRepository = tradeDetailsRepository;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void setTradeDetailsQuery(TradeDetailsDynamicQuery tradeDetailsQuery) {
        this.tradeDetailsQuery = tradeDetailsQuery;
    }
}
