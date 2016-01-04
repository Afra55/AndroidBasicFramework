//-------------------------------------------------------------------------------------
// CMB Confidential
//
// Copyright (C) 2013 China Merchants Bank Co., Ltd. All rights reserved.
//
// No part of this file may be reproduced or transmitted in any form or by any means, 
// electronic, mechanical, photocopying, recording, or otherwise, without prior  
// written permission of China Merchants Bank Co., Ltd.
//
//-------------------------------------------------------------------------------------

package com.magus.youxiclient.view.loadup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.magus.youxiclient.R;


/**
 * listView上拉出现加载更多，主要方法有list.setRefreshLoadingListener();
 * 是否刷新完成list.refreshComplete(Boolean);
 * @author gaobo
 *
 * 2015-5-18
 */
public class PullToBottomRefreshListView extends ListView implements OnScrollListener {

    private static final int PULL_TO_REFRESH = 1;
    private static final int REFRESHING = 2;
    private static final int FINISH = 3;
    
    private LayoutInflater mInflater;
    public LinearLayout mRefreshView;
    public TextView mRefreshViewText;
    private ImageView mRefreshViewImage;
    private ProgressBar mRefreshViewProgress;
    
    public RefreshLoadingListener mListener;
    
    public int status;
    public int scrollStatus;
    public int mRefreshOriginalTopPadding;
    
    public boolean isTouch = false;
	
	public PullToBottomRefreshListView(Context context) {
		super(context);
        init(context);
	}
	
	public PullToBottomRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
        init(context);
	}
	
	public void setRefreshLoadingListener(RefreshLoadingListener l){
		mListener = l;
	}
	
	public PullToBottomRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        init(context);
        

	}
	
	public void init(Context context){
		status = PULL_TO_REFRESH;
        mInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        addFooter();
        super.setOnScrollListener(this);

	}

	private void resetFooter(){
        if (status != PULL_TO_REFRESH) {
    		status = PULL_TO_REFRESH;

            // Set refresh view text to the pull label
//            mRefreshViewText.setText("更多");
            mRefreshViewText.setText("");
            // Clear the full rotation animation
            mRefreshViewImage.clearAnimation();
            // Hide progress bar and arrow.
            mRefreshViewImage.setVisibility(View.GONE);
            mRefreshViewProgress.setVisibility(View.GONE);
        }
	}
	


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollStatus = scrollState;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if ( ( scrollStatus == SCROLL_STATE_TOUCH_SCROLL || scrollStatus == SCROLL_STATE_FLING )
						&& firstVisibleItem + visibleItemCount == totalItemCount && status == PULL_TO_REFRESH) {
			onRefresh();
		}
	}
	
	private static final int DIRECTION_UP = 0x0001;
	private static final int DIRECTION_DOWN = 0x0002;
	
	private static int CURRENT_DIRECTION = -1 ;
	
	private int startY,endY ;
	
	@Override
    public boolean onTouchEvent(MotionEvent ev) {
    	switch(ev.getAction()){
    	case MotionEvent.ACTION_DOWN:
    		isTouch = true;
    		startY = (int) ev.getY() ;
    		break;
    	case MotionEvent.ACTION_CANCEL:
    		isTouch = false;
    		break;
    	case MotionEvent.ACTION_UP:
    		endY = (int) ev.getY() ;
    		if ( endY > startY ) {
    			CURRENT_DIRECTION = DIRECTION_UP ;
    		} else {
    			CURRENT_DIRECTION = DIRECTION_DOWN ;
    		}
    		isTouch = false;
    		break;
    	}
    	return super.onTouchEvent(ev);

    }
    
	public interface RefreshLoadingListener{
		public void onLoading();
	}
	
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0,
                0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }
    
    protected class OnClickRefreshListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (status != REFRESHING) {
                onRefresh();
            }
        }

    }

    
    private void prepareForRefresh() {

        mRefreshViewImage.setVisibility(View.GONE);
        // We need this hack, otherwise it will keep the previous drawable.
        mRefreshViewImage.setImageDrawable(null);
        mRefreshViewProgress.setVisibility(View.VISIBLE);

        // Set refresh view text to the refreshing label
        mRefreshViewText.setText("加载中...");

        status = REFRESHING;
    }
    
    private void onRefresh(){
    	prepareForRefresh();
        if(mListener != null){
	        mListener.onLoading();
        }
    }
    
    public void refreshFailed(){
    	refreshFailed(0);
    }
    
    public void refreshFailed(final int position){
    	this.post(new Runnable() {
			public void run() {
				setSelection(position);
			}
		});
    	resetFooter();
    }
    
    public void refreshComplete(boolean isFinish) {
    	if(isFinish){
        	status = FINISH;
            
mRefreshViewText.setText("");
//        	if(null != mRefreshView) {
//        		removeFooterView(mRefreshView);
//        	}
            if(mRefreshViewProgress!=null){
                mRefreshViewProgress.setVisibility(INVISIBLE);
            }
    	} else {
    		if(getFooterViewsCount() <= 0){
                addFooter();
    		}
    		resetFooter();
    	}
    }
    
	private void addFooter() {
        mRefreshView = (LinearLayout) mInflater.inflate(
                R.layout.pull_refresh_footer, this, false);
        mRefreshViewText =
                (TextView) mRefreshView.findViewById(R.id.pull_to_refresh_text);
        mRefreshViewImage =
                (ImageView) mRefreshView.findViewById(R.id.pull_to_refresh_image);
        mRefreshViewProgress =
                (ProgressBar) mRefreshView.findViewById(R.id.pull_to_refresh_progress);
//        mRefreshViewText.setText("更多");
        mRefreshViewText.setText("");
        addFooterView(mRefreshView);
        mRefreshViewImage.setMinimumHeight(50);
        mRefreshView.setOnClickListener(new OnClickRefreshListener());
        mRefreshView.getPaddingTop();
        mRefreshOriginalTopPadding = mRefreshView.getPaddingTop();
        measureView(mRefreshView);

   }
}
