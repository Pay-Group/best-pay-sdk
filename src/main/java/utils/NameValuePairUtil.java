/**
 * Copyright (c) 2015, 59store. All rights reserved.
 */
package utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link NameValuePair}工具类.
 *
 * @version 1.0 2015年12月24日
 * @since 1.0
 */
public abstract class NameValuePairUtil {

    /**
     * 将Map转换为List<{@link NameValuePair}>.
     * 
     * @param map
     * @return
     */
    public static List<NameValuePair> convert(Map<String, String> map) {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        map.forEach((key, value) -> {
            nameValuePairs.add(new BasicNameValuePair(key, value));
        });

        return nameValuePairs;
    }

}
