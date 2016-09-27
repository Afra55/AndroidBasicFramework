package com.afra55.commontutils.media.photograph.ui;

import android.content.Context;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by Victor Yang on 2016/9/23.
 */

public interface PhotographUI {

    SurfaceView getSurfaceView();

    void showFocusIndex(int pointX, int pointY);

    void showToast(String tip);

    void showLoading(String tip);

    void dismissLoading();

    void handleObtainedImage(String imagePath);

    void flashOn();

    void flashAuto();

    void flashOff();

    Context getContext();
}
