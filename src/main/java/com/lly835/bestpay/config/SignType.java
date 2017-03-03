package com.lly835.bestpay.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 签名方式.
 * <p>
 * 即时到账(老接口)支持MD5和RSA的签名方式, App支付和Wap支付支持RSA和RSA2的签名方式. DSA签名方式暂时不支持.
 */
public enum SignType {
    MD5, RSA, RSA2;

    private static Map<String, SignType> values = new HashMap<>();

    static {
        for (SignType value : values()) {
            values.put(value.name(), value);
        }
    }

    public static SignType from(String strValue) {
        SignType value = values.get(strValue);
        if (value != null) {
            return value;
        } else {
            return null;
        }
    }

    public static SignType from(String strValue, SignType defaultValue) {
        SignType value = from(strValue);
        if (value == null) {
            return defaultValue;
        } else {
            return value;
        }
    }

    @Override
    public String toString() {
        return this.name();
    }

}
