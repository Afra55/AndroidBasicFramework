package com.afra55.baseclient.module.community.presenter;

import com.afra55.apimodule.bean.TransResultBean;
import com.afra55.apimodule.bean.TranslateBean;
import com.afra55.apimodule.helper.ToTransltateHelper;
import com.afra55.baseclient.module.community.ui.CommunityFragmentUI;

import java.util.List;

/**
 * Created by Victor Yang on 2016/6/30.
 */
public class CommunityFragmentPresenter {

    private CommunityFragmentUI mCommunityFragmentUI;

    public CommunityFragmentPresenter(CommunityFragmentUI communityFragmentUI) {
        mCommunityFragmentUI = communityFragmentUI;
    }

    public void toTranslate(String string) {
        ToTransltateHelper.getInstance().toTanstale(string, new ToTransltateHelper.ToTranslateResultListener() {
            @Override
            public void showProgressDialog() {
                mCommunityFragmentUI.showProgressDialog();
            }

            @Override
            public void onSuccess(TranslateBean translateBean) {
                List<TransResultBean> list = translateBean.getTrans_result();
                String result = "";
                for (TransResultBean resultBean : list) {
                    result += resultBean.getDst() + " ";
                }
                mCommunityFragmentUI.setTranslateResult(result);
            }

            @Override
            public void onFaile(String tip) {
                mCommunityFragmentUI.showToast(tip);
                mCommunityFragmentUI.setTranslateError(tip);
            }
        });
    }

}
