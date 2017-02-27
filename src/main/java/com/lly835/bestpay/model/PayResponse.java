package com.lly835.bestpay.model;

import java.util.Date;

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

    /** 买家支付宝用户号, 买家支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字. */
    private String buyerId;

    /** 买家支付宝账号, 买家支付宝账号, 邮箱或者手机号. */
    private String buyerLogonId;

    /** 支付时间. */
    private Date payTime;

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

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
}
