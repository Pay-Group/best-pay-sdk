package com.lly835.bestpay.service.impl;

import com.lly835.bestpay.config.AlipayConfig;
import com.lly835.bestpay.constants.AlipayConstants;
import com.lly835.bestpay.encrypt.RSA;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonUtil;
import utils.MapUtil;
import utils.NameValuePairUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝PC端支付
 * Created by null on 2017/2/14.
 */
public class AlipayPCServiceImpl extends BestPayServiceImpl{

    private final static Logger logger = LoggerFactory.getLogger(AlipayPCServiceImpl.class);

    @Override
    public PayResponse pay(PayRequest request) throws Exception{

        logger.info("【支付宝PC端支付】request={}", JsonUtil.toJson(request));

        PayResponse response =  new PayResponse();
        response.setOrderId(request.getOrderId());
        response.setOrderAmount(request.getOrderAmount());

        //1. 封装参数
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("service", "create_direct_pay_by_user");
        requestMap.put("partner", AlipayConfig.getPartner());
        requestMap.put("_input_charset", AlipayConfig.getInputCharset());
        requestMap.put("out_trade_no", request.getOrderId());
        requestMap.put("subject", request.getOrderName());
        requestMap.put("payment_type", "1");
        requestMap.put("total_fee", String.valueOf(request.getOrderAmount()));
        requestMap.put("seller_id", AlipayConfig.getPartner());
        requestMap.put("notify_url", request.getNotifyUrl());
        requestMap.put("sign_type", AlipayConfig.getSignType());

        //2. 加密
        //Map转Url
        String content = MapUtil.toUrlWithSort(requestMap);

        //使用私钥签名
        String sign = RSA.sign(content, AlipayConfig.getPrivateKey(), AlipayConfig.getInputCharset());
        logger.debug("【支付宝PC端支付】计算出来的签名:{}", sign);

        requestMap.put("sign", sign);
        logger.debug("【支付宝PC端支付】构造好的完整参数={}", JsonUtil.toJson(requestMap));

        //3. 构造url
        String url = new URIBuilder(AlipayConstants.ALIPAY_GATEWAY_NEW).setParameters(NameValuePairUtil.convert(requestMap)).toString();
        response.setRedirectUrl(url);

        logger.debug(url);
        logger.info("【支付宝PC端支付】respones={}", JsonUtil.toJson(response));

        return response;
    }
}
