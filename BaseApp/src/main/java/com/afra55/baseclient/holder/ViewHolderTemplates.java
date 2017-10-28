package com.afra55.baseclient.holder;

import android.content.Context;
import android.view.ViewGroup;

import com.afra55.baseclient.common.AppViewSugar;
import com.afra55.commontutils.base.BaseBean;

/**
 * Created by Afra55 on 2017/10/28.
 * Smile is the best name card.
 */

public class ViewHolderTemplates extends AppViewSugar {

    public static AppViewSugar getInstance(Context context, ViewGroup parent) {
        return new ViewHolderTemplates(context, parent);
    }

    // 通过 id 使用 BufferKnife 绑定布局

    public ViewHolderTemplates(Context context, ViewGroup parent) {
        super(context, parent);
    }

    @Override
    public int getLayoutId() {
        // 布局id
        return 0;
    }

    @Override
    public <T extends BaseBean> void bind(T obj) {
        // 数据操作
    }
}
