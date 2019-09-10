package com.lly835.bestpay.utils;

import javax.servlet.ServletRequest;
import java.util.Enumeration;
import java.util.Objects;
import java.util.TreeMap;

public class ServletRequestUtils {

    public static TreeMap<String, String> getParameterMap(ServletRequest request) {
        Objects.requireNonNull(request, "request is null.");
        TreeMap<String, String> map = new TreeMap<>();
        Enumeration enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = (String) enu.nextElement();
            map.put(paraName, request.getParameter(paraName));
        }

        return map;
    }

}
