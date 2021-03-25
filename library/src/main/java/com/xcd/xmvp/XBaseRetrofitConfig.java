package com.xcd.xmvp;


/**
 * @author xcd
 */
public class XBaseRetrofitConfig {
    private static String baseUrl;

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static void setBaseUrl(String baseApi) {
        XBaseRetrofitConfig.baseUrl = baseApi;
    }
}
