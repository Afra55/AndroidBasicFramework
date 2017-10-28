package com.afra55.commontutils.base;

/**
 * Created by Afra55 on 2017/10/13.
 * Smile is the best name card.
 */

public interface OnItemClickListener {
    void onItemClicked(int position, Object obj);

    boolean onItemLongClicked(int position, Object obj);
}
