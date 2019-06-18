package com.apps.searchandpagination.service.account.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressJson {
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private Double longitude;
    private Double latitude;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("coordinates")
    private void unpackNameFromNestedObject(Map<String, Double> coordinates) {
        this.longitude = coordinates.get("lng");
        this.latitude = coordinates.get("lat");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(" ").append(street);
        sb.append(" ").append(city);
        sb.append(" ").append(state);
        sb.append(" ").append(postalCode);
        return sb.toString();
    }
}
