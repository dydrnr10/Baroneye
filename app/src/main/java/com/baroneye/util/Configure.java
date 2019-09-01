package com.baroneye.util;

import android.content.Context;
import android.content.Intent;

import com.baroneye.DataBase.RecomandDB;


public class Configure {

    public static Context mContext;

    public static final String KEY_PREF = "@baron@";
    public static int TIMER_NUMBER = 0;

    public static boolean is_lock = false;
    public static boolean is_unlock = true;

    public static String PASSWORD = "1234";

    public static Intent service_intent = null;
    public static Intent service_onoff_intent = null;

    public static boolean is_on_holding = false;

    public static float ACCEL_X = 3.0f;
    public static float ACCEL_Y = 2.0f;
    public static float ACCEL_Z = 0.0f;

    public static boolean is_lock_screen = false;
    public static String IS_GOOD = "is_good";

    public static final String KEY_CURRENT_TIME = "current_time";

    public static final String KEY_IS_ON = "is_on";

    public static final String KEY_TOTAL_ANGLE = "total_angle";
    public static final String KEY_TOTAL_SHAKE = "total_shake";
    public static final String KEY_TOTAL_TIME = "total_time";

    public static final String KEY_TMP_ANGLE = "tmp_angle";
    public static final String KEY_TMP_SHAKE = "tmp_angle";

    public static final String KEY_START_TIME = "START_TIME";

    public static final String KEY_IS_LOCK_BUTTON = "is_lock";

    public static final String KEY_PASSWORD = "password_key";
    public static final String KEY_RECENT_UNLOCK = "recent_unlock";

    public static final String KEY_FROM_HOLDING = "from_holding";

    public static final String KEY_RECENT_LOCK = "recent_lock";

    public static final String KEY_SHARK_TIME = "shark_time";
    public static final String KEY_SHARK_INDEX = "shark_index";

    public static final String KEY_NUMBER_VIDEO = "key_number_video";
    public static final String KEY_LAST_VIEDO = "key_last_video";

    public static RecomandDB recomandDB;
}
