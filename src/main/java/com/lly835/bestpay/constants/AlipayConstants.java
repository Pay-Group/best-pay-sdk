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
public interface AlipayConstants {

    /** 请求处理成功. */
    String SUCCESS            = "success";

    /** 请求处理失败. */
    String FAIL               = "fail";

    /** 支付宝新网关. 合作伙伴和无线产品都用这个 见https://b.alipay.com/order/pidAndKey.htm */
    String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do";
//    String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do";

    /** 支付宝网关. 开放平台 见https://b.alipay.com/order/pidAndKey.htm */
    String ALIPAY_GATEWAY_OPEN     = "https://openapi.alipay.com/gateway.do";

    /** 支付宝返回码 - 成功. */
    String RESPONSE_CODE_SUCCESS = "10000";

    /** 支付宝消息验证地址. */
    String ALIPAY_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

}
