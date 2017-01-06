package com.webapps.view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * 滑动时放大动画的字母索引
 * Created by leon on 16/12/6.
 */

public class SlideView extends View {

    private Paint mPaint;
    private String[] mLetters = {"#","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    private int mChoose = -1;
    private float mDensity;
    private RectF mIsDownRect = new RectF();
    private boolean mIsBeingDragger;
    private float mWidth,mHeight;
    private float mLetterHeight;
    private float mAnimStep;
    private float mY;
    private int mTouchSlop;
    private float mInitDownY;


    public SlideView(Context context) {
        super(context);
        init(context);
    }

    public SlideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void init(Context context){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mDensity = context.getResources().getDisplayMetrics().density;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setPadding(0,dip2PX(20),0,dip2PX(20));
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h - getPaddingTop() - getPaddingBottom();
        mLetterHeight = mHeight / mLetters.length;
        int textSize = (int)(mLetterHeight * 0.7);
        mPaint.setTextSize(textSize);
        mWidth = w - dip2PX(16);
        mIsDownRect.set(w-dip2PX(32),0,w,h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0;i<mLetters.length;i++){

            float lettersPos = mLetterHeight *(i+1) + getPaddingTop();
            float diffY;
            float diffX;
            float diff;

            if (mChoose == i && i!= 0 && i != mLetters.length - 1){
                diff = 2.2f;
                diffY = 0;
                diffX = 0;
            }else{
                float distanceDiff = Math.abs((mY - lettersPos) / mHeight);
                float maxPos = distanceDiff * 7;
                if (distanceDiff < 0.174 && mChoose != -1){
                    diff = 2.2f - maxPos;
                }else{
                    diff = 1;
                }
                diffX = maxPos * 50;
                if (mY > lettersPos ){
                    diffY = -maxPos * 50;
                }else{
                    diffY = maxPos * 50;
                }
            }

            canvas.save();
            canvas.scale(diff,diff,mWidth * 1.2f + diffX,lettersPos + diffY);

            if (diff == 1){
                mPaint.setAlpha(255);
                mPaint.setTypeface(Typeface.DEFAULT);
            }else{
                int alpha = (int) (255 * (1 - Math.min(0.9,diff-1)));
                if (mChoose == i){
                    alpha = 255;
                }
                mPaint.setAlpha(alpha);
                mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            }
            canvas.drawText(mLetters[i],mWidth,lettersPos,mPaint);
            canvas.restore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mIsBeingDragger = false;
                float initDownY = event.getY();
                // 如果触摸点不在指定范围内
                if (!mIsDownRect.contains(event.getX(),event.getY())){
                    return false;
                }
                mInitDownY = initDownY;
                touchInvalidate(mInitDownY);
                break;
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                float diff = Math.abs(y-mInitDownY);
                // 如果移动值大于触摸值
                if (diff > mTouchSlop && !mIsBeingDragger){
                    mIsBeingDragger = true;
                }
                if (mIsBeingDragger){
                    touchInvalidate(y);
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsBeingDragger = false;
                mInitDownY = -1;
                mChoose = -1;
                mY = -1;
                if (mSlideViewListener != null){
                    mSlideViewListener.onChange("");
                }
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }


        return true;
    }

    private void touchInvalidate(float y){
        mY = y;
        float sY = y - getPaddingTop();
        // 获取字母index
        int charterIndex = (int)(sY / mHeight * mLetters.length);
        if (mChoose != charterIndex){
            mChoose = charterIndex;
            if (mChoose >= 0 && mChoose < mLetters.length){
                if (mSlideViewListener != null){
                    mSlideViewListener.onChange(mLetters[charterIndex]);
                }
            }
        }
        invalidate();
    }

    private int dip2PX(float dixPX){
        return (int)(dixPX * mDensity + 0.5f);
    }

    interface SlideViewListener{
        void onChange(String character);
    }

    private SlideViewListener mSlideViewListener;

    public SlideViewListener getSlideViewListener(){
        return mSlideViewListener;
    }

    public void setSlideViewListener(SlideViewListener slideViewListener){
        mSlideViewListener = slideViewListener;
    }

}
