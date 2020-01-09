package com.github.lly835.controller;

import com.github.lly835.config.WechatAccountConfig;
import com.google.gson.Gson;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.*;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.lly835.bestpay.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * 支付相关
 * @version 1.0 2017/3/2
 * @auther <a href="mailto:lly835@163.com">廖师兄</a>
 * @since 1.0
 */
@Controller
@Slf4j
public class PayController {

    @Autowired
    private WechatAccountConfig wechatAccountConfig;

    @Autowired
    private BestPayServiceImpl bestPayService;

    /**
     * 发起支付
     */
    @PostMapping(value = "/pay")
    @ResponseBody
    public PayResponse pay(@RequestParam(value = "openid", required = false) String openid,
                           @RequestParam BestPayTypeEnum payType,
                           @RequestParam String orderId,
                           @RequestParam Double amount,
                           @RequestParam(required = false) String buyerLogonId,
                           @RequestParam(required = false) String buyerId) {
        //支付请求参数
        PayRequest request = new PayRequest();
        request.setPayTypeEnum(payType);
        request.setOrderId(orderId);
        request.setOrderAmount(amount);
        request.setOrderName("最好的支付sdk");
        request.setOpenid(openid);
        request.setAttach("这里是附加的信息");

        if (payType == BestPayTypeEnum.ALIPAY_H5) {
            request.setBuyerLogonId(buyerLogonId);
            request.setBuyerId(buyerId);
        }

        log.info("【发起支付】request={}", JsonUtil.toJson(request));

        PayResponse payResponse = bestPayService.pay(request);
        log.info("【发起支付】response={}", JsonUtil.toJson(payResponse));
        return payResponse;
    }

    /**
     * 微信h5支付，要求referer是白名单的地址，这里做个重定向
     * @param prepayId
     * @param packAge
     * @return
     */
    @GetMapping("/wxpay_mweb_redirect")
    public ModelAndView wxpayMweb(@RequestParam("prepay_id") String prepayId,
                                  @RequestParam("package") String packAge,
                                  Map map) {
        String url = String.format("https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?prepay_id=%s&package=%s", prepayId, packAge);
        map.put("url", url);
        return new ModelAndView("pay/wxpayMwebRedirect");
    }

    @GetMapping("/query")
	@ResponseBody
	public OrderQueryResponse query(@RequestParam String orderId,
                                    @RequestParam("platform") BestPayPlatformEnum platformEnum) {
		OrderQueryRequest orderQueryRequest = new OrderQueryRequest();
		orderQueryRequest.setOrderId(orderId);
		orderQueryRequest.setPlatformEnum(platformEnum);
		OrderQueryResponse queryResponse = bestPayService.query(orderQueryRequest);
		return queryResponse;
	}

    @GetMapping("/refund")
    @ResponseBody
    public RefundResponse refund(@RequestParam String orderId) {
        RefundRequest request = new RefundRequest();
        request.setOrderId(orderId);
        request.setPayTypeEnum(BestPayTypeEnum.WXPAY_MWEB);
        request.setOrderAmount(0.1);
        RefundResponse response = bestPayService.refund(request);
        return response;
    }

    /**
     * 小程序支付
     * @param code
     * @return
     */
    @GetMapping(value = "/mini_pay")
    @ResponseBody
    public PayResponse minipay(@RequestParam(value = "code") String code){

        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+wechatAccountConfig.getMiniAppId()+"&secret="+wechatAccountConfig.getMiniAppSecret()+"&js_code="+code+"&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String userInfo = restTemplate.getForObject(url, String.class);

        Random random = new Random();
        DateTime dateTime = new DateTime(new Date());
        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(((String) new Gson().fromJson(userInfo, Map.class).get("openid")));
        payRequest.setOrderAmount(0.01);
        payRequest.setOrderId(System.currentTimeMillis() + String.valueOf(random.nextInt(900000) + 100000)+dateTime.toString("yyyymmdd")+String.valueOf(random.nextInt(90000) + 10000));
        payRequest.setOrderName("小程序支付");
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_MINI);
        log.info("【发起支付】request={}", JsonUtil.toJson(payRequest));
        PayResponse payResponse = bestPayService.pay(payRequest);
        log.info("【发起支付】response={}", JsonUtil.toJson(payResponse));
        return payResponse;
    }

    /**
     * 发起支付
     */
    @GetMapping(value = "/alipay/pay")
    public ModelAndView aliPay(Map<String, Object> map) {
        PayRequest request = new PayRequest();
        Random random = new Random();
        //支付请求参数
        request.setPayTypeEnum(BestPayTypeEnum.ALIPAY_PC);
        request.setOrderId(String.valueOf(random.nextInt(1000000000)));
        request.setOrderAmount(0.01);
        request.setOrderName("最好的支付sdk");
        log.info("【发起支付】request={}", JsonUtil.toJson(request));
        PayResponse payResponse = bestPayService.pay(request);
        log.info("【发起支付】response={}", JsonUtil.toJson(payResponse));
        map.put("payResponse", payResponse);

        return new ModelAndView("pay/alipayPc", map);
    }

    /**
     * 异步回调
     */
    @PostMapping(value = "/notify")
    public ModelAndView notify(@RequestBody String notifyData) {
        log.info("【异步通知】支付平台的数据request={}", notifyData);
        PayResponse response = bestPayService.asyncNotify(notifyData);
        log.info("【异步通知】处理后的数据data={}", JsonUtil.toJson(response));

        //返回成功信息给支付平台，否则会不停的异步通知
        if (response.getPayPlatformEnum() == BestPayPlatformEnum.WX) {
            return new ModelAndView("pay/responeSuccessForWx");
        }else if (response.getPayPlatformEnum() == BestPayPlatformEnum.ALIPAY) {
            return new ModelAndView("pay/responeSuccessForAlipay");
        }
        throw new RuntimeException("错误的支付平台");
    }

    @GetMapping("/pay/close")
    @ResponseBody
    public CloseResponse close(@RequestParam String orderId) {
        CloseRequest request = new CloseRequest();
        request.setPayTypeEnum(BestPayTypeEnum.ALIPAY_PC);
        request.setOrderId(orderId);

        CloseResponse close = bestPayService.close(request);
        return close;
    }
}
