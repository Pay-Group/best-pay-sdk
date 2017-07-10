package com.lly835.bestpay.model.wxpay.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 退款请求参数
 * Created by 廖师兄
 * 2017-07-02 01:09
 */
@XStreamAlias("xml")
@Data
public class WxPayRefundRequest {
    private String appid;

    @XStreamAlias("mch_id")
    private String mchId;

    @XStreamAlias("nonce_str")
    private String nonceStr;

    private String sign;

    @XStreamAlias("sign_type")
    private String signType;

    @XStreamAlias("transaction_id")
    private String transactionId;

    @XStreamAlias("out_trade_no")
    private String outTradeNo;

    @XStreamAlias("out_refund_no")
    private String outRefundNo;

    @XStreamAlias("total_fee")
    private Integer totalFee;

    @XStreamAlias("refund_fee")
    private Integer refundFee;

    @XStreamAlias("refund_fee_type")
    private String refundFeeType;

    @XStreamAlias("refund_desc")
    private String refundDesc;

    @XStreamAlias("refund_account")
    private String refundAccount;
}
