package com.webapps.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.webapps.view.R;
import com.webapps.view.anim.path.AnimatorPath;
import com.webapps.view.anim.path.PathEvaluator;
import com.webapps.view.anim.path.PathPoint;
import com.webapps.view.databinding.ActivityPathAnimatorBinding;

/**
 * Created by leon on 17/1/3.
 */

public class AnimatorPathActivity extends AppCompatActivity {

    View mFab;
    ActivityPathAnimatorBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_path_animator);
        mFab = mActivityMainBinding.cv;
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFabPressed(v);
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */


//    public PathPoint mPathPoint;


    private boolean mRevealFlag;

    protected void onFabPressed(View view){
        final float startX = view.getX();
        AnimatorPath animatorPath = new AnimatorPath();
        animatorPath.moveTo(0,0);
        animatorPath.curveTo(-200,200,-400,0,-600,50);

        ObjectAnimator oa = ObjectAnimator.ofObject(
                this,
                "pathPoint",// 反射
                new PathEvaluator(),
                animatorPath.getPoints().toArray());

        oa.setDuration(700);
//        oa.setRepeatCount(ValueAnimator.INFINITE); // 无限次
//        oa.setRepeatMode(ValueAnimator.REVERSE); // 反转动画
        oa.start();

        oa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 监听到了某个位置的时候就开始执行
                if (Math.abs(startX - mFab.getX()) > 200){
                    if (!mRevealFlag){
                        mRevealFlag = true;
//                        mFab.setImageDrawable(new BitmapDrawable());
                        mFab.animate()
                                .scaleXBy(13) // 相对缩放
                                .scaleYBy(13)
                                .setDuration(700)
                                .setListener(endRevealListener);
                    }
                }
            }
        });


    }



    private AnimatorListenerAdapter endRevealListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            // 动画执行完成

        }
    };

    public void setPathPoint(PathPoint newLoc){
        mFab.setTranslationX(newLoc.mX);
        if (mRevealFlag){
            mFab.setTranslationY(newLoc.mY - 100);
        }
        mFab.setTranslationY(newLoc.mY);
    }


}
