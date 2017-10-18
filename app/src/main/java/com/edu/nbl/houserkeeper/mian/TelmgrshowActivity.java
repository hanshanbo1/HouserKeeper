package com.edu.nbl.houserkeeper.mian;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.edu.nbl.houserkeeper.R;
import com.edu.nbl.houserkeeper.adapter.TelListAdapter;
import com.edu.nbl.houserkeeper.biz.DBTelManager;
import com.edu.nbl.houserkeeper.entity.TableInfo;

import java.util.ArrayList;

public class TelmgrshowActivity extends BaseActivity {
    private ListView lv_number;//listView
    private ArrayList<TableInfo> tableInfos;//数据
    private TelListAdapter adapter;
    private int idx;//下标代表不同的表 idx=1代表table1 idx=2代表table2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telmgrshow);
        //1.获取Intent实例
        Intent intent = getIntent();
        //2.获取Intent中存储的数据
        String title = intent.getStringExtra("title");
        idx = intent.getIntExtra("idx", 0);
        initActiongBar(title, R.drawable.btn_homeasup_default, -1, listener);
        //1.初始化ListView
        lv_number = (ListView) findViewById(R.id.lv_number);
        //2.数据
        tableInfos = new ArrayList<>();
        //3.构建适配器
        adapter = new TelListAdapter(this, tableInfos);
        //4.设置适配器
        lv_number.setAdapter(adapter);
        //设置item监听,拨打电话
        lv_number.setOnItemClickListener(onItemClickListener);
        //异步加载我们的数据 {{麦当劳麦乐送 ，4008517517}，{肯德基宅急送，4008823823}}
        asyncTask();
    }

    private void asyncTask() {
        //构建子线程 执行查询数据的耗时操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取查询的数据
                tableInfos = DBTelManager.readTable("table" + idx);//table1,table2,table3
                //回到主线程 更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addDatas(tableInfos);//将查询的数据更新到适配器集合中
                        adapter.notifyDataSetChanged();//中途数据变动，更新适配器
                    }
                });
            }
        }).start();
    }

    //点击返回键销毁当前页面
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_left:
                    finish();
                    break;
            }
        }
    };
    //item监听，点击拨打电话
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        /**
         *
         * @param parent listview
         * @param view ListView对应位置的item布局
         * @param position ListView 对应的item位置
         * @param id ListView对应item的id
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //获取对应位置
            TableInfo tableInfo = (TableInfo) adapter.getItem(position);
            //获取号码
            long number = tableInfo.getNumber();
            //点击对应位置的item,取出号码拨打电话
            Intent intent = new Intent(Intent.ACTION_CALL);//启动打电话
            intent.setData(Uri.parse("tel:" + number));//设置电话号码
            if (ActivityCompat.checkSelfPermission(TelmgrshowActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent);
        }
    };
}
