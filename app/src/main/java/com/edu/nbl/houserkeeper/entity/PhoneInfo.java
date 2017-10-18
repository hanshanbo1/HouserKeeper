package com.edu.nbl.houserkeeper.entity;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/8/17.
 * 封装手机信息的一个实体类
 *
 */

public class PhoneInfo {
    private Drawable icon;//item图标
    private String title;//item里的标题
    private String text;//item里的文本内容

    public PhoneInfo() {

    }

    public PhoneInfo(Drawable icon, String title, String text) {
        this.icon = icon;
        this.title = title;
        this.text = text;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "PhoneInfo{" +
                "icon=" + icon +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
