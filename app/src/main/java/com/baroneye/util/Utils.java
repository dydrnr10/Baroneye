package com.baroneye.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.baroneye.Service.OnOffService;
import com.baroneye.R;

import com.baroneye.YoutubePlayer;

import java.util.Date;


public class Utils {

    private static Activity mActivity;

    public synchronized static void setActivity(Activity activity) {
        mActivity = activity;
    }

    public static Activity getActivity() {
        return mActivity;
    }

    public static Context getContext() {
        if(mActivity != null) {
            return mActivity.getApplicationContext();
        }
        return null;
    }

    public static void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(final String msg, final int time) {
        if(mActivity == null) {
            return;
        }

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mActivity, msg, time).show();
            }
        });
    }





    private static ProgressDialog dialog;



    public static void showProgressDialog(Activity activity, String title, String msg) {
        if (activity == null) {
            return;
        }

        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = ProgressDialog.show(activity, title, msg, true, false);
    }

    public static void showProgressDialog(String title, String msg) {
        showProgressDialog(mActivity, title, msg);
    }

    public static void hideProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }



    public static int getColor(int resId) {
        if (Build.VERSION.SDK_INT >= 23) {
            return getContext().getResources().getColor(resId, getContext().getTheme());
        } else {
            return getContext().getResources().getColor(resId);
        }
    }


    public static int getDeviceWidth() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        return width;
    }

    public static int getDeviceHeight() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        return height;
    }

    public static int dpTopx(int dp) {
        Resources r = getContext().getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                r.getDisplayMetrics());
        return px;
    }


    public static void save_pref(String key, String data) {
        Context ctx = Utils.getContext();
        if (ctx == null) {
            return;
        }
        SharedPreferences pref = ctx.getSharedPreferences(com.baroneye.util.Configure.KEY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putString(key, data);
        ed.commit();
    }

    static public String load_pref_str(String key, String def) {
        try {
            Context ctx = Utils.getContext();
            SharedPreferences pref = ctx.getSharedPreferences(com.baroneye.util.Configure.KEY_PREF, Context.MODE_PRIVATE);
            return pref.getString(key, def);
        } catch (Exception e) {
            return def;
        }
    }

    public static void save_pref(String key, boolean data) {
        Context ctx = Utils.getContext();
        if (ctx == null) {
            return;
        }
        SharedPreferences pref = ctx.getSharedPreferences(com.baroneye.util.Configure.KEY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putBoolean(key, data);
        ed.commit();
    }

    public static void save_pref(String key, boolean data, Context mContext) {
        Context ctx = mContext;
        if (ctx == null) {
            return;
        }
        SharedPreferences pref = ctx.getSharedPreferences(com.baroneye.util.Configure.KEY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putBoolean(key, data);
        ed.commit();
    }

    static public boolean load_pref_bool(String key, boolean def) {
        try{
            Context ctx = Utils.getContext();

            SharedPreferences pref = ctx.getSharedPreferences(com.baroneye.util.Configure.KEY_PREF, Context.MODE_PRIVATE);
            return pref.getBoolean(key, def);
        } catch (Exception e) {
            return def;
        }
    }


    static public boolean load_pref_bool(String key, boolean def, Context mContext) {
        try{
            Context ctx = mContext;

            SharedPreferences pref = ctx.getSharedPreferences(com.baroneye.util.Configure.KEY_PREF, Context.MODE_PRIVATE);
            return pref.getBoolean(key, def);
        } catch (Exception e) {
            return def;
        }
    }


    public static void save_pref(String key, int data) {
        Context ctx = Utils.getContext();
        if (ctx == null) {
            return;
        }
        SharedPreferences pref = ctx.getSharedPreferences(com.baroneye.util.Configure.KEY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putInt(key, data);
        ed.commit();
    }

    static public int load_pref_int(String key, int def) {
        try{
            Context ctx = Utils.getContext();

            SharedPreferences pref = ctx.getSharedPreferences(com.baroneye.util.Configure.KEY_PREF, Context.MODE_PRIVATE);
            return pref.getInt(key, def);
        } catch (Exception e) {
            return def;
        }
    }
    static public int load_pref_int(String key, int def, Context mContext) {
        try{
            Context ctx = mContext;

            SharedPreferences pref = ctx.getSharedPreferences(com.baroneye.util.Configure.KEY_PREF, Context.MODE_PRIVATE);
            return pref.getInt(key, def);
        } catch (Exception e) {
            return def;
        }
    }

    public static void save_pref(String key, Date data) {
        Context ctx = Utils.getContext();
        if (ctx == null) {
            return;
        }

        SharedPreferences pref = ctx.getSharedPreferences(com.baroneye.util.Configure.KEY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putLong(key, data.getTime());
        ed.commit();
    }

    public static void save_pref(String key, Date data, Context mContext) {
        Context ctx = mContext;
        if (ctx == null) {
            return;
        }

        SharedPreferences pref = ctx.getSharedPreferences(com.baroneye.util.Configure.KEY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putLong(key, data.getTime());
        ed.commit();
    }

    static public Date load_pref_date(String key, Date def) {
        try {
            Context ctx = Utils.getContext();


            SharedPreferences pref = ctx.getSharedPreferences(com.baroneye.util.Configure.KEY_PREF, Context.MODE_PRIVATE);
            return new Date(pref.getLong(key, new Date().getTime()));
        } catch (Exception e) {
            return def;
        }
    }

    public static void save_pref(String key, long data) {
        Context ctx = Utils.getContext();
        if (ctx == null) {
            return;
        }

        SharedPreferences pref = ctx.getSharedPreferences(com.baroneye.util.Configure.KEY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        ed.putLong(key, data);
        ed.commit();
    }

    static public long load_pref_long(String key, long def) {
        try{
            Context ctx = Utils.getContext();
            SharedPreferences pref = ctx.getSharedPreferences(com.baroneye.util.Configure.KEY_PREF, Context.MODE_PRIVATE);
            return pref.getLong(key, def);
        } catch (Exception e){
        return def;
        }
    }



    //perference remove
    static public void remove_object(String key) {
        Context ctx = Utils.getContext();
        if (ctx == null) {
            return;
        }
        SharedPreferences preferences = ctx
                .getSharedPreferences(com.baroneye.util.Configure.KEY_PREF,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    static public void set_noti(Context ctx){

        Log.d("Set Noti", "NotiSet");

        boolean is_on = Utils.load_pref_bool(com.baroneye.util.Configure.KEY_IS_ON, false, ctx);

        Intent intent = new Intent(ctx, OnOffService.class);
        PendingIntent noti_intent = PendingIntent.getService(ctx, ((int)Math.random())%10000, intent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_CANCEL_CURRENT);

        Intent _playIntent = new Intent(ctx, com.baroneye.YoutubePlayer.class);
        _playIntent.setAction("play");
        PendingIntent _playPendingIntent = PendingIntent.getService(ctx, 0, _playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews _smallView = new RemoteViews(ctx.getPackageName(), R.layout.noti_view);
        _smallView.setOnClickPendingIntent(R.id.timer, _playPendingIntent);

        RemoteViews _smallView2 = new RemoteViews(ctx.getPackageName(), R.layout.noti_view_play);
        _smallView.setOnClickPendingIntent(R.id.timer, _playPendingIntent);

        Notification noti = new Notification.Builder(ctx)
                //.setContentTitle("바론아이")
                //.setContentText(is_on? "실행중입니다." : "정지했습니다.")
                .setSmallIcon(R.mipmap.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_logo))
                .setShowWhen(false)
                .setContentIntent(noti_intent)
                .setContent(is_on? _smallView2 : _smallView)
                .build();

        noti.flags = Notification.FLAG_NO_CLEAR;
        // Send the notification.
        ((NotificationManager)ctx.getSystemService(ctx.NOTIFICATION_SERVICE)).notify(6100, noti);
    }
}
