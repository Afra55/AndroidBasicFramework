package com.afra55.apimodule.helper;

import android.text.TextUtils;

import com.afra55.apimodule.api.APIField;
import com.afra55.apimodule.api.APIServices;
import com.afra55.apimodule.bean.TranslateBean;
import com.afra55.commontutils.AppCache;
import com.afra55.commontutils.http.BaseDisposableObserver;
import com.afra55.commontutils.http.IActionListener;
import com.afra55.commontutils.http.RequestBody;
import com.afra55.commontutils.http.RequestQuery;
import com.afra55.commontutils.http.RxPresenter;
import com.afra55.commontutils.string.MD5;
import com.afra55.commontutils.tip.ToastUtils;

import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Victor Yang on 2016/7/4.
 * ToTranslatePresenter
 */
public class ToTranslatePresenter extends RxPresenter {

    private static final String TAG = ToTranslatePresenter.class.getSimpleName();
    
    private APIServices apiServices;

    private ToTranslatePresenter(IActionListener.ViewAction viewAction) {
        super(viewAction);
        apiServices = RetrofitHelper.createService(APIField.OtherHttp.TRANSLATE_HOST, APIServices.class);

    }

    public static ToTranslatePresenter getInstance(IActionListener.ViewAction viewAction) {
        return new ToTranslatePresenter(viewAction);
    }

    public void toTranslate(String string) {

        if (TextUtils.isEmpty(string.trim())) {
                ToastUtils.showToast(AppCache.getContext(), "请输入文字！！");
            return;
        }

        int salt = new Random(100).nextInt();
        apiServices.toTranslate(
                RequestQuery.getBuildInstance()
                .withParam("q", string)
                .withParam("from", "auto")
                .withParam("to", "auto")
                .withParam("appid", APIField.OtherHttp.APPID)
                .withParam("salt", String.valueOf(salt))
                .withParam("sign", MD5.getStringMD5(APIField.OtherHttp.APPID + string + salt + APIField.OtherHttp.SECRET))
                .build()
        , RequestBody.getBuilderInstance().build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(addSubscriber(new BaseDisposableObserver<TranslateBean>() {
                    @Override
                    public void onSuccess(TranslateBean o) {

                        if (viewAction != null) {
                            viewAction.showInfoView(0, o);
                        }
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMsg) {
                        if (viewAction != null) {
                            viewAction.showInfoView(0, null);
                        }
                    }
                }));
        
    }
}
