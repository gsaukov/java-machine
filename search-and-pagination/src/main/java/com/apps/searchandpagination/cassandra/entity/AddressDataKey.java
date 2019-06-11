package com.apps.searchandpagination.cassandra.entity;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class AddressDataKey {
    @PrimaryKeyColumn(
            name = "address_id",
            ordinal = 1,
            type = PrimaryKeyType.PARTITIONED,
            ordering = Ordering.DESCENDING)
    private String addressId;

    @PrimaryKeyColumn(
            name = "account_id",
            ordinal = 2,
            type = PrimaryKeyType.PARTITIONED,
            ordering = Ordering.DESCENDING)
    private String accountId;

    @PrimaryKeyColumn(
            name = "longitude",
            ordinal = 3,
            type = PrimaryKeyType.PARTITIONED,
            ordering = Ordering.DESCENDING)
    private double longitude;

    @PrimaryKeyColumn(
            name = "latitude",
            ordinal = 4,
            type = PrimaryKeyType.PARTITIONED,
            ordering = Ordering.DESCENDING)
    private double latitude;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public AddressDataKey() {
    }

    private AddressDataKey(Builder builder) {
        this.accountId = builder.accountId;
        this.longitude = builder.longitude;
        this.addressId = builder.addressId;
        this.latitude = builder.latitude;
    }

    public static final class Builder {
        private String addressId;
        private String accountId;
        private double longitude;
        private double latitude;

        public Builder() {
        }

        public Builder withAddressId(String addressId) {
            this.addressId = addressId;
            return this;
        }

        public Builder withAccountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder withLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder withLatitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public AddressDataKey build() {
            return new AddressDataKey(this);
        }
    }
}
