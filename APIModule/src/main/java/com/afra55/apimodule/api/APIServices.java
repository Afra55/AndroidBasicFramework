package com.afra55.apimodule.api;

import com.afra55.apimodule.bean.TranslateBean;
import com.afra55.commontutils.http.RequestBody;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by Victor Yang on 2016/7/1.
 */
public interface APIServices {

    @POST(APIField.OtherHttp.TRANSLATE_API)
    Observable<TranslateBean> toTranslate(@QueryMap Map<String, Object> map, @Body RequestBody data);
}
