package com.baroneye;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baroneye.R;
import com.baroneye.util.Configure;
import com.baroneye.util.Utils;

import java.util.ArrayList;



public class ChangePasswordActivity extends Activity {


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

        setContentView(R.layout.activity_password);

        mContext = this;

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
                current_password_index = 0;

                Toast.makeText(getApplicationContext(), "비밀번호를 한번 더 입력해 주세요.", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ChangePasswordActivity.this, CheckChangePasswordActivity.class);
                intent.putExtra("Password", password);
                ChangePasswordActivity.this.startActivity(intent);

                finish();
            }
        }
        catch (Exception e){}



    }



    public void onDestroy() {

        super.onDestroy();
    }





}


