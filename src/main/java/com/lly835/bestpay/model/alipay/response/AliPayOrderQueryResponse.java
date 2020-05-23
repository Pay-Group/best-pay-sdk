package com.lly835.bestpay.model.alipay.response;

import lombok.Data;

/**
 * alipay.trade.query(统一收单线下交易查询) 响应
 * https://docs.open.alipay.com/api_1/alipay.trade.query
 * Created by 廖师兄
 */
@Data
public class AliPayOrderQueryResponse {

	private AlipayTradeQueryResponse alipayTradeQueryResponse;

	private String sign;

	@Data
	public static class AlipayTradeQueryResponse {

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

		/**
		 * 买家支付宝账号
		 */
		private String buyerLogonId;

		/**
		 * 交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
		 */
		private String tradeStatus;

		/**
		 * 交易的订单金额，单位为元，两位小数。该参数的值为支付时传入的total_amount
		 */
		private String totalAmount;

		/**
		 * 本次交易打款给卖家的时间
		 */
		private String sendPayDate;

		/**
		 * 买家在支付宝的用户id
		 */
		private String buyerUserId;

		/**
		 * 订单标题；
		 * 只在间连场景下返回；
		 */
		private String subject;

		/**
		 * 订单描述;
		 * 只在间连场景下返回；
		 */
		private String body;
	}
}
