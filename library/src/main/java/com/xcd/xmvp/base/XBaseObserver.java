package com.xcd.xmvp.base;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * @author xcd
 */
public abstract class XBaseObserver<T> extends DisposableObserver<T> {

    protected XBaseView view;
    private boolean isShowDialog;

    protected XBaseObserver(XBaseView view) {
        this.view = view;
    }

    /**
     * 带进度条的初始化方法
     *
     * @param view         view
     * @param isShowDialog 是否显示进度条
     */
    protected XBaseObserver(XBaseView view, boolean isShowDialog) {
        this.view = view;
        this.isShowDialog = isShowDialog;
    }

    @Override
    protected void onStart() {
        if (view != null && isShowDialog) {
            view.showLoading();
        }
    }

    @Override
    public void onNext(T o) {
        onSuccess(o);
    }

    @Override
    public void onError(Throwable e) {
        if (view != null && isShowDialog) {
            view.hideLoading();
        }
        XBaseException be;
        if (e != null) {
            //自定义异常
            if (e instanceof XBaseException) {
                be = (XBaseException) e;
                //回调到view层 处理 或者根据项目情况处理
                if (view != null) {
                    // 处理登录失效 更新
                    view.onErrorCode(new XBaseBean(be.getErrorCode(), be.getErrorMsg()));
                } else {
                    onError(be.getErrorMsg());
                }
                //系统异常
            } else {
                if (e instanceof HttpException) {
                    //HTTP错误
                    be = new XBaseException(XBaseException.BAD_NETWORK_MSG, e);
                } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
                    //连接错误
                    be = new XBaseException(XBaseException.CONNECT_ERROR_MSG, e);
                } else if (e instanceof InterruptedIOException) {
                    //连接超时
                    be = new XBaseException(XBaseException.CONNECT_TIMEOUT_MSG, e);
                } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
                    //解析错误
                    be = new XBaseException(XBaseException.PARSE_ERROR_MSG, e);
                } else {
                    be = new XBaseException(XBaseException.OTHER_MSG, e);
                }
            }
        } else {
            be = new XBaseException(XBaseException.OTHER_MSG);
        }
        onError(be.getErrorMsg());
    }

    @Override
    public void onComplete() {
        if (view != null && isShowDialog) {
            view.hideLoading();
        }
    }


    public abstract void onSuccess(T o);

    public abstract void onError(String msg);

}
