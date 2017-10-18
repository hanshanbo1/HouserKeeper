package com.edu.nbl.houserkeeper.mian;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.edu.nbl.houserkeeper.R;
import com.edu.nbl.houserkeeper.biz.FileManager;
import com.edu.nbl.houserkeeper.utils.CommonUtils;

public class FilemgrActivity extends BaseActivity {
    private View rl_file_all, rl_file_text, rl_file_video, rl_file_audio, rl_file_image, rl_file_apk, rl_file_zip;
    private TextView tv_file_size,tv_file_all, tv_file_text, tv_file_video, tv_file_audio, tv_file_image, tv_file_apk, tv_file_zip;
    private ProgressBar pb_file_all, pb_file_text, pb_file_video, pb_file_audio, pb_file_image, pb_file_apk, pb_file_zip;
    private ImageView iv_file_all, iv_file_text, iv_file_video, iv_file_audio, iv_file_image, iv_file_apk, iv_file_zip;
    private FileManager fileManager;//文件管理器
    private Thread thread;//子线程
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO: 2017/8/21 处理我们接受过来的消息
            myHandleMessage(msg);
        }
    };

    //处理接受来的消息
    public void myHandleMessage(Message msg) {
        //通过what区分消息
        if (msg.what == 1) {//表示正在搜索
            setDatas();
        }
        if (msg.what == 2) {//表示结束搜素
            setDatas();
            //progressBar
            pb_file_all.setVisibility(View.GONE);
            pb_file_text.setVisibility(View.GONE);
            pb_file_video.setVisibility(View.GONE);
            pb_file_audio.setVisibility(View.GONE);
            pb_file_image.setVisibility(View.GONE);
            pb_file_zip.setVisibility(View.GONE);
            pb_file_apk.setVisibility(View.GONE);
            //右边箭头出来
            iv_file_all.setVisibility(View.VISIBLE);
            iv_file_text.setVisibility(View.VISIBLE);
            iv_file_video.setVisibility(View.VISIBLE);
            iv_file_audio.setVisibility(View.VISIBLE);
            iv_file_image.setVisibility(View.VISIBLE);
            iv_file_zip.setVisibility(View.VISIBLE);
            iv_file_apk.setVisibility(View.VISIBLE);
        }
    }

    private void setDatas() {
        Log.d("TAG", "tv_file_size="+tv_file_size+"fileManager="+fileManager);
        tv_file_size.setText(CommonUtils.getFileSize(fileManager.getAllFileSize()));//设置已发现文件大小
        tv_file_all.setText(CommonUtils.getFileSize(fileManager.getAllFileSize()));//设置全部文件大小
        tv_file_text.setText(CommonUtils.getFileSize(fileManager.getTextFileSize()));//设置文档文件大小
        tv_file_video.setText(CommonUtils.getFileSize(fileManager.getVideoFileSize()));//设置视频文件大小
        tv_file_audio.setText(CommonUtils.getFileSize(fileManager.getAudioFileSize()));//设置音频文件大小
        tv_file_image.setText(CommonUtils.getFileSize(fileManager.getImageFileSize()));//设置图像文件大小
        tv_file_zip.setText(CommonUtils.getFileSize(fileManager.getZipFileSize()));//设置压缩包文件大小
        tv_file_apk.setText(CommonUtils.getFileSize(fileManager.getApkFileSize()));//设置程序包文件大小
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filemgr);
        //初始化ActionBar
        initActiongBar("文件管理", R.drawable.btn_homeasup_default, -1, listener);
        //初始化控件
        initView();
        //异步加载数据
        asyncLoadData();
    }

    //异步加载数据
    private void asyncLoadData() {
        fileManager = FileManager.getFileManager();//单例模式获取实例
        fileManager.setSearchFileListener(searchListener);//搜素监听
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //子线程执行的耗时任务
                fileManager.searchSDCardFile();
            }
        });
        thread.start();
    }

    //初始化控件
    private void initView() {
        rl_file_all = findViewById(R.id.rl_file_all);//全部
        rl_file_text = findViewById(R.id.rl_file_text);//文档
        rl_file_video = findViewById(R.id.rl_file_video);//视频
        rl_file_audio = findViewById(R.id.rl_file_audio);//音频
        rl_file_image = findViewById(R.id.rl_file_image);//图片
        rl_file_zip = findViewById(R.id.rl_file_zip);//压缩包
        rl_file_apk = findViewById(R.id.rl_file_apk);//程序包

        tv_file_size = (TextView) findViewById(R.id.tv_filesize);//已发现
        tv_file_all = (TextView) findViewById(R.id.tv_file_all);//全部
        tv_file_text = (TextView) findViewById(R.id.tv_file_text);//文档
        tv_file_video = (TextView) findViewById(R.id.tv_file_video);//视频
        tv_file_audio = (TextView) findViewById(R.id.tv_file_audio);//音频
        tv_file_image = (TextView) findViewById(R.id.tv_file_image);//图片
        tv_file_apk = (TextView) findViewById(R.id.tv_file_apk);//程序包
        tv_file_zip = (TextView) findViewById(R.id.tv_file_zip);//压缩包

        pb_file_all = (ProgressBar) findViewById(R.id.pb_file_all);//全部
        pb_file_text = (ProgressBar) findViewById(R.id.pb_file_text);//文档
        pb_file_video = (ProgressBar) findViewById(R.id.pb_file_video);//视频
        pb_file_audio = (ProgressBar) findViewById(R.id.pb_file_audio);//音频
        pb_file_image = (ProgressBar) findViewById(R.id.pb_file_image);//图片
        pb_file_apk = (ProgressBar) findViewById(R.id.pb_file_apk);//程序包
        pb_file_zip = (ProgressBar) findViewById(R.id.pb_file_zip);//压缩包

        iv_file_all = (ImageView) findViewById(R.id.iv_file_all);//全部
        iv_file_text = (ImageView) findViewById(R.id.iv_file_text);//文档
        iv_file_video = (ImageView) findViewById(R.id.iv_file_video);//视频
        iv_file_audio = (ImageView) findViewById(R.id.iv_file_audio);//音频
        iv_file_image = (ImageView) findViewById(R.id.iv_file_image);//图片
        iv_file_apk = (ImageView) findViewById(R.id.iv_file_apk);//程序包
        iv_file_zip = (ImageView) findViewById(R.id.iv_file_zip);//压缩包
        //设置监听
        rl_file_all.setOnClickListener(listener);
        rl_file_text.setOnClickListener(listener);
        rl_file_video.setOnClickListener(listener);
        rl_file_audio.setOnClickListener(listener);
        rl_file_image.setOnClickListener(listener);
        rl_file_zip.setOnClickListener(listener);
        rl_file_apk.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_left:
                    finish();//销毁当前页
                    break;
                case R.id.rl_file_all://全部
                case R.id.rl_file_text://文档
                case R.id.rl_file_video://视频
                case R.id.rl_file_audio://音频
                case R.id.rl_file_image://图片
                case R.id.rl_file_zip://压缩包
                case R.id.rl_file_apk://程序包
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", v.getId());//传递我们点击的按钮id
                    Intent intent = new Intent(FilemgrActivity.this, FilemgrShowActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,100);
                    break;
            }
        }
    };
    //发消息
    //搜素文件监听
    public FileManager.SearchFileListener searchListener = new FileManager.SearchFileListener() {
        @Override
        public void searching() {//表示正在搜索
            Message message = handler.obtainMessage();
            message.what = 1;
            handler.sendMessage(message);//如果what是1表示正在搜索
        }

        @Override
        public void end(boolean isExceptionEnd) {//搜索结束
            Message message = handler.obtainMessage();
            message.what = 2;
            handler.sendMessage(message);//如果what是2表示结束搜索
        }
    };
    //重新加载数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==100){
            tv_file_size.setText(CommonUtils.getFileSize(fileManager.getAllFileSize()));//总文件更新
            tv_file_all.setText(CommonUtils.getFileSize(fileManager.getAllFileSize()));//全部文件
            tv_file_text.setText(CommonUtils.getFileSize(fileManager.getTextFileSize()));//文档文件
            tv_file_video.setText(CommonUtils.getFileSize(fileManager.getVideoFileSize()));//视频文件
            tv_file_audio.setText(CommonUtils.getFileSize(fileManager.getAudioFileSize()));//音频文件
            tv_file_image.setText(CommonUtils.getFileSize(fileManager.getImageFileSize()));//图像文件
            tv_file_zip.setText(CommonUtils.getFileSize(fileManager.getZipFileSize()));//压缩包文件
            tv_file_apk.setText(CommonUtils.getFileSize(fileManager.getApkFileSize()));//程序包文件
        }
    }

    //重写onDestroy方法
    @Override
    protected void onDestroy() {
        super.onDestroy();
        fileManager.setStopSearch(true);//设置停止搜索为true
        fileManager.initData();
        thread.interrupt();
        thread = null;
    }
}
