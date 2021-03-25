package com.xcd.xmvp.base;

/**
 * @author xcd
 */
@SuppressWarnings("ALL")
public interface XBaseView {

    void showLoading();

    void hideLoading();

    void onErrorCode(XBaseBean bean);

}
