package com.example.gurudevkwt.speechrecognizer;

import android.content.Intent;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private TextView textView;
    private TextView textView2;

    VoiceCatch voiceCatch;

    private boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(voiceCatch != null){
            end();
        }
    }

    @Override
    public void onClick(View v) {
        if (!started) {
            started = true;
            button.setText("STOP");
            start();
        } else {
            started = false;
            button.setText("START");
            end();
        }
    }

    public void result(String s) {
        end();
        if (s.equals("")) {
            start();
        } else {
            textView.setText(s);
            started = false;
            button.setText("START");
        }
    }

    public void start() {
        voiceCatch = new VoiceCatch(this);
        voiceCatch.isRuning(true);
    }

    public void end() {
        voiceCatch.isRuning(false);
        voiceCatch = null;
    }

    public void decibel(String s) {
        textView2.setText(s);
    }
}
