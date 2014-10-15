package org.studentrobotics.tablet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class WiFiScanResultsReceiver extends BroadcastReceiver {

    private static final String TAG = WiFiScanResultsReceiver.class.getSimpleName();

    private static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifi.isConnectedOrConnecting();
    }

    private static File getWiFiConfigurationFile() {
        File directory = Environment.getExternalStorageDirectory();
        return new File(directory, "wifi");
    }

    private static WifiConfiguration buildConfiguration(ScanResult result, String password) {
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "\"" + result.SSID + "\"";
        config.preSharedKey = "\"" + password + "\"";
        config.status = WifiConfiguration.Status.ENABLED;
        return config;
    }

    private static boolean hasNetworkConfiguration(ScanResult result, WifiManager wifi) {
        for (WifiConfiguration config : wifi.getConfiguredNetworks()) {
            if (config.SSID.equals("\"" + result.SSID + "\"")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isOnline(context)) {
            // We can assume they have a working internet connection here, we don't need to do
            // anything.
            return;
        }

        String ssid, password;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(getWiFiConfigurationFile()));
            ssid = reader.readLine().trim();
            password = reader.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Log.i(TAG, "WiFi scan results available!");
        Log.d(TAG, "We're not online... better do something about that.");
        Log.d(TAG, "Looking for WiFi SSID of: " + ssid);

        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        List<ScanResult> results = wifi.getScanResults();
        for (ScanResult result : results) {
            Log.d(TAG, "Found a result: " + result.SSID + " (" + result.BSSID + ")");
            if (result.SSID.equals(ssid)) {
                if (!hasNetworkConfiguration(result, wifi)) {
                    WifiConfiguration configuration = buildConfiguration(result, password);
                    Log.i(TAG, "Adding a network configuration for: " + ssid);
                    int id = wifi.addNetwork(configuration);
                    wifi.enableNetwork(id, false);
                }
            }
        }
    }

}
