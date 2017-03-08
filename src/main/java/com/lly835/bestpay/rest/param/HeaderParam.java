package com.lly835.bestpay.rest.param;

import javax.ws.rs.client.Invocation.Builder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HeaderParam {

    private final Map<String, String> headerParams;

    private HeaderParam() {
        this.headerParams = new HashMap<>();
    }

    public static HeaderParam build() {
        return new HeaderParam();
    }

    public HeaderParam append(String headerParamName, String headerParam) {
        Objects.requireNonNull(headerParamName, "HeaderParamName is null.");
        Objects.requireNonNull(headerParam, "HeaderParam is null.");
        this.headerParams.put(headerParamName, headerParam);
        return this;
    }

    public Builder appendToRequest(Builder builder) {
        Builder newBuilder = builder;
        for (String headerParamName : this.headerParams.keySet()) {
            String headerParam = this.headerParams.get(headerParamName);
            newBuilder = newBuilder.header(headerParamName, headerParam);
        }

        return newBuilder;
    }

    @Override
    public String toString() {
        return this.headerParams.entrySet().stream().filter(e -> e.getKey() != null && e.getValue() != null)
                .map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(", ", "{HeaderParam: ", "}"));
    }

}
