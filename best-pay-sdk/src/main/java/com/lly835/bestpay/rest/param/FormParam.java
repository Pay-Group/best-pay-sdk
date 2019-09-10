package com.lly835.bestpay.rest.param;

import javax.ws.rs.core.Form;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FormParam {

    private final Map<String, List<String>> formParams;

    private FormParam() {
        this.formParams = new HashMap<>();
    }

    public static FormParam build() {
        return new FormParam();
    }

    public FormParam append(String formParamName, String... formParams) {
        Objects.requireNonNull(formParamName, "FormParamName is null.");
        Objects.requireNonNull(formParams, "FormParams is null.");
        List<String> toAppendValues = Stream.of(formParams).filter(formParam -> formParam != null).collect(Collectors.toList());
        List<String> formValues = this.formParams.putIfAbsent(formParamName, toAppendValues);
        if (formValues != null) {
            formValues.addAll(toAppendValues);
        }

        return this;
    }

    public FormParam append(String formParamName, List<String> formParams) {
        Objects.requireNonNull(formParamName, "FormParamName is null.");
        Objects.requireNonNull(formParams, "FormParams is null.");
        List<String> toAppendValues = formParams.stream().filter(formParam -> formParam != null).collect(Collectors.toList());
        List<String> formValues = this.formParams.putIfAbsent(formParamName, toAppendValues);
        if (formValues != null) {
            formValues.addAll(toAppendValues);
        }

        return this;
    }

    public Form toForm() {
        Form form = new Form();
        for (String formParamName : this.formParams.keySet()) {
            List<String> formParamValues = this.formParams.get(formParamName);
            for (String formParamValue : formParamValues) {
                form.param(formParamName, formParamValue);
            }
        }

        return form;
    }

    @Override
    public String toString() {
        return this.formParams.entrySet().stream().filter(e -> e.getKey() != null && e.getValue() != null)
                .map(e -> e.getValue().stream().filter(v -> v != null).map(v -> e.getKey() + "=" + v).collect(Collectors.joining("&")))
                .collect(Collectors.joining("&", "{FormParam: ", "}"));
    }

}
