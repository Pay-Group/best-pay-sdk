package com.lly835.bestpay.model.alipay.response;

import lombok.Data;

/**
 * alipay.trade.create(统一收单交易创建接口)
 * https://docs.open.alipay.com/api_1/alipay.trade.create
 * Created by 廖师兄
 */
@Data
public class AliPayOrderCreateResponse {

	private AlipayTradeCreateResponse alipayTradeCreateResponse;

	private String sign;

	@Data
	public static class AlipayTradeCreateResponse {

		private String code;

		private String msg;

		private String subCode;

		private String subMsg;

		/**
		 * 支付宝交易号
		 */
		private String tradeNo;

		/**
		 * 商家订单号
		 */
		private String outTradeNo;
	}
}
