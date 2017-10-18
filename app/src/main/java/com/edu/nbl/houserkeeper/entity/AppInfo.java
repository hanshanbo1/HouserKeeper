package com.edu.nbl.houserkeeper.entity;

import android.content.pm.PackageInfo;

/**
 * Created by Administrator on 2017/8/11.
 *
 */

public class AppInfo {
    private boolean isDel;//是否删除
    private PackageInfo packageInfo;//此类封装了软件的信息（应用程序的名称，包名，图标，版本）

    public AppInfo() {
    }

    public AppInfo(boolean isDel, PackageInfo packageInfo) {
        this.isDel = isDel;
        this.packageInfo = packageInfo;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        isDel = del;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "isDel=" + isDel +
                ", packageInfo=" + packageInfo +
                '}';
    }
}
