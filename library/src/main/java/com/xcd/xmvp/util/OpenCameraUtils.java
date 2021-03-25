package com.xcd.xmvp.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.util.ArrayList;
import java.util.List;

import com.xcd.xmvp.R;

public class OpenCameraUtils {
    /**
     * 需要进行检测的权限数组
     */
    public static String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public static void openCamera(boolean isCamera,int selectnum, List<LocalMedia> list) {
        if (PermissionUtils.isGranted(needPermissions)) {
            if (isCamera) {
                openCamera();
            }else{
                openGallery(selectnum, list);
            }
        } else {
            checkPermissions(isCamera, selectnum, list, needPermissions);
        }
    }

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private static final int PERMISSON_REQUESTCODE = 0;

    /**
     * @param permissions
     * @since 2.5.0
     */
    private static void checkPermissions(boolean isCamera, int selectnum, List<LocalMedia> list, String... permissions) {
        try {
            if (Build.VERSION.SDK_INT >= 23
                    && ActivityUtils.getTopActivity().getApplicationInfo().targetSdkVersion >= 23) {
                List<String> needRequestPermissonList = findDeniedPermissions(ActivityUtils.getTopActivity(), permissions);
                if (null != needRequestPermissonList
                        && needRequestPermissonList.size() > 0) {
                    String[] array = needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
                    java.lang.reflect.Method method = ActivityUtils.getTopActivity().getClass().getMethod("requestPermissions", new Class[]{String[].class,
                            int.class});

                    method.invoke(ActivityUtils.getTopActivity(), array, PERMISSON_REQUESTCODE);
                }
                if (needRequestPermissonList.size() == 0) {
                    if (isCamera) {
                        openCamera();
                    }else{
                        openGallery(selectnum, list);
                    }
                }
            }
        } catch (Throwable e) {
        }
    }

    public static void openGallery(int selectnum, List<LocalMedia> list) {
        PictureSelector.create(ActivityUtils.getTopActivity())
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_QQ_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项   参考Demo MainActivity中代码
                .isWithVideoImage(false)// 图片和视频是否可以同选,只在ofAll模式下有效
                .isUseCustomCamera(false)// 是否使用自定义相机
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)// 设置相册Activity方向，不设置默认使用系统
                .isOriginalImageControl(false)// 是否显示原图控制按钮，如果用户勾选了 压缩、裁剪功能将会失效
                .isWeChatStyle(false)// 是否开启微信图片选择风格，此开关开启了才可使用微信主题！！！
                .isAndroidQTransform(true)//是否需要处理Androi d Q 拷贝至应用沙盒的操作，只针对compress(false); && enableCrop(false);有效
                .isMultipleSkipCrop(false)// 多图裁剪时是否支持跳过，默认支持
//                .setLanguage(language)// 设置语言，默认中文
                .isPageStrategy(true)// 是否开启分页策略 & 每页多少条；默认开启
//                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
//                .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
//                .setPictureWindowAnimationStyle(mWindowAnimationStyle)// 自定义相册启动退出动画
//                .setRecyclerAnimationMode(animationMode)// 列表动画效果
                .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
                //.isAutomaticTitleRecyclerTop(false)// 连续点击标题栏RecyclerView是否自动回到顶部,默认true
                .loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())// 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
                //.setOutputCameraPath()// 自定义相机输出目录，只针对Android Q以下，例如 Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) +  File.separator + "Camera" + File.separator;
                //.setButtonFeatures(CustomCameraView.BUTTON_STATE_BOTH)// 设置自定义相机按钮状态
                .maxSelectNum(selectnum)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .minVideoSelectNum(1)// 视频最小选择数量，如果没有单独设置的需求则可以不设置，同用minSelectNum字段
                .maxVideoSelectNum(1) // 视频最大选择数量，如果没有单独设置的需求则可以不设置，同用maxSelectNum字段
                //.closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 关闭在AndroidQ下获取图片或视频宽高相反自动转换
                .imageSpanCount(4)// 每行显示个数 int
                .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                .closeAndroidQChangeWH(true)//如果图片有旋转角度则对换宽高,默认为true
                .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                .queryMaxFileSize(20)// 只查多少M以内的图片、视频、音频  单位M
//                .querySpecifiedFormatSuffix(PictureMimeType.ofPNG())// 查询指定后缀格式资源
                //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义视频播放回调控制，用户可以使用自己的视频播放界面
                //.bindCustomPreviewCallback(new MyCustomPreviewInterfaceListener())// 自定义图片预览回调接口
                //.bindCustomCameraInterfaceListener(new MyCustomCameraInterfaceListener())// 提供给用户的一些额外的自定义操作回调
