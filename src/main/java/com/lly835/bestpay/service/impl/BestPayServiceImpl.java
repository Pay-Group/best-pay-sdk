package com.lly835.bestpay.service.impl;

import com.lly835.bestpay.config.SignType;
import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.enums.BestPayResultEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.exception.BestPayException;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.BestPayService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class BestPayServiceImpl extends AbstractComponent implements BestPayService {

    private WxPayH5Config wxPayH5Config;

    public void setWxPayH5Config(WxPayH5Config wxPayH5Config) {
        this.wxPayH5Config = wxPayH5Config;
    }

    @Override
    public PayResponse pay(PayRequest request) {
        //微信h5支付
        WxPayServiceImpl wxPayService = new WxPayServiceImpl();
        wxPayService.setWxPayH5Config(this.wxPayH5Config);

        return wxPayService.pay(request);
    }

    /**
     * 同步返回
     *
     * @param request
     * @return
     */
    public PayResponse syncNotify(HttpServletRequest request) {
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
    public PayResponse asyncNotify(String notifyData) {

        //微信h5支付
        WxPayServiceImpl wxPayService = new WxPayServiceImpl();
        wxPayService.setWxPayH5Config(this.wxPayH5Config);

        return wxPayService.asyncNotify(notifyData);
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

    @Override
    public RefundResponse refund(RefundRequest request) {
        //微信h5支付
        WxPayServiceImpl wxPayService = new WxPayServiceImpl();
        wxPayService.setWxPayH5Config(this.wxPayH5Config);
        return wxPayService.refund(request);
    }
}