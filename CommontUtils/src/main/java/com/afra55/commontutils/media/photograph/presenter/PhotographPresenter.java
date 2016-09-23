package com.afra55.commontutils.media.photograph.presenter;

import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.ScaleAnimation;

import com.afra55.commontutils.log.LogUtil;
import com.afra55.commontutils.media.CameraUtils;
import com.afra55.commontutils.media.photograph.ui.PhotographUI;
import com.afra55.commontutils.sys.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import static android.content.ComponentCallbacks2.TRIM_MEMORY_BACKGROUND;
import static com.afra55.commontutils.media.CameraUtils.setDisplayOrientation;

/**
 * Created by Victor Yang on 2016/9/23.
 */

public class PhotographPresenter {

    private static final String TAG = PhotographPresenter.class.getSimpleName();

    private PhotographUI mPhotographUI;

    private Camera cameraInst;
    private Camera.Parameters parameters;
    private Camera.Size adapterSize;
    private Camera.Size previewSize;
    private SurfaceView mSurfaceView;

    private int curZoomValue = 0;

    public PhotographPresenter(PhotographUI photographUI) {
        this.mPhotographUI = photographUI;
    }

    public void initSurfaceView() {
        mSurfaceView = mPhotographUI.getSurfaceView();
        SurfaceHolder surfaceHolder = mPhotographUI.getSurfaceView().getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.setKeepScreenOn(true);
        mPhotographUI.getSurfaceView().setFocusable(true);
        mPhotographUI.getSurfaceView().setBackgroundColor(TRIM_MEMORY_BACKGROUND);

        //为SurfaceView的句柄添加一个回调函数
        mPhotographUI.getSurfaceView().getHolder().addCallback(new SurfaceCallback());
    }


    /**
     * 定点对焦的代码, 并显示对焦视图动画
     */
    public void pointFocus(int x, int y) {
        cameraInst.cancelAutoFocus();
        parameters = cameraInst.getParameters();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            showPoint(x, y);
        }
        cameraInst.setParameters(parameters);
        autoFocus();
        mPhotographUI.showFocusIndex(x, y);
    }

    private void showPoint(int x, int y) {
        if (parameters.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> areas = new ArrayList<Camera.Area>();
            //xy变换了
            int rectY = -x * 2000 / ScreenUtil.screenWidth + 1000;
            int rectX = y * 2000 / ScreenUtil.screenHeight - 1000;

            int left = rectX < -900 ? -1000 : rectX - 100;
            int top = rectY < -900 ? -1000 : rectY - 100;
            int right = rectX > 900 ? 1000 : rectX + 100;
            int bottom = rectY > 900 ? 1000 : rectY + 100;
            Rect area1 = new Rect(left, top, right, bottom);
            areas.add(new Camera.Area(area1, 800));
            parameters.setMeteringAreas(areas);
        }

        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
    }

    /**
     * 两点的距离
     */
    public float spacing(MotionEvent event) {
        if (event == null) {
            return 0;
        }
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    public void addZoomIn(int delta) {

        try {
            Camera.Parameters params = cameraInst.getParameters();
            Log.d("Camera", "Is support Zoom " + params.isZoomSupported());
            if (!params.isZoomSupported()) {
                return;
            }
            curZoomValue += delta;
            if (curZoomValue < 0) {
                curZoomValue = 0;
            } else if (curZoomValue > params.getMaxZoom()) {
                curZoomValue = params.getMaxZoom();
            }

            if (!params.isSmoothZoomSupported()) {
                params.setZoom(curZoomValue);
                cameraInst.setParameters(params);
                return;
            } else {
                cameraInst.startSmoothZoom(curZoomValue);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "addZoomIn", e);
        }
    }

    /*SurfaceCallback*/
    private final class SurfaceCallback implements SurfaceHolder.Callback {

        public void surfaceDestroyed(SurfaceHolder holder) {
            try {
                if (cameraInst != null) {
                    cameraInst.stopPreview();
                    cameraInst.release();
                    cameraInst = null;
                }
            } catch (Exception e) {
                //相机已经关了
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (null == cameraInst) {
                try {
                    cameraInst = Camera.open();
                    cameraInst.setPreviewDisplay(holder);
                    initCamera();
                    cameraInst.startPreview();
                } catch (Exception e) {
                    LogUtil.e(TAG, "surfaceCreated", e);
                }
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            autoFocus();
        }
    }

    private void initCamera() {
        parameters = cameraInst.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);
        setUpPicSize();
        setUpPreviewSize();
        //}
        if (adapterSize != null) {
            parameters.setPictureSize(adapterSize.width, adapterSize.height);
        }
        if (previewSize != null) {
            parameters.setPreviewSize(previewSize.width, previewSize.height);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
        } else {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        setDispaly(parameters, cameraInst);
        try {
            cameraInst.setParameters(parameters);
        } catch (Exception e) {
            LogUtil.e(TAG, "cameraInst.setParameters", e);
        }
        cameraInst.startPreview();
        cameraInst.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
    }

    private void setUpPicSize() {

        if (adapterSize != null) {
            return;
        }
        adapterSize = CameraUtils.findBestPictureResolution(cameraInst);
    }

    private void setUpPreviewSize() {

        if (previewSize != null) {
            return;
        }
        previewSize = CameraUtils.findBestPreviewResolution(cameraInst);
    }

    //控制图像的正确显示方向
    private void setDispaly(Camera.Parameters parameters, Camera camera) {
        if (Build.VERSION.SDK_INT >= 8) {
            setDisplayOrientation(camera, 90);
        } else {
            parameters.setRotation(90);
        }
    }

    //实现自动对焦
    private void autoFocus() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (cameraInst == null) {
                    return;
                }
                cameraInst.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success) {
                            initCamera();//实现相机的参数初始化
                        }
                    }
                });
            }
        };
    }

}
