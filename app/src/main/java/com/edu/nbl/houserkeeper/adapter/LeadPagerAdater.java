package com.edu.nbl.houserkeeper.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/8/1.
 * //1.继承PagerAdapter
 * //2.重写相关方法
 */

public class LeadPagerAdater extends PagerAdapter{

    private ImageView[] datas;

    public LeadPagerAdater(ImageView[] datas){
        this.datas = datas;
    }
    //返回显示item的数量（item的类型是ImageView类型）
    @Override
    public int getCount() {
        return datas.length;//3
    }
    //确定显示的当前页
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
    //销毁不需要的页面
    /**
     *
     * @param container ViewPager容器
     * @param position 要销毁的页面位置
     * @param object 被我们销毁的item
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d("TAG","container="+container+"\nposition="+position+"\nobject="+object);
        ImageView view = datas[position];//要销毁的页面
        container.removeView(view);
    }
    //初始化需要的页面
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view = datas[position];//要初始化的页面
        container.addView(view);
        return view;//注意返回实例化的item
    }
}
