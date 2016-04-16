package com.afra55.baseclient.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by yangshuai in the 13:37 of 2016.01.06 .
 */
public class BinnerAdapter extends PagerAdapter {


    private final ArrayList<View> contentArray;

    public BinnerAdapter(ArrayList<View> arr) {
        this.contentArray = arr;
    }

    @Override
    public int getCount() {
        return contentArray.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(contentArray.get(position));
        return contentArray.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        viewPager.removeView((View)object);
    }
}
