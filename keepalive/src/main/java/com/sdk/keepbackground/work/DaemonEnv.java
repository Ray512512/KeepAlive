package com.sdk.keepbackground.work;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import com.sdk.keepbackground.utils.JumpWindowPemiManagement;
import com.sdk.keepbackground.utils.NotificationSetUtil;
import com.sdk.keepbackground.utils.SpManager;
import com.sdk.keepbackground.watch.AbsServiceConnection;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.sdk.keepbackground.work.IntentWrapper.getApplicationName;


/**
 *环境配置 每个进程独享一份 不同的进程，所有的静态和单例都会失效
 *
 * 这里将Daemon类 做成工具类
 *
 * 每一个进程都会有一个Daemon类
 *
 */
public final class DaemonEnv {

    /**
     * 向 WakeUpReceiver 发送带有此 Action 的广播, 即可在不需要服务运行的时候取消 Job / Alarm / Subscription.
     */
    public static final String ACTION_START_JOB_ALARM_SUB = "com.sdk.START_JOB_ALARM_SUB";
    public static final String ACTION_CANCEL_JOB_ALARM_SUB = "com.sdk.CANCEL_JOB_ALARM_SUB";

    public static final int DEFAULT_WAKE_UP_INTERVAL = 2 * 60 * 1000; // 默认JobScheduler 唤醒时间为 2 分钟
    public static final int MINIMAL_WAKE_UP_INTERVAL = 60 * 1000; // 最小时间为 1 分钟

    public static Context app;
    public static void init(Context context){
        //开启保护
        app=context.getApplicationContext();
        DaemonEnv.sendStartWorkBroadcast(context);
    }
    public static void startServiceMayBind( final Context context,
                                     final Class<? extends Service> serviceClass,
                                     AbsServiceConnection connection) {

        // 判断当前绑定的状态
        if (!connection.mConnectedState) {
            Log.d("sj_keep", "启动并绑定服务 ："+serviceClass.getSimpleName());
            final Intent intent = new Intent(context, serviceClass);
            startServiceSafely(context, serviceClass);
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    public static void startServiceSafely(Context context, Class<? extends Service> i) {
        Log.d("sj_keep", "安全启动服务。。: "+i.getSimpleName());
        try {
            if (Build.VERSION.SDK_INT >= 26){
                context.startForegroundService(new Intent(context,i));
            }else {
                context.startService(new Intent(context,i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void startServiceSafelyWithData(Context context, Class<? extends Service> i){
        try {
            if (Build.VERSION.SDK_INT >= 26){
                context.startForegroundService(new Intent(context,i));
            }else {
                context.startService(new Intent(context,i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getWakeUpInterval(int sWakeUpInterval) {
        return Math.max(sWakeUpInterval, MINIMAL_WAKE_UP_INTERVAL);
    }


    public static void safelyUnbindService(Service service, AbsServiceConnection mConnection){
        try{
            if (mConnection.mConnectedState) {
                service.unbindService(mConnection);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 当前哪个进程使用的时候 就用其上下文发送广播
     *
     * 如果是同一进程，可以自定义启动方式 不使用广播的模式
     */
    public static void sendStartWorkBroadcast(Context context) {
        Log.d("sj_keep", "发送开始广播。。。。");
        // 以广播的形式通知所有进程终止
        context.sendBroadcast(new Intent(ACTION_START_JOB_ALARM_SUB));
    }


    /**
     * 当前哪个进程使用的时候 就用其上下文发送广播
     */
    public static void sendStopWorkBroadcast(Context context) {
        Log.d("sj_keep", "发送停止广播。。。。");
        // 以广播的形式通知所有进程终止
        context.sendBroadcast(new Intent(ACTION_CANCEL_JOB_ALARM_SUB));
    }

    /**
     * 后台允许白名单
     * @param a
     * @param reason
     */
    public static void whiteListMatters(final Activity a, String reason){
        try{
            if (ContextCompat.checkSelfPermission(a, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {//判断是否已经赋予权限
                ActivityCompat.requestPermissions(a,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            IntentWrapper.whiteListMatters(a, reason);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 后台允许白名单
     * @param a
     * @param reason
     * @param doAll  将所有影响保活开关都打开
     */
    public static void whiteListMatters(final Activity a, String reason,boolean doAll){
        try{
            IntentWrapper.whiteListMatters(a, reason);
            if(doAll){
                openPushSwitch(a,reason);
                checkWindowPerission(a,reason);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 打开通知权限
     * @param context
     * @param reason
     */
    public static void openPushSwitch(final Context context, String reason){
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle("需要开启 " + getApplicationName(context) + " 的通知开关")
                .setMessage(reason + "需要 " + getApplicationName(context) + " 开启通知管理开关。\n\n" +
                        "请点击『确定』，在弹出的『通知管理』页面打开允许通知选项。")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int w) {
                        NotificationSetUtil.OpenNotificationSetting(context, null);
                    }
                })
                .show();
    }

    /**
     * 检测悬浮窗权限
     * 兼容6.0及以下
     */
    public static void checkWindowPerission(final Context context, String reason) {
        if(!SpManager.getInstance().getBoolean(SpManager.Keys.IS_HINT_SYSTEM_WINDOW)){
            new AlertDialog.Builder(context)
                    .setCancelable(false)
                    .setTitle("需要开启 " + getApplicationName(context) + " 的悬浮窗权限")
                    .setMessage(reason + "需要 " + getApplicationName(context) + " 开启悬浮窗开关。\n\n" +
                            "请点击『确定』，在弹出的『悬浮窗』页面打开允许在其他应用上层显示开关。")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface d, int w) {
                            SpManager.getInstance().putBoolean(SpManager.Keys.IS_HINT_SYSTEM_WINDOW,true);
                            JumpWindowPemiManagement.goToWindow(context);
                        }
                    })
                    .show();
        }else {
            windowPermissionPassWithCheck(context,reason);
        }
    }

    private static void windowPermissionPassWithCheck(final Context context, String reason) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !JumpWindowPemiManagement.hasWindowPei(context)) {
            new AlertDialog.Builder(context)
                    .setCancelable(false)
                    .setTitle("需要开启 " + getApplicationName(context) + " 的悬浮窗权限")
                    .setMessage(reason + "需要 " + getApplicationName(context) + " 开启悬浮窗开关。\n\n" +
                            "请点击『确定』，在弹出的『悬浮窗』页面打开允许在其他应用上层显示开关。")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface d, int w) {
                            SpManager.getInstance().putBoolean(SpManager.Keys.IS_HINT_SYSTEM_WINDOW,true);
                            JumpWindowPemiManagement.goToWindow(context);
                            Intent intent =new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
                            context.startActivity(intent);
                        }
                    })
                    .show();
            }
    }
}
