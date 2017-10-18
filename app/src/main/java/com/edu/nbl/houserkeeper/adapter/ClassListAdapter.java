package com.edu.nbl.houserkeeper.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.nbl.houserkeeper.R;
import com.edu.nbl.houserkeeper.entity.ClassInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 * 1.继承BaseAdpter
 * 2.重写相关方法
 */

public class ClassListAdapter extends BaseAdapter {
    private Context context;//上下文
    private List<ClassInfo> datas;//存储适配器集合的数据

    public ClassListAdapter(Context context, List<ClassInfo> datas) {
        this.context = context;
        this.datas = datas;
    }

    //网格视图 对应item下标
    @Override
    public int getCount() {
        return datas.size();
    }

    //网格视图 对应item的数据
    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    //网格视图 对应item的id
    @Override
    public long getItemId(int position) {
        return position;
    }

    //构建对应的item的布局
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1.item的布局 convertView指的是item布局的根元素
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_classlist_item, null);//R.layout.layout_classlist_item填充item的布局
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //2.获取数据
        ClassInfo classInfo = datas.get(position);
        switch (position % 3) {
            case 0://如果下标给3取余，余数为0显示红色
                convertView.setBackgroundResource(R.drawable.selector_classlist_bg1);//没按下去是红色。按下去是灰色
                break;
            case 1://如果下标给3取余，余数为1显示黄色
                convertView.setBackgroundResource(R.drawable.selector_classlist_bg2);//没按下去是黄色
                break;
            case 2://如果下标给3取余，余数为2显示绿色
                convertView.setBackgroundResource(R.drawable.selector_classlist_bg3);//没按下去是绿色
                break;
        }
        //3.将数据放到对应的控件上
        Log.d("TAG", "holder="+holder+"tv_title="+holder.tv_title+"classInfo="+classInfo);
        holder.tv_title.setText(classInfo.getName());
        return convertView;//返回组装好的item
    }

    class ViewHolder {
        TextView tv_title;

        public ViewHolder(View convertView) {
            this.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        }
    }

    public void addDatas(ArrayList<ClassInfo> datas) {
        this.datas.clear();//清空老数据
        this.datas.addAll(datas);//添加新数据
    }
}
