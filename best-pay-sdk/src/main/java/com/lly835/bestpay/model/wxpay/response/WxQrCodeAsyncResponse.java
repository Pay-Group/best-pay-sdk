package com.lly835.bestpay.model.wxpay.response;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 微信扫码异步调用请求
 * Created by steven ma
 * 2019/9/10 17:56
 */

@Data
@Root(name = "xml", strict = false)
public class WxQrCodeAsyncResponse {

    @Element(name = "appid", required = false)
    private String appid;

    @Element(name = "openid", required = false)
    private String openId;

    @Element(name = "mch_id", required = false)
    private String mchId;

    @Element(name = "is_subscribe", required = false)
    private String isSubscribe;

    @Element(name = "nonce_str", required = false)
    private String nonceStr;

    @Element(name = "product_id", required = false)
    private String productId; //商品ID

    @Element(name = "sign", required = false)
    private String sign;

}
