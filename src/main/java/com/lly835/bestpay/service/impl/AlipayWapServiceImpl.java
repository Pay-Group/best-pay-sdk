package com.lly835.bestpay.service.impl;

import com.lly835.bestpay.config.AlipayConfig;
import com.lly835.bestpay.config.SignType;
import com.lly835.bestpay.model.AlipayBizParam;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 支付宝Wap端支付
 * https://doc.open.alipay.com/doc2/detail.htm?treeId=203&articleId=105463&docType=1
 * Created by null on 2017/2/14.
 */
class AlipayWapServiceImpl extends AbstractComponent implements BestPayService {

    private AlipayConfig alipayConfig;
    private AlipaySignature signature;

    public AlipayWapServiceImpl(AlipayConfig alipayConfig, AlipaySignature signature) {
        Objects.requireNonNull(alipayConfig, "alipayConfig is null.");
        this.alipayConfig = alipayConfig;
        Objects.requireNonNull(signature, "signature is null.");
        this.signature = signature;
    }

    @Override
    public PayResponse pay(PayRequest request) {
        this.logger.info("【支付宝Wap端支付】request={}", JsonUtil.toJson(request));

        /* 1. 封装参数 */
        SortedMap<String, String> commonParamMap = new TreeMap<>();
        commonParamMap.put("app_id", this.alipayConfig.getAppId());
        commonParamMap.put("method", "alipay.trade.wap.pay");
        commonParamMap.put("format", "JSON");
        commonParamMap.put("return_url", this.alipayConfig.getReturnUrl());
        commonParamMap.put("charset", this.alipayConfig.getInputCharset());
        commonParamMap.put("sign_type", this.alipayConfig.getSignType().name());
        commonParamMap.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        commonParamMap.put("version", "1.0");
        commonParamMap.put("notify_url", this.alipayConfig.getNotifyUrl());
        commonParamMap.put("biz_content", JsonUtil.toJson(this.buildParam(request).getBizParam()));

        /* 2. 签名 */
        String sign = this.signature.sign(commonParamMap);
        commonParamMap.put("sign", sign);

        /* 3. 构造支付url */
        String payUrl;
        try {
            payUrl = new URIBuilder("https://openapi.alipay.com/gateway.do").setParameters(
                    commonParamMap.entrySet().stream().filter(e -> {
                        String k = e.getKey();
                        String v = e.getValue();
                        return !(StringUtils.isBlank(k) || StringUtils.isBlank(v));
                    }).map(e -> {
                        String k = e.getKey();
                        String v = e.getValue();
                        return new BasicNameValuePair(k, v);
                    }).collect(Collectors.toList())).toString();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("illegal pay url.", e);
        }

        /* 4. 返回签名结果 */
        PayResponse response = new PayResponse();
        response.setPayUri(URI.create(payUrl));
        this.logger.info("【支付宝Wap端支付】response={}", JsonUtil.toJson(response));
        return response;
    }

    @Override
    public boolean verify(Map<String, String> toBeVerifiedParamMap, SignType signType, String sign) {
        return this.signature.verify(toBeVerifiedParamMap, signType, sign);
    }

    /**
     * 构造支付宝需要的业务参数
     * @param request
     * @return
     */
    private AlipayBizParam buildParam(PayRequest request) {
        AlipayBizParam alipayBizParam = new AlipayBizParam();
        alipayBizParam.setSubject(request.getOrderName());
        alipayBizParam.setOutTradeNo(request.getOrderId());
        alipayBizParam.setTotalAmount(String.valueOf(request.getOrderAmount()));
        alipayBizParam.setProductCode("QUICK_WAP_PAY");
        return alipayBizParam;
    }

}
