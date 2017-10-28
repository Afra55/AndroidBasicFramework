package com.afra55.baseclient.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afra55.baseclient.R;
import com.afra55.baseclient.common.AppFragment;

public class MeFragment extends AppFragment {


    public static MeFragment newInstance(String param1, String param2) {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MeFragment() {
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
        setLayoutView(inflater, container, R.layout.fragment_me);
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
