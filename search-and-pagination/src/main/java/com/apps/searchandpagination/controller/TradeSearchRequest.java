package com.apps.searchandpagination.controller;

public class TradeSearchRequest {
    private String ids;
    private String symbols;
    private String accounts;
    private String route;
    private String amountGreater;
    private String amountLess;
    private String currency;
    private String dateAfter;
    private String dateBefore;
    private String iban;
    private String firstName;
    private String firstNameComparisonType;
    private String lastName;
    private String lastNameComparisonType;
    private String itemsSize;
    private String order;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getSymbols() {
        return symbols;
    }

    public void setSymbols(String symbols) {
        this.symbols = symbols;
    }

    public String getAccounts() {
        return accounts;
    }

    public void setAccounts(String accounts) {
        this.accounts = accounts;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getAmountGreater() {
        return amountGreater;
    }

    public void setAmountGreater(String amountGreater) {
        this.amountGreater = amountGreater;
    }

    public String getAmountLess() {
        return amountLess;
    }

    public void setAmountLess(String amountLess) {
        this.amountLess = amountLess;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDateAfter() {
        return dateAfter;
    }

    public void setDateAfter(String dateAfter) {
        this.dateAfter = dateAfter;
    }

    public String getDateBefore() {
        return dateBefore;
    }

    public void setDateBefore(String dateBefore) {
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

    public String getFirstNameComparisonType() {
        return firstNameComparisonType;
    }

    public void setFirstNameComparisonType(String firstNameComparisonType) {
        this.firstNameComparisonType = firstNameComparisonType;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastNameComparisonType() {
        return lastNameComparisonType;
    }

    public void setLastNameComparisonType(String lastNameComparisonType) {
        this.lastNameComparisonType = lastNameComparisonType;
    }

    public String getItemsSize() {
        return itemsSize;
    }

    public void setItemsSize(String itemsSize) {
        this.itemsSize = itemsSize;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

}
