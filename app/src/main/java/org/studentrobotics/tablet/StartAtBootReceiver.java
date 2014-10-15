package org.studentrobotics.tablet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

public class StartAtBootReceiver extends BroadcastReceiver {

    private static final String TAG = StartAtBootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Got boot finished broadcast receiver.");
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        manager.setWifiEnabled(true);
        manager.startScan();
    }

}
