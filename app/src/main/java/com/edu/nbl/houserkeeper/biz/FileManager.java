package com.edu.nbl.houserkeeper.biz;

import android.util.Log;

import com.edu.nbl.houserkeeper.entity.FileInfo;
import com.edu.nbl.houserkeeper.utils.FileTypeUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by HanShanBo on 2017/8/21.
 * 检索内外置sdcard的所以文件
 * 内置sdcard：mnt/shell/emulated/0
 * 外置sdcard：storage/sdcard1
 */

public class FileManager {
    private static File inSdcard;

    private static File outSdcard;

    //静态代码块
    static {
        //获取手机内置sdcard的路径
        if (MemoryManager.getPhoneInSDCardPath() != null) {//判断内置sdcard路径是否存在
            inSdcard = null;
            inSdcard = new File(MemoryManager.getPhoneInSDCardPath());//构建sdcard：mnt/shell/emulated/0目录
        }
        //获取手机外置sdcard路径（在模拟器上不存在）
        if (MemoryManager.getPhoneOutSDCardPath() != null) {
            outSdcard = null;
            outSdcard = new File(MemoryManager.getPhoneOutSDCardPath());
        }
    }

    //单例模式
    private FileManager() {

    }

    private static FileManager fileManager;

    //懒汉式 调用方法实例化
    public static FileManager getFileManager() {
        if (fileManager == null) {
            fileManager = new FileManager();
        }
        return fileManager;
    }

    //是否停止搜索
    private boolean isStopSearch = false;

    //get方法
    public boolean iaStopSearch() {
        return isStopSearch;
    }

    //set方法
    public void setStopSearch(boolean isStopSearch) {
        this.isStopSearch = isStopSearch;
    }

    //用不同的集合来区分不同的文件
    private ArrayList<FileInfo> allFileList = new ArrayList<>();//全部文件
    private long allFileSize;//全部文件大小
    private ArrayList<FileInfo> textFileList = new ArrayList<>();//所以有文档文件
    private long textFileSize;//所以文档文件大小
    private ArrayList<FileInfo> videoFileList = new ArrayList<>();//所以视频文件
    private long videoFileSize;//所以视频文件大小
    private ArrayList<FileInfo> audioFileList = new ArrayList<>();//所有音频文件
    private long audioFileSize;//所有音频文件大小
    private ArrayList<FileInfo> imageFileList = new ArrayList<>();//所有图像文件
    private long imageFileSize;//所有图像文件大小
    private ArrayList<FileInfo> zipFileList = new ArrayList<>();//所有压缩包文件
    private long zipFileSize;//所有压缩包文件大小
    private ArrayList<FileInfo> apkFileList = new ArrayList<>();//所有程序包文件
    private long apkFileSize;//所有程序包文件大小

    public boolean isStopSearch() {
        return isStopSearch;
    }

    public ArrayList<FileInfo> getAllFileList() {
        return allFileList;
    }

    public void setAllFileList(ArrayList<FileInfo> allFileList) {
        this.allFileList = allFileList;
    }

    public long getAllFileSize() {
        return allFileSize;
    }

    public void setAllFileSize(long allFileSize) {
        this.allFileSize = allFileSize;
    }

    public ArrayList<FileInfo> getTextFileList() {
        return textFileList;
    }

    public void setTextFileList(ArrayList<FileInfo> textFileList) {
        this.textFileList = textFileList;
    }

    public long getTextFileSize() {
        return textFileSize;
    }

    public void setTextFileSize(long textFileSize) {
        this.textFileSize = textFileSize;
    }

    public ArrayList<FileInfo> getVideoFileList() {
        return videoFileList;
    }

    public void setVideoFileList(ArrayList<FileInfo> videoFileList) {
        this.videoFileList = videoFileList;
    }

    public long getVideoFileSize() {
        return videoFileSize;
    }

    public void setVideoFileSize(long videoFileSize) {
        this.videoFileSize = videoFileSize;
    }

    public ArrayList<FileInfo> getAudioFileList() {
        return audioFileList;
    }

    public void setAudioFileList(ArrayList<FileInfo> audioFileList) {
        this.audioFileList = audioFileList;
    }

    public long getAudioFileSize() {
        return audioFileSize;
    }

    public void setAudioFileSize(long audioFileSize) {
        this.audioFileSize = audioFileSize;
    }

    public ArrayList<FileInfo> getImageFileList() {
        return imageFileList;
    }

    public void setImageFileList(ArrayList<FileInfo> imageFileList) {
        this.imageFileList = imageFileList;
    }

    public long getImageFileSize() {
        return imageFileSize;
    }

    public void setImageFileSize(long imageFileSize) {
        this.imageFileSize = imageFileSize;
    }

    public ArrayList<FileInfo> getZipFileList() {
        return zipFileList;
    }

    public void setZipFileList(ArrayList<FileInfo> zipFileList) {
        this.zipFileList = zipFileList;
    }

    public long getZipFileSize() {
        return zipFileSize;
    }

    public void setZipFileSize(long zipFileSize) {
        this.zipFileSize = zipFileSize;
    }

    public ArrayList<FileInfo> getApkFileList() {
        return apkFileList;
    }

    public void setApkFileList(ArrayList<FileInfo> apkFileList) {
        this.apkFileList = apkFileList;
    }

    public long getApkFileSize() {
        return apkFileSize;
    }

    public void setApkFileSize(long apkFileSize) {
        this.apkFileSize = apkFileSize;
    }

