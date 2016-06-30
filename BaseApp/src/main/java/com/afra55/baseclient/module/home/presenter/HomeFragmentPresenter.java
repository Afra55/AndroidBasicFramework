package com.afra55.baseclient.module.home.presenter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.afra55.baseclient.adapter.BinnerAdapter;
import com.afra55.baseclient.module.home.ui.HomeFragmentUI;
import com.afra55.baseclient.util.BinnerHelper;
import com.afra55.commontutils.fresco.ImageLoadUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by Victor Yang on 2016/6/30.
 */
public class HomeFragmentPresenter {

    private HomeFragmentUI mHomeFragmentUI;

    public HomeFragmentPresenter(HomeFragmentUI homeFragmentUI) {
        mHomeFragmentUI = homeFragmentUI;
    }

    public void initBinner(Context context, ViewPager binnerVp, RadioGroup binnerIndicatorRg) {
        ArrayList<View> binnerViewArray = new ArrayList<>();
        String binnerPath = "http://ossweb-img.qq.com/images/lol/web201310/skin/big143000.jpg";
        ArrayList<View> startAndEndView = new ArrayList<>(); // 请务必存储 开始和最后的view
        for (int i = 0; i < 5; i++) {
            SimpleDraweeView draweeView = new SimpleDraweeView(context);
            ImageLoadUtils.getInstance(context).display(binnerPath, draweeView);
            binnerViewArray.add(draweeView);
            if (i == 0) { // 开始的view
                SimpleDraweeView start = new SimpleDraweeView(context);
                ImageLoadUtils.getInstance(context).display(binnerPath, start);
                startAndEndView.add(start);
            } else if (i == 4) { // 结束的view
                SimpleDraweeView end = new SimpleDraweeView(context);
                ImageLoadUtils.getInstance(context).display(binnerPath, end);
                startAndEndView.add(end);
            }
        }

        BinnerHelper.initViewList(binnerViewArray, startAndEndView);
        BinnerAdapter binnerAdapter = new BinnerAdapter(binnerViewArray);
        binnerVp.setAdapter(binnerAdapter);
        BinnerHelper.getInstance().start(context, binnerVp, binnerViewArray, binnerIndicatorRg);
    }

}
