package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class MainActivity extends WearableActivity {

    private static final String[] PERMISSIONS_LIST = {
            Manifest.permission.BODY_SENSORS,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int REQCODE_REQUEST_PERMISSIONS = 1;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Enables Always-on
        setAmbientEnabled();

        initPermissions();
    }

    private void initPermissions() {
        //설명_ 권한 체크
        boolean checkPermissionsEnabled = false;
        ArrayList<String> permissions = new ArrayList<>();
        for (int i = 0; i < PERMISSIONS_LIST.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, PERMISSIONS_LIST[i]) != PackageManager.PERMISSION_GRANTED) {
                checkPermissionsEnabled = true;
                permissions.add(PERMISSIONS_LIST[i]);
            }
        }
        if (checkPermissionsEnabled) {
            String[] str_permissions = new String[permissions.size()];
            permissions.toArray(str_permissions);
            ActivityCompat.requestPermissions(this, str_permissions, REQCODE_REQUEST_PERMISSIONS);
        } else {
            startActivity(new Intent(MainActivity.this, FragmentActivity.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQCODE_REQUEST_PERMISSIONS: {
                boolean isAllEnabled = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        isAllEnabled = false;
                    }
                }
                if (!isAllEnabled) {
                    initPermissions();
                } else {
                    startActivity(new Intent(MainActivity.this, FragmentActivity.class));
                }
                break;
            }
        }
    }
}
