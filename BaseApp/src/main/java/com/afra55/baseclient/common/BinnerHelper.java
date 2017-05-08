package com.afra55.baseclient.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.afra55.baseclient.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yangshuai in the 17:13 of 2016.01.06 .
 */
public class BinnerHelper {

    private ViewPager binnerVp;
    private Context context;

    private BinnerHelper() {
    }

    public static BinnerHelper getInstance() {
        return new BinnerHelper();
    }

    public BinnerHelper start(Context context, ViewPager viewPager, final ArrayList<View> binnerViewArray, RadioGroup binnerIndicatorRg) {
        this.context = context;
        binnerVp = viewPager;
        binnerVp.setCurrentItem(1);
        binnerVp.setOffscreenPageLimit(binnerViewArray.size());

        addChangeListener(binnerViewArray, binnerIndicatorRg);

        initIndicator(binnerViewArray, binnerIndicatorRg);

        beginPagerTimer();
        return this;
    }

    private void initIndicator(ArrayList<View> binnerViewArray, RadioGroup binnerIndicatorRg) {
        binnerIndicatorRg.removeAllViews();
        for (int i = 0; i < binnerViewArray.size(); i++) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            RadioButton rbIndicator = (RadioButton) layoutInflater.inflate(
                    R.layout.ad_vp_indicator_rb, binnerIndicatorRg, false);
            if (binnerViewArray.size() > 3)
                if (i == 0) {
                    rbIndicator.setVisibility(View.GONE);
                } else if (i == binnerViewArray.size() - 1) {
                    rbIndicator.setVisibility(View.GONE);
                }
            binnerIndicatorRg.addView(rbIndicator);
        }
        ((RadioButton) binnerIndicatorRg.getChildAt(0)).setChecked(true);
    }

    private int lastCheckedPosition = 0;
    private void addChangeListener(final ArrayList<View> binnerViewArray, final RadioGroup binnerIndicatorRg) {
        binnerVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int willCheckedPosition;
                if (positionOffset >= 0.5) {
                    int nextPosition = position + 1;
                    if (nextPosition == binnerViewArray.size() - 1) {
                        willCheckedPosition = 1;
                    } else {
                        willCheckedPosition = nextPosition;
                    }
                } else if (position == 0) {
                    willCheckedPosition = binnerViewArray.size() - 2;
                } else {
                    willCheckedPosition = position;
                }
                if (willCheckedPosition != lastCheckedPosition) {
                    ((RadioButton) binnerIndicatorRg.getChildAt(lastCheckedPosition)).setChecked(false);
                }
                ((RadioButton) binnerIndicatorRg.getChildAt(willCheckedPosition)).setChecked(true);
                lastCheckedPosition = willCheckedPosition;
                if (binnerViewArray.size() <= 1) {
                    return;
                }
                if (position == 0 && positionOffset <= 0.01) {
                    binnerVp.setCurrentItem(binnerViewArray.size() - 2, false);
                } else if (position == binnerViewArray.size() - 1) {
                    binnerVp.setCurrentItem(1, false);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:// 如果用户点击viewpager，则取消自动翻页
                        pageTouch = true;
                        canelPagerTimer();
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:// 如果用户点击viewpager并松开，则开启自动翻页
                        if (pageTouch) {
                            pageTouch = false;
                            beginPagerTimer();
                        }
                        break;
                }
            }
        });
    }


    /* 自动翻页功能 start */
    private Timer timer;
    private int index = 0;
    private MyTimerTask mTimerTask;
    /**
     * 自动翻页时间间隔
     **/
    private final int TIMER_OFFSET = 3 * 1000;
    /**
     * viewpager滚动速率
     **/
    private final int SPEED_OFFSET = 800;
    /**
     * 判断手指是否碰触viewpager
     **/
    private boolean pageTouch;

    /**
     * 创建或重置计时器
     */
    public void beginPagerTimer() {
        canelPagerTimer();
        // 设置viewpaper自动播放
        timer = new Timer();
        if (mTimerTask != null) {
            mTimerTask.cancel(); // 将原任务从队列中移除
        }
        mTimerTask = new MyTimerTask(); // 新建一个任务
        timer.schedule(mTimerTask, TIMER_OFFSET, TIMER_OFFSET + SPEED_OFFSET);
    }

    /***
     * 停止翻页计时器
     */
    public void canelPagerTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 定时器，实现自动播放
     */
    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 2;
            index = binnerVp.getCurrentItem();
            index++;
            handler.sendMessage(message);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    binnerVp.setCurrentItem(index);
                    break;
                default:
                    break;
            }
        }
    };
    /* 自动翻页功能 end */

    /**
     * 初始化viewpager资源
     *
     * @param binnerViewArray view list
     * @param startAndEndView 存储开始和结束的 view
     */
    public static void initViewList(ArrayList<View> binnerViewArray, ArrayList<View> startAndEndView) {
        if (binnerViewArray.size() <= 1) {
            return;
        }
        ArrayList<View> binnerViewArrayClone = (ArrayList<View>) binnerViewArray.clone();
        binnerViewArray.clear();
        for (int i = 0; i < binnerViewArrayClone.size() + 2; i++) {
            if (i <= 0) {
                binnerViewArray.add(startAndEndView.get(startAndEndView.size() - 1));
            } else if (i >= binnerViewArrayClone.size() + 1) {
                binnerViewArray.add(startAndEndView.get(0));
            } else {
                binnerViewArray.add(binnerViewArrayClone.get(i - 1));
            }
        }
    }
}
