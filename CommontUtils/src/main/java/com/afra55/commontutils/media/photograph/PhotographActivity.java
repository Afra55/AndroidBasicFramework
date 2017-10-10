package com.afra55.commontutils.media.photograph;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.afra55.commontutils.R;
import com.afra55.commontutils.base.BaseActivity;
import com.afra55.commontutils.log.LogUtils;
import com.afra55.commontutils.media.CameraUtils;
import com.afra55.commontutils.media.photograph.helper.CameraHelper;
import com.afra55.commontutils.sys.ScreenUtil;
import com.afra55.commontutils.ui.dialog.DialogMaker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.afra55.commontutils.media.CameraUtils.setDisplayOrientation;

public class PhotographActivity extends BaseActivity{

    private final static String TAG = LogUtils.makeLogTag(PhotographActivity.class);

    private CameraHelper mCameraHelper;
    private SurfaceView mSurfaceView;
    private Button mTakePhotoBtn;
    private ImageView mFlashImg;
    private ImageView mFlipCamaraImg;
    private View mFocusIndex;
    private float pointX, pointY;
    static final int FOCUS = 1;            // 聚焦
    static final int ZOOM = 2;            // 缩放
    private int mode;                      //0是聚焦 1是放大
    private float dist;
    private Handler handler = new Handler();

    private Camera cameraInst;
    private Camera.Parameters parameters;
    private Camera.Size adapterSize;
    private Camera.Size previewSize;

    private int curZoomValue = 0;
    private int mCurrentCameraId = 0;

