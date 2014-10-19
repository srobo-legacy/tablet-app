package org.studentrobotics.tablet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private class OpenBrowserTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            if (isServerAvailable("robot.sr")) {
                return "robot.sr";
            } else if (isServerAvailable("robot")) {
                return "robot";
            } else {
                return null;
            }
        }

        private boolean isServerAvailable(String server) {
            try {
                Log.d(TAG, "Trying robot at: " + server);
                InetAddress ina = InetAddress.getByName(server);
                return ina.isReachable(1000);
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
            if (server == null) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Please ensure the tablet is connected to the robot via WiFi and try again. If this problem persists, consult the docs or ask a question on the forum.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .show();
            } else {
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
