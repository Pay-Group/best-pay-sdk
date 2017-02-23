package com.lly835.bestpay.enums;

/**
 * Created by null on 2017/2/23.
 */
public enum BestPayResultEnum {

    UNKNOW_ERROR(-1, "未知异常"),
    SUCCESS(0, "成功"),
    PARAM_ERROR(1, "参数错误"),
    ;

    private Integer code;

    private String msg;

    BestPayResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
