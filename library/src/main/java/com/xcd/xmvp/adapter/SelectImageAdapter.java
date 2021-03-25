package com.xcd.xmvp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.luck.picture.lib.entity.LocalMedia;
import com.lxj.easyadapter.EasyAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;

import org.xutils.image.ImageOptions;

import java.util.ArrayList;
import java.util.List;

import com.xcd.xmvp.R;
import com.xcd.xmvp.util.OpenCameraUtils;

public class SelectImageAdapter extends EasyAdapter<Object> {
    public Context context;
    public ImageOptions imageOptions = null;
    public List<LocalMedia> medialist = new ArrayList<>();
    public List<Object> list_img = new ArrayList<>();
    public List<Object> list_img2 = new ArrayList<>();
    public int selectnum = 1;
    public boolean isSingle = false;
    public SelectImageAdapter(Context context, List<Object> list_img, List<Object> list_img2, List<LocalMedia> medialist, int selectnum,boolean isSingle) {
        super(list_img2, R.layout.adapter_image);
        this.context = context;
        this.list_img = list_img;
        this.list_img2 = list_img2;
        this.medialist = medialist;
        this.selectnum = selectnum;
        this.isSingle = isSingle;
        imageOptions = new ImageOptions.Builder()
                .setFadeIn(false)//淡入效果
                .setCircular(false) //设置图片显示为圆形
                .setSquare(false) //设置图片显示为正方形
                .setCrop(false).setSize(200,200) //设置大小
//    .setAnimation(animation) //设置动画
                .setFailureDrawable(context.getResources().getDrawable(R.drawable.img_bg)) //设置加载失败的动画
                .setLoadingDrawable(context.getResources().getDrawable(R.drawable.img_bg))
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
//    .setFailureDrawableId(int failureDrawable) //以资源id设置加载失败的动画
//    .setLoadingDrawableId(int loadingDrawable) //以资源id设置加载中的动画
                .setIgnoreGif(false) //忽略Gif图片
//    .setParamsBuilder(ParamsBuilder paramsBuilder) //在网络请求中添加一些参数
//    .setRaduis(int raduis) //设置拐角弧度
                .setUseMemCache(true) //设置使用MemCache，默认true
                .build();
    }

    @Override
    protected void bind(@NonNull final ViewHolder holder, @NonNull final Object s, final int position) {
        final ImageView imageView = holder.<ImageView>getView(R.id.image);
        final TextView tv_clear_img = holder.getView(R.id.tv_clear_img);
        // 这样我就能拿到原始图片的Matrix，才能有完美的过渡效果
        if(TextUtils.isEmpty(s.toString())&&holder.getPosition()==list_img2.size()-1){
            imageView.setBackground(context.getResources().getDrawable(R.drawable.ico_add_pic));
            imageView.setImageBitmap(null);
            tv_clear_img.setVisibility(View.GONE);
        }else{
            tv_clear_img.setVisibility(View.VISIBLE);
            tv_clear_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FileUtils.delete(s.toString());
                    list_img2.remove(holder.getPosition());
                    if(list_img.size()==selectnum){
                        list_img2.add("");
                    }
                    medialist.remove(holder.getPosition());
                    list_img.remove(holder.getPosition());
                    notifyItemRemoved(holder.getPosition());
                }
            });
            org.xutils.x.image().bind(imageView,s.toString(),imageOptions);
            // 这样我就能拿到原始图片的Matrix，才能有完美的过渡效果
//            Glide.with(imageView).load(s).apply(new RequestOptions().placeholder(R.drawable.img_bg)
//                    .override(Target.SIZE_ORIGINAL))
//                    .into(imageView);
        }

        //2. 设置点击
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(s.toString())&&holder.getPosition()==list_img2.size()-1){
                    if(isSingle){
                        OpenCameraUtils.openCamera(true,selectnum,medialist);
                    }else{
                        OpenCameraUtils.openCamera(false,selectnum,medialist);
                    }
                }else{
                    new XPopup.Builder(holder.itemView.getContext()).asImageViewer(imageView, position, list_img,true
                            ,false,-1,-1,-1,false,Color.rgb(32, 36, 46),new OnSrcViewUpdateListener() {
                                @Override
                                public void onSrcViewUpdate(ImageViewerPopupView popupView, int position) {
                                    View view = LayoutInflater.from(context)
                                            .inflate(R.layout.adapter_image2,popupView,false);
                                    ImageView iv = view.findViewById(R.id.image);
                                    popupView.updateSrcView(iv);
                                }
                            }, new ImageLoader())
                            .show();
                }
            }
        });
    }
}
