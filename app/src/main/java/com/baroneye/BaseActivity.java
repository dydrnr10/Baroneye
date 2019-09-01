package com.baroneye;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baroneye.util.EventBus;
import com.squareup.otto.Subscribe;


public class BaseActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getInst().register(this);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        EventBus.getInst().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    protected void initComponent() {

    }

    public void showToast(final String msg) {
        if (!TextUtils.isEmpty(msg)) {
            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    public void showAlert(String msg, String btn, DialogInterface.OnClickListener listener){
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(BaseActivity.this);
        alert_confirm.setTitle("바론아이").setMessage(msg).setCancelable(false).setPositiveButton(btn, listener);
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }

    public void showAlertTwoBtn(String msg, String btn1, String btn2, DialogInterface.OnClickListener listener1, DialogInterface.OnClickListener listener2 ){
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(BaseActivity.this);
        alert_confirm.setTitle("바론아이").setMessage(msg).setCancelable(false).setPositiveButton(btn1, listener1).setNegativeButton(btn2,listener2);
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }


    @Subscribe
    public void onEvent(String event) {
        // 이벤트가 발생한뒤 수행할 작업(공통) override 할것
        // main 만 super.onEvent() 콜 안함 ( main은 닫고 처음화면 open)
        Log.e("event_called",event);
    }



}
