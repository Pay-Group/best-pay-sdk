package com.lly835.bestpay.enums;

/**
 * 支付宝交易状态枚举.
 * @version 1.0 2017/2/27
 * @auther <a href="mailto:lly835@163.com">廖师兄</a>
 * @since 1.0
 */
public enum AlipayTradeStatusEnum {

    /** 交易创建，等待买家付款。 */
    WAIT_BUYER_PAY,

    /**
     * <pre>
     * 在指定时间段内未支付时关闭的交易；
     * 在交易完成全额退款成功时关闭的交易。
     * </pre>
     */
    TRADE_CLOSED,

    /** 交易成功，且可对该交易做操作，如：多级分润、退款等。 */
    TRADE_SUCCESS,

    /** 等待卖家收款（买家付款后，如果卖家账号被冻结）。 */
    TRADE_PENDING,

    /** 交易成功且结束，即不可再做任何操作。 */
    TRADE_FINISHED,

    ;
}
