package com.xcd.mvpdemo;


import com.xcd.mvpdemo.contants.Contant;
import com.xcd.xmvp.app.XMvp;

/**
 * @author xcd
 */
public class BaseApplication extends XMvp {
    @Override
    public String initBaseUrl() {
        return Contant.BASE_URL;
    }
}
