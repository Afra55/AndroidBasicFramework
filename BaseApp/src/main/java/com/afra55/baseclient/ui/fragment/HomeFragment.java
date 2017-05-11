package com.afra55.baseclient.ui.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.afra55.baseclient.R;
import com.afra55.baseclient.adapter.BannerAdapter;
import com.afra55.baseclient.common.BinnerHelper;
import com.afra55.commontutils.base.BaseFragment;
import com.afra55.commontutils.base.BasePresenter;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment {

    private ViewPager bannerVp; // 广告Binner
    private ArrayList<View> binnerViewArray; // 存储Binner view的容器
    private BannerAdapter bannerAdapter;

    public static HomeFragment newInstance(String param1, String param2) {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        setContainerId(R.id.main_fragment_content);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    protected void initView(View view) {
        bannerVp = findView(R.id.vp_banner);
    }

    @Override
    protected void initLogic() {
        initBanner((RadioGroup) findView(R.id.vp_indicator_rg));
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    /* 初始化Binner */
    private void initBanner(RadioGroup binnerIndicatorRg) {
        initBanner(getContext(), bannerVp, binnerIndicatorRg);
    }

    public void initBanner(Context context, ViewPager binnerVp, RadioGroup binnerIndicatorRg) {
        ArrayList<View> binnerViewArray = new ArrayList<>();
        String binnerPath = "http://ossweb-img.qq.com/images/lol/web201310/skin/big143000.jpg";
        ArrayList<View> startAndEndView = new ArrayList<>(); // 请务必存储 开始和最后的view
        for (int i = 0; i < 5; i++) {
            SimpleDraweeView draweeView = new SimpleDraweeView(context);
            draweeView.setImageURI(Uri.parse(binnerPath));
            draweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP);
            binnerViewArray.add(draweeView);
            if (i == 0) { // 开始的view
                SimpleDraweeView start = new SimpleDraweeView(context);
                start.setImageURI(Uri.parse(binnerPath));
                startAndEndView.add(start);
            } else if (i == 4) { // 结束的view
                SimpleDraweeView end = new SimpleDraweeView(context);
                end.setImageURI(Uri.parse(binnerPath));
                startAndEndView.add(end);
            }
        }

        BinnerHelper.initViewList(binnerViewArray, startAndEndView);
        BannerAdapter bannerAdapter = new BannerAdapter(binnerViewArray);
        binnerVp.setAdapter(bannerAdapter);
        BinnerHelper.getInstance().start(context, binnerVp, binnerViewArray, binnerIndicatorRg);
    }

    @Override
    public void onFragmentSelected(boolean isFirst) {

    }

    @Override
    public void onFragmentUnSelected() {

    }

}
