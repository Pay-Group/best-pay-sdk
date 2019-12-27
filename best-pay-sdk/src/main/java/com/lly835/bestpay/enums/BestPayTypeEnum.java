package com.lly835.bestpay.enums;

import com.lly835.bestpay.exception.BestPayException;

import static com.lly835.bestpay.enums.BestPayPlatformEnum.ALIPAY;
import static com.lly835.bestpay.enums.BestPayPlatformEnum.WX;

/**
 * 支付方式
 * Created by null on 2017/2/14.
 */
public enum BestPayTypeEnum {

    ALIPAY_APP("alipay_app", ALIPAY, "支付宝app"),

    ALIPAY_PC("alipay_pc", ALIPAY, "支付宝pc"),

    ALIPAY_WAP("alipay_wap", ALIPAY, "支付宝wap"),

    ALIPAY_H5("alipay_h5", ALIPAY, "支付宝统一下单(h5)", "alipay.trade.create"),

    WXPAY_MP("JSAPI", WX,"微信公众账号支付"),

    WXPAY_MWEB("MWEB", WX, "微信H5支付"),

    WXPAY_NATIVE("NATIVE", WX, "微信Native支付"),

    WXPAY_MINI("JSAPI", WX, "微信小程序支付"),

    WXPAY_APP("APP", WX, "微信APP支付"),
    ;

    private String code;

    private BestPayPlatformEnum platform;

    private String desc;

    private String methodName;

    BestPayTypeEnum(String code, BestPayPlatformEnum platform, String desc) {
        this.code = code;
        this.platform = platform;
        this.desc = desc;
    }

    BestPayTypeEnum(String code, BestPayPlatformEnum platform, String desc, String methodName) {
        this.code = code;
        this.platform = platform;
        this.desc = desc;
        this.methodName = methodName;
    }

    public String getCode() {
        return code;
    }

    public BestPayPlatformEnum getPlatform() {
        return platform;
    }

    public String getDesc() {
        return desc;
    }

    public String getMethodName() {
        return methodName;
    }

    public static BestPayTypeEnum getByName(String code) {
        for (BestPayTypeEnum bestPayTypeEnum : BestPayTypeEnum.values()) {
            if (bestPayTypeEnum.name().equalsIgnoreCase(code)) {
                return bestPayTypeEnum;
            }
        }
        throw new BestPayException(BestPayResultEnum.PAY_TYPE_ERROR);
    }
}
