package com.lly835.bestpay.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.net.URI;

/**
 * 支付时的同步/异步返回参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PayBankResponse {
    /**
     * 商户订单号，需要保持唯一
     */
    private String orderId;

    /**
     * 代付成功后，返回的内部业务单号
     */
    private String outTradeNo;

    /**
     * 以下为支付宝字段
     * <p>
     * 支付宝支付资金流水号
     */
    private String payFundOrderId;

    /**
     * 转账单据状态。
     */
    private String status;


    /**
     * 以下为微信字段
     * <p>
     * 退款金额
     */
    private Double amount;

    /**
     * 手续费金额
     */
    private Double cmmsAmt;

    /**
     * 微信商户号
     */
    private String mchId;

    /**
     * 以下参数只有微信支付会返回 (在微信付款码支付使用)
     * returnCode returnMsg resultCode errCode errCodeDes
     */
    private String returnCode;

    private String returnMsg;

    private String resultCode;

    private String errCode;

    private String errCodeDes;
}
