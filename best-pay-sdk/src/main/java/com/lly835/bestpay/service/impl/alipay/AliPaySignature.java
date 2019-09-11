package com.lly835.bestpay.service.impl.alipay;

import com.lly835.bestpay.constants.AliPayConstants;
import com.lly835.bestpay.utils.StreamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created By this
 * 普通公钥方式实现
 */
@Slf4j
public class AliPaySignature {

    /**
     *
     * @param params   参数map
     * @param privateKey        商户私钥
     * @return
     */
    public static String sign(Map<String, String> params, String privateKey) {
        String signType = params.get("sign_type");
        String signContent = getSignContent(params);
        log.info("sign content: {}",signContent);
        if(AliPayConstants.SIGN_TYPE_RSA.equals(signType)) {
            return rsaSign(signContent,privateKey);
        } else if (AliPayConstants.SIGN_TYPE_RSA2.equals(signType)) {
            return rsa256Sign(signContent, privateKey);
        } else {
            return null;
        }
    }

    public static String getSignContent(Map<String, String> params) {
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
        }
        return content.toString();
    }

    /**
     * sha1WithRsa 加签
     *
     * @param content
     * @param privateKey
     * @return
     * @throws RuntimeException
     */
    public static String rsaSign(String content, String privateKey) {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(AliPayConstants.SIGN_TYPE_RSA,
                    new ByteArrayInputStream(privateKey.getBytes()));

            Signature signature = java.security.Signature
                    .getInstance(AliPayConstants.SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(AliPayConstants.CHARSET_UTF8));
            byte[] signed = signature.sign();

            return new String(Base64.encodeBase64(signed));
        } catch (InvalidKeySpecException ie) {
            throw new RuntimeException("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥", ie);
        } catch (Exception e) {
            throw new RuntimeException("RSAcontent = " + content + "; charset = utf-8" , e);
        }
    }

    /**
     * sha256WithRsa 加签
     *
     * @param content
     * @param privateKey
     * @return
     * @throws RuntimeException
     */
    public static String rsa256Sign(String content, String privateKey)  {

        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(AliPayConstants.SIGN_TYPE_RSA,
                    new ByteArrayInputStream(privateKey.getBytes()));

            Signature signature = java.security.Signature
                    .getInstance(AliPayConstants.SIGN_SHA256RSA_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(AliPayConstants.CHARSET_UTF8));
            byte[] signed = signature.sign();

            return new String(Base64.encodeBase64(signed));
        } catch (Exception e) {
            throw new RuntimeException("RSAcontent = " + content + "; charset = utf-8", e);
        }

    }

    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {

        if (ins == null || StringUtils.isEmpty(algorithm)) {
            return null;
        }
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        byte[] encodedKey = StreamUtil.readText(ins).getBytes();
        encodedKey = Base64.decodeBase64(encodedKey);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }


    /**
     * 校验签名
     * @param params
     * @param signKey
     * @return
     */
    public static Boolean verify(Map<String, String> params, String signKey) {
        String sign = sign(params, signKey);
        return sign.equals(params.get("sign"));
    }
}
