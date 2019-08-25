package com.sdk.keepbackground.watch;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.sdk.keepbackground.work.DaemonEnv;
import com.sdk.keepbackground.utils.ForegroundNotificationUtils;
import com.sdk.keepbackground.R;

/**
 * 后台播放无声音乐
 */
public class PlayMusicService extends Service {
    private boolean mNeedStop = false; //控制是否播放音频
    private MediaPlayer mMediaPlayer;
    private StopBroadcastReceiver stopBroadcastReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ForegroundNotificationUtils.startForegroundNotification(this);
        startRegisterReceiver();
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.no_notice);
        mMediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startPlayMusic();
        return START_STICKY;
    }

    private void startPlayMusic(){
        if (mMediaPlayer!=null && !mMediaPlayer.isPlaying() && !mNeedStop) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("sj_keep", "开始后台播放音乐");
                    mMediaPlayer.start();
                }
            }).start();
        }
    }

    private void stopPlayMusic() {
        if (mMediaPlayer != null) {
            Log.d("sj_keep", "关闭后台播放音乐");
            mMediaPlayer.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlayMusic();
        Log.d("sj_keep",  "----> stopPlayMusic ,停止服务");
        // 重启自己
        if (!mNeedStop) {
            Log.d("sj_keep",  "----> PlayMusic ,重启服务");
            Intent intent = new Intent(getApplicationContext(), PlayMusicService.class);
            startService(intent);
        }
    }

    private void startRegisterReceiver(){
        if (stopBroadcastReceiver == null){
            stopBroadcastReceiver = new StopBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(DaemonEnv.ACTION_CANCEL_JOB_ALARM_SUB);
            registerReceiver(stopBroadcastReceiver,intentFilter);
        }
    }

    private void startUnRegisterReceiver(){
        if (stopBroadcastReceiver != null){
            unregisterReceiver(stopBroadcastReceiver);
            stopBroadcastReceiver = null;
        }
    }

    /**
     * 停止自己
     */
    private void stopService(){
        mNeedStop = true;
        stopPlayMusic();
        startUnRegisterReceiver();
        stopSelf();
    }

    class StopBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            stopService();
        }
    }
}
