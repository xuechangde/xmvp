package com.xcd.xmvp.http.interceptor;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.io.IOException;
import java.net.UnknownHostException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Description: 网络检查拦截器
 * Author: djs
 * Date: 2019/6/12.
 */
public class NetCheckInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!NetworkUtils.isConnected()){
            throw new UnknownHostException("no network is connected");
        }
        return chain.proceed(chain.request().newBuilder().build());
    }
}
