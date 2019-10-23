package com.forest.treeviewapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Iterator;
import java.util.LinkedList;

public class TreeView extends View {
    //分析
    //拆分
    //存放所有需要绘制的枝干
    private LinkedList<Branch> growingBranches;
    private Paint paint;
    private Bitmap bitmap;
    private Canvas treeCanvas;
    //树的缩放大小
    private int scaleFraction = 1;


    public TreeView(Context context) {
        this(context, null);
    }

    public TreeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TreeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化
        growingBranches = new LinkedList<>();
        //添加枝干
        growingBranches.add(getBranch());

        paint = new Paint();

        paint.setColor(0xff7D3722);
        paint.setTextSize(50);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        treeCanvas = new Canvas(bitmap);
    }

    public Branch getBranch() {
        int[][] data = new int[][]{
                {0, -1, 217, 490, 252, 60, 182, 10, 30, 100},
                {1, 0, 222, 310, 137, 227, 22, 210, 13, 100},
                {2, 1, 132, 245, 116, 240, 76, 205, 2, 40},
                {3, 0, 232, 255, 282, 166, 362, 155, 12, 100},
                {4, 3, 260, 210, 330, 219, 343, 236, 3, 80},
                {5, 0, 217, 91, 219, 58, 216, 27, 3, 40},
                {6, 0, 228, 207, 95, 57, 10, 54, 9, 80},
                {7, 6, 109, 96, 65, 63, 53, 15, 2, 40},
                {8, 6, 180, 155, 117, 125, 77, 140, 4, 60},
                {9, 0, 228, 167, 290, 62, 360, 31, 6, 100},
                {10, 9, 272, 103, 328, 87, 330, 81, 2, 80}
        };
        //获取枝干的总数
        int length = data.length;
        Branch[] branches = new Branch[length];

        for (int i = 0; i < length; i++) {
            branches[i] = new Branch(data[i]);
            int parentId = data[i][1];
            if (parentId != -1) {
                //有父节点
                branches[parentId].addChild(branches[i]);
            }
        }
        return branches[0];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas进行绘制能否保存上一次的绘制结果
        // 不能 ViewRootImpl drawSoftware(...)
        //canvas.drawColor(0, PorterDuff.Mode.CLEAR);清空了，所以不会保存
        //怎么样保存上一步的内容

        //绘制单次新增内容
        drawBranches();
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    private void drawBranches() {
        scaleFraction = 2;
        if (!growingBranches.isEmpty()) {
            //遍历
            treeCanvas.save();
            treeCanvas.translate(getWidth() / 2 - 217 * scaleFraction, getHeight() - 490 * scaleFraction);
            LinkedList<Branch> tempBranchs = null;
            Iterator<Branch> iterator = growingBranches.iterator();
            while (iterator.hasNext()) {
                Branch branch = iterator.next();
                if (!branch.grow(treeCanvas, paint, scaleFraction)) {
                    iterator.remove();
                    //判断是否存在分支
                    if (branch.childList != null) {
                        //有分支需要继续处理
                        if (tempBranchs == null) {
                            tempBranchs = branch.childList;
                        } else {
                            tempBranchs.addAll(branch.childList);
                        }
                    }
                }

            }

            treeCanvas.restore();
            //判断是否有分支需要继续处理
            if (tempBranchs != null) {
                growingBranches.addAll(tempBranchs);
            }
        }
        if (!growingBranches.isEmpty()) {

            //继续绘制
            invalidate();
        }
    }
}
