package com.edu.nbl.houserkeeper.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edu.nbl.houserkeeper.R;

/**
 * Created by Administrator on 2017/8/3.
 * 自定义视图
 * 1.继承View
 * 2.添加构造方法
 */

public class ActionBarView extends LinearLayout {
    //声明控件
    private ImageView iv_left, iv_right;//左右的ImageView按钮
    private TextView tv_title;//中间标题

    //当初始化ActionBarView的时候调用
    public ActionBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.layout_actionbar, this);//将layout_actionbar添加到ActionBarView此线性布局中
        iv_left = (ImageView) findViewById(R.id.iv_left);//左边按钮
        iv_right = (ImageView) findViewById(R.id.iv_right);//右边按钮
        tv_title = (TextView) findViewById(R.id.tv_title);//中间标题
    }
    //给左右按钮添加监听器

    /**
     * @param title     此为ActionBar的中间标题
     * @param leftResID 此为ActionBar左边的ImageView
     * @param rightID   此为ActionBar右边的ImageView
     * @param listener  给左右按钮设置的监听器
     */
    public void initActionBar(String title, int leftResID, int rightID, OnClickListener listener) {
        //设置标题
        tv_title.setText(title);
        if (rightID == -1) {//传过来为-1，就不显示图片
            iv_right.setVisibility(View.INVISIBLE);//隐藏图片
        } else {//如果传过来的图片补位-1就显示图片
            iv_right.setVisibility(View.VISIBLE);//显示图片
            iv_right.setImageResource(rightID);
        }
        iv_left.setImageResource(leftResID);//设置左侧图片资源
        //设置监听
        iv_left.setOnClickListener(listener);
        iv_right.setOnClickListener(listener);
    }
}
