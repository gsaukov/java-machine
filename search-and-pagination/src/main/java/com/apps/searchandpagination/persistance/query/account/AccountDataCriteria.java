package com.apps.searchandpagination.persistance.query.account;

import java.util.List;

public class AccountDataCriteria {

    public static final AccountDataCriteria EMPTY_CRITERIA = new AccountDataCriteria();

    private List<String> accounts;
    private List<String> addresses;
    private String email;
    private String firstName;
    private ComparisonType firstNameComparisonType;
    private String lastName;
    private ComparisonType lastNameComparisonType;
    private String city;
    private String state;
    private String postalCode;
    private Order order;

    public List<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public enum Order {
        ACCOUNT,
        CITY,
        LAST_NAME
    }

    public enum ComparisonType {
        EQUAL,
        LIKE
    }
}
