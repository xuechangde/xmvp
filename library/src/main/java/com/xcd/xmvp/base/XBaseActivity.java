package com.xcd.xmvp.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gyf.immersionbar.ImmersionBar;
import com.xcd.xmvp.R;
import com.xcd.xmvp.util.DialogLoadingUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class XBaseActivity<P extends XBasePresenter> extends AppCompatActivity implements XBaseView {

    private Unbinder unbinder;

    protected P presenter;

    protected abstract P createPresenter();

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    private Toolbar mToolbar;
    private ImageView iv_back;
    private TextView tv_title;
    private LinearLayout ll_right;
    private TextView tv_right;
    private ImageView iv_right;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(LayoutInflater.from(this).inflate(getLayoutId(), null));
        //绑定数据
        unbinder = ButterKnife.bind(this);
        mToolbar = findViewById(R.id.toolbar);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        ll_right = findViewById(R.id.ll_right);
        tv_right = findViewById(R.id.tv_right);
        iv_right = findViewById(R.id.iv_right);
        presenter = createPresenter();
        //初始化沉浸式
        initImmersionBar();
        //view与数据绑定
        initView();
        //初始化数据
        initData();
    }

    /**
     * 设置中间标题
     * @param title
     */
    protected void setTitle(String title){tv_title.setText(title);}
    /**
     * 设置左侧按键显示或隐藏
     * @param show
     */
    protected void showTitleBack(boolean show){iv_back.setVisibility(show? View.VISIBLE : View.GONE);}
    /**
     * 隐藏右侧按键
     * @param show
     */
    protected void showTitleRight(boolean show){ ll_right.setVisibility(show ? View.VISIBLE : View.GONE);}
    /**
     * 设置标题栏右侧文字或图片
     * @param titleRight
     */
    protected void setTitleRight(String titleRight) {
        tv_right.setText(titleRight);
    }
    protected void setTitleRightColor(int color) {tv_right.setTextColor(color);}
    protected void setRightImg(int Rids) {
        iv_right.setImageResource(Rids);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁时，解除绑定
        if (presenter != null) {
            presenter.detachView();
        }
        unbinder.unbind();
    }

    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected void initImmersionBar() {
        //设置共同沉浸式样式
        ImmersionBar.with(this)
                .titleBar(R.id.toolbar)
                .navigationBarColor(R.color.colorPrimary)
                .init();
    }

    protected void initListener() {
    }

    @Override
    public void showLoading() {
        DialogLoadingUtil.showLoading();
    }

    @Override
    public void hideLoading() {
        DialogLoadingUtil.dismissLoading();
    }

    /**
     * 可以处理异常
     */
    @Override
    public void onErrorCode(XBaseBean bean) {
    }

}
