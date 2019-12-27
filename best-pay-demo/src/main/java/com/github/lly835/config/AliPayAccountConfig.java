package com.github.lly835.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by this on 2019/9/11 23:56
 */
@Data
@ConfigurationProperties(prefix = "alipay")
@Component
public class AliPayAccountConfig {
    /**
     * appId
     */
    private String appId;
    /**
     * 商户私钥
     */
    private String privateKey;
    /**
     * 支付宝公钥
     */
    private String aliPayPublicKey;
    /**
     * 异步通知url
     */
    private String notifyUrl;

    /**
     * 同步返回的url
     */
    private String returnUrl;

    /**
     * 是否使用沙箱
     */
    private Boolean sandbox;
}
