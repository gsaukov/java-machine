package com.apps.cloud.common.data.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;


public class JsonUtils {

    private final static ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); // Do not fail if no accessors are found.
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL); // Do not serialize [null] values.
        MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    public static String toJson(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (final IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (final IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> valueTypeRef) {
        try {
            return MAPPER.readValue(json, valueTypeRef);
        } catch (final IOException exc) {
            throw new RuntimeException(exc);
        }
    }

}

