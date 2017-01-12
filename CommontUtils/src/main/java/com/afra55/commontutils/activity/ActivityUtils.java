/*
 * Copyright (c) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.afra55.commontutils.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;

import com.afra55.commontutils.R;
import com.afra55.commontutils.base.BaseFragment;
import com.afra55.commontutils.log.LogUtils;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Utility methods for activities management.
 */
public class ActivityUtils {

    private static final String TAG = LogUtils.makeLogTag(ActivityUtils.class);

    /**
     * Enables back navigation for activities that are launched from the NavBar. See {@code
     * AndroidManifest.xml} to find out the parent activity names for each activity.
     *
     * {@code android:parentActivityName=".ui.activity.MainActivity"}
     */
    public static void createBackStack(Activity activity, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            TaskStackBuilder builder = TaskStackBuilder.create(activity);
            builder.addNextIntentWithParentStack(intent);
            builder.startActivities();
        } else {
            activity.startActivity(intent);
            activity.finish();
        }
    }


    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     *
     */
    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        Fragment addedFragment = fragmentManager.findFragmentById(frameId);
        if (addedFragment == null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(frameId, fragment);
            transaction.commit();
        }
    }

    /**
     * fragment 只使用一次就被替换掉，使用 replace
     *
     * @param fragmentManager FragmentManager
     * @param fragment BaseFragment
     * @return BaseFragment
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
            LogUtils.e(TAG, "replaceFragmentContent", e);
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