    public static void start(Context context) {
        context.startActivity(new Intent(context, PhotographActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photograph);
        initView();
    }

    @Override
    protected String getScreenTitle() {
        return null;
    }

    private void initView() {
        mSurfaceView = findView(R.id.photograph_surface_view);
        mFlashImg = findView(R.id.photograph_flash_img);
        mFlipCamaraImg = findView(R.id.photograph_flip_camara_img);
        mFocusIndex = findView(R.id.photograph_focus_index);
        mTakePhotoBtn = findView(R.id.photograph_take_picture_btn);
        mCameraHelper = new CameraHelper(PhotographActivity.this);
        initSurfaceView();

        initEvent();
    }

    private void initEvent() {

        mFlashImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnLightState();
            }
        });

        mFlipCamaraImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchCamera();
            }
        });

        mTakePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    // 主点按下
                    case MotionEvent.ACTION_DOWN:
                        pointX = motionEvent.getX();
                        pointY = motionEvent.getY();
                        mode = FOCUS;
                        break;
                    // 副点按下
                    case MotionEvent.ACTION_POINTER_DOWN:
                        dist = spacing(motionEvent);
                        // 如果连续两点距离大于10，则判定为多点模式
                        if (spacing(motionEvent) > 10f) {
                            mode = ZOOM;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = FOCUS;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == FOCUS) {
                            //pointFocus((int) event.getRawX(), (int) event.getRawY());
                        } else if (mode == ZOOM) {
                            float newDist = spacing(motionEvent);
                            if (newDist > 10f) {
                                float tScale = (newDist - dist) / dist;
                                if (tScale < 0) {
                                    tScale = tScale * 10;
                                }
                                addZoomIn((int) tScale);
                            }
                        }
                        break;
                }
                return false;
            }
        });

        mSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    pointFocus((int) pointX, (int) pointY);
                } catch (Exception e) {
                    LogUtils.e(TAG, "pointFocus", e);
                }
            }
        });
    }

    private void showFocusIndex(int pointX, int pointY) {
        ConstraintLayout.LayoutParams layout = (ConstraintLayout.LayoutParams) mFocusIndex.getLayoutParams();
        layout.setMargins(pointX - ScreenUtil.dip2px(40) / 2, pointY - ScreenUtil.dip2px(40) / 2, 0, 0);
        mFocusIndex.setVisibility(View.VISIBLE);
        mFocusIndex.setLayoutParams(layout);
        ScaleAnimation sa = new ScaleAnimation(3f, 1f, 3f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(800);
        mFocusIndex.startAnimation(sa);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFocusIndex.setVisibility(View.GONE);
            }
        }, 800);
    }

    private void showLoading(String tip) {
        DialogMaker.showProgressDialog(this, tip);
    }

    private void dismissLoading() {
        DialogMaker.dismissProgressDialog();
    }

    private void handleObtainedImage(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(new File(imagePath));
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        showToast(imagePath);
    }

    private void flashOn() {
        mFlashImg.setImageResource(R.drawable.camera_flash_on);
    }

    private void flashAuto() {
        mFlashImg.setImageResource(R.drawable.camera_flash_auto);
    }

    private void flashOff() {
        mFlashImg.setImageResource(R.drawable.camera_flash_off);
    }

    private SurfaceView getSurfaceView() {
        return mSurfaceView;
    }

    private void initSurfaceView() {
        mSurfaceView = getSurfaceView();
        SurfaceHolder surfaceHolder = getSurfaceView().getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.setKeepScreenOn(true);
        getSurfaceView().setFocusable(true);
        getSurfaceView().setBackgroundColor(TRIM_MEMORY_BACKGROUND);

        //为SurfaceView的句柄添加一个回调函数
        getSurfaceView().getHolder().addCallback(new SurfaceCallback());
    }


    /**
     * 定点对焦的代码, 并显示对焦视图动画
     */
    private void pointFocus(int x, int y) {
        cameraInst.cancelAutoFocus();
        parameters = cameraInst.getParameters();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            showPoint(x, y);
        }
        cameraInst.setParameters(parameters);
        autoFocus();
        showFocusIndex(x, y);
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
    private float spacing(MotionEvent event) {
        if (event == null) {
            return 0;
        }
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void addZoomIn(int delta) {

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
            LogUtils.e(TAG, "addZoomIn", e);
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
                    LogUtils.e(TAG, "surfaceCreated", e);
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
            LogUtils.e(TAG, "cameraInst.setParameters", e);
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

    /**
     * 拍照
     */
    public void takePicture() {
        try {
            cameraInst.takePicture(null, null, new MyPictureCallback());
        } catch (Exception t) {
            LogUtils.e(TAG, "takePicture", t);
            showToast("拍照失败，请重试！");
            try {
                cameraInst.startPreview();
            } catch (Throwable e) {
            }
        }
    }

    private final class MyPictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            Bundle bundle = new Bundle();
            bundle.putByteArray("bytes", data); //将图片字节数据保存在bundle当中，实现数据交换
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<String> subscriber) throws Exception {
                    String imagePath;
                    try {
                        imagePath = CameraUtils.saveToSDCard(data, mCurrentCameraId);
                        subscriber.onNext(imagePath);
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                    subscriber.onComplete();
                }

            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<String>() {

                        @Override
                        public void onStart() {
                            showLoading("处理中");
                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissLoading();
                            LogUtils.e(TAG, "onPictureTaken", e);
                        }

                        @Override
                        public void onComplete() {
                            dismissLoading();
                        }

                        @Override
                        public void onNext(String imagePath) {

                            if (!TextUtils.isEmpty(imagePath)) {
                                dismissLoading();
                                handleObtainedImage(imagePath);
                            } else {
                                showToast("拍照失败，请稍后重试！");
                            }
                        }
                    });
            camera.startPreview(); // 拍完照后，重新开始预览
        }
    }


    /**
     * 闪光灯开关   开->关->自动
     */
    public void turnLightState() {
        if (cameraInst == null || cameraInst.getParameters() == null
                || cameraInst.getParameters().getSupportedFlashModes() == null) {
            return;
        }
        Camera.Parameters parameters = cameraInst.getParameters();
        String flashMode = cameraInst.getParameters().getFlashMode();
        List<String> supportedModes = cameraInst.getParameters().getSupportedFlashModes();
        if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)
                && supportedModes.contains(Camera.Parameters.FLASH_MODE_ON)) {//关闭状态
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            cameraInst.setParameters(parameters);
            flashOn();
        } else if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {//开启状态
            if (supportedModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                flashAuto();
                cameraInst.setParameters(parameters);
            } else if (supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                flashAuto();
                cameraInst.setParameters(parameters);
            }
        } else if (Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)
                && supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            cameraInst.setParameters(parameters);
            flashOff();
        }
    }

    //切换前后置摄像头
    public void switchCamera() {
        mCurrentCameraId = (mCurrentCameraId + 1) % mCameraHelper.getNumberOfCameras();
        releaseCamera();
        Log.d("DDDD", "DDDD----mCurrentCameraId" + mCurrentCameraId);
        setUpCamera(mCurrentCameraId);
    }

    private void releaseCamera() {
        if (cameraInst != null) {
            cameraInst.setPreviewCallback(null);
            cameraInst.release();
            cameraInst = null;
        }
        adapterSize = null;
        previewSize = null;
    }

    private void setUpCamera(int mCurrentCameraId2) {
        cameraInst = getCameraInstance(mCurrentCameraId2);
        if (cameraInst != null) {
            try {
                cameraInst.setPreviewDisplay(getSurfaceView().getHolder());
                initCamera();
                cameraInst.startPreview();
            } catch (Exception e) {
                LogUtils.e(TAG, "setUpCamera", e);
            }
        } else {
            showToast("切换失败，请重试！");
        }
    }

    private Camera getCameraInstance(final int id) {
        Camera c = null;
        try {
            c = mCameraHelper.openCamera(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

}
