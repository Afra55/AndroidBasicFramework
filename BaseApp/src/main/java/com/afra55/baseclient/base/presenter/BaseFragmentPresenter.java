package com.afra55.baseclient.base.presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.afra55.baseclient.base.BaseActivity;
import com.afra55.baseclient.base.OnFragmentInteractionListener;
import com.afra55.baseclient.base.OnFragmentSelectListener;
import com.afra55.baseclient.base.ui.BaseFragmentUI;
import com.afra55.commontutils.device.KeyBoardUtils;
import com.afra55.commontutils.log.LogUtil;

/**
 * Created by Victor Yang on 2016/6/28.
 */
public class BaseFragmentPresenter {

    private BaseFragmentUI mBaseFragmentUI;

    private BaseActivity mActivity;

    private OnFragmentInteractionListener mInteractionListener;

    private OnFragmentSelectListener mFragmentSelectListener;

    public BaseFragmentPresenter(BaseFragmentUI baseFragmentUI) {
        mBaseFragmentUI = baseFragmentUI;
        if (baseFragmentUI.getFragment() instanceof OnFragmentSelectListener) {
            mFragmentSelectListener = (OnFragmentSelectListener) baseFragmentUI.getFragment();
        } else {
            throw new RuntimeException(baseFragmentUI.getFragment().toString()
                    + " must implement OnFragmentSelectListener");
        }
    }

    private int containerId;

    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";

    private String mInitParam1;
    private String mInitParam2;


    private boolean destroyed;

    public final boolean isDestroyed() {
        return destroyed;
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    private static final Handler handler = new Handler();

    public final Handler getHandler() {
        return handler;
    }

    public final void postRunnable(final Runnable runnable) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                // validate
                // TODO use getActivity ?
                if (!mBaseFragmentUI.getFragment().isAdded()) {
                    return;
                }

                // run
                runnable.run();
            }
        });
    }

    public final void postDelayed(final Runnable runnable, long delay) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // validate
                // TODO use getActivity ?
                if (!mBaseFragmentUI.getFragment().isAdded()) {
                    return;
                }

                // run
                runnable.run();
            }
        }, delay);
    }

    public void onCreate(Bundle savedInstanceState) {
        if (mBaseFragmentUI.getFragment().getArguments() != null) {
            mInitParam1 = mBaseFragmentUI.getFragment().getArguments().getString(ARG_PARAM1);
            mInitParam2 = mBaseFragmentUI.getFragment().getArguments().getString(ARG_PARAM2);
        }
    }

    public String getInitParam1() {
        return mInitParam1;
    }

    public String getInitParam2() {
        return mInitParam2;
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        LogUtil.ui("fragment: " + getClass().getSimpleName() + " onActivityCreated()");

        destroyed = false;
    }

    public void onAttach(Context context) {
        if (context instanceof OnFragmentInteractionListener) {
            mInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if (context instanceof BaseActivity) {
            mActivity = (BaseActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must extends BaseActivity");
        }
    }

    public void onDetach() {
        mInteractionListener = null;
    }

    public void onDestroy() {
        LogUtil.ui("fragment: " + getClass().getSimpleName() + " onDestroy()");
        destroyed = true;
    }

    public boolean isFirst = true;

    /**
     * 当Fragment选中时
     */
    public void setFragmentSeleted(boolean selected) {
        if (mInteractionListener == null) {
            return;
        }
        if (!selected) {
            mFragmentSelectListener.onFragmentUnSelected();
        } else if (isFirst) {
            mFragmentSelectListener.onFragmentSelected(isFirst);
            isFirst = false;
        } else {
            mFragmentSelectListener.onFragmentSelected(isFirst);
        }
    }


    /**
     * 在onActivityCreated(Bundle savedInstanceState)里使用
     *
     * @param resId
     * @param <T>
     * @return
     */
    public <T extends View> T findView(int resId) {
        return (T) (mBaseFragmentUI.getFragment().getView().findViewById(resId));
    }

    public void showKeyboard(boolean isShow) {
        KeyBoardUtils.showKeyboard(mActivity, isShow);
    }

    public void hideKeyboard(View view) {
        KeyBoardUtils.hideKeyboard(mActivity, view);
    }
}
