package com.lly835.bestpay;

import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.impl.alipay.AliPayServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 * Created by this on 2019/9/8 20:53
 */
public class AliPayTest {

    private AliPayConfig aliPayConfig;

    private AliPayServiceImpl aliPayService = new AliPayServiceImpl();

    @Before
    public void init() {
        AliPayConfig aliPayConfig = new AliPayConfig();
        aliPayConfig.setNotifyUrl("http://127.0.0.1:8080");
        aliPayConfig.setAppId("appid");
        aliPayConfig.setPrivateKey("商户私钥");
        aliPayConfig.setAliPayPublicKey("支付宝公钥");
        aliPayConfig.setSandbox(true);
        this.aliPayConfig = aliPayConfig;
        aliPayService.setAliPayConfig(aliPayConfig);
    }

    @Test
    public void pay() {
        PayRequest request = new PayRequest();
        request.setPayTypeEnum(BestPayTypeEnum.ALIPAY_PC);
        request.setOrderId("111111111222" + new Random().nextInt(1000));
        request.setOrderAmount(0.01);
        request.setOrderName("aliPay PC");
        PayResponse response = aliPayService.pay(request);
        System.out.println(response.getBody());
    }
}
