package com.apps.searchandpagination.persistance.entity;

import org.joda.money.BigMoney;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Access(AccessType.FIELD)
@Table(name = "ACCOUNT_DATA")
public class AccountData {

    @Id
    @Column(name = "ACCOUNT_ID")
    private String accountId;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ORGANIZATION")
    private String organization;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "OCCUPATION")
    private String occupation;

    @JoinColumn(name = "ACCOUNT_ID")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountAddress> addresses;

    @Column(name = "LIMITATIONS")
    private Boolean limitations;

    @Column(name = "VERIFIED_FOR_ACH")
    private Boolean verifiedforACH;

    @Column(name = "PAYMENTS")
    private String payments;

    @Column(name = "VAT_NUMBER")
    private Integer vatNumber;

    @Embedded
    @AttributeOverrides(
            { @AttributeOverride(name = "amount", column = @Column(name = "LIMIT_AMOUNT")),
              @AttributeOverride(name = "currency", column = @Column(name = "LIMIT_CURRENCY")) })
    private EmbeddableBigMoney maximumLimit;

    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    public AccountData() {
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public List<AccountAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AccountAddress> addresses) {
        this.addresses = addresses;
    }

    public Boolean getLimitations() {
        return limitations;
    }

    public void setLimitations(Boolean limitations) {
        this.limitations = limitations;
    }

    public Boolean getVerifiedforACH() {
        return verifiedforACH;
    }

    public void setVerifiedforACH(Boolean verifiedforACH) {
        this.verifiedforACH = verifiedforACH;
    }

    public String getPayments() {
        return payments;
    }

    public void setPayments(String payments) {
        this.payments = payments;
    }

    public Integer getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(Integer vatNumber) {
        this.vatNumber = vatNumber;
    }

    public EmbeddableBigMoney getMaximumLimit() {
        return maximumLimit;
    }

    public void setMaximumLimit(EmbeddableBigMoney maximumLimit) {
        this.maximumLimit = maximumLimit;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
