package com.github.lly835.controller;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.wxpay.response.WxQrCode2WxResponse;
import com.lly835.bestpay.model.wxpay.response.WxQrCodeAsyncResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.lly835.bestpay.utils.JsonUtil;
import com.lly835.bestpay.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
    private BestPayServiceImpl bestPayService;

    @Autowired
    private HttpServletRequest request;


    /**
     * 发起支付
     */
    @GetMapping(value = "/pay")
    public ModelAndView pay(@RequestParam(value = "openid") String openid,
                            Map<String, Object> map) {
        PayRequest request = new PayRequest();
        Random random = new Random();

        //支付请求参数
        request.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        request.setOrderId(String.valueOf(random.nextInt(1000000000)));
        request.setOrderAmount(0.01);
        request.setOrderName("最好的支付sdk");
        request.setOpenid(openid);
        log.info("【发起支付】request={}", JsonUtil.toJson(request));

        PayResponse payResponse = bestPayService.pay(request);
        log.info("【发起支付】response={}", JsonUtil.toJson(payResponse));

        map.put("payResponse", payResponse);

        return new ModelAndView("pay/create", map);
    }

    /**
     * 发起支付
     */
    @GetMapping(value = "/aliay/pay")
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

        return new ModelAndView("pay/create", map);
    }

    /**
     * 异步回调
     */
    @PostMapping(value = "/notify")
    public ModelAndView notify(@RequestBody String notifyData) throws Exception {
        log.info("【异步回调】request={}", notifyData);
        PayResponse response = bestPayService.asyncNotify(notifyData);
        log.info("【异步回调】response={}", JsonUtil.toJson(response));
        return new ModelAndView("pay/success");
    }


    /**
     * 根据商品ID及openid生成订单并向微信发起预支付
     * @param productId
     * @param openid
     * @return
     */
    private PayResponse payByProductIdAndOpenId(String productId, String openid) {

        //TODO: 根据productid生成商户系统的订单
        // -- 实际情况下是应该根据productid和openid到数据库中查询/生成相应的订单)
        // -- 这里只是简单的做个设置

        log.info("【支付信息】 productId = {}, openid = {}",
                productId, openid);

        PayRequest request = new PayRequest();
        Random random = new Random();

        //支付请求参数
        request.setPayTypeEnum(BestPayTypeEnum.WXPAY_NATIVE);
        request.setOrderId(String.valueOf(random.nextInt(1000000000)));
        request.setOrderAmount(0.01);
        request.setOrderName("最好的支付sdk-扫码付模式1");
        request.setOpenid(openid);
        log.info("【发起支付-扫码模式1】request={}", JsonUtil.toJson(request));

        PayResponse payResponse = bestPayService.pay(request);
        log.info("【发起支付-扫码模式1】response={}", JsonUtil.toJson(payResponse));

        return payResponse;

    }

    /**
     * 扫码支付模式1入口
     * @return
     */
    @GetMapping(value = "/qr_pay_v1")
    public ModelAndView qrPayV1(Map<String, Object> map) {

        String productId = "10001"; //商品id:应该根据实际需要调整
        String payUrl = bestPayService.getQrCodeUrl(productId);
        map.put("payTitle", "扫码付-模式1");
        map.put("payUrl", payUrl);
        return new ModelAndView("pay/qrpayV1", map);
    }

    @PostMapping(value = "/qr_code_notify")
    @ResponseBody
    public String qrCodeNotify(@RequestBody String notifyData) {

        log.info("【扫码回调】request={}", notifyData);
        //解析回调数据
        WxQrCodeAsyncResponse response =  bestPayService.asyncQrCodeNotify(notifyData);
        log.info("【扫码回调解析】 request={}", response);


        //调用统一下单获取prepay_id
        PayResponse payResponse = payByProductIdAndOpenId(
                response.getProductId(),
                response.getOpenId());

        //需要对prepay_id进行一次处理
        payResponse.setPackAge(payResponse.getPackAge().substring(10));
        log.info("【预支付返回】prepay_id={}", payResponse.getPackAge());

        //返回数据给微信以进行下一步操作
        WxQrCode2WxResponse qrCode2WxResponse = bestPayService.buildQrCodeResponse(payResponse);

        String xml = XmlUtil.toString(qrCode2WxResponse);
        log.info("【扫码返回微信XML】{}", xml);

        return xml;
    }


    /**
     * 扫码付模式2的支付操作
     * @return
     */
    private PayResponse payQrV2() {

        PayRequest request = new PayRequest();
        Random random = new Random();

        //支付请求参数
        request.setPayTypeEnum(BestPayTypeEnum.WXPAY_NATIVE);
        request.setOrderId(String.valueOf(random.nextInt(1000000000)));
        request.setOrderAmount(0.02);
        request.setOrderName("最好的支付sdk-扫码付模式2");
        log.info("【发起支付-扫码模式2】request={}", JsonUtil.toJson(request));

        PayResponse payResponse = bestPayService.pay(request);
        log.info("【发起支付-扫码模式2】response={}", JsonUtil.toJson(payResponse));

        return payResponse;

    }

    @GetMapping(value = "/qr_pay_v2")
    public ModelAndView qrPayV2(Map<String, Object> map){
        log.info("【扫码支付模式2】调用统一支付生成预支付交易");

        PayResponse payResponse = payQrV2();

        map.put("payTitle", "扫码付-模式2");
        map.put("payUrl", payResponse.getCodeUrl());


        return new ModelAndView("pay/qrpayV1", map);
    }

}
