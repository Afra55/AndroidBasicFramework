package com.afra55.commontutils.base.presenter;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.afra55.commontutils.R;
import com.afra55.commontutils.base.BaseFragment;
import com.afra55.commontutils.base.ui.BaseActivityUI;
import com.afra55.commontutils.device.KeyBoardUtils;
import com.afra55.commontutils.fragment.FragmentUtils;
import com.afra55.commontutils.fresco.ImageLoadUtils;
import com.afra55.commontutils.sys.ReflectionUtil;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Yang on 2016/6/16.
 * BaseActivityPresenter
 */
public class BaseActivityPresenter {

    private BaseActivityUI mBaseUI;

    public BaseActivityPresenter(BaseActivityUI baseUI) {
        mBaseUI = baseUI;
    }

    /**
     * 初始化某些事情
     */
    public void initSomeThing() {
        Fresco.initialize(mBaseUI.getContext(), ImageLoadUtils.CusstomConfig(mBaseUI.getContext()));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void invokeFragmentManagerNoteStateNotSaved(FragmentManager fragmentManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ReflectionUtil.invokeMethod(fragmentManager, "noteStateNotSaved", null);
        }
    }

    public BaseFragment addFragment(FragmentManager fragmentManager, BaseFragment fragment) {
        return FragmentUtils.addFragment(fragmentManager, fragment);
    }

    public List<BaseFragment> addFragments(FragmentManager fragmentManager, List<BaseFragment> fragments) {
        return FragmentUtils.addFragments(fragmentManager, fragments);
    }

    /**
     * fragment 只使用一次就被替换掉，使用 replace
     * @param fragment
     * @return
     */
    public BaseFragment replaceFragmentContent(FragmentManager fragmentManager, BaseFragment fragment) {
        return FragmentUtils.replaceFragmentContent(fragmentManager, fragment, false);
    }

    public BaseFragment replaceFragmentContent(FragmentManager fragmentManager, BaseFragment fragment
            , boolean needAddToBackStack) {
        return FragmentUtils.replaceFragmentContent(fragmentManager, fragment, needAddToBackStack);
    }

    /**
     * 如果使用 fragment 切换动画或常驻界面的话，最好使用 hide 和 show。
     * @param from
     * @param to
     */
    public void switchFragment(FragmentManager fragmentManager, BaseFragment from, BaseFragment to) {
        FragmentUtils.switchFragment(fragmentManager, from, to);
    }

}
