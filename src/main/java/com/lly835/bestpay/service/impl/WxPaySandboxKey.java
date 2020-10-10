package com.lly835.bestpay.service.impl;

import com.lly835.bestpay.constants.WxPayConstants;
import com.lly835.bestpay.model.wxpay.WxPayApi;
import com.lly835.bestpay.model.wxpay.response.WxPaySandboxKeyResponse;
import com.lly835.bestpay.utils.JsonUtil;
import com.lly835.bestpay.utils.RandomUtil;
import com.lly835.bestpay.utils.XmlUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lly835@163.com
 * 2018-05-16 20:35
 */
@Slf4j
public class WxPaySandboxKey {

    public void get(String mchId, String mchKey) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WxPayConstants.WXPAY_GATEWAY)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        SandboxParam sandboxParam = new SandboxParam();
        sandboxParam.setMchId(mchId);
        sandboxParam.setNonceStr(RandomUtil.getRandomStr());
        sandboxParam.setSign(WxPaySignature.sign(sandboxParam.buildMap(), mchKey));

        String xml = XmlUtil.toString(sandboxParam);
        RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), xml);
        Call<WxPaySandboxKeyResponse> call = retrofit.create(WxPayApi.class).getsignkey(body);
        Response<WxPaySandboxKeyResponse> retrofitResponse = null;
        try {
            retrofitResponse = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!retrofitResponse.isSuccessful()) {
            throw new RuntimeException("【微信统一支付】发起支付，网络异常，" + retrofitResponse);
        }
        Object response = retrofitResponse.body();
        log.info("【获取微信沙箱密钥】response={}", JsonUtil.toJson(response));
    }


    @Data
    @Root(name = "xml")
    static class SandboxParam {

        @Element(name = "mch_id")
        private String mchId;

        @Element(name = "nonce_str")
        private String nonceStr;

        @Element(name = "sign")
        private String sign;

        public Map<String, String> buildMap() {
            Map<String, String> map = new HashMap<>();
            map.put("mch_id", this.mchId);
            map.put("nonce_str", this.nonceStr);
            return map;
        }
    }
}
