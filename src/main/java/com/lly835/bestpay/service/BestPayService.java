package com.lly835.bestpay.service;

import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;

/**
 * 支付相关
 * Created by null on 2017/2/14.
 */
public interface BestPayService {

    /** 发起支付. */
    public PayResponse pay(PayRequest request) throws Exception;
}
