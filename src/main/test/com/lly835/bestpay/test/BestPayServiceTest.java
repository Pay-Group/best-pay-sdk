package com.lly835.bestpay.test;

import com.lly835.bestpay.config.AlipayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.junit.Test;

/**
 * Created by null on 2017/2/14.
 */
public class BestPayServiceTest {

    private BestPayServiceImpl bestPayService = new BestPayServiceImpl();

    @Test
    public void pay() throws Exception {
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setAppid("265");
        alipayConfig.setPartner("20883");
        alipayConfig.setPrivateKey("MIICeAIdW");

        PayRequest request = new PayRequest();
        request.setOrderId("152100236041388424");
        request.setOrderAmount(0.01);
        request.setOrderName("押金充值");
        request.setPayTypeEnum(BestPayTypeEnum.ALIPAY_APP);
        request.setNotifyUrl("http:///pay/alipay/app/notify");

        bestPayService.pay(request);
    }

}