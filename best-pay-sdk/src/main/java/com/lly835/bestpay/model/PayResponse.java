package com.lly835.bestpay.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.net.URI;

/**
 * 支付时的同步返回参数
 */
@Data
public class PayResponse {

    private String prePayParams;

    private URI payUri;

    /** 以下字段仅在微信h5支付返回. */
    private String appId;

    private String timeStamp;

    private String nonceStr;

    @JsonProperty("package")
    private String packAge;

    private String signType;

    private String paySign;

    /** 以下字段在微信异步通知下返回. */
    private Double orderAmount;

    private String orderId;

    //第三方支付的流水号
    private String outTradeNo;

    /** 以下支付是h5支付返回*/
    private String mwebUrl;

    /**
     * AliPay  pc网站支付返回的body体，html 可直接嵌入网页使用
     */
    private String body;
}
