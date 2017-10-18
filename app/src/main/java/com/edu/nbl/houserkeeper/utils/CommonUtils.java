package com.edu.nbl.houserkeeper.utils;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/10.
 * 进行单位换算的工具类
 */

public class CommonUtils {
    //正则表达式
    //把long值对应的文件变成对应单位的
    public static String getFileSize(long fileSize){
        //保留小数点后两位
        DecimalFormat df =new DecimalFormat("#.00");
        StringBuilder msgbuilder = new StringBuilder();
        //对应的是byte字节
        //1G=102M=1024*1024kb=1024*1024*1024B
        if (fileSize<1024){//<KB就用byte本身的单位 例如：890byte
            msgbuilder.append(fileSize);
            msgbuilder.append("B");//例如: 890B
        }
        else if (fileSize<1024*1024){//1KB=<1M
            msgbuilder.append(df.format((double)fileSize/1024));//保留两位小数
            msgbuilder.append("K");
        }else if (fileSize<1024*1024*1024){//1M<1G
            msgbuilder.append(df.format((double)fileSize/1024/1024));
            msgbuilder.append("M");
        }else {
            msgbuilder.append(df.format((double) fileSize/1024/1024/1024));
            msgbuilder.append("G");
        }
        return msgbuilder.toString();//将Stingbulider-->String
    }
    //把long类型的毫米数变成固定的格式字符串时间2017-8-22 11:10:20
    public static String getStrTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:ss");//2017-8-22 11:10:20设置格式
        Date date = new Date(time);//把毫秒数变成Data类型
        String strTime = sdf.format(date);//把Data类型变成字符串
        return strTime;
    }
}