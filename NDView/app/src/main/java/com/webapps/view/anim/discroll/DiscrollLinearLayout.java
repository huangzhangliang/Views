package com.webapps.view.anim.discroll;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.webapps.view.R;

/**
 * Created by leon on 16/12/28.
 */

public class DiscrollLinearLayout extends LinearLayout {

    public DiscrollLinearLayout(Context context) {
        super(context);
    }

    public DiscrollLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        // 获取xml传出来的参数
        return new DiscrollLayoutParams(getContext(),attrs);
    }

    private boolean isDiscrollvable(DiscrollLayoutParams params){
        return params.mDiscrollvcAlpha ||
                params.mDiscrollvcScaleX ||
                params.mDiscrollvcScaleY ||
                params.mDiscrollvcTranslation != -1 ||
                (params.mDiscrollvcFromBgColor != -1 &&
                params.mDiscrollvcToBgColor != -1);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        // 自动在外包裹一个容器
        DiscrollLayoutParams dl = (DiscrollLayoutParams) params;
        // 是否有自定义属性
        if (!isDiscrollvable(dl)){
            super.addView(child,index,params);
        }else {
            DiscrollFrameLayout df  = new DiscrollFrameLayout(getContext());
            df.setDiscrollvcAlpha(dl.mDiscrollvcAlpha);
            df.setDiscrollvcFromBgColor(dl.mDiscrollvcFromBgColor);
            df.setDiscrollvcToBgColor(dl.mDiscrollvcToBgColor);
            df.setDiscrollvcScaleX(dl.mDiscrollvcScaleX);
            df.setDiscrollvcScaleY(dl.mDiscrollvcScaleY);
            df.setDiscrollvcTranslation(dl.mDiscrollvcTranslation);
            df.addView(child);
            super.addView(df, index, params);
        }
    }


    public static class DiscrollLayoutParams extends LayoutParams{

        int mDiscrollvcFromBgColor;
        int mDiscrollvcToBgColor;
        boolean mDiscrollvcAlpha; // 是否需要透明度动画
        int mDiscrollvcTranslation; // 平移值
        boolean mDiscrollvcScaleX; // 是否需要X轴方向缩放
        boolean mDiscrollvcScaleY; // 是否需要Y轴方向缩放

        DiscrollLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.DiscrollView_layoutParams);
            mDiscrollvcFromBgColor = a.getColor(R.styleable.DiscrollView_layoutParams_discrollvc_fromBgColor,-1);
            mDiscrollvcToBgColor = a.getColor(R.styleable.DiscrollView_layoutParams_discrollvc_toBgColor,-1);
            mDiscrollvcAlpha = a.getBoolean(R.styleable.DiscrollView_layoutParams_discrollvc_alpha,false);
            mDiscrollvcScaleX = a.getBoolean(R.styleable.DiscrollView_layoutParams_discrollvc_scaleX,false);
            mDiscrollvcScaleY = a.getBoolean(R.styleable.DiscrollView_layoutParams_discrollvc_scaleY,false);
            mDiscrollvcTranslation = a.getInt(R.styleable.DiscrollView_layoutParams_discrollvc_translation,-1);
        }

    }

}

