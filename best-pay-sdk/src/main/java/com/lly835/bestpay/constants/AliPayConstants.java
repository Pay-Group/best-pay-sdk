/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.lly835.bestpay.constants;

/**
 * 支付宝常量.
 * 
 * @version 1.0 2015年12月24日
 * @since 1.0
 */
public interface AliPayConstants {

    /** 请求处理成功. */
    String SUCCESS            = "success";

    /** 请求处理失败. */
    String FAIL               = "fail";

    /** 支付宝网关. 开放平台 见https://b.alipay.com/order/pidAndKey.htm */
    String ALIPAY_GATEWAY_OPEN     = "https://openapi.alipay.com/";

    /** 支付宝返回码 - 成功. */
    String RESPONSE_CODE_SUCCESS = "10000";

    /** 支付宝消息验证地址. */
    String ALIPAY_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

    /**
     * 沙箱测试
     */
    String ALIPAY_GATEWAY_OPEN_DEV     = "https://openapi.alipaydev.com/";

    /**
     * RSA签名类型
     */
    String SIGN_TYPE_RSA                  = "RSA";

    /**
     * sha256WithRsa 算法请求类型
     */
    String SIGN_TYPE_RSA2                 = "RSA2";

    String SIGN_ALGORITHMS                = "SHA1WithRSA";

    String SIGN_SHA256RSA_ALGORITHMS      = "SHA256WithRSA";

    /** UTF-8字符集 **/
    String CHARSET_UTF8                   = "UTF-8";

    /**
     * 接口名称   method参数
     */
    String ALIPAY_TRADE_PAGE_PAY = "alipay.trade.page.pay";

    /**
     * 销售产品码，与支付宝签约的产品码名称。
     * 注：目前仅支持FAST_INSTANT_TRADE_PAY
     */
    String FAST_INSTANT_TRADE_PAY = "FAST_INSTANT_TRADE_PAY";

    /**
     * 商户签约的产品支持退款功能的前提下，买家付款成功；
     */
    String TRADE_SUCCESS = "TRADE_SUCCESS";

    /**
     * 商户签约的产品不支持退款功能的前提下，买家付款成功；或者，商户签约的产品支持退款功能的前提下，交易已经成功并且已经超过可退款期限。
     */
    String TRADE_FINISHED  = "TRADE_FINISHED";
}
