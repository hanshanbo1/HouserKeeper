package com.edu.nbl.houserkeeper.mian;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.edu.nbl.houserkeeper.R;
import com.edu.nbl.houserkeeper.view.ActionBarView;

/**
 * Created by Administrator on 2017/8/4.
 */

public class BaseActivity extends AppCompatActivity{
    private ActionBarView actionBarView;

    //不带动画的跳转
    public void startActivity(Class<?> name){
        Intent intent = new Intent(this,name);
        startActivity(intent);
    }
    //动画的跳转
    public void startActivity(Class<?> name,int enterAnim,int extiAnim){//SecondActivity.class
        Intent intent = new Intent(this,name);
        startActivity(intent);
        overridePendingTransition(enterAnim,extiAnim);
    }
    //初始化ActionBar
    public void initActiongBar(String title, int leftResID, int rightRestID, View.OnClickListener listener){
        actionBarView = (ActionBarView) findViewById(R.id.actionBar);
        actionBarView.initActionBar(title,leftResID,rightRestID,listener);
    }
    protected Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            myHandleMessage(msg);
        }
    };
    protected void myHandleMessage(Message msg) {
    }
}
