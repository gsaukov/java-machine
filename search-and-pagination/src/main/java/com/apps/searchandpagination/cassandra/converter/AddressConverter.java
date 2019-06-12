package com.apps.searchandpagination.cassandra.converter;

import com.apps.searchandpagination.cassandra.entity.AddressData;
import com.apps.searchandpagination.cassandra.entity.AddressDataKey;
import com.apps.searchandpagination.cassandra.entity.Polygon;
import fr.dudie.nominatim.model.Address;
import fr.dudie.nominatim.model.BoundingBox;
import fr.dudie.nominatim.model.PolygonPoint;

import java.util.*;
import java.util.stream.Collectors;

public class AddressConverter {

    public static AddressData toAddressData (String accountId, String addressId, Address address){
        AddressDataKey addressDataKey = new AddressDataKey.Builder()
                .withAccountId(accountId)
                .withAddressId(addressId)
                .withLatitude(address.getLatitude())
                .withLongitude(address.getLongitude())
                .build();

        AddressData addressData = new AddressData.Builder()
                .withKey(addressDataKey)
                .withAddressElements(toAddressElements(address))
                .withBoundingBox(toBoundingDox(address))
                .withDisplayName(address.getDisplayName())
                .withElementClass(address.getElementClass())
                .withElementType(address.getElementType())
                .withImportance(address.getImportance())
                .withNameDetails(toNameDetails(address))
                .withOsmId(address.getOsmId())
                .withOsmType(address.getOsmType())
                .withPlaceId(address.getPlaceId())
                .withPlaceRank(address.getPlaceRank())
                .withPolygonPoints(toPolygonPoints(address))
                .withWkt(address.getWkt())
                .build();

        return addressData;
    }

    private static Map<String, String> toAddressElements(Address address) {
        if(address.getAddressElements() == null){
            return null;
        } else {
            return Arrays.stream(address.getAddressElements())
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
        }
    }

    private static Map<String, Double> toBoundingDox(Address address) {
        Map<String, Double> res = new HashMap<>();
        if(address.getBoundingBox() != null){
            BoundingBox boundingBox = address.getBoundingBox();
            res.put("east", boundingBox.getEast());
            res.put("north", boundingBox.getNorth());
            res.put("south", boundingBox.getSouth());
            res.put("west", boundingBox.getWest());
        }
        return res;
    }

    private static Map<String, String> toNameDetails(Address address) {
        if(address.getNameDetails() == null){
            return null;
        } else {
            return Arrays.stream(address.getNameDetails())
                    .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
        }
    }

    private static List<Polygon> toPolygonPoints(Address address) {
        if(address.getPolygonPoints() == null){
            return null;
        } else {
            List<Polygon> res = new ArrayList<>();
            for(PolygonPoint polygonPoint : address.getPolygonPoints()){
                res.add(new Polygon(polygonPoint.getLatitude(), polygonPoint.getLongitude()));
            }
            return res;
        }
    }

}
