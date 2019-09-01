package com.baroneye.Service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baroneye.LockScreenAppActivity;
import com.baroneye.PasswordActivity;
import com.baroneye.R;
import com.baroneye.RecommandYoutube;
import com.baroneye.StartActivity;
import com.baroneye.util.Configure;
import com.baroneye.util.EventBus;
import com.baroneye.util.Utils;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class OnOffService extends Service {

    //public Context mServiceContext;

    private LinearLayout v;
    public static Context mContext;

    TimerTask task;
    TimerTask image_task_1;
    TimerTask image_task_2;
    TimerTask image_task_3;
    TimerTask image_task_4;
    Timer mTimer;
    Timer mTimer_1;
    Timer mTimer_2;
    Timer mTimer_3;

    int minute;

    Handler handler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        final boolean is_on = Utils.load_pref_bool(Configure.KEY_IS_ON, false);
        Log.e("zzz",""+!is_on);

        return null;
    }



    public void onStart(Intent intent, int startId) {
        final boolean is_on = Utils.load_pref_bool(Configure.KEY_IS_ON, false);
        Log.e("zzz",""+!is_on);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;

        final boolean is_on = Utils.load_pref_bool(Configure.KEY_IS_ON, false, mContext);

        Log.e("zzz",""+!is_on);



        if(is_on){

            Intent intent2;
            intent2 = new Intent(this, PasswordActivity.class);

            try {
                if (com.baroneye.Service.SensorService.mContext != null) {
                    ((com.baroneye.Service.SensorService) com.baroneye.Service.SensorService.mContext).RemoveWindow();
                }
            }
            catch (Exception e)
            {

            }

            try {
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);

            }
            catch(Exception e)
            {
                Log.e("Error" , "" + e);
            }

        }else{

            Log.d("Log", "is On");
            try{
               EventBus.getInst().post(EventBus.EVENT_START);
            }
            catch (Exception e){}
            Configure.service_intent = new Intent(this, com.baroneye.Service.SensorService.class);
            //서비스 시작시키기
            try {
                startService(Configure.service_intent);
            }
            catch (Exception e){}

            try {
                ((StartActivity) StartActivity.mContext).image_switch.setImageResource(is_on ? R.drawable.switch_off : R.drawable.switch_on);
            }
            catch (Exception e){}

            handler = new Handler();

            SetTimer();
            StartTimer();

            Utils.save_pref(Configure.KEY_START_TIME, new Date(), mContext);


            Intent intent2;
            intent2 = new Intent(this, RecommandYoutube.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(intent2);
            }
            catch (Exception e){}

            Utils.save_pref(Configure.KEY_IS_ON,!is_on, mContext);
            Utils.set_noti(this);
        }

        return START_NOT_STICKY;
    }

    public void SetTimer()
    {
        minute = Utils.load_pref_int(Configure.KEY_CURRENT_TIME, 0);
        Log.d("TAG", "" + minute);

        if(minute == 0)
        {
            return;
        }

        try {
            task = new TimerTask() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                        public void run() {

                            Log.d("TAG", "4");
                            Toast.makeText(getApplicationContext(), "바론아이 사용시간이 끝났습니다", Toast.LENGTH_SHORT).show();

                            Configure.TIMER_NUMBER = 0;

                            boolean is_on = Utils.load_pref_bool(Configure.KEY_IS_ON, false);

                            Log.d("TAG", is_on + ": is On");

                            if (is_on) {

                                Log.d("TAG", "Unlock");
                                try{
                                    ((com.baroneye.Service.SensorService) com.baroneye.Service.SensorService.mContext).RemoveWindow();
                                }
                                catch (Exception e){}


                                Toast.makeText(getApplicationContext(), "종료 되었습니다.", Toast.LENGTH_LONG).show();

                                try {
                                    Utils.save_pref(Configure.KEY_IS_ON, !is_on);
                                    Utils.set_noti(mContext);
                                }
                                catch (Exception e){}
                                try {
                                    EventBus.getInst().post(EventBus.EVENT_STOP);

                                    CancleTimer();
                                } catch (Exception e) {
                                }

                                long total_sec = Utils.load_pref_long(Configure.KEY_TOTAL_TIME, 0);
                                long time_diff = new Date().getTime() - Utils.load_pref_date(Configure.KEY_START_TIME, new Date()).getTime();
                                Utils.save_pref(Configure.KEY_TOTAL_TIME, total_sec + time_diff);


                                Utils.save_pref(Configure.KEY_TOTAL_TIME, 0);
                                Utils.save_pref(Configure.KEY_TOTAL_ANGLE, 0);
                                Utils.save_pref(Configure.IS_GOOD, true);

                            }
                            try {
                                startService(new Intent(getApplicationContext(), com.baroneye.Service.MyService.class));
                            }
                            catch (Exception e){}
                            Intent intent = new Intent(mContext, LockScreenAppActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            try {
                                mContext.startActivity(intent);
                            } catch (Exception e) {
                            }

                        }
                    });
                }
            };

            image_task_1 = new TimerTask() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {

                            Log.d("TAG", "1");
                            Toast toast = Toast.makeText(getApplicationContext(), "바론아이 사용시간이 \" + minute * 3/4 + \"분 남았습니다.", Toast.LENGTH_SHORT);

                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View v2 = inflater.inflate(R.layout.activity_time_bar, null);
                            TextView time_text = (TextView) v2.findViewById(R.id.timebar_time);

                            time_text.setText(minute * 3 / 4 + "분 !");

                                            /* 토스트에 뷰 셋팅하기 xml 통째로 넣어도 됨 */
                            toast.setView(v2);
                            //위치 지정
                            toast.setGravity(Gravity.CENTER, 10, 10);
                            //여백 지정
                            toast.setMargin(0, -17);

                            toast.show();
                        }
                    });
                }
            };

            image_task_2 = new TimerTask() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {

                            Log.d("TAG", "2");

                            Toast toast = Toast.makeText(getApplicationContext(), "바론아이 사용시간이 \" + minute * 3/4 + \"분 남았습니다.", Toast.LENGTH_SHORT);

                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View v2 = inflater.inflate(R.layout.activity_time_bar, null);
                            TextView time_text = (TextView) v2.findViewById(R.id.timebar_time);

                            time_text.setText(minute * 2 / 4 + "분 !");

                            toast.setView(v2);
                            //위치 지정
                            toast.setGravity(Gravity.CENTER, 10, 10);
                            //여백 지정
                            toast.setMargin(0, -17);

                            toast.show();
                        }
                    });
                }
            };

            image_task_3 = new TimerTask() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {

                            Log.d("TAG", "3");
                            Toast toast = Toast.makeText(getApplicationContext(), "바론아이 사용시간이 \" + minute * 3/4 + \"분 남았습니다.", Toast.LENGTH_SHORT);

                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View v2 = inflater.inflate(R.layout.activity_time_bar, null);
                            TextView time_text = (TextView) v2.findViewById(R.id.timebar_time);

                            time_text.setText(minute * 1 / 4 + "분 !");

                            toast.setView(v2);
                            toast.setGravity(Gravity.CENTER, 10, 10);
                            toast.setMargin(0, -17);

                            toast.show();
                        }
                    });
                }
            };
        }
        catch (Exception e){}

    }

    public void StartTimer()
    {
        if(image_task_1 != null) {



            mTimer_1 = new Timer();
            mTimer_1.schedule(image_task_1, minute * 60 * 1000 / 4);

            mTimer_2 = new Timer();
            mTimer_2.schedule(image_task_2, minute * 60 * 1000 / 2);

            mTimer_3 = new Timer();
            mTimer_3.schedule(image_task_3, minute * 60 * 1000 / 4 * 3);

            mTimer = new Timer();
            mTimer.schedule(task, minute * 60 * 1000);

            Toast toast= Toast.makeText(getApplicationContext(), "바론아이 사용시간이 \" + minute + \"분 남았습니다.", Toast.LENGTH_SHORT);

            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v2 = inflater.inflate(R.layout.activity_time_bar, null);
            TextView time_text = (TextView)v2.findViewById(R.id.timebar_time);

            time_text.setText(minute + "분 !");

            toast.setView(v2);
            //위치 지정
            toast.setGravity(Gravity.CENTER,10,10);
            //여백 지정
            toast.setMargin(0, -17);

            toast.show();
        }
    }

    public void CancleTimer()
    {
        if(mTimer != null) {
            mTimer.cancel();
            task.cancel();
            image_task_1.cancel();
            image_task_2.cancel();
            image_task_3.cancel();
            Toast.makeText(this, "타이머가 종료되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

}



