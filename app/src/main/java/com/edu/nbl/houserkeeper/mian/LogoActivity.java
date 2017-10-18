package com.edu.nbl.houserkeeper.mian;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.edu.nbl.houserkeeper.R;

public class LogoActivity extends BaseActivity {

    private ImageView iv_logo;//商标
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        //加载动画
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.logo_alpha);
        //设置动画监听
        animation.setAnimationListener(animationListener);
        //启动动画
        iv_logo.startAnimation(animation);
    }
    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        //动画开始
        @Override
        public void onAnimationStart(Animation animation) {

        }
        //动画结束
        @Override
        public void onAnimationEnd(Animation animation) {
            //动画结束跳转到主界面
            startActivity(MainActivity.class);
            finish();
        }
        //动画重复
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
}
