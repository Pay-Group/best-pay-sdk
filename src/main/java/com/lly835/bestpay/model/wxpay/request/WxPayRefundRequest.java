package com.lly835.bestpay.model.wxpay.request;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 退款请求参数
 * Created by 廖师兄
 * 2017-07-02 01:09
 */
@Data
@Root(name = "xml", strict = false)
public class WxPayRefundRequest {

    @Element(name = "appid")
    private String appid;

    @Element(name = "mch_id")
    private String mchId;

    @Element(name = "nonce_str")
    private String nonceStr;

    @Element(name = "sign")
    private String sign;

    @Element(name = "sign_type", required = false)
    private String signType;

    @Element(name = "transaction_id", required = false)
    private String transactionId;

    @Element(name = "out_trade_no")
    private String outTradeNo;

    @Element(name = "out_refund_no")
    private String outRefundNo;

    @Element(name = "total_fee")
    private Integer totalFee;

    @Element(name = "refund_fee")
    private Integer refundFee;

    @Element(name = "refund_fee_type", required = false)
    private String refundFeeType;

    @Element(name = "refund_desc", required = false)
    private String refundDesc;

    @Element(name = "refund_account", required = false)
    private String refundAccount;
}
