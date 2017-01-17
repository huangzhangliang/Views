package com.webapps.view.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.webapps.view.R;

/**
 * 水波浪动画
 * Created by leon on 17/1/16.
 */

public class WaveView extends View {


    private Paint mPaint;
    private Path mPath;
    private int mWaveView_boatBitmap;
    private boolean mWaveView_rise;
    private int mWaveView_duration;
    private int mWaveView_originY;
    private int mWaveView_waveHeight;
    private int mWaveView_waveLength;
    private Bitmap mBitmap;
    private int mWidth;
    private int mHeight;
    private int mDx;
    private int mDy;
    private Region mRegion;

    public WaveView(Context context) {
        this(context,null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initAttrs(context,attrs);
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ff00ff"));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPath = new Path();
    }

    private void initAttrs(Context context,AttributeSet attrs){

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.WaveView);
        mWaveView_boatBitmap = a.getResourceId(R.styleable.WaveView_boatBitmap,0);
        mWaveView_rise = a.getBoolean(R.styleable.WaveView_rise,false);
        mWaveView_duration = (int) a.getDimension(R.styleable.WaveView_duration,2000);
        mWaveView_originY = (int) a.getDimension(R.styleable.WaveView_originY,500);
        mWaveView_waveHeight = (int) a.getDimension(R.styleable.WaveView_waveHeight,80);
        mWaveView_waveLength = (int) a.getDimension(R.styleable.WaveView_waveLength,400);
        a.recycle();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        if (mWaveView_boatBitmap > 0){
            mBitmap = BitmapFactory.decodeResource(getResources(),mWaveView_boatBitmap,options);
        }else {
            mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon_app_120,options);
        }

    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setPathData();


        Rect bounds = mRegion.getBounds();
        canvas.drawPath(mPath,mPaint);

        if (bounds.top > 0 || bounds.right > 0){
            if (bounds.top < mWaveView_originY - mDy){
                canvas.drawBitmap(mBitmap,bounds.right-mBitmap.getWidth() / 2,bounds.top - mBitmap.getHeight(),mPaint);
            }else {
                canvas.drawBitmap(mBitmap,bounds.right-mBitmap.getWidth() / 2,bounds.bottom - mBitmap.getHeight(),mPaint);
            }
        }else {
            float x = mWidth / 2 - mBitmap.getWidth() / 2;
            canvas.drawBitmap(mBitmap,x,mWaveView_originY-mDy-mBitmap.getHeight(),mPaint);;
        }

    }


    private void setPathData(){
        mWidth = getWidth();
        mHeight = getHeight();
        mPath.reset();
        // 不断的绘制一个又一个波形，保证屏幕左边有一个完整的波形
        mPath.moveTo(-mWaveView_waveLength+mDx,mWaveView_originY);
        for(int i = - mWaveView_waveLength; i < mWidth + mWaveView_waveLength;i += mWaveView_waveLength){
            // 相对绘制二阶贝塞尔曲线
            mPath.rQuadTo(mWaveView_waveLength/4,-mWaveView_waveHeight,mWaveView_waveLength/2,0);
            mPath.rQuadTo(mWaveView_waveLength/4,mWaveView_waveHeight,mWaveView_waveLength/2,0);
        }


        float x = mWidth / 2;
        mRegion = new Region();
        Region clip = new Region((int)(x - 0.1),0,(int)x,mHeight *2);
        mRegion.setPath(mPath,clip);


        mPath.lineTo(mWidth,mHeight);
        mPath.lineTo(0,mHeight);
        mPath.close();
    }


    public void startAnimation(){
        ValueAnimator animator = ValueAnimator.ofFloat(0,1);
        animator.setInterpolator(new LinearInterpolator()); // 匀速
        animator.setDuration(mWaveView_duration);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 不断的修改mDx
                Float fraction = (Float) animation.getAnimatedValue();
                mDx = (int) (mWaveView_waveLength * fraction);
                if (mWaveView_rise){
                    if (mDy < mWaveView_originY + mWaveView_waveHeight){
                        mDy += 8;
                    }else {
                        animation.cancel();
                    }
                }
                postInvalidate();
            }
        });
        animator.start();
    }

}
