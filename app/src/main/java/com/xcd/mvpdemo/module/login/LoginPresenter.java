package com.xcd.mvpdemo.module.login;

import android.text.TextUtils;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.xcd.mvpdemo.api.TestApi;
import com.xcd.mvpdemo.bean.User;
import com.xcd.xmvp.base.XBaseBean;
import com.xcd.xmvp.base.XBaseObserver;
import com.xcd.xmvp.base.XBasePresenter;
import com.xcd.xmvp.common.GlobalConstant;

/**
 * @author xcd
 */
class LoginPresenter extends XBasePresenter<ILoginView> {

    LoginPresenter(ILoginView baseView) {
        super(baseView);
    }

    /**
     * 登陆
     * @param username         username
     * @param password         password
     * @param usernameCountMax 账号规定输入字符最大值
     * @param passwordCountMax 密码规定输入字符最大值
     */
    void login(String username, String password, int usernameCountMax, int passwordCountMax) {
        KeyboardUtils.hideSoftInput(ActivityUtils.getTopActivity());
        //判断输入的账号密码是否符合规范
        if (isValid(username, password, usernameCountMax, passwordCountMax)) {
            addDisposable(retrofitService.createRs(TestApi.class).login(username, password), new XBaseObserver<XBaseBean<User>>(baseView, true) {
                @Override
                public void onSuccess(XBaseBean<User> bean) {
                    baseView.showLoginSuccess("登录成功（￣▽￣）");
                    //将登陆的账号存进sp里面
                    SPUtils.getInstance().put(GlobalConstant.IS_LOGIN, true);
                    SPUtils.getInstance().put(GlobalConstant.USERNAME, bean.data.username);
                    SPUtils.getInstance().put(GlobalConstant.PASSWORD, bean.data.password);
                    baseView.doSuccess();
                }

                @Override
                public void onError(String msg) {
                    baseView.showLoginFailed(msg + "(°∀°)ﾉ");
                }
            });
        } else {
            baseView.showLoginFailed("填写错误 (°∀°)ﾉ");
        }
    }


    /**
     * 判断输入的账号密码是否符合规范
     *
     * @param userName         username
     * @param password         password
     * @param usernameCountMax 账号规定输入字符最大值
     * @param passwordCountMax 密码规定输入字符最大值
     * @return 是否合规
     */
    private boolean isValid(String userName, String password, int usernameCountMax, int passwordCountMax) {
        return check(userName, usernameCountMax) && check(password, passwordCountMax);
    }

    /**
     * 判断输入是否规范
     *
     * @param string              输入的内容
     * @param tilCounterMaxLength textInputLayout控件的输入字符的最大长度
     * @return 是否合规
     */
    private boolean check(String string, int tilCounterMaxLength) {
        return !TextUtils.isEmpty(string) && string.length() <= tilCounterMaxLength && tilCounterMaxLength / 2 <= string.length();
    }
}
