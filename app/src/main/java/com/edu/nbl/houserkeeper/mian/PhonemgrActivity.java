package com.edu.nbl.houserkeeper.mian;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.edu.nbl.houserkeeper.R;
import com.edu.nbl.houserkeeper.adapter.PhonemgrAdapter;
import com.edu.nbl.houserkeeper.biz.MemoryManager;
import com.edu.nbl.houserkeeper.biz.PhoneManager;
import com.edu.nbl.houserkeeper.entity.PhoneInfo;
import com.edu.nbl.houserkeeper.utils.CommonUtils;

import java.util.ArrayList;

//手机检测
public class PhonemgrActivity extends BaseActivity {
    private View ll_battery;//线性布局的电池
    private ListView lv_phone_param;//线性布局
    private TextView tv_battery;//电池电量
    private PhonemgrAdapter adapter;//适配器
    private ProgressBar pb_battery,pb_loading;//电池进度条，正在加载时有一个旋转的进度条
    private int currentBattery;//当前电量
    private int temperatureBattery;//当前温度
    private ArrayList<PhoneInfo> phoneInfos;//数据
    private BatteryBroadCastReceiver batteryBroadCastReceiver;//广播接收器，监听电池电量改变

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonemgr);
        initActiongBar("手机检测",R.drawable.btn_homeasup_default,-1,listener);
        //初始化控件
        initView();
        //初始化数据
        initDataSource();
    }
    //初始化数据
    private void initDataSource() {
        pb_loading.setVisibility(View.VISIBLE);//显示progressbar
        lv_phone_param.setVisibility(View.INVISIBLE);//ListView消失
        //构建子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取数据
                PhoneManager phoneManager = PhoneManager.getPhoneManage(PhonemgrActivity.this);
                String title ;//标题
                String text ;//文本内容
                Drawable icon ;//图标
                //取第一个item数据
                title = "设备名称:"+phoneManager.getPhoneName1();//获取设备名称
                text = "系统版本:"+phoneManager.getPhoneSystemVersion();//获取系统版本
                icon = getResources().getDrawable(R.drawable.setting_info_icon_version);
                final PhoneInfo phoneInfo = new PhoneInfo(icon,title,text);//存储数据
                //取第二个item数据
                title = "全部运行内存:"+ CommonUtils.getFileSize(MemoryManager.getPhoneTotalRamMemory());//单位换算
                text = "剩余运行内存:"+CommonUtils.getFileSize(MemoryManager.getPhoneFreeRamMemory(PhonemgrActivity.this));//单位换算
                icon = getResources().getDrawable(R.drawable.setting_info_icon_space);//获取icon图标
                final PhoneInfo phoneInfo1 = new PhoneInfo(icon,title,text);//存储数据
                //取第3个item数据
                title = "CPU名称:"+phoneManager.getPhoneCpuName();//获取cpu名称
                text  = "CUP的数量:"+phoneManager.getPhoneCpuNumber();//获取cup的数量
                icon = getResources().getDrawable(R.drawable.setting_info_icon_cpu);//获取icon图标
                final PhoneInfo phoneInfo2 = new PhoneInfo(icon,title,text);//存储数据
                //取第4个item数据
                title = "手机分辨率:"+phoneManager.getResolution();
                text = "相机分辨率:"+phoneManager.getMaxPhotoSize();
                icon = getResources().getDrawable(R.drawable.setting_info_icon_camera);
                final PhoneInfo phoneInfo3 = new PhoneInfo(icon,title,text);//存储数据
                //取第5个item数据
                title = "基带版本:"+phoneManager.getPhoneSystemBasebandVersion();
                text = "是否ROOT:"+phoneManager.isRoot();
                icon = getResources().getDrawable(R.drawable.setting_info_icon_root);
                final PhoneInfo phoneInfo4 = new PhoneInfo(icon,title,text);//存储数据
                //数据一拿到，回到主线程更新数据
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addData(phoneInfo);//添加第一个item
                        adapter.addData(phoneInfo1);//添加第二个item
                        adapter.addData(phoneInfo2);//添加第三个item
                        adapter.addData(phoneInfo3);//添加第四个item
                        adapter.addData(phoneInfo4);//添加第五个item
                        pb_loading.setVisibility(View.INVISIBLE);//消失
                        lv_phone_param.setVisibility(View.VISIBLE);//显示
                        //更新数据
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    //初始化控件
    private void initView() {
        ll_battery = findViewById(R.id.ll_battery);//线性布局的电池
        ll_battery.setOnClickListener(listener);//点击弹出电池电量和温度的对话框
        lv_phone_param = (ListView) findViewById(R.id.lv_phone_param);//ListView
        tv_battery = (TextView) findViewById(R.id.tv_battery);//电池电量
        pb_battery = (ProgressBar) findViewById(R.id.pb_battery);//电池电量进度条
        pb_loading = (ProgressBar) findViewById(R.id.pb_phone_param);//正在加载的进度条
        //2.测试数据
        phoneInfos = new ArrayList<>();
        //3.构建适配器
        adapter = new PhonemgrAdapter(this,phoneInfos);
        //4.设置适配器
        lv_phone_param.setAdapter(adapter);
        //注册广播
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);//电池电量改变
        batteryBroadCastReceiver = new BatteryBroadCastReceiver();//构建广播接收器
        registerReceiver(batteryBroadCastReceiver,filter);//注册广播
    }
    //广播接收器，接收电池电量的改变
    class BatteryBroadCastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){//电池电量改变
                Bundle bundle = intent.getExtras();//存储了电池电量
                int maxBattery = bundle.getInt(BatteryManager.EXTRA_SCALE);//获取全部电量
                currentBattery = bundle.getInt(BatteryManager.EXTRA_LEVEL);//获取当前电量
                temperatureBattery = bundle.getInt(BatteryManager.EXTRA_TEMPERATURE);//获取当前温度
                pb_battery.setMax(maxBattery);//设置全部电量
                pb_battery.setProgress(currentBattery);//设置当前电量
                int current100 = 100*currentBattery/maxBattery;
                tv_battery.setText(current100+"%");
            }
        }
    }
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_left://点击左边返回键销毁当前页
                    finish();
                    break;
                case R.id.ll_battery://点击弹出电池电量和温度
                    showBatteryMessage();
                    break;
            }
        }
    };
    //淡出对话框
    private void showBatteryMessage() {
        //1.构建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("电池电量信息");
        builder.setItems(new String[] {"当前电量："+currentBattery,"当前温度："+temperatureBattery},null);
        builder.create().show();//创建并显示
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryBroadCastReceiver);//撤销注册
    }
}
