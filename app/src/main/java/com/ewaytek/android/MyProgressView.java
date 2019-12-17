package com.ewaytek.android;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class MyProgressView extends View {

    private Paint paint;//画笔
    private ObjectAnimator animator;
    private Context context;
    private float radius = 0;
    private float progress = 0;
    private int color = Color.BLACK;
    private long duration;
    private long currentProgress;//当前进度值


    public MyProgressView(Context context) {
        super(context);
        this.context = context;
        initView();
    }


    public MyProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public MyProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        //初始化画笔
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        animator = new ObjectAnimator();
        //设置动画属性
        animator.setPropertyName("progress");
        //设置执行动画的View
        animator.setTarget(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画笔属性
        paint.setAntiAlias(true);                  //设置画笔为无锯齿
        paint.setColor(color);                    //设置画笔颜色
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);
        //圆角形状设置到画布
        RectF rectF = new RectF(0, 0, progress, getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    public float getMyProgress() {
        return progress;
    }

    /**
     * 返回属性动画实例，可用于动画进度监听做后续操作
     * @return
     */
    public ObjectAnimator getAnimator() {
        return animator;
    }

    public void startAnim() {
        if (animator.isRunning()) animator.end();
        //设置进度数组，  0 - max
        animator.setFloatValues(0, progress);
        //设置动画时间
        animator.setDuration(duration);
        //动画开启
        animator.start();
    }

}
