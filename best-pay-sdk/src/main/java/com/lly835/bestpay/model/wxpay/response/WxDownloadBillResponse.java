package com.lly835.bestpay.model.wxpay.response;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 下载对账文件返回-只有发生错误的时候才会返回
 * Created by steven ma
 * 2019/3/20 16:48
 */
@Data
@Root(name = "xml", strict = false)
public class WxDownloadBillResponse {

    @Element(name = "return_code")
    private String returnCode;

    @Element(name = "return_msg", required = false)
    private String returnMsg;

    @Element(name = "error_code")
    private String errorCode;
}
