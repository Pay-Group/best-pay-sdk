/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.lly835.bestpay.utils;


import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.util.*;

/**
 * Created by null on 2017/2/14.
 */
public class MapUtil {
	
	public static Map<String, String> getMap(String mapStr) {
		if(StringUtils.isEmpty(mapStr)) {
			return null;
		}
		Map<String, String> map = new HashMap<>();
		String[] key2ValArr = mapStr.replace("{", "").replace("}", "").split(", ");
		for (int i = 0; i < key2ValArr.length; i++) {
			String[] keyAndVal = key2ValArr[i].split("="); 
			map.put(keyAndVal[0], keyAndVal[1]);
		}
		return map;
	}
	
    /**
     * map转为url
     * 结果类似 token=abccdssx&sign=ccsacccss
     * @return
     */
    public static String toUrl(Map<String, String> map){

        String url = "";
        for(Map.Entry<String, String> entry : map.entrySet()){
            url += entry.getKey() + "=" + entry.getValue() + "&";
        }

        //移除最后一个&
        url = StringUtils.substringBeforeLast(url, "&");

        return url;
    }

    /**
     * map转url 排序后转
     * @param map
     * @return
     */
    public static String toUrlWithSort(Map<String, String> map){
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);

        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = map.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    /**
     * 去除不参与签名的参数
     * 支付宝中是去除sign和sign_type
     * @param map
     * @return
     */
    public static Map<String, String> removeParamsForAlipaySign(Map<String, String> map){
        map.remove("sign");
        map.remove("sign_type");

        return map;
    }

    /**
     * 移除map中空的key和value
     * @param map
     * @return
     */
    public static Map<String, String> removeEmptyKeyAndValue(Map<String, String> map) {

        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();

            if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
                it.remove();
            }
        }

        return map;
    }

    /**
     * 将map中的key转换成小写
     * @param map
     * @return
     */
    public static Map<String, String> keyToLowerCase(Map<String, String> map) {
        Map<String, String> responseMap = new HashMap<>();

        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();

            responseMap.put(key.toLowerCase(), value);
        }

        return responseMap;
    }

    /**
     * map转url 排序后转
     * @param map
     * @return
     */
    public static String toUrlWithSortAndEncode(Map<String, String> map){
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);

        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = map.get(key);
            if (value == null) {
                break;
            }
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + URLEncoder.encode(value);
            } else {
                prestr = prestr + key + "=" + URLEncoder.encode(value) + "&";
            }
        }
        return prestr;
    }
}
