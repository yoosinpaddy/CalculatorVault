package com.calculator.vault.gallery.locker.hide.data.appLock;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.common.FingerprintHandler;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.data.ImageVideoDatabase;
import com.calculator.vault.gallery.locker.hide.data.model.UserModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
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

public class PinActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout clMain;
    EditText etCode;
    ImageView ivAppIcon, ivPOne, ivPTwo, ivPThree, ivPFour, ivDelete;
    TextView tvAppName;
    ImageView tvOne, tvTwo, tvThree, tvFour, tvFive, tvSix, tvSeven, tvEight, tvNine, tvZero;
    private int pDotRes, pDashRes;
    //private KeyguardManager.KeyguardLock mLock;
    ImageVideoDatabase moDBimageVideoDatabase = new ImageVideoDatabase(this);
    private ArrayList<ThemeModel> themeModels = new ArrayList<>();

    private KeyStore keyStore;
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "com.calculatorvault.backup";
    private Cipher cipher;
    private List<String> listPermissionsNeeded = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_pin);

        clMain = findViewById(R.id.pin_cl_main);

        ivAppIcon = findViewById(R.id.pin_iv_appIcon);
        tvAppName = findViewById(R.id.pin_tv_appName);

        PackageManager pm = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).activityInfo.applicationInfo.packageName.equals(Share.msCurrentLockedPackege)){
                ivAppIcon.setImageDrawable(list.get(i).activityInfo.applicationInfo.loadIcon(pm));
                tvAppName.setText(list.get(i).activityInfo.applicationInfo.loadLabel(pm).toString());
                Log.e("TOPAPPNAME", "onCreate: " + list.get(i).activityInfo.applicationInfo.loadLabel(pm).toString());
                break;
            }
        }

        etCode = findViewById(R.id.pin_edt_code);

        ivPOne = findViewById(R.id.pin_iv_pOne);
        ivPTwo = findViewById(R.id.pin_iv_pTwo);
        ivPThree = findViewById(R.id.pin_iv_pThree);
        ivPFour = findViewById(R.id.pin_iv_pFour);
        ivDelete = findViewById(R.id.pin_iv_delete);

        tvOne = findViewById(R.id.pin_tv_one);
        tvTwo = findViewById(R.id.pin_tv_two);
        tvThree = findViewById(R.id.pin_tv_three);
        tvFour = findViewById(R.id.pin_tv_four);
        tvFive = findViewById(R.id.pin_tv_five);
        tvSix = findViewById(R.id.pin_tv_six);
        tvSeven = findViewById(R.id.pin_tv_seven);
        tvEight = findViewById(R.id.pin_tv_eight);
        tvNine = findViewById(R.id.pin_tv_nine);
        tvZero = findViewById(R.id.pin_tv_zero);

        String savedList = Preferences.getStringPref(PinActivity.this, Preferences.KEY_SAVED_THEME_LIST_PIN);
        themeModels = new Gson().fromJson(savedList, new TypeToken<ArrayList<ThemeModel>>(){}.getType());

        if (themeModels!=null && !themeModels.isEmpty()){
            for (int i = 0; i < themeModels.size(); i++) {

                if (themeModels.get(i).isSelected()){

                    clMain.setBackgroundResource(themeModels.get(i).getThemeBgRes());
                    pDotRes = themeModels.get(i).getDotRes();
                    pDashRes = themeModels.get(i).getDashRes();

                    tvOne.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, themeModels.get(i).getOneRes()));

                    tvTwo.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, themeModels.get(i).getTwoRes()));

                    tvThree.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, themeModels.get(i).getThreeRes()));

                    tvFour.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, themeModels.get(i).getFourRes()));

                    tvFive.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, themeModels.get(i).getFiveRes()));

                    tvSix.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, themeModels.get(i).getSixRes()));

                    tvSeven.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, themeModels.get(i).getSevenRes()));

                    tvEight.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, themeModels.get(i).getEightRes()));

                    tvNine.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, themeModels.get(i).getNineRes()));

                    tvZero.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, themeModels.get(i).getZeroRes()));

                    ivDelete.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, themeModels.get(i).getDelRes()));
                    ivDelete.setPadding(0,0,0,0);
                }

            }

        }




        //WIFI.......
        if (Share.msCurrentLockedPackege.equals("wifi_enable")){
            ivAppIcon.setImageResource(R.drawable.ic_wifi);
            tvAppName.setText("WiFi");
            Preferences.setBooleanPref(PinActivity.this, Preferences.KEY_DISABLED_FROM_PATTERN, true);
            Preferences.setBooleanPref(PinActivity.this, Preferences.KEY_ENABLED_FROM_PATTERN, false);
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            new Handler().postDelayed(() -> {
                wifiManager.setWifiEnabled(false);
            },100);
        }

        if (Share.msCurrentLockedPackege.equals("wifi_disable")){
            ivAppIcon.setImageResource(R.drawable.ic_wifi);
            tvAppName.setText("WiFi");
            Preferences.setBooleanPref(PinActivity.this, Preferences.KEY_ENABLED_FROM_PATTERN, true);
            Preferences.setBooleanPref(PinActivity.this, Preferences.KEY_DISABLED_FROM_PATTERN, false);
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            new Handler().postDelayed(() -> {
                wifiManager.setWifiEnabled(true);
            },100);

        }

        //BLUETOOTH.......
        if (Share.msCurrentLockedPackege.equals("bluetooth_disable")){
            ivAppIcon.setImageResource(R.drawable.ic_bluetooth);
            tvAppName.setText("Bluetooth");
            Preferences.setBooleanPref(PinActivity.this, Preferences.KEY_BLUETOOTH_ENABLED_FROM_PATTERN, true);
            Preferences.setBooleanPref(PinActivity.this, Preferences.KEY_BLUETOOTH_DISABLED_FROM_PATTERN, false);

            new Handler().postDelayed(() -> {
                turnOnBluetooth(true);
            },100);
        }

        if (Share.msCurrentLockedPackege.equals("bluetooth_enable")){
            ivAppIcon.setImageResource(R.drawable.ic_bluetooth);
            tvAppName.setText("Bluetooth");
            Preferences.setBooleanPref(PinActivity.this, Preferences.KEY_BLUETOOTH_DISABLED_FROM_PATTERN, true);
            Preferences.setBooleanPref(PinActivity.this, Preferences.KEY_BLUETOOTH_ENABLED_FROM_PATTERN, false);
            new Handler().postDelayed(() -> {
                turnOnBluetooth(false);
            },100);
        }

        ivPOne.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDashRes));
        ivPTwo.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDashRes));
        ivPThree.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDashRes));
        ivPFour.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDashRes));

        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("TAG", "afterTextChanged: "+ s.toString().length());

                switch (s.toString().trim().length()){
                    case 0:
                        ivPOne.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDashRes));
                        ivPTwo.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDashRes));
                        ivPThree.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDashRes));
                        ivPFour.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDashRes));
                        break;

                    case 1:
                        ivPOne.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDotRes));
                        ivPTwo.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDashRes));
                        ivPThree.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDashRes));
                        ivPFour.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDashRes));
                        break;

                    case 2:
                        ivPOne.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDotRes));
                        ivPTwo.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDotRes));
                        ivPThree.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDashRes));
                        ivPFour.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDashRes));
                        break;

                    case 3:
                        ivPOne.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDotRes));
                        ivPTwo.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDotRes));
                        ivPThree.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDotRes));
                        ivPFour.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDashRes));
                        break;

                    case 4:
                        ivPOne.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDotRes));
                        ivPTwo.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDotRes));
                        ivPThree.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDotRes));
                        ivPFour.setImageDrawable(ActivityCompat.getDrawable(PinActivity.this, pDotRes));
                        break;
                }

                if (s.toString().trim().length() == 4){
                    if (checkAndRequestPermissions()){
                        validate(s.toString().trim());
                    }else {
                        ActivityCompat.requestPermissions(PinActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 159);
                    }
                }

            }
        });

