package com.edu.nbl.houserkeeper.Junit;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2017/8/18.
 */

public class FileDemo {
    public static void getAllFiles(){
        File dir = Environment.getExternalStorageDirectory();//  /mnt/shell/emulated/0
        File[] files = dir.listFiles();
        for (File file:files){//获取0目录下的所有东西
            System.out.println("fileName="+file.getName());
        }
    }
}
