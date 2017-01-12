package com.afra55.baseclient.ui.fragment;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afra55.apimodule.bean.TransResultBean;
import com.afra55.apimodule.bean.TranslateBean;
import com.afra55.apimodule.helper.ToTransltateHelper;
import com.afra55.baseclient.R;
import com.afra55.commontutils.base.BaseFragment;
import com.afra55.commontutils.tip.ToastUtils;
import com.afra55.commontutils.ui.dialog.DialogMaker;

import java.util.List;

public class CommunityFragment extends BaseFragment {

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
        mTextInputEditText = findView(R.id.commnunity_translate_et);
        mTextInputLayout = findView(R.id.commnunity_translate_layout);
        findView(R.id.commnunity_translate_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTranslate(mTextInputEditText.getText().toString());
            }
        });
        mTextTranstaleResult = findView(R.id.commnunity_translate_result);
    }

    public void showProgressDialog() {
        DialogMaker.showProgressDialog(mActivity, "Loading");
    }

    public void showToast(String s) {
        ToastUtils.showToast(mActivity, s);
    }

    public void setTranslateResult(String result) {
        showKeyboard(false);
        DialogMaker.dismissProgressDialog();
        mTextInputLayout.setError(null);
        mTextTranstaleResult.setText(result);
    }

    public void setTranslateError(String s) {
        mTextInputLayout.setError(s);
        showKeyboard(false);
        DialogMaker.dismissProgressDialog();
    }

    public void toTranslate(String string) {
        ToTransltateHelper.getInstance().toTanstale(string, new ToTransltateHelper.ToTranslateResultListener() {
            @Override
            public void showProgressDialog() {
                showProgressDialog();
            }

            @Override
            public void onSuccess(TranslateBean translateBean) {
                List<TransResultBean> list = translateBean.getTrans_result();
                String result = "";
                for (TransResultBean resultBean : list) {
                    result += resultBean.getDst() + " ";
                }
                setTranslateResult(result);
            }

            @Override
            public void onFaile(String tip) {
                showToast(tip);
                setTranslateError(tip);
            }
        });
    }
}
