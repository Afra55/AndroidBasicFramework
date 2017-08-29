package com.afra55.baseclient.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afra55.apimodule.domain.model.LoginBean;
import com.afra55.apimodule.presentation.presenters.LoginPresenter;
import com.afra55.apimodule.presentation.presenters.impl.LoginPresenterImpl;
import com.afra55.baseclient.R;
import com.afra55.commontutils.base.BaseFragment;
import com.afra55.commontutils.log.LogUtils;
import com.afra55.commontutils.threading.MainThreadImpl;
import com.afra55.commontutils.threading.ThreadExecutor;
import com.afra55.commontutils.tip.ToastUtils;

public class MeFragment extends BaseFragment implements LoginPresenter.View {

    private LoginPresenter loginPresenter;

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
        if (loginPresenter != null) {
            if (isFirst) {
                loginPresenter.toLogin(11110000);
            } else {
                loginPresenter.toLogin(222200000);
            }
        }
    }

    @Override
    protected void onFragmentUnSelected() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        loginPresenter = new LoginPresenterImpl(ThreadExecutor.getInstance()
                , MainThreadImpl.getInstance(), this);
    }

    @Override
    public void onLoginResultReturn(LoginBean loginBean) {
        if (loginBean != null) {
            String message = loginBean.toString();
            ToastUtils.showToast(getContext(), message);
            LogUtils.i("onLoginResultReturn", message);
        }
    }
}
