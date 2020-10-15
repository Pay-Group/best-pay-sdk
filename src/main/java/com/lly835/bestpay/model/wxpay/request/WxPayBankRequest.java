package com.lly835.bestpay.model.wxpay.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by 廖师兄
 * 2017-07-02 13:42
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Root(name = "xml", strict = false)
public class WxPayBankRequest {

    @Element(name = "mch_id")
    private String mchId;

    @Element(name = "nonce_str")
    private String nonceStr;

    @Element(name = "sign")
    private String sign;

    /**
     * 商户企业付款单号
     */
    @Element(name = "partner_trade_no")
    private String partnerTradeNo;

    /**
     * 收款方银行卡号
     */
    @Element(name = "enc_bank_no")
    private String encBankNo;

    /**
     * 收款方用户名
     */
    @Element(name = "enc_true_name")
    private String encTrueName;

    /**
     * 收款方开户行
     * https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=24_4&index=5
     */
    @Element(name = "bank_code")
    private String bankCode;

    /**
     * 付款金额
     */
    @Element(name = "amount")
    private Integer amount;

    /**
     * 付款说明
     */
    @Element(name = "desc")
    private String desc;
}
