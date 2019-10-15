package com.lly835.bestpay;

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.*;
import com.lly835.bestpay.model.wxpay.response.WxPayAsyncResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.lly835.bestpay.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Random;

/**
 * Created by 廖师兄
 * 2017-07-02 14:26
 */
@Slf4j
public class WxPayTest {

    private WxPayConfig wxPayConfig;

    private BestPayServiceImpl bestPayService = new BestPayServiceImpl();

    @Before
    public void init() {
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setNotifyUrl("http://127.0.0.1:8080");
        wxPayConfig.setReturnUrl("http://127.0.0.1:8080");
        wxPayConfig.setAppId("wxd898fcb01713c658");
        wxPayConfig.setAppSecret("xxxx");
        wxPayConfig.setMchId("1483469312");
        wxPayConfig.setMchKey("2301500e77a3a6705ccbf56bb2922656");
        wxPayConfig.setKeyPath("h5.p12");
        this.wxPayConfig = wxPayConfig;

        bestPayService.setWxPayConfig(wxPayConfig);
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
        request.setPayTypeEnum(BestPayTypeEnum.WXPAY_MP);
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
        request.setOrderId("4171207152120180517165324719749");
        request.setOrderAmount(5.51);
        RefundResponse response = bestPayService.refund(request);
        log.info(JsonUtil.toJson(response));
    }

    @Test
    public void query() {
        OrderQueryRequest request = new OrderQueryRequest();
        request.setPayTypeEnum(BestPayTypeEnum.WXPAY_MP);
        request.setOrderId("1528103259255858491");
        OrderQueryResponse response = bestPayService.query(request);
        log.info(JsonUtil.toJson(response));
    }

    /**
     * 解析对账文件示例
     * @param billFile
     */
    private void parseBill(String billFile) throws IOException{

        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(billFile.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
        String line;
//        StringBuffer strbuf=new StringBuffer();
        int count = 0;
        while ( (line = br.readLine()) != null ) {
            if(!line.trim().equals("")){

//                strbuf.append(line+"\r\n");
                log.info("第{}行数据: {}", count + 1, line);
                count ++;
            }
        }


    }

    @Test
    public void downloadBill() throws IOException{
        DownloadBillRequest request = new DownloadBillRequest();
        request.setBillDate("20190318");

        try {
            String response = bestPayService.downloadBill(request);
            log.info("【对账文件内容】 {}", response);

            //分析对账文件
            parseBill(response);
        } catch (RuntimeException e) {
            log.error("【对账文件获取失败】{}", e.getMessage());
            Assert.assertNull(e);
        }

    }

    @Test
    public void getQrCodeUrl() {

        String url = bestPayService.getQrCodeUrl("1001");
        log.info("【二维码URL】url={}", url);
    }

}
