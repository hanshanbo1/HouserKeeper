package com.edu.nbl.houserkeeper.mian;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.edu.nbl.houserkeeper.R;
import com.edu.nbl.houserkeeper.biz.MemoryManager;
import com.edu.nbl.houserkeeper.utils.CommonUtils;
import com.edu.nbl.houserkeeper.view.PiechartView;

import java.text.DecimalFormat;

public class SoftmgrActivity extends BaseActivity {
    private RelativeLayout all_soft, system_soft, user_soft;//所有，系统，用户
    private ProgressBar phoneSpace, sdcardSpace;//手机内置空间的进度，外置空间sdcardSpace的进度
    private TextView phoneSpaceMsg, sdcardSpaceMsg;//手机内置空间信息空闲/全部，外置空间信息sdcardSpaceMsg空闲/全部
    private PiechartView piechartView;//饼形图

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_softmgr);
        //初始化ActionBar
        initActiongBar("软件管理", R.drawable.btn_homeasup_default, -1, listener);
        //初始化视图
        initViews();
        //初始化数据
        initData();
    }

    //初始化数据,对应的数据放到对应的控件上
    private void initData() {
        //手机的自身空间
        long phoneSelfTotal = MemoryManager.getPhoneSelfSize();//获取system+cache目录容量总和
        //手机的自身空闲空间
        long phoneSelfUnused = MemoryManager.getPhoneSelfFreeSize();//获取system+cache目录空间容量总和
        //手机已使用的自身空间
        long phoneSelfUsed = phoneSelfTotal - phoneSelfUnused;
        //获取手机内置的sdcard容量/mnt/shell/emulated/0
        long phoneSelfSDCardTotal = MemoryManager.getPhoneSelfSDCardSize();//获取手机内置的sdcard容量/mnt/shell/emulated/0总容量
        //获取手机内置的sdcard未使用的容量
        long phoneSelfSDCardUnused = MemoryManager.getPhoneSelfSDCardFreeSize();//获取/mnt/shell/emulated/0可用容量
        //手机内置的sdcard已使用的容量
        long phoneSelfSDCardused = phoneSelfSDCardTotal - phoneSelfSDCardUnused;
        //手机自带的全部内存
        long phoneTotalSpace = phoneSelfTotal + phoneSelfSDCardTotal;//system+cache+mnt
        //手机自带的全部未使用的内存
        long phoneUnusedSpace = phoneSelfSDCardUnused + phoneSelfUnused;
        //手机自带的全部已使用的内存
        long phoneUsedSpace = phoneSelfUsed + phoneSelfSDCardused;
        //获取外置sdcard
        long phoneOutSDCardTotal = MemoryManager.getPhoneOutSDCardSize(this);
        //获取外置sdcard未使用的容量
        long phoneOutSDCardUnused = MemoryManager.getPhoneOutSDCardFreeSize(this);
        //获取外置sdcard已使用的容量
        long phoneOutSDCardUsed = phoneOutSDCardTotal - phoneOutSDCardUnused;
        //设置手机内置空间的进度条
        phoneSpace.setMax((int) (phoneTotalSpace / 1024));//进度条的总容量
        phoneSpace.setProgress((int) (phoneUsedSpace / 1024));//设置进度条已使用的进度
        //设置外置空间进度条
        sdcardSpace.setMax((int) (phoneOutSDCardTotal / 1024));//B-->K,long-->int
        sdcardSpace.setProgress((int) (phoneOutSDCardUsed / 1024));
        //设置空间使用情况  可用/全部

        phoneSpaceMsg.setText("可用:" + CommonUtils.getFileSize(phoneUnusedSpace) + "/" + CommonUtils.getFileSize(phoneTotalSpace));
        sdcardSpaceMsg.setText("可用:" + CommonUtils.getFileSize(phoneOutSDCardUnused) + "/" + CommonUtils.getFileSize(phoneOutSDCardTotal));
        //画饼形图
        //手机总空间
        float phoneAllSpace = phoneTotalSpace + phoneOutSDCardTotal;
        //因为没有外置sdcard所以手机外置的sdcard存储空间为0
        //计算百分比
        float phonePercent = phoneTotalSpace / phoneAllSpace;//手机内置空间百分比
        float sdcardPerent = phoneOutSDCardTotal / phoneAllSpace;//手机外置空间百分比
        //保留小数点后两位
        DecimalFormat df = new DecimalFormat("#.00");
        phonePercent = Float.parseFloat(df.format(phonePercent));
        sdcardPerent = Float.parseFloat(df.format(sdcardPerent));
        //设置饼形图比例
        piechartView.setPiechartProportionWithAnim(phonePercent,sdcardPerent);

    }

    //初始化视图
    private void initViews() {
        all_soft = (RelativeLayout) findViewById(R.id.rl_all_soft);//所有软件
        system_soft = (RelativeLayout) findViewById(R.id.rl_system_soft);//系统软件
        user_soft = (RelativeLayout) findViewById(R.id.rl_user_soft);//用户软件
        phoneSpace = (ProgressBar) findViewById(R.id.pb_phone);//手机内置空间的进度，
        sdcardSpace = (ProgressBar) findViewById(R.id.pb_sdcard);//外置空间sdcardSpace的进度
        phoneSpaceMsg = (TextView) findViewById(R.id.tv_phone);//手机内置空间信息空闲/全部
        sdcardSpaceMsg = (TextView) findViewById(R.id.tv_sdcard);//外置空间信息sdcardSpaceMsg空闲/全部
        piechartView = (PiechartView) findViewById(R.id.piechart);//饼形图
        all_soft.setOnClickListener(listener);
        system_soft.setOnClickListener(listener);
        user_soft.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {//点谁指谁
            switch (view.getId()) {
                case R.id.iv_left://返回键
                    finish();
                    break;
                //没有break一直往下运行，不管是什么软件都跳转到同一页面
                case R.id.rl_all_soft://所有软件
                case R.id.rl_system_soft://系统软件
                case R.id.rl_user_soft://用户软件
                    Bundle bundle = new Bundle();
                    bundle.putInt("id",view.getId());//把对应点击的item的Id穿过去，区分各个软件
                    Intent intent = new Intent(SoftmgrActivity.this, SoftmgrShowActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
            }
        }
    };
}
