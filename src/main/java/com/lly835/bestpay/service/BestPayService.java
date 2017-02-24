package com.lly835.bestpay.service;

import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付相关
 * Created by null on 2017/2/14.
 */
public interface BestPayService {

    /** 发起支付. */
    PayResponse pay(PayRequest request) throws Exception;

    /** 异步回调. */
//    PayResponse asyncNotify();

    /** 同步回调. */
    PayResponse syncNotify(HttpServletRequest request);
}
