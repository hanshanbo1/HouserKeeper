package com.edu.nbl.houserkeeper.mian;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.edu.nbl.houserkeeper.R;
import com.edu.nbl.houserkeeper.adapter.ProcessAppAdapter;
import com.edu.nbl.houserkeeper.biz.AppInfoManager;
import com.edu.nbl.houserkeeper.biz.MemoryManager;
import com.edu.nbl.houserkeeper.biz.SystemManager;
import com.edu.nbl.houserkeeper.entity.RunningAppInfo;
import com.edu.nbl.houserkeeper.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpeedActivity extends BaseActivity {

    private TextView phone_name,phone_modle,phone_ram;//手机名、手机型号、手机运行内存
    private ProgressBar pb_ram,pb_precess;//运行内存进度条、数据加载之前显示的进度条
    private Button btn_clean,btn_process;//一键清理和显示进程
    private ListView lv_process;//显示进程的ListView
    private CheckBox cb_all_process;//全选按钮
    private ArrayList<RunningAppInfo> runningAppInfos;//数据
    private Map<Integer, List<RunningAppInfo>> processAppInfos;//全局变量
    private int state = AppInfoManager.RUNING_APP_TYPE_USER;//存储要显示的进程状态
    private ProcessAppAdapter appAdapter;//适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed);
        initActiongBar("手机加速",R.drawable.btn_homeasup_default,-1,listener);
        //初始化视图
        initView();
        //初始化数据
        initData();
        //初始化手机型号和手机版本
        initPhoneData();
        //初始化运行内存数据
        initRamData();
    }
    //初始化运行内存
    private void initRamData() {
        //获取全部的运行内存
        float totalRam = MemoryManager.getPhoneTotalRamMemory();
        //获取空闲的运行内存
        float freeRam = MemoryManager.getPhoneFreeRamMemory(this);//没有用过的
        float uesdRam = totalRam-freeRam;
        //设置进度条
        float usedP = uesdRam/totalRam;//已用的比例 例如：0.3
        pb_ram.setMax(100);
        pb_ram.setProgress((int) (usedP*100));//设置进度条的百分比
        //设置TextView的比例
        phone_ram.setText("已用内存："+ CommonUtils.getFileSize((long) uesdRam)+"/"+CommonUtils.getFileSize((long) totalRam));
    }

    private void initPhoneData() {
        phone_name.setText(SystemManager.getPhoneName().toUpperCase());//手机名称
        phone_modle.setText(SystemManager.getPhoneModelName());//手机型号和版本
    }

    private void initView() {
        phone_name = (TextView) findViewById(R.id.tv_phone_name);//手机名
        phone_modle = (TextView) findViewById(R.id.tv_phone_modle);//手机型号
        phone_ram = (TextView) findViewById(R.id.tv_ram);//手机的运行内存、
        pb_ram = (ProgressBar) findViewById(R.id.pb_ram);//手机运行内存进度条
        pb_precess = (ProgressBar) findViewById(R.id.pb_process);//加载进度条
        btn_clean = (Button) findViewById(R.id.btn_clean);//一键清理按钮
        btn_process = (Button) findViewById(R.id.btn_process);//切换进程数据
        cb_all_process = (CheckBox) findViewById(R.id.cb_all_process);//全选CheckBox
        //1.获取ListView
        lv_process = (ListView) findViewById(R.id.lv_process);//进程Listener
        //2.数据
        runningAppInfos = new ArrayList<RunningAppInfo>();
        //3.构建适配器
        appAdapter = new ProcessAppAdapter(this, runningAppInfos);
        //4.设置适配器
        lv_process.setAdapter(appAdapter);
        //设置监听器
        btn_process.setOnClickListener(listener);//切换进程
        btn_clean.setOnClickListener(listener);//一键清理
        cb_all_process.setOnCheckedChangeListener(checkedChangeListener);//全选
    }
    //初始化数据
    private void initData() {
        lv_process.setVisibility(View.INVISIBLE);//listView不可见
        pb_precess.setVisibility(View.VISIBLE);//旋转的progressBar可见
        //构建子线程，去获取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取数据 获取的是系统进程或用户进程
                processAppInfos = AppInfoManager.getAppInfoManager(SpeedActivity.this).getRuningAppInfos();
                //回到主线程，更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb_precess.setVisibility(View.INVISIBLE);//隐藏progressbar
                        lv_process.setVisibility(View.VISIBLE);//显示progressbar
                        //数据放到适配器集合中
                        if (state==AppInfoManager.RUNING_APP_TYPE_USER){//显示用户的进程
                            List<RunningAppInfo> userapp = processAppInfos.get(AppInfoManager.RUNING_APP_TYPE_USER);//所有用户的进程
                            appAdapter.addDatas(userapp);
                            appAdapter.notifyDataSetChanged();//更新适配器
                            Log.d("TAG", "数据已更新processAppInfos="+processAppInfos);
                        }else if (state==AppInfoManager.RUNING_APP_TYPE_SYS){//显示系统进程
                            List<RunningAppInfo> sysapp = processAppInfos.get(AppInfoManager.RUNING_APP_TYPE_SYS);//所有系统进程
                            appAdapter.addDatas(sysapp);//将数据放到适配器集合中
                            appAdapter.notifyDataSetChanged();//更新适配器
                        }
                    }
                });
            }
        }).start();
    }
    //初始化视图
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_left://点击左边的返回键
                    finish();
                    break;
                case R.id.btn_process://切换进程按钮
                    if (processAppInfos!=null){//次map集合不为空，包含系统和用户进程
                        switch (state){//刚开始的状态是用户进程
                            case AppInfoManager.RUNING_APP_TYPE_USER://如果是用户进程,点击显示系统数据
                                appAdapter.addDatas(processAppInfos.get(AppInfoManager.RUNING_APP_TYPE_SYS));//获取系统进程数据，放入适配器集合
                                btn_process.setText("只显示用户进程");
                                state=AppInfoManager.RUNING_APP_TYPE_SYS;//当前用户状态，点击完变成系统状态
                                break;
                            case AppInfoManager.RUNING_APP_TYPE_SYS://如果是系统进程,点击显示用户数据
                                appAdapter.addDatas(processAppInfos.get(AppInfoManager.RUNING_APP_TYPE_USER));//获取用户进程数据，放入适配器集合
                                btn_process.setText("只显示系统进程");
                                state=AppInfoManager.RUNING_APP_TYPE_USER;//当前系统状态，点击完变成用户状态
                                break;
                        }
                        appAdapter.notifyDataSetChanged();//都需要更新适配器
                        cb_all_process.setChecked(false);
                    }
                    break;
                case R.id.btn_clean://一键清理
                    //获取适配器所有的item数据
                    List<RunningAppInfo> appInfos = appAdapter.getDataFromAdapter();//获取适配器中所有数据
                    for (RunningAppInfo appInfo:appInfos){//遍历循环所以元素
                        if (appInfo.isClear()){//勾选上了
                            String packageName = appInfo.getPackagename();//获取包名
                            Log.d("TAG", "packageName="+packageName);
                            //杀死相应包名的进程
                            AppInfoManager.getAppInfoManager(SpeedActivity.this).killProcesses(packageName);
                        }
                    }
                    initData();//重新加载数据
                    cb_all_process.setChecked(false);
                    break;
            }
        }
    };
    //全选按钮
    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            List<RunningAppInfo> appInfos = appAdapter.getDataFromAdapter();//获取所有元素
            for (RunningAppInfo appInfo:appInfos){
                appInfo.setClear(isChecked);//把所有的CheckBox的勾选状态变成isCheck
            }
            //更新适配器
            appAdapter.notifyDataSetChanged();
        }
    };
}
