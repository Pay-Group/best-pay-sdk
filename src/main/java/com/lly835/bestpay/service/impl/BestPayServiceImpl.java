package com.lly835.bestpay.service.impl;

import com.lly835.bestpay.config.AlipayConfig;
import com.lly835.bestpay.enums.BestPayResultEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.exception.BestPayException;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.Signature;
import com.lly835.bestpay.service.impl.signatrue.AlipayAppSignatrueImpl;
import com.lly835.bestpay.service.impl.signatrue.AlipayPCSignatrueImpl;
import com.lly835.bestpay.utils.ServletRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by null on 2017/2/14.
 */
public class BestPayServiceImpl implements BestPayService{

    private final static Logger logger = LoggerFactory.getLogger(BestPayServiceImpl.class);

    private Map<BestPayTypeEnum, BestPayService> payServiceMap = new HashMap<>();

    private Map<BestPayTypeEnum, Signature> signatureMap = new HashMap<>();

    public BestPayServiceImpl() {
//        map.put(BestPayTypeEnum.ALIPAY_APP, new AlipayAppServiceImpl());
        payServiceMap.put(BestPayTypeEnum.ALIPAY_PC, new AlipayPCServiceImpl());
        payServiceMap.put(BestPayTypeEnum.ALIPAY_WAP, new AlipayWapServiceImpl());

        signatureMap.put(BestPayTypeEnum.ALIPAY_PC, new AlipayPCSignatrueImpl());
        signatureMap.put(BestPayTypeEnum.ALIPAY_WAP, new AlipayAppSignatrueImpl());
        signatureMap.put(BestPayTypeEnum.ALIPAY_APP, new AlipayAppSignatrueImpl());

        //检查配置参数
        AlipayConfig.check();
    }

    @Override
    public PayResponse pay(PayRequest request) throws Exception {

        //检查支付参数
        request.check();

        //判断是什么支付方式
        BestPayService alipayService = payServiceMap.get(request.getPayTypeEnum());
        return alipayService.pay(request);
    }

    /**
     * 同步返回
     * @param request
     * @return
     */
    @Override
    public PayResponse syncNotify(HttpServletRequest request) {

        //判断是否校验通过
        if (!this.verify(request)) {
            logger.error("【同步返回校验】签名验证不通过");
            throw new BestPayException(BestPayResultEnum.SYNC_SIGN_VERIFY_FAIL);
        }

        BestPayService bestPayService =  payServiceMap.get(this.payType(request));
        return bestPayService.syncNotify(request);
    }

    /**
     * 同步验证, 包含notify_id验证和签名验证
     * @return
     */
    public Boolean verify(HttpServletRequest request) {

        //String转换为Map
        Map<String, String> parameterMap = ServletRequestUtils.getParameterMap(request);

        //判断是什么支付类型
        Signature signature = signatureMap.get(this.payType(request));
        return signature.verify(parameterMap, parameterMap.get("sign"));
    }

    /**
     * 异步回调
     * @return
     */
    @Override
    public PayResponse asyncNotify(HttpServletRequest request) throws Exception {

        //判断是否校验通过
        if (!this.verify(request)) {
            logger.error("【异步返回校验】签名验证不通过");
            throw new BestPayException(BestPayResultEnum.ASYNC_SIGN_VERIFY_FAIL);
        }

        BestPayService bestPayService =  payServiceMap.get(this.payType(request));
        return bestPayService.asyncNotify(request);
    }

    /**
     * 判断是什么支付类型(从同步回调中获取参数)
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
        }else {
            //支付宝异步(发起支付时使用这个参数标识支付方式)
            String payType = request.getParameter("passback_params");
            return BestPayTypeEnum.getByCode(payType);
        }

        throw new BestPayException(BestPayResultEnum.PAY_TYPE_ERROR);
    }
}