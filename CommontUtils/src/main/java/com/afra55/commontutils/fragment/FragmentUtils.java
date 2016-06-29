package com.afra55.commontutils.fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.afra55.commontutils.R;
import com.afra55.commontutils.base.BaseActivity;
import com.afra55.commontutils.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Yang on 2016/6/29.
 */
public class FragmentUtils {
    public static BaseFragment addFragment(FragmentManager fragmentManager
            , BaseFragment fragment) {
        List<BaseFragment> fragments = new ArrayList<>(1);
        fragments.add(fragment);

        return addFragments(fragmentManager, fragments).get(0);
    }

    public static List<BaseFragment> addFragments(FragmentManager fragmentManager, List<BaseFragment> fragments) {
        List<BaseFragment> fragmentList = new ArrayList<>(fragments.size());

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        boolean commit = false;
        for (int i = 0; i < fragmentList.size(); i++) {
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
     *
     * @param fragment
     * @return
     */
    public static BaseFragment replaceFragmentContent(FragmentManager fragmentManager, BaseFragment fragment) {
        return replaceFragmentContent(fragmentManager, fragment, false);
    }

    public static BaseFragment replaceFragmentContent(FragmentManager fragmentManager, BaseFragment fragment, boolean needAddToBackStack) {
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
     *
     * @param from
     * @param to
     */
    public static void switchFragment(FragmentManager fragmentManager
                                , BaseFragment from
                                , BaseFragment to) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
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
}
