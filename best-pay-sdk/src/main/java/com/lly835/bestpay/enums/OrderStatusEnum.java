package com.lly835.bestpay.enums;

import lombok.Getter;

/**
 * 订单状态
 * Created by 廖师兄
 * 2018-06-04 16:58
 */
@Getter
public enum OrderStatusEnum {

    SUCCESS("支付成功"),

    REFUND("转入退款"),

    NOTPAY("未支付"),

    CLOSED("已关闭"),

    REVOKED("已撤销（刷卡支付）"),

    USERPAYING("用户支付中"),

    PAYERROR("支付失败"),

    UNKNOW("未知状态"),
    ;

    /**
     * 描述 微信退款后有内容
     */
    private String desc;

    OrderStatusEnum(String desc) {
        this.desc = desc;
    }

    public static OrderStatusEnum findByName(String name) {
        for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {
            if (name.toLowerCase().equals(orderStatusEnum.name().toLowerCase())) {
                return orderStatusEnum;
            }
        }
        throw new RuntimeException("错误的微信支付状态");
    }
}
