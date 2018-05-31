package com.lly835.bestpay.model;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.Data;

/**
 * 支付订单查询
 * Created by 廖师兄
 * 2018-05-31 17:52
 */
@Data
public class OrderQueryRequest {

    /**
     * 支付方式.
     */
    private BestPayTypeEnum payTypeEnum;

    private String orderId;
}
