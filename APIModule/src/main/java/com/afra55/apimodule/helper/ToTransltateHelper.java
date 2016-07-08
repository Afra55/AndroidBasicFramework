package com.afra55.apimodule.helper;

import android.text.TextUtils;

import com.afra55.apimodule.api.APIField;
import com.afra55.apimodule.api.APIServices;
import com.afra55.apimodule.bean.TranslateBean;
import com.afra55.commontutils.string.MD5;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    public void toTanstale(String string, final ToTranslateResultListener listener) {

        if (TextUtils.isEmpty(string.trim())) {
            if (listener != null)
                listener.onFaile("请输入文字！！");
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIField.OtherHttp.TRANSLATE_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIServices apiServices = retrofit.create(APIServices.class);

        Map<String, String> map = new HashMap<>();
        map.put("q", string);
        map.put("from", "auto");
        map.put("to", "auto");
        map.put("appid", APIField.OtherHttp.APPID);
        int salt = new Random(100).nextInt();
        map.put("salt", String.valueOf(salt));
        map.put("sign", MD5.getStringMD5(APIField.OtherHttp.APPID + string + salt + APIField.OtherHttp.SECRET));
        Call<TranslateBean> call = apiServices.toTranslate(map);

        call.enqueue(new Callback<TranslateBean>() {
            @Override
            public void onResponse(Call<TranslateBean> call, Response<TranslateBean> response) {
                if (listener != null)
                    listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<TranslateBean> call, Throwable t) {
                if (listener != null)
                    listener.onFaile(t.toString());
            }
        });
    }

    public interface ToTranslateResultListener {
        void onSuccess(TranslateBean translateBean);

        void onFaile(String tip);
    }
}
