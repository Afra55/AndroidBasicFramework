package com.afra55.baseclient.ui.fragment;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afra55.apimodule.domain.model.TransResultBean;
import com.afra55.apimodule.domain.model.TranslateBean;
import com.afra55.apimodule.presentation.presenters.CommunityPresenter;
import com.afra55.apimodule.presentation.presenters.impl.CommunityPresenterImpl;
import com.afra55.apimodule.threading.MainThreadImpl;
import com.afra55.apimodule.threading.ThreadExecutor;
import com.afra55.baseclient.R;
import com.afra55.commontutils.base.BaseFragment;
import com.afra55.commontutils.base.BasePresenter;
import com.afra55.commontutils.ui.dialog.DialogMaker;

import java.util.List;

public class CommunityFragment extends BaseFragment implements CommunityPresenter.View{

    private TextInputEditText mTextInputEditText;

    private TextInputLayout mTextInputLayout;

    private TextView mTextTranslateResult;

    private CommunityPresenter mPresenter;

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
    protected void initView(View view) {
        mTextInputEditText = findView(R.id.commnunity_translate_et);
        mTextInputLayout = findView(R.id.commnunity_translate_layout);
        mTextTranslateResult = findView(R.id.commnunity_translate_result);
    }

    @Override
    protected void initLogic() {

        findView(R.id.commnunity_translate_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTranslate(mTextInputEditText.getText().toString());
            }
        });
    }

    @Override
    protected BasePresenter createPresenter() {
        mPresenter = new CommunityPresenterImpl(
                ThreadExecutor.getInstance()
                , MainThreadImpl.getInstance()
                ,this);
        return mPresenter;
    }


    public void setTranslateResult(String result) {
        showKeyboard(false);
        DialogMaker.dismissProgressDialog();
        mTextInputLayout.setError(null);
        mTextTranslateResult.setText(result);
    }

    public void setTranslateError(String s) {
        mTextInputLayout.setError(s);
        showKeyboard(false);
        DialogMaker.dismissProgressDialog();
    }

    public void toTranslate(String string) {

        mPresenter.translateText(string);

    }

    @Override
    public void showError(String message) {
        super.showError(message);
        setTranslateError(message);
    }

    @Override
    public void onTranslateResultReturn(TranslateBean translateBean) {
        if (translateBean == null) {
            return;
        }
        List<TransResultBean> list = translateBean.getTrans_result();
        String result = "";
        for (TransResultBean resultBean : list) {
            result += resultBean.getDst() + " ";
        }
        setTranslateResult(result);
    }

}
