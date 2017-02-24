package com.lly835.bestpay.config;

import com.lly835.bestpay.enums.BestPayResultEnum;
import com.lly835.bestpay.exception.BestPayException;
import org.apache.commons.lang3.StringUtils;

/**
 * 支付宝配置信息
 * 不需要每一项都配置
 * 合作伙伴(即时到账支付, wap支付) https://openhome.alipay.com/platform/keyManage.htm?keyType=partner
 * app(App支付, 扫码付等) https://openhome.alipay.com/platform/keyManage.htm
 * Created by null on 2017/2/14.
 */

public class AlipayConfig {

    /** 合作者身份ID. */
    private static String partnerId;

    /** 合作伙伴的私钥(自己产生). */
    private static String partnerPrivateKey;

    /** 合作伙伴的公钥(支付宝提供). */
    private static String partnerPublicKey;

    /** 应用id, 支付宝新版把app支付, 扫码付只需要用一个appid就行了. */
    private static String appId;

    /** 应用私钥. */
    private static String appPrivateKey;

    /** 应用公钥. */
    private static String appPublicKey;

    /** 签名方式：DSA、RSA、MD5三个值可选，必须大写. */
    private static String signType = "RSA";

    private static String inputCharset = "UTF-8";

    public static String getPartnerId() {
        return partnerId;
    }

    public static void setPartnerId(String partnerId) {
        AlipayConfig.partnerId = partnerId;
    }

    public static String getPartnerPrivateKey() {
        return partnerPrivateKey;
    }

    public static void setPartnerPrivateKey(String partnerPrivateKey) {
        AlipayConfig.partnerPrivateKey = partnerPrivateKey;
    }

    public static String getPartnerPublicKey() {
        return partnerPublicKey;
    }

    public static void setPartnerPublicKey(String partnerPublicKey) {
        AlipayConfig.partnerPublicKey = partnerPublicKey;
    }

    public static String getAppId() {
        return appId;
    }

    public static void setAppId(String appId) {
        AlipayConfig.appId = appId;
    }

    public static String getAppPrivateKey() {
        return appPrivateKey;
    }

    public static void setAppPrivateKey(String appPrivateKey) {
        AlipayConfig.appPrivateKey = appPrivateKey;
    }

    public static String getAppPublicKey() {
        return appPublicKey;
    }

    public static void setAppPublicKey(String appPublicKey) {
        AlipayConfig.appPublicKey = appPublicKey;
    }

    public static String getSignType() {
        return signType;
    }

    public static String getInputCharset() {
        return inputCharset;
    }

    public static Boolean check() {
        if(StringUtils.isEmpty(partnerId)) {
            throw new BestPayException(BestPayResultEnum.CONFIG_ERROR);
        }
        return true;
    }
}
