package com.lly835.bestpay.model;

import lombok.Data;

import java.net.URI;

/**
 * 支付时的同步返回参数
 */
@Data
public class PayResponse {

    private String prePayParams;
    private URI payUri;

    public String getPrePayParams() {
        return this.prePayParams;
    }

    public void setPrePayParams(String prePayParams) {
        this.prePayParams = prePayParams;
    }

    public URI getPayUri() {
        return this.payUri;
    }

    public void setPayUri(URI payUri) {
        this.payUri = payUri;
    }

}
