package com.xcd.mvpdemo.adapter;

import android.os.Build;
import android.text.Html;
import android.widget.ImageView;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xcd.mvpdemo.R;
import com.xcd.mvpdemo.bean.Article;

import java.util.List;

import static androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY;

public class ArticleAdapter extends BaseQuickAdapter<Article.DataDetailBean, BaseViewHolder> {

    public ArticleAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    private boolean mTypeIsCollect = false;

    public void setType(boolean typeIsCollect) {
        mTypeIsCollect = typeIsCollect;
    }

    @Override
    protected void convert(BaseViewHolder helper, Article.DataDetailBean item) {
        //fromHtml，因为搜索结果中的title中含有html标签
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            helper.setText(R.id.article_title, Html.fromHtml(item.title, FROM_HTML_MODE_LEGACY));
        } else {
            helper.setText(R.id.article_title, Html.fromHtml(item.title));
        }
        helper.setText(R.id.article_chapter, item.chapterName);
        helper.setText(R.id.article_author, item.author);
        //先判断类型是不是收藏列表，因为收藏列表不返回item.collect字段，所以没法判断
        if (item.collect) {
            Glide.with(Utils.getApp()).load(R.drawable.ic_like_checked).into((ImageView) helper.getView(R.id.article_favorite));
        } else {
            Glide.with(Utils.getApp()).load(R.drawable.ic_like_normal).into((ImageView) helper.getView(R.id.article_favorite));
        }
    }


}
