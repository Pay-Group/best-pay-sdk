package com.lly835.bestpay.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Json Utils.
 */
public class Json {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Convert target object to json string.
     *
     * @param obj target object.
     * @return converted json string.
     */
    public static String getJsonFromObject(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Fail to convert '" + obj + "' to String.", e);
        }
    }

    /**
     * Convert json string to target object.
     *
     * @param json      json string.
     * @param valueType target object class type.
     * @param <T>       target class type.
     * @return converted target object.
     */
    public static <T> T getObjectFromJson(String json, Class<T> valueType) {
        if (json == null) {
            throw new NullPointerException("Param json is null.");
        }

        if (valueType == null) {
            throw new NullPointerException("Param valueType is null.");
        }

        try {
            return mapper.readValue(json, valueType);
        } catch (IOException e) {
            throw new IllegalStateException("Fail to convert '" + json + "' to " + valueType + ".", e);
        }
    }

}
