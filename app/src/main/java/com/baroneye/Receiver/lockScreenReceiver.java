package com.baroneye.Receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baroneye.LockScreenAppActivity;
import com.baroneye.Service.SensorService;
import com.baroneye.util.Configure;

public class lockScreenReceiver extends BroadcastReceiver {
    public static boolean wasScreenOn = true;


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("Receiver", "receive?");
        //Toast.makeText(context, "" + "enterrrrrr", Toast.LENGTH_SHORT).show();
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            //Toast.makeText(context, "" + "screeen off", Toast.LENGTH_SHORT).show();

            ((SensorService)SensorService.mContext).RemoveWindow();

            if(Configure.is_lock_screen)
                return;

            wasScreenOn=false;

            Intent intent11 = new Intent(context,LockScreenAppActivity.class);
            intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Log.d("TAG", "Open Intent");
            Configure.is_lock_screen = true;
            context.startActivity(intent11);

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            try {
                if (SensorService.mContext != null) {
                    ((SensorService) SensorService.mContext).RemoveWindow();
                }
            }
            catch (Exception e)
            {

            }

            wasScreenOn=true;
            Intent intent11 = new Intent(context,LockScreenAppActivity.class);
            intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            ((SensorService)SensorService.mContext).RemoveWindow();

        }
        else if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {

        }

    }


}
