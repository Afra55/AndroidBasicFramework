package com.afra55.commontutils.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;

import com.afra55.commontutils.base.presenter.BaseFragmentPresenter;
import com.afra55.commontutils.base.ui.BaseFragmentUI;
import com.afra55.commontutils.device.KeyBoardUtils;
import com.afra55.commontutils.log.LogUtils;


public abstract class BaseFragment extends Fragment
        implements View.OnClickListener, BaseFragmentUI {

    private static final String TAG = LogUtils.makeLogTag(BaseFragment.class);

    private BaseFragmentPresenter mBaseFragmentPresenter;

    private boolean destroyed;

    protected BaseActivity mActivity;

    protected OnFragmentInteractionListener mInteractionListener;

    public static final String ARG_PARAM1 = "mInitParam1";
    public static final String ARG_PARAM2 = "mInitParam2";

    private String mInitParam1;
    private String mInitParam2;

    private static final Handler handler = new Handler();

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    private int containerId;

    @Override
    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public BaseFragment() {
        mBaseFragmentPresenter = new BaseFragmentPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mInitParam1 = getArguments().getString(ARG_PARAM1);
            mInitParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.ui("fragment: " + getClass().getSimpleName() + " onActivityCreated()");
        destroyed = false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtils.d(TAG, "Attaching to activity");
        if (activity instanceof BaseActivity) {

            mActivity = (BaseActivity) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mInteractionListener = null;
        mActivity = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.ui("fragment: " + getClass().getSimpleName() + " onDestroy()");
        destroyed = true;
    }

    @Override
    public final Handler getHandler() {
        return handler;
    }

    @Override
    public final void postRunnable(final Runnable runnable) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                // validate
                // TODO use getActivity ?
                if (!isAdded()) {
                    return;
                }

                // run
                runnable.run();
            }
        });
    }

    @Override
    public final void postDelayed(final Runnable runnable, long delay) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // validate
                // TODO use getActivity ?
                if (!isAdded()) {
                    return;
                }

                // run
                runnable.run();
            }
        }, delay);
    }

    public boolean isFirst = true;

    /**
     * 当Fragment选中时, 手动调用
     */
    @Override
    public void setFragmentSeleted(boolean selected) {
        if (!selected) {
            onFragmentUnSelected();
        } else if (isFirst) {
            onFragmentSelected(isFirst);
            isFirst = false;
        } else {
            onFragmentSelected(isFirst);
        }
    }

    protected abstract void onFragmentSelected(boolean isFirst);
    protected abstract void onFragmentUnSelected();

    /**
     * 在onActivityCreated(Bundle savedInstanceState)里使用
     *
     * @param resId
     * @param <T>
     * @return
     */
    @Override
    public <T extends View> T findView(int resId) {
        return (T) (getView().findViewById(resId));
    }

    @Override
    public void showKeyboard(boolean isShow) {
        KeyBoardUtils.showKeyboard(mActivity, isShow);
    }

    @Override
    public void hideKeyboard(View view) {
        KeyBoardUtils.hideKeyboard(mActivity, view);
    }

    @Override
    public String getInitParam1() {
        return mInitParam1;
    }

    @Override
    public String getInitParam2() {
        return mInitParam2;
    }

    @Override
    public BaseFragment getFragment() {
        return this;
    }
}
