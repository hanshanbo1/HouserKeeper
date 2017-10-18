package com.edu.nbl.houserkeeper.biz;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.edu.nbl.houserkeeper.entity.ClassInfo;
import com.edu.nbl.houserkeeper.entity.TableInfo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 * 此工具类帮助我们将assets目录下的db文件拷贝到/data/data/包名下
 */

public class DBTelManager {
    //文件名
    private static final String FILE_NAME = "commonnum.db";
    //包名
    private static final String PACKAGE_NAME = "com.edu.nbl.houserkeeper";
    //拼接存储路径 /data/data、包名
    private static final String FILE_PATH = "/data" + Environment.getDataDirectory() + "/" + PACKAGE_NAME;

    //存储数据
    //InputStream is 指将我们拷贝的文件变成流 is代表commonnum.db
    public static void readUpdateDB(InputStream is) throws IOException {
        File toFile = new File(FILE_PATH + "/" + FILE_NAME);//在/data/data、包名/commonnum.db新建commonnum.db
        if (toFile.exists()) {//存在 不在拷贝文件
            return;
        }
        //如果存在就将assets下面的commonnum.db文件拷贝到手机中
        BufferedInputStream bis = new BufferedInputStream(is);//提高读写效率 缓冲输入字节流
        FileOutputStream fos = new FileOutputStream(toFile, false);//文件字节输出流
        BufferedOutputStream bos = new BufferedOutputStream(fos);//高级流可以放低级流
        int len;
        byte[] data = new byte[1024 * 5];//5k 1G=1024M=1024*1024k=1024*1024*B
        while ((len = bis.read(data)) != -1) {
            bos.write(data, 0, len);
        }
        bos.flush();//在缓冲区不满的情况下也能刷出数据
        bis.close();
        bos.close();
    }

    //从手机中/data/data/包名/commonnum.db查询数据，例如：订餐电话，公共服务
    public static ArrayList<ClassInfo> readClassListTable() {
        //打开FILE_PATH + "/" + FILE_NAME路径下的db文件
        //1.构建SQLiteDatabase实例
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(FILE_PATH + "/" + FILE_NAME, null);
        //用一个集合去存储我们查询的数据
        ArrayList<ClassInfo> classInfos = new ArrayList<>();
        //查询数据
        //2.编写查询语句
        String sql = "select * from classlist";//*表示查询所有，包括name和idx
        //3.执行查询语句，获取游标
        Cursor cursor = database.rawQuery(sql, null);
        //4.写循环查询所有数据
        if (cursor.moveToFirst()) {//将光标移到第一行，也就是订餐电话
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));//获取name字段所占的列下标
                int idx = cursor.getInt(cursor.getColumnIndex("idx"));
                //取一个存一个
                ClassInfo classInfo = new ClassInfo(name, idx);
                classInfos.add(classInfo);
            } while (cursor.moveToNext());//移动到下一行，如果有为true,没有就为false
        }
        //关闭资源，释放内存
        cursor.close();
        database.close();
        return classInfos;//{{订餐电话，1}，{公共服务，2}}
    }

    //根据表名查询数据 例如：{{麦当劳麦乐送 ，4008517517}，{肯德基宅急送，4008823823}}
    public static ArrayList<TableInfo> readTable(String tableName) {
        ArrayList<TableInfo> tableInfos = new ArrayList<>();//存储所有数据
        //1.打开/data/data/包名/commonnum.db数据库
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(FILE_PATH + "/" + FILE_NAME, null);
        //2.编写查询语句
        String sql = "select * from " + tableName;//例如：table1,table2
        //3.执行查询语句，获取游标
        Cursor cursor = database.rawQuery(sql, null);
        //4.通过游标遍历循环数据库中所有的数据
        if (cursor.moveToFirst()) {//移动数据库的第一行
            do {
                //取对应字段的数据
                String name = cursor.getString(cursor.getColumnIndex("name"));
                long number = cursor.getLong(cursor.getColumnIndex("number"));
                //取一个存一个
                TableInfo tableInfo = new TableInfo(name, number);
                tableInfos.add(tableInfo);
            } while (cursor.moveToNext());//条件是移到下一行是否还有数据
        }
        //关闭资源，释放内存
        cursor.close();
        database.close();
        return tableInfos;//返回获取的所有数据 例如：{{麦当劳麦乐送 ，4008517517}，{肯德基宅急送，4008823823}}
    }
}
