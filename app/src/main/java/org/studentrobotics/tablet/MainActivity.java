package org.studentrobotics.tablet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Intent intent;

            if (isAppInstalled("org.chromium.chrome.shell", this)) {
                intent = getPackageManager().getLaunchIntentForPackage("org.chromium.chrome.shell");
            } else {
                intent = new Intent(Intent.ACTION_VIEW);
            }

            // TODO: test for robot.sr or robot

            intent.setData(Uri.parse("http://robot"));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

            WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            manager.setWifiEnabled(true);
            manager.startScan();
        }
    }

    private static boolean isAppInstalled(String name, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(name, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}
