package com.sdk.keepbackground;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;


import java.util.List;

/**
 * Desc    :
 * Creator : sj
 * Date    : 2019/8/13
 * Modifier:
 */
public class KeepAliveApplication extends Application {
    private static KeepAliveApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        String processName = getProcessName(this.getApplicationContext());
        if ("com.sdk.keepbackground".equals(processName)){
            // 主进程 进行一些其他的操作
            Log.d("sj_keep", "启动主进程");
        }else if ("com.sdk.keepbackground:work".equals(processName)){
            Log.d("sj_keep", "启动了工作进程");
        }else if ("com.sdk.keepbackground:watch".equals(processName)){
            // 这里要设置下看护进程所启动的主进程信息
            Log.d("sj_keep", "启动了观察进程");
        }
    }

    //创建一个静态的方法，以便获取context对象
    public static KeepAliveApplication getInstance() {
        return instance;
    }

    public static String getProcessName(Context context){
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) {
            return null;
        }
        List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
        if (processes == null){
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo info : processes){
            if (info.pid == pid){
                return info.processName;
            }
        }
        return null;
    }
}
