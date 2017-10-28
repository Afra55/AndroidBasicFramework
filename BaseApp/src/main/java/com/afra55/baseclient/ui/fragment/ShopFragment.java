package com.afra55.baseclient.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afra55.baseclient.R;
import com.afra55.baseclient.common.AppFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends AppFragment {


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
    protected void onFragmentSelected(boolean isFirst) {

    }

    @Override
    protected void onFragmentUnSelected() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setLayoutView(inflater, container, R.layout.fragment_shop);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initLogic() {

    }

}
