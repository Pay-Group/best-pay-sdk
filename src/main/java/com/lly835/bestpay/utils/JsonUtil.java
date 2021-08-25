package com.lly835.bestpay.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Objects;

/**
 * Json Utils.
 */
public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static GsonBuilder gsonBuilder = new GsonBuilder();

    /**
     * Convert target object to json string.
     *
     * @param obj target object.
     * @return converted json string.
     */
    public static String toJson(Object obj) {
        gsonBuilder.setPrettyPrinting();
        return gsonBuilder.create().toJson(obj);
    }

    public static String toJsonWithUnderscores(Object obj) {
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create().toJson(obj);
    }

    /**
     * Convert json string to target object.
     *
     * @param json      json string.
     * @param valueType target object class type.
     * @param <T>       target class type.
     * @return converted target object.
     */
    public static <T> T toObject(String json, Class<T> valueType) {
        Objects.requireNonNull(json, "json is null.");
        Objects.requireNonNull(valueType, "value type is null.");

        try {
            return mapper.readValue(json, valueType);
        } catch (IOException e) {
            throw new IllegalStateException("fail to convert [" + json + "] to [" + valueType + "].", e);
        }
    }

}
