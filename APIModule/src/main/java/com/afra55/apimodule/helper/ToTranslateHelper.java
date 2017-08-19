package com.afra55.apimodule.helper;

import android.text.TextUtils;

import com.afra55.apimodule.api.APIField;
import com.afra55.apimodule.api.APIServices;
import com.afra55.apimodule.bean.TranslateBean;
import com.afra55.apimodule.util.DataCoverSubscriber;
import com.afra55.commontutils.AppCache;
import com.afra55.commontutils.http.IActionListener;
import com.afra55.commontutils.http.RequestBody;
import com.afra55.commontutils.http.RequestQuery;
import com.afra55.commontutils.http.RxPresenter;
import com.afra55.commontutils.string.MD5;
import com.afra55.commontutils.tip.ToastUtils;

import java.util.Random;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Victor Yang on 2016/7/4.
 * ToTranslateHelper
 */
public class ToTranslateHelper extends RxPresenter {

    private static final String TAG = ToTranslateHelper.class.getSimpleName();
    
    private APIServices apiServices;

    private ToTranslateHelper(IActionListener.ViewAction viewAction) {
        super(viewAction);
        apiServices = RetrofitHelper.createService(APIField.OtherHttp.TRANSLATE_HOST, APIServices.class);

    }

    public static ToTranslateHelper getInstance(IActionListener.ViewAction viewAction) {
        return new ToTranslateHelper(viewAction);
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
                .subscribe(addSubscriber(new DataCoverSubscriber<TranslateBean>() {
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
