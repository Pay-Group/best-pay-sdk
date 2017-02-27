package com.lly835.bestpay.service.impl;

import com.lly835.bestpay.config.AlipayConfig;
import com.lly835.bestpay.constants.AlipayConstants;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.Signature;
import com.lly835.bestpay.service.impl.signatrue.AlipayAppSignatrueImpl;
import com.lly835.bestpay.utils.JsonUtil;
import com.lly835.bestpay.utils.NameValuePairUtil;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝Wap端支付
 * https://doc.open.alipay.com/doc2/detail.htm?treeId=203&articleId=105463&docType=1
 * Created by null on 2017/2/14.
 */
public class AlipayWapServiceImpl implements BestPayService{

    private final static Logger logger = LoggerFactory.getLogger(AlipayWapServiceImpl.class);

    public PayResponse pay(PayRequest request) throws Exception{

        logger.info("【支付宝Wap端支付】request={}", JsonUtil.toJson(request));

        PayResponse response =  new PayResponse();
        response.setOrderId(request.getOrderId());
        response.setOrderAmount(request.getOrderAmount());

        //1. 封装参数
        Map<String, String> parameterMap = new HashMap<>();
        Map<String, Object> bizContentMap = new HashMap<>();

        bizContentMap.put("body", "对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。");
        bizContentMap.put("subject", request.getOrderName());
        bizContentMap.put("out_trade_no", request.getOrderId());
        bizContentMap.put("timeout_express", "30m");
        bizContentMap.put("total_amount", request.getOrderAmount());
        bizContentMap.put("product_code", "QUICK_WAP_PAY");

        parameterMap.put("app_id", AlipayConfig.getAppId());
        parameterMap.put("method", "alipay.trade.wap.pay");
        parameterMap.put("format", "JSON");
        parameterMap.put("return_url", request.getReturnUrl());
        parameterMap.put("charset", "utf-8");
        parameterMap.put("sign_type", AlipayConfig.getSignType());
        parameterMap.put("timestamp", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        parameterMap.put("version", "1.0");
        parameterMap.put("notify_url", request.getNotifyUrl());
        parameterMap.put("biz_content", JsonUtil.toJson(bizContentMap));

        //2. 签名
        Signature signature = new AlipayAppSignatrueImpl();
        String sign = signature.sign(parameterMap);
        parameterMap.put("sign", sign);
        logger.debug("【支付宝Wap端支付】构造好的完整参数={}", JsonUtil.toJson(parameterMap));

        //3. 构造url
        String url = new URIBuilder(AlipayConstants.ALIPAY_GATEWAY_OPEN).setParameters(NameValuePairUtil.convert(parameterMap)).toString();
        response.setRedirectUrl(url);

        logger.info("【支付宝Wap端支付】respones={}", JsonUtil.toJson(response));
        logger.debug(url);

        return response;
    }

    @Override
    public PayResponse syncNotify(HttpServletRequest request) {

        //构造返回对象
        PayResponse response = new PayResponse();
        response.setOrderId(request.getParameter("out_trade_no"));
        response.setTradeNo(request.getParameter("trade_no"));

        return response;
    }
}
