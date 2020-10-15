package com.lly835.bestpay.model.alipay.request;

import com.lly835.bestpay.constants.AliPayConstants;
import lombok.Data;

/**
 * @author zicheng
 * @date 2020/10/14
 * Description:
 * https://opendocs.alipay.com/apis/api_1/alipay.trade.refund
 */
@Data
public class AliPayOrderRefundRequest {
    /**
     * app_id
     */
    private String appId;
    /**
     * 接口名称
     */
    private String method = "alipay.trade.refund";

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
     * 详见应用授权概述
     * https://docs.open.alipay.com/20160728150111277227/intro
     */
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
         * 支付宝交易号，和商户订单号不能同时为空
         */
        private String tradeNo;

        /**
         * 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
         */
        private Double refundAmount;

        /**
         * 退款的原因说明
         */
        private String refundReason;

        /**
         * 查询选项，商户通过上送该字段来定制查询返回信息
         */
        private String queryOptions;
    }
}
