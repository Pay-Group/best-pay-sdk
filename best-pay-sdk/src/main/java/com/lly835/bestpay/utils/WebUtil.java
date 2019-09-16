package com.lly835.bestpay.utils;

import com.lly835.bestpay.constants.AliPayConstants;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by this on 2019/9/9 18:27
 */
public class WebUtil {

    public static String buildForm(String baseUrl,Map<String, String> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<form name=\"punchout_form\" method=\"post\" action=\"");
        sb.append(baseUrl);
        sb.append("\">\n");
        sb.append(buildHiddenFields(parameters));
        sb.append("<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >\n");
        sb.append("</form>\n");
        sb.append("<script>document.forms[0].submit();</script>");
        String form = sb.toString();
        return form;
    }

    private static String buildHiddenFields(Map<String, String> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            StringBuffer sb = new StringBuffer();
            Set<String> keys = parameters.keySet();
            Iterator var3 = keys.iterator();

            while(var3.hasNext()) {
                String key = (String)var3.next();
                String value = (String)parameters.get(key);
                if (key != null && value != null) {
                    sb.append(buildHiddenField(key, value));
                }
            }

            String result = sb.toString();
            return result;
        } else {
            return "";
        }
    }

    private static String buildHiddenField(String key, String value) {
        StringBuffer sb = new StringBuffer();
        sb.append("<input type=\"hidden\" name=\"");
        sb.append(key);
        sb.append("\" value=\"");
        String a = value.replace("\"", "&quot;");
        sb.append(a).append("\">\n");
        return sb.toString();
    }

    public static String buildQuery(Map<String, String> params, String charset) throws IOException {
        if (params != null && !params.isEmpty()) {
            StringBuilder query = new StringBuilder();
            Set<Map.Entry<String, String>> entries = params.entrySet();
            boolean hasParam = false;
            Iterator var5 = entries.iterator();

            while(var5.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)var5.next();
                String name = entry.getKey();
                String value = entry.getValue();
                if (StringUtil.areNotEmpty(new String[]{name, value})) {
                    if (hasParam) {
                        query.append("&");
                    } else {
                        hasParam = true;
                    }
                    query.append(name).append("=").append(URLEncoder.encode(value, charset));
                }
            }
            return query.toString();
        } else {
            return null;
        }
    }

    public static String getRequestUrl(Map<String,String> parameters,Boolean isSandbox)  {
        StringBuffer urlSb;
        if(isSandbox)
            urlSb = new StringBuffer(AliPayConstants.ALIPAY_GATEWAY_OPEN_DEV);
        else
            urlSb = new StringBuffer(AliPayConstants.ALIPAY_GATEWAY_OPEN);

        try {
            String charset = null != parameters.get("charset") ? parameters.get("charset") : "utf-8";
            String sysMustQuery = WebUtil.buildQuery(parameters, charset);
            urlSb.append("?");
            urlSb.append(sysMustQuery);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlSb.toString();
    }
}
