package com.lly835.bestpay.service;

import com.lly835.bestpay.config.SignType;
import com.lly835.bestpay.model.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 支付相关
 * Created by null on 2017/2/14.
 */
public interface BestPayService {

    /**
     * 发起支付.
     */
    PayResponse pay(PayRequest request);

    /**
     * 验证支付结果. 包括同步和异步.
     *
     * @param toBeVerifiedParamMap 待验证的支付结果参数.
     * @param signType             签名方式.
     * @param sign                 签名.
     * @return 验证结果.
     */
    boolean verify(Map<String, String> toBeVerifiedParamMap, SignType signType, String sign);

    /**
     * 同步回调
     * @param request
     * @return
     */
    PayResponse syncNotify(HttpServletRequest request);

    /**
     * 异步回调
     * @param notifyData
     * @return
     */
    PayResponse asyncNotify(String notifyData);

    /**
     * 退款
     * @param request
     * @return
     */
    RefundResponse refund(RefundRequest request);

    /**
     * 查询订单
     * @param request
     * @return
     */
    OrderQueryResponse query(OrderQueryRequest request);


    /**
     * 下载对账单
     * @param request
     * @return
     */
    String downloadBill(DownloadBillRequest request);


    /**
     * 根据规则生成二维码URL
     * @param productId 商品ID
     * @return 二维码中的内容为链接
     */
    String getQrCodeUrl(String productId);
}
