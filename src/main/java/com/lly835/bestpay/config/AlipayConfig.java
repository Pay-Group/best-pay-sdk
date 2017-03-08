package com.lly835.bestpay.config;

import org.apache.commons.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

/**
 * 支付宝配置信息(非即时到账接口)
 * <p>
 * 文档地址: https://openhome.alipay.com/developmentDocument.htm
 */
public class AlipayConfig extends PayConfig {

    /**
     * 应用id, 支付宝新版App支付, Wap支付的统一ID.
     */
    private String appId;

    /**
     * 应用的RSA私钥(合作伙伴自行创建).
     */
    private PrivateKey appRSAPrivateKey;

    /**
     * 支付宝的RSA公钥(由合作伙伴上传RSA公钥后支付宝提供).
     */
    private PublicKey alipayRSAPublicKey;

    /**
     * 签名方式: RSA, RSA2两个值可选, 必须大写.
     */
    private SignType signType;

    /**
     * 支付宝配置信息. 默认使用RSA2签名方式.
     *
     * @param appId              应用id, 支付宝新版App支付, Wap支付的统一ID.
     * @param appRSAPrivateKey   应用的RSA私钥(合作伙伴自行创建).
     * @param alipayRSAPublicKey 支付宝的RSA公钥(由合作伙伴上传RSA公钥后支付宝提供).
     * @param notifyUrl          异步通知地址.
     */
    public AlipayConfig(String appId, String appRSAPrivateKey, String alipayRSAPublicKey, String notifyUrl) {
        this(appId, appRSAPrivateKey, alipayRSAPublicKey, notifyUrl, null);
    }

    /**
     * 支付宝配置信息.
     *
     * @param appId              应用id, 支付宝新版App支付, Wap支付的统一ID.
     * @param appRSAPrivateKey   应用的RSA私钥(合作伙伴自行创建).
     * @param alipayRSAPublicKey 支付宝的RSA公钥(由合作伙伴上传RSA公钥后支付宝提供).
     * @param signType           签名方式.
     * @param notifyUrl          异步通知地址.
     */
    public AlipayConfig(String appId, String appRSAPrivateKey, String alipayRSAPublicKey, SignType signType,
                        String notifyUrl) {
        this(appId, appRSAPrivateKey, alipayRSAPublicKey, signType, notifyUrl, null);
    }

    /**
     * 支付宝配置信息. 默认使用RSA2签名方式.
     *
     * @param appId              应用id, 支付宝新版App支付, Wap支付的统一ID.
     * @param appRSAPrivateKey   应用的RSA私钥(合作伙伴自行创建).
     * @param alipayRSAPublicKey 支付宝的RSA公钥(由合作伙伴上传RSA公钥后支付宝提供).
     * @param notifyUrl          异步通知地址.
     * @param returnUrl          同步返回地址.
     */
    public AlipayConfig(String appId, String appRSAPrivateKey, String alipayRSAPublicKey, String notifyUrl,
                        String returnUrl) {
        this(appId, appRSAPrivateKey, alipayRSAPublicKey, SignType.RSA2, notifyUrl, returnUrl);
    }

    /**
     * 支付宝配置信息.
     *
     * @param appId              应用id, 支付宝新版App支付, Wap支付的统一ID.
     * @param appRSAPrivateKey   应用的RSA私钥(合作伙伴自行创建).
     * @param alipayRSAPublicKey 支付宝的RSA公钥(由合作伙伴上传RSA公钥后支付宝提供).
     * @param signType           签名方式.
     * @param notifyUrl          异步通知地址.
     * @param returnUrl          同步返回地址.
     */
    public AlipayConfig(String appId, String appRSAPrivateKey, String alipayRSAPublicKey, SignType signType,
                        String notifyUrl, String returnUrl) {
        super(notifyUrl, returnUrl);
        Objects.requireNonNull(appId, "config param 'appId' is null.");
        if (appId.length() > 32) {
            throw new IllegalArgumentException("config param 'appId' is incorrect: size exceeds 32.");
        }
        this.appId = appId;

        Objects.requireNonNull(signType, "config param 'signType' is null.");
        this.signType = signType;
        switch (signType) {
            case MD5:
                throw new IllegalArgumentException("config param 'signType' [" + signType + "] is not match.");
            case RSA:
            case RSA2:
                Objects.requireNonNull(appRSAPrivateKey, "config param 'appRSAPrivateKey' is null.");
                try {
                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                    this.appRSAPrivateKey = keyFactory.generatePrivate(
                            new PKCS8EncodedKeySpec(Base64.decodeBase64(appRSAPrivateKey)));
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    throw new IllegalArgumentException("config param 'appRSAPrivateKey' is incorrect.", e);
                }
                Objects.requireNonNull(alipayRSAPublicKey, "config param 'alipayRSAPublicKey' is null.");
                try {
                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                    this.alipayRSAPublicKey = keyFactory.generatePublic(
                            new X509EncodedKeySpec(Base64.decodeBase64(alipayRSAPublicKey)));
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    throw new IllegalArgumentException("config param 'alipayRSAPublicKey' is incorrect.", e);
                }
                break;
        }
    }

    public String getInputCharset() {
        return "utf-8";
    }

    public String getAppId() {
        return this.appId;
    }

    public PrivateKey getAppRSAPrivateKey() {
        return this.appRSAPrivateKey;
    }

    public PublicKey getAlipayRSAPublicKey() {
        return this.alipayRSAPublicKey;
    }

    public SignType getSignType() {
        return this.signType;
    }

}