//        button = findViewById(R.id.button);
//        button.setOnClickListener(v -> {
//
//            String pin = editText.getText().toString().trim();
//
//            if (TextUtils.isEmpty(pin)){
//                Toast.makeText(this, "Can not be empty!", Toast.LENGTH_LONG).show();
//            }else if (pin.length() != 4){
//                Toast.makeText(this, "Must be 4 character long!", Toast.LENGTH_LONG).show();
//            }else if (!pin.equals("1234")){
//                Toast.makeText(this, "Pin is wrong!", Toast.LENGTH_LONG).show();
//            }else {
//                Preferences.setStringPref(PinActivity.this, Preferences.KEY_TOP, getIntent().getStringExtra("package"));
//                Preferences.setBooleanPref(PinActivity.this, Preferences.KEY_LOCKED, false);
//                PinActivity.this.finishAffinity();
//            }
//
//        });
        tvOne.setOnClickListener(this);
        tvTwo.setOnClickListener(this);
        tvThree.setOnClickListener(this);
        tvFour.setOnClickListener(this);
        tvFive.setOnClickListener(this);
        tvSix.setOnClickListener(this);
        tvSeven.setOnClickListener(this);
        tvEight.setOnClickListener(this);
        tvNine.setOnClickListener(this);
        tvZero.setOnClickListener(this);
        ivDelete.setOnClickListener(this);

        if (SharedPrefs.getBoolean(PinActivity.this, SharedPrefs.isFingerLockOn)) {
            checkForFingerPrint();
        }else {
            findViewById(R.id.pin_iv_finger).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TOPAPPNAME", "onResume: ");

        PackageManager pm = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).activityInfo.applicationInfo.packageName.equals(Share.msCurrentLockedPackege)){
                ivAppIcon.setImageDrawable(list.get(i).activityInfo.applicationInfo.loadIcon(pm));
                tvAppName.setText(list.get(i).activityInfo.applicationInfo.loadLabel(pm).toString());
                Log.e("TOPAPPNAME", "onCreate: " + list.get(i).activityInfo.applicationInfo.loadLabel(pm).toString());
                break;
            }
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
                findViewById(R.id.pin_iv_finger).setVisibility(View.INVISIBLE);
                Log.e("FINGER_PRINT", "Your Device does not have a Fingerprint Sensor");
                SharedPrefs.savePref(PinActivity.this, SharedPrefs.isFingerLockOn, false);
            } else {
                // Checks whether fingerprint permission is set on manifest
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                    findViewById(R.id.pin_iv_finger).setVisibility(View.INVISIBLE);
                    Log.e("FINGER_PRINT", "Fingerprint authentication permission not enabled");
                    SharedPrefs.savePref(PinActivity.this, SharedPrefs.isFingerLockOn, false);
                } else {
                    // Check whether at least one fingerprint is registered
                    if (!fingerprintManager.hasEnrolledFingerprints()) {
                        findViewById(R.id.pin_iv_finger).setVisibility(View.INVISIBLE);
                        Log.e("FINGER_PRINT", "Register at least one fingerprint in Settings");
                        SharedPrefs.savePref(PinActivity.this, SharedPrefs.isFingerLockOn, false);
                    } else {
                        // Checks whether lock screen security is enabled or not
                        if (!keyguardManager.isKeyguardSecure()) {
                            findViewById(R.id.pin_iv_finger).setVisibility(View.INVISIBLE);
                            Log.e("FINGER_PRINT", "Lock screen security not enabled in Settings");
                            SharedPrefs.savePref(PinActivity.this, SharedPrefs.isFingerLockOn, false);
                        } else {
                            generateKey();
                            if (cipherInit()) {
                                FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                FingerprintHandler helper = new FingerprintHandler(this, true);
                                helper.startAuth(fingerprintManager, cryptoObject);
                            }
                        }
                    }
                }
            }
        }
    }

    private void validate(String s) {

        UserModel loUserModel = moDBimageVideoDatabase.getSingleUserData(1);

        if (!s.equals(loUserModel.getPassword())){

            Animation rotation = AnimationUtils.loadAnimation(this, R.anim.shake_anim_pin);
            rotation.setRepeatCount(Animation.ABSOLUTE);
            ivPOne.startAnimation(rotation);
            ivPTwo.startAnimation(rotation);
            ivPThree.startAnimation(rotation);
            ivPFour.startAnimation(rotation);
            new Handler().postDelayed(() -> {
                    etCode.getText().clear();
                }, 1000);

        } else {
            Preferences.setStringPref(PinActivity.this, Preferences.KEY_TOP, getIntent().getStringExtra("package"));
            Preferences.setBooleanPref(PinActivity.this, Preferences.KEY_LOCKED, false);

            if (Share.msCurrentLockedPackege.equals("wifi_disable")){
                Preferences.setBooleanPref(PinActivity.this, Preferences.KEY_ENABLED_FROM_PATTERN, false);
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(false);
            }
            if (Share.msCurrentLockedPackege.equals("wifi_enable")){
                Preferences.setBooleanPref(PinActivity.this, Preferences.KEY_DISABLED_FROM_PATTERN, false);
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);
            }

            if (Share.msCurrentLockedPackege.equals("bluetooth_disable")){
                Preferences.setBooleanPref(PinActivity.this, Preferences.KEY_BLUETOOTH_ENABLED_FROM_PATTERN, false);
                turnOnBluetooth(false);
            }
            if (Share.msCurrentLockedPackege.equals("bluetooth_enable")){
                Preferences.setBooleanPref(PinActivity.this, Preferences.KEY_BLUETOOTH_DISABLED_FROM_PATTERN, false);
                turnOnBluetooth(true);
            }

            try {
                new Handler().postDelayed(() -> {
                    PinActivity.this.finishAffinity();
                    PinActivity.this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                },1000);
            } catch (Exception e) {
                Log.e("TAG", "validate: "+e.toString() );
            }

        }

    }

    private void turnOnBluetooth(boolean v) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (v){
            adapter.enable();
        }else {
            adapter.disable();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.pin_tv_one:
                enterText("1");
                break;

            case R.id.pin_tv_two:
                enterText("2");
                break;

            case R.id.pin_tv_three:
                enterText("3");
                break;

            case R.id.pin_tv_four:
                enterText("4");
                break;

            case R.id.pin_tv_five:
                enterText("5");
                break;

            case R.id.pin_tv_six:
                enterText("6");
                break;

            case R.id.pin_tv_seven:
                enterText("7");
                break;

            case R.id.pin_tv_eight:
                enterText("8");
                break;

            case R.id.pin_tv_nine:
                enterText("9");
                break;

            case R.id.pin_tv_zero:
                enterText("0");
                break;

            case R.id.pin_iv_delete:
                if (etCode.getText().toString().trim().length() > 0) {
                    enterText("del");
                }
                break;

        }

    }

    private void enterText(String s) {
        if (s.equals("del")){
            String prev = etCode.getText().toString().trim();
            String after = prev.substring(0,(prev.length() - 1));
            etCode.setText(after);
        }else {
            String prev = etCode.getText().toString().trim();
            etCode.setText(prev+s);
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
        Intent homeIntent= new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(homeIntent);
        super.onBackPressed();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkAndRequestPermissions() {
        listPermissionsNeeded.clear();

        int audio = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (audio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (write != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        Log.e("TAG", "listPermissionsNeeded===>" + listPermissionsNeeded);
        return listPermissionsNeeded.isEmpty();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (permissions.length == 0) {
                return;
            }
            boolean allPermissionsGranted = true;
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false;
                        break;
                    }
                }
            }
            if (!allPermissionsGranted) {
                boolean somePermissionsForeverDenied = false;
                for (String permission : permissions) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        //denied
                        if (requestCode == 2) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 2);
                            break;
                        }
                    } else {
                        if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                            Log.e("allowed", permission);
                        } else {
                            //set to never ask again
                            Log.e("set to never ask again", permission);
                            somePermissionsForeverDenied = true;
                        }
                    }
                }
                if (somePermissionsForeverDenied) {

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Permissions Required");
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        alertDialogBuilder.setMessage("Please allow permission for " + "Storage");
                    } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        alertDialogBuilder.setMessage("Please allow permission for " + "Camera");
                    }
                    alertDialogBuilder.setPositiveButton("Cancel", (dialog, which) -> {
                    }).setNegativeButton("Ok", (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }).setCancelable(false).create().show();
                }
            }
        }

        if (requestCode == 159) {
            ArrayList<Boolean> perm = new ArrayList<>();
            ArrayList<Boolean> perm1 = new ArrayList<>();
            boolean storageForceDeny = false;
            boolean recordForceDeny = false;
            for (int grantResult : grantResults) {
                perm.add(grantResult == PackageManager.PERMISSION_GRANTED);
            }
            System.out.println("==========permissiopn--->>>>>>> " + perm);
            if (!perm.contains(false)) {
                // accecpt
            } else {

                for (int j = 0; j < listPermissionsNeeded.size(); j++) {
                    perm1.add(shouldShowRequestPermissionRationale(listPermissionsNeeded.get(j)));
                }
                if (perm1.contains(true)) {
                    Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        System.out.println("=======" + perm.get(1) + "....." + perm1.get(1));
                        System.out.println("=======" + perm.get(0) + "....." + perm1.get(0));

                        if (perm.get(1) == perm1.get(1)) {
                            storageForceDeny = true;
                        }
                        if (perm.get(0) == perm1.get(0)) {
                            recordForceDeny = true;
                        }
                    } catch (Exception e) {
                        if (listPermissionsNeeded.get(0).equals("android.permission.WRITE_EXTERNAL_STORAGE")) {
                            storageForceDeny = true;
                        } else if (listPermissionsNeeded.get(0).equals("android.permission.CAMERA")) {
                            recordForceDeny = true;
                        }
                    }
                    dontAskAgain(storageForceDeny, recordForceDeny);
                }
            }
        }
    }

    private void dontAskAgain(boolean storageForceDeny, boolean recordForceDeny) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PinActivity.this);
        builder.setTitle("Permissions Required");

        if (storageForceDeny && recordForceDeny) {
            builder.setMessage("Please allow permission for Camera and Storage.");
        } else if (storageForceDeny) {
            builder.setMessage("Please allow permission for Storage.");
        } else if (recordForceDeny) {
            builder.setMessage("Please allow permission for Camera.");
        }

        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(intent);
        });
        builder.setNegativeButton("Cancle", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }
}
