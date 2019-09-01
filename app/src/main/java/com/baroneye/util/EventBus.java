package com.baroneye.util;


import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

public final class EventBus {
    private static final MainThreadBus BUS = new MainThreadBus();


    public static MainThreadBus getInst() {
        return BUS;
    }

    private EventBus() {
        // No instances.
    }

    /**
     *  EventBus Keys
     */

    public final static String EVENT_STOP = "STOP";
    public final static String EVENT_START = "START";




    public static class MainThreadBus extends Bus {
        MainThreadBus(){
            super();
        }
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void post(final Object event) {

                if (Looper.myLooper() == Looper.getMainLooper()) {
                    try {
                         super.post(event);
                    }catch(Exception e){
                        com.baroneye.util.Utils.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MainThreadBus.super.post(event);
                            }
                        });

                    }
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            MainThreadBus.super.post(event);
                        }
                    });
                }

        }
    }
}