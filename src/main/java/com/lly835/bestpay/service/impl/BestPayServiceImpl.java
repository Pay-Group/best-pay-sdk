package com.lly835.bestpay.service.impl;

import com.lly835.bestpay.config.AliDirectPayConfig;
import com.lly835.bestpay.config.AlipayConfig;
import com.lly835.bestpay.config.SignType;
import com.lly835.bestpay.enums.BestPayResultEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.exception.BestPayException;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BestPayServiceImpl extends AbstractComponent implements BestPayService {

    private Map<BestPayTypeEnum, BestPayService> payServiceMap = new HashMap<>();

    public BestPayServiceImpl(AlipayConfig alipayConfig, AliDirectPayConfig aliDirectPayConfig) {
        AlipaySignature appSignature = new AlipaySignature(alipayConfig);
        AlipayPCSignature pcSignature = new AlipayPCSignature(aliDirectPayConfig);

        payServiceMap.put(BestPayTypeEnum.ALIPAY_APP, new AlipayAppServiceImpl(alipayConfig, appSignature));
        payServiceMap.put(BestPayTypeEnum.ALIPAY_PC, new AlipayPCServiceImpl(aliDirectPayConfig, pcSignature));
        payServiceMap.put(BestPayTypeEnum.ALIPAY_WAP, new AlipayWapServiceImpl(alipayConfig, appSignature));
    }

    @Override
    public PayResponse pay(PayRequest request) throws Exception {

        //判断是什么支付方式
        BestPayService alipayService = payServiceMap.get(request.getPayTypeEnum());
        return alipayService.pay(request);
    }

    /**
     * 同步返回
     *
     * @param request
     * @return
     */
    public PayResponse syncNotify(HttpServletRequest request) {

        //判断是否校验通过
//        if (!this.verify(request)) {
//            logger.error("【同步返回校验】签名验证不通过");
//            throw new BestPayException(BestPayResultEnum.SYNC_SIGN_VERIFY_FAIL);
//        }
//
//        BestPayService bestPayService = payServiceMap.get(this.payType(request));
//        return bestPayService.syncNotify(request);
        return null;
    }

    @Override
    public boolean verify(Map<String, String> toBeVerifiedParamMap, SignType signType, String sign) {
        return false;
    }

    /**
     * 异步回调
     *
     * @return
     */
    public PayResponse asyncNotify(HttpServletRequest request) throws Exception {

        //判断是否校验通过
//        if (!this.verify(request)) {
//            logger.error("【异步返回校验】签名验证不通过");
//            throw new BestPayException(BestPayResultEnum.ASYNC_SIGN_VERIFY_FAIL);
//        }

        BestPayService bestPayService = payServiceMap.get(this.payType(request));
        return null;
    }

    /**
     * 判断是什么支付类型(从同步回调中获取参数)
     *
     * @param request
     * @return
     */
    private BestPayTypeEnum payType(HttpServletRequest request) {
        //先判断是微信还是支付宝 是否是xml
        //支付宝同步还是异步
        if (request.getParameter("notify_type") == null) {
            //支付宝同步
            if (request.getParameter("exterface") != null && request.getParameter("exterface").equals("create_direct_pay_by_user")) {
                return BestPayTypeEnum.ALIPAY_PC;
            }
            if (request.getParameter("method") != null && request.getParameter("method").equals("alipay.trade.wap.pay.return")) {
                return BestPayTypeEnum.ALIPAY_WAP;
            }
        } else {
            //支付宝异步(发起支付时使用这个参数标识支付方式)
            String payType = request.getParameter("passback_params");
            return BestPayTypeEnum.getByCode(payType);
        }

        throw new BestPayException(BestPayResultEnum.PAY_TYPE_ERROR);
    }

}