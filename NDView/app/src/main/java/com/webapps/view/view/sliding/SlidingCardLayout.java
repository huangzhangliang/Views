package com.webapps.view.view.sliding;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.webapps.view.R;
import com.webapps.view.RecyclerAdapter;

/**
 * 卡片Layout
 * Created by leon on 17/1/9.
 */

@CoordinatorLayout.DefaultBehavior(SlidingCardBehavior.class)
public class SlidingCardLayout extends FrameLayout{

    private int mHeaderViewHeight;
    private int mOutVideoPosition;
    private int mInVideoPosition;
    public boolean mToTop = true;

    public SlidingCardLayout(Context context) {
        this(context,null);
    }

    public SlidingCardLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlidingCardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.widget_card,this);
        final RecyclerView list = (RecyclerView) findViewById(R.id.list);
        TextView header = (TextView) findViewById(R.id.headerText);

        list.setAdapter(new RecyclerAdapter());
        list.setLayoutManager(new LinearLayoutManager(context));

        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.SlidingCardLayout,defStyleAttr,0);
        header.setBackgroundColor(a.getColor(R.styleable.SlidingCardLayout_android_colorBackground,0));
        header.setText(a.getText(R.styleable.SlidingCardLayout_android_text));
        a.recycle();

        initListener(list,header);

    }

    private void initListener(final RecyclerView list, TextView header) {
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int totalDy = 0;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totalDy -= dy;
                Log.v("onScrolled","totalDy=" + totalDy);
                if (totalDy == 0){
                    mToTop = true;
                }else {
                    mToTop = false;
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        list.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                LinearLayoutManager manager = (LinearLayoutManager) list.getLayoutManager();
                mInVideoPosition = manager.getPosition(view);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                LinearLayoutManager manager = (LinearLayoutManager) list.getLayoutManager();
                mOutVideoPosition = manager.getPosition(view);
                Log.v("mOutVideoPosition =",""+mOutVideoPosition);
            }
        });

        header.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mToTop = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        mToTop = false;
                        break;
                }
                return false;
            }
        });

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh){
            mHeaderViewHeight = findViewById(R.id.headerText).getMeasuredHeight();
        }
    }

    public int getHeaderHeight(){
        return mHeaderViewHeight;
    }

    public onChildListListener mOnChildListListener;

    public void setOnChildListListener(onChildListListener onChildListListener) {
        mOnChildListListener = onChildListListener;
    }

    public interface onChildListListener{
        void toTop(boolean isTo);
        void toBottom(boolean isTo);
    }

}
