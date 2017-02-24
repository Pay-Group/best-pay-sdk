/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package com.lly835.bestpay.utils;

import javax.servlet.ServletRequest;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@link ServletRequest}相关工具类.
 * 
 * @author <a href="mailto:zhuzm@59store.com">天河</a>
 * @version 1.0 2015年12月24日
 * @since 1.0
 */
public abstract class ServletRequestUtils {
    private static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    /**
     * 获取请求参数映射.
     * 
     * @param request {@link ServletRequest}
     * @return
     */
    public static Map<String, String> getParameterMap(ServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();

        Enumeration enu=request.getParameterNames();
        while(enu.hasMoreElements()){
            String paraName=(String)enu.nextElement();
            map.put(paraName, request.getParameter(paraName));
        }

        return map;
    }

}
