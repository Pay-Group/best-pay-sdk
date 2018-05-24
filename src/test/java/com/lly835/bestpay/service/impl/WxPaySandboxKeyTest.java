package com.lly835.bestpay.service.impl;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by lly835@163.com
 * 2018-05-16 20:44
 */
public class WxPaySandboxKeyTest {


    @Test
    public void get() {
        WxPaySandboxKey wxPaySandboxKey = new WxPaySandboxKey();
        wxPaySandboxKey.get("1483469312");
    }
}