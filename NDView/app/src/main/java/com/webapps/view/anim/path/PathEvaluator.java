package com.webapps.view.anim.path;

import android.animation.TypeEvaluator;

/**
 * 路径估值器
 */

public class PathEvaluator implements TypeEvaluator<PathPoint> {

    @Override
    public PathPoint evaluate(float t, PathPoint startValue, PathPoint endValue) {
        // fraction t:百分比 0~1

        float x = 0,y = 0;

        if (endValue.mOperation == PathPoint.CURVE){
            // 贝塞尔曲线
            float oneMinusT = 1 - t;
            x = oneMinusT * oneMinusT * oneMinusT * startValue.mX +
                    3 * oneMinusT * oneMinusT * t * endValue.mControl0X +
                    3 * oneMinusT * t * t * endValue.mControl1X +
                    t * t * t * endValue.mX;
            y = oneMinusT * oneMinusT * oneMinusT * startValue.mY +
                    3 * oneMinusT * oneMinusT * t * endValue.mControl0Y +
                    3 * oneMinusT * t * t * endValue.mControl1Y +
                    t * t * t * endValue.mY;

        }else if (endValue.mOperation == PathPoint.LINE){
            x = startValue.mX + t * (endValue.mX - startValue.mX);
            y = startValue.mY + t * (endValue.mY - startValue.mY);
        }else {
            // moveTo
            x = endValue.mX;
            y = endValue.mY;
        }

        return PathPoint.moveTo(x,y);
    }

}
