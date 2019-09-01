package com.baroneye;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baroneye.DataBase.RecomandDB;
import com.baroneye.Service.OnOffService;
import com.baroneye.util.Configure;
import com.baroneye.util.EventBus;
import com.baroneye.util.Utils;
import com.squareup.otto.Subscribe;
import com.baroneye.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class StartActivity extends BaseActivity {


    private TextView text_period;

    private TextView text_shake;
    public TextView text_angle;
    public ImageView image_switch;
    private ImageButton manage_button;

    public ImageView imageisGood;


    private final static int PERMISSION_CODE = 6100;

    public static Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(Settings.System.canWrite(this)) {
                Toast.makeText(this, "onCreate: Already Granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"onCreate: Not Granted. Permission Requested", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData((Uri.parse("package:" + this.getPackageName())));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                Utils.setActivity(this);

                mContext = this;

                Configure.recomandDB = new RecomandDB();

                //잠금화면에서도 어플 구동

                initComponent();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                Toast.makeText(this, "onResume: Granted", Toast.LENGTH_SHORT).show();
            }
        }
        if(checkPermission() == false){
            return;
        }

        Log.d("TAG", "IsResume?");

        Utils.set_noti(this);
        setCnt();
    }

    private void setCnt(){
        text_angle.setText(""+Utils.load_pref_int(Configure.KEY_TOTAL_ANGLE,0)+" 번");
        text_period.setText(long_to_date_str(Utils.load_pref_long(Configure.KEY_TOTAL_TIME,0)));
//        text_shake.setText("준비 중입니다.");
    }

    @Override
    protected void initComponent(){
        //가장최근 start한 날짜와 오늘날짜 비교해서 다르면 누계 초기화
        Date latest_start = Utils.load_pref_date(Configure.KEY_START_TIME, new Date());
        Calendar cal_latest = new GregorianCalendar(Locale.KOREA);
        cal_latest.setTime(latest_start);

        Calendar cal_now = new GregorianCalendar(Locale.KOREA);
        cal_now.setTime(new Date());
        if(cal_latest.get(Calendar.DATE) != cal_now.get(Calendar.DATE)){

            Utils.save_pref(Configure.KEY_TOTAL_TIME,(long)0);
            Utils.save_pref(Configure.KEY_TOTAL_ANGLE,0);
            Utils.save_pref(Configure.KEY_TOTAL_SHAKE,0);
            Utils.save_pref(Configure.KEY_TMP_SHAKE,0);
            Utils.save_pref(Configure.KEY_TMP_SHAKE,0);
        }

        text_period = (TextView)findViewById(R.id.text_period);

        //text_shake = (TextView)findViewById(R.id.text_shake);
        text_angle = (TextView)findViewById(R.id.text_angle);
        image_switch = (ImageView)findViewById(R.id.image_switch);


        setCnt();

        //초기버튼 세팅

        boolean is_on = Utils.load_pref_bool(Configure.KEY_IS_ON, false);

        Log.d("TAG", is_on + "");

        boolean is_good = Utils.load_pref_bool(Configure.IS_GOOD , true);

        imageisGood = (ImageView)findViewById(R.id.imageView10); // 아이의 자세가 좋으면 기뻐하는 그림

        imageisGood.setImageResource(is_good ? R.drawable.image_good : R.drawable.image_bad);
        // 아이의 자세가 나쁘면 안좋은 그림

        if(!is_on)
        {
            Utils.save_pref(Configure.IS_GOOD , true);
            imageisGood.setImageResource(R.drawable.image_good);
        }

        image_switch.setImageResource( is_on ? R.drawable.switch_on : R.drawable.switch_off);

        image_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("TAG", "Click Button");

                boolean is_on = Utils.load_pref_bool(Configure.KEY_IS_ON, false);

                try {
                    startService(new Intent(getApplicationContext(), OnOffService.class));
                }
                catch (Exception e){}


                try {
                    if (is_on == false) {
                        finish();
                    }
                }
                catch (Exception e){}

            }
        });

        manage_button = (ImageButton)findViewById(R.id.button_manage);

        manage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final boolean is_on = Utils.load_pref_bool(Configure.KEY_IS_ON, false);

                if(is_on)
                {
                    Toast.makeText(getApplicationContext(), "잠금을 먼저 해제해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(StartActivity.this, com.baroneye.ManageActivity.class);
                StartActivity.this.startActivity(intent);
            }
        });

        //초기 버튼 잠금 세팅

    }

    public void onSystem()
    {
        boolean is_on = Utils.load_pref_bool(Configure.KEY_IS_ON, false);

        if(!is_on)
            image_switch.performClick();
    }

    public void offSystem()
    {
        Log.d("OffSystem?", "TAG");
        boolean is_on = Utils.load_pref_bool(Configure.KEY_IS_ON, false);

        if(is_on) {
            Log.d("OffSystem?", "PerformClick");
            image_switch.performClick();
        }
    }

    public void lockButton()
    {
        image_switch.setEnabled(false);
    }

    public void unlockButton()
    {
        image_switch.setEnabled(true);
    }

    @Subscribe
    public void onEvent(String event) {
        // 이벤트가 발생한뒤 수행할 작업(공통) override 할것
        // main 만 super.onEvent() 콜 안함 ( main은 닫고 처음화면 open)
        if(EventBus.EVENT_STOP.equals(event) || EventBus.EVENT_START.equals(event)){
            // on/off버튼 및 통계 refresh;
            initComponent();
        }
    }

    private String long_to_date_str(long t){
        t = t/1000;
        int s = (int)t%60;
        t = t/60;
        int m = (int)t%60;
        t = t/60;
        int h = (int)t%60;
        return String.format("%02d:%02d:%02d",h,m,s);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(Settings.System.canWrite(this) && Settings.canDrawOverlays(this)){
                    showToast("권한을 모두 허가 받았습니다.\n이제 바론아이를 사용하실수 있습니다.");
                }
            }
    }



    private boolean checkPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, PERMISSION_CODE);
                return false;
            }

            if(!Settings.System.canWrite(this)){
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, PERMISSION_CODE);
                return false;
            }
        }
        return true;
    }

}
