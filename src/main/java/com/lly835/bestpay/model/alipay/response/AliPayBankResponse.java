package com.lly835.bestpay.model.alipay.response;

import lombok.Data;

/**
 * @author zicheng
 * @date 2020/10/14
 * Description:
 */
@Data
public class AliPayBankResponse {

    private AliPayBankResponse.AlipayFundTransUniTransferResponse alipayFundTransUniTransferResponse;

    private String sign;

    @Data
    public static class AlipayFundTransUniTransferResponse {

        private String code;

        private String msg;

        /**
         * SUCCESS：成功（对转账到银行卡的单据, 该状态可能变为退票[REFUND]状态）；
         *
         * FAIL：失败；
         *
         * DEALING：处理中；
         *
         * REFUND：退票；
         */
        private String status;

        /**
         * 商户订单号
         */
        private String outBizNo;

        /**
         * 支付宝转账订单号
         */
        private String orderId;

        /**
         * 支付宝支付资金流水号
         */
        private String payRundOrderId;
    }
}
