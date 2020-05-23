package com.lly835.bestpay.model.wxpay.response;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by lly835@163.com
 * 2018-05-17 11:32
 */
@Data
@Root(name = "xml", strict = false)
public class WxPaySandboxKeyResponse {

    @Element(name = "return_code")
    private String returnCode;

    @Element(name = "return_msg", required = false)
    private String returnMsg;

    @Element(name = "mch_id", required = false)
    private String mchId;

    @Element(name = "sandbox_signkey", required = false)
    private String sandboxSignkey;
}
