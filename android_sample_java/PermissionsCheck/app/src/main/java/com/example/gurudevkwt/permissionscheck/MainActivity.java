package com.example.gurudevkwt.permissionscheck;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import static com.example.gurudevkwt.permissionscheck.Permissions.PERMISSIONS_LIST;
import static com.example.gurudevkwt.permissionscheck.Permissions.checkPermissionsEnabled;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);

        boolean checkPermissionsEnabled = false;
        ArrayList<String> permissions = new ArrayList<>();
        for(int i = 0; i < PERMISSIONS_LIST.length; i++){
            if(ContextCompat.checkSelfPermission(this, PERMISSIONS_LIST[i]) != PackageManager.PERMISSION_GRANTED){
                checkPermissionsEnabled = true;
                permissions.add(PERMISSIONS_LIST[i]);
            }
        }
        if (checkPermissionsEnabled) {
            String[] str_permissions = new String[permissions.size()];
            permissions.toArray(str_permissions);
            ActivityCompat.requestPermissions(this, str_permissions, 1);
        }
    }

    @Override
    public void onClick(View view) {
        if(view == button){
            checkPermissionsEnabled(this);
        }
    }

    private void initPermissions() {
        //설명_ 권한 체크
        boolean checkPermissionsEnabled = false;
        ArrayList<String> permissions = new ArrayList<>();
        for (int i = 0; i < PERMISSIONS_LIST.length; i++) {
            if (ContextCompat.checkSelfPermission(this, PERMISSIONS_LIST[i]) != PackageManager.PERMISSION_GRANTED) {
                checkPermissionsEnabled = true;
                permissions.add(PERMISSIONS_LIST[i]);
            }
        }
        if (checkPermissionsEnabled) {
            String[] str_permissions = new String[permissions.size()];
            permissions.toArray(str_permissions);
            ActivityCompat.requestPermissions(this, str_permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        checkPermissionsEnabled(this);
                    }
                }
                break;
            }
        }
    }
}
