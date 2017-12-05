/*     */
package com.payfort.sdk.android.dependancies.utils;
/*     */ 
/*     */

import com.google.gson.Gson;
import com.payfort.sdk.android.dependancies.models.SdkResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Utils
/*     */ {
    /*     */
    public static String getLanguage(Map<String, String> requestMap)
/*     */ {
/*  28 */
        if ((requestMap != null) &&
/*  29 */       (requestMap.containsKey("language"))) {
/*  30 */
            switch (requestMap.get("language")) {
/*     */
                case "ar":
/*     */
                case "en":
/*  33 */
                    return requestMap.get("language");
/*     */
            }
/*     */
        }
/*  36 */
        return "en";
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */
    public static String getPaymentOptionValue(Map<String, String> requestMap)
/*     */ {
/*  44 */
        if ((requestMap != null) &&
/*  45 */       (requestMap.containsKey("payment_option"))) {
/*  46 */
            switch (requestMap.get("payment_option")) {
/*     */
                case "VISA":
/*     */
                case "MASTERCARD":
/*     */
                case "AMEX":
/*  50 */
                    return requestMap.get("payment_option");
/*     */
            }
/*     */
        }
/*  53 */
        return null;
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */
    public static String getParamValue(Map<String, String> requestMap, String key)
/*     */ {
/*  62 */
        if ((requestMap != null) &&
/*  63 */       (requestMap.containsKey(key)) && 
/*  64 */       (requestMap.get(key) != null)) {
/*  65 */
            return requestMap.get(key);
/*     */
        }
/*     */     
/*  68 */
        return null;
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */
    public static SdkResponse collectResponse(String responseMsg, String jsonResponse, Map<String, String> merchantRequestMap)
/*     */ {
/*  78 */
        SdkResponse sdkResponse = null;
/*  79 */
        if (jsonResponse == null)
/*     */ {
/*     */ 
/*  82 */
            sdkResponse = new SdkResponse();
/*  83 */
            sdkResponse.setSuccess(false);
/*  84 */
            Map<String, String> responseMap = new HashMap(merchantRequestMap);
/*  85 */
            responseMap.put("status", "00");
/*  86 */
            responseMap.put("response_code", "00071");
/*  87 */
            responseMap.put("response_message", responseMsg);
/*  88 */
            sdkResponse.setResponseMap(responseMap);
/*     */
        } else {
/*     */
            try {
/*  91 */
                if (jsonResponse.isEmpty())
/*  92 */ throw new Exception();
/*  93 */
                Gson gson = new Gson();
/*  94 */
                sdkResponse = gson.fromJson(jsonResponse, SdkResponse.class);
/*     */
            }
/*     */ catch (Exception ex)
/*     */ {
/*  98 */
                sdkResponse = new SdkResponse();
/*  99 */
                sdkResponse.setSuccess(false);
/* 100 */
                Map<String, String> responseMap = new HashMap(merchantRequestMap);
/* 101 */
                responseMap.put("status", "00");
/* 102 */
                responseMap.put("response_code", "00006");
/* 103 */
                responseMap.put("response_message", responseMsg);
/* 104 */
                sdkResponse.setResponseMap(responseMap);
/*     */
            }
/*     */
        }
/* 107 */
        return sdkResponse;
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */
    private static Map<String, String> splitURI(String urlString)
/*     */ {
/*     */
        try
/*     */ {
/* 116 */
            URL url = new URL(urlString);
/* 117 */
            Map<String, String> query_pairs = new LinkedHashMap();
/* 118 */
            String query = url.getQuery();
/* 119 */
            String[] pairs = query.split("&");
/* 120 */
            for (String pair : pairs) {
/* 121 */
                int idx = pair.indexOf("=");
/* 122 */
                query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
/*     */
            }
/* 124 */
            return query_pairs;
/*     */
        } catch (Exception e) {
        }
/* 126 */
        return null;
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */
    public static SdkResponse collectResponseFromURL(String url)
/*     */ {
/* 135 */
        SdkResponse sdkResponse = new SdkResponse();
/* 136 */
        sdkResponse.setResponseMap(splitURI(url));
/* 137 */
        if ((sdkResponse.getResponseMap() != null) &&
/* 138 */       (!sdkResponse.getResponseMap().isEmpty()) && 
/* 139 */       (sdkResponse.getResponseMap().containsKey("response_code")) && 
/* 140 */       (sdkResponse.getResponseMap().get("response_code") != null) && 
/* 141 */       (sdkResponse.getResponseMap().get("response_code").length() >= 5) &&
/* 142 */       (sdkResponse.getResponseMap().get("response_code").substring(2).equals("000")))
/* 143 */ sdkResponse.setSuccess(true);
/* 144 */
        return sdkResponse;
/*     */
    }

    /*     */
/*     */ 
/*     */ 
/*     */ 
/*     */
    public static String readHttpResponse(HttpsURLConnection urlConnection)
/*     */ {
/*     */
        try
/*     */ {
/* 154 */
            StringBuilder sb = new StringBuilder();
/* 155 */
            int HttpResult = urlConnection.getResponseCode();
/* 156 */
            if (HttpResult == 200)
/*     */ {
/* 158 */
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
/* 159 */
                String line = null;
/* 160 */
                while ((line = br.readLine()) != null) {
/* 161 */
                    sb.append(line + "\n");
/*     */
                }
/* 163 */
                br.close();
/*     */
            }
/* 165 */
            return "" + sb.toString();
/*     */
        } catch (Exception e) {
        }
/* 167 */
        return null;
/*     */
    }
/*     */
}