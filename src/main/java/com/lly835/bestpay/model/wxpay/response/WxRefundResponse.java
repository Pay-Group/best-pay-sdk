package com.lly835.bestpay.model.wxpay.response;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 微信退款返回参数
 * Created by 廖师兄
 * 2017-07-02 13:33
 */
@Data
@Root(name = "xml", strict = false) //name:要解析的xml数据的头部
public class WxRefundResponse {

    @Element(name = "return_code")
    private String returnCode;

    @Element(name = "return_msg", required = false)
    private String returnMsg;

    /** 以下字段在return_code为SUCCESS的时候有返回. */
    @Element(name = "result_code", required = false)
    private String resultCode;

    @Element(name = "err_code", required = false)
    private String errCode;

    @Element(name = "err_code_des", required = false)
    private String errCodeDes;

    @Element(name = "appid", required = false)
    private String appid;

    @Element(name = "mch_id", required = false)
    private String mchId;

    @Element(name = "nonce_str", required = false)
    private String nonceStr;

    @Element(name = "sign", required = false)
    private String sign;

    @Element(name = "transaction_id", required = false)
    private String transactionId;

    @Element(name = "out_trade_no", required = false)
    private String outTradeNo;

    @Element(name = "out_refund_no", required = false)
    private String outRefundNo;

    @Element(name = "refund_id", required = false)
    private String refundId;

    @Element(name = "refund_fee", required = false)
    private Integer refundFee;

    @Element(name = "settlement_refund_fee", required = false)
    private Integer settlementRefundFee;

    @Element(name = "total_fee", required = false)
    private Integer totalFee;

    @Element(name = "settlement_total_fee", required = false)
    private Integer settlementTotalFee;

    @Element(name = "fee_type", required = false)
    private String feeType;

    @Element(name = "cash_fee", required = false)
    private Integer cashFee;

    @Element(name = "cash_fee_type", required = false)
    private String cashFeeType;

    @Element(name = "cash_refund_fee", required = false)
    private Integer cashRefundFee;

    @Element(name = "coupon_refund_fee", required = false)
    private Integer couponRefundFee;

    @Element(name = "coupon_refund_count", required = false)
    private Integer couponRefundCount;
}
