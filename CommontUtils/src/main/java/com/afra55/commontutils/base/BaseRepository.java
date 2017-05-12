package com.afra55.commontutils.base;


import android.support.annotation.NonNull;

/**
 * Created by yangshuai on 2017/5/12.
 */

public interface BaseRepository {
    <T extends BaseBean> void  insert(@NonNull T data);

    <T extends BaseBean> void update(@NonNull T data);

    <T extends BaseBean> void delete(@NonNull T data);
}
