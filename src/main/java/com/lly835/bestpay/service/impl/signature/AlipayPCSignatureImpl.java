package com.lly835.bestpay.service.impl.signature;

import com.lly835.bestpay.config.AliDirectPayConfig;
import com.lly835.bestpay.config.SignType;
import com.lly835.bestpay.constants.AlipayConstants;
import com.lly835.bestpay.service.AbstractComponent;
import com.lly835.bestpay.service.Signature;
import com.lly835.bestpay.utils.HttpRequestUtil;

import java.util.Map;
import java.util.Objects;

/**
 * 支付宝即时到账方式签名
 * @version 1.0 2017/2/27
 * @auther <a href="mailto:lly835@163.com">廖师兄</a>
 * @since 1.0
 */
public class AlipayPCSignatureImpl extends AbstractComponent implements Signature {

    private AliDirectPayConfig aliDirectPayConfig;

    public AlipayPCSignatureImpl(AliDirectPayConfig aliDirectPayConfig) {
        Objects.requireNonNull(aliDirectPayConfig, "aliDirectPayConfig is null.");
        this.aliDirectPayConfig = aliDirectPayConfig;
    }

    @Override
    public String sign(Map<String, String> parameterMap) {

//        //去掉内容为空的参数 && Map转Url
//        String content = MapUtil.toUrlWithSort(MapUtil.removeEmptyKeyAndValue(parameterMap));
//
//        //使用私钥签名
//        return RSA.sign(content, AlipayConfig.getPartnerPrivateKey(), AlipayConfig.getInputCharset());
        return "";

    }

    @Override
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
}
