package com.apps.cloud.common.data.converter;

import javax.persistence.AttributeConverter;

import static com.apps.cloud.common.data.converter.JsonUtils.fromJson;
import static com.apps.cloud.common.data.converter.JsonUtils.toJson;

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
