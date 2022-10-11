package com.apps.searchandpagination.cassandra.entity;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@UserDefinedType("polygon")
public class Polygon {

    @CassandraType(type = CassandraType.Name.DOUBLE)
    private Double latitude;
    @CassandraType(type = CassandraType.Name.DOUBLE)
    private Double longitude;

    public Polygon() {
    }

    public Polygon(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
