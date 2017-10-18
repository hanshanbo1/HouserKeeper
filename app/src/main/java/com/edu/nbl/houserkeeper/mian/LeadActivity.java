package com.edu.nbl.houserkeeper.mian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.nbl.houserkeeper.R;
import com.edu.nbl.houserkeeper.adapter.LeadPagerAdater;
import com.edu.nbl.houserkeeper.service.MusicService;

public class LeadActivity extends BaseActivity {

    private ViewPager viewPager;//轮播视图
    private ImageView[] icons = new ImageView[3];//放三个点
    private LeadPagerAdater adater;//轮播图适配器
    private ImageView[] iv_leads = new ImageView[3];//第三个ImageView的引导页
    private TextView tv_skip;//直接跳过的TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //1.获取SharedPreferences实例
        SharedPreferences preferences = getSharedPreferences("lead_config", Context.MODE_PRIVATE);
        //2.取数据 取不到默认true 也就是取不到默认是第一次登录
        boolean isFirstRun = preferences.getBoolean("isFirstRun", true);
        if (!isFirstRun) {//不是第一次登录，直接跳转到logoActivity
            startActivity(LogoActivity.class);
            finish();
        } else {//是第一次登录 显示引导页面 放歌
            setContentView(R.layout.activity_lead);//加载布局
            //初始化控件
            initView();
            //2.构建数据
            initData();
            //设置适配器，并监听
            setLeadAdater();
            //启动服务 放歌
            Intent intent = new Intent(this, MusicService.class);
            startService(intent);
        }
    }

    //1.初始化控件
    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        icons[0] = (ImageView) findViewById(R.id.iv_icon0);
        icons[0].setImageResource(R.drawable.adware_style_selected);
        icons[1] = (ImageView) findViewById(R.id.iv_icon1);
        icons[2] = (ImageView) findViewById(R.id.iv_icon2);
        tv_skip = (TextView) findViewById(R.id.tv_skip);//直接跳过TextView
        //给直接跳过添加监听器
        tv_skip.setOnClickListener(onClickListener);
    }

    private void initData() {
        //三个ImageView
        ImageView iv_lead = null;
        //LayoutInflater.from(this);
        //从xml文件中实例化ImageView
        iv_lead = (ImageView) getLayoutInflater().inflate(R.layout.iv_item_lead, null);
        //设置ImageView的src属性
        iv_lead.setImageResource(R.drawable.adware_style_applist);
        //将带图片的ImageView放在数组中
        iv_leads[0] = iv_lead;
        iv_lead = (ImageView) getLayoutInflater().inflate(R.layout.iv_item_lead, null);
        iv_lead.setImageResource(R.drawable.adware_style_banner);
        iv_leads[1] = iv_lead;
        iv_lead = (ImageView) getLayoutInflater().inflate(R.layout.iv_item_lead, null);
        iv_lead.setImageResource(R.drawable.adware_style_creditswall);
        iv_leads[2] = iv_lead;
    }

    //设置适配器，并监听
    private void setLeadAdater() {
        //3.构建适配器
        adater = new LeadPagerAdater(iv_leads);//三张有图片的ImageView传过去
        //4.设置适配器
        viewPager.setAdapter(adater);
        viewPager.addOnPageChangeListener(onPagerListener);
    }

    private ViewPager.OnPageChangeListener onPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.d("滑动", "onPageScrolled");
        }

        //当页面改变时，要相应的改变三个相应的圆点（圆点指示器）
        @Override
        public void onPageSelected(int position) {
            Log.d("滑动", "onPageSelected");
            //先让所有点都变成无色的
            for (int i = 0; i < icons.length; i++) {
                icons[i].setImageResource(R.drawable.adware_style_default);
                tv_skip.setVisibility(View.GONE);//每个页面都不可见
            }
            //再让对应位置的点变成有色的
            icons[position].setImageResource(R.drawable.adware_style_selected);
            //当到了第三个页面是可见
            if (position == 2) {//到了第三个界面可见
                tv_skip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.d("滑动", "onPageScrollStateChanged");

        }
    };
    //跳转到LogoActivity页面（显示商标的页面）
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //点击直接跳过 存储登录状态 不是第一次登录
            savePreferences();
            //停止服务 播放停止
            stopService();
            //Intent可以实现Activity之间的跳转
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
        }
    };

    private void stopService() {
        Intent intentService = new Intent(LeadActivity.this, MusicService.class);
        stopService(intentService);
    }

    private void savePreferences() {
        //1.构建SharedPreferences，创建lead_fig.xml文件
        SharedPreferences preferences = getSharedPreferences("lead_config", Context.MODE_PRIVATE);
        //2.构建Edtor实例
        SharedPreferences.Editor editor = preferences.edit();
        //3.存储数据
        editor.putBoolean("isFirstRun", false);
        //4.提交
        editor.commit();
    }

    //点击回退键，销毁Activity是调用onDestroy()
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }
}
