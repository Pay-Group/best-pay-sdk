/**
 * Copyright (c) 2016, 59store. All rights reserved.
 */
package com.lly835.bestpay.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.simpleframework.xml.Element;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by null on 2017/2/14.
 */
public class MapUtil {

    final static ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 对象转map
     * @param obj
     * @return
     */
    public static Map<String, String> buildMap(Object obj) {
        Map<String, String> map = new HashMap<>();

        try {
            Class<?> clazz = obj.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();

                //如果 element 注解 name 字段设置了内容, 使用其当成字段名
                Element element = field.getAnnotation(Element.class);
                if (element != null && StringUtils.isNotEmpty(element.name())) {
                    fieldName = element.name();
                }

                String value = field.get(obj) == null ? "" : String.valueOf(field.get(obj));
                map.put(fieldName, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
	
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

    /**
     * 表单字符串转化成 hashMap
     * @param orderinfo
     * @return
     */
    public static HashMap<String, String> form2Map( String orderinfo) {
        String[] listinfo;
        HashMap<String, String> map = new HashMap<>();
        listinfo = orderinfo.split("&");
        for(String s : listinfo)
        {
            String[] list = s.split("=", 2);
            if(list.length > 1)
            {
                map.put(list[0], list[1]);
            }
        }
        return map;
    }

    /**
     * 表单字符串转化成 hashMap，将具有下划线的key转换为小驼峰
     * @param orderinfo,
     * @return
     */
    public static HashMap<String, String> form2MapWithCamelCase( String orderinfo) {
        String listinfo[];
        HashMap<String, String> map = new HashMap<String, String>();
        listinfo = orderinfo.split("&");
        for(String s : listinfo)
        {
            String list[]  = s.split("=");
            if(list.length>1)
            {
                map.put(CamelCaseUtil.toCamelCase(list[0]),list[1]);
            }
        }
        return map;
    }

    /**
     * 对象转map,将字段转换为下划线形式
     * @param obj
     * @return
     */
    public static Map<String, String> object2MapWithUnderline(Object obj) {
        Map<String, String> map = new HashMap<>();
        try {
            Class<?> clazz = obj.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                fieldName = CamelCaseUtil.toUnderlineName(fieldName);
                String value = field.get(obj) == null ? "" : String.valueOf(field.get(obj));
                map.put(fieldName, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static <T> T mapToObject(Object obj,Class<T> clazz) {
        try {
            return objectMapper.readValue(serialize(obj),clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String serialize(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj.getClass() == String.class) {
            return (String) obj;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
