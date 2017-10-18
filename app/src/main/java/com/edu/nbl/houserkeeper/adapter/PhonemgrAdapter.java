package com.edu.nbl.houserkeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.nbl.houserkeeper.R;
import com.edu.nbl.houserkeeper.entity.PhoneInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/17.
 * 手机检测中的适配器
 * //1.继承BaseAdapter
 * //2.重写相关方法
 * //3.在getView方法中，去向item布局中填充数据
 */

public class PhonemgrAdapter extends BaseAdapter{
    private Context context;//上下文
    private List<PhoneInfo> datas;//数据
    //初始化
    public PhonemgrAdapter(Context context, List<PhoneInfo> datas) {
        this.context = context;
        this.datas = datas;
    }
    //数据数量
    @Override
    public int getCount() {
        return datas.size();
    }
    //根据位置来获取对应的数据
    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }
    //获取item的ID
    @Override
    public long getItemId(int position) {
        return position;
    }
    //被我们的数据和item布局进行组装
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1.获取item布局
        convertView = LayoutInflater.from(context).inflate(R.layout.phone_list_item,null);//item的布局
        //2.获取item布局上的控件
        ImageView iv_icon = (ImageView) convertView.findViewById(R.id.iv_phoneparam_icon);//图标
        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_phoneparam_title);//标题
        TextView tv_text = (TextView) convertView.findViewById(R.id.tv_phoneparam_text);//文内容
        //3.获取数据
        PhoneInfo phoneInfo = (PhoneInfo) getItem(position);//获取对应位置的数据
        //4.给item布局上的控件放置数据
        if (phoneInfo.getIcon()==null){
            iv_icon.setImageResource(R.drawable.setting_info_icon_version);
        }else {
            iv_icon.setImageDrawable(phoneInfo.getIcon());
        }
        iv_icon.setImageDrawable(phoneInfo.getIcon());
        tv_title.setText(phoneInfo.getTitle());
        tv_text.setText(phoneInfo.getText());
        //给3取余不同的颜色
        switch (position%3){
            case 0://背景是绿色
                iv_icon.setBackgroundResource(R.drawable.shape_green_rect);
                break;
            case 1://背景是红色
                iv_icon.setBackgroundResource(R.drawable.shape_red_rect);
                break;
            case 2://背景是黄色
                iv_icon.setBackgroundResource(R.drawable.shape_yellow_rect);
                break;
        }
        //返回组装好的item
        return convertView;
    }
    //向适配器集合中添加一个元素
    public void addData(PhoneInfo phoneInfo) {
        datas.add(phoneInfo);
    }
}
