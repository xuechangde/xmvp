package com.xcd.mvpdemo.module.home;


import com.xcd.mvpdemo.bean.Article;
import com.xcd.xmvp.base.XBaseBean;
import com.xcd.xmvp.base.XBaseView;

/**
 * @author xcd
 */
public interface IHomeView extends XBaseView {
    /**
     * 设置文章数据
     *
     * @param list 文章list
     */
    void setArticleData(XBaseBean<Article> list);

    /**
     * 显示文章失败
     *
     * @param errorMessage 失败信息
     */
    void showArticleError(String errorMessage);

    /**
     * 显示收藏成功
     *
     * @param successMessage 成功信息
     */
    void showCollectSuccess(String successMessage);

    /**
     * 显示收藏失败
     *
     * @param errorMessage 失败信息
     */
    void showCollectError(String errorMessage);

    /**
     * 显示未收藏成功
     *
     * @param successMessage 成功信息
     */
    void showUncollectSuccess(String successMessage);

    /**
     * 显示未收藏失败
     *
     * @param errorMessage 失败信息
     */
    void showUncollectError(String errorMessage);
}
