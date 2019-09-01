package com.baroneye.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.baroneye.Receiver.lockScreenReceiver;

public class MyService extends Service{
    BroadcastReceiver mReceiver;
    // Intent myIntent;
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    @Override
    public void onCreate() {
        Log.d("LockTag", "IsLock?");

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        mReceiver = new lockScreenReceiver();
        registerReceiver(mReceiver, filter);
        Log.d("LockTag", "IsLock?");

        super.onCreate();
    }
    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub

        super.onStart(intent, startId);
    }



    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        Log.d("TAG", "UnRegisteR");
        super.onDestroy();
    }
}