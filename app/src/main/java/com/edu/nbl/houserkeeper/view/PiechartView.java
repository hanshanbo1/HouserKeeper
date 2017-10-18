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
 * Created by Administrator on 2017/8/10.
 * 自定义饼形图，需要画三个圆弧
 * 1.继承View
 * 2.添加有效的构造方法
 * 3.重写oMneasure方法
 * 4.重写onDraw方法
 */

public class PiechartView extends View{
    private Paint paint;//画笔
    private RectF oval;//限制圆形大小
    private float proportionPhone;//手机内置空间所占比例
    private float proportionSD;//手机外置空间所占比例
    //要把比例换算成角度
    private float piechartAnglePhone = 0;//手机内置空间的角度
    private float piechartAngleSD = 0;//手机外置存储空间的角度

    private int phoneColor = 0;//手机内置空间的颜色  蓝色
    private int sdColor = 0;//手机外置空间的颜色  绿色
    private int baseColor = 0;//饼形图底部颜色 橘黄色
    private float phoneTargetAngle;
    private float sdcardTargetAngle;

    public PiechartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();//初始化画笔
        phoneColor = context.getResources().getColor(R.color.phone_color);//手机内置空间的颜色  蓝色
        sdColor = context.getResources().getColor(R.color.sd_color);//手机外置空间的颜色  绿色
        baseColor = context.getResources().getColor(R.color.base_color);//饼形图底部颜色 橘黄色
    }
    public void setPiechartProportion(float proportionPhone,float proportionSD){
        //初始化手机内置空间的比例
        this.proportionPhone = proportionPhone;//例如：0.5
        //初始化手机外置空间的比例
        this.proportionSD =proportionSD;//例如：0.5
        //把比例换成角度
        float phoneTargetAngle = 360*this.proportionPhone;//0.5*360=180
        float sdcardTargetAngle = 360*this.proportionSD;//0.5*360=180
        //初始化要绘制的角度
        piechartAnglePhone = phoneTargetAngle;//要绘制180
        piechartAngleSD = sdcardTargetAngle;//要绘制180
        postInvalidate();//重绘
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);//获取要绘制的宽
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);//获取要绘制的高
        oval = new RectF(0,0,viewWidth,viewHeight);//限制圆形的大小
        setMeasuredDimension(viewWidth,viewHeight);//将测量的尺寸告知系统底层
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setAntiAlias(true);//设置抗锯齿
        //先画底部
        paint.setColor(baseColor);//设置画笔颜色
        canvas.drawArc(oval,-90,360,true,paint);//画底部黄色圆环
        //画手机内置空间蓝色
        paint.setColor(phoneColor);//蓝色
        canvas.drawArc(oval,-90,piechartAnglePhone,true,paint);
        //外置存储空间的圆弧 绿色
        paint.setColor(sdColor);//绿色
        canvas.drawArc(oval,-90+piechartAnglePhone,piechartAngleSD,true,paint);
    }
    //带动画，画饼形图
    public void setPiechartProportionWithAnim(float proportionPhone,float proportionSD){
        this.proportionPhone = proportionPhone;//例如：0.5
        this.proportionSD = proportionSD;//例如：0.5
        //换算成角度
        phoneTargetAngle = 360*this.proportionPhone;//0.5*360=180
        sdcardTargetAngle = 360*this.proportionSD;//0.5*360=180
        //写一个循环（定时器），每26毫秒执行一次，每一次加4度
        final Timer timer = new Timer();
        //初始化一个定时任务
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //执行任务，每26毫秒加4度
                piechartAnglePhone += 4;//手机内置存储的角度加4度
                piechartAngleSD += 4;//手机外置存储加4度
                //加一点画一点
                postInvalidate();//重绘
                if (piechartAnglePhone>=phoneTargetAngle){//例如当我们增加的角度>=180
                    piechartAnglePhone = phoneTargetAngle;//将角度固定下来 180
                }
                if (piechartAngleSD>=sdcardTargetAngle){
                    piechartAngleSD = sdcardTargetAngle;//例如 将角度固定 180
                }
                //表示两个圆弧都画完了
                if (piechartAnglePhone==phoneTargetAngle&&piechartAngleSD==sdcardTargetAngle){
                    timer.cancel();//关闭定时器
                }
            }
        };
        timer.schedule(timerTask,28,26);//timerTask是要调度的任务，28第一次执行任务需要的时间，26下一次之后每26毫秒执行一次run方法
    }
}
