package com.lly835.bestpay.model;

import com.lly835.bestpay.enums.BestPayResultEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.exception.BestPayException;
import org.apache.commons.lang3.StringUtils;

/**
 * 支付时请求参数
 * Created by null on 2017/2/14.
 */
public class PayRequest {
    /** 订单号. */
    private String orderId;

    /** 订单金额. */
    private Double orderAmount;

    /** 订单名字. */
    private String orderName;

    /** 支付方式. */
    private BestPayTypeEnum payTypeEnum;

    /** 异步通知地址. */
    private String notifyUrl;

    /** 同步返回地址. */
    private String returnUrl;

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

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public BestPayTypeEnum getPayTypeEnum() {
        return payTypeEnum;
    }

    public void setPayTypeEnum(BestPayTypeEnum payTypeEnum) {
        this.payTypeEnum = payTypeEnum;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public Boolean check() {
        if (StringUtils.isEmpty(orderId)) {
            throw new BestPayException(BestPayResultEnum.PARAM_ERROR);
        }
        if (orderAmount == null || orderAmount <= 0) {
            throw new BestPayException(BestPayResultEnum.PARAM_ERROR);
        }
        if (StringUtils.isEmpty(orderName)) {
            throw new BestPayException(BestPayResultEnum.PARAM_ERROR);
        }
        if (payTypeEnum == null) {
            throw new BestPayException(BestPayResultEnum.PARAM_ERROR);
        }
        if (StringUtils.isEmpty(notifyUrl)) {
            throw new BestPayException(BestPayResultEnum.PARAM_ERROR);
        }
        return true;
    }
}
