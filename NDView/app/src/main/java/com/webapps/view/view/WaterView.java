package com.webapps.view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.webapps.view.R;

/**
 * QQ消息水滴效果
 * Created by leon on 16/12/25.
 */

public class WaterView extends FrameLayout {

    private Paint mPaint;

    private float DEFAULT_RADIUS = 20;
    private TextView mNewsTextView;
    private PointF mStartPoint;
    private PointF mCurPoint;
    private Path mPath;
    private boolean mTouch;
    private boolean isAnimStart;
    private float mRadius;
    private ImageView mBombImageView;

    public WaterView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mStartPoint = new PointF(100,100);
        mCurPoint = new PointF();
        mPath = new Path();

        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mNewsTextView = new TextView(getContext());
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mNewsTextView.setLayoutParams(params);
        setPadding(10,10,10,10);
        mNewsTextView.setTextColor(Color.WHITE);
        mNewsTextView.setBackgroundResource(R.drawable.tv_bg);
        mNewsTextView.setText("99+");

        mBombImageView = new ImageView(getContext());
        mBombImageView.setLayoutParams(params);
        mBombImageView.setVisibility(INVISIBLE);
//        bombImageView.setImageResource();

        mRadius = DEFAULT_RADIUS;

        addView(mNewsTextView);
        addView(mBombImageView);

    }

    public WaterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(0,0,getWidth(),getHeight(),mPaint,Canvas.ALL_SAVE_FLAG);
        if (!mTouch || isAnimStart){ // 1.默认状态
            mNewsTextView.setX(mStartPoint.x - mNewsTextView.getWidth() / 2);
            mNewsTextView.setY(mStartPoint.y - mNewsTextView.getHeight() / 2);
        }else { // 2.拖拽状态

            // 1.计算四个点，把路径保存到Path里面
            calculatePath();

            // 2.画
            canvas.drawPath(mPath,mPaint);
            canvas.drawCircle(mStartPoint.x,mStartPoint.y,mRadius,mPaint);
            canvas.drawCircle(mCurPoint.x,mCurPoint.y,mRadius,mPaint);

            mNewsTextView.setX(mCurPoint.x - mNewsTextView.getWidth() / 2);
            mNewsTextView.setY(mCurPoint.y - mNewsTextView.getHeight() / 2);
        }

//        canvas.drawCircle();

        canvas.restore(); // 恢复到之前的状态
        super.dispatchDraw(canvas);
    }

    private void calculatePath() {
        float startX = mStartPoint.x;
        float startY = mStartPoint.y;


        float dx = mCurPoint.x - startX;
        if (dx == 0){
            dx = 0.001f;
        }
        float dy = mCurPoint.y - startY;
        // a角度值
        double a = Math.atan(dy / dx);

        // r半径 -- 大小随着两个圆心的距离不断变不

        // 勾股定理
        float distance = (float) Math.sqrt(Math.pow(mCurPoint.y - startY,2) + Math.pow(mCurPoint.x - startX,2));
        mRadius = DEFAULT_RADIUS - distance / 13;
        if (mRadius < 8){
            // 结束动画
//            mBombImageView.setX(mCurPoint.x - mNewsTextView.getWidth() / 2);
//            mBombImageView.setY(mCurPoint.y - mNewsTextView.getHeight() / 2);
//            mBombImageView.setVisibility(VISIBLE);
//            AnimationDrawable animationDrawable = (AnimationDrawable) mBombImageView.getDrawable();
//            animationDrawable.start();
            mNewsTextView.setVisibility(GONE);
        }
        float offsetX = (float) (mRadius * Math.sin(a));
        float offsetY = (float) (mRadius * Math.cos(a));


        // 起始圆的第一个点
        float p0x = startX + offsetX;
        float p0y = startY - offsetY;

         // 拖拽圆的第一个点
        float p1x = mCurPoint.x + offsetX;
        float p1y = mCurPoint.y - offsetY;

         // 拖拽圆的第二个点
        float p2x = mCurPoint.x - offsetX;
        float p2y = mCurPoint.y + offsetY;

        // 起始圆的第二个点
        float p3x = startX - offsetX;
        float p3y = startY + offsetY;

         // 锚点
        float anchorX = (startX + mCurPoint.x) / 2;
        float anchorY = (startY + mCurPoint.y) / 2;

        mPath.reset();
        mPath.moveTo(p0x,p0y); // 落笔点
        mPath.quadTo(anchorX,anchorY,p1x,p1y); // 画曲线
        mPath.lineTo(p2x,p2y); // 画直线
        mPath.quadTo(anchorX,anchorY,p3x,p3y); // 画曲线
//        mPath.lineTo(p0x,p0y);
        mPath.close(); // 封闭
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mTouch = true;
                // 判断触摸点是否在ImageView中
                Rect rect = new Rect();
                int [] location = new int[2];
                mNewsTextView.getLocationOnScreen(location);
                rect.left = location[0];
                rect.top = location[1];
                rect.right = mNewsTextView.getWidth() + location[0];
                rect.bottom = mNewsTextView.getHeight() + location[1];
                if (rect.contains((int)event.getRawX(),(int)event.getRawY())){
                    mTouch = true;
                }

                break;
            case MotionEvent.ACTION_UP:
                // 松开
                mTouch = false;
                break;
        }

        postInvalidate();

        mCurPoint.set(event.getX(),event.getY());

        return true;
    }
}
