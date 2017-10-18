package com.edu.nbl.houserkeeper.mian;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.edu.nbl.houserkeeper.R;
import com.edu.nbl.houserkeeper.adapter.FileAdapter;
import com.edu.nbl.houserkeeper.biz.FileManager;
import com.edu.nbl.houserkeeper.entity.FileInfo;
import com.edu.nbl.houserkeeper.utils.CommonUtils;
import com.edu.nbl.houserkeeper.utils.FileTypeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilemgrShowActivity extends BaseActivity {
    private int id;//用id来区分当前显示的页面
    private TextView tv_file_number;//文件数据
    private TextView tv_file_size;//文件占用空间
    private Button btn_file_delete;//删除按钮
    private ListView lv_file;//列表视图
    private List<FileInfo> fileInfos;//数据
    private FileAdapter fileAdapter;//适配器
    private String title;//ActionBar的标题
    private long filesize;//所以文件的大小
    private int fileNumber;//文件个数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filemgr_show);
        //初始化控件
        initViews();
        //初始化数据
        initDatas();
        //初始化ActionBar
        initActiongBar(title, R.drawable.btn_homeasup_default, -1, listener);
        //把数据放到对应的控件上
        setDatas();
    }

    //初始化相关成员变量
    private void initViews() {
        id = getIntent().getIntExtra("id", -1);//根据id获取对应控件
        Log.d("ID", "id=" + Integer.toString(id));
        tv_file_number = (TextView) findViewById(R.id.tv_file_number);//文件数量
        tv_file_size = (TextView) findViewById(R.id.tv_file_size);//文件大小
        btn_file_delete = (Button) findViewById(R.id.btn_file_delete);//删除文件
        btn_file_delete.setOnClickListener(listener);
        lv_file = (ListView) findViewById(R.id.lv_file);//列表视图
    }

    //初始化数据
    private void initDatas() {
        //通过id来区分要显示的标题
        switch (id) {
            case R.id.rl_file_all://全部
                title = "全部";
                fileInfos = FileManager.getFileManager().getAllFileList();//获取全部文件
                filesize = FileManager.getFileManager().getAllFileSize();//全部文件大小
                Log.d("FILE", "fileInfos=" + fileInfos);
                break;
            case R.id.rl_file_text://文档
                title = "文档";
                fileInfos = FileManager.getFileManager().getTextFileList();//获取全文档文件
                filesize = FileManager.getFileManager().getTextFileSize();//文档文件大小
                Log.d("FILE", "fileInfos=" + fileInfos);
                break;
            case R.id.rl_file_video://视频
                title = "视频";
                fileInfos = FileManager.getFileManager().getVideoFileList();//获取视频文件
                filesize = FileManager.getFileManager().getVideoFileSize();//视频文件大小
                Log.d("FILE", "fileInfos=" + fileInfos);
                break;
            case R.id.rl_file_audio://音频
                title = "音频";
                fileInfos = FileManager.getFileManager().getAudioFileList();//获取音频文件
                filesize = FileManager.getFileManager().getAudioFileSize();//音频文件大小
                Log.d("FILE", "fileInfos=" + fileInfos);
                break;
            case R.id.rl_file_image://图像
                title = "图像";
                fileInfos = FileManager.getFileManager().getImageFileList();//获取图像文件
                filesize = FileManager.getFileManager().getImageFileSize();//图像文件大小
                Log.d("FILE", "fileInfos=" + fileInfos);
                break;
            case R.id.rl_file_zip://压缩包
                title = "压缩包";
                fileInfos = FileManager.getFileManager().getZipFileList();//获取压缩包文件
                filesize = FileManager.getFileManager().getZipFileSize();//压缩包文件大小
                Log.d("FILE", "fileInfos=" + fileInfos);
                break;

            case R.id.rl_file_apk://程序包
                title = "程序包";
                fileInfos = FileManager.getFileManager().getApkFileList();//获取APK文件
                filesize = FileManager.getFileManager().getApkFileSize();//APK文件大小
                Log.d("FILE", "fileInfos=" + fileInfos);
                break;
        }
        fileNumber = fileInfos.size();//文件个数
    }

    //把数据放到对应的控件上
    private void setDatas() {
        tv_file_number.setText(fileNumber + "");//文件个数
        tv_file_size.setText(CommonUtils.getFileSize(filesize));//占用空间
        //构建适配器放置ListView数据
        Log.d("DATA", "fileInfos=" + fileInfos);
        fileAdapter = new FileAdapter(this, fileInfos);
        //设置适配器
        lv_file.setAdapter(fileAdapter);
        lv_file.setOnItemClickListener(itemClickListener);//列表监听器
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_left://返回键
                    finish();
                    break;
                case R.id.btn_file_delete://点击删除文件
                    deleteFiles();
                    break;
            }
        }
    };

    //删除文件
    private void deleteFiles() {
        //获取所有文件
        List<FileInfo> allFileInfos = fileAdapter.getDataFromAdapter();
        //要删除的文件集合
        List<FileInfo> delFileInfos = new ArrayList<>();
        for (int i = 0; i < allFileInfos.size(); i++) {//遍历所有文件
            FileInfo fileInfo = allFileInfos.get(i);//拿到一个文件
            if (fileInfo.isSalect()) {//是否选中
                //选中
                delFileInfos.add(fileInfo);
            }
        }
        //把要删除的集合里所有文件删除
        for (int i = 0; i < delFileInfos.size(); i++) {
            FileInfo fileInfo = delFileInfos.get(i);//拿到一个要删除的文件
            String fileType = fileInfo.getFileType();//获取文件类型
            File file = fileInfo.getFile();
            long size = file.length();//删除文件的大小
            if (file.delete()) {//删除文件
                //删除成功 更新数据
                fileAdapter.getDataFromAdapter().remove(fileInfo);//删除适配器集合中的元素
                FileManager.getFileManager().setAllFileSize(FileManager.getFileManager().getAllFileSize() - size);//设置最新文件大小
                filesize = FileManager.getFileManager().getAllFileSize();//获取最新文件大小
                FileManager.getFileManager().getAllFileList().remove(fileInfo);//真正移除
                switch (fileType){
                    case "txt"://文档
                        FileManager.getFileManager().getTextFileList().remove(fileInfo);//删除文档中的集合
                        break;
                    case "video"://视频
                        FileManager.getFileManager().getVideoFileList().remove(fileInfo);//删除视频中的集合
                        break;
                    case "audio"://音频
                        FileManager.getFileManager().getAudioFileList().remove(fileInfo);//删除音频中的集合
                        break;
                    case "image"://图像
                        FileManager.getFileManager().getImageFileList().remove(fileInfo);//删除图像中的集合
                        break;
                    case "zip"://压缩包
                        FileManager.getFileManager().getZipFileList().remove(fileInfo);//删除压缩包中的集合
                        break;
                    case "apk"://程序包
                        FileManager.getFileManager().getApkFileList().remove(fileInfo);//删除程序包中的集合
                        break;
                }
                switch (id) {
                    case R.id.rl_file_text://文档
                        FileManager.getFileManager().setTextFileSize(FileManager.getFileManager().getTextFileSize() - size);//设置文档新文件大小
                        filesize = FileManager.getFileManager().getTextFileSize();//获取最新文档大小
                        break;
                    case R.id.rl_file_video://视频
                        FileManager.getFileManager().setVideoFileSize(FileManager.getFileManager().getVideoFileSize() - size);//设置视频新文件大小
                        filesize = FileManager.getFileManager().getVideoFileSize();//获取最新视频大小
                        break;
                    case R.id.rl_file_audio://音频
                        FileManager.getFileManager().setAudioFileSize(FileManager.getFileManager().getAudioFileSize() - size);//设置音频新文件大小
                        filesize = FileManager.getFileManager().getAudioFileSize();//获取最新音频大小
                        break;
                    case R.id.rl_file_image://图像
                        FileManager.getFileManager().setImageFileSize(FileManager.getFileManager().getImageFileSize() - size);//设置图像新文件大小
                        filesize = FileManager.getFileManager().getImageFileSize();//获取最新图像大小
                        break;
                    case R.id.rl_file_zip://压缩包
                        FileManager.getFileManager().setZipFileSize(FileManager.getFileManager().getZipFileSize() - size);//设置压缩包新文件大小
                        filesize = FileManager.getFileManager().getZipFileSize();//获取最新压缩包大小
                        break;
                    case R.id.rl_file_apk://程序包
                        FileManager.getFileManager().setApkFileSize(FileManager.getFileManager().getApkFileSize() - size);//设置程序包新文件大小
                        filesize = FileManager.getFileManager().getApkFileSize();//获取最新程序包大小
                        break;
                }
//                initDatas();
//                fileAdapter.addDatas(fileInfos);
                //更新UI
                fileAdapter.notifyDataSetChanged();//更新ListView
                //更新文件数量
                fileNumber = fileAdapter.getDataFromAdapter().size();
                tv_file_number.setText(fileNumber+"个");
                //更新文件大小
                tv_file_size.setText(CommonUtils.getFileSize(filesize));
                System.gc();//主动调用垃圾回收器，回收垃圾
                Thread.yield();//放弃线程当前的执行权
            }
        }
    }
    //列表监听器，调用系统组件打开响应的文件
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            FileInfo fileInfo = (FileInfo) fileAdapter.getItem(position);//获取点击的item
            File file = fileInfo.getFile();//获取点击的文件
            String type = FileTypeUtil.getMIMEType(file);//拿到mime
            //通过mime指定组件打开文件
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);//启动组件打开文件
            intent.setDataAndType(Uri.fromFile(file),type);//设置数据和类型
            startActivity(intent);//启动组件
        }
    };
}
