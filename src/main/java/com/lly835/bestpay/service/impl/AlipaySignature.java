package com.lly835.bestpay.service.impl;

import com.lly835.bestpay.config.AlipayConfig;
import com.lly835.bestpay.config.SignType;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;

/**
 * 支付宝Application方式签名
 *
 * @version 1.0 2017/2/27
 * @auther <a href="mailto:lly835@163.com">廖师兄</a>
 * @since 1.0
 */
class AlipaySignature extends AbstractComponent {

    private AlipayConfig alipayConfig;

    public AlipaySignature(AlipayConfig alipayConfig) {
        Objects.requireNonNull(alipayConfig, "alipayConfig is null.");
        this.alipayConfig = alipayConfig;
    }

    public String sign(SortedMap<String, String> sortedParamMap) {
        Objects.requireNonNull(sortedParamMap, "sortedParamMap is null.");
        List<String> paramList = new ArrayList<>();
        sortedParamMap.forEach((k, v) -> {
            if (StringUtils.isBlank(k) || k.equals("sign") || StringUtils.isBlank(v)) {
                return;
            }

            paramList.add(k + "=" + v);
        });
        String param = String.join("&", paramList);
        return signParamWithRSA(param);
    }

    public boolean verify(Map<String, String> toBeVerifiedParamMap, SignType signType, String sign) {
        Objects.requireNonNull(toBeVerifiedParamMap, "to be verified param map is null.");
        if (toBeVerifiedParamMap.isEmpty()) {
            throw new IllegalArgumentException("to be verified param map is empty.");
        }

        Objects.requireNonNull(signType, "sign type is null.");
        switch (signType) {
            case MD5:
                throw new IllegalArgumentException("unsupported sign type: MD5.");
        }

        if (StringUtils.isBlank(sign)) {
            throw new IllegalArgumentException("sign is blank.");
        }

        /* 1. 验签 */
        List<String> paramList = new ArrayList<>();
        toBeVerifiedParamMap.forEach((k, v) -> {
            if (StringUtils.isBlank(k) || k.equals("sign_type") || k.equals("sign") || StringUtils.isEmpty(v)) {
                return;
            }

            paramList.add(k + "=" + v);
        });
        Collections.sort(paramList);
        String toBeVerifiedStr = String.join("&", paramList);
        boolean r = verifyParamWithRSA(toBeVerifiedStr, signType, sign);
        if (!r) {
            this.logger.warn("fail to verify sign with sign type {}.", signType.name());
            return false;
        }

        /* 2. 校验notify_id, 同步返回是没有notify_id参数的 */
        String notifyId = toBeVerifiedParamMap.get("notify_id");
        if (!StringUtils.isEmpty(notifyId)) {
            r = this.verifyNotifyId(notifyId);
            if (!r) {
                this.logger.warn("fail to verify notify id.");
                return false;
            }
        }

        return true;
    }


    /**
     * TODO
     * 验证notifyId, 注意notifyId的时效性大约为1分钟
     *
     * @param notifyId 异步通知通知id
     * @return 验证notifyId通过返回true, 否则返回false
     */
    private boolean verifyNotifyId(String notifyId) {
        return false;
    }

    private String signParamWithRSA(String param) {
        String algorithm;
        switch (this.alipayConfig.getSignType()) {
            case RSA:
                algorithm = "SHA1WithRSA";
                break;
            case RSA2:
                algorithm = "SHA256WithRSA";
                break;
            default:
                throw new IllegalStateException("unsupported sign type [" + this.alipayConfig.getSignType() + "].");
        }
        try {
            java.security.Signature sig = java.security.Signature.getInstance(algorithm);
            sig.initSign(this.alipayConfig.getAppRSAPrivateKeyObject());
            sig.update(param.getBytes(this.alipayConfig.getInputCharset()));
            return Base64.getEncoder().encodeToString(sig.sign());
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
            throw new IllegalStateException("sign error.", e);
        }
    }

    private boolean verifyParamWithRSA(String param, SignType signType, String sign) {
        String algorithm;
        switch (signType) {
            case RSA:
                algorithm = "SHA1WithRSA";
                break;
            case RSA2:
                algorithm = "SHA256WithRSA";
                break;
            default:
                throw new IllegalStateException("unsupported sign type [" + this.alipayConfig.getSignType() + "].");
        }
        try {
            java.security.Signature sig = java.security.Signature.getInstance(algorithm);
            sig.initVerify(this.alipayConfig.getAlipayRSAPublicKeyOBject());
            sig.update(param.getBytes("utf-8"));
            return sig.verify(Base64.getDecoder().decode(sign));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | UnsupportedEncodingException e) {
            throw new IllegalStateException("AliPay verify error.");
        }
    }

}
