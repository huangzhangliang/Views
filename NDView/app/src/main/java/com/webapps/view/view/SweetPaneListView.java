package com.webapps.view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ListView;

import com.webapps.view.R;

/**
 * Created by leon on 17/2/4.
 */

public class SweetPaneListView extends ListView implements AbsListView.OnScrollListener{

    private static final String TAG = "SweetPaneListView";

    private View mScrollBarPanelView;
    private int mWidthMeasureSpec;
    private int mHeightMeasureSpec;
    private int mScrollBarPanelPosition = 0;
    private Animation mInAnimation;
    private Animation mOutAnimation;
    public int mThumbOffset = 0;
    private int mLastPosition = -1;
    private OnPositionChangedListener mPositionChangedListener;

    public SweetPaneListView(Context context) {
        this(context,null);
    }

    public SweetPaneListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SweetPaneListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExtendedListView);
        int layoutId = a.getResourceId(R.styleable.ExtendedListView_scrollBarPanel,-1);
        int inAnimation = a.getResourceId(R.styleable.ExtendedListView_scrollBarPanelInAnimation,-1);
        int outAnimation = a.getResourceId(R.styleable.ExtendedListView_scrollBarPanelOutAnimation,-1);
        Log.v(TAG,""+layoutId);
        Log.v(TAG,""+inAnimation);
        Log.v(TAG,""+outAnimation);
        a.recycle();
        setScrollBarPanelView(layoutId);
        setOnScrollListener(this);
        mInAnimation = AnimationUtils.loadAnimation(context,inAnimation);
        mOutAnimation = AnimationUtils.loadAnimation(context,outAnimation);
        long durationMillis = ViewConfiguration.getScrollBarFadeDuration();
        mOutAnimation.setDuration(durationMillis);
        mOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (mScrollBarPanelView != null){
                    mScrollBarPanelView.setVisibility(GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void setScrollBarPanelView(int layoutId ){
        mScrollBarPanelView = LayoutInflater.from(getContext()).inflate(layoutId,this,false);
        mScrollBarPanelView.setVisibility(GONE);
        requestLayout();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 测量滑块view的大小
        if (mScrollBarPanelView != null && getAdapter() != null){
            mWidthMeasureSpec = widthMeasureSpec;
            mHeightMeasureSpec = heightMeasureSpec;
            measureChild(mScrollBarPanelView,mWidthMeasureSpec,mHeightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        // 摆放滑块的位置
        if (mScrollBarPanelView != null){
            measuredPanelView();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mScrollBarPanelView != null && mScrollBarPanelView.getVisibility() == VISIBLE){
            drawChild(canvas,mScrollBarPanelView,getDrawingTime());
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.v(TAG,"firstVisibleItem："+firstVisibleItem);
        if (mScrollBarPanelView != null && mPositionChangedListener != null){

//            computeHorizontalScrollOffset(); // 滚动条在纵向滚动范围内它自身厚度的幅度
//            computeHorizontalScrollExtent(); // 滚动条的纵向幅度的位置
//            computeHorizontalScrollRange(); // 0~10000 , 纵向滚动条代表的整个纵向范围

            // 1.滑块的高度，思路：滑块的高度/ListView的高度 = extent / Range
            int height = Math.round(1.0f * getMeasuredHeight() * computeVerticalScrollExtent() / computeVerticalScrollRange());
            // 2.得到滑块正中间的y坐标，思路：滑块的高度 / extent = thumbOffset / offset

            mThumbOffset = height * computeVerticalScrollOffset() / computeVerticalScrollExtent();
            mThumbOffset += height / 2;

            mScrollBarPanelPosition = mThumbOffset - mScrollBarPanelView.getHeight() / 2;

            measuredPanelView();

            // 3.回调 --- 显示位置信息
            for (int i = 0;i<getChildCount();i++){
                View childView = getChildAt(i);
                if (childView != null){
                    if (mThumbOffset > childView.getTop() && mThumbOffset < childView.getBottom()){
                        if (mLastPosition != firstVisibleItem + i){
                            mLastPosition = firstVisibleItem + i;
                            mPositionChangedListener.onPositionChanged(this,mLastPosition,mScrollBarPanelView);
                            // 宽度会发生改变，所以需要重新测量一下
                            measureChild(mScrollBarPanelView,mWidthMeasureSpec,mHeightMeasureSpec);
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected boolean awakenScrollBars(int startDelay, boolean invalidate) {
        // 唤醒滑块
        // 判断系统的滑块是否唤醒了

        boolean awaken = super.awakenScrollBars(startDelay, invalidate);
        if (awaken && mScrollBarPanelView != null){
            if (mScrollBarPanelView.getVisibility() == GONE){
                mScrollBarPanelView.setVisibility(VISIBLE);
                if (mInAnimation != null){
                    mScrollBarPanelView.startAnimation(mInAnimation);
                }
            }
            // 消失
            mHandler.removeCallbacks(mScrollRunnable);
            mHandler.postAtTime(mScrollRunnable,startDelay + AnimationUtils.currentAnimationTimeMillis());
        }
        return awaken;
    }

    Handler mHandler = new Handler();

    private final  Runnable mScrollRunnable = new Runnable() {
        @Override
        public void run() {
            if (mOutAnimation != null){
                mScrollBarPanelView.startAnimation(mOutAnimation);
            }
        }
    };


    public interface OnPositionChangedListener{
        void onPositionChanged(SweetPaneListView listView,int position,View scrollBarPanelView);
    }

    public void setOnPositionChangedListener(OnPositionChangedListener positionChangedListener){
        mPositionChangedListener = positionChangedListener;
    }

    private void measuredPanelView(){
        int left = getMeasuredWidth() - mScrollBarPanelView.getMeasuredWidth() - getVerticalScrollbarWidth();
        Log.v(TAG,"left："+left);
        mScrollBarPanelView.layout(left,
                mScrollBarPanelPosition,
                left + mScrollBarPanelView.getMeasuredWidth(),
                mScrollBarPanelPosition + mScrollBarPanelView.getMeasuredHeight());
    }

}
