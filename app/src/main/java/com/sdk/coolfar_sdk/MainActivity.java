package com.sdk.coolfar_sdk;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sdk.keepbackground.utils.DaemonEnv;
import com.sdk.keepbackground.utils.IntentWrapper;
import com.sdk.keepbackground.utils.SpManager;
import com.sdk.keepbackground.work.AbsWorkService;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLocaService();
    }

    private void initLocaService() {
        //忽略电池优化
        if(!SpManager.getInstance().getBoolean(SpManager.Keys.SP_IS_ACTION_WHITE_POWER)){
            IntentWrapper.whiteListMatters(this, "轨迹跟踪服务的持续运行");
            SpManager.getInstance().putBoolean(SpManager.Keys.SP_IS_ACTION_WHITE_POWER,true);
        }
        //开启保护
        DaemonEnv.sendStartWorkBroadcast(MainActivity.this);
        //启动work服务
        DaemonEnv.startServiceSafelyWithData(MainActivity.this,AbsWorkService.class);
    }

    //防止华为机型未加入白名单时按返回键回到桌面再锁屏后几秒钟进程被杀
    public void onBackPressed() {
        IntentWrapper.onBackPressed(this);
    }
}
