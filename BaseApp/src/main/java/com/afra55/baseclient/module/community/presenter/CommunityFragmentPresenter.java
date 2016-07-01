package com.afra55.baseclient.module.community.presenter;

import com.afra55.baseclient.module.community.ui.CommunityFragmentUI;
import com.afra55.commontutils.string.MD5;
import com.example.shuai.apimodule.api.APIField;
import com.example.shuai.apimodule.api.APIServices;
import com.example.shuai.apimodule.bean.TranslateBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Victor Yang on 2016/6/30.
 */
public class CommunityFragmentPresenter {

    private CommunityFragmentUI mCommunityFragmentUI;

    public CommunityFragmentPresenter(CommunityFragmentUI communityFragmentUI) {
        mCommunityFragmentUI = communityFragmentUI;
    }

    public void toTranslate(String string) {
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
                TranslateBean translateBean = response.body();
                List<TranslateBean.TransResultBean> list = translateBean.getTrans_result();
                String result = "";
                for (TranslateBean.TransResultBean resultBean : list) {
                    result += resultBean.getDst() + " ";
                }
                mCommunityFragmentUI.setTranslateResult(result);
            }

            @Override
            public void onFailure(Call<TranslateBean> call, Throwable t) {
                mCommunityFragmentUI.showToast(t.toString());
                mCommunityFragmentUI.setTranslateError(t.toString());
            }
        });
    }
}
