package com.lly835.bestpay.service.impl.wx;

import com.lly835.bestpay.constants.WxPayConstants;
import com.lly835.bestpay.model.OrderQueryRequest;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.wxpay.WxPayApi;
import com.lly835.bestpay.model.wxpay.request.WxPayUnifiedorderRequest;
import com.lly835.bestpay.model.wxpay.response.WxPaySyncResponse;
import com.lly835.bestpay.service.impl.WxPayServiceImpl;
import com.lly835.bestpay.service.impl.WxPaySignature;
import com.lly835.bestpay.utils.MapUtil;
import com.lly835.bestpay.utils.MoneyUtil;
import com.lly835.bestpay.utils.RandomUtil;
import com.lly835.bestpay.utils.XmlUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

/**
 * @author zicheng
 * @date 2020/10/14
 * Description:
 */
public class WxPayMicroServiceImpl extends WxPayServiceImpl {

    /**
     * 微信付款码支付
     * 提交支付请求后微信会同步返回支付结果。
     * 当返回结果为“系统错误 {@link WxPayConstants#SYSTEMERROR}”时，商户系统等待5秒后调用【查询订单API {@link WxPayServiceImpl#query(OrderQueryRequest)}】，查询支付实际交易结果；
     * 当返回结果为“正在支付 {@link WxPayConstants#USERPAYING}”时，商户系统可设置间隔时间(建议10秒)重新查询支付结果，直到支付成功或超时(建议30秒)；
     *
     * @param request
     * @return
     */
    @Override
    public PayResponse pay(PayRequest request) {
        WxPayUnifiedorderRequest wxRequest = new WxPayUnifiedorderRequest();
        wxRequest.setOutTradeNo(request.getOrderId());
        wxRequest.setTotalFee(MoneyUtil.Yuan2Fen(request.getOrderAmount()));
        wxRequest.setBody(request.getOrderName());
        wxRequest.setOpenid(request.getOpenid());
        wxRequest.setAuthCode(request.getAuthCode());

        wxRequest.setAppid(wxPayConfig.getAppId());
        wxRequest.setMchId(wxPayConfig.getMchId());
        wxRequest.setNonceStr(RandomUtil.getRandomStr());
        wxRequest.setSpbillCreateIp(StringUtils.isEmpty(request.getSpbillCreateIp()) ? "8.8.8.8" : request.getSpbillCreateIp());
        wxRequest.setAttach(request.getAttach());
        wxRequest.setSign(WxPaySignature.sign(MapUtil.buildMap(wxRequest), wxPayConfig.getMchKey()));

        //对付款码支付无用的字段
        wxRequest.setNotifyUrl("");
        wxRequest.setTradeType("");

        RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), XmlUtil.toString(wxRequest));

        WxPayApi api = null;
        if (wxPayConfig.isSandbox()) {
            api = devRetrofit.create(WxPayApi.class);
        } else {
            api = retrofit.create(WxPayApi.class);
        }

        Call<WxPaySyncResponse> call = api.micropay(body);

        Response<WxPaySyncResponse> retrofitResponse = null;
        try {
            retrofitResponse = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert retrofitResponse != null;
        if (!retrofitResponse.isSuccessful()) {
            throw new RuntimeException("【微信付款码支付】发起支付, 网络异常");
        }
        return buildPayResponse(retrofitResponse.body());
    }
}
