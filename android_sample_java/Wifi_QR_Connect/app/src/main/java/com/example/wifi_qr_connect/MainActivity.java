package com.example.wifi_qr_connect;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    Button btn_wifi_connect;
    Button btn_qr_check;

    DialogWifi dialogWifi;
    DialogCreateQrcode dialogCreateQrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_wifi_connect = findViewById(R.id.btn_wifi_connect);
        btn_qr_check = findViewById(R.id.btn_qr_check);
        btn_wifi_connect.setOnClickListener(this);
        btn_qr_check.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_wifi_connect) {
            dialogWifi = new DialogWifi(this);
            dialogWifi.show();
            dialogWifi.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (dialogWifi.getResult() == 1) {
                        showDialogCreateQrcode();
                    }
                }
            });
        } else if (v == btn_qr_check) {
            startQrCode();
        }
    }

    private void showDialogCreateQrcode() {
        dialogCreateQrcode = new DialogCreateQrcode(MainActivity.this, dialogWifi.getInfoWifi());
        dialogCreateQrcode.setCanceledOnTouchOutside(false);
        dialogCreateQrcode.show();
    }

    public void startQrCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(ActivityQRScaner.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                InfoWifi infoWifi = InfoWifi.fromJson(result.getContents());
                if (WifiScanner.connectToWifi(infoWifi, this)) {
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                    Log.i("!---", result.getContents());
                } else {
                    Toast.makeText(this, R.string.msg_cannot_connect_to_wifi, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
