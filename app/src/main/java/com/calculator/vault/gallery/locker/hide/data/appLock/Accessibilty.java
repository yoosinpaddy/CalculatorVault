package com.calculator.vault.gallery.locker.hide.data.appLock;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.webkit.WebView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.appLock.patternlockview.PatternLockView;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Accessibilty extends AccessibilityService {

    //public volatile AccessibilityNodeInfo findFocus;
    private static final String TAG = "AppLockService";
    private String topAppName, previousAppName = "blank";
    private ArrayList<String> lockedPackages = new ArrayList<>();

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event.getPackageName() != null) {
            topAppName = event.getPackageName().toString();
            String savedP = Preferences.getStringPref(Accessibilty.this, Preferences.KEY_PACKAGES);
            if (savedP != null && !savedP.isEmpty()) {
                lockedPackages = new Gson().fromJson(savedP, new TypeToken<ArrayList<String>>() {
                }.getType());
            } else {
                lockedPackages = new ArrayList<>();
            }

            //findFocus = event.getSource();
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                Log.e(TAG, "onAccessibilityEvent: TOPAPPNAME :" + topAppName);
//            Log.e("TAG", "onAccessibilityEvent: " + findFocus.getPackageName());

//            for (int i = 0; i < lockedPackages.size(); i++) {
//                Log.e(TAG, "onAccessibilityEvent: LOCKED:: " + lockedPackages.get(i));
//            }

                if (lockedPackages.contains(topAppName)) {
                    Log.e(TAG, "Lock Found For: " + topAppName);
                    boolean flag = Preferences.getBooleanPref2(Accessibilty.this, Preferences.KEY_LOCKED);

                    Log.e(TAG, "Condition1: " + !topAppName.equals(Preferences.getStringPref(Accessibilty.this, Preferences.KEY_TOP)));
                    Log.e(TAG, "Condition2: " + flag);
                    Log.e(TAG, "Condition3: " + !topAppName.equals(previousAppName));

                    if (!topAppName.equals(Preferences.getStringPref(Accessibilty.this, Preferences.KEY_TOP)) && flag && !topAppName.equals(previousAppName)) {
                        Log.e(TAG, "Unlock Pin Open: ");
                        Intent iPin;
                        if (!Preferences.getBooleanPref(Accessibilty.this, Preferences.KEY_IS_PATTERN_LOCK_ENABLED)) {
                            iPin = new Intent(Accessibilty.this, PinActivity.class);
                        }else {
                            iPin = new Intent(Accessibilty.this, PatternActivity.class);
                        }
                        iPin.putExtra("package", topAppName);
                        Share.msCurrentLockedPackege = topAppName;
                        iPin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.fade_in, R.anim.fade_out);
                        startActivity(iPin, options.toBundle());
                        //startActivity(iPin);
                        previousAppName = topAppName;
                    } else {
                        Log.e(TAG, "Unlock Pin not Open: ");
                        new Handler().postDelayed(() -> Preferences.setBooleanPref(Accessibilty.this, Preferences.KEY_LOCKED, true), 1500);
                        previousAppName = topAppName;
                    }
                } else {
                    Log.e(TAG, "Lock not Found For: " + topAppName);
                    previousAppName = topAppName;
                }
                Preferences.setStringPref(Accessibilty.this, Preferences.KEY_TOP, "");
            }
        }
    }

    @Override
    public void onInterrupt() {

    }
}
