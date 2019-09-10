package com.lly835.bestpay.service.impl;

import com.lly835.bestpay.service.EncryptAndDecryptService;

/**
 * Created by 廖师兄
 * 2018-05-30 16:21
 */
abstract class AbstractEncryptAndDecryptServiceImpl implements EncryptAndDecryptService {

    /**
     * 加密
     *
     * @param key
     * @param data
     * @return
     */
    @Override
    public Object encrypt(String key, String data) {
        return null;
    }

    /**
     * 解密
     *
     * @param key
     * @param data
     * @return
     */
    @Override
    public Object decrypt(String key, String data) {
        return null;
    }
}
