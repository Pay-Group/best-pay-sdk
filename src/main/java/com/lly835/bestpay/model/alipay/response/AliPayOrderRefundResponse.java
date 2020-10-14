package com.lly835.bestpay.model.alipay.response;

import lombok.Data;

/**
 * @author zicheng
 * @date 2020/10/14
 * Description:
 */
@Data
public class AliPayOrderRefundResponse {

    private AliPayOrderRefundResponse.AlipayTradeRefundResponse alipayTradeRefundResponse;

    private String sign;

    @Data
    public static class AlipayTradeRefundResponse {

        private String code;

        private String msg;

        private String subCode;

        private String subMsg;

        /**
         * 支付宝交易号
         */
        private String tradeNo;

        /**
         * 商家订单号
         */
        private String outTradeNo;

        /**
         * 买家支付宝账号
         */
        private String buyerLogonId;

        /**
         * 本次退款是否发生了资金变化
         */
        private String fundChange;

        /**
         * 退款总金额 单位为元
         */
        private Double refundFee;

        /**
         * 退款支付时间	 2014-11-27 15:45:57
         */
        private String gmtRefundPay;

        /**
         * 买家在支付宝的用户id
         */
        private String buyerUserId;
    }
}
