package com.lly835.bestpay.model;

import com.lly835.bestpay.enums.BestPayTypeEnum;

/**
 * 支付时请求参数
 */
public class PayRequest {

    private BestPayTypeEnum payTypeEnum;
    private AlipayBizParam alipayBizParam;

    public BestPayTypeEnum getPayTypeEnum() {
        return this.payTypeEnum;
    }

    public void setPayTypeEnum(BestPayTypeEnum payTypeEnum) {
        this.payTypeEnum = payTypeEnum;
    }

    public AlipayBizParam getAlipayBizParam() {
        return this.alipayBizParam;
    }

    public void setAlipayBizParam(AlipayBizParam alipayBizParam) {
        this.alipayBizParam = alipayBizParam;
    }

}
