package com.afra55.commontutils.network;

import android.text.TextUtils;

import com.afra55.commontutils.AppCache;
import com.afra55.commontutils.log.LogUtils;
import com.afra55.commontutils.sys.NetworkUtil;
import com.afra55.commontutils.tip.ToastUtils;

import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public abstract class DataCoverSubscriber<T> extends Subscriber<T> {

    private final String TAG = DataCoverSubscriber.class.getSimpleName();

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Throwable throwable = e;
        //获取最根源的异常
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }
        //无网络连接
        if (!NetworkUtil.isNetAvailable(AppCache.getContext())
                || e instanceof SocketTimeoutException) {
            onFailure(1, e.getMessage());
            ToastUtils.showToast(AppCache.getContext(),"网络不正常,请检查网络");
        } else if (e instanceof HttpRuntimeException) {
            //业务异常
            HttpRuntimeException exception = (HttpRuntimeException) e;
            int errorCode = Integer.parseInt(exception.getErrorCode());
            onFailure(errorCode, exception.getErrorMsg());
            if (errorCode == 401) {
                ToastUtils.showToast(AppCache.getContext(),"您的登录已过期，请重新登录");
//                LogoutHelper.logout(AppCache.getContext());
                return;
            }
            String message = e.getMessage();
            if (!TextUtils.isEmpty(message)) {
                ToastUtils.showToast(AppCache.getContext(), message);
            }
        } else if (e instanceof HttpException) {
            //网络连接异常
            HttpException httpException = (HttpException) e;
            int errorCode = httpException.code();
            onFailure(errorCode,
                    httpException.getMessage());
            if (errorCode == 401) {
                ToastUtils.showToast(AppCache.getContext(),"您的登录已过期，请重新登录");
//                LogoutHelper.logout(AppCache.getContext());
            }
        } else {
            //其他异常
            onFailure(0, e.getMessage());
            if(!TextUtils.isEmpty(e.getMessage())){
                LogUtils.e(TAG,"======="+e.getMessage(), e);
                if (!e.getMessage().contains("Attempt to invoke interface method")) {
                    ToastUtils.showToast(AppCache.getContext(), e.getMessage());
                } else {
                    ToastUtils.showToast(AppCache.getContext(), "获取数据失败，请稍后再试");
                }
            }
        }
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    public abstract void onSuccess(T t);
    public abstract void onFailure(int errorCode,String errorMsg);
}
