//package com.lly835.bestpay.service.impl;
//
//import com.lly835.bestpay.config.AliDirectPayConfig;
//import com.lly835.bestpay.config.SignType;
//import com.lly835.bestpay.model.PayRequest;
//import com.lly835.bestpay.model.PayResponse;
//import com.lly835.bestpay.service.BestPayService;
//import com.lly835.bestpay.utils.JsonUtil;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.message.BasicNameValuePair;
//
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.Map;
//import java.util.Objects;
//import java.util.SortedMap;
//import java.util.TreeMap;
//import java.util.stream.Collectors;
//
///**
// * 支付宝PC端支付(即时到账)
// * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7386797.0.0.0NDYyr&treeId=62&articleId=103566&docType=1
// * Created by null on 2017/2/14.
// */
//class AlipayPCServiceImpl extends AbstractComponent implements BestPayService {
//
//    private AliDirectPayConfig aliDirectPayConfig;
//    private AlipayPCSignature signature;
//
//    public AlipayPCServiceImpl(AliDirectPayConfig aliDirectPayConfig, AlipayPCSignature signature) {
//        Objects.requireNonNull(aliDirectPayConfig, "aliDirectPayConfig is null.");
//        this.aliDirectPayConfig = aliDirectPayConfig;
//        Objects.requireNonNull(signature, "signature is null.");
//        this.signature = signature;
//    }
//
//    @Override
//    public PayResponse pay(PayRequest request) {
//        this.logger.info("【支付宝PC端支付】request={}", JsonUtil.toJson(request));
//
//        /* 1. 封装参数 */
//        SortedMap<String, String> commonParamMap = new TreeMap<>();
//        commonParamMap.put("service", "create_direct_pay_by_user");
//        commonParamMap.put("partner", this.aliDirectPayConfig.getPartnerId());
//        commonParamMap.put("_input_charset", this.aliDirectPayConfig.getInputCharset());
//        commonParamMap.put("sign_type", this.aliDirectPayConfig.getSignType().name());
//        commonParamMap.put("notify_url", this.aliDirectPayConfig.getNotifyUrl());
//        commonParamMap.put("return_url", this.aliDirectPayConfig.getReturnUrl());
//        commonParamMap.put("out_trade_no", request.getOrderId());
//        commonParamMap.put("subject", request.getOrderName());
//        commonParamMap.put("payment_type", "1");
//        commonParamMap.put("total_fee", String.valueOf(request.getOrderAmount()));
//        commonParamMap.put("seller_id", this.aliDirectPayConfig.getPartnerId());
////        commonParamMap.put("body", request.getAlipayBizParam().getBody());
////        commonParamMap.put("enable_paymethod", request.getAlipayBizParam().getEnablePayChannels());
////        commonParamMap.put("disable_paymethod", request.getAlipayBizParam().getDisablePayChannels());
////        commonParamMap.put("extra_common_param", request.getAlipayBizParam().getPassbackParams());
////        commonParamMap.put("it_b_pay", request.getAlipayBizParam().getTimeoutExpress());
////        commonParamMap.put("goods_type", request.getAlipayBizParam().getGoodsType());
////        commonParamMap.put("extend_param", request.getAlipayBizParam().getExtendParams());
//
//        /* 2. 签名 */
//        String sign = this.signature.sign(commonParamMap);
//        commonParamMap.put("sign", sign);
//
//        logger.debug("【支付宝PC端支付】构造好的完整参数={}", JsonUtil.toJson(commonParamMap));
//
//        /* 3. 构造支付url */
//        String payUrl;
//        try {
//            payUrl = new URIBuilder("https://mapi.alipay.com/gateway.do").setParameters(
//                    commonParamMap.entrySet().stream().filter(e -> {
//                        String k = e.getKey();
//                        String v = e.getValue();
//                        return !(StringUtils.isBlank(k) || StringUtils.isBlank(v));
//                    }).map(e -> {
//                        String k = e.getKey();
//                        String v = e.getValue();
//                        return new BasicNameValuePair(k, v);
//                    }).collect(Collectors.toList())).toString();
//        } catch (URISyntaxException e) {
//            throw new IllegalArgumentException("illegal pay url.", e);
//        }
//
//        /* 4. 返回签名结果 */
//        PayResponse response = new PayResponse();
//        response.setPayUri(URI.create(payUrl));
//        return response;
//    }
//
//    @Override
//    public boolean verify(Map<String, String> toBeVerifiedParamMap, SignType signType, String sign) {
//        return this.signature.verify(toBeVerifiedParamMap, signType, sign);
//    }
//
//}
