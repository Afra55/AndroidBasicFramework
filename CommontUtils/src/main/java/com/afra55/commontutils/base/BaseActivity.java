package com.afra55.commontutils.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.afra55.commontutils.R;
import com.afra55.commontutils.device.KeyBoardUtils;
import com.afra55.commontutils.http.RxPresenter;
import com.afra55.commontutils.log.LogUtils;
import com.afra55.commontutils.sys.ReflectionUtil;
import com.afra55.commontutils.tip.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * View 对应于Activity，负责View的绘制以及与用户交互
 * Model 业务逻辑和实体模型
 * Presenter 负责完成View于Model间的交互
 */
public abstract class BaseActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private static Handler handler;

    private boolean destroyed = false;

    private List<RxPresenter> rxPresenterList = new ArrayList<>();
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LayoutInflaterCompat.setFactory(LayoutInflater.from(this), new LayoutInflaterFactory() {
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

        LogUtils.ui("activity: " + getClass().getSimpleName() + " onCreate()");

        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // 在这里配置 action bar  和 侧边栏

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initSupportActionBar(getScreenTitle());
    }

    /**
     * 初始化 actionBar，需要满足一个条件, 在布局中 include 默认的 actionBar 或者 自定义 actionBar：
     * {@code <include layout="@layout/default_appbar_layout" />}
     *
     * @param title getScreenTitle()
     */
    private void initSupportActionBar(String title) {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null) {
            return;
        }
        toolbar.setTitle(title);

        if (displayHomeAsUpEnabled()) {
            toolbar.setNavigationIcon(R.drawable.ic_back_black);
            ImageButton mNavButtonView = (ImageButton) ReflectionUtil.getFieldValue(toolbar, "mNavButtonView");
            Toolbar.LayoutParams navigationLayoutParams = (Toolbar.LayoutParams) mNavButtonView.getLayoutParams();
            navigationLayoutParams.width = getResources().getDimensionPixelSize(R.dimen.actionbar_navigation_size);
            navigationLayoutParams.height = getResources().getDimensionPixelSize(R.dimen.actionbar_navigation_size);
            navigationLayoutParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.actionbar_navigation_margin_left);
            mNavButtonView.setLayoutParams(navigationLayoutParams);
            mNavButtonView.setAdjustViewBounds(true);
        }

        setSupportActionBar(toolbar);

        View.OnClickListener navigationOnClickListener = navigationOnClickListener();
        if (navigationOnClickListener != null) {
            toolbar.setNavigationOnClickListener(navigationOnClickListener);
        }
    }

    protected View.OnClickListener navigationOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        };
    }

    protected abstract String getScreenTitle();

    protected boolean displayHomeAsUpEnabled() {
        return true;
    }

    /**
     * 需要注册的时候再注册
     */
    protected void setEventBusAction() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onStop();
    }

    protected void addPresenter(RxPresenter rxPresenter) {
        if (!rxPresenterList.contains(rxPresenter)) {
            rxPresenterList.add(rxPresenter);
        }
    }

    @Override
    protected void onDestroy() {
        LogUtils.ui("activity: " + getClass().getSimpleName() + " onDestroy()");
        destroyed = true;
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (!rxPresenterList.isEmpty()) {
            for (RxPresenter rxPresenter : rxPresenterList) {
                rxPresenter.removeView();
            }
        }
        super.onDestroy();
    }

    public final Handler getHandler() {
        if (handler == null) {
            handler = new Handler(getMainLooper());
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
            return destroyed || isFinishing();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public boolean isDestroyedCompatible17() {
        return isDestroyed();
    }

    @Override
    public void onBackPressed() {
        invokeFragmentManagerNoteStateNotSaved(getSupportFragmentManager());
        super.onBackPressed();
    }

    public void showToast(String message) {
        ToastUtils.showToast(this, message);
    }

    public <T> T showToast(T t) {
        return ToastUtils.showToast(this, t);
    }

    public void showKeyboard(boolean isShow) {
        KeyBoardUtils.showKeyboard(this, isShow);
    }

    /**
     * 延时弹出键盘
     *
     * @param focus ：键盘的焦点项
     */
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void invokeFragmentManagerNoteStateNotSaved(FragmentManager fragmentManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ReflectionUtil.invokeMethod(fragmentManager, "noteStateNotSaved", null);
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * This utility method handles Up navigation intents by searching for a parent activity and
     * navigating there if defined. When using this for an activity make sure to define both the
     * native parentActivity as well as the AppCompat one when supporting API levels less than 16.
     * when the activity has a single parent activity. If the activity doesn't have a single parent
     * activity then don't define one and this method will use back button functionality. If "Up"
     * functionality is still desired for activities without parents then use {@code
     * syntheticParentActivity} to define one dynamically.
     * <p/>
     * Note: Up navigation intents are represented by a back arrow in the top left of the Toolbar in
     * Material Design guidelines.
     *
     * @param currentActivity         Activity in use when navigate Up action occurred.
     * @param syntheticParentActivity Parent activity to use when one is not already configured.
     */
    public static void navigateUpOrBack(Activity currentActivity,
                                        Class<? extends Activity> syntheticParentActivity) {
        // Retrieve parent activity from AndroidManifest.
        Intent intent = NavUtils.getParentActivityIntent(currentActivity);

        // Synthesize the parent activity when a natural one doesn't exist.
        if (intent == null && syntheticParentActivity != null) {
            try {
                intent = NavUtils.getParentActivityIntent(currentActivity, syntheticParentActivity);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (intent == null) {
            // No parent defined in manifest. This indicates the activity may be used by
            // in multiple flows throughout the app and doesn't have a strict parent. In
            // this case the navigation up button should act in the same manner as the
            // back button. This will result in users being forwarded back to other
            // applications if currentActivity was invoked from another application.
            currentActivity.onBackPressed();
        } else {
            if (NavUtils.shouldUpRecreateTask(currentActivity, intent)) {
                // Need to synthesize a backstack since currentActivity was probably invoked by a
                // different app. The preserves the "Up" functionality within the app according to
                // the activity hierarchy defined in AndroidManifest.xml via parentActivity
                // attributes.
                TaskStackBuilder builder = TaskStackBuilder.create(currentActivity);
                builder.addNextIntentWithParentStack(intent);
                builder.startActivities();
            } else {
                // Navigate normally to the manifest defined "Up" activity.
                NavUtils.navigateUpTo(currentActivity, intent);
            }
        }
    }

    /**
     * Configure this Activity as a floating window, with the given {@code width}, {@code height}
     * and {@code alpha}, and dimming the background with the given {@code dim} value.
     */
    protected void setupFloatingWindow(int width, int height, int alpha, float dim) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = getResources().getDimensionPixelSize(width);
        params.height = getResources().getDimensionPixelSize(height);
        params.alpha = alpha;
        params.dimAmount = dim;
        params.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getWindow().setAttributes(params);
    }


}
