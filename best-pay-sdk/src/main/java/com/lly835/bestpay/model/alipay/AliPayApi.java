package com.lly835.bestpay.model.alipay;


import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AliPayApi {

    @POST
    Call<String> pc(@Body RequestBody body);
}
