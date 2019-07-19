package com.apps.searchandpagination.service.account;

import com.apps.searchandpagination.persistance.entity.AccountData;
import com.apps.searchandpagination.persistance.query.account.AccountDataCriteria;
import com.apps.searchandpagination.persistance.query.account.AccountDataDynamicQuery;
import com.apps.searchandpagination.persistance.repository.AccountAddressRepository;
import com.apps.searchandpagination.persistance.repository.AccountDataRepository;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AccountDataService {

    @Autowired
    private AccountDataRepository accountDataRepository;

    @Autowired
    private AccountAddressRepository accountAddressRepository;

    @Autowired
    private AccountDataDynamicQuery accountDetailsDynamicQuery;

    public Page<AccountData> findAccounts(Pageable page, Optional<AccountDataCriteria> criteria) {
        return accountDetailsDynamicQuery.queryAccounts(page, criteria.orElse(AccountDataCriteria.EMPTY_CRITERIA));
    }

    public AccountData getAccountData(String accountId) {
        return accountDataRepository.findById(accountId).get();
    }

    public List<String> findAllCities() {
        return accountAddressRepository.findAllCities();
    }

    public List<String> findAllStates() {
        return accountAddressRepository.findAllStates();
    }

    public String getAccountVatData(String accountId) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < RandomUtils.nextInt(1, 7); i++){
            getRandomHtml(sb, RandomUtils.nextInt(3, 15));
        }
        return sb.toString();
    }

    private void getRandomHtml(StringBuilder sb, int words) {
        if(RandomUtils.nextInt(1, 3)%2 == 0){
            int h = RandomUtils.nextInt(1, 3);
            sb.append("<h"+h+">" + RandomStringUtils.randomAlphabetic(1, 10).toUpperCase() + "</h"+h+">");
        }
        sb.append("<p>");
        for(int i = 0; i < words; i++){
            sb.append(RandomStringUtils.randomAlphabetic(1, 30));
            if(RandomUtils.nextInt(1, 15)%15 == 0){
                sb.append("<br>");
            }else{
                sb.append(" ");
            }
        }
        sb.append("</p>");
    }

    public File getAccountPerformance(String accountId) {
        return convertToCSV(accountId,  getAccountPerformanceData());
    }

    private List<AccountPerformance> getAccountPerformanceData() {
        List<AccountPerformance> accountPerformanceData = new ArrayList<>();
        int year = RandomUtils.nextInt(1980, 2019);
        int asset = RandomUtils.nextInt(0, 5000);
        for(int i = 0; i < RandomUtils.nextInt(1, 15); i++){
            int change = RandomUtils.nextInt(0, 200);
            boolean minus = RandomUtils.nextBoolean();
            if(minus){
                asset = asset - change;
            } else {
                asset = asset + change;
            }
            accountPerformanceData.add(new AccountPerformance(String.valueOf(year++), asset));
        }
        return accountPerformanceData;
    }

    public File convertToCSV(String accountId, List<AccountPerformance> data) {
        File file = new File(accountId +"_performace.csv");
        try (PrintWriter writer = new PrintWriter(file)) {
            StringBuilder sb = new StringBuilder();

            sb.append("year");
            sb.append(",");
            sb.append(accountId + " assets");
            sb.append('\n');
            for(AccountPerformance performance : data){
                sb.append(performance.getYear());
                sb.append(',');
                sb.append(performance.getAssets());
                sb.append('\n');
            }

            writer.write(sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }
}