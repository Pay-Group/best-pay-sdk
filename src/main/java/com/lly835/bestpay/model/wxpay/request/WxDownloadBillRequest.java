package com.lly835.bestpay.model.wxpay.request;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by steven ma
 * 2019-03-15 11:49
 */
@Data
@Root(name = "xml", strict = false)
public class WxDownloadBillRequest {

    //公众账号ID
    @Element(name = "appid")
    private String appid;

    //商户号
    @Element(name = "mch_id")
    private String mchId;

    //随机字符串
    @Element(name = "nonce_str")
    private String nonceStr;

    //签名
    @Element(name = "sign")
    private String sign;

    //对账单日期
    @Element(name = "bill_date")
    private String billDate;

    //账单类型
    @Element(name = "bill_type", required = false)
    private String billType = "ALL";

//    //压缩账单
//    @Element(name = "tar_type", required = false)
//    private String tarType = "GZIP";

}
