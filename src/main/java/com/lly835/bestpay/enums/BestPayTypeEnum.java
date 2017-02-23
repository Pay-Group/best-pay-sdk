package com.lly835.bestpay.enums;

import lombok.Getter;

/**
 * 支付方式
 * Created by null on 2017/2/14.
 */
@Getter
public enum BestPayTypeEnum {

    ALIPAY_APP("alipay_app", "支付宝app"),

    ALIPAY_PC("alipay_pc", "支付宝pc"),
    ;

    private String code;

    private String name;

    BestPayTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
