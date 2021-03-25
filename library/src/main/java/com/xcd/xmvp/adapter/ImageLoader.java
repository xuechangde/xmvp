package com.xcd.xmvp.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.xcd.xmvp.R;

import org.xutils.image.ImageOptions;

import java.io.File;

public class ImageLoader implements XPopupImageLoader {
    @Override
    public void loadImage(int position, @NonNull Object url, @NonNull ImageView imageView) {
        org.xutils.x.image().bind(imageView, url.toString(), new ImageOptions.Builder()
                .setFadeIn(false)//淡入效果
                .setCircular(false) //设置图片显示为圆形
                .setSquare(false) //设置图片显示为正方形
                .setFailureDrawable(Utils.getApp().getResources().getDrawable(R.drawable.img_bg)) //设置加载失败的动画
                .setLoadingDrawable(Utils.getApp().getResources().getDrawable(R.drawable.img_bg))
                .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                .setPlaceholderScaleType(ImageView.ScaleType.FIT_CENTER)
                .setIgnoreGif(false) //忽略Gif图片
                .setUseMemCache(true) //设置使用MemCache，默认true
                .build());
        //必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
//            Glide.with(imageView).load(url).apply(new RequestOptions().placeholder(R.drawable.img_bg).override(Target.SIZE_ORIGINAL)).into(imageView);
    }

    @Override
    public File getImageFile(@NonNull Context context, @NonNull Object uri) {
        try {
            return Glide.with(context).downloadOnly().load(uri).submit().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
