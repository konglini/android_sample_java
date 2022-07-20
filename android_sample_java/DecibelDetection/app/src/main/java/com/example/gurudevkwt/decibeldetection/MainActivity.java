package com.example.gurudevkwt.decibeldetection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_start;
    Button btn_stop;

    DecibelDetection decibelDetection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);

        decibelDetection = new DecibelDetection();
        decibelDetection.startDecibelDetection(decibelDetection);
    }

    @Override
    public void onClick(View view) {
        if (view == btn_start) {
            if(decibelDetection == null){
                decibelDetection = new DecibelDetection();
                decibelDetection.startDecibelDetection(decibelDetection);
            }
        } else if (view == btn_stop) {
            if(decibelDetection != null){
                decibelDetection.stopDecibelDetection(decibelDetection);
                decibelDetection = null;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
