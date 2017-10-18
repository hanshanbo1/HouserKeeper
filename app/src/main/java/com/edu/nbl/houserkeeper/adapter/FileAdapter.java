package com.edu.nbl.houserkeeper.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.nbl.houserkeeper.R;
import com.edu.nbl.houserkeeper.entity.FileInfo;
import com.edu.nbl.houserkeeper.utils.CommonUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 * //1.继承BaseAdapter
 * //2.重写相关方法
 */

public class FileAdapter extends BaseAdapter {
    private Context context;//上下文
    private List<FileInfo> datas;//数据

    //构造方法初始化数据
    public FileAdapter(Context context, List<FileInfo> datas) {
        this.context = context;
        this.datas = datas;
    }

    //该显示的数据数量
    @Override
    public int getCount() {
        return datas.size();
    }

    //获取对应位置的数据
    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    //获取对应位置上item的id和position相同
    @Override
    public long getItemId(int position) {
        return position;
    }

    //把数据放到对应item的控件上
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            //获取item布局
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_file_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //把数据方法哦对应的控件上
        FileInfo fileInfo = datas.get(position);//根据位置获取一个数据
        String filename = fileInfo.getFile().getName();//获取此文件的文件名
        String filetime  = CommonUtils.getStrTime(fileInfo.getFile().lastModified());//把龙值的毫秒数变成Sting
        String filesize = CommonUtils.getFileSize(fileInfo.getFile().length());//进行单位换算
        boolean isSelect = fileInfo.isSalect();//是否删除文件
        //fileInfo.getIconName:drawable下的图片名称."drawable"表示从drawable目录下获取。context.getPackageName：包名
         int icon = context.getResources().getIdentifier(fileInfo.getIconName(),"drawable",context.getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),icon);//把drawable下的资源变成bitmap
        //放数据
        holder.tv_file_name.setText(filename);//文件名
        holder.tv_file_time.setText(filetime);//文件修改时间
        holder.tv_file_size.setText(filesize);//文件大小
        holder.cb_file_del.setChecked(isSelect);//是否删除
        holder.iv_file_icon.setImageBitmap(bitmap);//放置icon图片
        //设置CheckBox的监听器
        holder.cb_file_del.setOnCheckedChangeListener(onCheckedChangeListener);
        holder.cb_file_del.setTag(position);//设置状态改变的位置
        return convertView;
    }
    //获取适配器里所以文件
    public List<FileInfo> getDataFromAdapter() {
        return datas;
    }

    public void addDatas(List<FileInfo> fileInfos) {
        datas.clear();//清空老数据
        datas.addAll(fileInfos);//添加新数据
    }

    //对控件的优化
    class ViewHolder {
        CheckBox cb_file_del;//是否删除此文件
        ImageView iv_file_icon;//此文件的icon图标
        TextView tv_file_name;//文件的名称
        TextView tv_file_time;//文件上一次修改时间
        TextView tv_file_size;//文件的大小

        public ViewHolder(View convertView) {
            cb_file_del = (CheckBox) convertView.findViewById(R.id.cb_file_del);
            iv_file_icon = (ImageView) convertView.findViewById(R.id.iv_file_icon);
            tv_file_name = (TextView) convertView.findViewById(R.id.tv_file_name);
            tv_file_time = (TextView) convertView.findViewById(R.id.tv_file_time);
            tv_file_size = (TextView) convertView.findViewById(R.id.tv_file_size);
        }
    }
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        /**
         *
         * @param buttonView 点击的那个CheckBox
         * @param isChecked 点击的CheckBox的选中状态
         */
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = (int) buttonView.getTag();//获取状态改变的CheckBox对应的item位置
            datas.get(position).setSalect(isChecked);
        }
    };
}
