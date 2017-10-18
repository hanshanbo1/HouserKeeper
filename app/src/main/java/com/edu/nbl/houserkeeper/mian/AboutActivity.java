package com.edu.nbl.houserkeeper.mian;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.edu.nbl.houserkeeper.R;
import com.edu.nbl.houserkeeper.view.ActionBarView;

public class AboutActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_avtivity);
        initActiongBar("关于我们", R.drawable.btn_homeasup_default, -1, listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_left:
                    String className = getIntent().getStringExtra("className");//获取类名SettingActivity
                    if (className == null || className.equals("")) {//从主页面过来的
                        startActivity(MainActivity.class);//再回主页面
                        finish();//销毁当前页
                        return;//结束onclick方法不会执行return下面语句
                    }
                    if (className.equals(SettingActivity.class.getSimpleName())) {//表示从设置页过来
                        startActivity(SettingActivity.class);
                    } else {//不是从设置页面过来的
                        startActivity(MainActivity.class);//回到主页面
                    }
                    //startActivity(LogoActivity.class);
                    finish();//销毁当前页面
                    break;
            }
        }
    };
}
