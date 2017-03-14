package com.lly835.bestpay.model;

import java.util.HashMap;
import java.util.Map;

public class AlipayBizParam {

    private String body;
    private String subject;
    private String outTradeNo;
    private String timeoutExpress;
    private String totalAmount;
    private String sellerId;
    private String productCode;
    private String goodsType;
    private String passbackParams;
    private String promoParams;
    private String extendParams;
    private String enablePayChannels;
    private String disablePayChannels;
    private String storeId;

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOutTradeNo() {
        return this.outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTimeoutExpress() {
        return this.timeoutExpress;
    }

    public void setTimeoutExpress(String timeoutExpress) {
        this.timeoutExpress = timeoutExpress;
    }

    public String getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSellerId() {
        return this.sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getGoodsType() {
        return this.goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getPassbackParams() {
        return this.passbackParams;
    }

    public void setPassbackParams(String passbackParams) {
        this.passbackParams = passbackParams;
    }

    public String getPromoParams() {
        return this.promoParams;
    }

    public void setPromoParams(String promoParams) {
        this.promoParams = promoParams;
    }

    public String getExtendParams() {
        return this.extendParams;
    }

    public void setExtendParams(String extendParams) {
        this.extendParams = extendParams;
    }

    public String getEnablePayChannels() {
        return this.enablePayChannels;
    }

    public void setEnablePayChannels(String enablePayChannels) {
        this.enablePayChannels = enablePayChannels;
    }

    public String getDisablePayChannels() {
        return this.disablePayChannels;
    }

    public void setDisablePayChannels(String disablePayChannels) {
        this.disablePayChannels = disablePayChannels;
    }

    public String getStoreId() {
        return this.storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Map<String, String> getBizParam() {
        Map<String, String> bizParam = new HashMap<>();
        bizParam.put("body", this.body);
        bizParam.put("subject", this.subject);
        bizParam.put("out_trade_no", this.outTradeNo);
        bizParam.put("timeout_express", this.timeoutExpress);
        bizParam.put("total_amount", this.totalAmount);
        bizParam.put("seller_id", this.sellerId);
        bizParam.put("product_code", this.productCode);
        bizParam.put("goods_type", this.goodsType);
        bizParam.put("passback_params", this.passbackParams);
        bizParam.put("promo_params", this.promoParams);
        bizParam.put("extend_params", this.extendParams);
        bizParam.put("enable_pay_channels", this.enablePayChannels);
        bizParam.put("disable_pay_channels", this.disablePayChannels);
        bizParam.put("store_id", this.storeId);
        return bizParam;
    }

}
