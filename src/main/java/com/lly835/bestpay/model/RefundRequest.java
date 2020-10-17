package com.lly835.bestpay.model;

import com.lly835.bestpay.enums.BestPayPlatformEnum;
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
    private BestPayPlatformEnum payPlatformEnum;

    /**
     * 订单号.
     */
    private String orderId;

    /**
     * 退款单号
     * 标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
     */
    private String refundNo;

    /**
     * 订单金额.
     * 微信退款需要
     */
    private Double orderAmount;

    /**
     * 退款金额
     */
    private Double refundAmount;

    /**
     * 退款原因
     */
    private String refundReason;
}
