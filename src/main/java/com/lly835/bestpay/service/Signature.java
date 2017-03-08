package com.lly835.bestpay.service;

import com.lly835.bestpay.config.SignType;

import java.util.Map;

/**
 * 签名接口
 * 针对支付宝微信的参数签名
 *
 * @version 1.0 16/1/7
 * @since 1.0
 */
public interface Signature {

    /**
     * 对参数进行签名.
     * <p>
     * <p>
     * 使用系统默认签名算法.
     * </p>
     *
     * @param parameters 参数
     * @return 签名
     */
    String sign(Map<String, String> parameters);

    /**
     * 验证参数签名.
     * <p>
     * <p>
     * 使用系统默认签名算法.
     * </p>
     *
     * @param parameters 参数
     * @param sign       需要校验的签名
     * @return true：签名正确，false：签名有误
     */
    boolean verify(Map<String, String> parameters, SignType signType, String sign);
}
