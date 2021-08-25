package com.lly835.bestpay.model;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.alipay.request.AliPayTradeCreateRequest;
import lombok.Data;
import org.simpleframework.xml.Element;

/**
 * 支付时请求参数
 */
@Data
public class PayBankRequest {

    /**
     * 支付方式.
     */
    private BestPayTypeEnum payTypeEnum;

    /**
     * 订单号.
     */
    private String orderId;

    /**
     * 订单金额.
     */
    private Double orderAmount;

    /**
     * 转账说明.
     */
    private String desc;

    /**
     * 收款方银行卡号
     */
    private String bankNo;

    /**
     * 收款方用户名
     */
    private String trueName;

    /**
     * 收款方开户行
     * https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=24_4&index=5
     */
    private String bankCode;
}
