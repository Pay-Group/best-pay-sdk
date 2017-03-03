package com.lly835.bestpay.service.impl;

import com.lly835.bestpay.config.AlipayConfig;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.AbstractComponent;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.Signature;
import com.lly835.bestpay.service.impl.signature.AlipayAppSignatureImpl;
import com.lly835.bestpay.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 支付宝app支付
 * Created by null on 2017/2/14.
 */
class AlipayAppServiceImpl extends AbstractComponent implements BestPayService {

    private AlipayConfig alipayConfig;

    public AlipayAppServiceImpl(AlipayConfig alipayConfig) {
        Objects.requireNonNull(alipayConfig, "alipayConfig is null.");
        this.alipayConfig = alipayConfig;
    }

    @Override
    public PayResponse pay(PayRequest request) {
        PayResponse response = new PayResponse();
        response.setOrderId(request.getOrderId());
        response.setOrderAmount(request.getOrderAmount());

        //1. 封装参数
        SortedMap<String, String> parameterMap = new TreeMap<>();
        Map<String, Object> bizContentMap = new HashMap<>();

        bizContentMap.put("subject", request.getOrderName());
        bizContentMap.put("out_trade_no", request.getOrderId());
        bizContentMap.put("timeout_express", "30m");
        bizContentMap.put("total_amount", request.getOrderAmount());
        bizContentMap.put("product_code", "QUICK_MSECURITY_PAY");
        bizContentMap.put("body", "");

        parameterMap.put("app_id", this.alipayConfig.getAppId());
        parameterMap.put("method", "alipay.trade.app.pay");
        parameterMap.put("charset", this.alipayConfig.getInputCharset());
        parameterMap.put("sign_type", this.alipayConfig.getSignType().name());
        parameterMap.put("timestamp", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        parameterMap.put("version", "1.0");
        parameterMap.put("notify_url", this.alipayConfig.getNotifyUrl());
        parameterMap.put("biz_content", JsonUtil.toJson(bizContentMap));

        //2. 签名
        Signature signature = new AlipayAppSignatureImpl(this.alipayConfig);
        String sign = signature.sign(parameterMap);
        String encodedSign;
        try {
            encodedSign = URLEncoder.encode(sign, this.alipayConfig.getInputCharset());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("illegal encoding charset.", e);
        }

        //3. 返回的结果
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (StringUtils.isBlank(k) || k.equals("sign") || StringUtils.isBlank(v)) {
                continue;
            }

            String encodedV;
            try {
                encodedV = URLEncoder.encode(v, this.alipayConfig.getInputCharset());
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException("illegal encoding charset.", e);
            }
            sb.append(k).append("=").append(encodedV).append("&");
        }
        sb.append("sign=").append(encodedSign);
        response.setPrePayParams(sb.toString());

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

    @Override
    public PayResponse asyncNotify(HttpServletRequest request) {
        return null;
    }
}
