package com.sdk.keepbackground.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 授权此服务提供给SyncAdapter framework，用于调用Authenticator的方法
 */

public class AuthenticatorService extends Service {

    private Authenticator mAuthenticator;
    public AuthenticatorService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}