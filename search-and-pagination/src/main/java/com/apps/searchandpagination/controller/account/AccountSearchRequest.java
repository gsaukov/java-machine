package com.apps.searchandpagination.controller.account;

public class AccountSearchRequest {

    private String accounts;
    private String addresses;
    private String email;
    private String firstName;
    private String firstNameComparisonType;
    private String lastName;
    private String lastNameComparisonType;
    private String city;
    private String state;
    private String postalCode;
    private String itemsSize;
    private String order;

    public String getAccounts() {
        return accounts;
    }

    public void setAccounts(String accounts) {
        this.accounts = accounts;
    }

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
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
