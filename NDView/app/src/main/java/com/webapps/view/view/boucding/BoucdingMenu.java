package com.webapps.view.view.boucding;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.webapps.view.R;

/**
 * 跳跃的菜单
 * Created by leon on 17/1/4.
 */

public class BoucdingMenu {
    private static ViewGroup mParentVG;
    private View mRooView;
    private BoucdingView mBoucdingView;
    private RecyclerView mRecyclerView;


    public BoucdingMenu(View view,int resId){
        // 1.找到系统里面的FarmeLayout android:id="@android:id/content"
        mParentVG = findRootParent(view);
        // 渲染布局
        mRooView = LayoutInflater.from(view.getContext()).inflate(resId, null);
        mBoucdingView = (BoucdingView) mRooView.findViewById(R.id.bv);
//        mBoucdingView.setAnimatorListener(new AnimationListener());
//        mRecyclerView = (RecyclerView) mRooView.findViewById(R.id.rv);
    }


    public static BoucdingMenu makeMenu(View view,int resId){
        return new BoucdingMenu(view,resId);
    }



    public void dismiss(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(mRooView,
                "translationY",
                0,mRooView.getHeight());
        animator.setDuration(400);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 移动
                mParentVG.removeView(mRooView);
                mRooView = null;
            }
        });
        animator.start();
    }


    /**
     * 找到系统里面的FarmeLayout
     * @return
     */
    private static ViewGroup findRootParent(View view) {
        do {
            if (view instanceof FrameLayout){
                if (view.getId() == android.R.id.content){
                    return (ViewGroup) view;
                }

                if (view != null){
                    ViewParent parent = view.getParent();
                    view = parent instanceof View ? (View) parent :null;
                }

            }
        }while (view != null);
        return null;
    }


    public BoucdingMenu show(){
        // 2.往地FarmeLayout里面添加自定义控件
        if (mRooView.getParent() != null){
            mParentVG.removeView(mRooView);
        }
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mParentVG.addView(mRooView,lp);
        // 3.开启动画
        mBoucdingView.show();
        return this;
    }

    /**
     * 是否显示出来
     * @return
     */
    public boolean isShow() {
        return mRooView != null;
    }


    class AnimationListener implements BoucdingView.OnAnimationListener{

        @Override
        public void onStart() {

        }

        @Override
        public void onEnd() {

        }

        @Override
        public void onContentShow() {
            // 显示数据
//            mRecyclerView.setAdapter(adapter);
            // 展现动画
//            mRecyclerView.scheduleLayoutAnimation();
        }
    }
}
