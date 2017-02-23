package com.lly835.bestpay.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;

/**
 * Created by null on 2017/2/14.
 */
public class BestPayServiceImpl implements BestPayService{

    public PayResponse pay(PayRequest request) throws Exception {

        //检查参数
        request.check();

        //判断是什么支付方式
        if (request.getPayTypeEnum() == BestPayTypeEnum.ALIPAY_APP) {
            AlipayAppServiceImpl alipayService = new AlipayAppServiceImpl();
            return alipayService.pay(request);
        }else if (request.getPayTypeEnum() == BestPayTypeEnum.ALIPAY_PC) {
            AlipayPCServiceImpl alipayService = new AlipayPCServiceImpl();
            return alipayService.pay(request);
        }

        return null;
    }
}
