package com.afra55.baseclient.module.community;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afra55.baseclient.R;
import com.afra55.baseclient.module.community.presenter.CommunityFragmentPresenter;
import com.afra55.baseclient.module.community.ui.CommunityFragmentUI;
import com.afra55.commontutils.base.BaseFragment;

public class CommunityFragment extends BaseFragment implements CommunityFragmentUI{

    private CommunityFragmentPresenter mCommunityFragmentPresenter;

    private TextInputEditText mTextInputEditText;

    private TextInputLayout mTextInputLayout;

    public static CommunityFragment newInstance(String param1, String param2) {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        CommunityFragment fragment = new CommunityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CommunityFragment() {
        setContainerId(R.id.main_fragment_content);
    }

    @Override
    protected void onFragmentSelected(boolean isFirst) {

    }

    @Override
    protected void onFragmentUnSelected() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_community, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCommunityFragmentPresenter = new CommunityFragmentPresenter(this);
        mTextInputEditText = findView(R.id.commnunity_translate_et);
        mTextInputLayout = findView(R.id.commnunity_translate_layout);
        findView(R.id.commnunity_translate_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commnunity_translate_btn:

                break;
        }
    }
}
