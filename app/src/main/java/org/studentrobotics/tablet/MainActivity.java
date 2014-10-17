package org.studentrobotics.tablet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class MainActivity extends Activity {

    private class OpenBrowserTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            if (isServerAvailable("robot.sr")) {
                return "robot.sr";
            } else {
                return "robot";
            }
        }

        private boolean isServerAvailable(String server) {
            try {
                InetAddress ina = InetAddress.getByName(server);
                return ina.isReachable(500);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        private boolean isAppInstalled(String name, Context context) {
            PackageManager pm = context.getPackageManager();
            try {
                pm.getPackageInfo(name, PackageManager.GET_ACTIVITIES);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(String server) {
            Intent intent;

            if (isAppInstalled("org.chromium.chrome.shell", MainActivity.this)) {
                intent = getPackageManager().getLaunchIntentForPackage("org.chromium.chrome.shell");
            } else {
                intent = new Intent(Intent.ACTION_VIEW);
            }

            intent.setData(Uri.parse("http://" + server));
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            new OpenBrowserTask().execute();

            WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            manager.setWifiEnabled(true);
            manager.startScan();
        }
    }

}
