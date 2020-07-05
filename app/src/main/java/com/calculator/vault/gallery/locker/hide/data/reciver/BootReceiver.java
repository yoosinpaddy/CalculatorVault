package com.calculator.vault.gallery.locker.hide.data.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.calculator.vault.gallery.locker.hide.data.appLock.Preferences;
import com.calculator.vault.gallery.locker.hide.data.appLock.QuickSettingsLockService;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive: Boot Completed" + intent.getAction());
        if (!Preferences.getBooleanPref(context, Preferences.KEY_SERVICE_INITIAL_START)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, QuickSettingsLockService.class));
            } else {
                context.startService(new Intent(context, QuickSettingsLockService.class));
            }
        }
    }
}
