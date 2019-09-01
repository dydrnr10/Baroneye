package com.baroneye;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.baroneye.util.Configure;
import com.baroneye.R;
import com.baroneye.util.Configure;

public class SensorActivity extends BaseActivity {


    EditText accel_x;
    EditText accel_y;
    EditText accel_z;

    EditText accel_portrait_x;
    EditText accel_portrait_y;
    EditText accel_portrait_z;

    Button set_accel_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        initComponent();
    }

    public void initComponent()
    {
        accel_x = (EditText)findViewById(R.id.accel_x);
        accel_y = (EditText)findViewById(R.id.accel_y);
        accel_z = (EditText)findViewById(R.id.accel_z);

        set_accel_button = (Button)findViewById(R.id.setAccel);

        set_accel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Configure.ACCEL_X = Integer.parseInt(accel_x.getText().toString());
                Configure.ACCEL_Y = Integer.parseInt(accel_y.getText().toString());
                Configure.ACCEL_Z = Integer.parseInt(accel_z.getText().toString());
            }
        });
    }
}
