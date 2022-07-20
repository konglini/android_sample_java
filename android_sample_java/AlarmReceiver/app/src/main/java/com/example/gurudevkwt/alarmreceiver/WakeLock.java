package com.example.gurudevkwt.alarmreceiver;

import android.content.Context;
import android.os.PowerManager;

/**
 * Created by User on 2016-08-04.
 */
public class WakeLock {
    private static final String TAG = WakeLock.class.getSimpleName();
    private static PowerManager.WakeLock mWakeLock;

    public static void wakeLock(Context context) {
        if (mWakeLock != null) {
            return;
        }

        PowerManager pm = (PowerManager) context
                .getSystemService(Context.POWER_SERVICE);

        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "GURU");
        mWakeLock.acquire();
    }

    public static void releaseWakeLock() {

        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }
}
