package com.webapps.view.anim.discroll;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by leon on 16/12/28.
 */

public class DiscrollFrameLayout extends FrameLayout implements DiscrollInterface{


    public static final int TRANSLATION_FROM_TOP = 0x01;
    public static final int TRANSLATION_FROM_BOTTOM = 0x02;
    public static final int TRANSLATION_FROM_LEFT = 0x04;
    public static final int TRANSLATION_FROM_RIGHT = 0x08;

    private static ArgbEvaluator sArgbEvaluator = new ArgbEvaluator();

    private int mDiscrollvcFromBgColor;  // 颜色动画起始值
    private int mDiscrollvcToBgColor; // 颜色动画最终值
    private boolean mDiscrollvcAlpha; // 是否需要透明度动画
    private int mDiscrollvcTranslation; // 平移值
    private boolean mDiscrollvcScaleX; // 是否需要X轴方向缩放
    private boolean mDiscrollvcScaleY; // 是否需要Y轴方向缩放
    private int mHeight; // 高度
    private int mWidth; // 宽度


    public DiscrollFrameLayout(Context context) {
        super(context);
    }

    public DiscrollFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        onResetDiscroll();
    }

    public void setDiscrollvcFromBgColor(int discrollvcFromBgColor) {
        mDiscrollvcFromBgColor = discrollvcFromBgColor;
    }


    public void setDiscrollvcToBgColor(int discrollvcToBgColor) {
        mDiscrollvcToBgColor = discrollvcToBgColor;
    }

    public void setDiscrollvcAlpha(boolean discrollvcAlpha) {
        mDiscrollvcAlpha = discrollvcAlpha;
    }

    public void setDiscrollvcTranslation(int discrollvcTranslation) {
        mDiscrollvcTranslation = discrollvcTranslation;
    }

    public void setDiscrollvcScaleX(boolean discrollvcScaleX) {
        mDiscrollvcScaleX = discrollvcScaleX;
    }

    public void setDiscrollvcScaleY(boolean discrollvcScaleY) {
        mDiscrollvcScaleY = discrollvcScaleY;
    }

    private boolean isDiscrollTranslationFrom(int translationMask){
        if (mDiscrollvcTranslation == -1){
            return false;
        }
        return (mDiscrollvcTranslation & translationMask) == translationMask;
    }

    @Override
    public void onDiscroll(float ratio) {
        // 根据百分比执行动画
        if (mDiscrollvcAlpha) {
            setAlpha(ratio);
        }
        if (mDiscrollvcScaleX) {
            setScaleX(ratio);
        }
        if (mDiscrollvcScaleY) {
            setScaleY(ratio);
        }

        if (isDiscrollTranslationFrom(TRANSLATION_FROM_BOTTOM)){
            setTranslationY(mHeight * (1 - ratio)); //mHeight- - >0(0代表恢复到本来原来的位置)
        }
        if (isDiscrollTranslationFrom(TRANSLATION_FROM_TOP)){
            setTranslationY(-mHeight * (1 - ratio)); //-mHeight- - >0(0代表恢复到本来原来的位置)
        }
        if (isDiscrollTranslationFrom(TRANSLATION_FROM_LEFT)){
            setTranslationX(-mWidth * (1 - ratio)); //mWidth- - >0(0代表恢复到本来原来的位置)
        }
        if (isDiscrollTranslationFrom(TRANSLATION_FROM_RIGHT)){
            setTranslationX(mWidth * (1 - ratio)); //-mWidth- - >0(0代表恢复到本来原来的位置)
        }

        // 判断从什么颜色到什么颜色
        if (mDiscrollvcFromBgColor != -1 && mDiscrollvcToBgColor != -1){
            setBackgroundColor((int) sArgbEvaluator.evaluate(ratio,mDiscrollvcFromBgColor,mDiscrollvcToBgColor));;
        }


    }

    @Override
    public void onResetDiscroll() {
        if (mDiscrollvcAlpha) {
            setAlpha(0);
        }
        if (mDiscrollvcScaleX) {
            setScaleX(0);
        }
        if (mDiscrollvcScaleY) {
            setScaleY(0);
        }

        if (isDiscrollTranslationFrom(TRANSLATION_FROM_BOTTOM)){
            setTranslationY(mHeight); //mHeight- - >0(0代表恢复到本来原来的位置)
        }
        if (isDiscrollTranslationFrom(TRANSLATION_FROM_TOP)){
            setTranslationY(-mHeight); //-mHeight- - >0(0代表恢复到本来原来的位置)
        }
        if (isDiscrollTranslationFrom(TRANSLATION_FROM_LEFT)){
            setTranslationX(-mWidth); //mWidth- - >0(0代表恢复到本来原来的位置)
        }
        if (isDiscrollTranslationFrom(TRANSLATION_FROM_RIGHT)){
            setTranslationX(mWidth); //-mWidth- - >0(0代表恢复到本来原来的位置)
        }

    }
}
