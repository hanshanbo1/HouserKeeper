package com.edu.nbl.houserkeeper.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.edu.nbl.houserkeeper.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/8/16.
 * //1.继承View
 * //2.创建带属性的构造方法
 * //3.onMeasure测绘
 * //4.onDraw
 */
//1.继承View
public class ClearArcView extends View {
    private Paint paint = new Paint();//画笔
    private RectF oval;//限制圆
    private final int START_ANGLE = -90;//开始角度
    private int sweepAngle = 0;//圆弧要画的弧度
    private int state = 0;//有前进（加角度）和后退（减角度）两种状态 0：回退状态 1：前进状态
    private int arcColor = 0xFFF8C00;//橘黄色底色
    private int[] back = new int[]{-5, -5, -5, -5};//回退要减的角度
    private int[] forward = new int[]{5, 5, 5, 5};//前进要加的角度
    private int backIndex;//后退下标
    private int forwardIndex;//前进下标
    private boolean isRunning;//如果是正在运行点击是没有效果的
    private int angle;

    //2.创建带属性的构造方法
    public ClearArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        arcColor = getResources().getColor(R.color.base_color);//橘黄色背景
        //一来就画360°
        setAngle(360);
    }

    public void setAngle(int angle) {
        sweepAngle = angle;//存储角度
        postInvalidate();//重绘
        isRunning = false;//不是正在运行
    }

    //设置圆环角度，带动画
    public void setAngleNithAnim(final int angle) {//目标角度
        if (isRunning) {
            return;//结束方法，下面代码不会运行
        }
        isRunning = true;//表示正在运行
        state = 0;//有前进（加角度）和后退（减角度）两种状态 0：回退状态 1：前进状态
        final Timer timer = new Timer();//定时器
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //每隔固定时间加或减角度
                //判断状态
                switch (state) {
                    case 0://0：回退状态
                        //减角度 back = new int[]{-5,-5,-5,-5};//回退要减的角度
                        sweepAngle = sweepAngle + back[backIndex++];//没26毫秒减5°
                        //控制下标越界
                        if (backIndex >= back.length) {//下标>=4
                            backIndex = back.length - 1;//下标再这回来，取3 防止下标越界
                        }
                        //减完角度要画一次
                        postInvalidate();//重绘圆弧
                        if (sweepAngle<=0){//当角度小于0时，
                            sweepAngle = 0;//重置角度
                            state = 1;//改变为前进状态
                            backIndex = 0;
                        }
                        break;
                    case 1://1：前进状态 加角度
                        //forward = new int[]{5,5,5,5};//前进要加的角度
                        sweepAngle += forward[forwardIndex++];//没26毫秒加5°
                        if (forwardIndex >= forward.length) {//下标>=4
                            forwardIndex = forward.length - 1;//下标再这回来，取3 防止下标越界
                        }
                        //加完角度在画一次
                        postInvalidate();//重绘圆弧
                        if (sweepAngle >= angle) {//当要画的角度达到目标角度时，就不用再画了。
                            sweepAngle = angle;//重置画的角度
                            timer.cancel();//达到目标角度，取消定时器
                            forwardIndex = 0;//重置下标
                            isRunning = false;//运行已经结束
                            break;
                        }
                }
            }
        };
        //执行任务
        timer.schedule(task, 26, 26);
    }

    //3.onMeasure测绘
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthView = MeasureSpec.getSize(widthMeasureSpec);//获取布局中定义的宽
        int heightView = MeasureSpec.getSize(heightMeasureSpec);//获取布局中定义的高
        oval = new RectF(0, 0, widthView, heightView);//画一个矩形
        setMeasuredDimension(widthView, heightView);//告知底层要画的矩形
    }

    //4.onDraw画圆弧
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(arcColor);//画笔颜色橘黄色
        paint.setAntiAlias(true);//抗锯齿
        canvas.drawArc(oval, START_ANGLE, sweepAngle, true, paint);
    }
}
