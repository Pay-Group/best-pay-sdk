package com.lly835.bestpay.config;

/**
 * 支付宝配置信息
 * Created by null on 2017/2/14.
 */

public class AlipayConfig {

    /** 合作者身份ID. */
    private static String partner;

    /** 应用id, 支付宝新版把app支付, 扫码付只需要用一个appid就行了. */
    private static String appid;

    /** 签名方式：DSA、RSA、MD5三个值可选，必须大写. */
    private static String signType = "RSA";

    /** 私钥. */
    private static String privateKey;

    /** 公钥. */
    private static String publicKey;

    private static String inputCharset = "UTF-8";

    public static String getPartner() {
        return partner;
    }

    public static void setPartner(String partner) {
        AlipayConfig.partner = partner;
    }

    public static String getAppid() {
        return appid;
    }

    public static void setAppid(String appid) {
        AlipayConfig.appid = appid;
    }

    public static String getSignType() {
        return signType;
    }

    public static void setSignType(String signType) {
        AlipayConfig.signType = signType;
    }

    public static String getPrivateKey() {
        return privateKey;
    }

    public static void setPrivateKey(String privateKey) {
        AlipayConfig.privateKey = privateKey;
    }

    public static String getPublicKey() {
        return publicKey;
    }

    public static void setPublicKey(String publicKey) {
        AlipayConfig.publicKey = publicKey;
    }

    public static String getInputCharset() {
        return inputCharset;
    }

    public static void setInputCharset(String inputCharset) {
        AlipayConfig.inputCharset = inputCharset;
    }
}
