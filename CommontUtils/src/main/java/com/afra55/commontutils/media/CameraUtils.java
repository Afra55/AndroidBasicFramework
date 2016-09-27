package com.afra55.commontutils.media;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;

import com.afra55.commontutils.file.AttachmentStore;
import com.afra55.commontutils.log.LogUtil;
import com.afra55.commontutils.storage.StorageUtil;
import com.afra55.commontutils.sys.ScreenUtil;

import java.io.ByteArrayInputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Victor Yang on 2016/9/23.
 * 相机相关帮助类
 */

public class CameraUtils {

    private static final String TAG = CameraUtils.class.getSimpleName();

    /**
     * 最小预览界面的分辨率
     */
    private static final int MIN_PREVIEW_PIXELS = 480 * 320;
    /**
     * 最大宽高比差
     */
    private static final double MAX_ASPECT_DISTORTION = 0.15;

    private CameraUtils() {
    }

    /**
     * 获取最好的图片分辨率
     *
     * @param cameraInst Camera
     * @return Camera.Size
     */
    public static Camera.Size findBestPictureResolution(Camera cameraInst) {
        return findBestPictureResolution(cameraInst, MAX_ASPECT_DISTORTION);
    }

    public static Camera.Size findBestPictureResolution(Camera cameraInst, double maxAspectDistortion) {
        Camera.Parameters cameraParameters = cameraInst.getParameters();
        List<Camera.Size> supportedPicResolutions = cameraParameters.getSupportedPictureSizes(); // 至少会返回一个值

        StringBuilder picResolutionSb = new StringBuilder();
        for (Camera.Size supportedPicResolution : supportedPicResolutions) {
            picResolutionSb.append(supportedPicResolution.width).append('x')
                    .append(supportedPicResolution.height).append(" ");
        }
        LogUtil.d(TAG, "Supported picture resolutions: " + picResolutionSb);

        Camera.Size defaultPictureResolution = cameraParameters.getPictureSize();
        LogUtil.d(TAG, "default picture resolution " + defaultPictureResolution.width + "x"
                + defaultPictureResolution.height);

        // 排序
        List<Camera.Size> sortedSupportedPicResolutions = new ArrayList<Camera.Size>(
                supportedPicResolutions);
        Collections.sort(sortedSupportedPicResolutions, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });

        // 移除不符合条件的分辨率
        double screenAspectRatio = (double) ScreenUtil.screenWidth
                / (double) ScreenUtil.screenHeight;
        Iterator<Camera.Size> it = sortedSupportedPicResolutions.iterator();
        while (it.hasNext()) {
            Camera.Size supportedPreviewResolution = it.next();
            int width = supportedPreviewResolution.width;
            int height = supportedPreviewResolution.height;

            // 在camera分辨率与屏幕分辨率宽高比不相等的情况下，找出差距最小的一组分辨率
            // 由于camera的分辨率是width>height，我们设置的portrait模式中，width<height
            // 因此这里要先交换然后在比较宽高比
            boolean isCandidatePortrait = width > height;
            int maybeFlippedWidth = isCandidatePortrait ? height : width;
            int maybeFlippedHeight = isCandidatePortrait ? width : height;
            double aspectRatio = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
            double distortion = Math.abs(aspectRatio - screenAspectRatio);
            if (distortion > maxAspectDistortion) {
                it.remove();
            }
        }
        // 如果没有找到合适的，并且还有候选的像素，对于照片，则取其中最大比例的，而不是选择与屏幕分辨率相同的
        if (!sortedSupportedPicResolutions.isEmpty()) {
            return sortedSupportedPicResolutions.get(0);
        }

