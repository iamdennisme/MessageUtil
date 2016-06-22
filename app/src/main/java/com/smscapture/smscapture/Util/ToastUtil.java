package com.smscapture.smscapture.Util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ${dennis} on 5/14/16.
 */
public class ToastUtil {
    private static String oldMessage;
    private static Toast toast = null;
    private static int duration = Toast.LENGTH_SHORT;
    private static long firstTime = 0;
    private static long nextTime = 0;
    private static long durationTime = 0;
    private static final int LONG_DELAY = 3500; //LENGTH_LONG
    private static final int SHORT_DELAY = 2000; // LENGTH_SHORT


    public static void show(Context context, String message, int showTime) {
        if (showTime != Toast.LENGTH_SHORT && showTime != Toast.LENGTH_LONG) {
            throw new IllegalArgumentException("showTime must be Toast.LENGTH_SHORT or Toast.LENGTH_SHORT");
        }
        duration = showTime;
        show(context, message);
    }

    public static void show(Context context, String message) {
        durationTime = duration == Toast.LENGTH_SHORT ? SHORT_DELAY : LONG_DELAY;
        if (toast == null) {
            toast = Toast.makeText(context,message,duration);
        }
        if (message.equals(oldMessage)) {
            nextTime = System.currentTimeMillis();
            if ((nextTime - firstTime) > durationTime) {
                toast.show();
            }
        } else {
            oldMessage = message;
            firstTime = System.currentTimeMillis();
            toast.setText(message);
            toast.setDuration(duration);
            toast.show();
        }

    }
}
