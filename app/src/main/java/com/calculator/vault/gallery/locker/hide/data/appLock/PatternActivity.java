package com.calculator.vault.gallery.locker.hide.data.appLock;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.appLock.patternlockview.PatternLockView;
import com.calculator.vault.gallery.locker.hide.data.appLock.patternlockview.listener.PatternLockViewListener;
import com.calculator.vault.gallery.locker.hide.data.appLock.patternlockview.utils.PatternLockUtils;
import com.calculator.vault.gallery.locker.hide.data.common.FingerprintHandler;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class PatternActivity extends AppCompatActivity {

    private static final String TAG = "PatternActivity";
    private PatternLockView pvLock;
    private ConstraintLayout cvMain;
    private ImageView ivAppIcon;
    private TextView tvAppName;
    private String fromWhere;
    private String pOne = "";

    private KeyStore keyStore;
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "com.calculatorvault.backup";
    private Cipher cipher;
    Boolean isChanging=false;
    FingerprintHandler helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_pattern);

        fromWhere = getIntent().getStringExtra("fromWhere");
        if (fromWhere == null) {
            fromWhere = "";
        }

        PackageManager pm = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED);

        cvMain = findViewById(R.id.pattern_main);
        pvLock = findViewById(R.id.pattern_pv_view);

        ivAppIcon = findViewById(R.id.pattern_iv_appIcon);
        tvAppName = findViewById(R.id.pattern_tv_appName);

        if (fromWhere.equalsIgnoreCase("new_pattern")) {
            ivAppIcon.setImageResource(R.drawable.ic_applock);
            tvAppName.setText(R.string.set_new_pattern);
        } else if (fromWhere.equalsIgnoreCase("change_pattern")) {
            ivAppIcon.setImageResource(R.drawable.ic_applock);
            tvAppName.setText(R.string.draw_old_pattern);
        }

        String savedList = Preferences.getStringPref(PatternActivity.this, Preferences.KEY_SAVED_THEME_LIST);
        ArrayList<ThemeModel> themeModels = new Gson().fromJson(savedList, new TypeToken<ArrayList<ThemeModel>>() {
        }.getType());
        for (int i = 0; i < themeModels.size(); i++) {
            if (themeModels.get(i).isSelected()) {
                cvMain.setBackgroundResource(themeModels.get(i).getThemeBgRes());
                if (themeModels.get(i).getShapeRes() != -1) {
                    pvLock.setDotDrawable(ActivityCompat.getDrawable(PatternActivity.this, themeModels.get(i).getShapeRes()));
                } else {
                    pvLock.setDotNormalSize(dpToPx(PatternActivity.this, 18));
                    pvLock.setDotSelectedSize(dpToPx(PatternActivity.this, 18));
                }
                pvLock.setNormalStateColor(ActivityCompat.getColor(PatternActivity.this, themeModels.get(i).getLineColorRes()));
            }

        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).activityInfo.applicationInfo.packageName.equals(Share.msCurrentLockedPackege)) {
                ivAppIcon.setImageDrawable(list.get(i).activityInfo.applicationInfo.loadIcon(pm));
                tvAppName.setText(list.get(i).activityInfo.applicationInfo.loadLabel(pm).toString());
            }
        }

        //WIFI.......
        if (Share.msCurrentLockedPackege.equals("wifi_enable")) {
            ivAppIcon.setImageResource(R.drawable.ic_wifi);
            tvAppName.setText("WiFi");
            Preferences.setBooleanPref(PatternActivity.this, Preferences.KEY_DISABLED_FROM_PATTERN, true);
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(false);
        }

        if (Share.msCurrentLockedPackege.equals("wifi_disable")) {
            ivAppIcon.setImageResource(R.drawable.ic_wifi);
            tvAppName.setText("WiFi");
            Preferences.setBooleanPref(PatternActivity.this, Preferences.KEY_ENABLED_FROM_PATTERN, true);
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
        }

        //BLUETOOTH.......
        if (Share.msCurrentLockedPackege.equals("bluetooth_disable")) {
            ivAppIcon.setImageResource(R.drawable.ic_bluetooth);
            tvAppName.setText("Bluetooth");
            Preferences.setBooleanPref(PatternActivity.this, Preferences.KEY_BLUETOOTH_ENABLED_FROM_PATTERN, true);
            turnOnBluetooth(true);
        }

        if (Share.msCurrentLockedPackege.equals("bluetooth_enable")) {
            ivAppIcon.setImageResource(R.drawable.ic_bluetooth);
            tvAppName.setText("Bluetooth");
            Preferences.setBooleanPref(PatternActivity.this, Preferences.KEY_BLUETOOTH_DISABLED_FROM_PATTERN, true);
            turnOnBluetooth(false);
        }


        pvLock.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {
                Log.e(TAG, "onStarted: ");
            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {
                //Log.e(TAG, "onProgress: " + PatternLockUtils.patternToString(pvLock, progressPattern));
            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {

                Log.e(TAG, "onComplete: " + PatternLockUtils.patternToString(pvLock, pattern));
                if (fromWhere.equalsIgnoreCase("new_pattern")) {
                    if (pOne.isEmpty()) {

                        if (PatternLockUtils.patternToString(pvLock, pattern).length() < 4) {
                            tvAppName.setText("Invalid pattern, draw again.");
                            pvLock.clearPattern();
                        } else {
                            pOne = PatternLockUtils.patternToString(pvLock, pattern);
                            tvAppName.setText("Draw again!");
                            pvLock.clearPattern();
                        }
                    } else {
                        if (!pOne.equals(PatternLockUtils.patternToString(pvLock, pattern))) {
                            tvAppName.setText("Draw again!");
                            pvLock.clearPattern();
                        } else {
                            Preferences.setStringPref(PatternActivity.this, Preferences.KEY_SAVED_PATTERN, PatternLockUtils.patternToString(pvLock, pattern));
                            Preferences.setBooleanPref(PatternActivity.this, Preferences.KEY_IS_PATTERN_LOCK_ENABLED, true);
                            pvLock.clearPattern();
                            setResult(RESULT_OK);
                            PatternActivity.this.finish();
                        }
                    }
                } else if (fromWhere.equalsIgnoreCase("change_pattern")) {

                    String savedPattern = Preferences.getStringPref(PatternActivity.this, Preferences.KEY_SAVED_PATTERN);
                    if (!savedPattern.equals(PatternLockUtils.patternToString(pvLock, pattern))) {
                        tvAppName.setText("WRONG PATTERN. Draw again!");
                        pvLock.clearPattern();
                    } else {
                        fromWhere = "new_pattern";
                        tvAppName.setText("Draw new pattern.");
                        pvLock.clearPattern();
                    }
                } else {
                    validatePattern(PatternLockUtils.patternToString(pvLock, pattern));
                }
            }

            @Override
            public void onCleared() {
            }
        });

        if (SharedPrefs.getBoolean(PatternActivity.this, SharedPrefs.isFingerLockOn)) {
            checkForFingerPrint();
        } else {
            findViewById(R.id.pattern_iv_finger).setVisibility(View.INVISIBLE);
        }
    }
    public void fingerValidated(){
        fromWhere = "new_pattern";
        tvAppName.setText("Draw new pattern.");
        pvLock.clearPattern();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).activityInfo.applicationInfo.packageName.equals(Share.msCurrentLockedPackege)) {
                ivAppIcon.setImageDrawable(list.get(i).activityInfo.applicationInfo.loadIcon(pm));
                tvAppName.setText(list.get(i).activityInfo.applicationInfo.loadLabel(pm).toString());
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    public static int dpToPx(Activity activity, int dp) {
        Resources r = activity.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void turnOnBluetooth(boolean v) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (v) {
            adapter.enable();
        } else {
            adapter.disable();
        }
    }

    public void setMobileDataState(boolean mobileDataEnabled) {
        try {
            TelephonyManager telephonyService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);

            if (null != setMobileDataEnabledMethod) {
                setMobileDataEnabledMethod.invoke(telephonyService, mobileDataEnabled);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error setting mobile data state", ex);
        }
    }

    private void validatePattern(String pattern) {

        if (!pattern.equals(Preferences.getStringPref(PatternActivity.this, Preferences.KEY_SAVED_PATTERN))) {
            pvLock.setViewMode(PatternLockView.PatternViewMode.WRONG);
            new Handler().postDelayed(() -> pvLock.clearPattern(), 500);
        } else {
            pvLock.setViewMode(PatternLockView.PatternViewMode.CORRECT);
            Preferences.setStringPref(PatternActivity.this, Preferences.KEY_TOP, getIntent().getStringExtra("package"));
            Preferences.setBooleanPref(PatternActivity.this, Preferences.KEY_LOCKED, false);
            if (Share.msCurrentLockedPackege.equals("wifi_disable")) {
                Preferences.setBooleanPref(PatternActivity.this, Preferences.KEY_ENABLED_FROM_PATTERN, false);
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(false);
            }
            if (Share.msCurrentLockedPackege.equals("wifi_enable")) {
                Preferences.setBooleanPref(PatternActivity.this, Preferences.KEY_DISABLED_FROM_PATTERN, false);
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);
            }

            if (Share.msCurrentLockedPackege.equals("data_disable")) {
                Preferences.setBooleanPref(PatternActivity.this, Preferences.KEY_DATA_ENABLED_FROM_PATTERN, false);
                setMobileDataState(false);
            }
            if (Share.msCurrentLockedPackege.equals("data_enable")) {
                Preferences.setBooleanPref(PatternActivity.this, Preferences.KEY_DATA_DISABLED_FROM_PATTERN, false);
                setMobileDataState(true);
            }

            if (Share.msCurrentLockedPackege.equals("bluetooth_disable")) {
                Preferences.setBooleanPref(PatternActivity.this, Preferences.KEY_BLUETOOTH_ENABLED_FROM_PATTERN, false);
                turnOnBluetooth(false);
            }
            if (Share.msCurrentLockedPackege.equals("bluetooth_enable")) {
                Preferences.setBooleanPref(PatternActivity.this, Preferences.KEY_BLUETOOTH_DISABLED_FROM_PATTERN, false);
                turnOnBluetooth(true);
            }

            new Handler().postDelayed(() -> {
                PatternActivity.this.finishAffinity();
                PatternActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }, 1000);
        }

    }


    private void checkForFingerPrint() {

        //Finger Print.....
        // Initializing both Android Keyguard Manager and Fingerprint Manager
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!fingerprintManager.isHardwareDetected()) {
                /**
                 * An error message will be displayed if the device does not contain the fingerprint hardware.
                 * However if you plan to implement a default authentication method,
                 * you can redirect the user to a default authentication activity from here.
                 * Example:
                 * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
                 * startActivity(intent);
                 */
                findViewById(R.id.pattern_iv_finger).setVisibility(View.INVISIBLE);
                Log.e("FINGER_PRINT", "Your Device does not have a Fingerprint Sensor");
                SharedPrefs.savePref(PatternActivity.this, SharedPrefs.isFingerLockOn, false);
            } else {
                // Checks whether fingerprint permission is set on manifest
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                    findViewById(R.id.pattern_iv_finger).setVisibility(View.INVISIBLE);
                    Log.e("FINGER_PRINT", "Fingerprint authentication permission not enabled");
                    SharedPrefs.savePref(PatternActivity.this, SharedPrefs.isFingerLockOn, false);
                } else {
                    // Check whether at least one fingerprint is registered
                    if (!fingerprintManager.hasEnrolledFingerprints()) {
                        findViewById(R.id.pattern_iv_finger).setVisibility(View.INVISIBLE);
                        Log.e("FINGER_PRINT", "Register at least one fingerprint in Settings");
                        SharedPrefs.savePref(PatternActivity.this, SharedPrefs.isFingerLockOn, false);
                    } else {
                        // Checks whether lock screen security is enabled or not
                        if (!keyguardManager.isKeyguardSecure()) {
                            findViewById(R.id.pattern_iv_finger).setVisibility(View.INVISIBLE);
                            Log.e("FINGER_PRINT", "Lock screen security not enabled in Settings");
                            SharedPrefs.savePref(PatternActivity.this, SharedPrefs.isFingerLockOn, false);
                        } else {
                            generateKey();
                            if (cipherInit()) {
                                FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                helper = new FingerprintHandler(this, true);

                                helper.startAuth(fingerprintManager, cryptoObject);
                            }
                        }
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
