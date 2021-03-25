package com.xcd.mvpdemo.module.home;

import com.xcd.mvpdemo.api.TestApi;
import com.xcd.mvpdemo.bean.Article;
import com.xcd.xmvp.base.XBaseBean;
import com.xcd.xmvp.base.XBaseObserver;
import com.xcd.xmvp.base.XBasePresenter;

/**
 * GitHub : https://github.com/yechaoa
 * CSDN : http://blog.csdn.net/yechaoa
 * <p>
 * Created by yechao on 2018/4/22.
 * Describe :
 */
public class HomePresenter extends XBasePresenter<IHomeView> {

    HomePresenter(IHomeView baseView) {
        super(baseView);
    }

    /**
     * 第一次加载文章列表
     */
    public void getArticleList() {
        addDisposable(retrofitService.createRs(TestApi.class).getArticleList(0), new XBaseObserver<XBaseBean<Article>>(baseView, true) {
            @Override
            public void onSuccess(XBaseBean<Article> bean) {
                baseView.setArticleData(bean);
            }

            @Override
            public void onError(String msg) {
                baseView.showArticleError(msg + "(°∀°)ﾉ");
            }
        });
    }


    /**
     * 收藏
     *
     * @param id 文章id
     */
    public void collect(int id) {
        addDisposable(retrofitService.createRs(TestApi.class).collectIn(id), new XBaseObserver<XBaseBean>(baseView) {
            @Override
            public void onSuccess(XBaseBean bean) {
                baseView.showCollectSuccess("收藏成功（￣▽￣）");
            }

            @Override
            public void onError(String msg) {
                baseView.showCollectError(msg + "(°∀°)ﾉ");
            }
        });
    }

    /**
     * 取消收藏
     *
     * @param id 文章id
     */
    public void uncollect(int id) {
        addDisposable(retrofitService.createRs(TestApi.class).uncollect(id), new XBaseObserver<XBaseBean>(baseView) {
            @Override
            public void onSuccess(XBaseBean bean) {
                baseView.showUncollectSuccess("取消收藏成功（￣▽￣）");
            }

            @Override
            public void onError(String msg) {
                baseView.showUncollectError(msg + "(°∀°)ﾉ");
            }
        });
    }

}