//                .cameraFileName(System.currentTimeMillis() +".jpg") // 重命名拍照文件名、注意这个只在使用相机时可以使用
//                .renameCompressFile(System.currentTimeMillis() +".jpg")// 重命名压缩文件名、 注意这个不要重复，只适用于单张图压缩使用
//                .renameCropFileName(System.currentTimeMillis() +".jpg"// 重命名裁剪文件名、 注意这个不要重复，只适用于单张图裁剪使用
                .isSingleDirectReturn(false)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
//                .setTitleBarBackgroundColor()//相册标题栏背景色
//                .isChangeStatusBarFontColor()// 是否关闭白色状态栏字体颜色
//                .setStatusBarColorPrimaryDark()// 状态栏背景色
//                .setUpArrowDrawable()// 设置标题栏右侧箭头图标
//                .setDownArrowDrawable()// 设置标题栏右侧箭头图标
//                .isOpenStyleCheckNumMode()// 是否开启数字选择模式 类似QQ相册
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .isPreviewImage(true)// 是否可预览图片 true or false
                .isPreviewVideo(true)// 是否可预览视频 true or false
                .isEnablePreviewAudio(true) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                //.isMultipleRecyclerAnimation(false)// 多图裁剪底部列表显示动画效果
//                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
//                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .isEnableCrop(false)// 是否裁剪 true or false
                //.basicUCropConfig()//对外提供所有UCropOptions参数配制，但如果PictureSelector原本支持设置的还是会使用原有的设置
                //.setCropDimmedColor(ContextCompat.getColor(getContext(), R.color.app_color_white))// 设置裁剪背景色值
//                .setCircleDimmedColor()// 设置圆形裁剪背景色值
//                .setCircleDimmedBorderColor()// 设置圆形裁剪边框色值
                .setCircleStrokeWidth(3)// 设置圆形裁剪边框粗细
                .isCompress(true)// 是否压缩 true or false
                .compressQuality(100)// 图片压缩后输出质量 0~ 100
//                .glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
//                .compressSavePath(getPath())//压缩图片保存地址
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .isOpenClickSound(false)// 是否开启点击声音 true or false
                .selectionData(list)// 是否传入已选图片 List<LocalMedia> list
                .isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cutOutQuality(90)// 裁剪输出质量 默认100
                .minimumCompressSize(1)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .cropWH()// 裁剪后图片输出宽高，设置如果大于图片本身宽高则无效 int
                //.cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .videoQuality(0)// 视频录制质量 0 or 1 int
                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                .recordVideoSecond(60)//视频秒数录制 默认60s int
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    public static void openCamera(){
        PictureSelector.create(ActivityUtils.getTopActivity())
                .openCamera(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_QQ_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项   参考Demo MainActivity中代码
                .isUseCustomCamera(false)// 是否使用自定义相机
                //.setButtonFeatures(CustomCameraView.BUTTON_STATE_BOTH)// 设置自定义相机按钮状态
                .closeAndroidQChangeWH(true)//如果图片有旋转角度则对换宽高,默认为true
                .isSingleDirectReturn(false)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .isCompress(true)// 是否压缩 true or false
                .compressQuality(100)// 图片压缩后输出质量 0~ 100
                .minimumCompressSize(1)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .forResult(PictureConfig.REQUEST_CAMERA);//结果回调onActivityResult code
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private static List<String> findDeniedPermissions(Activity activity, String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= 23
                && activity.getApplicationInfo().targetSdkVersion >= 23) {
            try {
                for (String perm : permissions) {
                    java.lang.reflect.Method checkSelfMethod = activity.getClass().getMethod("checkSelfPermission", String.class);
                    java.lang.reflect.Method shouldShowRequestPermissionRationaleMethod = activity.getClass().getMethod("shouldShowRequestPermissionRationale",
                            String.class);
                    if ((Integer) checkSelfMethod.invoke(activity, perm) != PackageManager.PERMISSION_GRANTED
                            || (Boolean) shouldShowRequestPermissionRationaleMethod.invoke(activity, perm)) {
                        needRequestPermissonList.add(perm);
                    }
                }
            } catch (Throwable e) {

            }
        }
        return needRequestPermissonList;
    }

    /**
     * 清空缓存包括裁剪、压缩、AndroidQToPath所生成的文件，注意调用时机必须是处理完本身的业务逻辑后调用；非强制性
     */
    public void clearCache(Activity activity) {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        if (PermissionChecker.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //PictureFileUtils.deleteCacheDirFile(this, PictureMimeType.ofImage());
            PictureFileUtils.deleteAllCacheDirFile(activity);
        } else {
            PermissionChecker.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE);
        }
    }
}