    //搜索存储卡目录下的所以文件，保存到allFileList
    public void searchSDCardFile() {
        if (allFileList == null || allFileList.size() <= 0) {
            //搜索
            initData();//初始化数据
            //搜索文件
            searchFile(inSdcard, false);//搜索内置inSdcard,false表示搜索不结束
            searchFile(outSdcard, true);//搜索外置outSdcard,true表示搜索结束
        }
    }

    //搜索存储空间
    private void searchFile(File file, boolean isFirstRunFlag) {
        //使用递归搜索所有文件
        if (isStopSearch) {//第一次是false
            if (isFirstRunFlag) {//是否搜索结束 当搜索外置sdcard时为false
                // TODO: 2017/8/21 通过回调函数停止搜索
                callBackSearchEndFileListener(true);//非正常终止
            }
            return;
        }
        //第一次搜索
        //排除不正常文件
        if (file == null || !file.canRead() || !file.exists()) {//又不能读又为空或不存在 外置sdcard满足条件
            if (isFirstRunFlag) {
                // TODO: 2017/8/21 通过回调函数停止搜索
                callBackSearchEndFileListener(true);//异常终止
            }
            return;
        }

        if (!file.isDirectory()) {//是文件
            //判断不正常文件
            if (file.length() <= 0) {//例如 a.text里面什么都没有
                return;
            }
            //a.text找不到“.”为-1
            if (file.getName().lastIndexOf(".") == -1) {//没有以"."作为后缀名的
                return;
            }
            //正常文件
            //获取文件类型（后缀名,还有对应文件的图标）
            String[] iconAndType = FileTypeUtil.getFileIconAndTypeName(file);
            String iconname = iconAndType[0];//此为文件icon名
            String typename = iconAndType[1];//此为文件所属类型
            //构建一个FileInfo实体
            FileInfo fileInfo = new FileInfo(false, iconname, typename, file);
            Log.d("TAG", "fileInfo="+fileInfo);
            allFileList.add(fileInfo);//将一个文件添加到全部文件中
            allFileSize += file.length();//讲一个文件的大小添加到全部文件大小中
            //typename = text
            if (typename.equals(FileTypeUtil.TYPE_TXT)) {//后缀名匹配上
                textFileList.add(fileInfo);
                textFileSize += file.length();
            } else if (typename.equals(FileTypeUtil.TYPE_VIDEO)) {//视频
                videoFileList.add(fileInfo);
                videoFileSize += file.length();
            } else if (typename.equals(FileTypeUtil.TYPE_AUDIO)) {//音频
                audioFileList.add(fileInfo);
                audioFileSize += file.length();
            } else if (typename.equals(FileTypeUtil.TYPE_IMAGE)) {//图像
                imageFileList.add(fileInfo);
                imageFileSize += file.length();
            } else if (typename.equals(FileTypeUtil.TYPE_ZIP)) {//压缩包
                zipFileList.add(fileInfo);
                zipFileSize += file.length();
            } else if (typename.equals(FileTypeUtil.TYPE_APK)) {//程序包
                apkFileList.add(fileInfo);
                apkFileSize += file.length();
            }
            // TODO: 2017/8/21 通知数据更新
            //拿到一个文件，更新一个文件
            callBackSearchingFileListener(typename);//txt
            return;
        }
        //是目录
        File[] files = file.listFiles();//拿到此目录下所有东西
        if (files == null || files.length <= 0) {
            return;//直接结束方法
        }
        //遍历循环所有东西
        for (int i = 0; i < files.length; i++) {
            File tmpFile = files[i];//拿到其中一个东西
            //通过递归再去检索文件
            searchFile(tmpFile, false);//表示没有结束
        }
        //遍历完所有的文件
        if (isFirstRunFlag) {//递归检索完所有的文件时，结束了
            // TODO: 2017/8/21 更新UI
            callBackSearchEndFileListener(false);//正常结束
        }
    }

    //初始化数据
    public void initData() {
        isStopSearch = false;//不停止搜索
        //清空数据
        allFileList.clear();
        textFileList.clear();
        videoFileList.clear();
        audioFileList.clear();
        imageFileList.clear();
        zipFileList.clear();
        apkFileList.clear();
        //文件大小初始化
        allFileSize = 0;
        textFileSize = 0;
        videoFileSize = 0;
        audioFileSize = 0;
        imageFileSize = 0;
        zipFileSize = 0;
        apkFileSize = 0;
    }

    //搜索监听器 正在搜索和搜搜结束
    private SearchFileListener searchFileListener;

    public interface SearchFileListener {
        void searching();//每搜索到一个文件就调用一次

        void end(boolean isExceptionEnd);//搜索结束之后调用
    }

    //设置监听器
    public void setSearchFileListener(SearchFileListener listener) {
        searchFileListener = listener;
    }

    //用来回调SearchFileListener接口内的searching方法
    private void callBackSearchingFileListener(String typename) {//txt
        if (searchFileListener != null) {
            searchFileListener.searching();//正在搜索
        }
    }
    //用来回调SearchFileListener接口内的end方法
    private void callBackSearchEndFileListener(boolean isException){
        if (searchFileListener!=null){
            searchFileListener.end(isException);//搜索结束
        }
    }
    //获取文件或文件的总大小
    public static long getFileSize(File file){
        long size = 0;//局部变量初始化
        if (!file.isDirectory()){//是文件
            return file.length();
        }
        File[] files = file.listFiles();//拿到File目录下的所以
        for (int i = 0;i<files.length;i++){
            size = size+getFileSize(files[i]);//递归
        }
        return size;
    }
    public static void deleteFile(File f){//传一个文件过来
        if (f.isDirectory()){//是文件夹
            File[] files = f.listFiles();
            for (int i = 0;i<files.length;i++){
                deleteFile(files[i]);//递归
            }
        }else{
            f.delete();
        }
    }
}
