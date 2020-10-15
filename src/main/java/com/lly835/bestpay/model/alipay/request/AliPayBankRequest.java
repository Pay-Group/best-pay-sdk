package com.lly835.bestpay.model.alipay.request;

import com.lly835.bestpay.constants.AliPayConstants;
import lombok.Data;

/**
 * @author zicheng
 * @date 2020/10/14
 * Description:
 * https://opendocs.alipay.com/apis/api_28/alipay.fund.trans.uni.transfer
 */
@Data
public class AliPayBankRequest {
    /**
     * app_id
     */
    private String appId;
    /**
     * 接口名称
     */
    private String method = "alipay.fund.trans.uni.transfer";

    /**
     * 请求使用的编码格式，如utf-8,gbk,gb2312等
     */
    private String charset = "utf-8";
    /**
     * 生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
     */
    private String signType = AliPayConstants.SIGN_TYPE_RSA2;
    /**
     * 商户请求参数的签名串，详见签名 https://docs.open.alipay.com/291/105974
     */
    private String sign;
    /**
     * 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
     */
    private String timestamp;
    /**
     * 调用的接口版本，固定为：1.0
     */
    private String version = "1.0";

    /**
     * 详见应用授权概述
     * https://docs.open.alipay.com/20160728150111277227/intro
     */
    private String appAuthToken;

    /**
     * 请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
     */
    private String bizContent;

    @Data
    public static class BizContent {
        /**
         * 商户端的唯一订单号，对于同一笔转账请求，商户需保证该订单号唯一。
         */
        private String outBizNo;

        /**
         * 订单总金额，单位为元，精确到小数点后两位，
         * STD_RED_PACKET产品取值范围[0.01,100000000]；
         * TRANS_ACCOUNT_NO_PWD产品取值范围[0.1,100000000]
         */
        private Double transAmount;

        /**
         * 业务产品码，
         * 单笔无密转账到支付宝账户固定为:
         * TRANS_ACCOUNT_NO_PWD；
         * 单笔无密转账到银行卡固定为:
         * TRANS_BANKCARD_NO_PWD;
         * 收发现金红包固定为:
         * STD_RED_PACKET；
         */
        private String productCode;

        /**
         * 转账业务的标题，用于在支付宝用户的账单里显示
         */
        private String orderTitle;

        /**
         * 原支付宝业务单号。C2C现金红包-红包领取时，传红包支付时返回的支付宝单号；B2C现金红包、单笔无密转账到支付宝/银行卡不需要该参数。
         */
        private String originalOrderId;

        /**
         * 收款方信息
         */
        private Participant payeeInfo;

        /**
         * 备注
         */
        private String remark;

        @Data
        public static class Participant {
            /**
             * 参与方的唯一标识
             */
            private String identity;

            /**
             * 参与方的标识类型，目前支持如下类型：
             * 1、ALIPAY_USER_ID 支付宝的会员ID
             * 2、ALIPAY_LOGON_ID：支付宝登录号，支持邮箱和手机号格式
             * 2、BANKCARD_ACCOUNT 银行卡账户
             */
            private String identityType;

            /**
             * 收款方银行账户名称
             */
            private String name;

            /**
             * 收款方银行卡扩展信息
             */
            private BankcardExtInfo bankcardExtInfo;

            /**
             * https://opendocs.alipay.com/open/common/transfertocard#%E5%8D%95%E7%AC%94%E8%BD%AC%E8%B4%A6%E5%88%B0%E9%93%B6%E8%A1%8C%E5%8D%A1%E6%8E%A5%E5%8F%A3%20alipay.fund.trans.uni.transfer
             */
            @Data
            public static class BankcardExtInfo {
                /**
                 * 银行名
                 * 当收款账户为对公账户时，机构名称必填；当收款账户为对私账户时，机构名称可为空。
                 */
                private String instName;

                /**
                 * 收款账户类型。
                 * <p>
                 * 1：对公（在金融机构开设的公司账户）,如果银行卡为对公，必须传递省市支行信息或者联行号
                 * <p>
                 * 2：对私（在金融机构开设的个人账户）
                 */
                private Integer accountType;

                /**
                 * 银行所在省份
                 */
                private String instProvince;

                /**
                 * 收款银行所在市
                 */
                private String instCity;

                /**
                 * 收款银行所属支行
                 */
                private String instBranchName;

                /**
                 * 银行支行联行号
                 */
                private String bankCode;
            }
        }
    }
}
