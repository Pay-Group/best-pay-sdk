package com.lly835.bestpay.model;

import com.lly835.bestpay.enums.BestPayPlatformEnum;
import lombok.Data;

/**
 * 支付订单查询
 * Created by 廖师兄
 * 2018-05-31 17:52
 */
@Data
public class OrderQueryRequest {

    /**
     * 支付平台.
     */
    private BestPayPlatformEnum platformEnum;

    /**
     * 订单号(orderId 和 outOrderId 二选一，两个都传以outOrderId为准)
     */
    private String orderId = "";

    /**
     * 外部订单号(例如微信生成的)
     */
    private String outOrderId = "";
}
