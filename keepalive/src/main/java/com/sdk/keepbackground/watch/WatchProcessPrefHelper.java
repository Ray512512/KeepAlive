package com.sdk.keepbackground.watch;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.sdk.keepbackground.utils.FileUtils;
import com.sdk.keepbackground.utils.SpManager;
import com.sdk.keepbackground.work.AbsWorkService;

import static com.sdk.keepbackground.utils.SpManager.Keys.WORK_SERVICE;


/**
 * 用于多进程通讯的 SharedPreferences
 *
 * 此处存在着风险  github --> PreferencesProvider 项目
 */

public class WatchProcessPrefHelper {

    private static final String SHARED_UTILS = "watch_process";

    private static final String KEY_IS_START_DAEMON = "is_start_sport"; // 是否开始了一次保活（做为保活的判断依据）

    // 多进程时，尽量少用静态、单例 此处不得已
    public static Class<? extends AbsWorkService> mWorkServiceClass;

    public static Class<? extends AbsWorkService> getWorkService(){
        if(mWorkServiceClass==null){
            try {
                String localC= "";
                try{
                    localC=SpManager.getInstance().getString(WORK_SERVICE);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(TextUtils.isEmpty(localC)){
                    localC=FileUtils.readTxtFile(FileUtils.FILE_PKG_PATH);
                }
                Log.v("mWorkServiceClass","保活目标服务："+localC);
                mWorkServiceClass= (Class<? extends AbsWorkService>) Class.forName(localC);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return mWorkServiceClass;
    }

    public static void setIsStartSDaemon(Context context,boolean mapType){
        context.getSharedPreferences(SHARED_UTILS, Context.MODE_MULTI_PROCESS).edit().putBoolean(KEY_IS_START_DAEMON, mapType).apply();
    }

    public static boolean getIsStartDaemon(Context context){
        return context.getSharedPreferences(SHARED_UTILS, Context.MODE_MULTI_PROCESS).getBoolean(KEY_IS_START_DAEMON, false);
    }


}
