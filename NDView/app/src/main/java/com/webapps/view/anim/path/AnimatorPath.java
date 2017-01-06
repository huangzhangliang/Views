package com.webapps.view.anim.path;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 保存一系列的路径动作
 */

public class AnimatorPath {

    ArrayList<PathPoint> mPoints = new ArrayList<>();

    void lineTo(float x,float y){
        mPoints.add(PathPoint.lineTo(x,y));
    }

    public void curveTo(float c0x, float c0y, float c1x, float c2y, float x, float y){
        mPoints.add(PathPoint.curveTo(c0x,c0y,c1x,c2y,x,y));
    }

    public void moveTo(float x, float y){
        mPoints.add(PathPoint.moveTo(x,y));
    }


    public Collection<PathPoint> getPoints(){
        return mPoints;
    }


}
