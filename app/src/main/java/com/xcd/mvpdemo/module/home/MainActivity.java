package com.xcd.mvpdemo.module.home;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.xcd.mvpdemo.R;
import com.xcd.mvpdemo.adapter.ArticleAdapter;
import com.xcd.mvpdemo.bean.Article;
import com.xcd.xmvp.base.XBaseActivity;
import com.xcd.xmvp.base.XBaseBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author xcd
 */
public class MainActivity extends XBaseActivity<HomePresenter> implements IHomeView,OnItemChildClickListener {

    @BindView(R.id.home_recycler_view)
    RecyclerView mHomeRecyclerView;
    private ArticleAdapter mArticleAdapter;
    private List<Article.DataDetailBean> mArticles = new ArrayList<>();
    private int mPosition;

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        showTitleBack(false);
        setTitle("首页");
        mHomeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {
        presenter.getArticleList();
    }

    @Override
    public void setArticleData(XBaseBean<Article> list) {
        mArticles = list.data.datas;
        mArticleAdapter = new ArticleAdapter(R.layout.item_article_list, list.data.datas);
        mHomeRecyclerView.setAdapter(mArticleAdapter);
        mArticleAdapter.addChildClickViewIds(R.id.article_favorite);
        mArticleAdapter.setOnItemChildClickListener(this);
    }

    @Override
    public void showArticleError(String errorMessage) {
        ToastUtils.showShort(errorMessage);
    }

    @Override
    public void showCollectSuccess(String successMessage) {
        ToastUtils.showShort(successMessage);
        mArticles.get(mPosition).collect = true;
        //因为收藏成功，所以要刷新界面，以显示小红心
        mArticleAdapter.notifyDataSetChanged();
    }

    @Override
    public void showCollectError(String errorMessage) {
        ToastUtils.showShort(errorMessage);
    }

    @Override
    public void showUncollectSuccess(String successMessage) {
        ToastUtils.showShort(successMessage);
        mArticles.get(mPosition).collect = false;
        //因为取消收藏成功，所以要刷新界面，以取消显示小红心
        mArticleAdapter.notifyDataSetChanged();
    }

    @Override
    public void showUncollectError(String errorMessage) {
        ToastUtils.showShort(errorMessage);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.article_favorite) {
            mPosition = position;
            if (mArticles.get(position).collect) {
                presenter.uncollect(mArticles.get(position).id);
            } else {
                presenter.collect(mArticles.get(position).id);
            }
        }
    }
}
