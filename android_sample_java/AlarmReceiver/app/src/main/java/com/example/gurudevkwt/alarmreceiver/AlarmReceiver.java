package com.example.gurudevkwt.alarmreceiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by User on 2020-08-31.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //화면깨우기
        WakeLock.wakeLock(context);

        // 화면패턴은 열리지 않음...(확인필요)
        LockKeyGuard guard = new LockKeyGuard(context);
        guard.disable();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmm");
        int current_time = Integer.valueOf(simpleDateFormat.format(new Date()));
        if (current_time < 100) {
            current_time = current_time + 2400;
        }

        int wake_time = Integer.valueOf(intent.getStringExtra("wake_time"));
        int sleep_time = Integer.valueOf(intent.getStringExtra("sleep_time"));

        if (current_time >= wake_time && current_time <= sleep_time) {
            startActivity(context, current_time);
        } else if (wake_time > sleep_time) {
            if (current_time < wake_time && current_time > sleep_time) {
            } else {
                startActivity(context, current_time);
            }
        }
    }

    private void startActivity(Context context, int current_time) {
        Intent intent2 = new Intent(context, subActivity.class);
        intent2.putExtra("time", current_time);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent2);
    }
}
