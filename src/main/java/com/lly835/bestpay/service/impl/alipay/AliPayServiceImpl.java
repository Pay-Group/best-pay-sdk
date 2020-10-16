package com.lly835.bestpay.service.impl.alipay;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.config.SignType;
import com.lly835.bestpay.constants.AliPayConstants;
import com.lly835.bestpay.enums.AlipayTradeStatusEnum;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.*;
import com.lly835.bestpay.model.alipay.AliPayApi;
import com.lly835.bestpay.model.alipay.request.*;
import com.lly835.bestpay.model.alipay.response.*;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.lly835.bestpay.utils.JsonUtil;
import com.lly835.bestpay.utils.MapUtil;
import com.lly835.bestpay.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by this on 2019/9/8 15:50
 */
@Slf4j
public class AliPayServiceImpl extends BestPayServiceImpl {

    protected final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    protected AliPayConfig aliPayConfig;

    @Override
    public void setAliPayConfig(AliPayConfig aliPayConfig) {
        this.aliPayConfig = aliPayConfig;
    }

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AliPayConstants.ALIPAY_GATEWAY_OPEN)
            .addConverterFactory(GsonConverterFactory.create(
                    //下划线驼峰互转
                    new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
            ))
            .client(new OkHttpClient.Builder()
                    .addInterceptor((new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY)))
                    .build()
            )
            .build();

    private Retrofit devRetrofit = new Retrofit.Builder()
            .baseUrl(AliPayConstants.ALIPAY_GATEWAY_OPEN_DEV)
            .addConverterFactory(GsonConverterFactory.create(
                    //下划线驼峰互转
                    new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
            ))
            .client(new OkHttpClient.Builder()
                    .addInterceptor((new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY)))
                    .build()
            )
            .build();

    @Override
    public PayResponse pay(PayRequest request) {
        if (request.getPayTypeEnum() == BestPayTypeEnum.ALIPAY_H5) {
            AlipayH5ServiceImpl alipayH5Service = new AlipayH5ServiceImpl();
            alipayH5Service.setAliPayConfig(aliPayConfig);
            return alipayH5Service.pay(request);
        } else if (request.getPayTypeEnum() == BestPayTypeEnum.ALIPAY_QRCODE) {
            AlipayQRCodeServiceImpl alipayQRCodeService = new AlipayQRCodeServiceImpl();
            alipayQRCodeService.setAliPayConfig(aliPayConfig);
            return alipayQRCodeService.pay(request);
        } else if (request.getPayTypeEnum() == BestPayTypeEnum.ALIPAY_BARCODE) {
            AlipayBarCodeServiceImpl alipayBarCodeService = new AlipayBarCodeServiceImpl();
            alipayBarCodeService.setAliPayConfig(aliPayConfig);
            return alipayBarCodeService.pay(request);
        }
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put("out_trade_no", request.getOrderId());
        AliPayPcRequest aliPayRequest = new AliPayPcRequest();
        if (request.getPayTypeEnum() == BestPayTypeEnum.ALIPAY_PC) {
            requestParams.put("product_code", AliPayConstants.FAST_INSTANT_TRADE_PAY);
            aliPayRequest.setMethod(AliPayConstants.ALIPAY_TRADE_PAGE_PAY);
        } else {
            requestParams.put("product_code", AliPayConstants.QUICK_WAP_PAY);
            aliPayRequest.setMethod(AliPayConstants.ALIPAY_TRADE_WAP_PAY);
        }
        requestParams.put("total_amount", String.valueOf(request.getOrderAmount()));
        requestParams.put("subject", String.valueOf(request.getOrderName()));

        aliPayRequest.setAppId(aliPayConfig.getAppId());
        aliPayRequest.setCharset("utf-8");
        aliPayRequest.setSignType(AliPayConstants.SIGN_TYPE_RSA2);
        aliPayRequest.setNotifyUrl(aliPayConfig.getNotifyUrl());
        //优先使用PayRequest.returnUrl
        aliPayRequest.setReturnUrl(StringUtils.isEmpty(request.getReturnUrl()) ? aliPayConfig.getReturnUrl() : request.getReturnUrl());
        aliPayRequest.setTimestamp(LocalDateTime.now().format(formatter));
        aliPayRequest.setVersion("1.0");
        // 剔除空格、制表符、换行
        aliPayRequest.setBizContent(JsonUtil.toJson(requestParams).replaceAll("\\s*", ""));
        aliPayRequest.setSign(AliPaySignature.sign(MapUtil.object2MapWithUnderline(aliPayRequest), aliPayConfig.getPrivateKey()));

        Map<String, String> parameters = MapUtil.object2MapWithUnderline(aliPayRequest);
        Map<String, String> applicationParams = new HashMap<>();
        applicationParams.put("biz_content", aliPayRequest.getBizContent());
        parameters.remove("biz_content");
        String baseUrl = WebUtil.getRequestUrl(parameters, aliPayConfig.isSandbox());
        String body = WebUtil.buildForm(baseUrl, applicationParams);

        // pc 网站支付 只需返回body
        PayResponse response = new PayResponse();
        response.setBody(body);
        return response;
    }


    @Override
    public boolean verify(Map<String, String> toBeVerifiedParamMap, SignType signType, String sign) {
        return AliPaySignature.verify(toBeVerifiedParamMap, aliPayConfig.getAliPayPublicKey());
    }

    /**
     * 异步通知
     *
     * @param notifyData
     * @return
     */
    @Override
    public PayResponse asyncNotify(String notifyData) {
        try {
            notifyData = URLDecoder.decode(notifyData, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //签名校验
        if (!AliPaySignature.verify(MapUtil.form2Map(notifyData), aliPayConfig.getAliPayPublicKey())) {
            log.error("【支付宝支付异步通知】签名验证失败, response={}", notifyData);
            throw new RuntimeException("【支付宝支付异步通知】签名验证失败");
        }
        HashMap<String, String> params = MapUtil.form2MapWithCamelCase(notifyData);
        AliPayAsyncResponse response = MapUtil.mapToObject(params, AliPayAsyncResponse.class);
        String tradeStatus = response.getTradeStatus();
        if (!tradeStatus.equals(AliPayConstants.TRADE_FINISHED) &&
                !tradeStatus.equals(AliPayConstants.TRADE_SUCCESS)) {
            throw new RuntimeException("【支付宝支付异步通知】发起支付, trade_status != SUCCESS | FINISHED");
        }
        return buildPayResponse(response);
    }

    @Override
    public RefundResponse refund(RefundRequest request) {
        AliPayOrderRefundRequest aliPayOrderRefundRequest = new AliPayOrderRefundRequest();
        aliPayOrderRefundRequest.setAppId(aliPayConfig.getAppId());
        aliPayOrderRefundRequest.setTimestamp(LocalDateTime.now().format(formatter));
        AliPayOrderRefundRequest.BizContent bizContent = new AliPayOrderRefundRequest.BizContent();
        bizContent.setOutTradeNo(request.getOrderId());
        bizContent.setRefundReason(request.getRefundReason());
        bizContent.setRefundAmount(request.getOrderAmount());
        bizContent.setOutRequestNo(request.getOutRequestNo());
        aliPayOrderRefundRequest.setBizContent(JsonUtil.toJsonWithUnderscores(bizContent).replaceAll("\\s*", ""));
        aliPayOrderRefundRequest.setSign(AliPaySignature.sign(MapUtil.object2MapWithUnderline(aliPayOrderRefundRequest), aliPayConfig.getPrivateKey()));

        Call<AliPayOrderRefundResponse> call = null;
        if (aliPayConfig.isSandbox()) {
            call = devRetrofit.create(AliPayApi.class).refund((MapUtil.object2MapWithUnderline(aliPayOrderRefundRequest)));
        } else {
            call = retrofit.create(AliPayApi.class).refund((MapUtil.object2MapWithUnderline(aliPayOrderRefundRequest)));
        }

        Response<AliPayOrderRefundResponse> retrofitResponse = null;
        try {
            retrofitResponse = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert retrofitResponse != null;
        if (!retrofitResponse.isSuccessful()) {
            throw new RuntimeException("【支付宝退款】网络异常");
        }
        assert retrofitResponse.body() != null;
        AliPayOrderRefundResponse.AlipayTradeRefundResponse response = retrofitResponse.body().getAlipayTradeRefundResponse();
        if (!response.getCode().equals(AliPayConstants.RESPONSE_CODE_SUCCESS)) {
            throw new RuntimeException("【支付宝退款】code=" + response.getCode() + ", returnMsg=" + response.getMsg() + String.format("|%s|%s", response.getSubCode(), response.getSubMsg()));
        }

        return RefundResponse.builder()
                .outTradeNo(response.getTradeNo())
                .orderId(response.getOutTradeNo())
                .outRefundNo(response.getTradeNo())
                .orderAmount(response.getRefundFee())
                .build();
    }

    @Override
    public OrderQueryResponse query(OrderQueryRequest request) {
        AliPayOrderQueryRequest aliPayOrderQueryRequest = new AliPayOrderQueryRequest();
        aliPayOrderQueryRequest.setAppId(aliPayConfig.getAppId());
        aliPayOrderQueryRequest.setTimestamp(LocalDateTime.now().format(formatter));
        AliPayOrderQueryRequest.BizContent bizContent = new AliPayOrderQueryRequest.BizContent();
        bizContent.setOutTradeNo(request.getOrderId());
        bizContent.setTradeNo(request.getOutOrderId());
        aliPayOrderQueryRequest.setBizContent(JsonUtil.toJsonWithUnderscores(bizContent).replaceAll("\\s*", ""));
        aliPayOrderQueryRequest.setSign(AliPaySignature.sign(MapUtil.object2MapWithUnderline(aliPayOrderQueryRequest), aliPayConfig.getPrivateKey()));

        Call<AliPayOrderQueryResponse> call = null;
        if (aliPayConfig.isSandbox()) {
            call = devRetrofit.create(AliPayApi.class).orderQuery((MapUtil.object2MapWithUnderline(aliPayOrderQueryRequest)));
        } else {
            call = retrofit.create(AliPayApi.class).orderQuery((MapUtil.object2MapWithUnderline(aliPayOrderQueryRequest)));
        }

        Response<AliPayOrderQueryResponse> retrofitResponse = null;
        try {
            retrofitResponse = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert retrofitResponse != null;
        if (!retrofitResponse.isSuccessful()) {
            throw new RuntimeException("【查询支付宝订单】网络异常");
        }
        assert retrofitResponse.body() != null;
        AliPayOrderQueryResponse.AlipayTradeQueryResponse response = retrofitResponse.body().getAlipayTradeQueryResponse();
        if (!response.getCode().equals(AliPayConstants.RESPONSE_CODE_SUCCESS)) {
            throw new RuntimeException("【查询支付宝订单】code=" + response.getCode() + ", returnMsg=" + response.getMsg() + String.format("|%s|%s", response.getSubCode(), response.getSubMsg()));
        }

        return OrderQueryResponse.builder()
                .orderStatusEnum(AlipayTradeStatusEnum.findByName(response.getTradeStatus()).getOrderStatusEnum())
                .outTradeNo(response.getTradeNo())
                .orderId(response.getOutTradeNo())
                .resultMsg(response.getMsg())
                .finishTime(response.getSendPayDate())
                .build();
    }

    @Override
    public String downloadBill(DownloadBillRequest request) {
        return super.downloadBill(request);
    }

    private PayResponse buildPayResponse(AliPayAsyncResponse response) {
        PayResponse payResponse = new PayResponse();
        payResponse.setPayPlatformEnum(BestPayPlatformEnum.ALIPAY);
        payResponse.setOrderAmount(Double.valueOf(response.getTotalAmount()));
        payResponse.setOrderId(response.getOutTradeNo());
        payResponse.setOutTradeNo(response.getTradeNo());
        return payResponse;
    }

    @Override
    public CloseResponse close(CloseRequest request) {
        AliPayOrderCloseRequest aliPayOrderCloseRequest = new AliPayOrderCloseRequest();
        aliPayOrderCloseRequest.setAppId(aliPayConfig.getAppId());
        aliPayOrderCloseRequest.setTimestamp(LocalDateTime.now().format(formatter));
        AliPayOrderQueryRequest.BizContent bizContent = new AliPayOrderQueryRequest.BizContent();
        bizContent.setOutTradeNo(request.getOrderId());
        bizContent.setTradeNo(request.getOutOrderId());
        aliPayOrderCloseRequest.setBizContent(JsonUtil.toJsonWithUnderscores(bizContent).replaceAll("\\s*", ""));
        aliPayOrderCloseRequest.setSign(AliPaySignature.sign(MapUtil.object2MapWithUnderline(aliPayOrderCloseRequest), aliPayConfig.getPrivateKey()));

        Call<AliPayOrderCloseResponse> call = null;
        if (aliPayConfig.isSandbox()) {
            call = devRetrofit.create(AliPayApi.class).close((MapUtil.object2MapWithUnderline(aliPayOrderCloseRequest)));
        } else {
            call = retrofit.create(AliPayApi.class).close((MapUtil.object2MapWithUnderline(aliPayOrderCloseRequest)));
        }

        Response<AliPayOrderCloseResponse> retrofitResponse = null;
        try {
            retrofitResponse = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert retrofitResponse != null;
        if (!retrofitResponse.isSuccessful()) {
            throw new RuntimeException("【关闭支付宝订单】网络异常");
        }
        assert retrofitResponse.body() != null;
        AliPayOrderCloseResponse.AlipayTradeCloseResponse response = retrofitResponse.body().getAlipayTradeCloseResponse();
        if (!response.getCode().equals(AliPayConstants.RESPONSE_CODE_SUCCESS)) {
            throw new RuntimeException("【关闭支付宝订单】code=" + response.getCode() + ", returnMsg=" + response.getMsg() + String.format("|%s|%s", response.getSubCode(), response.getSubMsg()));
        }

        CloseResponse closeResponse = new CloseResponse();
        closeResponse.setOrderId(request.getOrderId() != null ? request.getOrderId() : "");
        closeResponse.setOutTradeNo(response.getTradeNo());
        return closeResponse;
    }

    @Override
    public PayBankResponse payBank(PayBankRequest request) {
        AliPayBankRequest aliPayBankRequest = new AliPayBankRequest();
        aliPayBankRequest.setAppId(aliPayConfig.getAppId());
        aliPayBankRequest.setTimestamp(LocalDateTime.now().format(formatter));
        AliPayBankRequest.BizContent bizContent = new AliPayBankRequest.BizContent();
        bizContent.setOutBizNo(request.getOrderId());
        bizContent.setProductCode("TRANS_BANKCARD_NO_PWD");
        bizContent.setOrderTitle(request.getDesc());
        bizContent.setRemark(request.getDesc());
        AliPayBankRequest.BizContent.Participant participant = new AliPayBankRequest.BizContent.Participant();
        participant.setIdentity(request.getBankNo());
        participant.setName(request.getTrueName());
        participant.setIdentityType("BANKCARD_ACCOUNT");
        AliPayBankRequest.BizContent.Participant.BankcardExtInfo bankcardExtInfo = new AliPayBankRequest.BizContent.Participant.BankcardExtInfo();
        //暂时先默认对私
        bankcardExtInfo.setAccountType(2);
        bankcardExtInfo.setBankCode(request.getBankCode());
        participant.setBankcardExtInfo(bankcardExtInfo);
        bizContent.setPayeeInfo(participant);
        aliPayBankRequest.setBizContent(JsonUtil.toJsonWithUnderscores(bizContent).replaceAll("\\s*", ""));
        aliPayBankRequest.setSign(AliPaySignature.sign(MapUtil.object2MapWithUnderline(aliPayBankRequest), aliPayConfig.getPrivateKey()));

        Call<AliPayBankResponse> call = retrofit.create(AliPayApi.class).payBank((MapUtil.object2MapWithUnderline(aliPayBankRequest)));

        Response<AliPayBankResponse> retrofitResponse = null;
        try {
            retrofitResponse = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert retrofitResponse != null;
        if (!retrofitResponse.isSuccessful()) {
            throw new RuntimeException("【支付宝转账到银行卡】网络异常");
        }
        assert retrofitResponse.body() != null;
        AliPayBankResponse.AlipayFundTransUniTransferResponse response = retrofitResponse.body().getAlipayFundTransUniTransferResponse();
        if (!response.getCode().equals(AliPayConstants.RESPONSE_CODE_SUCCESS)) {
            throw new RuntimeException("【支付宝转账到银行卡】code=" + response.getCode() + ", returnMsg=" + response.getMsg());
        }

        return PayBankResponse.builder()
                .orderId(response.getOutBizNo())
                .outTradeNo(response.getOrderId())
                .payFundOrderId(response.getPayRundOrderId())
                .status(response.getStatus())
                .build();
    }
}
