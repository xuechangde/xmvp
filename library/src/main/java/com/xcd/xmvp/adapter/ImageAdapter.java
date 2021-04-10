package com.xcd.xmvp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.Utils;
import com.lxj.easyadapter.EasyAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.ImageViewerPopupView;
import org.xutils.image.ImageOptions;
import java.util.List;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;
import com.xcd.xmvp.R;

public class ImageAdapter extends EasyAdapter<Object> {
    public List<Object> list = null;
    public Context context;

    public ImageAdapter(List<Object> list, Context context) {
        super(list, R.layout.adapter_image);
        this.list = list;
        this.context = context;
    }

    @Override
    protected void bind(@NonNull final ViewHolder holder, @NonNull final Object s, final int position) {
        ImageView imageView = holder.getView(R.id.image);
        // 这样我就能拿到原始图片的Matrix，才能有完美的过渡效果
//        Glide.with(imageView).load(s).apply(new RequestOptions().placeholder(R.drawable.img_bg)
//                .override(Target.SIZE_ORIGINAL))
//                .into(imageView);
        org.xutils.x.image().bind(imageView, s.toString(), new ImageOptions.Builder()
                .setFadeIn(false)//淡入效果
                .setCircular(false) //设置图片显示为圆形
                .setSquare(false) //设置图片显示为正方形
                .setCrop(false).setSize(200,200) //设置大小
                .setFailureDrawable(Utils.getApp().getResources().getDrawable(R.drawable.img_bg)) //设置加载失败的动画
                .setLoadingDrawable(Utils.getApp().getResources().getDrawable(R.drawable.img_bg))
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setIgnoreGif(false) //忽略Gif图片
                .setUseMemCache(true) //设置使用MemCache，默认true
                .build());

        //2. 设置点击
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(holder.itemView.getContext()).asImageViewer(imageView, position, list, true
                        , false, -1, -1, -1
                        , true, Color.rgb(32, 36, 46), new OnSrcViewUpdateListener() {
                            @Override
                            public void onSrcViewUpdate(ImageViewerPopupView popupView, int position) {
                                RecyclerView rv = (RecyclerView) holder.itemView.getParent();
                                popupView.updateSrcView((ImageView) rv.getChildAt(position));
                            }
                        }, new ImageLoader()).show();
            }
        });
    }
}