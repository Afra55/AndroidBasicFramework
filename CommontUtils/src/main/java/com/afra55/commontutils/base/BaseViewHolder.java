package com.afra55.commontutils.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afra55.commontutils.R;

import butterknife.ButterKnife;

/**
 * Created by Afra55 on 2017/10/12.
 * Smile is the best name card.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private View itemView;
    protected Context context;

    public BaseViewHolder(Context context, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.itemView = itemView;
        this.context = context;
    }

    public void bind(Object obj) {
        // 子类需要重写

    }

    public View getItemView() {
        return itemView;
    }

    public static BaseViewHolder createViewHolder(Context context, View itemView) {
        return new BaseViewHolder(context, itemView);
    }

    public static BaseViewHolder createViewHolder(Context context,
                                                  ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        return new BaseViewHolder(context, itemView);
    }

    public void setTag(BaseViewSugar viewItem) {
        itemView.setTag(R.integer.sugar, viewItem);

    }

    public BaseViewSugar getTag() {
        Object tag = itemView.getTag(R.integer.sugar);
        if (tag != null
                && tag instanceof BaseViewSugar) {
            return (BaseViewSugar) tag;
        }
        return null;
    }


}
