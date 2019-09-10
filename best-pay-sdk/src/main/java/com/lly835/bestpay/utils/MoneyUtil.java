package com.lly835.bestpay.utils;

import java.math.BigDecimal;

/**
 * Created by 廖师兄
 * 2017-07-02 13:53
 */
public class MoneyUtil {

    /**
     * 元转分
     * @param yuan
     * @return
     */
    public static Integer Yuan2Fen(Double yuan) {
        return new BigDecimal(String.valueOf(yuan)).movePointRight(2).intValue();
    }

    /**
     * 分转元
     * @param fen
     * @return
     */
    public static Double Fen2Yuan(Integer fen) {
        return new BigDecimal(fen).movePointLeft(2).doubleValue();
    }
}
