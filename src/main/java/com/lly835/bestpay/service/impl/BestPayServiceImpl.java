package com.lly835.bestpay.service.impl;

import com.lly835.bestpay.config.AlipayConfig;
import com.lly835.bestpay.constants.AlipayConstants;
import com.lly835.bestpay.encrypt.RSA;
import com.lly835.bestpay.enums.BestPayResultEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.exception.BestPayException;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.utils.HttpRequestUtil;
import com.lly835.bestpay.utils.MapUtil;
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

    private Map<BestPayTypeEnum, BestPayService> map = new HashMap<>();

    public BestPayServiceImpl() {
//        map.put(BestPayTypeEnum.ALIPAY_APP, new AlipayAppServiceImpl());
        map.put(BestPayTypeEnum.ALIPAY_PC, new AlipayPCServiceImpl());
        map.put(BestPayTypeEnum.ALIPAY_WAP, new AlipayWapServiceImpl());

        //检查配置参数
        AlipayConfig.check();
    }

    @Override
    public PayResponse pay(PayRequest request) throws Exception {

        //检查支付参数
        request.check();

        //判断是什么支付方式
        BestPayService alipayService = map.get(request.getPayTypeEnum());
        return alipayService.pay(request);
    }

    /**
     * 同步返回
     * @param request
     * @return
     */
    @Override
    public PayResponse syncNotify(HttpServletRequest request) {


        //String转换为Map
        Map<String, String> parameterMap = ServletRequestUtils.getParameterMap(request);

        String sign = parameterMap.get("sign");

        //去除不参与签名的参数
        parameterMap = MapUtil.removeParamsForAlipaySign(parameterMap);
        //Map转Url
        String content = MapUtil.toUrlWithSort(parameterMap);
        logger.info("【支付宝web端支付验证签名】要验证的签名内容:{}", content);

        logger.info("【支付宝web端支付验证签名】支付宝传过来的sign={}", sign);

        logger.info("【支付宝web端支付验证签名】验证签名使用的key={}", AlipayConfig.getPartnerPublicKey());

        //使用公钥验证
        boolean flag = RSA.verify(content, sign, AlipayConfig.getPartnerPublicKey(), AlipayConfig.getInputCharset());
 
        if(!flag){
            logger.error("【支付宝web端支付验证签名】验证签名失败");
            throw new BestPayException(BestPayResultEnum.ALIPAY_ASYNC_SIGN_VERIFY_FAIL);
        }

        logger.debug("【支付宝web端支付验证签名】验证notifyId和签名成功");
        return null;
    }

    /**
     * 同步验证, 包含notify_id验证和签名验证
     * @return
     */
    public Boolean verify(HttpServletRequest request) {
        //先验证notify_id, 这个是不区分的
        //支付宝消息校验
        boolean notifyIdFlag =  this.verifyByNotifyId(request.getParameter("notify_id"));
        if(!notifyIdFlag){
//            logger.error("【支付宝web端支付验证签名】验证notifyId失败");
//            throw new BestPayException(BestPayResultEnum.ALIPAY_NOTIFY_ID_VERIFY_FAIL);
        }

        //区分是即时到账还是手机网站支付
        if (request.getParameter("exterface").equals("create_direct_pay_by_user")) {
            //即时到账

        }else if (request.getParameter("method").equals("alipay.trade.wap.pay.return")) {
            //手机网站支付

        }

       return true;
    }

    /**
     * 验证notifyId, 注意notifyId的时效性大约为1分钟
     * @param notifyId
     * @return
     */
    private Boolean verifyByNotifyId(String notifyId) {
        String veryfyUrl = AlipayConstants.ALIPAY_VERIFY_URL + "partner=" + AlipayConfig.getPartnerId() + "&notify_id=" + notifyId;
        logger.debug("【支付宝消息id检验】veryfyUrl={}", veryfyUrl);

        String result = HttpRequestUtil.get(veryfyUrl);
        logger.debug("【支付宝消息id检验】支付宝返回的信息, result={}", result);

        if(result.equals("true")){
            return true;
        }

        return false;
    }
}
