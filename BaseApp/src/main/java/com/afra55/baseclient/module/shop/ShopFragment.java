package com.afra55.baseclient.module.shop;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afra55.baseclient.R;
import com.afra55.commontutils.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends BaseFragment {

    public static ShopFragment newInstance(String param1, String param2) {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        ShopFragment fragment = new ShopFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ShopFragment() {
        setContainerId(R.id.main_fragment_content);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop, container, false);
    }

    @Override
    protected void onFragmentSeleted(boolean isFirst) {

    }

    @Override
    protected void onFragmentUnSeleted() {

    }

    @Override
    public void onClick(View v) {

    }
}
