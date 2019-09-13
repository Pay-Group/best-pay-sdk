package com.lly835.bestpay.service.impl;

import com.lly835.bestpay.config.SignType;
import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.constants.WxPayConstants;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.*;
import com.lly835.bestpay.model.wxpay.WxPayApi;
import com.lly835.bestpay.model.wxpay.request.WxDownloadBillRequest;
import com.lly835.bestpay.model.wxpay.request.WxOrderQueryRequest;
import com.lly835.bestpay.model.wxpay.request.WxPayRefundRequest;
import com.lly835.bestpay.model.wxpay.request.WxPayUnifiedorderRequest;
import com.lly835.bestpay.model.wxpay.response.*;
import com.lly835.bestpay.utils.MapUtil;
import com.lly835.bestpay.utils.MoneyUtil;
import com.lly835.bestpay.utils.RandomUtil;
import com.lly835.bestpay.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 廖师兄
 * 2017-07-02 13:40
 */
@Slf4j
public class WxPayServiceImpl extends BestPayServiceImpl {

    private WxPayH5Config wxPayH5Config;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(WxPayConstants.WXPAY_GATEWAY)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .client(new OkHttpClient.Builder()
                    .addInterceptor((new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY)))
                    .build()
            )
            .build();

    public void setWxPayH5Config(WxPayH5Config wxPayH5Config) {
        this.wxPayH5Config = wxPayH5Config;
    }

    @Override
    public PayResponse pay(PayRequest request) {
        WxPayUnifiedorderRequest wxRequest = new WxPayUnifiedorderRequest();
        wxRequest.setOutTradeNo(request.getOrderId());
        wxRequest.setTotalFee(MoneyUtil.Yuan2Fen(request.getOrderAmount()));
        wxRequest.setBody(request.getOrderName());
        wxRequest.setOpenid(request.getOpenid());

        wxRequest.setTradeType(switchH5TradeType(request.getPayTypeEnum()));
        wxRequest.setAppid(wxPayH5Config.getAppId());
        wxRequest.setMchId(wxPayH5Config.getMchId());
        wxRequest.setNotifyUrl(wxPayH5Config.getNotifyUrl());
        wxRequest.setNonceStr(RandomUtil.getRandomStr());
        wxRequest.setSpbillCreateIp(request.getSpbillCreateIp() == null || request.getSpbillCreateIp().isEmpty() ? "8.8.8.8" : request.getSpbillCreateIp());
        wxRequest.setSign(WxPaySignature.sign(MapUtil.buildMap(wxRequest), wxPayH5Config.getMchKey()));

        RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), XmlUtil.toString(wxRequest));
        Call<WxPaySyncResponse> call = retrofit.create(WxPayApi.class).unifiedorder(body);
        Response<WxPaySyncResponse> retrofitResponse  = null;
        try{
            retrofitResponse = call.execute();
        }catch (IOException e) {
            e.printStackTrace();
        }
        if (!retrofitResponse.isSuccessful()) {
            throw new RuntimeException("【微信统一支付】发起支付, 网络异常");
        }
        WxPaySyncResponse response = retrofitResponse.body();

        if(!response.getReturnCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信统一支付】发起支付, returnCode != SUCCESS, returnMsg = " + response.getReturnMsg());
        }
        if (!response.getResultCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信统一支付】发起支付, resultCode != SUCCESS, err_code = " + response.getErrCode() + " err_code_des=" + response.getErrCodeDes());
        }

        return buildPayResponse(response);
    }

    @Override
    public boolean verify(Map map, SignType signType, String sign) {
        return WxPaySignature.verify(map, wxPayH5Config.getMchKey());
    }

    @Override
    public PayResponse syncNotify(HttpServletRequest request) {
        return null;
    }

    /**
     * 异步通知
     * @param notifyData
     * @return
     */
    @Override
    public PayResponse asyncNotify(String notifyData) {
        //签名校验
        if (!WxPaySignature.verify(XmlUtil.toMap(notifyData), wxPayH5Config.getMchKey())) {
            log.error("【微信支付异步通知】签名验证失败, response={}", notifyData);
            throw new RuntimeException("【微信支付异步通知】签名验证失败");
        }

        //xml解析为对象
        WxPayAsyncResponse asyncResponse = (WxPayAsyncResponse) XmlUtil.toObject(notifyData, WxPayAsyncResponse.class);

        if(!asyncResponse.getReturnCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信支付异步通知】发起支付, returnCode != SUCCESS, returnMsg = " + asyncResponse.getReturnMsg());
        }
        //该订单已支付直接返回
        if (!asyncResponse.getResultCode().equals(WxPayConstants.SUCCESS)
                && asyncResponse.getErrCode().equals("ORDERPAID")) {
            return buildPayResponse(asyncResponse);
        }

        if (!asyncResponse.getResultCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信支付异步通知】发起支付, resultCode != SUCCESS, err_code = " + asyncResponse.getErrCode() + " err_code_des=" + asyncResponse.getErrCodeDes());
        }

        return buildPayResponse(asyncResponse);
    }

    /**
     * 微信退款
     * @param request
     * @return
     */
    public RefundResponse refund(RefundRequest request) {
        WxPayRefundRequest wxRequest = new WxPayRefundRequest();
        wxRequest.setOutTradeNo(request.getOrderId());
        wxRequest.setOutRefundNo(request.getOrderId());
        wxRequest.setTotalFee(MoneyUtil.Yuan2Fen(request.getOrderAmount()));
        wxRequest.setRefundFee(MoneyUtil.Yuan2Fen(request.getOrderAmount()));

        wxRequest.setAppid(wxPayH5Config.getAppId());
        wxRequest.setMchId(wxPayH5Config.getMchId());
        wxRequest.setNonceStr(RandomUtil.getRandomStr());
        wxRequest.setSign(WxPaySignature.sign(MapUtil.buildMap(wxRequest), wxPayH5Config.getMchKey()));

        //初始化证书
        if (wxPayH5Config.getSslContext() == null) {
            wxPayH5Config.initSSLContext();
        }
        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .sslSocketFactory(wxPayH5Config.getSslContext().getSocketFactory())
                .addInterceptor((new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WxPayConstants.WXPAY_GATEWAY)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(okHttpClient)
                .build();
        String xml = XmlUtil.toString(wxRequest);
        RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"),xml);
        Call<WxRefundResponse> call = retrofit.create(WxPayApi.class).refund(body);
        Response<WxRefundResponse> retrofitResponse  = null;
        try{
            retrofitResponse = call.execute();
        }catch (IOException e) {
            e.printStackTrace();
        }
        if (!retrofitResponse.isSuccessful()) {
            throw new RuntimeException("【微信退款】发起退款, 网络异常");
        }
        WxRefundResponse response = retrofitResponse.body();

        if(!response.getReturnCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信退款】发起退款, returnCode != SUCCESS, returnMsg = " + response.getReturnMsg());
        }
        if (!response.getResultCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信退款】发起退款, resultCode != SUCCESS, err_code = " + response.getErrCode() + " err_code_des=" + response.getErrCodeDes());
        }

        return buildRefundResponse(response);
    }

    /**
     * 查询订单
     *
     * @param request
     * @return
     */
    @Override
    public OrderQueryResponse query(OrderQueryRequest request) {
        WxOrderQueryRequest wxRequest = new WxOrderQueryRequest();
        wxRequest.setOutTradeNo(request.getOrderId());
        wxRequest.setTransactionId(request.getOutOrderId());

        wxRequest.setAppid(wxPayH5Config.getAppId());
        wxRequest.setMchId(wxPayH5Config.getMchId());
        wxRequest.setNonceStr(RandomUtil.getRandomStr());
        wxRequest.setSign(WxPaySignature.sign(MapUtil.buildMap(wxRequest), wxPayH5Config.getMchKey()));
        RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), XmlUtil.toString(wxRequest));

        Call<WxOrderQueryResponse> call = retrofit.create(WxPayApi.class).orderquery(body);
        Response<WxOrderQueryResponse> retrofitResponse  = null;
        try{
            retrofitResponse = call.execute();
        }catch (IOException e) {
            e.printStackTrace();
        }
        if (!retrofitResponse.isSuccessful()) {
            throw new RuntimeException("【微信订单查询】网络异常");
        }
        WxOrderQueryResponse response = retrofitResponse.body();
        if(!response.getReturnCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信订单查询】returnCode != SUCCESS, returnMsg = " + response.getReturnMsg());
        }
        if (!response.getResultCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信订单查询】resultCode != SUCCESS, err_code = " + response.getErrCode() + ", err_code_des=" + response.getErrCodeDes());
        }

        return OrderQueryResponse.builder()
                .orderStatusEnum(OrderStatusEnum.findByName(response.getTradeState()))
                .resultMsg(response.getTradeStateDesc() == null ? "" : response.getTradeStateDesc())
                .build();
    }

    private RefundResponse buildRefundResponse(WxRefundResponse response) {
        RefundResponse refundResponse = new RefundResponse();
        refundResponse.setOrderId(response.getOutTradeNo());
        refundResponse.setOrderAmount(MoneyUtil.Fen2Yuan(response.getTotalFee()));
        refundResponse.setOutTradeNo(response.getTransactionId());
        refundResponse.setRefundId(response.getOutRefundNo());
        refundResponse.setOutRefundNo(response.getRefundId());
        return refundResponse;
    }

    private PayResponse buildPayResponse(WxPayAsyncResponse response) {
        PayResponse payResponse = new PayResponse();
        payResponse.setOrderAmount(MoneyUtil.Fen2Yuan(response.getTotalFee()));
        payResponse.setOrderId(response.getOutTradeNo());
        payResponse.setOutTradeNo(response.getTransactionId());
        payResponse.setMwebUrl(response.getMwebUrl());
        return payResponse;
    }

    /**
     * 返回给h5的参数
     * @param response
     * @return
     */
    private PayResponse buildPayResponse(WxPaySyncResponse response) {
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = RandomUtil.getRandomStr();
        String packAge = "prepay_id=" + response.getPrepayId();
        String signType = "MD5";

        //先构造要签名的map
        Map<String, String> map = new HashMap<>();
        map.put("appId", response.getAppid());
        map.put("timeStamp", timeStamp);
        map.put("nonceStr", nonceStr);
        map.put("package", packAge);
        map.put("signType", signType);

        PayResponse payResponse = new PayResponse();
        payResponse.setAppId(response.getAppid());
        payResponse.setTimeStamp(timeStamp);
        payResponse.setNonceStr(nonceStr);
        payResponse.setPackAge(packAge);
        payResponse.setSignType(signType);
        payResponse.setPaySign(WxPaySignature.sign(map, wxPayH5Config.getMchKey()));
        payResponse.setMwebUrl(response.getMwebUrl());

        return payResponse;
    }


    /**
     * H5支付交易类型选择
     */
    public String switchH5TradeType(BestPayTypeEnum payTypeEnum){
        String tradeType = "JSAPI";
        switch (payTypeEnum){
            case WXPAY_H5:
                tradeType = "JSAPI";
                break;
            case WXPAY_MINI:
                tradeType = "JSAPI";
                break;
            case WXPAY_MWEB:
                tradeType = "MWEB";
                break;
            default:
                tradeType = payTypeEnum.getCode();
                break;
        }
        return tradeType;
    }

    /**
     *
     * @param request
     * @return
     */
    @Override
    public String downloadBill(DownloadBillRequest request) {

        WxDownloadBillRequest wxRequest = new WxDownloadBillRequest();
        wxRequest.setBillDate(request.getBillDate());

        wxRequest.setAppid(wxPayH5Config.getAppId());
        wxRequest.setMchId(wxPayH5Config.getMchId());
        wxRequest.setNonceStr(RandomUtil.getRandomStr());
        wxRequest.setSign(WxPaySignature.sign(MapUtil.buildMap(wxRequest), wxPayH5Config.getMchKey()));
        RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), XmlUtil.toString(wxRequest));

        Call<ResponseBody> call = retrofit.create(WxPayApi.class).downloadBill(body);
        Response<ResponseBody> retrofitResponse  = null;
        try{
            retrofitResponse = call.execute();
        }catch (IOException e) {
            e.printStackTrace();
        }

        if (!retrofitResponse.isSuccessful()) {
            throw new RuntimeException("【微信订单查询】网络异常");
        }

        String response = null;
        try {
            response = retrofitResponse.body().string();

            //如果返回xml格式，表示返回异常
            if(response.startsWith("<")) {
                WxDownloadBillResponse downloadBillResponse =  (WxDownloadBillResponse)XmlUtil.toObject(response,
                        WxDownloadBillResponse.class);
                throw new RuntimeException("【对账文件】返回异常 错误码: " +
                        downloadBillResponse.getErrorCode() +
                        " 错误信息: " + downloadBillResponse.getReturnMsg());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return response;
    }

    /**
     * 根据微信规则生成扫码二维码的URL
     * @return
     */
    @Override
    public String getQrCodeUrl(String productId) {

        String appid = wxPayH5Config.getAppId();
        String mch_id = wxPayH5Config.getMchId();
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = RandomUtil.getRandomStr();


        //先构造要签名的map
        Map<String, String> map = new HashMap<>();
        map.put("appid", appid);
        map.put("mch_id", mch_id);
        map.put("product_id", productId);
        map.put("time_stamp", timeStamp);
        map.put("nonce_str", nonceStr);


        String url = "weixin://wxpay/bizpayurl?"
                + "appid=" + appid
                + "&mch_id=" + mch_id
                + "&product_id=" + productId
                + "&time_stamp=" + timeStamp
                + "&nonce_str=" + nonceStr
                + "&sign=" + WxPaySignature.sign(map, wxPayH5Config.getMchKey());




        return url;
    }

    @Override
    public WxQrCodeAsyncResponse asyncQrCodeNotify(String notifyData) {

        //签名校验
        if (!WxPaySignature.verify(XmlUtil.toMap(notifyData), wxPayH5Config.getMchKey())) {
            log.error("【微信支付异步通知】签名验证失败, response={}", notifyData);
            throw new RuntimeException("【微信支付异步通知】签名验证失败");
        }

        //xml解析为对象
        WxQrCodeAsyncResponse asyncQrCodeResponse = (WxQrCodeAsyncResponse) XmlUtil.toObject(notifyData, WxQrCodeAsyncResponse.class);


        return asyncQrCodeResponse;
    }

    public WxQrCode2WxResponse buildQrCodeResponse(PayResponse payResponse) {
        WxQrCode2WxResponse response = new WxQrCode2WxResponse();
        response.setReturnCode("SUCCESS");
//        response.setReturnMsg("");
        response.setAppid(wxPayH5Config.getAppId());
        response.setMchId(wxPayH5Config.getMchId());
        response.setNonceStr(payResponse.getNonceStr());
        response.setPrepayId(payResponse.getPackAge());
        response.setResultCode("SUCCESS");
//        response.setErrCodeDes("");


        //先构造要签名的map
        Map<String, String> map = new HashMap<>();
        map.put("return_code", response.getReturnCode());
//        map.put("return_msg", response.getReturnMsg());
        map.put("appid", response.getAppid());
        map.put("mch_id", response.getMchId());
        map.put("nonce_str", response.getNonceStr());
        map.put("prepay_id", response.getPrepayId());
        map.put("result_code", response.getResultCode());
//        map.put("err_code_des", response.getErrCodeDes());

        response.setSign(WxPaySignature.sign(map, wxPayH5Config.getMchKey()));

        return response;
    }

}
