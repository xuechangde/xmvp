package com.xcd.mvpdemo.module.login;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.xcd.mvpdemo.R;
import com.xcd.mvpdemo.module.home.MainActivity;
import com.xcd.mvpdemo.module.register.RegisterActivity;
import com.xcd.xmvp.base.XBaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xcd
 */
public class LoginActivity extends XBaseActivity<LoginPresenter> implements ILoginView {

    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.til_username)
    TextInputLayout mTilUsername;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.til_password)
    TextInputLayout mTilPassword;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.btn_register)
    Button mBtnRegister;

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        setTitle("登录");
        showTitleBack(false);
        LoginTextWatcher textWatcher = new LoginTextWatcher(mTilUsername, mTilPassword);
        mEtUsername.addTextChangedListener(textWatcher);
        mEtPassword.addTextChangedListener(textWatcher);
    }


    @Override
    public void showLoginSuccess(String successMessage) {
        ToastUtils.showShort(successMessage);
    }

    @Override
    public void showLoginFailed(String errorMessage) {
        ToastUtils.showShort(errorMessage);
    }

    @Override
    public void doSuccess() {
        ActivityUtils.startActivity(MainActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        ActivityUtils.finishActivity(MainActivity.class);
    }


    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String username = mEtUsername.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();
                int tilUsernameCounterMaxLength = mTilUsername.getCounterMaxLength();
                int tilPasswordCounterMaxLength = mTilPassword.getCounterMaxLength();
                presenter.login(username, password, tilUsernameCounterMaxLength, tilPasswordCounterMaxLength);
                break;
            case R.id.btn_register:
                KeyboardUtils.hideSoftInput(ActivityUtils.getTopActivity());
                ActivityUtils.startActivity(RegisterActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            default:
                break;
        }
    }
}
