package com.afra55.apimodule.services;

import com.afra55.apimodule.domain.model.TranslateBean;
import com.afra55.commontutils.http.RequestBody;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by Victor Yang on 2016/7/1.
 */
public interface APIServices {

    @POST
    Observable<TranslateBean> toTranslate(@Url String url, @QueryMap Map<String, Object> queryMap, @Body RequestBody data);
}
