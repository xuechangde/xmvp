package com.xcd.xmvp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    protected Context mContext;

    public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId)
    {
        super(itemLayoutId,mDatas);
        this.mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, T item) {
        converts(helper,item);
    }

    public abstract void converts(BaseViewHolder helper, T item);
}
