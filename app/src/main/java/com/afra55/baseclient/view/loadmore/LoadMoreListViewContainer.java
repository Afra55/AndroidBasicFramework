package com.afra55.baseclient.view.loadmore;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by yangshuai in the 16:35 of 2015.11.23 .
 */
public class LoadMoreListViewContainer extends LoadMoreContainerBase {

    private ListView mListView;

    public LoadMoreListViewContainer(Context context) {
        super(context);
    }

    public LoadMoreListViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void addFooterView(View view) {
        mListView.addFooterView(view);
    }

    @Override
    protected void removeFooterView(View view) {
        mListView.removeFooterView(view);
    }

    @Override
    protected AbsListView retrieveAbsListView() {
        mListView = (ListView) getChildAt(0);
        return mListView;
    }
}
