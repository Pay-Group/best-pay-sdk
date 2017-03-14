package com.lly835.bestpay.service.impl;

import com.lly835.bestpay.config.AlipayConfig;
import com.lly835.bestpay.config.SignType;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.AbstractComponent;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 支付宝app支付
 * Created by null on 2017/2/14.
 */
class AlipayAppServiceImpl extends AbstractComponent implements BestPayService {

    private AlipayConfig alipayConfig;
    private AlipaySignature signature;

    public AlipayAppServiceImpl(AlipayConfig alipayConfig, AlipaySignature signature) {
        Objects.requireNonNull(alipayConfig, "alipayConfig is null.");
        this.alipayConfig = alipayConfig;
        Objects.requireNonNull(signature, "signature is null.");
        this.signature = signature;
    }

    @Override
    public PayResponse pay(PayRequest request) {
        this.logger.info("【支付宝App端支付】request={}", JsonUtil.toJson(request));

        /* 1. 封装参数 */
        SortedMap<String, String> commonParamMap = new TreeMap<>();
        commonParamMap.put("app_id", this.alipayConfig.getAppId());
        commonParamMap.put("method", "alipay.trade.app.pay");
        commonParamMap.put("format", "JSON");
        commonParamMap.put("charset", this.alipayConfig.getInputCharset());
        commonParamMap.put("sign_type", this.alipayConfig.getSignType().name());
        commonParamMap.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        commonParamMap.put("version", "1.0");
        commonParamMap.put("notify_url", this.alipayConfig.getNotifyUrl());
        commonParamMap.put("biz_content", JsonUtil.toJson(request.getAlipayBizParam().getBizParam()));

        /* 2. 签名 */
        String sign = this.signature.sign(commonParamMap);
        String encodedSign;
        try {
            encodedSign = URLEncoder.encode(sign, this.alipayConfig.getInputCharset());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("illegal encoding charset.", e);
        }

        /* 3. 返回的结果 */
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : commonParamMap.entrySet()) {
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

        /* 4. 返回签名结果 */
        PayResponse response = new PayResponse();
        response.setPrePayParams(sb.toString());
        return response;
    }

    @Override
    public boolean verify(Map<String, String> toBeVerifiedParamMap, SignType signType, String sign) {
        return this.signature.verify(toBeVerifiedParamMap, signType, sign);
    }

}
