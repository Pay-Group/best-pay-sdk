package com.lly835.bestpay.config;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

/**
 * 支付宝即时到账支付配置信息
 * <p>
 * 文档地址: https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7386797.0.0.7eQWOp&treeId=62&articleId=103566&docType=1
 */
public class AliDirectPayConfig extends PayConfig {

    /**
     * 合作伙伴身份ID, 以2088开头的16位纯数字.
     */
    private String partnerId;

    /**
     * 合作伙伴MD5秘钥.
     */
    private String partnerMD5Key;

    /**
     * 合作伙伴的RSA私钥(合作伙伴自行创建).
     */
    private PrivateKey partnerRSAPrivateKey;

    /**
     * 支付宝的RSA公钥(由合作伙伴上传RSA公钥后支付宝提供).
     */
    private PublicKey alipayRSAPublicKey;

    /**
     * 签名方式: MD5, RSA两个值可选, 必须大写.
     */
    private SignType signType;

    /**
     * 支付宝配置信息. 默认使用RSA签名方式.
     *
     * @param partnerId            合作伙伴身份ID, 以2088开头的16位纯数字.
     * @param partnerMD5Key        合作伙伴的MD5秘钥.
     * @param partnerRSAPrivateKey 合作伙伴的RSA私钥(合作伙伴自行创建).
     * @param alipayRSAPublicKey   支付宝的RSA公钥(由合作伙伴上传RSA公钥后支付宝提供).
     * @param notifyUrl            异步通知地址.
     * @param returnUrl            同步返回地址.
     */
    public AliDirectPayConfig(String partnerId, String partnerMD5Key, String partnerRSAPrivateKey,
                              String alipayRSAPublicKey, String notifyUrl, String returnUrl) {
        this(partnerId, partnerMD5Key, partnerRSAPrivateKey, alipayRSAPublicKey, SignType.RSA, notifyUrl, returnUrl);
    }

    /**
     * 支付宝配置信息.
     *
     * @param partnerId            合作伙伴身份ID, 以2088开头的16位纯数字.
     * @param partnerMD5Key        合作伙伴的MD5秘钥.
     * @param partnerRSAPrivateKey 合作伙伴的RSA私钥(合作伙伴自行创建).
     * @param alipayRSAPublicKey   支付宝的RSA公钥(由合作伙伴上传RSA公钥后支付宝提供).
     * @param signType             签名方式.
     * @param notifyUrl            异步通知地址.
     * @param returnUrl            同步返回地址.
     */
    public AliDirectPayConfig(String partnerId, String partnerMD5Key, String partnerRSAPrivateKey,
                              String alipayRSAPublicKey, SignType signType, String notifyUrl, String returnUrl) {
        super(notifyUrl, returnUrl);
        Objects.requireNonNull(partnerId, "config param 'partnerId' is null.");
        if (!partnerId.matches("^2088[0-9]{12}$")) {
            throw new IllegalArgumentException("config param 'partnerId' [" + partnerId + "] is incorrect.");
        }
        this.partnerId = partnerId;

        Objects.requireNonNull(signType, "config param 'signType' is null.");
        this.signType = signType;
        switch (signType) {
            case MD5:
                if (StringUtils.isEmpty(partnerMD5Key)) {
                    throw new IllegalArgumentException("config param 'partnerMD5Key' is empty.");
                }
                this.partnerMD5Key = partnerMD5Key;
                break;
            case RSA:
                Objects.requireNonNull(partnerRSAPrivateKey, "config param 'partnerRSAPrivateKey' is null.");
                try {
                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                    this.partnerRSAPrivateKey = keyFactory.generatePrivate(
                            new PKCS8EncodedKeySpec(Base64.decodeBase64(partnerRSAPrivateKey)));
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    throw new IllegalArgumentException("config param 'partnerRSAPrivateKey' is incorrect.", e);
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
            case RSA2:
                throw new IllegalArgumentException("config param 'signType' [" + signType + "] is not match.");
        }
    }

    public String getInputCharset() {
        return "utf-8";
    }

    public String getPartnerId() {
        return this.partnerId;
    }

    public String getPartnerMD5Key() {
        return this.partnerMD5Key;
    }

    public PrivateKey getPartnerRSAPrivateKey() {
        return this.partnerRSAPrivateKey;
    }

    public PublicKey getAlipayRSAPublicKey() {
        return this.alipayRSAPublicKey;
    }

    public SignType getSignType() {
        return this.signType;
    }

}
