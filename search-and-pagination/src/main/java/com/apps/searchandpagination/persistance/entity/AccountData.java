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

}
