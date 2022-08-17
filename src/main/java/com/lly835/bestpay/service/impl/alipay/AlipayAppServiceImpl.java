package com.lly835.bestpay.service.impl.alipay;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.lly835.bestpay.constants.AliPayConstants;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.alipay.AliPayApi;
import com.lly835.bestpay.model.alipay.request.AliPayTradeCreateRequest;
import com.lly835.bestpay.model.alipay.response.AliPayOrderCreateResponse;
import com.lly835.bestpay.utils.JsonUtil;
import com.lly835.bestpay.utils.MapUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 支付APP支付
 * Created by 廖师兄
 * https://opendocs.alipay.com/open/01dcc0
 */
@Slf4j
public class AlipayAppServiceImpl extends AliPayServiceImpl {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(AliPayConstants.ALIPAY_GATEWAY_OPEN)
            .addConverterFactory(GsonConverterFactory.create(
                    //下划线驼峰互转
                    new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
            ))
            .client(new OkHttpClient.Builder()
                    .addInterceptor((new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY)))
                    .followRedirects(false)  //禁制OkHttp的重定向操作，我们自己处理重定向
                    .followSslRedirects(false)
                    .build()
            )
            .build();

    private final Retrofit devRetrofit = new Retrofit.Builder()
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
        AliPayTradeCreateRequest aliPayOrderQueryRequest = new AliPayTradeCreateRequest();
        aliPayOrderQueryRequest.setMethod(AliPayConstants.ALIPAY_TRADE_APP_PAY);
        aliPayOrderQueryRequest.setAppId(aliPayConfig.getAppId());
        aliPayOrderQueryRequest.setTimestamp(LocalDateTime.now().format(formatter));
        aliPayOrderQueryRequest.setNotifyUrl(aliPayConfig.getNotifyUrl());
        AliPayTradeCreateRequest.BizContent bizContent = new AliPayTradeCreateRequest.BizContent();
        bizContent.setOutTradeNo(request.getOrderId());
        bizContent.setTotalAmount(request.getOrderAmount());
        bizContent.setSubject(request.getOrderName());
        bizContent.setPassbackParams(request.getAttach());

        aliPayOrderQueryRequest.setBizContent(JsonUtil.toJsonWithUnderscores(bizContent).replaceAll("\\s*", ""));
        String sign = AliPaySignature.sign(MapUtil.object2MapWithUnderline(aliPayOrderQueryRequest), aliPayConfig.getPrivateKey());
        aliPayOrderQueryRequest.setSign(URLEncoder.encode(sign));

        Map<String, String> stringStringMap = MapUtil.object2MapWithUnderline(aliPayOrderQueryRequest);
        String body = MapUtil.toUrl(stringStringMap);
        PayResponse payResponse = new PayResponse();
        payResponse.setBody(body);
        return payResponse;
    }
}
