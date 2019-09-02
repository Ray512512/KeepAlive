package com.sdk.keepbackground.singlepixel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;


import com.sdk.keepbackground.watch.WatchDogService;



/**
 * 该Activity的View只要设置为1像素然后设置在Window对象上即可。在Activity的onDestroy周期中进行保活服务的存活判断从而唤醒服务。
 * 运行在:watch进程, 为了提高watch进程的优先级 oom_adj值越小，优先级越高。
 * copy from shihu wang
 */
public class SinglePixelActivity extends Activity {

    private static final String TAG = SinglePixelActivity.class.getSimpleName();

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window mWindow = getWindow();
        mWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams attrParams = mWindow.getAttributes();
        attrParams.x = 0;
        attrParams.y = 0;
        attrParams.height = 1;
        attrParams.width = 1;
        mWindow.setAttributes(attrParams);
        ScreenManager.getInstance(this).setSingleActivity(this);
    }

    @Override
    protected void onDestroy() {
//        if (!SystemUtils.isAppAlive(this, Constant.PACKAGE_NAME)) {
        Log.d("sj_keep", " 1 像素Activity --- onDestroy");
            Intent intentAlive = new Intent(this, WatchDogService.class);
            startService(intentAlive);
//        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isScreenOn()) {
            finishSelf();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        finishSelf();
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        finishSelf();
        return super.onTouchEvent(motionEvent);
    }

    public void finishSelf() {
        if (!isFinishing()) {
            finish();
        }
    }


    private boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            return powerManager.isInteractive();
        } else {
            return powerManager.isScreenOn();
        }
    }
}
