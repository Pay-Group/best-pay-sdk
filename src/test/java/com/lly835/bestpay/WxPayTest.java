package com.lly835.bestpay;

import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.model.wxpay.response.WxPayAsyncResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.lly835.bestpay.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by 廖师兄
 * 2017-07-02 14:26
 */
@Slf4j
public class WxPayTest {

    private WxPayH5Config wxPayH5Config;

    private BestPayServiceImpl bestPayService = new BestPayServiceImpl();

    @Before
    public void init() {
        WxPayH5Config wxPayH5Config = new WxPayH5Config();
        wxPayH5Config.setNotifyUrl("http://127.0.0.1:8080");
        wxPayH5Config.setReturnUrl("http://127.0.0.1:8080");
        wxPayH5Config.setAppId("wxd898fcb01713c658");
        wxPayH5Config.setAppSecret("xxxx");
        wxPayH5Config.setMchId("1483469312");
        wxPayH5Config.setMchKey("C5245D70627C1F8E9964D494B0735025");
        wxPayH5Config.setKeyPath("/Users/admin/Downloads/cert/h5.p12");
        this.wxPayH5Config = wxPayH5Config;

        bestPayService.setWxPayH5Config(wxPayH5Config);
    }

    public static void sync() throws Exception {
//        WxPayUnifiedorderResponse response = (WxPayUnifiedorderResponse) XmlUtil.fromXML("", WxPayUnifiedorderResponse.class);
//        System.out.println(response);

        Serializer serializer = new Persister();
        InputStream inputStream = new FileInputStream("/Users/admin/code/myProjects/java/best-pay-sdk/test.xml");

//        Map<String, String> map = (Map<String, String>)serializer.read(HashMap.class, inputStream);

        WxPayAsyncResponse response = serializer.read(WxPayAsyncResponse.class, inputStream);
        System.out.println(response);
    }

    @Test
    public void pay() {
        PayRequest request = new PayRequest();
        request.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        request.setOrderId("111111111222" + new Random().nextInt(1000));
        request.setOrderAmount(0.01);
        request.setOrderName("微信h5订单");
        request.setOpenid("oTgZpweNnfivA9ER9EIXoH-jlrWQ");

        PayResponse response = bestPayService.pay(request);
        System.out.println(JsonUtil.toJson(response));
    }

    @Test
    public void refund() {
        RefundRequest request = new RefundRequest();
        request.setOrderId("201122989784170657");
        request.setOrderAmount(0.02);
        RefundResponse response = bestPayService.refund(request);
        log.info(JsonUtil.toJson(response));
    }

}
