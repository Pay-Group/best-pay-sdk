package com.lly835.bestpay.service.impl.signature;

import com.lly835.bestpay.config.AlipayConfig;
import com.lly835.bestpay.constants.AlipayConstants;
import com.lly835.bestpay.encrypt.RSA;
import com.lly835.bestpay.service.AbstractComponent;
import com.lly835.bestpay.service.Signature;
import com.lly835.bestpay.utils.HttpRequestUtil;
import com.lly835.bestpay.utils.MapUtil;
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
public class AlipayAppSignatureImpl extends AbstractComponent implements Signature {

    private AlipayConfig alipayConfig;

    public AlipayAppSignatureImpl(AlipayConfig alipayConfig) {
        Objects.requireNonNull(alipayConfig, "alipayConfig is null.");
        this.alipayConfig = alipayConfig;
    }

    @Override
    public String sign(Map<String, String> sortedParamMap) {
        Objects.requireNonNull(sortedParamMap, "sortedParamMap is null.");
        if (!(sortedParamMap instanceof SortedMap)) {
            throw new IllegalArgumentException("sortedParamMap is not sorted.");
        }

        List<String> paramList = new ArrayList<>();
        for (Map.Entry<String, String> entry : sortedParamMap.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (StringUtils.isBlank(k) || k.equals("sign") || StringUtils.isBlank(v)) {
                continue;
            }

            paramList.add(k + "=" + v);
        }

        String param = String.join("&", paramList);
        return signParamWithRSA(param);
    }

    /**
     * TODO
     * @param parameterMap
     * @param sign 需要校验的签名
     * @return
     */
    @Override
    public boolean verify(Map<String, String> parameterMap, String sign) {

        //首先校验notify_id, 同步返回是没有notify_id参数的
        String notifyId = parameterMap.get("notify_id");
        if (!StringUtils.isEmpty(notifyId)) {
            boolean notifyIdFlag = this.verifyByNotifyId(notifyId);
            if (!notifyIdFlag) { //TODO
//            return false;
            }
        }

        //去除不参与签名的参数
        parameterMap = MapUtil.removeParamsForAlipaySign(parameterMap);
        //Map转Url
        String content = MapUtil.toUrlWithSort(parameterMap);
        //使用公钥验证
//        boolean flag = RSA.verify(content, sign, AlipayConfig.getAppPublicKey(), AlipayConfig.getInputCharset());
//        if (!flag) {
//            return false;
//        }

        return true;
    }


    /**
     * 验证notifyId, 注意notifyId的时效性大约为1分钟
     *
     * @param notifyId
     * @return
     */
    private Boolean verifyByNotifyId(String notifyId) {
//        String veryfyUrl = AlipayConstants.ALIPAY_VERIFY_URL + "partner=" + AlipayConfig.getPartnerId() + "&notify_id=" + notifyId;
//        String result = HttpRequestUtil.get(veryfyUrl);
//
//        if (result.equals("true")) {
//            return true;
//        }

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
            sig.initSign(this.alipayConfig.getAppRSAPrivateKey());
            sig.update(param.getBytes(this.alipayConfig.getInputCharset()));
            return new String(org.apache.commons.codec.binary.Base64.encodeBase64(sig.sign()));
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
            throw new IllegalStateException("sign error.", e);
        }
    }

}
