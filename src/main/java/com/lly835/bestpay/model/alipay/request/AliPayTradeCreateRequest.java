package com.lly835.bestpay.model.alipay.request;

import com.lly835.bestpay.constants.AliPayConstants;
import lombok.Data;

/**
 * https://docs.open.alipay.com/api_1/alipay.trade.create
 * Created by 廖师兄
 */
@Data
public class AliPayTradeCreateRequest {

    /**
     * app_id
     */
    private String appId;
    /**
     * 接口名称
     */
    private String method = "alipay.trade.create";
    /**
     * 请求使用的编码格式，如utf-8,gbk,gb2312等
     */
    private String charset = "utf-8";
    /**
     * 生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
     */
    private String signType = AliPayConstants.SIGN_TYPE_RSA2;
    /**
     * 商户请求参数的签名串，详见签名 https://docs.open.alipay.com/291/105974
     */
    private String sign;
    /**
     * 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
     */
    private String timestamp;
    /**
     * 调用的接口版本，固定为：1.0
     */
    private String version = "1.0";
    /**
     * 支付宝服务器主动通知商户服务器里指定的页面http/https路径。
     */
    private String notifyUrl;

    private String appAuthToken;

    /**
     * 请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
     */
    private String bizContent;

    @Data
    public static class BizContent {
        /**
         * 订单支付时传入的商户订单号,和支付宝交易号不能同时为空。
         * trade_no,out_trade_no如果同时存在优先取trade_no
         */
        private String outTradeNo;

        /**
         * 订单总金额，单位为元
         */
        private Double totalAmount;

        /**
         * 订单标题
         */
        private String subject;

        /**
         * 买家支付宝账号(和buyer_id不能同时为空)
         * api文档上只剩buyer_id了，推荐使用buyer_id
         *
         */
        private String buyerLogonId;

        /**
         * 买家的支付宝唯一用户号（2088开头的16位纯数字）
         * 获取方式见 https://docs.open.alipay.com/api_9/alipay.user.info.auth
         */
        private String buyerId;

        /**
         * 付款码
         */
        private String authCode;

        /**
         * 是否异步支付，传入true时，表明本次期望走异步支付，会先将支付请求受理下来，再异步推进。商户可以通过交易的异步通知或者轮询交易的状态来确定最终的交易结果
         */
        private Boolean isAsyncPay;

        private String timeExpire;

        /**
         * 异步回传参数，attach
         */
        private String passbackParams;
    }
}
