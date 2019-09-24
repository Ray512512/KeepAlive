package com.sdk.coolfar_sdk;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;

import com.sdk.keepbackground.work.AbsWorkService;

public class MyService extends AbsWorkService {
    public MyService() {
    }

    private boolean mIsRunning;

    @Override
    public Boolean needStartWorkService() {
        return true;
    }

    /**
     * 开启具体业务，实际执行与isWorkRunning方法返回值有关，
     * 当isWorkRunning返回false才会执行该方法
     */
    @Override
    public void startWork() {
        doWork();
    }

    private void doWork(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // do something
                mIsRunning=true;
            }
        }).start();
    }
    /**
     * 业务执行完成需要进行的操作
     * 手动停止服务sendStopWorkBroadcast时回调
     */
    @Override
    public void stopWork() {

    }

    /**
     * 任务是否正在运行? 由实现者处理
     * @return 任务正在运行, true; 任务当前不在运行, false; 无法判断, 什么也不做, null.
     */
    @Override
    public Boolean isWorkRunning() {
        return mIsRunning;
    }

    /**
     * 绑定远程service 可根据实际业务情况是否绑定自定义binder或者直接返回默认binder
     */
    @Override
    public IBinder onBindService(Intent intent, Void aVoid) {
        return new Messenger(new Handler()).getBinder();
    }

    /**
     * 服务被kill
     */
    @Override
    public void onServiceKilled() {

    }
}
