package com.webapps.view.view.sliding;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * Created by leon on 17/1/9.
 */

public class SlidingCardBehavior extends CoordinatorLayout.Behavior<SlidingCardLayout>{
    private static final String TAG = "SlidingCardBehavior";

    private int mInitialOffset;
    private Map<String,SlidingCardLayout> mCardLayoutList = new TreeMap<>();


    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, SlidingCardLayout child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        // 当前控件的高度 ＝ 父容器的高度 - 上面和下面child的头部的高度
        int offset = getChildMeasureOffset(parent,child);
        int heightMeasureSpec = View.MeasureSpec.getSize(parentHeightMeasureSpec) - offset;
        child.measure(parentWidthMeasureSpec,View.MeasureSpec.makeMeasureSpec(heightMeasureSpec, View.MeasureSpec.EXACTLY));
        return true;
    }

    /**
     * 获取child偏移后的高度
     * @param parent
     * @param child
     * @return
     */
    private int getChildMeasureOffset(CoordinatorLayout parent, SlidingCardLayout child) {
        int offset = 0;
        for (int i = 0;i<parent.getChildCount();i++){
            View view = parent.getChildAt(i);
            if (view != child && view instanceof SlidingCardLayout){
                offset += ((SlidingCardLayout)view).getHeaderHeight();
            }
        }
        return offset;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, SlidingCardLayout child, int layoutDirection) {
        Log.v(TAG,"child="+child);
        parent.onLayoutChild(child,layoutDirection);
        SlidingCardLayout previous = getPreviousChild(parent,child);
        if (previous != null){
            int offset = previous.getTop() + previous.getHeaderHeight();
            child.offsetTopAndBottom(offset);
        }
        mInitialOffset = child.getTop();
        return true;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, SlidingCardLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        // 是否处理左右滑动
        boolean isVertical = (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        return isVertical && child == directTargetChild;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout parent, SlidingCardLayout child, View target, int dx, int dy, int[] consumed) {
//        super.onNestedPreScroll(parent, child, target, dx, dy, consumed);
        // 1.控制自己的滑动
        if (dy > 0){ // 上推
            consumed[1] = scroll(child,
                    dy,
                    mInitialOffset,
                    mInitialOffset + child.getHeight() - child.getHeaderHeight());
            // 2.控制上边和下边child的滑动
            shiftSlidings(consumed[1],parent,child);
        }else { // 下滑
            if (child.mToTop){ // 是否滑动到顶部
                consumed[1] = scroll(child,
                        dy,
                        mInitialOffset,
                        mInitialOffset + child.getHeight() - child.getHeaderHeight());
                // 2.控制上边和下边child的滑动
                shiftSlidings(consumed[1],parent,child);
            }else {
                super.onNestedPreScroll(parent, child, target, dx, dy, consumed);
            }
        }

    }

    @Override
    public void onNestedScroll(CoordinatorLayout parent, SlidingCardLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(parent, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

//        // 1.控制自己的滑动
//        int shift = scroll(child,
//                dyConsumed,
//                mInitialOffset,
//                mInitialOffset + child.getHeight() - child.getHeaderHeight());
//        // 2.控制上边和下边child的滑动
//        if (shift == 0) {
//            return;
//        }
//        shiftSlidings(shift,parent,child);


    }

    private void shiftSlidings(int shift, CoordinatorLayout parent, SlidingCardLayout child) {
        if (shift > 0) { // 往上推
            // 推动上面所有的卡片
            SlidingCardLayout current = child;
            SlidingCardLayout card = getPreviousChild(parent, current);
            while (card != null) {
                int offset = getHeaderOverlap(card, current);
                if (offset > 0)
                    card.offsetTopAndBottom(-offset);
                current = card;
                card = getPreviousChild(parent,current);
            }

        } else { // 往下推
            SlidingCardLayout current = child;
            SlidingCardLayout card = getNextChild(parent, current);
            while (card != null) {
                int offset = getHeaderOverlap(current,card);
                if (offset > 0)
                    card.offsetTopAndBottom(offset);
                current = card;
                card = getNextChild(parent,current);
            }
        }
    }

    /**
     * 获取当前item和上一个或下一个的偏移值
     * @param above
     * @param below
     * @return
     */
    private int getHeaderOverlap(SlidingCardLayout above, SlidingCardLayout below) {
        return above.getTop() + above.getHeaderHeight() - below.getTop();
    }

    /**
     * 滑动当前child
     * @param child
     * @param dy
     * @param minOffset
     * @param maxOffset
     * @return
     */
    private int scroll(SlidingCardLayout child, int dy,int minOffset,int maxOffset) {
        // dy:[min,max]
        int initialOffset = child.getTop();
        int offset = clamp(initialOffset - dy,minOffset,maxOffset) - initialOffset;
        child.offsetTopAndBottom(offset);
        return - offset; // 是往上滑还是往上滑
    }


    /**
     * 获取上一个child
     * @param parent
     * @param child
     * @return
     */
    private SlidingCardLayout getPreviousChild(CoordinatorLayout parent, SlidingCardLayout child) {
        int cardIndex = parent.indexOfChild(child);
        for (int i = cardIndex - 1; i >= 0 ; i--){
            View v = parent.getChildAt(i);
            if (v instanceof SlidingCardLayout){
                return (SlidingCardLayout) v;
            }
        }
        return null;
    }


    /**
     * 获取下一个child
     * @param parent
     * @param child
     * @return
     */
    private SlidingCardLayout getNextChild(CoordinatorLayout parent, SlidingCardLayout child) {
        int cardIndex = parent.indexOfChild(child);
        for (int i = cardIndex + 1 ;i < parent.getChildCount();i++){
            View v = parent.getChildAt(i);
            if (v instanceof SlidingCardLayout){
                return (SlidingCardLayout) v;
            }
        }
        return null;
    }


    /**
     * 取中间值
     * @param i
     * @param minOffset
     * @param maxOffset
     * @return
     */
    private int clamp(int i,int minOffset,int maxOffset){
        if ( i > maxOffset){
            return maxOffset;
        }else if (i < minOffset){
            return minOffset;
        }else {
            return i;
        }
    }

}
