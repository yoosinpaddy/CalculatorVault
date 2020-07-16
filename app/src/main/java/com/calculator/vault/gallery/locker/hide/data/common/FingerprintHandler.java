package com.calculator.vault.gallery.locker.hide.data.common;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.activity.SelectionActivity;
import com.calculator.vault.gallery.locker.hide.data.appLock.PatternActivity;
import com.calculator.vault.gallery.locker.hide.data.appLock.Preferences;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Activity context;
    private boolean isFromPin = false;
    private boolean isChanging = false;
    private static final String TAG = "FingerprintHandler";


    // Constructor
    public FingerprintHandler(Activity mContext, boolean isFromPin) {
        context = mContext;
        this.isFromPin = isFromPin;
    }
    public void setChanging(Boolean a){
        isChanging=a;
    }


    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString, false);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString, false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.", false);
    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Fingerprint Authentication succeeded.", true);
    }


    public void update(String e, Boolean success) {
        if (success) {
            if (isFromPin) {
                if (context instanceof PatternActivity){
                   ((PatternActivity) context).fingerValidated();
                }else {
                    Log.e(TAG, "update: was not an instance" );
                    Preferences.setStringPref(context, Preferences.KEY_TOP, context.getIntent().getStringExtra("package"));
                    Preferences.setBooleanPref(context, Preferences.KEY_LOCKED, false);
                    context.finishAffinity();
                    context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            } else {
                Intent intent = new Intent(context, SelectionActivity.class);
                context.startActivity(intent);
                context.finish();
            }
        }
    }

}
