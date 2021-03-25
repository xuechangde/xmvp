package com.xcd.xmvp.app;

import android.app.Application;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.xcd.xmvp.XBaseRetrofitConfig;
import com.xcd.xmvp.common.GlobalConstant;

import org.litepal.LitePal;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

public abstract class XMvp extends Application {
    protected String mBaseUrl;

    public abstract String initBaseUrl();

    @Override
    public void onCreate() {
        super.onCreate();
        mBaseUrl = initBaseUrl();
        init();
    }

    /**
     * 初始化 application
     */
    public void init() {
        //初始化工具类
        Utils.init(this);
        //设置打印开关
        LogUtils.getConfig().setLogSwitch(true);
        //设置控制台输出
        LogUtils.getConfig().setConsoleSwitch(true);
        //初始化xPopup弹窗
        x.Ext.init(this);
        //是否输出debug日志, 开启debug会影响性能.
        x.Ext.setDebug(false);
        XBaseRetrofitConfig.setBaseUrl(mBaseUrl);
        //本地数据库
//        LitePal.initialize(this);
        //初始化bugly 发布的时候改为false，测试的时候改为true
//        CrashReport.initCrashReport(getApplicationContext(), "", true);
        //初始化极光推送
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);
        //初始化腾讯X5浏览器内核
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
            }

            @Override
            public void onViewInitFinished(boolean b) {
            }
        });
        registerNetworkStatusChangedListener();
    }

    /**
     * 注册网络状态监听服务
     */
    private void registerNetworkStatusChangedListener() {
        NetworkUtils.OnNetworkStatusChangedListener onNetworkStatusChangedListener = new NetworkUtils.OnNetworkStatusChangedListener() {
            @Override
            public void onDisconnected() {
                if (SPUtils.getInstance().getBoolean(GlobalConstant.NETWORK_STATUS)) {
                    SPUtils.getInstance().put(GlobalConstant.NETWORK_STATUS, false);
                    LogUtils.i("网络状态", "当前网络不可用");
                    new XPopup.Builder(ActivityUtils.getTopActivity())
                            .asConfirm("网络状态", "当前网络不可用", new OnConfirmListener() {
                                @Override
                                public void onConfirm() {

                                }
                            })
                            .show();
                }
            }

            @Override
            public void onConnected(NetworkUtils.NetworkType networkType) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.i("网络状态", "网络已连接");
                        if (!SPUtils.getInstance().getBoolean(GlobalConstant.NETWORK_STATUS)) {
                            SPUtils.getInstance().put(GlobalConstant.NETWORK_STATUS, true);
                            new XPopup.Builder(ActivityUtils.getTopActivity())
                                    .asConfirm("网络状态", "网络已连接", new OnConfirmListener() {
                                        @Override
                                        public void onConfirm() {

                                        }
                                    })
                                    .show();
                        }
                    }
                }).start();
            }
        };
        NetworkUtils.registerNetworkStatusChangedListener(onNetworkStatusChangedListener);
    }
}
