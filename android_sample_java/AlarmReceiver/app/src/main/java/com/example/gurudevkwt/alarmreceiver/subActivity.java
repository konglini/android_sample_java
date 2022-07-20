package com.example.gurudevkwt.alarmreceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class subActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txt_1;
    Button btn_1;

    String wake_time;
    String sleep_time;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        txt_1 = (TextView) findViewById(R.id.txt_1);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);

        wake_time = getIntent().getStringExtra("wake_time");
        sleep_time = getIntent().getStringExtra("sleep_time");

        alarm(true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        txt_1.setText(intent.getExtras().getInt("time") + "/" + count);

        count += 1;
        if (count >= 6) {
            alarm(false);
            count = 0;

            txt_1.setText(0 + "/" + 0);
        }
    }

    @Override
    public void onClick(View view) {
        alarm(false);
        finish();
    }

    private void alarm(boolean b) {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("wake_time", wake_time);
        intent.putExtra("sleep_time", sleep_time);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        if (b) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10 * 1000, pendingIntent);
        }
    }
}
