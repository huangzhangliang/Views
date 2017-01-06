package com.webapps.view.anim.path;

/**
 * Created by leon on 16/12/30.
 */

public class PathPoint {

    public int mOperation;
    public float mX,mY;
    public float mControl0X,mControl0Y;
    public float mControl1X,mControl1Y;

    static final int MOVE = 0;
    static final int LINE = 1;
    static final int CURVE = 2;


    private PathPoint(int operation, float x, float y) {
        mOperation = operation;
        mX = x;
        mY = y;
    }

    private PathPoint(float control0X, float control0Y, float control1X, float control1Y, float x, float y) {
        mOperation = CURVE;
        mControl0X = control0X;
        mControl0Y = control0Y;
        mControl1X = control1X;
        mControl1Y = control1Y;
        mX = x;
        mY = y;
    }


    static PathPoint lineTo(float x, float y){
        return new PathPoint(LINE,x,y);
    }


    static PathPoint moveTo(float x, float y){
        return new PathPoint(MOVE,x,y);
    }


    static PathPoint curveTo(float x, float y, float control0X, float control0Y, float control1X, float control1Y){
        return new PathPoint(control0X,control0Y,control1X,control1Y,x,y);
    }



}
