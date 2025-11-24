package net.teujaem.nrDonation.common.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class UrlEncoding {

    public static String toJson(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append("\"")
                    .append(entry.getKey().replace("\"", "\\\""))
                    .append("\":\"")
                    .append(entry.getValue().replace("\"", "\\\""))
                    .append("\"");

            if (i < map.size() - 1) sb.append(",");
            i++;
        }

        sb.append("}");
        return sb.toString();
    }



    public static String toXWwwFormUrl(Map<String, String> map) {
        StringBuilder body = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!body.isEmpty()) body.append("&");
            body.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            body.append("=");
            body.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }

        return body.toString();
    }
}
