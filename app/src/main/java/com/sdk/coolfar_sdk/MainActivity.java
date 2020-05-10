package com.sdk.coolfar_sdk;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;


import com.sdk.keepbackground.work.DaemonEnv;
import com.sdk.keepbackground.work.IntentWrapper;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLocaService();
    }

    private void initLocaService() {
        //初始化
        DaemonEnv.init(this);
        //請求用戶忽略电池优化
        String reason="轨迹跟踪服务的持续运行";
        DaemonEnv.whiteListMatters(this, reason);
        //启动work服务
        DaemonEnv.startServiceSafelyWithData(MainActivity.this,MyService.class);
    }

    //防止华为机型未加入白名单时按返回键回到桌面再锁屏后几秒钟进程被杀
    @Override
    public void onBackPressed() {
        IntentWrapper.onBackPressed(this);
    }
}
