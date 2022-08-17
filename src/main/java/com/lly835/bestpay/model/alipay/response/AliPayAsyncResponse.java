package com.lly835.bestpay.model.alipay.response;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by this on 2019/9/13 2:36
 */
@Data
public class AliPayAsyncResponse implements Serializable {


    /**
     * 通知时间
     */
    private String notifyTime;

    /**
     * 	通知的类型
     */
    private String notifyType;

    /**
     * 通知校验ID
     */
    private String notifyId;

    /**
     * 编码格式
     */
    private String charset;

    /**
     * 接口版本
     */
    private String version;

    /**
     * 签名类型
     */
    private String signType;

    /**
     * 签名
     */
    private String sign;

    /**
     * 授权方的app_id
     */
    private String authAppId;

    /**
     * 支付宝交易号
     */
    private  String tradeNo;

    /**
     * APP_ID
     */
    private String appId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 交易状态
     */
    private String tradeStatus;

    /**
     * 订单金额
     */
    private String totalAmount;

    /**
     * 实收金额
     */
    private String receiptAmount;

    /**
     * 付款金额
     */
    private String buyerPayAmount;

    /**
     * 订单标题
     */
    private String subject;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 交易创建时间
     */
    private String gmtCreate;

    /**
     * 交易结束时间
     */
    private String gmtClose;

    /**
     * 附加/回传参数
     */
    private String passbackParams;
}
