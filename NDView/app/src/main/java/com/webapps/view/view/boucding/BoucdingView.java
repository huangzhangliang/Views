package com.webapps.view.view.boucding;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by leon on 17/1/4.
 */

public class BoucdingView extends View {

    private static final String TAG = "BoucdingView";

    private static final int NONE = 0;
    private static final int STATUS_SMOOTH_UP = 1;
    private static final int STATUS_DOWN = 2;

    private Paint mPaint;
    private int mArcHeight; // 当前弧高
    private int mMaxArcheitht;// 最大弧高
    private int mStatus; // 记录
    private OnAnimationListener mAnimatorListener;
    private Path mPath = new Path();


    public BoucdingView(Context context) {
        this(context,null);
    }

    public BoucdingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mMaxArcheitht = 800;
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBG(canvas);
    }

    private void drawBG(Canvas canvas){
        int currentPointY = 0;
        mPath.reset();
        // 计算 不断的变化Y值
        switch (mStatus){
            case NONE:
                currentPointY = 0;
                break;
            case STATUS_SMOOTH_UP:
                // currentPointY -- 跟mArcHeight的变化率差水多
                currentPointY = (int)(getHeight() * (1 - (float) mArcHeight / mMaxArcheitht) + mMaxArcheitht);
                break;
            case STATUS_DOWN:
                currentPointY = mMaxArcheitht;
                break;
        }

        mPath.moveTo(0,currentPointY);
        mPath.quadTo(getWidth() / 2,currentPointY - mArcHeight / 4,getWidth(),currentPointY);
        mPath.lineTo(getWidth(),getHeight());
        mPath.lineTo(0,getHeight());
        mPath.close();
        canvas.drawPath(mPath,mPaint);
    }


    public void show(){
        // 中间某个时间点显示数据
        if (mAnimatorListener != null){
            mAnimatorListener.onStart();
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 显示数据
                    mAnimatorListener.onContentShow();
                }
            },600);
        }
        mStatus = STATUS_SMOOTH_UP;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,mMaxArcheitht);
        valueAnimator.setDuration(400);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.i(TAG,""+(float)animation.getAnimatedValue());
                mArcHeight = (int) ((float) animation.getAnimatedValue());
                if (mArcHeight == mMaxArcheitht){
                    // 回弹一下
                    bounce();
                }
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.start();
    }

    /**
     * 回弹
     */
    private void bounce() {
        mStatus = STATUS_DOWN;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(mMaxArcheitht,0);
        valueAnimator.setDuration(400);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mArcHeight =(int) ((float) animation.getAnimatedValue());
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.start();
    }

    public void setAnimatorListener(OnAnimationListener animatorListener){
        mAnimatorListener = animatorListener;
    }

    public interface OnAnimationListener{
        void onStart();
        void onEnd();
        void onContentShow();
    }

}
