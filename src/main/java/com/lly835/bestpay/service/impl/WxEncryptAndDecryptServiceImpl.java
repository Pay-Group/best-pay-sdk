package com.lly835.bestpay.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Base64;

/**
 * Created by 廖师兄
 * 2018-05-30 16:26
 */
public class WxEncryptAndDecryptServiceImpl extends AbstractEncryptAndDecryptServiceImpl {
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS5Padding";

    /**
     * 加密
     *
     * @param key
     * @param data
     * @return
     */
    @Override
    public Object encrypt(String key, String data) {
        return super.encrypt(key, data);
    }

    /**
     * 解密
     * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_16#menu1
     *
     * @param key
     * @param data
     * @return
     */
    @Override
    public Object decrypt(String key, String data) {
        Security.addProvider(new BouncyCastleProvider());
        SecretKeySpec aesKey = new SecretKeySpec(DigestUtils.md5Hex(key).toLowerCase().getBytes(), ALGORITHM);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            return new String(cipher.doFinal(Base64.getDecoder().decode(data)));
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
