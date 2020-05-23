package com.lly835.bestpay.enums;

import lombok.Getter;

import static com.lly835.bestpay.enums.OrderStatusEnum.*;

/**
 * 支付宝交易状态枚举.
 * @version 1.0 2017/2/27
 * @auther <a href="mailto:lly835@163.com">廖师兄</a>
 * @since 1.0
 */
@Getter
public enum AlipayTradeStatusEnum {

    /** 交易创建，等待买家付款。 */
    WAIT_BUYER_PAY(NOTPAY),

    /**
     * <pre>
     * 在指定时间段内未支付时关闭的交易；
     * 在交易完成全额退款成功时关闭的交易。
     * </pre>
     */
    TRADE_CLOSED(CLOSED),

    /** 交易成功，且可对该交易做操作，如：多级分润、退款等。 */
    TRADE_SUCCESS(SUCCESS),

    /** 等待卖家收款（买家付款后，如果卖家账号被冻结）。 */
    TRADE_PENDING(NOTPAY),

    /** 交易成功且结束，即不可再做任何操作。 */
    TRADE_FINISHED(SUCCESS),

    ;

    private OrderStatusEnum orderStatusEnum;

    AlipayTradeStatusEnum(OrderStatusEnum orderStatusEnum) {
        this.orderStatusEnum = orderStatusEnum;
    }

    public static AlipayTradeStatusEnum findByName(String name) {
        for (AlipayTradeStatusEnum statusEnum : AlipayTradeStatusEnum.values()) {
            if (name.toLowerCase().equals(statusEnum.name().toLowerCase())) {
                return statusEnum;
            }
        }
        throw new RuntimeException("错误的支付宝支付状态");
    }
}
