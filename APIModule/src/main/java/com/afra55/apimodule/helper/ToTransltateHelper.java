package com.afra55.apimodule.helper;

import android.text.TextUtils;

import com.afra55.apimodule.api.APIField;
import com.afra55.apimodule.api.APIServices;
import com.afra55.apimodule.domain.model.TranslateBean;
import com.afra55.commontutils.string.MD5;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Victor Yang on 2016/7/4.
 */
public class ToTransltateHelper {

    private static final String TAG = ToTranslateResultListener.class.getSimpleName();

    private ToTransltateHelper() {

    }

    private static class Instance {
        public static final ToTransltateHelper instance = new ToTransltateHelper();
    }

    public static ToTransltateHelper getInstance() {
        return Instance.instance;
    }

    private Subscriber<TranslateBean> subscriber;

    public void toTanstale(String string, final ToTranslateResultListener listener) {

        if (TextUtils.isEmpty(string.trim())) {
            if (listener != null)
                listener.onFaile("请输入文字！！");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("q", string);
        map.put("from", "auto");
        map.put("to", "auto");
        map.put("appid", APIField.OtherHttp.APPID);
        int salt = new Random(100).nextInt();
        map.put("salt", String.valueOf(salt));
        map.put("sign", MD5.getStringMD5(APIField.OtherHttp.APPID + string + salt + APIField.OtherHttp.SECRET));

        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
        initSubscriber(listener);
        RetrofitHelper.retrofit(APIField.OtherHttp.TRANSLATE_HOST)
                .create(APIServices.class)
                .toTranslate(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
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
