package com.xcd.mvpdemo.module.login;

import com.xcd.xmvp.base.XBaseView;

/**
 * @author xcd
 */
public interface ILoginView extends XBaseView {

    /**
     * 显示登陆成功
     *
     * @param successMessage 成功信息
     */
    void showLoginSuccess(String successMessage);

    /**
     * 显示登陆失败
     *
     * @param errorMessage 失败信息
     */
    void showLoginFailed(String errorMessage);

    void doSuccess();

}
