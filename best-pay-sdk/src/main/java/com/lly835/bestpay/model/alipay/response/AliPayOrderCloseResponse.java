package com.lly835.bestpay.model.alipay.response;

import lombok.Data;

/**
 * alipay.trade.query(统一收单线下交易查询) 响应
 * https://docs.open.alipay.com/api_1/alipay.trade.query
 * Created by 廖师兄
 */
@Data
public class AliPayOrderCloseResponse {

	private AlipayTradeCloseResponse alipayTradeCloseResponse;

	private String sign;

	@Data
	public static class AlipayTradeCloseResponse {

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
