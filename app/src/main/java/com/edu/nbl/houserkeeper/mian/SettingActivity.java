package com.edu.nbl.houserkeeper.mian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.edu.nbl.houserkeeper.R;
import com.edu.nbl.houserkeeper.utils.NotificationUtils;

//BaseActivity中有对ActionBar初始化
public class SettingActivity extends BaseActivity {
    private ToggleButton tb_notify;
    private RelativeLayout rl_help, rl_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //初始化ActionBar设置标题和左右图片
        initActiongBar("设置", R.drawable.btn_homeasup_default, -1, listener);
        tb_notify = (ToggleButton) findViewById(R.id.tb_notify);
        //获取通知按钮之前储存的状态
        tb_notify.setChecked(NotificationUtils.getOpenNotification(this));
        tb_notify.setOnCheckedChangeListener(onCheckedChangeListener);
        //初始化引导和关于界面
        rl_help = (RelativeLayout) findViewById(R.id.rl_help);
        rl_about = (RelativeLayout) findViewById(R.id.rl_about);
        rl_help.setOnClickListener(listener);
        rl_about.setOnClickListener(listener);
    }

    //判断ToggleButton是否选中
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        //isChecked为true表示选中，isChecked为false表示没有选中，无色按钮
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {//选中状态，发送通知
                NotificationUtils.showNotification(SettingActivity.this);
            } else {//未选中状态，取消通知
                NotificationUtils.cancleNotification(SettingActivity.this);
            }
        }
    };
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {//获取点击控件
                case R.id.iv_left://直接销毁当前页，显示主界面
                    finish();
                    break;
                case R.id.rl_help://进入引导页
                    //1.构建SharedPreferences，创建lead_fig.xml文件
                    SharedPreferences preferences = getSharedPreferences("lead_config", Context.MODE_PRIVATE);
                    //2.构建Editor实例
                    SharedPreferences.Editor editor = preferences.edit();
                    //3.存储数据
                    editor.putBoolean("isFirstRun", true);
                    //4.提交
                    editor.commit();
                    //1.数据
                    String className = SettingActivity.class.getSimpleName();//获取的类名是SettingActivity
                    //2.将数据储存到bundle中
                    Bundle bundle_help = new Bundle();
                    bundle_help.putString("className", className);
                    //将bundle放到intent中
                    Intent intent_help = new Intent(SettingActivity.this, LeadActivity.class);
                    intent_help.putExtras(bundle_help);
                    //4.启动组件，传递数据
                    startActivity(intent_help);
                    finish();
                    break;
                case R.id.rl_about://进入关于界面
                    //1.数据
                    String className1 = SettingActivity.class.getSimpleName();//获取的类名是SettingActivity
                    //2.将数据储存到bundle中
                    Bundle bundle_about = new Bundle();
                    bundle_about.putString("className", className1);
                    //将bundle放到intent中
                    Intent intent_about = new Intent(SettingActivity.this, AboutActivity.class);
                    intent_about.putExtras(bundle_about);
                    //4.启动组件，传递数据
                    startActivity(intent_about);
                    finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁设置页面，存储通知按钮的选中状态
        NotificationUtils.setOpenNotification(SettingActivity.this, tb_notify.isChecked());
    }
}
