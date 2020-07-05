package com.calculator.vault.gallery.locker.hide.data.appLock;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.common.Share;

import java.util.Timer;
import java.util.TimerTask;

public class QuickSettingsLockService extends Service {

    private String TAG = "FileObserverService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("SERVICE", "onCreate");
        registerReceiver(wifi, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        registerReceiver(bluetooth, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

        new Handler().postDelayed(() -> Preferences.setBooleanPref(QuickSettingsLockService.this,
                Preferences.KEY_SERVICE_INITIAL_START, false), 1500);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SERVICE", "onStartCommand");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel("AppLockService",
                    "App Lock Service", NotificationManager.IMPORTANCE_NONE);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (service != null) {
                service.createNotificationChannel(notificationChannel);
            }

            Notification.Builder builder = new Notification.Builder(this, "AppLockService")
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("App Lock is running!")
                    .setAutoCancel(true);

            Notification notification = builder.build();
            startForeground(1, notification);
            letTheShowBegin();

        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                    "fileObserverDemo")
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("FileObserverDemo is running!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            Notification notification = builder.build();
            startForeground(1, notification);
            letTheShowBegin();
        }

        return START_STICKY;
    }

    private void letTheShowBegin() {

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.e(TAG, "run: APP LOCK SERVICE IS RUNNING");
            }
        }, 0, 1000);

    }

    private BroadcastReceiver wifi = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            try {
                int WifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);

                Log.e(TAG, "onReceive: WIFI " + WifiState);
                Log.e(TAG, "onReceive: WIFI " + Preferences.getBooleanPref(QuickSettingsLockService.this, Preferences.KEY_SERVICE_INITIAL_START));


                Log.e(TAG, "onReceive__: bluetooth " + Preferences.getBooleanPref(QuickSettingsLockService.this, Preferences.KEY_SERVICE_BLUETOOTH_START));
                Log.e(TAG, "onReceive__: WIFI " + Preferences.getBooleanPref(QuickSettingsLockService.this, Preferences.KEY_SERVICE_WIFI_START));


                if (!Preferences.getBooleanPref(QuickSettingsLockService.this, Preferences.KEY_SERVICE_INITIAL_START)) {

                    switch (WifiState) {
                        case WifiManager.WIFI_STATE_ENABLED:

                            Log.e(TAG, "onReceive___  --> WIFI_STATE_ENABLED");


                            if (!Preferences.getBooleanPref(QuickSettingsLockService.this, Preferences.KEY_ENABLED_FROM_PATTERN)) {
                                Intent iPattern = new Intent(QuickSettingsLockService.this, PatternActivity.class);
                                if (!Preferences.getBooleanPref(QuickSettingsLockService.this, Preferences.KEY_IS_PATTERN_LOCK_ENABLED)) {
                                    iPattern = new Intent(QuickSettingsLockService.this, PinActivity.class);
                                }
                                iPattern.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                iPattern.putExtra("package", "wifi_enable");
                                Share.msCurrentLockedPackege = "wifi_enable";

                                if (Preferences.getBooleanPref(QuickSettingsLockService.this, Preferences.KEY_SERVICE_WIFI_START)) {
                                    Log.i(TAG, "onReceive11 WIFI: true");
                                    startActivity(iPattern);
                                } else {
                                    Log.i(TAG, "onReceive11 WIFI: false");
                                }

                            }
                            break;

                        case WifiManager.WIFI_STATE_ENABLING:
                            Log.e(TAG, "onReceive: WIFI_ENABLING");
                            break;

                        case WifiManager.WIFI_STATE_DISABLED:

                            Log.e(TAG, "onReceive___  --> WIFI_STATE_DISABLED");

                            if (!Preferences.getBooleanPref(QuickSettingsLockService.this, Preferences.KEY_DISABLED_FROM_PATTERN)) {
                                Intent iPatternl = new Intent(QuickSettingsLockService.this, PatternActivity.class);
                                if (!Preferences.getBooleanPref(QuickSettingsLockService.this, Preferences.KEY_IS_PATTERN_LOCK_ENABLED)) {
                                    iPatternl = new Intent(QuickSettingsLockService.this, PinActivity.class);
                                }
                                iPatternl.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                iPatternl.putExtra("package", "wifi_disable");
                                Share.msCurrentLockedPackege = "wifi_enable";


                                if (Preferences.getBooleanPref(QuickSettingsLockService.this, Preferences.KEY_SERVICE_WIFI_START)) {
                                    Log.i(TAG, "onReceive11 WIFI: true");
                                    startActivity(iPatternl);
                                } else {
                                    Log.i(TAG, "onReceive11 WIFI: false");
                                }


                            }
                            break;

                        case WifiManager.WIFI_STATE_DISABLING:
                            Log.e(TAG, "onReceive: WIFI_DISABLING");
                            break;

                        case WifiManager.WIFI_STATE_UNKNOWN:
                            Log.e(TAG, "onReceive: WIFI_UNKNOWN");
                            break;

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    };

    private final BroadcastReceiver bluetooth = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.e(TAG, "onReceive: " + action);


            Log.e(TAG, "onReceive__: bluetooth " + Preferences.getBooleanPref(QuickSettingsLockService.this, Preferences.KEY_SERVICE_BLUETOOTH_START));
            Log.e(TAG, "onReceive__: WIFI " + Preferences.getBooleanPref(QuickSettingsLockService.this, Preferences.KEY_SERVICE_WIFI_START));


            if (!Preferences.getBooleanPref(QuickSettingsLockService.this, Preferences.KEY_SERVICE_INITIAL_START)) {
                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);


                    switch (state) {
                        case BluetoothAdapter.STATE_OFF:
                            if (!Preferences.getBooleanPref(QuickSettingsLockService.this, Preferences.KEY_BLUETOOTH_DISABLED_FROM_PATTERN)) {
                                Intent iPattern = new Intent(QuickSettingsLockService.this, PatternActivity.class);
                                if (!Preferences.getBooleanPref(QuickSettingsLockService.this, Preferences.KEY_IS_PATTERN_LOCK_ENABLED)) {
                                    iPattern = new Intent(QuickSettingsLockService.this, PinActivity.class);
                                }
                                iPattern.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                iPattern.putExtra("package", "bluetooth_disable");
                                Share.msCurrentLockedPackege = "bluetooth_disable";

                                if (Preferences.getBooleanPref(QuickSettingsLockService.this, Preferences.KEY_SERVICE_BLUETOOTH_START)) {
                                    Log.i(TAG, "onReceive11 bluetooth: true");
                                    startActivity(iPattern);
                                } else {
                                    Log.i(TAG, "onReceive11 bluetooth: false");
                                }

                            }
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:

                            break;
                        case BluetoothAdapter.STATE_ON:
                            if (!Preferences.getBooleanPref(QuickSettingsLockService.this, Preferences.KEY_BLUETOOTH_ENABLED_FROM_PATTERN)) {
                                Intent iPattern = new Intent(QuickSettingsLockService.this, PatternActivity.class);
                                if (!Preferences.getBooleanPref(QuickSettingsLockService.this, Preferences.KEY_IS_PATTERN_LOCK_ENABLED)) {
                                    iPattern = new Intent(QuickSettingsLockService.this, PinActivity.class);
                                }
                                iPattern.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                iPattern.putExtra("package", "bluetooth_enable");
                                Share.msCurrentLockedPackege = "bluetooth_enable";


                                if (Preferences.getBooleanPref(QuickSettingsLockService.this, Preferences.KEY_SERVICE_BLUETOOTH_START)) {
                                    Log.i(TAG, "onReceive11 bluetooth: true");
                                    startActivity(iPattern);
                                } else {
                                    Log.i(TAG, "onReceive11 bluetooth: false");
                                }


                            }
                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:

                            break;
                    }
                }
            }

        }
    };

    @Override
    public void onDestroy() {
        unregisterReceiver(wifi);
        unregisterReceiver(bluetooth);
        super.onDestroy();
        stopSelf();
    }
}
