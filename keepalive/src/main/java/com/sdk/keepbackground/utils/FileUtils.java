package com.sdk.keepbackground.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

/**
 * Created by Ray on 2020/1/6.
 */
public class FileUtils {
    private static final String TAG = "FileUtils";

    //包名地址
    public static final String FILE_PKG_DIRRECT = "/mnt/sdcard/keepalive/";
    public static final String FILE_SERVICE_NAME = "service.txt";
    public static final String FILE_PKG_PATH = FILE_PKG_DIRRECT + FILE_SERVICE_NAME;

    public static String  readTxtFile(String path) {
        StringBuilder content = new StringBuilder();     //文件内容字符串
        File file = new File(path);    //打开文件
        if (!file.exists()) {
            return content.toString();
        }
        if (file.isDirectory())    //如果path是传递过来的参数，可以做一个非目录的判断
        {
            Log.d("TestFile", "The File doesn't not exist.");
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                //分行读取
                while ((line = buffreader.readLine()) != null) {
                    content.append(line);
                }
                instream.close();
            } catch (java.io.FileNotFoundException e) {
                Log.d("TestFile", "The File doesn't not exist.");
            } catch (IOException e) {
                Log.d("TestFile", e.getMessage());
            }
        }
        return content.toString();
    }


    public static void writeTxtToFile(final String strcontent, final String path, final String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                makeFilePath(path, name);
                String strFilePath = path + name;
                if (!strFilePath.contains(".txt")) {
                    strFilePath += ".txt";
                }
//            String strContent = TimeUtils.milliseconds2String(System.currentTimeMillis()) + strcontent + "\r\n";
                try {
                    File file = new File(strFilePath);
                    if (file.exists()) {
                        file.delete();
                    }
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                    RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                    raf.seek(file.length());
                    raf.write(strcontent.getBytes("UTF-8"));
                    raf.close();
                } catch (Exception e) {
                    Log.e("TestFile", "Error on write File:" + e);
                }
            }
        }).start();
    }


    private static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

}