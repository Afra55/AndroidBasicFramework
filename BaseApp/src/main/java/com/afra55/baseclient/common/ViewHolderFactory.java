package com.afra55.baseclient.common;

import android.content.Context;
import android.view.ViewGroup;

import com.afra55.baseclient.holder.ViewHolderTemplates;
import com.afra55.commontutils.base.BaseViewHolder;
import com.afra55.commontutils.base.BaseViewSugar;


/**
 * Created by Afra55 on 2017/10/14.
 * Smile is the best name card.
 */

public class ViewHolderFactory {

    public static final int TEMPLATES = 111;

    public static BaseViewHolder getViewHolder(Context context, ViewGroup parent, int type) {
        BaseViewSugar viewSugar = null;
        switch (type) {
            case TEMPLATES:
                viewSugar = ViewHolderTemplates.getInstance(context, parent);
                break;
        }
        if (viewSugar != null) {
            return viewSugar.getViewHolder();
        }
        return null;
    }
}
