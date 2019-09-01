package com.baroneye.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.baroneye.R;
import com.baroneye.StartActivity;
import com.baroneye.util.Configure;
import com.baroneye.util.EventBus;
import com.baroneye.util.Utils;
import com.squareup.otto.Subscribe;

import java.util.Date;

import static com.baroneye.util.Configure.is_lock;

public class SensorService extends Service {

    SensorManager sm;
    SensorEventListener accel_listener;
    SensorEventListener light_listener;
    SensorEventListener gyro_listener;
    Sensor gyroSensor;
    Sensor accelSensor;
    Sensor lightSensor;


    private LinearLayout v;

    public static Context mContext;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    public void onStart(Intent intent, int startId) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mContext = this;

        try {
            EventBus.getInst().register(this);
            sm = (SensorManager) getSystemService(SENSOR_SERVICE);

            accelSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            gyroSensor = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            lightSensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);


            accel_listener = new accelListener();
            light_listener = new lightListener();
            gyro_listener = new gyroListener();

            sm.registerListener(accel_listener, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
            sm.registerListener(light_listener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
            sm.registerListener(gyro_listener, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);

        }

        catch (Exception e)
        {
            Log.e("Error", "" + e);

        }
        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        EventBus.getInst().unregister(this);

        sm.unregisterListener(accel_listener);
        sm.unregisterListener(light_listener);
        sm.unregisterListener(gyro_listener);

        RemoveWindow();
        super.onDestroy();
    }



    private class accelListener implements SensorEventListener {
        public void onSensorChanged(SensorEvent event) {  // 가속도 센서 값이 바뀔때마다 호출됨

            final int rotation = ((WindowManager) SensorService.this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
            boolean is_portrait = true;
            boolean is_portrait_left = false;
            boolean is_portrait_right = false;

            //if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) is_portrait=false;

            if(rotation == Surface.ROTATION_90)
            {
                is_portrait=false;
                is_portrait_left = true;
                Log.d("Left Portrait" , "" + is_portrait_left);
            }
            else if (rotation == Surface.ROTATION_270)
            {
                is_portrait=false;
                is_portrait_right = true;
                Log.d("Right Portrait" , "" + is_portrait_right);
            }

            Log.e("eventportrait", ""+is_portrait);
            Log.e("eventValue1",""+event.values[0]);
            Log.e("eventValue2",""+event.values[1]);
            Log.e("eventValue3",""+event.values[2]);

            if(((event.values[0] < -3 || event.values[0] > 3)
                    || (event.values[1] < 7.5f || event.values[1] > 10.5f)
                    || (event.values[2] < 0)) && (is_portrait)
                    || ((event.values[0] < 6 || event.values[0] > 11.5f)
                    || (event.values[1] < -2 || event.values[1] > 2)
                    || (event.values[2] > 7) || event.values[2] < -1) && (!is_portrait && is_portrait_left)
                    || ((event.values[0] > -6 || event.values[0] < -11.5f)
                    || (event.values[1] < -2 || event.values[1] > 2)
                    || (event.values[2] > 7) || event.values[2] < -1) && (!is_portrait && is_portrait_right))
            {

                Log.e("eventOn","On");

                if(is_lock)return;

                Log.d("isUnLock?", ""+ Configure.is_unlock);

                if(Configure.is_unlock)
                {
                    Log.d("isSave?", "save");

                    Utils.save_pref(Configure.KEY_RECENT_UNLOCK, new Date().getTime());
                    Configure.is_unlock = false;
                }

                long un_lock_time = new Date().getTime() - Utils.load_pref_long(Configure.KEY_RECENT_UNLOCK,0);
                Log.d("un_lock_time", "" + un_lock_time);

                if(un_lock_time < 100){
                    return;
                }

                Log.d("isLock", "" + is_lock);


                is_lock = true;

                //화면 잠기는시간 1초간은 깜빡안거리게 체크
                Utils.save_pref(Configure.KEY_RECENT_LOCK, new Date().getTime());

                Log.d("Time", String.valueOf(new Date().getTime()));


                //하루누적 카운트
                int cnt = Utils.load_pref_int(Configure.KEY_TOTAL_ANGLE,0);
                Utils.save_pref(Configure.KEY_TOTAL_ANGLE, ++cnt);

                Log.d("TAG", "" + cnt);


                try {
                    //((StartActivity) StartActivity.mContext).text_angle.setText(String.valueOf(cnt) + " 번");

                }
                catch (Exception e){}


                if(cnt == 10)
                {
                    try {
                        ((StartActivity) StartActivity.mContext).imageisGood.setImageResource(R.drawable.image_bad);

                    }
                    catch (Exception e){}
                }


                //한번 on/off할때 누적 카운트
                cnt = Utils.load_pref_int(Configure.KEY_TMP_ANGLE,0);
                Utils.save_pref(Configure.KEY_TMP_ANGLE, ++cnt);



                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                        0 | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
                WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
                v = new LinearLayout(SensorService.this);
                v.setBackgroundColor(Color.parseColor("#000000"));
                v.setClickable(true);
                params.alpha = 1.0f;
                wm.addView(v, params);


            }else{
                long lock_time = new Date().getTime() - Utils.load_pref_long(Configure.KEY_RECENT_LOCK,0);
                // 최소 1초는 화면 잠그자 - 안그러면 경계각도쯤에서 깜빡깜빡거림

                if(Configure.is_unlock)
                {
                    return;
                }

                if(lock_time < 1000){
                    return;
                }

                Log.d("OnWindow", "");

                Configure.is_unlock = true;

                is_lock = false;
                WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
                if(v !=null) {
                    try {
                        wm.removeView(v);
                    }catch (Exception e){

                    }
                }
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }


    }

    public void RemoveWindow()
    {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        if(v !=null) {
            try {
                wm.removeView(v);
            }catch (Exception e){

            }
        }
    }

    private class gyroListener implements SensorEventListener {
        public void onSensorChanged(SensorEvent event) {  // 가속도 센서 값이 바뀔때마다 호출됨
//            text_gyroX.setText(""+event.values[0]);
//            text_gyroY.setText(""+event.values[1]);
//            text_gyroZ.setText(""+event.values[2]);
        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }
    private class lightListener implements SensorEventListener {
        public void onSensorChanged(SensorEvent event) {  // 가속도 센서 값이 바뀔때마다 호출됨
//            text_light.setText(""+event.values[0]);

        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }



    @Subscribe
    public void onEvent(String event) {
        // 이벤트가 발생한뒤 수행할 작업(공통) override 할것
        // main 만 super.onEvent() 콜 안함 ( main은 닫고 처음화면 open)
        Log.e("zzz","OFF");
        if(EventBus.EVENT_STOP.equals(event)){
            stopSelf();
        }
    }
}



