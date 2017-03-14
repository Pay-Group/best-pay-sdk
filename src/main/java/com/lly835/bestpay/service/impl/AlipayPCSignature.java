package com.lly835.bestpay.service.impl;

import com.lly835.bestpay.config.AliDirectPayConfig;
import com.lly835.bestpay.config.SignType;
import com.lly835.bestpay.constants.AlipayConstants;
import com.lly835.bestpay.service.AbstractComponent;
import com.lly835.bestpay.utils.HttpRequestUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;

/**
 * 支付宝即时到账方式签名
 * @version 1.0 2017/2/27
 * @auther <a href="mailto:lly835@163.com">廖师兄</a>
 * @since 1.0
 */
class AlipayPCSignature extends AbstractComponent {

    private AliDirectPayConfig aliDirectPayConfig;

    public AlipayPCSignature(AliDirectPayConfig aliDirectPayConfig) {
        Objects.requireNonNull(aliDirectPayConfig, "aliDirectPayConfig is null.");
        this.aliDirectPayConfig = aliDirectPayConfig;
    }

    public String sign(SortedMap<String, String> sortedParamMap) {
        List<String> paramList = new ArrayList<>();
        for (Map.Entry<String, String> entry : sortedParamMap.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (StringUtils.isBlank(k) ||  k.equals("sign") || k.equals("sign_type") || StringUtils.isBlank(v)) {
                continue;
            }

            paramList.add(k + "=" + v);
        }
        String param = String.join("&", paramList);
        switch (this.aliDirectPayConfig.getSignType()) {
            case MD5:
                return signParamWithMD5(param);
            case RSA:
                return signParamWithRSA(param);
        }
        return "";

    }

    public boolean verify(Map<String, String> toBeVerifiedParamMap, SignType signType, String sign) {
//        //首先校验notify_id
//        String notifyId = parameterMap.get("notify_id");
//        if (StringUtils.isEmpty(notifyId)) {
//            return false;
//        }
//        boolean notifyIdFlag = this.verifyByNotifyId(notifyId);
//        if (!notifyIdFlag) { //TODO
////            return false;
//        }
//
//        //去除不参与签名的参数
//        parameterMap = MapUtil.removeParamsForAlipaySign(parameterMap);
//        //Map转Url
//        String content = MapUtil.toUrlWithSort(parameterMap);
//        //使用公钥验证
////        boolean flag = RSA.verify(content, sign, AlipayConfig.getPartnerPublicKey(), AlipayConfig.getInputCharset());
////        if (!flag) {
////            return false;
////        }

        return false;
    }

    /**
     * 验证notifyId, 注意notifyId的时效性大约为1分钟
     * @param notifyId
     * @return
     */
    private Boolean verifyByNotifyId(String notifyId) {
        String veryfyUrl = AlipayConstants.ALIPAY_VERIFY_URL + "partner=" + this.aliDirectPayConfig.getPartnerId() + "&notify_id=" + notifyId;
        String result = HttpRequestUtil.get(veryfyUrl);

        if(result.equals("true")){
            return true;
        }

        return false;
    }

    private String signParamWithMD5(String param) {
        return DigestUtils.md5Hex(param + this.aliDirectPayConfig.getPartnerMD5Key());
    }

    private String signParamWithRSA(String param) {
        try {
            java.security.Signature sig = java.security.Signature.getInstance("SHA1WithRSA");
            sig.initSign(this.aliDirectPayConfig.getPartnerRSAPrivateKey());
            sig.update(param.getBytes(this.aliDirectPayConfig.getInputCharset()));
            return Base64.getEncoder().encodeToString(sig.sign());
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
            throw new IllegalStateException("sign error.", e);
        }
    }

    private boolean verifyParamWithRSA(String param, String sign) {
        try {
            java.security.Signature sig = java.security.Signature.getInstance("SHA1WithRSA");
            sig.initVerify(this.aliDirectPayConfig.getAlipayRSAPublicKey());
            sig.update(param.getBytes("utf-8"));
            return sig.verify(Base64.getDecoder().decode(sign));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | UnsupportedEncodingException e) {
            throw new IllegalStateException("AliPay verify error.");
        }
    }

}
