package com.magus.youxiclient.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.ProgressiveJpegConfig;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;
import com.magus.youxiclient.R;

/**
 * Created by yangshuai in the 14:10 of 2016.01.05 .
 * Fresco 图片加载帮助类
 */
public class ImageLoadUtils {
    private ImageLoadUtils() {
    }

    private static class UtilsHolder {
        public static ImageLoadUtils instance = new ImageLoadUtils();
    }

    public static ImageLoadUtils getInstance(Context context) {
        UtilsHolder.instance.initContext(context);
        return UtilsHolder.instance;
    }

    public static ImagePipelineConfig CusstomConfig(Context context) {
        /* 渐进式JPEG图 */
        ProgressiveJpegConfig pjpegConfig = new ProgressiveJpegConfig() {
            @Override
            public int getNextScanNumberToDecode(int scanNumber) {
                return scanNumber + 2;
            }

            public QualityInfo getQualityInfo(int scanNumber) {
                boolean isGoodEnough = (scanNumber >= 5);
                return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
            }
        };

        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setProgressiveJpegConfig(pjpegConfig)
                .build();
        return config;
    }

    private Context context;
    private Postprocessor redMeshPostprocessor = null; // 图片后处理器
    private ControllerListener controllerListener = null;

    public void initContext(Context context){
        this.context = context;
    }

    public GenericDraweeHierarchy initHierarchy(SimpleDraweeView simpleDraweeView) {
        GenericDraweeHierarchy hierarchy = simpleDraweeView.getHierarchy();
        hierarchy.setPlaceholderImage(R.mipmap.ic_launcher); // 修改占位图
        hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP); // 修改缩放类型
        hierarchy.setActualImageFocusPoint(new PointF(0.5f, 0.5f)); // 居中显示
        RoundingParams roundingParams = hierarchy.getRoundingParams();
        roundingParams.setCornersRadius(10);
        roundingParams.setBorder(R.color.gray, 1); // 设置边框颜色及宽度
        // roundingParams.setOverlayColor(R.color.transparent); // 固定背景颜色
        // roundingParams.setCornersRadii(10, 10, 10, 10); // 指定四个角的圆角度数
        // roundingParams.setRoundAsCircle(false); // 设置为圆圈
        hierarchy.setRoundingParams(roundingParams); // 设置圆角
        hierarchy.setFailureImage(context.getResources().getDrawable(R.mipmap.ic_launcher)); // 设置加载失败的占位图
        hierarchy.setRetryImage(context.getResources().getDrawable(R.mipmap.ic_launcher)); // 设置重试加载的占位图
        hierarchy.setProgressBarImage(new ProgressBarDrawable()); // 图片加载进度条, 如果想精确显示加载进度，需要重写 Drawable.onLevelChange
        hierarchy.setFadeDuration(250); // 淡出效果
        return hierarchy;
    }

    public ImageLoadUtils setPostprocessor(Postprocessor redMeshPostprocessor) {
        this.redMeshPostprocessor = redMeshPostprocessor;
        return this;
    }

    /* 设置下载监听 */
    public ImageLoadUtils setControllerListener(ControllerListener controllerListener) {
        this.controllerListener = controllerListener;
        return this;
    }

    public DraweeController initController(SimpleDraweeView simpleDraweeView, Uri uri) {

        if (redMeshPostprocessor == null) {
            redMeshPostprocessor = new BasePostprocessor() {
                @Override
                public String getName() {
                    return "redMeshPostprocessor";
                }

                @Override
                public void process(Bitmap bitmap) {
                /* 在这里进行图片处理 */
                }
            };
        }

        if (controllerListener == null) {
            controllerListener = new BaseControllerListener(){
                @Override
                public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
                    /* 成功 */
                }

                @Override
                public void onIntermediateImageSet(String id, Object imageInfo) {
                    /* 如果允许呈现渐进式JPEG，同时图片也是渐进式图片，onIntermediateImageSet会在每个扫描被解码后回调 */
                }

                @Override
                public void onFailure(String id, Throwable throwable) {
                    /* 失败 */
                }
            };
        }

        ImageRequest imageRequest =
                ImageRequestBuilder
                        .newBuilderWithSource(uri)
                        .setAutoRotateEnabled(true) // 自动旋转
                        .setPostprocessor(redMeshPostprocessor) // 设置后处理器
                        .setProgressiveRenderingEnabled(true) // 渐进式JPEG图
                        .setLocalThumbnailPreviewsEnabled(true) // 缩略图预览, 仅支持本地图片URI
                        .setAutoRotateEnabled(true) // 下载完之后自动播放动画，同时，当View从屏幕移除时，停止播放。支持GIF和WebP 格式图片
                        .build();

        DraweeController controller =
                Fresco.newDraweeControllerBuilder()
                        .setImageRequest(imageRequest)
                        .setControllerListener(controllerListener) // 监听下载事件
                        .setOldController(simpleDraweeView.getController()) // 设置旧 Controller
                        .setTapToRetryEnabled(true) // 点击重新加载图
                        .build();

        return controller;
    }

    /**
     * 支持的 URI 有 http://www.fresco-cn.org/docs/supported-uris.html#_ <br/>
     * 远程图片：http://, https:// <br/>
     * 本地文件：file:// <br/>
     * Content provider： content:// <br/>
     * asset目录下的资源： asset:// <br/>
     * res目录下的资源： Uri uri = Uri.parse("res://包名(实际可以是任何字符串甚至留空)/" + R.drawable.ic_launcher);
     * @param uri
     * @param simpleDraweeView
     */
    public void display(Uri uri, SimpleDraweeView simpleDraweeView) {
        simpleDraweeView.setController(initController(simpleDraweeView, uri));
        simpleDraweeView.setHierarchy(initHierarchy(simpleDraweeView));
    }

    public void display(String url, SimpleDraweeView simpleDraweeView) {
        Uri uri = Uri.parse(url);
        display(uri, simpleDraweeView);
    }

}
