package com.apps.searchandpagination.persistance.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Access(AccessType.FIELD)
@Table(name = "ACCOUNT_ADDRESS")
public class AccountAddress {

    @Id
    @Column(name = "ADDRESS_ID")
    private String addressId;
    @Column(name = "ACCOUNT_ID")
    private String accountId;
    @Column(name = "STREET")
    private String street;
    @Column(name = "CITY")
    private String city;
    @Column(name = "STATE")
    private String state;
    @Column(name = "POSTAL_CODE")
    private String postalCode;
    @Column(name = "LONGITUDE")
    private Double longitude;
    @Column(name = "LATITUDE")
    private Double latitude;

}
