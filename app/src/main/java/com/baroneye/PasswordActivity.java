package com.baroneye;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baroneye.R;
import com.baroneye.Service.OnOffService;
import com.baroneye.util.Configure;
import com.baroneye.util.EventBus;
import com.baroneye.util.Utils;

import java.util.ArrayList;
import java.util.Date;


public class PasswordActivity extends Activity {


    String password;

    Context mContext;
    int current_password_index;


    TextView password_text1;
    TextView password_text2;
    TextView password_text3;
    TextView password_text4;

    ImageView title;
    ImageView passwordImage;



    ArrayList<TextView> textList;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_password);
        }
        catch (OutOfMemoryError e){}

        mContext = this;

        Utils.setActivity(this);

        boolean is_on = Utils.load_pref_bool(Configure.KEY_IS_ON, false);

            password_text1 = (TextView)findViewById(R.id.password_1);
            password_text2 = (TextView)findViewById(R.id.password_2);
            password_text3 = (TextView)findViewById(R.id.password_3);
            password_text4 = (TextView)findViewById(R.id.password_4);

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


            ImageButton imageButton_1 = (ImageButton)findViewById(R.id.number_1);

            imageButton_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    password += "1";

                    checkPassword();
                }
            });

            ImageButton imageButton_2 = (ImageButton)findViewById(R.id.number_2);

            imageButton_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("CLICK", "TAG");
                    password += "2";

                    checkPassword();
                }
            });

            ImageButton imageButton_3 = (ImageButton)findViewById(R.id.number_3);

            imageButton_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("CLICK", "TAG");
                    password += "3";

                    checkPassword();
                }
            });

            ImageButton imageButton_4 = (ImageButton)findViewById(R.id.number_4);

            imageButton_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("CLICK", "TAG");
                    password += "4";

                    checkPassword();
                }
            });

            ImageButton imageButton_5 = (ImageButton)findViewById(R.id.number_5);

            imageButton_5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("CLICK", "TAG");
                    password += "5";

                    checkPassword();
                }
            });

            ImageButton imageButton_6 = (ImageButton)findViewById(R.id.number_6);

            imageButton_6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("CLICK", "TAG");
                    password += "6";

                    checkPassword();
                }
            });

            ImageButton imageButton_7 = (ImageButton)findViewById(R.id.number_7);

            imageButton_7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("CLICK", "TAG");
                    password += "7";

                    checkPassword();
                }
            });

            ImageButton imageButton_8 = (ImageButton)findViewById(R.id.number_8);

            imageButton_8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("CLICK", "TAG");
                    password += "8";

                    checkPassword();
                }
            });

            ImageButton imageButton_9 = (ImageButton)findViewById(R.id.number_9);

            imageButton_9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("CLICK", "TAG");
                    password += "9";

                    checkPassword();
                }
            });



    }

    public void checkPassword()
    {
        try {
            textList.get(current_password_index).setText("*");
            current_password_index++;

            if (current_password_index == 4) {
                if (password.equals(Configure.PASSWORD)) {
                    Toast.makeText(getApplicationContext(), "잠금을 해제합니다", Toast.LENGTH_LONG).show();

                    Utils.save_pref(Configure.KEY_PASSWORD, Configure.PASSWORD);

                    Toast.makeText(getApplicationContext(), "종료 되었습니다.", Toast.LENGTH_LONG).show();

                    final boolean is_on = Utils.load_pref_bool(Configure.KEY_IS_ON, false);

                    Utils.save_pref(Configure.KEY_IS_ON,!is_on);
                    Utils.set_noti(PasswordActivity.this);

                    try {
                        EventBus.getInst().post(EventBus.EVENT_STOP);

                        if(((OnOffService)OnOffService.mContext != null))
                        {
                            ((OnOffService)OnOffService.mContext).CancleTimer();
                        }
                    }
                    catch (Exception e){}

                    long total_sec = Utils.load_pref_long(Configure.KEY_TOTAL_TIME,0);
                    long time_diff = new Date().getTime() - Utils.load_pref_date(Configure.KEY_START_TIME, new Date()).getTime();
                    Utils.save_pref(Configure.KEY_TOTAL_TIME, total_sec+time_diff);


                    Utils.save_pref(Configure.KEY_TOTAL_TIME,0);
                    Utils.save_pref(Configure.KEY_TOTAL_ANGLE, 0);
                    Utils.save_pref(Configure.IS_GOOD, true);
                    Utils.set_noti(mContext);


                    LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = inflater.inflate(R.layout.activity_start, null);

                    ImageView image_switch = (ImageView)v.findViewById(R.id.image_switch);

                    ImageView imageView = (ImageView)v.findViewById(R.id.imageView10);
                    imageView.setImageResource(R.drawable.image_good);

                    image_switch.setImageResource( is_on ? R.drawable.switch_off : R.drawable.switch_on);



                    finish();


                    current_password_index = 0;
                    password = "";



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



    }



    public void onDestroy() {

        super.onDestroy();
    }





}


