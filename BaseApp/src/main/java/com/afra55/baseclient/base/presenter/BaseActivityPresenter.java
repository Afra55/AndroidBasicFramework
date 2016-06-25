package com.afra55.baseclient.base.presenter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.afra55.baseclient.R;
import com.afra55.baseclient.base.BaseFragment;
import com.afra55.baseclient.base.ui.BaseActivityUI;
import com.afra55.baseclient.util.ImageLoadUtils;
import com.afra55.commontutils.device.KeyBoardUtils;
import com.afra55.commontutils.log.LogUtil;
import com.afra55.commontutils.sys.ReflectionUtil;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Yang on 2016/6/16.
 * BaseActivityPresenter
 */
public class BaseActivityPresenter implements View.OnClickListener{

    private static Handler handler;

    private boolean destroyed = false;

    private BaseActivityUI mBaseUI;

    public void onCreate() {
        LayoutInflaterCompat.setFactory(LayoutInflater.from(mBaseUI.getContext()), new LayoutInflaterFactory() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {


                AppCompatDelegate delegate = mBaseUI.getActivity().getDelegate();
                View view = delegate.createView(parent, name, context, attrs);
                // 全局搜索 View 替换 View
//                if (view != null && view instanceof TextView) {
//                    (TextView)view;
//                }
                return view;
            }
        });
    }

    /**
     * 初始化某些事情
     */
    public void initSomeThing() {
        Fresco.initialize(mBaseUI.getContext(), ImageLoadUtils.CusstomConfig(mBaseUI.getContext()));
        LogUtil.ui("activity: " + getClass().getSimpleName() + " onCreate()");
    }

    public BaseActivityPresenter(BaseActivityUI baseUI) {
        mBaseUI = baseUI;
    }

    public void onDestroy() {
        LogUtil.ui("activity: " + getClass().getSimpleName() + " onDestroy()");
        destroyed = true;
    }

    public final Handler getHandler() {
        if (handler == null) {
            handler = new Handler(mBaseUI.getContext().getMainLooper());
        }
        return handler;
    }

    /**
     * 判断页面是否已经被销毁（异步回调时使用）
     */
    public boolean isDestroyedCompatible() {
        if (Build.VERSION.SDK_INT >= 17) {
            return isDestroyedCompatible17();
        } else {
            return destroyed || mBaseUI.getActivity().isFinishing();
        }
    }

    @TargetApi(17)
    public boolean isDestroyedCompatible17() {
        return mBaseUI.getActivity().isDestroyed();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void invokeFragmentManagerNoteStateNotSaved() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ReflectionUtil.invokeMethod(mBaseUI.getActivity().getFragmentManager(), "noteStateNotSaved", null);
        }
    }

    public void onBackPressed() {
        invokeFragmentManagerNoteStateNotSaved();
        mBaseUI.getActivity().onBackPressed();
    }

    //Toast公共方法
    private Toast toast = null;

    public void showToast(String message) {
        if (toast == null) {
            toast = Toast.makeText(mBaseUI.getContext(), message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public void showKeyboard(boolean isShow) {
        KeyBoardUtils.showKeyboard(mBaseUI.getActivity(), isShow);
    }

    public void showKeyboardDelayed(View focus) {
        final View viewToFocus = focus;
        if (focus != null) {
            focus.requestFocus();
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (viewToFocus == null || viewToFocus.isFocused()) {
                    showKeyboard(true);
                }
            }
        }, 200);
    }

    public <T extends View> T findView(int resId) {
        return (T) (mBaseUI.getActivity().findViewById(resId));
    }

    public BaseFragment addFragment(BaseFragment fragment) {
        List<BaseFragment> fragments = new ArrayList<>(1);
        fragments.add(fragment);

        return addFragments(fragments).get(0);
    }

    public List<BaseFragment> addFragments(List<BaseFragment> fragments) {
        List<BaseFragment> fragmentList = new ArrayList<>(fragments.size());

        FragmentManager fragmentManager = mBaseUI.getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        boolean commit = false;
        for (int i = 0; i< fragmentList.size(); i++) {
            BaseFragment fragment = fragments.get(i);
            int id = fragment.getContainerId();

            BaseFragment fragmentTemp = (BaseFragment) fragmentManager.findFragmentById(id);
            if (fragmentTemp == null) {
                fragmentTemp = fragment;
                fragmentTransaction.add(id, fragment);
                commit = true;
            }

            fragmentList.add(i, fragmentTemp);
        }

        if (commit) {
            try {
                fragmentTransaction.commitAllowingStateLoss();
            } catch (Exception e) {
            }
        }
        return fragmentList;
    }

    /**
     * fragment 只使用一次就被替换掉，使用 replace
     * @param fragment
     * @return
     */
    public BaseFragment replaceFragmentContent(BaseFragment fragment) {
        return replaceFragmentContent(fragment, false);
    }

    public BaseFragment replaceFragmentContent(BaseFragment fragment, boolean needAddToBackStack) {
        FragmentManager fragmentManager = mBaseUI.getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(fragment.getContainerId(), fragment);
        if (needAddToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        try {
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
        }
        return fragment;
    }

    /**
     * 如果使用 fragment 切换动画或常驻界面的话，最好使用 hide 和 show。
     * @param from
     * @param to
     */
    public void switchFragment(BaseFragment from,
                                  BaseFragment to) {
        FragmentManager manager = mBaseUI.getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
        if (from == null || !from.isAdded()) {
            if (!to.isAdded()) {
                transaction.add(to.getContainerId(), to, to.getClass().getSimpleName()).commit();
            } else {
                transaction.show(to).commit();
            }
        } else {
            if (!to.isAdded()) {
                from.setFragmentSeleted(false);
                transaction.hide(from).add(to.getContainerId(), to, to.getClass().getSimpleName()).commit();
            } else {
                from.setFragmentSeleted(false);
                transaction.hide(from).show(to).commit();
                to.setFragmentSeleted(true);
            }
        }
    }

    public boolean isCompatible(int sdk_int) {
        return android.os.Build.VERSION.SDK_INT >= sdk_int;
    }

    @Override
    public void onClick(View v) {

    }
}
