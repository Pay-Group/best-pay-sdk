package com.lly835.bestpay;

import com.lly835.bestpay.config.AlipayConfig;
import com.lly835.bestpay.config.SignType;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;

import java.util.Random;

/**
 * @version 1.0 2017/4/28
 * @auther <a href="mailto:lly835@163.com">廖师兄</a>
 * @since 1.0
 */
public class Test {

    public static void main(String[] args) {
        String appId = "XXXX";
        String privateKey = "XXXXXX";
        String publicKey = "XXXXXXX";
        
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setAppId(appId);
        alipayConfig.setAppRSAPrivateKey(privateKey);
        alipayConfig.setAlipayRSAPublicKey(publicKey);
        alipayConfig.setSignType(SignType.RSA);
        alipayConfig.setNotifyUrl("http://127.0.0.1:8080");
        alipayConfig.setReturnUrl("http://127.0.0.1:8080");

        PayRequest request = new PayRequest();
        request.setOrderId("111111111222" + new Random().nextInt(1000));
        request.setOrderAmount(0.01);
        request.setOrderName("支付宝订单");
        request.setPayTypeEnum(BestPayTypeEnum.ALIPAY_WAP);

        BestPayServiceImpl bestPayService = new BestPayServiceImpl(alipayConfig);
        PayResponse response = bestPayService.pay(request);
        System.out.println(response);
    }

}
