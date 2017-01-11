package com.afra55.commontutils.media.photograph;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.afra55.commontutils.R;
import com.afra55.commontutils.base.BaseActivity;
import com.afra55.commontutils.log.LogUtils;
import com.afra55.commontutils.media.photograph.helper.CameraHelper;
import com.afra55.commontutils.media.photograph.presenter.PhotographPresenter;
import com.afra55.commontutils.media.photograph.ui.PhotographUI;
import com.afra55.commontutils.sys.ScreenUtil;
import com.afra55.commontutils.ui.dialog.DialogMaker;

import java.io.File;

public class PhotographActivity extends BaseActivity implements PhotographUI{

    private final static String TAG = PhotographActivity.class.getSimpleName();

    private PhotographPresenter mPhotographPresenter;


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

    public static void start(Context context) {
        context.startActivity(new Intent(context, PhotographActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photograph);
        mPhotographPresenter = new PhotographPresenter(this);
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
        mCameraHelper = new CameraHelper(this);
        mPhotographPresenter.initSurfaceView();

        initEvent();
    }

    private void initEvent() {

        mFlashImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotographPresenter.turnLightState();
            }
        });

        mFlipCamaraImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotographPresenter.switchCamera();
            }
        });

        mTakePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotographPresenter.takePicture();
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
                        dist = mPhotographPresenter.spacing(motionEvent);
                        // 如果连续两点距离大于10，则判定为多点模式
                        if (mPhotographPresenter.spacing(motionEvent) > 10f) {
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
                            float newDist = mPhotographPresenter.spacing(motionEvent);
                            if (newDist > 10f) {
                                float tScale = (newDist - dist) / dist;
                                if (tScale < 0) {
                                    tScale = tScale * 10;
                                }
                                mPhotographPresenter.addZoomIn((int) tScale);
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
                    mPhotographPresenter.pointFocus((int) pointX, (int) pointY);
                } catch (Exception e) {
                    LogUtils.e(TAG, "pointFocus", e);
                }
            }
        });
    }

    @Override
    public void showFocusIndex(int pointX, int pointY) {
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

    @Override
    public void showLoading(String tip) {
        DialogMaker.showProgressDialog(this, tip);
    }

    @Override
    public void dismissLoading() {
        DialogMaker.dismissProgressDialog();
    }

    @Override
    public void handleObtainedImage(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(new File(imagePath));
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        showToast(imagePath);
    }

    @Override
    public void flashOn() {
        mFlashImg.setImageResource(R.drawable.camera_flash_on);
    }

    @Override
    public void flashAuto() {
        mFlashImg.setImageResource(R.drawable.camera_flash_auto);
    }

    @Override
    public void flashOff() {
        mFlashImg.setImageResource(R.drawable.camera_flash_off);
    }

    @Override
    public SurfaceView getSurfaceView() {
        return mSurfaceView;
    }

}
