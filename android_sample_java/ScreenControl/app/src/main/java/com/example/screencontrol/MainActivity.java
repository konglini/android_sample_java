package com.example.screencontrol;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private WindowManager.LayoutParams params;

    Handler handler = new Handler();

    TextView tv_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("!---", Build.MODEL + "/" + Build.MODEL.contains("muPAD"));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        params = getWindow().getAttributes();

        tv_bg = findViewById(R.id.tv_bg);
        tv_bg.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        runOnMainHandler(new Runnable() {
            @Override
            public void run() {
                Log.i("!---", "1");
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                tv_bg.setVisibility(View.VISIBLE);

                params.screenBrightness = 0.0f;
                getWindow().setAttributes(params);

                runOnMainHandler(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("!---", "2");
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                }, 50000);
            }
        }, 10000);
    }

    @Override
    protected void onPause() {
        super.onPause();

        runOnMainHandler(new Runnable() {
            @Override
            public void run() {
                Log.i("!---", "3");
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        }, 5000);
    }

    private void runOnMainHandler(Runnable runnable, int delay){
        handler.removeMessages(0);
        handler.postDelayed(runnable, delay);
    }
}
