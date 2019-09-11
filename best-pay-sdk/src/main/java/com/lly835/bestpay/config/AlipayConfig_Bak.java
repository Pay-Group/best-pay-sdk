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
public class AlipayConfig_Bak extends PayConfig {

    /**
     * 应用id, 支付宝新版App支付, Wap支付的统一ID.
     */
    private String appId;

    private String appRSAPrivateKey;

    private String alipayRSAPublicKey;

    /**
     * 应用的RSA私钥(合作伙伴自行创建).
     */
    private PrivateKey appRSAPrivateKeyObject;

    /**
     * 支付宝的RSA公钥(由合作伙伴上传RSA公钥后支付宝提供).
     */
    private PublicKey alipayRSAPublicKeyOBject;

    /**
     * 签名方式: RSA, RSA2两个值可选, 必须大写.
     */
    private SignType signType;

    public void check() {
        super.check();

        Objects.requireNonNull(appId, "config param 'appId' is null.");
        if (appId.length() > 32) {
            throw new IllegalArgumentException("config param 'appId' is incorrect: size exceeds 32.");
        }

        Objects.requireNonNull(signType, "config param 'signType' is null.");
        switch (signType) {
            case MD5:
                throw new IllegalArgumentException("config param 'signType' [" + signType + "] is not match.");
            case RSA:
            case RSA2:
                Objects.requireNonNull(appRSAPrivateKey, "config param 'appRSAPrivateKey' is null.");
                try {
                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                    this.appRSAPrivateKeyObject = keyFactory.generatePrivate(
                            new PKCS8EncodedKeySpec(Base64.decodeBase64(appRSAPrivateKey)));
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    throw new IllegalArgumentException("config param 'appRSAPrivateKey' is incorrect.", e);
                }
                Objects.requireNonNull(alipayRSAPublicKey, "config param 'alipayRSAPublicKey' is null.");
                try {
                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                    this.alipayRSAPublicKeyOBject = keyFactory.generatePublic(
                            new X509EncodedKeySpec(Base64.decodeBase64(alipayRSAPublicKey)));
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    throw new IllegalArgumentException("config param 'alipayRSAPublicKey' is incorrect.", e);
                }
                break;
        }
    }

    public String getAppId() {
        return appId;
    }

    public PrivateKey getAppRSAPrivateKeyObject() {
        return appRSAPrivateKeyObject;
    }

    public PublicKey getAlipayRSAPublicKeyOBject() {
        return alipayRSAPublicKeyOBject;
    }

    public SignType getSignType() {
        return signType;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setAppRSAPrivateKey(String appRSAPrivateKey) {
        this.appRSAPrivateKey = appRSAPrivateKey;
    }

    public void setAlipayRSAPublicKey(String alipayRSAPublicKey) {
        this.alipayRSAPublicKey = alipayRSAPublicKey;
    }

    public void setSignType(SignType signType) {
        this.signType = signType;
    }

    public String getInputCharset() {
        return "utf-8";
    }


}
