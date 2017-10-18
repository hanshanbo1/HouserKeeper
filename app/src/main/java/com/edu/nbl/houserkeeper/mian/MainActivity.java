package com.edu.nbl.houserkeeper.mian;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.nbl.houserkeeper.R;
import com.edu.nbl.houserkeeper.biz.AppInfoManager;
import com.edu.nbl.houserkeeper.biz.MemoryManager;
import com.edu.nbl.houserkeeper.view.ClearArcView;

public class MainActivity extends BaseActivity {
    private TextView tv_rocket, tv_softmgr, tv_phonemgr, tv_telmgr, tv_filemgr, tv_sdclean;
    private ClearArcView cav_clear;//一键清理饼形图
    private ImageView iv_clear;//一键清理ImageView
    private TextView tv_clear;//一键清理文字
    private TextView tv_score;//一键清理得分

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActiongBar("安卓管家", R.drawable.ic_launcher, R.drawable.ic_child_configs, listener);
        initViews();
        //一键加速
        initScoreClear();
        //一来就要一键加速
        initScoreData();
    }

    //初始化一键清理控件
    private void initScoreClear() {
        cav_clear = (ClearArcView) findViewById(R.id.cav_score);//一键清理饼形图
        iv_clear = (ImageView) findViewById(R.id.iv_score);//一键清理ImageView
        tv_clear = (TextView) findViewById(R.id.tv_speedup);//一键加速TextView
        tv_score = (TextView) findViewById(R.id.tv_score);//一键清理得分
        //监听器
        iv_clear.setOnClickListener(listener);//监听一键清理ImageView
        tv_clear.setOnClickListener(listener);//

    }

    private void initViews() {
        tv_rocket = (TextView) findViewById(R.id.tv_rocket);
        tv_softmgr = (TextView) findViewById(R.id.tv_softmgr);
        tv_phonemgr = (TextView) findViewById(R.id.tv_phonemgr);
        tv_telmgr = (TextView) findViewById(R.id.tv_telmgr);
        tv_filemgr = (TextView) findViewById(R.id.tv_filemgr);
        tv_sdclean = (TextView) findViewById(R.id.tv_sdclean);
        tv_rocket.setOnClickListener(listener);
        tv_softmgr.setOnClickListener(listener);
        tv_phonemgr.setOnClickListener(listener);
        tv_telmgr.setOnClickListener(listener);
        tv_filemgr.setOnClickListener(listener);
        tv_sdclean.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_left://左边按钮 进入关于界面
                    startActivity(AboutActivity.class, R.anim.left_in, R.anim.right_out);
                    break;
                case R.id.iv_right://右边按钮 进入设置页面
                    startActivity(SettingActivity.class, R.anim.right_in, R.anim.left_out);
                    break;
                case R.id.tv_rocket://手机加速
                    startActivity(SpeedActivity.class, R.anim.down_in, R.anim.up_out);
                    break;
                case R.id.tv_softmgr://软件管理
                    startActivity(SoftmgrActivity.class, R.anim.down_in, R.anim.up_out);
                    break;
                case R.id.tv_phonemgr://手机检测
                    startActivity(PhonemgrActivity.class, R.anim.down_in, R.anim.up_out);
                    break;
                case R.id.tv_telmgr://通讯大全
                    startActivity(TelmgrActivity.class, R.anim.down_in, R.anim.up_out);
                    break;
                case R.id.tv_filemgr://文件管理
                    startActivity(FilemgrActivity.class, R.anim.down_in, R.anim.up_out);
                    break;
                case R.id.tv_sdclean://垃圾清理
                    startActivity(SdcleanActivity.class, R.anim.down_in, R.anim.up_out);
                    break;
                case R.id.iv_score://一键清理
                case R.id.tv_speedup://一键清理加速
                    //按下按钮清除后台，服务，空进程
                    AppInfoManager.getAppInfoManager(MainActivity.this).killALLProcesses();//清除所有不需要的进程
                    initScoreData();//初始化得分数据
                    break;
            }
        }
    };

    //初始化得分数据
    private void initScoreData() {
        //获取全部的运行内存
        float totalRam = MemoryManager.getPhoneTotalRamMemory();
        //获取空闲的运行内存
        float freeRam = MemoryManager.getPhoneFreeRamMemory(this);
        //计算百分比
        float usedRam = totalRam - freeRam;
        float usedP = usedRam / totalRam;//正在使用/全部
        int used100 = (int) (usedP * 100);//例如：20.0--》20
        tv_score.setText(used100+"");//显示百分比
        //设置圆弧的进度
        //算角度
        int angle = (int) (usedP*360);
        cav_clear.setAngleNithAnim(angle);
    }
}
