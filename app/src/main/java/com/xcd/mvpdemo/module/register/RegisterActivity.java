package com.xcd.mvpdemo.module.register;

import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.xcd.mvpdemo.R;
import com.xcd.mvpdemo.bean.User;
import com.xcd.xmvp.base.XBaseActivity;
import com.xcd.xmvp.base.XBaseBean;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xcd
 */
public class RegisterActivity extends XBaseActivity<RegisterPresenter> implements IRegisterView {

    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.til_username)
    TextInputLayout mTilUsername;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.til_password)
    TextInputLayout mTilPassword;
    @BindView(R.id.et_repassword)
    EditText mEtRepassword;
    @BindView(R.id.til_repassword)
    TextInputLayout mTilRepassword;
    @BindView(R.id.btn_register)
    Button mBtnRegister;

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        TextWatcher textWatcher = new RegisterTextWatcher(mTilUsername, mTilPassword, mTilRepassword);
        mEtUsername.addTextChangedListener(textWatcher);
        mEtPassword.addTextChangedListener(textWatcher);
        mEtRepassword.addTextChangedListener(textWatcher);
    }

    @Override
    protected void initData() {
    }

    @Override
    public void showRegisterSuccess(String successMessage) {
        ToastUtils.showShort(successMessage);
    }

    @Override
    public void showRegisterFailed(String errorMessage) {
        ToastUtils.showShort(errorMessage);
    }

    @Override
    public void doSuccess(XBaseBean<User> user) {
        finish();
    }


    @OnClick(R.id.btn_register)
    public void onViewClicked() {
        String username = mEtUsername.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        String rePassword = mEtRepassword.getText().toString().trim();
        int tilUsernameCounterMaxLength = mTilUsername.getCounterMaxLength();
        int tilPasswordCounterMaxLength = mTilPassword.getCounterMaxLength();
        int tilRePasswordCounterMaxLength = mTilRepassword.getCounterMaxLength();
        presenter.register(username, password, rePassword, tilUsernameCounterMaxLength, tilPasswordCounterMaxLength, tilRePasswordCounterMaxLength);
    }
}
