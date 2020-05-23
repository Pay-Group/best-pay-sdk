package com.lly835.bestpay.model.wxpay.response;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 响应微信扫码回调的返回
 * Created by steven ma
 * 2019/9/10 18:12
 */

@Data
@Root(name = "xml", strict = false)
public class WxQrCode2WxResponse {

    @Element(name = "return_code")
    private String returnCode;

    @Element(name = "return_msg", required = false)
    private String returnMsg;

    @Element(name = "appid", required = false)
    private String appid;

    @Element(name = "mch_id", required = false)
    private String mchId;

    @Element(name = "nonce_str", required = false)
    private String nonceStr;

    @Element(name = "prepay_id", required = false)
    private String prepayId;

    @Element(name = "result_code", required = false)
    private String resultCode;

    @Element(name = "err_code_des", required = false)
    private String errCodeDes;

    @Element(name = "sign", required = false)
    private String sign;
}
