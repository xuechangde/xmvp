package com.xcd.mvpdemo.module.register;


import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;
import com.xcd.mvpdemo.module.login.LoginTextWatcher;

/**
 * @author xcd
 */
class RegisterTextWatcher implements TextWatcher {
    TextInputLayout mTilUsername;
    TextInputLayout mTilPassword;
    TextInputLayout mTilRepassword;

    RegisterTextWatcher(TextInputLayout username, TextInputLayout password, TextInputLayout rePassword) {
        mTilUsername = username;
        mTilPassword = password;
        mTilRepassword = rePassword;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        LoginTextWatcher.checkInput(mTilUsername);
        LoginTextWatcher.checkInput(mTilPassword);
        LoginTextWatcher.checkInput(mTilRepassword);
    }
}
