package com.example.wifi_qr_connect;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class DialogWifiConnect extends Dialog implements View.OnClickListener {

    Context context;
    Activity activity;
    private InfoWifi infoWifi;

    public DialogWifiConnect(Context context, InfoWifi infoWifi) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;
        this.infoWifi = infoWifi;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    Button btn_wifi_connect;
    Button btn_wifi_disconnect;

    int result_num = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wifi_connect);

        btn_wifi_connect = findViewById(R.id.btn_wifi_connect);
        btn_wifi_disconnect = findViewById(R.id.btn_wifi_disconnect);
        btn_wifi_connect.setOnClickListener(this);
        btn_wifi_disconnect.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        try {
            handler_wifi.removeMessages(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onStop();
    }

    @Override
    public void onClick(View v) {
        if (v == btn_wifi_connect) {
            if (WifiScanner.connectToWifi(infoWifi, context)) {
                checkWifi();
            } else {
                Toast.makeText(context, R.string.msg_cannot_connect_to_wifi, Toast.LENGTH_SHORT).show();
            }
        } else if (v == btn_wifi_disconnect) {
            if (WifiScanner.deleteWifi(infoWifi, context)) {
                dismiss();
            } else {
                Toast.makeText(context, R.string.msg_cannot_delete_wifi, Toast.LENGTH_SHORT).show();
            }
        }
    }

    Handler handler_wifi = new Handler();
    int wifi_connect_count = 0;

    //와이파이가 연결된 상태에서 다른 와이파이 연결시 BroadcastReceiver는 동작하지 않음.
    private void checkWifi() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        final WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        handler_wifi.removeMessages(0);
        handler_wifi.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!wifiInfo.getSSID().equals("\"" + infoWifi.apName + "\"") || wifi_connect_count <= 4) {
                    checkWifi();
                } else {
                    result_num = 1;
                    dismiss();
                }
                wifi_connect_count++;
            }
        }, 1000);
    }

    public int getResult() {
        return result_num;
    }

    public InfoWifi getInfoWifi() {
        return infoWifi;
    }
}
