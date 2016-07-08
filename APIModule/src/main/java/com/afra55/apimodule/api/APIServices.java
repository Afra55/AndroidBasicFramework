package com.afra55.apimodule.api;

import com.afra55.apimodule.bean.TranslateBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by Victor Yang on 2016/7/1.
 */
public interface APIServices {

    @POST(APIField.OtherHttp.TRANSLATE_API)
    Call<TranslateBean> toTranslate(@QueryMap Map<String, String> map);
}
