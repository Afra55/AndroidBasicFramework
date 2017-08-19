package com.afra55.apimodule.helper;

import android.text.TextUtils;

import com.afra55.apimodule.api.APIField;
import com.afra55.apimodule.api.APIServices;
import com.afra55.apimodule.bean.TranslateBean;
import com.afra55.apimodule.util.DataCoverSubscriber;
import com.afra55.commontutils.http.IActionListener;
import com.afra55.commontutils.http.RequestBody;
import com.afra55.commontutils.http.RequestQuery;
import com.afra55.commontutils.http.RxPresenter;
import com.afra55.commontutils.string.MD5;

import java.util.Random;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Victor Yang on 2016/7/4.
 */
public class ToTranslateHelper extends RxPresenter {

    private static final String TAG = ToTranslateResultListener.class.getSimpleName();
    
    private APIServices apiServices;

    private ToTranslateHelper(IActionListener.ViewAction viewAction) {
        super(viewAction);
        apiServices = RetrofitHelper.createService(APIField.OtherHttp.TRANSLATE_HOST, APIServices.class);

    }

    public static ToTranslateHelper getInstance(IActionListener.ViewAction viewAction) {
        return new ToTranslateHelper(viewAction);
    }

    private Subscriber<TranslateBean> subscriber;

    public void toTanstale(String string, final ToTranslateResultListener listener) {

        if (TextUtils.isEmpty(string.trim())) {
            if (listener != null)
                listener.onFaile("请输入文字！！");
            return;
        }

        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
        initSubscriber(listener);

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

                    }

                    @Override
                    public void onFailure(int errorCode, String errorMsg) {

                    }
                }));
        
    }

    private void initSubscriber(final ToTranslateResultListener listener) {
            subscriber = new Subscriber<TranslateBean>() {

                @Override
                public void onStart() {
                    if (listener != null) {
                        listener.showProgressDialog();
                    }
                    super.onStart();
                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    // 可以封装一个类专门抓取错误信息
                    if (listener != null)
                        listener.onFaile(e.toString());
                }

                @Override
                public void onNext(TranslateBean translateBean) {
                    if (listener != null)
                        listener.onSuccess(translateBean);
                }
            };
    }

    public interface ToTranslateResultListener {

        void showProgressDialog();

        void onSuccess(TranslateBean translateBean);

        void onFaile(String tip);
    }
}
