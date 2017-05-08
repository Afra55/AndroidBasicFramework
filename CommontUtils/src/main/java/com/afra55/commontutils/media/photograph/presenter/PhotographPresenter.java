package com.afra55.commontutils.media.photograph.presenter;

import android.hardware.Camera;
import android.view.SurfaceView;

import com.afra55.commontutils.media.photograph.helper.CameraHelper;
import com.afra55.commontutils.media.photograph.ui.PhotographUI;

/**
 * Created by Victor Yang on 2016/9/23.
 */

public class PhotographPresenter {

    private static final String TAG = PhotographPresenter.class.getSimpleName();

    private PhotographUI mPhotographUI;

    private CameraHelper mCameraHelper;
    private Camera cameraInst;
    private Camera.Parameters parameters;
    private Camera.Size adapterSize;
    private Camera.Size previewSize;
    private SurfaceView mSurfaceView;

    private int curZoomValue = 0;
    private int mCurrentCameraId = 0;

    public PhotographPresenter(PhotographUI photographUI) {
        this.mPhotographUI = photographUI;
        mCameraHelper = new CameraHelper(mPhotographUI.getContext());
    }


}
