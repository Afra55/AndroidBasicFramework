package com.afra55.commontutils.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.afra55.commontutils.base.presenter.BaseActivityPresenter;
import com.afra55.commontutils.base.ui.BaseActivityUI;
import com.afra55.commontutils.device.KeyBoardUtils;
import com.afra55.commontutils.log.LogUtil;
import com.afra55.commontutils.tip.ToastUtils;

import java.util.List;

/**
 * View 对应于Activity，负责View的绘制以及与用户交互
 * Model 业务逻辑和实体模型
 * Presenter 负责完成View于Model间的交互
 */
public class BaseActivity extends AppCompatActivity implements BaseActivityUI {

    private BaseActivityPresenter mBaseActivityPresenter;

    private boolean destroyed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LayoutInflaterCompat.setFactory(LayoutInflater.from(getContext()), new LayoutInflaterFactory() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {


                AppCompatDelegate delegate = getDelegate();
                View view = delegate.createView(parent, name, context, attrs);
                // 全局搜索 View 替换 View
                /*if (view != null && view instanceof TextView) {
                    (TextView)view;
                }*/
                return view;
            }
        });

        super.onCreate(savedInstanceState);

        mBaseActivityPresenter = new BaseActivityPresenter(this);
        mBaseActivityPresenter.initSomeThing();

        LogUtil.ui("activity: " + getClass().getSimpleName() + " onCreate()");
    }

    @Override
    protected void onDestroy() {
        LogUtil.ui("activity: " + getClass().getSimpleName() + " onDestroy()");
        destroyed = true;
        super.onDestroy();
    }

    @Override
    public final Handler getHandler() {
        return mBaseActivityPresenter.getHandler();
    }

    /**
     * 判断页面是否已经被销毁（异步回调时使用）
     */
    @Override
    public boolean isDestroyedCompatible() {
        if (Build.VERSION.SDK_INT >= 17) {
            return isDestroyedCompatible17();
        } else {
            return destroyed || isFinishing();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public boolean isDestroyedCompatible17() {
        return isDestroyed();
    }

    @Override
    public void onBackPressed() {
        mBaseActivityPresenter.invokeFragmentManagerNoteStateNotSaved(getSupportFragmentManager());
    }

    @Override
    public void showToast(String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public void showKeyboard(boolean isShow) {
        KeyBoardUtils.showKeyboard(getActivity(), isShow);
    }

    /**
     * 延时弹出键盘
     *
     * @param focus ：键盘的焦点项
     */
    @Override
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
        return (T) (findViewById(resId));
    }

    // fragment 相关
    @Override
    public BaseFragment addFragment(BaseFragment fragment) {

        return mBaseActivityPresenter.addFragment(getSupportFragmentManager(), fragment);
    }

    @Override
    public List<BaseFragment> addFragments(List<BaseFragment> fragments) {
        return mBaseActivityPresenter.addFragments(getSupportFragmentManager(), fragments);
    }

    /**
     * fragment 只使用一次就被替换掉，使用 replace
     *
     * @param fragment
     * @return
     */
    @Override
    public BaseFragment replaceFragmentContent(BaseFragment fragment) {
        return mBaseActivityPresenter.replaceFragmentContent(getSupportFragmentManager(), fragment);
    }

    @Override
    public BaseFragment replaceFragmentContent(BaseFragment fragment, boolean needAddToBackStack) {
        return mBaseActivityPresenter.replaceFragmentContent(getSupportFragmentManager(), fragment, needAddToBackStack);
    }

    /**
     * 如果使用 fragment 切换动画或常驻界面的话，最好使用 hide 和 show。
     *
     * @param from
     * @param to
     */
    @Override
    public void switchFragment(BaseFragment from, BaseFragment to) {
        mBaseActivityPresenter.switchFragment(getSupportFragmentManager(), from, to);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public BaseActivity getActivity() {
        return this;
    }

}
