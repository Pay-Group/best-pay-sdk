package com.lly835.bestpay.rest.param;

import org.glassfish.jersey.uri.UriComponent;

import javax.ws.rs.client.WebTarget;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueryParam {

    private final Map<String, List<String>> queryParams;

    private QueryParam() {
        this.queryParams = new HashMap<>();
    }

    public static QueryParam build() {
        return new QueryParam();
    }

    public QueryParam append(String queryParamName, Object... queryParams) {
        Objects.requireNonNull(queryParamName, "QueryParamName is null.");
        Objects.requireNonNull(queryParams, "QueryParams are null.");
        List<String> toAppendValues = Stream.of(queryParams).filter(p -> p != null).map(Object::toString).collect(Collectors.toList());
        List<String> queryParamValues = this.queryParams.putIfAbsent(queryParamName, toAppendValues);
        if (queryParamValues != null) {
            queryParamValues.addAll(toAppendValues);
        }

        return this;
    }

    public <T> QueryParam append(String queryParamName, List<T> queryParams) {
        Objects.requireNonNull(queryParamName, "QueryParamName is null.");
        Objects.requireNonNull(queryParams, "QueryParams are null.");
        List<String> toAppendValues = queryParams.stream().filter(p -> p != null).map(Object::toString).collect(Collectors.toList());
        List<String> queryParamValues = this.queryParams.putIfAbsent(queryParamName, toAppendValues);
        if (queryParamValues != null) {
            queryParamValues.addAll(toAppendValues);
        }

        return this;
    }

    public WebTarget appendToTarget(WebTarget target) {
        WebTarget newTarget = target;
        for (String queryParamName : this.queryParams.keySet()) {
            List<String> queryParamValues = this.queryParams.get(queryParamName);
            for (String queryParamValue : queryParamValues) {
                newTarget = newTarget.queryParam(queryParamName,
                        UriComponent.encode(queryParamValue, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED));
            }
        }

        return newTarget;
    }

    @Override
    public String toString() {
        return this.queryParams.entrySet().stream().filter(e -> e.getKey() != null && e.getValue() != null)
                .map(e -> e.getValue().stream().filter(v -> v != null).map(v -> e.getKey() + "=" + v).collect(Collectors.joining("&")))
                .collect(Collectors.joining("&", "{QueryParam: ", "}"));
    }

}