        // 没有找到合适的，就返回默认的
        return defaultPictureResolution;
    }

    /**
     * 找出最适合的预览界面分辨率
     *
     * @return
     */
    public static Camera.Size findBestPreviewResolution(Camera cameraInst) {
        return findBestPreviewResolution(cameraInst, MIN_PREVIEW_PIXELS, MAX_ASPECT_DISTORTION);
    }

    public static Camera.Size findBestPreviewResolution(Camera cameraInst, double minPreviewPixels, double maxAspectDistortion) {
        Camera.Parameters cameraParameters = cameraInst.getParameters();
        Camera.Size defaultPreviewResolution = cameraParameters.getPreviewSize();

        List<Camera.Size> rawSupportedSizes = cameraParameters.getSupportedPreviewSizes();
        if (rawSupportedSizes == null) {
            return defaultPreviewResolution;
        }

        // 按照分辨率从大到小排序
        List<Camera.Size> supportedPreviewResolutions = new ArrayList<Camera.Size>(rawSupportedSizes);
        Collections.sort(supportedPreviewResolutions, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });

        StringBuilder previewResolutionSb = new StringBuilder();
        for (Camera.Size supportedPreviewResolution : supportedPreviewResolutions) {
            previewResolutionSb.append(supportedPreviewResolution.width).append('x').append(supportedPreviewResolution.height)
                    .append(' ');
        }
        LogUtil.v(TAG, "Supported preview resolutions: " + previewResolutionSb);


        // 移除不符合条件的分辨率
        double screenAspectRatio = (double) ScreenUtil.screenWidth
                / (double) ScreenUtil.screenHeight;
        Iterator<Camera.Size> it = supportedPreviewResolutions.iterator();
        while (it.hasNext()) {
            Camera.Size supportedPreviewResolution = it.next();
            int width = supportedPreviewResolution.width;
            int height = supportedPreviewResolution.height;

            // 移除低于下限的分辨率，尽可能取高分辨率
            if (width * height < minPreviewPixels) {
                it.remove();
                continue;
            }

            // 在camera分辨率与屏幕分辨率宽高比不相等的情况下，找出差距最小的一组分辨率
            // 由于camera的分辨率是width>height，我们设置的portrait模式中，width<height
            // 因此这里要先交换然preview宽高比后在比较
            boolean isCandidatePortrait = width > height;
            int maybeFlippedWidth = isCandidatePortrait ? height : width;
            int maybeFlippedHeight = isCandidatePortrait ? width : height;
            double aspectRatio = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
            double distortion = Math.abs(aspectRatio - screenAspectRatio);
            if (distortion > maxAspectDistortion) {
                it.remove();
                continue;
            }

            // 找到与屏幕分辨率完全匹配的预览界面分辨率直接返回
            if (maybeFlippedWidth == ScreenUtil.screenWidth
                    && maybeFlippedHeight == ScreenUtil.screenHeight) {
                return supportedPreviewResolution;
            }
        }

        // 如果没有找到合适的，并且还有候选的像素，则设置其中最大比例的，对于配置比较低的机器不太合适
        if (!supportedPreviewResolutions.isEmpty()) {
            Camera.Size largestPreview = supportedPreviewResolutions.get(0);
            return largestPreview;
        }

        // 没有找到合适的，就返回默认的

        return defaultPreviewResolution;
    }

    /**
     * 实现的图像的正确显示
     */
    public static void setDisplayOrientation(Camera camera, int i) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation",
                    new Class[]{int.class});
            if (downPolymorphic != null) {
                downPolymorphic.invoke(camera, new Object[]{i});
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "setDisplayOrientation", e);
        }
    }

    /**
     * 将拍下来的照片存放在SD卡中
     *
     * @param data             byte[]
     * @param mCurrentCameraId 摄像头id， 1是前置 0是后置
     * @throws IOException
     */
    public static String saveToSDCard(byte[] data, int mCurrentCameraId) throws IOException {
        Bitmap croppedImage;

        //获得图片大小
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        int width = options.outWidth;
        int height = options.outHeight;
        options.inJustDecodeBounds = false;
        Rect r = new Rect(0, 0, width, height);
        try {
            croppedImage = decodeRegionCrop(data, r, mCurrentCameraId, width, height);
        } catch (Exception e) {
            return null;
        }
        String filePath =
                StorageUtil.getSystemPhotoPath() + "/" + System.currentTimeMillis() + ".jpg";
        AttachmentStore.saveBitmap(croppedImage, filePath, true);
        croppedImage.recycle();
        return filePath;
    }

    private static Bitmap decodeRegionCrop(byte[] data, Rect rect, int mCurrentCameraId,  int width, int height) {

        InputStream is = null;
        System.gc();
        Bitmap croppedImage = null;
        try {
            is = new ByteArrayInputStream(data);
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(is, false);

            try {
                croppedImage = decoder.decodeRegion(rect, new BitmapFactory.Options());
            } catch (IllegalArgumentException e) {
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (Exception e) {

            }
        }
        Matrix m = new Matrix();
        m.setRotate(90, width, height);
        if (mCurrentCameraId == 1) {
            m.postScale(1, -1);
        }
        Bitmap rotatedImage = Bitmap.createBitmap(croppedImage, 0, 0, width, height, m, true);
        if (rotatedImage != croppedImage)
            croppedImage.recycle();
        return rotatedImage;
    }
}
