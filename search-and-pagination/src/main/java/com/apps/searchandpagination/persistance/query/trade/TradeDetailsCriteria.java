package com.apps.searchandpagination.persistance.query.trade;

import com.apps.searchandpagination.persistance.entity.TradeData;
import org.joda.money.BigMoney;

import java.time.LocalDateTime;
import java.util.List;

public class TradeDetailsCriteria {

    public static final TradeDetailsCriteria EMPTY_CRITERIA = new TradeDetailsCriteria();

    private String tableId;
    private List<String> ids;
    private List<String> symbols;
    private List<String> accounts;
    private TradeData.Route route;
    private BigMoney amountGreater;
    private BigMoney amountLess;
    private LocalDateTime dateAfter;
    private LocalDateTime dateBefore;
    private String iban;
    private String firstName;
    private ComparisonType firstNameComparisonType;
    private String lastName;
    private ComparisonType lastNameComparisonType;
    private Order order;

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    public List<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }

    public TradeData.Route getRoute() {
        return route;
    }

    public void setRoute(TradeData.Route route) {
        this.route = route;
    }

    public BigMoney getAmountGreater() {
        return amountGreater;
    }

    public void setAmountGreater(BigMoney amountGreater) {
        this.amountGreater = amountGreater;
    }

    public BigMoney getAmountLess() {
        return amountLess;
    }

    public void setAmountLess(BigMoney amountLess) {
        this.amountLess = amountLess;
    }

    public LocalDateTime getDateAfter() {
        return dateAfter;
    }

    public void setDateAfter(LocalDateTime dateAfter) {
        this.dateAfter = dateAfter;
    }

    public LocalDateTime getDateBefore() {
        return dateBefore;
    }

    public void setDateBefore(LocalDateTime dateBefore) {
        this.dateBefore = dateBefore;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public ComparisonType getFirstNameComparisonType() {
        return firstNameComparisonType;
    }

    public void setFirstNameComparisonType(ComparisonType firstNameComparisonType) {
        this.firstNameComparisonType = firstNameComparisonType;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ComparisonType getLastNameComparisonType() {
        return lastNameComparisonType;
    }

    public void setLastNameComparisonType(ComparisonType lastNameComparisonType) {
        this.lastNameComparisonType = lastNameComparisonType;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public enum Order {
        SYMBOL,
        DATE,
        AMOUNT
    }

    public enum ComparisonType {
        EQUAL,
        LIKE
    }
}
