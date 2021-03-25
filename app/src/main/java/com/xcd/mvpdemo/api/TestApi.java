package com.xcd.mvpdemo.api;


import com.xcd.mvpdemo.bean.Article;
import com.xcd.mvpdemo.bean.User;
import com.xcd.xmvp.base.XBaseBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author xcd
 */
@SuppressWarnings("ALL")
public interface TestApi {
    //-----------------------【首页相关】----------------------
    //首页文章列表 这里的{}是填入页数
    @GET("article/list/{page}/json")
    Observable<XBaseBean<Article>> getArticleList(@Path("page") Integer page);

    //-----------------------【登录注册】----------------------
    //登录
    @FormUrlEncoded
    @POST("user/login")
    Observable<XBaseBean<User>> login(@Field("username") String username, @Field("password") String password);

    //注册
    @FormUrlEncoded
    @POST("user/register")
    Observable<XBaseBean<User>> register(@Field("username") String username, @Field("password") String password, @Field("repassword") String repassword);


    //-----------------------【  收藏  】----------------------

    //收藏站内文章
    @POST("lg/collect/{id}/json")
    Observable<XBaseBean> collectIn(@Path("id") Integer id);

    //取消收藏---文章列表
    @POST("lg/uncollect_originId/{id}/json")
    Observable<XBaseBean> uncollect(@Path("id") Integer id);
}
