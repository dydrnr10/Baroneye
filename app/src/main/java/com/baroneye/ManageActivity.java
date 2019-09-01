package com.baroneye;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baroneye.StartActivity;
import com.baroneye.R;
import com.baroneye.Service.MyService;
import com.baroneye.Service.SensorService;
import com.baroneye.util.Configure;
import com.baroneye.util.EventBus;
import com.baroneye.util.Utils;

import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ManageActivity extends AppCompatActivity {

    ImageButton time_button;
    ImageButton change_password_button;
    EditText time_text;
    EditText password_text;
    ImageButton home_button;
    ImageButton save_button;

    Button find_password_button;

    ToggleButton lock_button;

    EditText etEdit;

    ImageView image_1;
    ImageView image_2;
    ImageView image_3;
    ImageView image_4;

    int minute;

    static public Context mContext;

    TimerTask task;
    TimerTask image_task_1;
    TimerTask image_task_2;
    TimerTask image_task_3;
    TimerTask image_task_4;
    Timer mTimer;
    Timer mTimer_1;
    Timer mTimer_2;
    Timer mTimer_3;

    TextView current_time;

    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        mContext = this;

        initPassword();
        initTime();

        initButton();
    }

    private void initPassword()
    {
        Configure.PASSWORD = Utils.load_pref_str(Configure.KEY_PASSWORD, "1234");

        password = Utils.load_pref_str(Configure.KEY_PASSWORD, "1234");
    }

    private void initTime()
    {
        current_time = (TextView)findViewById(R.id.current_time);

        int time = Utils.load_pref_int(Configure.KEY_CURRENT_TIME, 0);

        current_time.setText("현재 사용시간 " + time + "분");

    }


    private void initButton()
    {
        initSaveButton();

        time_button = (ImageButton)findViewById(R.id.button_time);

        time_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                if(time_text.getText().toString().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "시간을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(Integer.parseInt(time_text.getText().toString()) == 0)
                {
                    Toast.makeText(getApplicationContext(), "시간을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    //return;
                }

                Configure.TIMER_NUMBER = 4;

                minute = Integer.parseInt(time_text.getText().toString());

                current_time.setText("현재 사용시간 " + time_text.getText().toString() + "분");

                Toast.makeText(getApplicationContext(), "타이머가 설정되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        time_text = (EditText)findViewById(R.id.text_time);
        password_text = (EditText)findViewById(R.id.text_password);

        home_button = (ImageButton)findViewById(R.id.button_home);
        home_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {

                finish();
            }
        });

        change_password_button = (ImageButton)findViewById(R.id.button_change_password);
        change_password_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Log.d("Password" , password_text.getText().toString() + " " + Configure.PASSWORD);
                if(password_text.getText().toString().equals( Configure.PASSWORD)) {
                    Intent intent = new Intent(ManageActivity.this, com.baroneye.ChangePasswordActivity.class);
                    ManageActivity.this.startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"비밀번호가 틀렸습니다",Toast.LENGTH_LONG).show();
                }
            }
        });

        find_password_button = (Button)findViewById(R.id.findpassword_button);
        find_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                etEdit = new EditText(mContext);
                etEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                Random random = new Random();
                int number1 = random.nextInt(900);
                int number2 = random.nextInt(900);
                final int answer = number1 * number2;

                builder.setTitle(number1 + " * " + number2 + " = ?" + " 정답을 입력해주세요");
                builder.setView(etEdit);

                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                if(answer == Integer.parseInt(etEdit.getText().toString()))
                                {
                                    Toast.makeText(getApplicationContext(),
                                            "비밀번호는 " + Utils.load_pref_str(Configure.KEY_PASSWORD, "1234") + "입니다.",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"정답이 틀렸습니다.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),"취소했습니다",Toast.LENGTH_LONG).show();
                            }
                        });
                builder.show();

            }
        });
    }

    private void initSaveButton()
    {
        save_button = (ImageButton)findViewById(R.id.button_save);
        save_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Utils.save_pref(Configure.KEY_CURRENT_TIME, minute);

                Configure.PASSWORD = password;
                Utils.save_pref(Configure.KEY_PASSWORD, Configure.PASSWORD);

                Toast.makeText(getApplicationContext(),"저장되었습니다.",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void SetTimer()
    {
        if(minute == 0)
        {
            return;
        }

        task = new TimerTask(){
            public void run() {
                runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                    public void run() {

                        Log.d("TAG", "4");
                        Toast.makeText(getApplicationContext(), "바론아이 사용시간이 끝났습니다", Toast.LENGTH_SHORT).show();

                        Configure.TIMER_NUMBER = 0;

                        boolean is_on = Utils.load_pref_bool(Configure.KEY_IS_ON, false);

                        Log.d("TAG", is_on + ": is On");

                        if(is_on) {

                            Log.d("TAG", "Unlock");
                            ((SensorService)SensorService.mContext).RemoveWindow();

                            Toast.makeText(getApplicationContext(), "종료 되었습니다.", Toast.LENGTH_LONG).show();

                            Utils.save_pref(Configure.KEY_IS_ON,!is_on);
                            Utils.set_noti(ManageActivity.this);

                            try {
                                EventBus.getInst().post(EventBus.EVENT_STOP);

                                if(((ManageActivity)ManageActivity.mContext != null))
                                {
                                    ((ManageActivity)ManageActivity.mContext).CancleTimer();
                                }
                            }
                            catch (Exception e){}

                            long total_sec = Utils.load_pref_long(Configure.KEY_TOTAL_TIME,0);
                            long time_diff = new Date().getTime() - Utils.load_pref_date(Configure.KEY_START_TIME, new Date()).getTime();
                            Utils.save_pref(Configure.KEY_TOTAL_TIME, total_sec+time_diff);


                            Utils.save_pref(Configure.KEY_TOTAL_TIME,0);
                            Utils.save_pref(Configure.KEY_TOTAL_ANGLE, 0);
                            Utils.save_pref(Configure.IS_GOOD, true);


                            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View v = inflater.inflate(R.layout.activity_start, null);

                            ImageView image_switch = (ImageView)v.findViewById(R.id.image_switch);

                            ImageView imageView = (ImageView)v.findViewById(R.id.imageView10);
                            imageView.setImageResource(R.drawable.image_good);

                            image_switch.setImageResource( is_on ? R.drawable.switch_off : R.drawable.switch_on);

                            finish();
                        }
                        startService(new Intent(getApplicationContext(), MyService.class));
                        Intent intent = new Intent(ManageActivity.this, com.baroneye.LockScreenAppActivity.class);
                        ManageActivity.this.startActivity(intent);

                    }
                });
            }
        };

        image_task_1 = new TimerTask(){
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {

                        Log.d("TAG", "1");
                        Toast toast= Toast.makeText(getApplicationContext(), "바론아이 사용시간이 \" + minute * 3/4 + \"분 남았습니다.", Toast.LENGTH_SHORT);

                        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v2 = inflater.inflate(R.layout.activity_time_bar, null);
                        TextView time_text = (TextView)v2.findViewById(R.id.timebar_time);

                        time_text.setText(minute * 3/4 + "분 !");

                                            /* 토스트에 뷰 셋팅하기 xml 통째로 넣어도 됨 */
                        toast.setView(v2);
                        //위치 지정
                        toast.setGravity(Gravity.CENTER,10,10);
                        //여백 지정
                        toast.setMargin(0, -17);

                        toast.show();
                    }
                });
            }
        };

        image_task_2 = new TimerTask(){
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {

                        Log.d("TAG", "2");

                        Toast toast= Toast.makeText(getApplicationContext(), "바론아이 사용시간이 \" + minute * 3/4 + \"분 남았습니다.", Toast.LENGTH_SHORT);

                        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v2 = inflater.inflate(R.layout.activity_time_bar, null);
                        TextView time_text = (TextView)v2.findViewById(R.id.timebar_time);

                        time_text.setText(minute * 2/4 + "분 !");

                        toast.setView(v2);
                        //위치 지정
                        toast.setGravity(Gravity.CENTER,10,10);
                        //여백 지정
                        toast.setMargin(0, -17);

                        toast.show();
                    }
                });
            }
        };

        image_task_3 = new TimerTask(){
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {

                        Log.d("TAG", "3");
                        Toast toast= Toast.makeText(getApplicationContext(), "바론아이 사용시간이 \" + minute * 3/4 + \"분 남았습니다.", Toast.LENGTH_SHORT);

                        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View v2 = inflater.inflate(R.layout.activity_time_bar, null);
                        TextView time_text = (TextView)v2.findViewById(R.id.timebar_time);

                        time_text.setText(minute * 1/4 + "분 !");

                        toast.setView(v2);
                        toast.setGravity(Gravity.CENTER,10,10);
                        toast.setMargin(0, -17);

                        toast.show();
                    }
                });
            }
        };

        image_task_4 = new TimerTask(){
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {

                        Log.d("EndOfTime", "Tag");
                        Toast.makeText(getApplicationContext(), "바론아이 사용시간이 끝났습니다", Toast.LENGTH_SHORT).show();


                        UnlockSystem();
                    }
                });
            }
        };
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

    public void LockSystem()
    {
        Utils.save_pref(Configure.KEY_IS_LOCK_BUTTON, true);
        ((StartActivity)StartActivity.mContext).onSystem();
    }

    public void UnlockSystem()
    {
        Log.d("is UnLock On Holding?", "TAG");
        Utils.save_pref(Configure.KEY_IS_LOCK_BUTTON, false);
        ((StartActivity)StartActivity.mContext).offSystem();
    }

    public void ChangePassword()
    {
            etEdit = new EditText(this);
            etEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("비밀번호 변경");
            builder.setView(etEdit);

            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(etEdit.getText().toString().length() != 4)
                            {
                                Toast.makeText(getApplicationContext(),"비밀번호를 4자리로 입력해주세요.",Toast.LENGTH_LONG).show();
                            }
                            else {
                                String tempPassword = etEdit.getText().toString();
                                Toast.makeText(getApplicationContext(), "비밀번호 확인", Toast.LENGTH_LONG).show();
                                ChangePassWordCheck(tempPassword);
                            }
                        }
                    });
            builder.setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"취소했습니다",Toast.LENGTH_LONG).show();
                        }
                    });
            builder.show();

    }

    public void ChangePassWordCheck(final String tempPassword)
    {
        etEdit = new EditText(this);
        etEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("비밀번호 확인");
        builder.setView(etEdit);

        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(tempPassword.equals(etEdit.getText().toString())) {
                            password = tempPassword;
                            password_text.setText("");
                            Toast.makeText(getApplicationContext(), "변경 되었습니다", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    public void CancleTimer()
    {
        if(mTimer != null) {
            mTimer.cancel();
            task.cancel();
            Toast.makeText(this, "타이머가 종료되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }


    public void MakeToast()
    {
        Log.d("Toast", "Toast");
        Toast.makeText(this, "바론아이 사용시간이 끝났습니다", Toast.LENGTH_SHORT).show();
    }

    private final class SendToast implements Runnable {
        private final int mMinute;

        public SendToast(int minute)
        {
            mMinute = minute;
        }

        public void run()
        {
            try {
                Thread.sleep(1000 * 60 * mMinute);
                Toast.makeText(getApplicationContext(), "바론아이 사용시간이 끝났습니다", Toast.LENGTH_SHORT).show();
            }
            catch(Exception e)
            {

            }
        }

    }

    public static boolean isScreenOn(Context context) {
        return ((PowerManager)context.getSystemService(Context.POWER_SERVICE)).isScreenOn();
    }






}
