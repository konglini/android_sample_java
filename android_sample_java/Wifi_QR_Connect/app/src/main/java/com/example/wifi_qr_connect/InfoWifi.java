package com.example.wifi_qr_connect;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by guru_dev_MMS on 2017-08-28.
 */

public class InfoWifi implements Comparable<InfoWifi> {
    public String apName;
    public int apStatus;
    public int apStrength;
    public int apSecurity;
    public String apBssid;
    public String apPasspharase;

    public static final String KEY_AP_NAME = "ap_name";
    public static final String KEY_AP_STATUS = "ap_status";
    public static final String KEY_AP_STRENGTH = "ap_strength";
    public static final String KEY_AP_SECURITY = "ap_security";
    public static final String KEY_AP_BSSID = "ap_bssid";
    public static final String KEY_AP_PASSPHRASE = "ap_passphrase";

    public static final int AP_STATUS_AVAILABLE = 0;
    public static final int AP_STATUS_NOT_IN_RANGE = -1;
    public static final int AP_STATUS_FAILED = -2;
    public static final int AP_STATUS_SAVED = 1;
    public static final int AP_STATUS_CONNECTED = 2;

    public static final int AP_SECURITY_NONE = 0;
    public static final int AP_SECURITY_WEP = 1;
    public static final int AP_SECURITY_WPA = 2;
    public static final int AP_SECURITY_WPA2 = 3;
    public static final int AP_SECURITY_EAP = 4;

    @Override
    public int compareTo(@NonNull InfoWifi other) {
        if (this.apStatus == AP_STATUS_CONNECTED) {
            return -999;    // 신호세기는 100 넘는게 잘 없을테니
        } else if (other.apStatus == AP_STATUS_CONNECTED) {
            return +999;    // 신호세기는 100 넘는게 잘 없을테니
        } else {
            return ((Integer) other.apStrength).compareTo(this.apStrength);
        }
    }

    @Override
    public String toString() {
        return "WifiSimpleInfo{" +
                "apName='" + apName + '\'' +
                ", apStatus=" + apStatus +
                ", apStrength=" + apStrength +
                ", apSecurity=" + apSecurity +
                ", apBssid='" + apBssid + '\'' +
                ", apPasspharase='" + apPasspharase + '\'' +
                '}';
    }

    public String toJson() {

        JSONObject result = new JSONObject();
        try {
            result.put(KEY_AP_NAME, apName);
            result.put(KEY_AP_STATUS, apStatus);
            result.put(KEY_AP_STRENGTH, apStrength);
            result.put(KEY_AP_SECURITY, apSecurity);
            result.put(KEY_AP_BSSID, apBssid);
            result.put(KEY_AP_PASSPHRASE, apPasspharase);
        } catch (JSONException jsone) {
            jsone.printStackTrace();
        }
        return result.toString();
    }

    public static InfoWifi fromJson(String json) {

        try {
            InfoWifi result = new InfoWifi();

            JSONObject jsonObject = new JSONObject(json);

            result.apName = jsonObject.getString(KEY_AP_NAME);
            result.apStatus = jsonObject.getInt(KEY_AP_STATUS);
            result.apStrength = jsonObject.getInt(KEY_AP_STRENGTH);
            result.apSecurity = jsonObject.getInt(KEY_AP_SECURITY);
            result.apBssid = jsonObject.getString(KEY_AP_BSSID);
            result.apPasspharase = jsonObject.getString(KEY_AP_PASSPHRASE);

            return result;
        } catch (JSONException jsone) {
            jsone.printStackTrace();
            return null;
        }
    }
}
