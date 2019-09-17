package com.lly835.bestpay.service.impl.alipay;

import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.config.SignType;
import com.lly835.bestpay.constants.AliPayConstants;
import com.lly835.bestpay.model.*;
import com.lly835.bestpay.model.alipay.request.AliPayPCRequest;
import com.lly835.bestpay.model.alipay.response.AliPayAsyncResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.lly835.bestpay.utils.*;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by this on 2019/9/8 15:50
 */
@Slf4j
public class AliPayServiceImpl extends BestPayServiceImpl {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private AliPayConfig aliPayConfig;

    public void setAliPayConfig(AliPayConfig aliPayConfig) {
        this.aliPayConfig = aliPayConfig;
    }

    @Override
    public PayResponse pay(PayRequest request) {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("out_trade_no",request.getOrderId());
        requestParams.put("product_code", AliPayConstants.FAST_INSTANT_TRADE_PAY);
        requestParams.put("total_amount", String.valueOf(request.getOrderAmount()));
        requestParams.put("subject", String.valueOf(request.getOrderName()));

        AliPayPCRequest aliPayRequest = new AliPayPCRequest();
        aliPayRequest.setAppId(aliPayConfig.getAppId());
        aliPayRequest.setCharset("utf-8");
        aliPayRequest.setMethod(AliPayConstants.ALIPAY_TRADE_PAGE_PAY);
        aliPayRequest.setSignType(AliPayConstants.SIGN_TYPE_RSA2);
        aliPayRequest.setNotifyUrl(aliPayConfig.getNotifyUrl());
        aliPayRequest.setTimestamp(LocalDateTime.now().format(formatter));
        aliPayRequest.setVersion("1.0");
        // 剔除空格、制表符、换行
        aliPayRequest.setBizContent(JsonUtil.toJson(requestParams).replaceAll("\\s*",""));
        aliPayRequest.setSign(AliPaySignature.sign(MapUtil.object2MapWithUnderline(aliPayRequest),aliPayConfig.getPrivateKey()));

        Map<String, String> parameters = MapUtil.object2MapWithUnderline(aliPayRequest);
        Map<String, String> applicationParams = new HashMap<>();
        applicationParams.put("biz_content",aliPayRequest.getBizContent());
        parameters.remove("biz_content");
        String baseUrl = WebUtil.getRequestUrl(parameters,aliPayConfig.isSandbox());
        String body = WebUtil.buildForm(baseUrl,applicationParams);

        // pc 网站支付 只需返回body
        PayResponse response = new PayResponse();
        response.setBody(body);
        return response;
    }


    @Override
    public boolean verify(Map<String, String> toBeVerifiedParamMap, SignType signType, String sign) {
        return AliPaySignature.verify(toBeVerifiedParamMap, aliPayConfig.getAliPayPublicKey());
    }

    /**
     * 异步通知
     * @param notifyData
     * @return
     */
    @Override
    public PayResponse asyncNotify(String notifyData) {
        try {
            notifyData = URLDecoder.decode(notifyData,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //签名校验
        if (!AliPaySignature.verify(MapUtil.form2Map(notifyData), aliPayConfig.getAliPayPublicKey())) {
            log.error("【支付宝支付异步通知】签名验证失败, response={}", notifyData);
            throw new RuntimeException("【支付宝支付异步通知】签名验证失败");
        }
        HashMap<String, String> params = MapUtil.form2MapWithCamelCase(notifyData);
        AliPayAsyncResponse response = MapUtil.mapToObject(params, AliPayAsyncResponse.class);
        String tradeStatus =response.getTradeStatus();
        if(!tradeStatus.equals(AliPayConstants.TRADE_FINISHED) &&
                !tradeStatus.equals(AliPayConstants.TRADE_SUCCESS)) {
            throw new RuntimeException("【支付宝支付异步通知】发起支付, trade_status != SUCCESS | FINISHED");
        }
        return buildPayResponse(response);
    }

    @Override
    public RefundResponse refund(RefundRequest request) {
        return super.refund(request);
    }

    @Override
    public OrderQueryResponse query(OrderQueryRequest request) {
        return super.query(request);
    }

    @Override
    public String downloadBill(DownloadBillRequest request) {
        return super.downloadBill(request);
    }

    private PayResponse buildPayResponse(AliPayAsyncResponse response) {
        PayResponse payResponse = new PayResponse();
        payResponse.setOrderAmount(Double.valueOf(response.getTotalAmount()));
        payResponse.setOrderId(response.getOutTradeNo());
        payResponse.setOutTradeNo(response.getTradeNo());
        payResponse.setAppId(response.getAppId());
        return payResponse;
    }
}
