package com.lly835.bestpay.model;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.Data;

/**
 * 支付时请求参数
 */
@Data
public class PayRequest {

    /**
     * 支付方式.
     */
    private BestPayTypeEnum payTypeEnum;

    /**
     * 订单号.
     */
    private String orderId;

    /**
     * 订单金额.
     */
    private Double orderAmount;

    /**
     * 订单名字.
     */
    private String orderName;

    /**
     * 微信openid, 仅微信公众号/小程序支付时需要
     */
    private String openid;

    /**
     * 客户端访问Ip  外部H5支付时必传，需要真实Ip
     * 20191015测试，微信h5支付已不需要真实的ip
     */
    private String spbillCreateIp;

    /**
     * 附加内容，发起支付时传入
     */
    private String attach;

    /**
     * 支付后跳转（支付宝PC网站支付）
     * 优先级高于PayConfig.returnUrl
     */
    private String returnUrl;
}
