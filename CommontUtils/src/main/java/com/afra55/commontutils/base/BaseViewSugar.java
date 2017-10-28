package com.afra55.commontutils.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Afra55 on 2017/10/14.
 * Smile is the best name card.
 */

public abstract class BaseViewSugar {
    protected Context context;
    protected ViewGroup parent;
    protected BaseViewHolder baseViewHolder;
    protected View rootView;

    public BaseViewSugar(Context context, ViewGroup parent) {
        this.context = context;
        this.parent = parent;
        baseViewHolder = createViewHolder(getLayoutId());
        rootView = baseViewHolder.getItemView();
        ButterKnife.bind(this, rootView);
    }

    public abstract
    @LayoutRes
    int getLayoutId();


    public abstract <T extends BaseBean> void bind(T obj);

    private BaseViewHolder createViewHolder(int layoutId) {
        return BaseViewHolder.createViewHolder(context, parent, layoutId);
    }

    public BaseViewHolder getViewHolder() {
        baseViewHolder.setTag(this);
        return baseViewHolder;
    }

}
