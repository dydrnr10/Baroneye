package com.baroneye;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baroneye.R;
import com.baroneye.Service.MyService;
import com.baroneye.util.Configure;
import com.baroneye.util.Utils;

import java.util.ArrayList;

public class LockScreenAppActivity extends Activity {

    KeyguardManager.KeyguardLock k1;

    String password;

    Context mContext;
    int current_password_index;


    TextView password_text1;
    TextView password_text2;
    TextView password_text3;
    TextView password_text4;

    ArrayList<TextView> textList;

    View v;

    WindowManager.LayoutParams params;
    WindowManager wm;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mContext = this;

        boolean is_on = Utils.load_pref_bool(Configure.KEY_IS_ON, false);

        try {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.activity_lock_screen_app, null);


        password_text1 = (TextView)v.findViewById(R.id.password_1);
        password_text2 = (TextView)v.findViewById(R.id.password_2);
        password_text3 = (TextView)v.findViewById(R.id.password_3);
        password_text4 = (TextView)v.findViewById(R.id.password_4);

        textList = new ArrayList<TextView>();

        textList.add(password_text1);
        textList.add(password_text2);
        textList.add(password_text3);
        textList.add(password_text4);

        for(int i = 0; i < 4; i++)
        {
            textList.get(i).setText("");
        }

        current_password_index = 0;
        password = "";

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,//항상 최 상위. 터치 이벤트 받을 수 있음.
                0 | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        params.alpha = 1.0f;

        ImageButton imageButton_1 = (ImageButton)v.findViewById(R.id.number_1);

        imageButton_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password += "1";

                checkPassword();
            }
        });

        ImageButton imageButton_2 = (ImageButton)v.findViewById(R.id.number_2);

        imageButton_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLICK", "TAG");
                password += "2";

                checkPassword();
            }
        });

        ImageButton imageButton_3 = (ImageButton)v.findViewById(R.id.number_3);

        imageButton_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLICK", "TAG");
                password += "3";

                checkPassword();
            }
        });

        ImageButton imageButton_4 = (ImageButton)v.findViewById(R.id.number_4);

        imageButton_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLICK", "TAG");
                password += "4";

                checkPassword();
            }
        });

        ImageButton imageButton_5 = (ImageButton)v.findViewById(R.id.number_5);

        imageButton_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLICK", "TAG");
                password += "5";

                checkPassword();
            }
        });

        ImageButton imageButton_6 = (ImageButton)v.findViewById(R.id.number_6);

        imageButton_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLICK", "TAG");
                password += "6";

                checkPassword();
            }
        });

        ImageButton imageButton_7 = (ImageButton)v.findViewById(R.id.number_7);

        imageButton_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLICK", "TAG");
                password += "7";

                checkPassword();
            }
        });

        ImageButton imageButton_8 = (ImageButton)v.findViewById(R.id.number_8);

        imageButton_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLICK", "TAG");
                password += "8";

                checkPassword();
            }
        });

        ImageButton imageButton_9 = (ImageButton)v.findViewById(R.id.number_9);

        imageButton_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLICK", "TAG");
                password += "9";

                checkPassword();
            }
        });


        Log.d("TAG", "Add Window");

        wm.addView(v, params);

        Log.d("TAG", "Finish Window");
        }
        catch (Exception e){}

        Configure.PASSWORD = Utils.load_pref_str(Configure.KEY_PASSWORD, "1234");

    }

    public void checkPassword()
    {
        try {
            textList.get(current_password_index).setText("*");

            current_password_index++;

            if (current_password_index == 4) {
                if (password.equals(Configure.PASSWORD)) {
                    Toast.makeText(getApplicationContext(), "잠금을 해제합니다", Toast.LENGTH_LONG).show();
                    Configure.is_lock_screen = false;


                    current_password_index = 0;
                    password = "";


                    try {
                        stopService(new Intent(getApplicationContext(), MyService.class));
                    }
                    catch (Exception e){}
                    WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
                    if (v != null) {
                        try {
                            wm.removeView(v);
                        } catch (Exception e) {

                        }
                    }

                    finish();
                } else {
                    current_password_index = 0;
                    password = "";
                    for (int i = 0; i < 4; i++) {
                        try {
                            textList.get(i).setText("");
                        } catch (Exception e) {
                        }
                    }
                    Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다", Toast.LENGTH_LONG).show();
                }
            }
        }
        catch (Exception e){}
        //wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.updateViewLayout(v, params);
    }

    class StateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    System.out.println("call Activity off hook");
                    finish();

                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
            }
        }
    };


    @Override
    public void onBackPressed() {
        // Don't allow back to dismiss.
        return;
    }

    // only used in lockdown mode
    @Override
    protected void onPause() {
        super.onPause();

        // Don't hang around.
        // finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Don't hang around.
        // finish();
    }


    public void onDestroy() {

        Log.d("TAG", "Destory");

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        if(v !=null) {
            try {
                wm.removeView(v);
            }catch (Exception e){

            }
        }

        super.onDestroy();
    }

}


