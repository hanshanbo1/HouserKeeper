package com.edu.nbl.houserkeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.nbl.houserkeeper.R;
import com.edu.nbl.houserkeeper.entity.TableInfo;
import com.edu.nbl.houserkeeper.mian.TelmgrshowActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/9.
 * 商家名称和商家号码的适配器
 * 例如：
 * 麦当劳麦乐送 4008517517
 */
//1.继承BaseAdapter
//2.重写相关方法
public class TelListAdapter extends BaseAdapter {
    private Context context;//上下文负责填充item布局
    private ArrayList<TableInfo> datas;//适配器里的数据 例如：{麦当劳麦乐送 ，4008517517}

    public TelListAdapter(Context context, ArrayList<TableInfo> tableInfo) {
        this.context = context;
        this.datas = tableInfo;
    }
    //数据量
    @Override
    public int getCount() {
        return datas.size();
    }
    //对应位置的数据
    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }
    //获取对应位置
    @Override
    public long getItemId(int position) {
        return position;
    }
    //组装数据
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1.获取item布局
        convertView = LayoutInflater.from(context).inflate(R.layout.layout_tellist_item,null);
        //2.获取对应item数据
        TableInfo tableInfo = datas.get(position);
        //3.获取convertView上的控件
        TextView tv_name= (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_number = (TextView) convertView.findViewById(R.id.tv_number);
        //4.将数据放到对应的控件上
        tv_name.setText(tableInfo.getName());
        tv_number.setText(tableInfo.getNumber()+"");
        return convertView;
    }
    //将变动的数据添加到适配器集合中
    public void addDatas(ArrayList<TableInfo> tableInfos) {
        datas.clear();//清空老数据
        datas.addAll(tableInfos);//添加所有数据
    }
}
