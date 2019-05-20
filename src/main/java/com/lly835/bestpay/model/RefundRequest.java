package com.lly835.bestpay.model;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.Data;

/**
 * 支付时请求参数
 */
@Data
public class RefundRequest {

    /**
     * 支付方式.
     */
    private BestPayTypeEnum payTypeEnum;

    /**
     * 订单号.
     */
    private String orderId;

    /**
     * 订单金额.
     */
    private Double orderAmount;

    /**
     * 子订单退款号
     */
    private String orderChildRefundId;

    /**
     * 订单总金额.
     */
    private Double totalOrderAmount;
}
