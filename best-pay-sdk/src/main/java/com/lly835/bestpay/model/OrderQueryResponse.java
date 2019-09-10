package com.lly835.bestpay.model;

import com.lly835.bestpay.enums.OrderStatusEnum;
import lombok.Builder;
import lombok.Data;

/**
 * 订单查询结果
 * Created by 廖师兄
 * 2018-06-04 16:52
 */
@Data
@Builder
public class OrderQueryResponse {

    /**
     * 订单状态
     */
    private OrderStatusEnum orderStatusEnum;

    /**
     * 错误原因
     */
    private String resultMsg;
}
