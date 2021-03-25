package com.xcd.xmvp.http;

import com.blankj.utilcode.util.Utils;
import com.xcd.xmvp.XBaseRetrofitConfig;
import com.xcd.xmvp.http.cookie.CookiesManager;
import com.xcd.xmvp.http.gson.BaseConverterFactory;
import com.xcd.xmvp.http.interceptor.NetCheckInterceptor;
import com.xcd.xmvp.http.interceptor.RequestInterceptor;
import com.xcd.xmvp.http.interceptor.ResponseInterceptor;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author xcd
 */
public class RetrofitService {

    private volatile static RetrofitService apiRetrofit;
    private Retrofit mRetrofit;


    /**
     * 单例调用
     *
     * @return RetrofitService
     */
    public static RetrofitService getInstance() {
        if (apiRetrofit == null) {
            synchronized (Object.class) {
                if (apiRetrofit == null) {
                    apiRetrofit = new RetrofitService();
                }
            }
        }
        return apiRetrofit;
    }


    /**
     * 初始化retrofit
     */
    private RetrofitService() {
        //配置okHttp并设置时间、日志信息和cookies
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                //网络监察拦截器
                .addInterceptor(new NetCheckInterceptor())
                //请求参数拦截器
                .addInterceptor(new RequestInterceptor())
                //请求返回参数拦截器
                .addInterceptor(new ResponseInterceptor())
                //设置超时时间
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(55, TimeUnit.SECONDS)
                //https请求证书设置
                .sslSocketFactory(HTTPSTools.getSSLSocketFactoryValue(null),
                        (X509TrustManager) HTTPSTools.getX509TrustManager(null)[0])
                //请求失败，是否可以重试
                .retryOnConnectionFailure(false)
                //设置Cookie持久化
                .cookieJar(new CookiesManager(Utils.getApp().getApplicationContext()))
                .build();

        //关联okHttp并加上rxJava和Gson的配置和baseUrl
        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(BaseConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(XBaseRetrofitConfig.getBaseUrl())
                .build();
    }

    /**
     * 利用泛型传入接口class返回接口实例
     *
     * @param ser 类
     * @param <T> 类的类型
     * @return Observable
     */
    public <T> T createRs(Class<T> ser) {
        return mRetrofit.create(ser);
    }

}
