package com.lly835.bestpay.service.impl;

import com.lly835.bestpay.config.AlipayConfig;
import com.lly835.bestpay.constants.AlipayConstants;
import com.lly835.bestpay.enums.AlipayRefundStatusEnum;
import com.lly835.bestpay.enums.AlipayTradeStatusEnum;
import com.lly835.bestpay.enums.BestPayResultEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.exception.BestPayException;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.AbstractComponent;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.Signature;
import com.lly835.bestpay.service.impl.signature.AlipayAppSignatureImpl;
import com.lly835.bestpay.utils.DateUtil;
import com.lly835.bestpay.utils.JsonUtil;
import com.lly835.bestpay.utils.NameValuePairUtil;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTime;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 支付宝Wap端支付
 * https://doc.open.alipay.com/doc2/detail.htm?treeId=203&articleId=105463&docType=1
 * Created by null on 2017/2/14.
 */
class AlipayWapServiceImpl extends AbstractComponent implements BestPayService {

    private AlipayConfig alipayConfig;

    public AlipayWapServiceImpl(AlipayConfig alipayConfig) {
        Objects.requireNonNull(alipayConfig, "alipayConfig is null.");
        this.alipayConfig = alipayConfig;
    }

    public PayResponse pay(PayRequest request) throws Exception {

        logger.info("【支付宝Wap端支付】request={}", JsonUtil.toJson(request));

        PayResponse response = new PayResponse();
        response.setOrderId(request.getOrderId());
        response.setOrderAmount(request.getOrderAmount());

        //1. 封装参数
        Map<String, String> parameterMap = new HashMap<>();
        Map<String, Object> bizContentMap = new HashMap<>();

        bizContentMap.put("subject", request.getOrderName());
        bizContentMap.put("out_trade_no", request.getOrderId());
        bizContentMap.put("timeout_express", "30m");
        bizContentMap.put("total_amount", request.getOrderAmount());
        bizContentMap.put("product_code", "QUICK_WAP_PAY");
        /** 公用回传参数(这里用作判断回调的接口的支付方式). */
        bizContentMap.put("passback_params", BestPayTypeEnum.ALIPAY_WAP.getCode());


        parameterMap.put("app_id", this.alipayConfig.getAppId());
        parameterMap.put("method", "alipay.trade.wap.pay");
        parameterMap.put("format", "JSON");
        parameterMap.put("return_url", this.alipayConfig.getReturnUrl());
        parameterMap.put("charset", "utf-8");
        parameterMap.put("sign_type", this.alipayConfig.getSignType().name());
        parameterMap.put("timestamp", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        parameterMap.put("version", "1.0");
        parameterMap.put("notify_url", this.alipayConfig.getNotifyUrl());
        parameterMap.put("biz_content", JsonUtil.toJson(bizContentMap));

        //2. 签名
        Signature signature = new AlipayAppSignatureImpl(this.alipayConfig);
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

    /**
     * 同步返回
     *
     * @param request
     * @return
     */
    @Override
    public PayResponse syncNotify(HttpServletRequest request) {

        //构造返回对象
        PayResponse response = new PayResponse();
        response.setOrderId(request.getParameter("out_trade_no"));
        response.setTradeNo(request.getParameter("trade_no"));

        return response;
    }

    /**
     * 异步返回
     *
     * @param request
     * @return
     */
    @Override
    public PayResponse asyncNotify(HttpServletRequest request) {

        //交易状态
        String tradeStatus = request.getParameter("trade_status");
        //退款状态
        String refundStatus = request.getParameter("refund_status");

        if (tradeStatus != null && !tradeStatus.equals(AlipayTradeStatusEnum.TRADE_SUCCESS.name())) {
            throw new BestPayException(BestPayResultEnum.ALIPAY_TRADE_STATUS_IS_NOT_SUCCESS);
        }
        //忽略退款成功的消息(部分金额退款会发送交易成功的消息)
        if (refundStatus != null && refundStatus.equals(AlipayRefundStatusEnum.REFUND_SUCCESS.name())) {
            throw new BestPayException(BestPayResultEnum.ALIPAY_TRADE_STATUS_IS_NOT_SUCCESS);
        }

        //构造返回对象
        PayResponse response = new PayResponse();
        response.setOrderId(request.getParameter("out_trade_no"));
        response.setTradeNo(request.getParameter("trade_no"));
        response.setOrderAmount(Double.valueOf(request.getParameter("total_amount")));
        response.setBuyerId(request.getParameter("buyer_id"));
        response.setBuyerLogonId(request.getParameter("buyer_logon_id"));
        try {
            response.setPayTime(DateUtil.toDate(request.getParameter("gmt_payment")));
        } catch (Exception e) {
            throw new BestPayException(BestPayResultEnum.ALIPAY_TIME_FORMAT_ERROR);
        }

        return response;
    }
}
