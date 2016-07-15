package org.tinywind.paypalexpresscheckout.util;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tinywind on 2016-07-15.
 */
public class UrlQueryEncoder {
    public static String encodeQueryParams(Map<String, Object> params) {
        return encodeQueryParams(convertMapToNameValuePair(params));
    }

    private static List<NameValuePair> convertMapToNameValuePair(Map<String, Object> params) {
        List<NameValuePair> li = new ArrayList<>();
        for (Map.Entry<String, Object> e : params.entrySet()) {
            Object value = e.getValue();
            if (value != null) {
                if (value instanceof List) {
                    for (Object o : (List) value) li.add(new BasicNameValuePair(e.getKey(), o.toString()));
                } else if (value instanceof Object[]) {
                    for (Object o : (Object[]) value) li.add(new BasicNameValuePair(e.getKey(), o.toString()));
                } else {
                    li.add(new BasicNameValuePair(e.getKey(), value.toString()));
                }
            }
        }
        return li;
    }

    private static String encodeQueryParams(List<NameValuePair> nameValuePairs) {
        return URLEncodedUtils.format(nameValuePairs, "UTF-8");
    }
}
