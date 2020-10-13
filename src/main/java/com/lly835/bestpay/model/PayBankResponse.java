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

    private String returnCode;

    private String returnMsg;

    private String resultCode;

    private String errCode;

    private String errCodeDes;

    private String mchId;

    /**
     * 商户订单号，需要保持唯一
     */
    private String partnerTradeNo;

    private Double amount;

    private String nonceStr;

    private String sign;

    /**
     * 代付成功后，返回的内部业务单号
     */
    private String paymentNo;

    /**
     * 手续费金额
     */
    private Double cmmsAmt;
}
