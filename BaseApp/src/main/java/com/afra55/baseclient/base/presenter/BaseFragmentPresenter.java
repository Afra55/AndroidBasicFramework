package com.afra55.baseclient.base.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.afra55.baseclient.base.BaseActivity;
import com.afra55.baseclient.base.ui.BaseFragmentUI;
import com.afra55.commontutils.device.KeyBoardUtils;
import com.afra55.commontutils.log.LogUtil;

/**
 * Created by Victor Yang on 2016/6/28.
 */
public class BaseFragmentPresenter {

    private BaseFragmentUI mBaseFragmentUI;

    private BaseActivity mActivity;

    public BaseFragmentPresenter(BaseFragmentUI baseFragmentUI) {
        mBaseFragmentUI = baseFragmentUI;
    }

    private int containerId;

    protected static final String ARG_PARAM1 = "param1";
    protected static final String ARG_PARAM2 = "param2";

    private String mInitParam1;
    private String mInitParam2;

    private OnFragmentInteractionListener mListener;

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void onFragmentInteraction(String message);

        /**
         * 当Fragment切换为选中时
         */
        void onFragmentSeleted(boolean isFirst);

        /**
         * 当Fragment切换为未选中时
         */
        void onFragmentUnSeleted();
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
            mListener = (OnFragmentInteractionListener) context;
            mActivity = (BaseActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        mListener = null;
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
        if (mListener == null) {
            return;
        }
        if (!selected) {
            mListener.onFragmentUnSeleted();
        } else if (isFirst) {
            mListener.onFragmentSeleted(true);
            isFirst = false;
        } else {
            mListener.onFragmentSeleted(false);
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
