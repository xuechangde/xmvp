package com.xcd.xmvp.http.interceptor;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Description: 请求返回拦截器
 * Author: djs
 * Date: 2019/6/11.
 */
public class ResponseInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);

        if (TextUtils.equals("GET", request.method())) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                MediaType contentType = responseBody.contentType();
                if ("json".equals(contentType.subtype())) { //只对json数据做处理
                    String json = responseBody.string();
                    if (!TextUtils.isEmpty(json)) {
                        LogUtils.e("解密前---:" + json);
                        //解密返回的json串
                        LogUtils.e("解密后---:" + json);
                    }
                    return response.newBuilder().body(ResponseBody.create(contentType, json)).build();
                }
            }
            return response;
        } else if (TextUtils.equals("POST", request.method())) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                MediaType contentType = responseBody.contentType();
                if ("json".equals(contentType.subtype())) { //只对json数据做处理
                    String json = responseBody.string();
                    if (!TextUtils.isEmpty(json)) {
                        LogUtils.e("解密前---:" + json);
                        //解密返回的json串
                        LogUtils.e("解密后---:" + json);
                    }
                    return response.newBuilder().body(ResponseBody.create(contentType, json)).build();
                }
            }
            return response;
        }
        return response;
    }
}
