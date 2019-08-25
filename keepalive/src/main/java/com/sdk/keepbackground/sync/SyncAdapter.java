package com.sdk.keepbackground.sync;

/**
 */

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.sdk.keepbackground.work.DaemonEnv;
import com.sdk.keepbackground.watch.WatchDogService;


public class SyncAdapter extends AbstractThreadedSyncAdapter {


    private Context mContext;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        DaemonEnv.startServiceSafely(mContext, WatchDogService.class);
    }
}