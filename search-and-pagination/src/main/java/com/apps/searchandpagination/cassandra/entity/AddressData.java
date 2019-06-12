package com.apps.searchandpagination.cassandra.entity;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;
import java.util.Map;

@Table("address_data")
public class AddressData {

    @PrimaryKey
    private AddressDataKey key;

    @Column("place_id")
    private long placeId;

    @Column("osm_type")
    private String osmType;

    @Column("osm_id")
    private String osmId;

    @Column("bounding_box")
    private Map<String, Double> boundingBox;

    @Column("polygon_points")
    private List<Polygon> polygonPoints;

    @Column("display_name")
    private String displayName;

    @Column("element_class")
    private String elementClass;

    @Column("element_type")
    private String elementType;

    @Column("address_elements")
    private Map<String, String> addressElements;

    @Column("name_details")
    private Map<String, String> nameDetails;

    @Column("place_rank")
    private int placeRank;

    @Column("importance")
    private double importance;

    @Column
    private String wkt;

    public AddressData() {
    }

    public AddressDataKey getKey() {
        return key;
    }

    public void setKey(AddressDataKey key) {
        this.key = key;
    }

    public long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public String getOsmType() {
        return osmType;
    }

    public void setOsmType(String osmType) {
        this.osmType = osmType;
    }

    public String getOsmId() {
        return osmId;
    }

    public void setOsmId(String osmId) {
        this.osmId = osmId;
    }

    public Map<String, Double> getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(Map<String, Double> boundingBox) {
        this.boundingBox = boundingBox;
    }

    public List<Polygon> getPolygonPoints() {
        return polygonPoints;
    }

    public void setPolygonPoints(List<Polygon> polygonPoints) {
        this.polygonPoints = polygonPoints;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getElementClass() {
        return elementClass;
    }

    public void setElementClass(String elementClass) {
        this.elementClass = elementClass;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public Map<String, String> getAddressElements() {
        return addressElements;
    }

    public void setAddressElements(Map<String, String> addressElements) {
        this.addressElements = addressElements;
    }

    public Map<String, String> getNameDetails() {
        return nameDetails;
    }

    public void setNameDetails(Map<String, String> nameDetails) {
        this.nameDetails = nameDetails;
    }

    public int getPlaceRank() {
        return placeRank;
    }

    public void setPlaceRank(int placeRank) {
        this.placeRank = placeRank;
    }

    public double getImportance() {
        return importance;
    }

    public void setImportance(double importance) {
        this.importance = importance;
    }

    public String getWkt() {
        return wkt;
    }

    public void setWkt(String wkt) {
        this.wkt = wkt;
    }

    private AddressData(Builder builder) {
        this.key = builder.key;
        this.osmId = builder.osmId;
        this.elementType = builder.elementType;
        this.displayName = builder.displayName;
        this.addressElements = builder.addressElements;
        this.placeRank = builder.placeRank;
        this.boundingBox = builder.boundingBox;
        this.elementClass = builder.elementClass;
        this.importance = builder.importance;
        this.polygonPoints = builder.polygonPoints;
        this.wkt = builder.wkt;
        this.osmType = builder.osmType;
        this.nameDetails = builder.nameDetails;
        this.placeId = builder.placeId;
    }

    public static final class Builder {
        private AddressDataKey key;
        private long placeId;
        private String osmType;
        private String osmId;
        private Map<String, Double> boundingBox;
        private List<Polygon> polygonPoints;
        private String displayName;
        private String elementClass;
        private String elementType;
        private Map<String, String> addressElements;
        private Map<String, String> nameDetails;
        private int placeRank;
        private double importance;
        private String wkt;

        public Builder withKey(AddressDataKey key) {
            this.key = key;
            return this;
        }

        public Builder withPlaceId(long placeId) {
            this.placeId = placeId;
            return this;
        }

        public Builder withOsmType(String osmType) {
            this.osmType = osmType;
            return this;
        }

        public Builder withOsmId(String osmId) {
            this.osmId = osmId;
            return this;
        }

        public Builder withBoundingBox(Map<String, Double> boundingBox) {
            this.boundingBox = boundingBox;
            return this;
        }

        public Builder withPolygonPoints(List<Polygon> polygonPoints) {
            this.polygonPoints = polygonPoints;
            return this;
        }

        public Builder withDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder withElementClass(String elementClass) {
            this.elementClass = elementClass;
            return this;
        }

        public Builder withElementType(String elementType) {
            this.elementType = elementType;
            return this;
        }

        public Builder withAddressElements(Map<String, String> addressElements) {
            this.addressElements = addressElements;
            return this;
        }

        public Builder withNameDetails(Map<String, String> nameDetails) {
            this.nameDetails = nameDetails;
            return this;
        }

        public Builder withPlaceRank(int placeRank) {
            this.placeRank = placeRank;
            return this;
        }

        public Builder withImportance(double importance) {
            this.importance = importance;
            return this;
        }

        public Builder withWkt(String wkt) {
            this.wkt = wkt;
            return this;
        }

        public AddressData build() {
            return new AddressData(this);
        }
    }
}
