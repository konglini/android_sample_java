package com.example.wifi_qr_connect;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DialogWifi extends Dialog {
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 301;

    Context context;
    Activity activity;

    public DialogWifi(Context context) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    private SwipeRefreshLayout srl_wifi_list;
    private ListView lv_wifi_list;

    DialogWifiConnect dialogWifiConnect;
    DialogWifiPassword dialogWifiPassword;

    private boolean isScanning = false;

    private WifiListAdapter wifiListAdapter;
    private ArrayList<InfoWifi> wifi_list;
    private InfoWifi get_infoWifi;

    int result_num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wifi);

        srl_wifi_list = findViewById(R.id.srl_wifi_list);
        srl_wifi_list.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshWifiList();

                srl_wifi_list.setRefreshing(false);
            }
        });

        lv_wifi_list = findViewById(R.id.lv_wifi_list);

        wifi_list = new ArrayList<>();

        wifiListAdapter = new WifiListAdapter();
        lv_wifi_list.setAdapter(wifiListAdapter);
        lv_wifi_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InfoWifi infoWifi = wifi_list.get(position);

                //연결된 wifi의 비밀번호는 가져올 수 없음. 무조건 비밀번호 치고 연결되도록 해야함.
//                if (infoWifi.apSecurity == infoWifi.AP_SECURITY_NONE ||
//                        infoWifi.apStatus == infoWifi.AP_STATUS_CONNECTED ||
//                        infoWifi.apStatus == infoWifi.AP_STATUS_SAVED) {
//                    showDialogWifiConnect(infoWifi);
//                } else {
//                    showDialogWifiPassword(infoWifi);
//                }

                showDialogWifiPassword(infoWifi);
            }
        });

        refreshWifiList();
    }

    private void showDialogWifiConnect(InfoWifi infoWifi) {
        dialogWifiPassword = new DialogWifiPassword(context, infoWifi);
        dialogWifiPassword.setCanceledOnTouchOutside(false);
        dialogWifiPassword.show();
        dialogWifiPassword.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (dialogWifiPassword.getResult() == 1) {
                    result_num = 1;
                    get_infoWifi = dialogWifiPassword.getInfoWifi();
                    dismiss();
                } else {
                    refreshWifiList();
                }
            }
        });
    }

    private void showDialogWifiPassword(InfoWifi infoWifi) {
        dialogWifiPassword = new DialogWifiPassword(context, infoWifi);
        dialogWifiPassword.setCanceledOnTouchOutside(false);
        dialogWifiPassword.show();
        dialogWifiPassword.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (dialogWifiPassword.getResult() == 1) {
                    result_num = 1;
                    get_infoWifi = dialogWifiPassword.getInfoWifi();
                    dismiss();
                } else {
                    refreshWifiList();
                }
            }
        });
    }

    private void refreshWifiList() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //WiFi 목록을 검사하기 위해 위치 권한도 필요합니다. 허락을 먼저 구합시다.
            activity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

            // 권한을 받지 못했기에 실행하지 않습니다.
            return;
        }
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                // 스캔중이면 더 스캔하지 않습니다.
                if (isScanning) {
                    return null;
                }
                isScanning = true;

                wifi_list = WifiScanner.getWifiApList(context);

                isScanning = false;
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (!isScanning) {
                    // 스캔중이 아닐때만 데이터셋 변경을 알려 ConcurrentModificationException을 방지합니다.
                    wifiListAdapter.notifyDataSetChanged();
                }
            }
        }.execute();
    }

    private class WifiListAdapter extends BaseAdapter {
        LayoutInflater inflater;

        public WifiListAdapter() {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return wifi_list.size();
        }

        @Override
        public InfoWifi getItem(int i) {
            return wifi_list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0L;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_wifi_entry, parent, false);
            }

            InfoWifi infoWifi = wifi_list.get(position);

            TextView tvApName = convertView.findViewById(R.id.tv_aplist_item_ap_name);
            TextView tvApStrength = convertView.findViewById(R.id.tv_aplist_item_ap_strength);
            TextView tvApStatus = convertView.findViewById(R.id.tv_aplist_item_ap_status);

            tvApName.setText(infoWifi.apName);
            tvApStrength.setText(infoWifi.apStrength + " dBm");
            switch (infoWifi.apStatus) {
                case InfoWifi.AP_STATUS_AVAILABLE:
                    tvApStatus.setText(String.format(context.getString(R.string.ap_status_available), getWiFiSecurityString(infoWifi.apSecurity, context)));
                    break;
                case InfoWifi.AP_STATUS_NOT_IN_RANGE:
                    tvApStatus.setText(R.string.ap_status_not_in_range);
                    break;
                case InfoWifi.AP_STATUS_FAILED:
                    tvApStatus.setText(R.string.ap_status_failed);
                    break;
                case InfoWifi.AP_STATUS_SAVED:
                    tvApStatus.setText(String.format(context.getString(R.string.ap_status_saved), getWiFiSecurityString(infoWifi.apSecurity, context)));
                    break;
                case InfoWifi.AP_STATUS_CONNECTED:
                    tvApStatus.setText(R.string.ap_status_connected);
                    break;
            }

            return convertView;
        }
    }

    private String getWiFiSecurityString(int security, Context ctx) {
        String securityStr;

        switch (security) {
            case InfoWifi.AP_SECURITY_EAP:
                securityStr = "EAP";
                break;
            case InfoWifi.AP_SECURITY_WPA2:
                securityStr = "WPA2";
                break;
            case InfoWifi.AP_SECURITY_WPA:
                securityStr = "WPA";
                break;
            case InfoWifi.AP_SECURITY_WEP:
                securityStr = "WEP";
                break;
            default:
            case InfoWifi.AP_SECURITY_NONE:
                securityStr = ctx.getString(R.string.ap_security_open);
                break;
        }

        return securityStr;
    }

    public int getResult() {
        return result_num;
    }

    public InfoWifi getInfoWifi() {
        return get_infoWifi;
    }
}
