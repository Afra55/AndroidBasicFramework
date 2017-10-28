package com.afra55.baseclient.common;

import android.content.Context;
import android.view.ViewGroup;

import com.afra55.commontutils.base.BaseRecyclerAdapter;
import com.afra55.commontutils.base.BaseViewHolder;

/**
 * Created by Afra55 on 2017/10/28.
 * Smile is the best name card.
 */

public class AppRecyclerAdapter extends BaseRecyclerAdapter {
    public AppRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseViewHolder onCreateViewHolder(Context context, ViewGroup parent, int viewType) {
        return ViewHolderFactory.getViewHolder(context, parent, viewType);
    }

}
