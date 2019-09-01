package com.baroneye.Alert;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baroneye.R;
import com.baroneye.Service.OnOffService;
import com.baroneye.util.Configure;
import com.baroneye.util.EventBus;
import com.baroneye.util.Utils;

import java.util.Date;

public class PasswordAlert extends AppCompatActivity {

    EditText etEdit;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_password_alert);

        etEdit = new EditText(this);
        etEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("비밀번호 입력");
        builder.setView(etEdit);

        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(Configure.PASSWORD.equals(etEdit.getText().toString())) {
                            Utils.save_pref(Configure.KEY_PASSWORD, Configure.PASSWORD);

                            Toast.makeText(getApplicationContext(), "종료 되었습니다.", Toast.LENGTH_LONG).show();

                            final boolean is_on = Utils.load_pref_bool(Configure.KEY_IS_ON, false);

                            Utils.save_pref(Configure.KEY_IS_ON,!is_on);
                            Utils.set_noti(PasswordAlert.this);

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


                            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View v = inflater.inflate(R.layout.activity_start, null);

                            ImageView image_switch = (ImageView)v.findViewById(R.id.image_switch);

                            ImageView imageView = (ImageView)v.findViewById(R.id.imageView10);
                            imageView.setImageResource(R.drawable.image_good);

                            image_switch.setImageResource( is_on ? R.drawable.switch_off : R.drawable.switch_on);

                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                finish();
            }
        });

        builder.show();
    }
}
