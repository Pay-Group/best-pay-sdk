package com.lly835.bestpay.model;

/**
 * 支付时的同步返回参数
 * Created by null on 2017/2/14.
 */
public class PayResponse {

    /** 订单号. */
    private String orderId;

    /** 订单金额. */
    private Double orderAmount;

    /** 重定向的url, 目前仅对支付宝PC和h5支付有用. */
    private String redirectUrl;

    /** 预支付的参数, 给app支付使用. */
    private String prePayParams;

    /** 流水号. */
    private String tradeNo;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getPrePayParams() {
        return prePayParams;
    }

    public void setPrePayParams(String prePayParams) {
        this.prePayParams = prePayParams;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }
}
