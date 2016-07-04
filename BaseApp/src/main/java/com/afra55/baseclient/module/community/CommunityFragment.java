package com.afra55.baseclient.module.community;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afra55.baseclient.R;
import com.afra55.baseclient.module.community.presenter.CommunityFragmentPresenter;
import com.afra55.baseclient.module.community.ui.CommunityFragmentUI;
import com.afra55.commontutils.base.BaseFragment;
import com.afra55.commontutils.tip.ToastUtils;

public class CommunityFragment extends BaseFragment implements CommunityFragmentUI{

    private CommunityFragmentPresenter mCommunityFragmentPresenter;

    private TextInputEditText mTextInputEditText;

    private TextInputLayout mTextInputLayout;

    private TextView mTextTranstaleResult;

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
        initView();
    }

    private void initView() {
        mCommunityFragmentPresenter = new CommunityFragmentPresenter(this);
        mTextInputEditText = findView(R.id.commnunity_translate_et);
        mTextInputLayout = findView(R.id.commnunity_translate_layout);
        findView(R.id.commnunity_translate_btn).setOnClickListener(this);
        mTextTranstaleResult = findView(R.id.commnunity_translate_result);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commnunity_translate_btn:
                mCommunityFragmentPresenter.toTranslate(mTextInputEditText.getText().toString());
                break;
        }
    }

    @Override
    public void showToast(String s) {
        ToastUtils.showToast(mActivity, s);
    }

    @Override
    public void setTranslateResult(String result) {
        mTextInputLayout.setError(null);
        mTextTranstaleResult.setText(result);
    }

    @Override
    public void setTranslateError(String s) {
        mTextInputLayout.setError(s);
    }
}
