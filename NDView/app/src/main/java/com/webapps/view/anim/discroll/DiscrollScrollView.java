package com.webapps.view.anim.discroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by leon on 16/12/28.
 */

public class DiscrollScrollView extends ScrollView {

    private DiscrollLinearLayout mContent;

    public DiscrollScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View content = getChildAt(0);
        mContent = (DiscrollLinearLayout)content;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        View first = mContent.getChildAt(0);
        first.getLayoutParams().height = getHeight();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int scrollViewHeight = getHeight();
        for (int i = 0;i<mContent.getChildCount();i++){
            View child = mContent.getChildAt(i);
            if (!(child instanceof  DiscrollInterface)){
                continue;
            }
            DiscrollInterface discrollInterface = (DiscrollInterface) child;
            // 什么时候执行动画
            int childHeight = child.getHeight(); //child的高度

            // child浮现的高度 = ScrollView的高度 - child离屏幕顶部的高度
            int childTop = child.getTop();
            // child离parent顶部的高度
            int absoluterTop = childTop - t;

            if (absoluterTop <= scrollViewHeight){ // 当child滑进屏幕的时候
                float visibleGap = scrollViewHeight - absoluterTop;//child浮现的高度
                // child浮现的高度/child的高度
                float ratio = visibleGap / childHeight;
                Log.i("INFO",""+ratio);
                // 确保 ratio是在0~1范围
                discrollInterface.onDiscroll(clamp(ratio,1f,0f));
            }else {
                discrollInterface.onResetDiscroll();
            }
        }
    }

    public float clamp(float value,float max,float min){
        return Math.max(Math.min(value,max),min);
    }

}
