package com.apps.searchandpagination.persistance.converters;

import javax.persistence.AttributeConverter;

import static com.apps.searchandpagination.persistance.converters.JsonUtils.fromJson;
import static com.apps.searchandpagination.persistance.converters.JsonUtils.toJson;

public class JsonConverter
        implements AttributeConverter<Object, String> {

    @Override
    public String convertToDatabaseColumn(Object object) {
        if(object == null){
            return null;
        }
        return toJson(object);
    }

    @Override
    public Object convertToEntityAttribute(String json) {
        if(json == null){
            return null;
        }
        return fromJson(json, Object.class);
    }


}
