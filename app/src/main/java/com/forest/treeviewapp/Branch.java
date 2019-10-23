package com.forest.treeviewapp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;

import java.util.LinkedList;

/**
 * 树干
 */
public class Branch {

    //枝干颜色
    private static int branchColor = 0xff7D3722;

    //二阶贝塞尔(三个控制点)
    private PointF[] cp = new PointF[3];

    private int maxLength;
    private int currentLength;
    //枝干的粗细
    private float radius;
    //一共分多少步去绘制
    private float part;

    //每一步绘制的位置
    private float growX;
    private float growY;

    //存储分支
    LinkedList<Branch> childList;

    //id,parentId,bezier control points(3 points,in 6 columns),max radius,length
    //{0,-1,217,490,252,60,182,10,30,100},


    public Branch(int data[]) {
        cp[0] = new PointF(data[2], data[3]);
        cp[1] = new PointF(data[4], data[5]);
        cp[2] = new PointF(data[6], data[7]);
        radius = data[8];
        maxLength = data[9];

        part = 1.0f / maxLength;
    }

    //添加分支
    public void addChild(Branch branch) {
        if (childList == null) {
            childList = new LinkedList<>();
        }
        childList.add(branch);
    }

    public boolean grow(Canvas canvas, Paint paint, int scaleFraction) {
        if (currentLength < maxLength) {
            //需要绘制当前枝干
            //计算
            bezier(part * currentLength);
            //绘制
            draw(canvas, paint, scaleFraction);
            currentLength++;
            radius *= 0.97f;
            return true;
        } else {
            return false;
        }


    }

    private void draw(Canvas canvas, Paint paint, int scaleFraction) {

        paint.setColor(branchColor);
        canvas.save();
        canvas.scale(scaleFraction, scaleFraction);
        canvas.drawCircle(growX, growY, radius, paint);
        canvas.restore();
    }

    private void bezier(float t) {

//        Path path = new Path();
//        path.quadTo();
//        PathMeasure measure = new PathMeasure(path,false);
//        measure.

        float c0 = (1 - t) * (1 - t);
        float c1 = 2 * t * (1 - t);
        float c2 = t * t;

        growX = c0 * cp[0].x + c1 * cp[1].x + c2 * cp[2].x;
        growY = c0 * cp[0].y + c1 * cp[1].y + c2 * cp[2].y;


    }

}
