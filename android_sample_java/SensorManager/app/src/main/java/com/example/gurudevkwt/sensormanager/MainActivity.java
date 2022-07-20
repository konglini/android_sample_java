package com.example.gurudevkwt.sensormanager;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txt_compass;
    TextView txt_light;
    Button btn_on;
    Button btn_off;

    CountDownTimer countDownTimer;
    int i = 0;
    int value1 = 0;
    int value2 = 0;
    int value3 = 0;
    int value4 = 0;

    SensorPackage sensorPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        txt_compass = findViewById(R.id.txt_compass);
        txt_light = findViewById(R.id.txt_light);
        btn_on = findViewById(R.id.btn_on);
        btn_off = findViewById(R.id.btn_off);
        btn_on.setOnClickListener(this);
        btn_off.setOnClickListener(this);

        sensorPackage = SensorPackage.getInstance(this);
        sensorPackage.setCompassListener(compassListener);
        sensorPackage.setLightListener(lightListener);
//        sensorPackage.startSensor();

//        timer();
    }

    @Override
    public void onClick(View view) {
        if (view == btn_on) {
            sensorPackage.startSensor();
            if (countDownTimer == null) {
                timer();
            }
        } else {
            sensorPackage.stopSensor();
            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        android.os.Process.killProcess(android.os.Process.myPid());
    }

    SensorPackage.CompassListener compassListener = new SensorPackage.CompassListener() {
        @Override
        public void compassData(int[] data) {
            value1 = data[0] < 0 ? (360 + data[0]) / 45 : data[0] / 45;
            value2 = data[1];
            value3 = data[2];
            txt_compass.setText("방위 : " + value1 + "\n기울기(전후) : " + data[1] + "\n기울기(좌우) : " + data[2]);
        }
    };

    SensorPackage.LightListener lightListener = new SensorPackage.LightListener() {
        @Override
        public void lightData(int data) {
            value4 = data;
            txt_light.setText("조도 : " + data);
        }
    };

    private void timer() {
        i++;
        Log.i("!---" + i, value1 + "/" + value2 + "/" + value3 + "/" + value4 + "/");
        countDownTimer = new CountDownTimer(500, 1000) {

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                timer();
            }
        }.start();
    }
}
