package com.lly835.bestpay.model.alipay.request;

import lombok.Data;
import org.simpleframework.xml.Element;

/**
 * Created by this
 * @date 2019/9/8 15:19
 */
@Data
public class AliPayPCRequest {

    /**
     * app_id
     */
    @Element(name="app_id")
    String appId;
    /**
     * 接口名称
     */
    String method;
    /**
     * 请求使用的编码格式，如utf-8,gbk,gb2312等
     */
    String charset;
    /**
     * 生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
     */
    @Element(name="sign_type")
    String signType;
    /**
     * 商户请求参数的签名串，详见签名 https://docs.open.alipay.com/291/105974
     */
    String sign;
    /**
     * 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
     */
    String timestamp;
    /**
     * 调用的接口版本，固定为：1.0
     */
    String version;
    /**
     * 支付宝服务器主动通知商户服务器里指定的页面http/https路径。
     */
    @Element(name="notify_url")
    String notifyUrl;
    /**
     * 请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
     */
    @Element(name="biz_content")
    String bizContent;
}
