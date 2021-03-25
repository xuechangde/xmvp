package com.xcd.mvpdemo.module.register;

import com.xcd.mvpdemo.bean.User;
import com.xcd.xmvp.base.XBaseBean;
import com.xcd.xmvp.base.XBaseView;

/**
 * @author xcd
 */
public interface IRegisterView extends XBaseView {

    /**
     * 显示注册成功
     *
     * @param successMessage
     */
    void showRegisterSuccess(String successMessage);

    /**
     * 显示注册失败
     *
     * @param errorMessage
     */
    void showRegisterFailed(String errorMessage);

    /**
     * 注册成功
     *
     * @param user
     */
    void doSuccess(XBaseBean<User> user);

}
