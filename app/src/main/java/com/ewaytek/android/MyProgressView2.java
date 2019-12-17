package com.ewaytek.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class MyProgressView2 extends View {

    private Paint mPaint = null ;
    private Paint mPaint2 = null;
    private int max=100;
    private int progress=0;
    private int NORMAL_TYPE=1;
    private int ALERT_TYPE=2;
    //private ObjectAnimator animator;

    public MyProgressView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        //initView();
    }

    public MyProgressView2(Context context) {
        super(context);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        //initView();
    }

    /*private void initView() {
        animator = new ObjectAnimator();
        //设置动画属性
        animator.setPropertyName("progress");
        //设置执行动画的View
        animator.setTarget(this);
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Point left_top = new Point(0,0);
        Point right_bottom = new Point(getWidth(),getHeight());
        double rate = (double)progress/(double)max;
        drawProgressBar(canvas, left_top, right_bottom, rate);
    }
    public void setProgress(int progress){
        this.progress = progress;
        invalidate();//使得onDraw重绘
    }
    private void drawProgressBar(Canvas canvas, Point left_top, Point right_bottom, double rate){
        int width = 5;
        int rad = 10;
        mPaint.setColor(Color.BLACK);//画边框
        mPaint.setStyle(Paint.Style.STROKE);//设置空心
        mPaint.setStrokeWidth(width);
        RectF rectF = new RectF(left_top.x, left_top.y, right_bottom.x, right_bottom.y);
        canvas.drawRoundRect(rectF, rad, rad, mPaint);

        mPaint2.setColor(Color.RED);//画progress bar
        mPaint2.setStyle(Paint.Style.FILL);
        int x_end = (int)(right_bottom.x * rate);
        RectF rectF2 = new RectF(left_top.x + width, left_top.y + width, x_end - width, right_bottom.y - width);
        canvas.drawRoundRect(rectF2, rad, rad, mPaint2);
    }

    /*public void startAnim() {
        if (animator.isRunning()) animator.end();
        //设置进度数组，  0 - max
        animator.setFloatValues(0, progress);
        animator.setDuration(50000);
        animator.start();
    }*/

}
