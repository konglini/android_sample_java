package com.example.wifi_qr_connect;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class WifiScanner {

    private static final String LOG_TAG = "WifiScanner";
    private static final boolean IS_DEBUG = true;

    /**
     * 주변의 Wi-Fi AP들을 스캔해서 목록으로 반환합니다.
     * 신호세기가 센 순으로 정렬되어있습니다.
     *
     * @param ctx WifiManager 서비스를 가져오기 위한 Context 객체
     * @return WifiSimpleInfo 목록
     */
    public static ArrayList<InfoWifi> getWifiApList(Context ctx) {
        ArrayList<InfoWifi> apList = null;

        //https://stackoverflow.com/a/28906046
        WifiManager wifiManager = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> networkList = wifiManager.getScanResults();

        //get current connected SSID for comparison to ScanResult
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String currentSSID = null;
        String currentBSSID = null;
        if (wifiInfo != null) {
            currentSSID = wifiInfo.getSSID();
            currentBSSID = wifiInfo.getBSSID();
        }

        List<WifiConfiguration> savedApList = null;

        if (networkList != null) {
            apList = new ArrayList<>();

            for (ScanResult network : networkList) {
                if (savedApList == null) {
                    savedApList = wifiManager.getConfiguredNetworks();
                }

                InfoWifi infoWifi = new InfoWifi();
                //check if current connected SSID
                //if (currentSSID.equals(network.SSID)){
                //get capabilities of current connection
                String capabilities = network.capabilities;

                if (IS_DEBUG) {
                    Log.d(LOG_TAG, network.SSID + "(" + network.BSSID + ")" + " capabilities : " + capabilities);
                }

                if(network.SSID.isEmpty()) continue;

                // BSSID는 AP의 고유 주소 입니다.
                infoWifi.apBssid = network.BSSID;

                // SSID는 AP 이름입니다.
                infoWifi.apName = network.SSID;

                // dBm 단위의 AP 신호 세기입니다.
                infoWifi.apStrength = network.level;

                // 보안 수준
                if (capabilities.contains("EAP")) {
                    //EAP 보안
                    infoWifi.apSecurity = InfoWifi.AP_SECURITY_EAP;
                } else if (capabilities.contains("WPA2")) {
                    //WPA2 보안
                    infoWifi.apSecurity = InfoWifi.AP_SECURITY_WPA2;
                } else if (capabilities.contains("WPA")) {
                    //WPA 보안
                    infoWifi.apSecurity = InfoWifi.AP_SECURITY_WPA;
                } else if (capabilities.contains("WEP")) {
                    //WEP 보안
                    infoWifi.apSecurity = InfoWifi.AP_SECURITY_WEP;
                } else {
                    //개방형?
                    infoWifi.apSecurity = InfoWifi.AP_SECURITY_NONE;
                }
                //}

                // 와이파이 상태
                if ((currentSSID != null && currentSSID.equals(infoWifi.apName)) ||
                        (currentBSSID != null && currentBSSID.equals(infoWifi.apBssid))) {
                    infoWifi.apStatus = InfoWifi.AP_STATUS_CONNECTED;
                } else if (checkIfWifiSaved(infoWifi.apName, savedApList) >= 0) {
                    infoWifi.apStatus = InfoWifi.AP_STATUS_SAVED;
                }

                //비밀번호는 공란으로 두기
                infoWifi.apPasspharase = "";

                apList.add(infoWifi);
            }
            Collections.sort(apList);
        }

        return apList;
    }

    /**
     * 제공된 AP 정보와 비밀번호로 지정된 AP에 접속하는 메소드입니다.
     *
     * @param infoWifi AP 정보 (비밀번호 포함)
     * @param ctx      WifiManager 서비스를 가져오기 위한 Context 객체
     * @return 성공 여부
     */
    public static boolean connectToWifi(InfoWifi infoWifi, Context ctx) {
        boolean isSuccessful = false;

        WifiManager wifiManager = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int netId = -1;
        String password = infoWifi.apPasspharase == null ? "" : infoWifi.apPasspharase;

        //비밀번호가 들어있는 경우는 입력받은 경우이므로 저장되거나 연결된 상태가 아님
        if (password.isEmpty() &&
                (infoWifi.apStatus == InfoWifi.AP_STATUS_SAVED ||
                        infoWifi.apStatus == InfoWifi.AP_STATUS_CONNECTED)) {
            List<WifiConfiguration> wifiConfigurations = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration wifiConfiguration : wifiConfigurations) {
                if (wifiConfiguration.SSID.equals("\"" + infoWifi.apName + "\"")) {
                    netId = wifiConfiguration.networkId;
                    break;
                }
            }
        } else {
            WifiConfiguration wifiConfig = new WifiConfiguration();
            wifiConfig.SSID = String.format("\"%s\"", infoWifi.apName);
            //           wifiConfig.BSSID = wifiSimpleInfo.apBssid;
            switch (infoWifi.apSecurity) {
                default:
                case InfoWifi.AP_SECURITY_NONE:
                    wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    wifiConfig.allowedAuthAlgorithms.clear();

                    break;
                case InfoWifi.AP_SECURITY_WEP:
                    wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);

                    if (wifiConfig.wepKeys == null || wifiConfig.wepKeys.length == 0) {
                        wifiConfig.wepKeys = new String[1]; // 단일 공유 키 대상
                    }
                    if (password != null && password.matches("^[0-9a-fA-F]+$")) {
                        // 비밀번호가 16진수 인 경우에만 따옴표로 감싸지 않습니다.
                        wifiConfig.wepKeys[0] = password;
                    } else {
                        wifiConfig.wepKeys[0] = String.format("\"%s\"", password);
                    }
                    break;
                case InfoWifi.AP_SECURITY_WPA:
                case InfoWifi.AP_SECURITY_WPA2:
                    wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                    wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                    wifiConfig.preSharedKey = password != null ? String.format("\"%s\"", password) : "";
                    break;
                case InfoWifi.AP_SECURITY_EAP:
                    wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
                    wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.LEAP);
                    wifiConfig.preSharedKey = password != null ? String.format("\"%s\"", password) : "";
                    break;
            }

            Log.i("!---", wifiConfig.SSID + "/" + infoWifi.apSecurity);
            // 추가한 네트워크id를 기억합니다.
            netId = wifiManager.addNetwork(wifiConfig);

            if(wifiManager.getWifiState()!=WifiManager.WIFI_STATE_ENABLED) wifiManager.setWifiEnabled(true);
        }
        try {
            boolean isDisconnected = wifiManager.disconnect();
            boolean isEnabled = wifiManager.enableNetwork(netId, true);
            boolean isReconnected = wifiManager.reconnect();

            isSuccessful = isDisconnected && isEnabled && isReconnected;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSuccessful;
    }

    public static boolean deleteWifi(InfoWifi infoWifi, Context ctx) {
        boolean isSuccessful = false;

        WifiManager wifiManager = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        List<WifiConfiguration> wifiConfigurations = wifiManager.getConfiguredNetworks();

        try {
            for (WifiConfiguration wifiConfiguration : wifiConfigurations) {
                if (wifiConfiguration.SSID.equals("\"" + infoWifi.apName + "\"")) {
                    boolean isDisconnected = wifiManager.disconnect();
                    boolean isRemoved = wifiManager.removeNetwork(wifiConfiguration.networkId);
                    wifiManager.reconnect(); // reconnect는 필수가 아니지 않을까요
                    isSuccessful = isDisconnected && isRemoved;
                    return isSuccessful; //TODO 20170830-1505 한 번에 하나만 지울 수 있게 할까 같은 이름은 다 지워지게 할까
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSuccessful;
    }

    /**
     * Wi-Fi 설정 목록에서 지정된 SSID로 된 AP가 있는지 확인합니다.
     *
     * @param ssid               AP의 SSID
     * @param wifiConfigurations Wi-Fi 설정목록
     * @return 0 < 값: 해당 SSID의 Wi-Fi 설정의 네트워크 id
     * -1 : 존재하지 않는 경우
     */
    private static int checkIfWifiSaved(String ssid, List<WifiConfiguration> wifiConfigurations) {
        if (wifiConfigurations != null) {
            for (WifiConfiguration wifiConfiguration : wifiConfigurations) {
                if (wifiConfiguration.SSID.equals("\"" + ssid + "\"")) {
                    return wifiConfiguration.networkId;
                }
            }
        }
        return -1;
    }

}
