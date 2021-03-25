package com.xcd.xmvp.util;

import com.blankj.utilcode.util.ActivityUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;

/**
 * @author xcd
 */
public class DialogLoadingUtil {
    private static LoadingPopupView loadingPopup;

    /**
     * 在当前 activity 显示加载中
     */
    public static void showLoading() {
        if(loadingPopup==null){
            loadingPopup = (LoadingPopupView) new XPopup.Builder(ActivityUtils.getTopActivity())
                    .dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .asLoading("加载中")
                    .show();
        }else {
            loadingPopup.show();
        }
    }

    /**
     * loading加载中长度变化
     * @param msg 弹窗文本长度变更
     */
    public static void showLoadingLengthChange(String msg){
        loadingPopup.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingPopup.setTitle(msg);
                loadingPopup.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingPopup.setTitle("");
                    }
                }, 2000);
            }
        }, 2000);
    }

    /**
     * 关闭loading弹窗
     */
    public static void dismissLoading() {
        if (loadingPopup != null && loadingPopup.isShow()) {
            loadingPopup.dismiss();
        }
    }
}
