package com.example.wifi_qr_connect;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class DialogWifiPassword extends Dialog {

    Context context;
    Activity activity;
    private InfoWifi infoWifi;

    public DialogWifiPassword(Context context, InfoWifi infoWifi) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;
        this.infoWifi = infoWifi;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    EditText et_wifi_password;
    CheckBox ckb_show_password;
    Button btn_wifi_connect;

    int result_num = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wifi_password);

        et_wifi_password = findViewById(R.id.et_wifi_password);
        ckb_show_password = findViewById(R.id.ckb_show_password);
        btn_wifi_connect = findViewById(R.id.btn_wifi_connect);
        btn_wifi_connect.setVisibility(View.INVISIBLE);

        et_wifi_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 비밀번호가 입력됐습니다. 비어있는 경우 연결 버튼을 비활성화합니다.
                if (TextUtils.isEmpty(editable)) {
                    btn_wifi_connect.setVisibility(View.INVISIBLE);
                } else {
                    btn_wifi_connect.setVisibility(View.VISIBLE);
                }
            }
        });

        ckb_show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //비밀번호 숨김을 해제합니다.
                    et_wifi_password.setTransformationMethod(null);
                } else {
                    //비밀번호 숨김을 설정합니다.
                    et_wifi_password.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        btn_wifi_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoWifi.apPasspharase = et_wifi_password.getText().toString();

                WifiScanner.connectToWifi(infoWifi, context);

                checkWifi();
//                if (WifiScanner.connectToWifi(infoWifi, context)) {
//                    checkWifi();
//                } else {
//                    Toast.makeText(context, R.string.msg_cannot_connect_to_wifi, Toast.LENGTH_SHORT).show();
//                }
            }
        });
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
