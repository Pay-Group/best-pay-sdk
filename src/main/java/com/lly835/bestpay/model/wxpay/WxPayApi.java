package com.lly835.bestpay.model.wxpay;

import com.lly835.bestpay.model.wxpay.response.WxPayRefundResponse;
import com.lly835.bestpay.model.wxpay.response.WxPaySyncResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by 廖师兄
 * 2017-07-02 13:36
 */
public interface WxPayApi {

    /**
     * 统一下单
     * @param body
     * @return
     */
    @POST("/pay/unifiedorder")
    Call<WxPaySyncResponse> unifiedorder(@Body RequestBody body);

    /**
     * 申请退款
     * @param body
     * @return
     */
    @POST("/secapi/pay/refund")
    Call<WxPayRefundResponse> refund(@Body RequestBody body);

    /**
     * 申请沙箱密钥
     * @param body
     * @return
     */
    @POST("/pay/getsignkey")
    Call<Object> getsignkey(@Body RequestBody body);
}
