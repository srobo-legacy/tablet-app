package org.studentrobotics.tablet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

public class WiFiScanResultsReceiver extends BroadcastReceiver {

    private static final String TAG = WiFiScanResultsReceiver.class.getSimpleName();

    private static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifi.isConnectedOrConnecting();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isOnline(context)) {
            // We can assume they have a working internet connection here, we don't need to do
            // anything.
            return;
        }

        Log.i(TAG, "WiFi scan results available!");
        Log.d(TAG, "We're not online... better do something about that.");

        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> results = wifi.getScanResults();
        for (ScanResult result : results) {
            Log.d(TAG, "Found a result: " + result.SSID + " (" + result.BSSID + ")");
        }
    }

}
