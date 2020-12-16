package com.lly835.bestpay.service.impl.alipay;

import com.lly835.bestpay.constants.AliPayConstants;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.alipay.request.AliPayTradeCreateRequest;
import com.lly835.bestpay.utils.JsonUtil;
import com.lly835.bestpay.utils.MapUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * @author zicheng
 * @date 2020/12/2
 * Description: https://opendocs.alipay.com/apis/api_1/alipay.trade.app.pay
 */
@Slf4j
public class AlipayAppServiceImpl extends AliPayServiceImpl {

    @Override
    public PayResponse pay(PayRequest request) {
        AliPayTradeCreateRequest payRequest = new AliPayTradeCreateRequest();
        payRequest.setMethod(AliPayConstants.ALIPAY_TRADE_APP_PAY);
        payRequest.setAppId(aliPayConfig.getAppId());
        payRequest.setTimestamp(LocalDateTime.now().format(formatter));
        payRequest.setNotifyUrl(aliPayConfig.getNotifyUrl());
        AliPayTradeCreateRequest.BizContent bizContent = new AliPayTradeCreateRequest.BizContent();
        bizContent.setOutTradeNo(request.getOrderId());
        bizContent.setTotalAmount(request.getOrderAmount());
        bizContent.setSubject(request.getOrderName());
        bizContent.setIsAsyncPay(true);

        payRequest.setBizContent(JsonUtil.toJsonWithUnderscores(bizContent).replaceAll("\\s*", ""));
        payRequest.setSign(AliPaySignature.sign(MapUtil.object2MapWithUnderline(payRequest), aliPayConfig.getPrivateKey()));

        PayResponse payResponse = new PayResponse();
        payResponse.setOrderInfo(MapUtil.toUrlWithSortAndEncode(MapUtil.removeEmptyKeyAndValue(MapUtil.object2MapWithUnderline(payRequest))));
        return payResponse;
    }
}
