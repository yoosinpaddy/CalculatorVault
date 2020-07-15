package com.calculator.vault.gallery.locker.hide.data.activity;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.ads.AdsListener;
import com.calculator.vault.gallery.locker.hide.data.ads.IntAdsHelper;
import com.calculator.vault.gallery.locker.hide.data.ads.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.appLock.PatternActivity;
import com.calculator.vault.gallery.locker.hide.data.appLock.Preferences;
import com.calculator.vault.gallery.locker.hide.data.appLock.ThemeListActivity;
import com.calculator.vault.gallery.locker.hide.data.common.OnSingleClickListener;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.common.Utils;
import com.google.android.gms.ads.AdView;
import com.greedygame.android.agent.GreedyGameAgent;
import com.greedygame.android.core.campaign.CampaignStateListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import java.io.File;

public class ChangePasscodeActivity extends AppCompatActivity implements View.OnClickListener
        /*MoPubInterstitial.InterstitialAdListener*//*, CampaignStateListener/*InterstitialAdHelper.onInterstitialAdListener*/ {

    private static final String TAG = "ChangePasscodeActivity";
    private LinearLayout moLlChangePasscode, moLlChangePattern, moLlLockTheme;
    private ImageView iv_back;
    private AdView adView;
    private SwitchCompat moSwFingerLock, moSwPatternLock;
    private KeyguardManager keyguardManager;
    private FingerprintManager fingerprintManager;
    private ImageView moIvMoreApp, moIvBlast;

//    private boolean isInterstitialAdLoaded = false;
//    private MoPubInterstitial mInterstitial;
    // TODO Admob Ads
//    private InterstitialAd interstitial;

    private GreedyGameAgent greedyGame;

//    @Override
//    public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {
//        if (Share.isNeedToAdShow(ChangePasscodeActivity.this)) {
//            isInterstitialAdLoaded = true;
//            moIvBlast.setVisibility(View.GONE);
//            moIvMoreApp.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {
//        Log.e(TAG, "onInterstitialFailed: "+ moPubErrorCode);
//        isInterstitialAdLoaded = false;
//        if (Utils.isNetworkConnected(ChangePasscodeActivity.this)) {
//            mInterstitial.load();
//        }
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onInterstitialShown(MoPubInterstitial moPubInterstitial) {
//
//    }
//
//    @Override
//    public void onInterstitialClicked(MoPubInterstitial moPubInterstitial) {
//
//    }
//
//    @Override
//    public void onInterstitialDismissed(MoPubInterstitial moPubInterstitial) {
//        isInterstitialAdLoaded = false;
//        if (Utils.isNetworkConnected(ChangePasscodeActivity.this)) {
//            mInterstitial.load();
//        }
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//    }
//      TODO Admob Ads
//    @Override
//    public void onLoad() {
//        isInterstitialAdLoaded = true;
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void onFailed() {
//        isInterstitialAdLoaded = false;
//        if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id1))){
//            interstitial = InterstitialAdHelper.getInstance().load2(ChangePasscodeActivity.this, this);
//        }else if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id2))){
//            interstitial = InterstitialAdHelper.getInstance().load(ChangePasscodeActivity.this, this);
//        }else {
//            interstitial = InterstitialAdHelper.getInstance().load1(ChangePasscodeActivity.this, this);
//        }
//
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onClosed() {
//        isInterstitialAdLoaded = false;
//        interstitial = InterstitialAdHelper.getInstance().load1(ChangePasscodeActivity.this, this);
//
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NativeAdvanceHelper.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passcode);

//        mInterstitial = new MoPubInterstitial(this, getString(R.string.mopub_int_id));
//        mInterstitial.setInterstitialAdListener(this);
//        if (Utils.isNetworkConnected(ChangePasscodeActivity.this)) {
//            mInterstitial.load();
            IntAdsHelper.loadInterstitialAds(this, new AdsListener() {
                @Override
                public void onLoad() {
                    moIvBlast.setVisibility(View.GONE);
//                    moIvMoreApp.setVisibility(View.VISIBLE);
                }

                @Override
                public void onClosed() {
                    moIvBlast.setVisibility(View.GONE);
                    moIvMoreApp.setVisibility(View.GONE);
                }
            });
//        }

//        TODO Admob Ads
//        interstitial = InterstitialAdHelper.getInstance().load1(ChangePasscodeActivity.this, this);

        initView();
        initViewListener();
//        adView = (AdView) findViewById(R.id.adView);
//        Share.loadAdsBanner(ChangePasscodeActivity.this,adView);

        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        } else {
            findViewById(R.id.ll_finger_lock).setVisibility(View.GONE);
//            findViewById(R.id.view_finger_lock).setVisibility(View.GONE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (fingerprintManager != null) {
                if (!fingerprintManager.isHardwareDetected()) {
                    /**
                     * An error message will be displayed if the device does not contain the fingerprint hardware.
                     * However if you plan to implement a default authentication method,
                     * you can redirect the user to a default authentication activity from here.
                     * Example:
                     * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
                     * startActivity(intent);
                     */
                    findViewById(R.id.ll_finger_lock).setVisibility(View.GONE);
//                    findViewById(R.id.view_finger_lock).setVisibility(View.GONE);
                    Log.e("FINGER_PRINT", "Your Device does not have a Fingerprint Sensor");
                }
            } else {
                findViewById(R.id.ll_finger_lock).setVisibility(View.GONE);
//                findViewById(R.id.view_finger_lock).setVisibility(View.GONE);
            }
        }

        if (SharedPrefs.getBoolean(ChangePasscodeActivity.this, SharedPrefs.isFingerLockOn)) {
            moSwFingerLock.setChecked(true);
        } else {
            moSwFingerLock.setChecked(false);
        }

        if (Share.isNeedToAdShow(ChangePasscodeActivity.this)) {
            NativeAdvanceHelper.loadNativeAd(this, findViewById(R.id.fl_adplaceholder), true);
        }

//        greedyGame = new GreedyGameAgent.Builder(this)
//                .enableAdmob(true) // To enable Admob
//                .setGameId("84360092")
//                .withAgentListener(this) // To get Campaign State callbacks
//                .addUnitId("unit-4257") // UNIT ID will be generated in next step
//                .addUnitId("float-4028") // Adding Clickable and Non-Clickable units to be shown inside the game.
//                .addUnitId("float-4029") // Adding Clickable and Non-Clickable units to be shown inside the game.
//                .build();
//        greedyGame.init();


    }

    @Override
    protected void onResume() {
        super.onResume();
//        greedyGame.startEventRefresh();
    }

//    @Override
//    public void onUnavailable() {
//        Log.e(TAG, "onUnavailable: ");
//    }
//
//    @Override
//    public void onAvailable(String s) {
//        Log.e(TAG, "onAvailable: " + s);
//
//        String path = greedyGame.getPath("float-4029");
//        if(path != null) {
//            File file = new File(path);
//            if (file.exists()) {
//                Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
//                if (bm != null) {
//                    //Use it on your element which needs to be branded.
//                    ImageView imageView = findViewById(R.id.greedyTestClickable);
//                    imageView.setImageBitmap(bm);
//                    imageView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            greedyGame.showUII("float-4029");
//                        }
//                    });
//                }
//            }
//        }
//
//    }
//
//    @Override
//    public void onError(String s) {
//        Log.e(TAG, "onError: " + s);
//    }

    private void initViewListener() {
        moLlChangePasscode.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        moLlLockTheme.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent loIntent = new Intent(ChangePasscodeActivity.this, ThemeListActivity.class);
                if (Preferences.getBooleanPref(ChangePasscodeActivity.this,Preferences.KEY_IS_PATTERN_LOCK_ENABLED)) {
                    loIntent.putExtra("forWhat", "pattern");
                }else {
                    loIntent.putExtra("forWhat", "pin");
                }
                startActivity(loIntent);
            }
        });

        moLlChangePattern.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent loIntent = new Intent(ChangePasscodeActivity.this, PatternActivity.class);
                loIntent.putExtra("fromWhere", "change_pattern");
                startActivityForResult(loIntent, 321);
            }
        });

        moIvMoreApp.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (Share.isNeedToAdShow(ChangePasscodeActivity.this)) {
                    if (IntAdsHelper.isInterstitialLoaded()) {
                        moIvMoreApp.setVisibility(View.GONE);
                        moIvBlast.setVisibility(View.VISIBLE);
                        ((AnimationDrawable) moIvBlast.getBackground()).start();
                        IntAdsHelper.showInterstitialAd();
//                  TODO Admob Ads
//                  interstitial.show();
                    }
                }
            }
        });

        moSwPatternLock.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                Intent loIntent = new Intent(ChangePasscodeActivity.this, PatternActivity.class);
                loIntent.putExtra("fromWhere", "new_pattern");
                startActivityForResult(loIntent, 123);
            } else {
                moLlChangePattern.setVisibility(View.GONE);
//                findViewById(R.id.view_change_pattern).setVisibility(View.GONE);
                Preferences.setBooleanPref(ChangePasscodeActivity.this, Preferences.KEY_IS_PATTERN_LOCK_ENABLED, false);
                Preferences.setStringPref(ChangePasscodeActivity.this, Preferences.KEY_SAVED_PATTERN,"");
            }

        });

        moSwFingerLock.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (fingerprintManager.isHardwareDetected()) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                            Log.e("FINGER_PRINT", "Fingerprint authentication permission not enabled");
                            Toast.makeText(this, "Fingerprint authentication permission not enabled", Toast.LENGTH_LONG).show();
                            moSwFingerLock.setChecked(false);
                        } else {
                            // Check whether at least one fingerprint is registered
                            if (!fingerprintManager.hasEnrolledFingerprints()) {
                                Log.e("FINGER_PRINT", "Register at least one fingerprint in Settings");
                                Toast.makeText(this, "Register at least one fingerprint in Settings", Toast.LENGTH_LONG).show();
                                moSwFingerLock.setChecked(false);
                            } else {
                                // Checks whether lock screen security is enabled or not
                                if (!keyguardManager.isKeyguardSecure()) {
                                    Log.e("FINGER_PRINT", "Lock screen security not enabled in Settings");
                                    Toast.makeText(this, "Lock screen security not enabled in Settings", Toast.LENGTH_LONG).show();
                                    moSwFingerLock.setChecked(false);
                                } else {
                                    SharedPrefs.savePref(ChangePasscodeActivity.this, SharedPrefs.isFingerLockOn, true);
                                    Log.e("FingerLock", " is ON");
                                }
                            }
                        }
                    }
                }
            } else {
                SharedPrefs.savePref(ChangePasscodeActivity.this, SharedPrefs.isFingerLockOn, false);
                Log.e("FingerLock", " is OFF");
            }
        });
    }

    private void initView() {
        moLlChangePasscode = findViewById(R.id.ll_changePasscode);
        iv_back = findViewById(R.id.iv_back);
        moSwFingerLock = findViewById(R.id.sw_finger_lock);
        moSwPatternLock = findViewById(R.id.sw_pattern_lock);
        moLlChangePattern = findViewById(R.id.ll_change_pattern);
        moLlLockTheme = findViewById(R.id.ll_lock_theme);

        if (Preferences.getBooleanPref(ChangePasscodeActivity.this, Preferences.KEY_IS_PATTERN_LOCK_ENABLED)) {
            moSwPatternLock.setChecked(true);
            moLlChangePattern.setVisibility(View.VISIBLE);
//            findViewById(R.id.view_change_pattern).setVisibility(View.VISIBLE);
        } else {
            moSwPatternLock.setChecked(false);
            moLlChangePattern.setVisibility(View.GONE);
//            findViewById(R.id.view_change_pattern).setVisibility(View.GONE);
        }

        // Gift Ads
        moIvMoreApp = findViewById(R.id.iv_more_app);
        moIvBlast = findViewById(R.id.iv_blast);
        moIvMoreApp.setVisibility(View.GONE);
        moIvMoreApp.setBackgroundResource(R.drawable.animation_list_filling);
        ((AnimationDrawable) moIvMoreApp.getBackground()).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_changePasscode:
                Share.ChangePassword = true;
                Intent intentpass = new Intent(ChangePasscodeActivity.this, CalculatorActivity.class);
                intentpass.putExtra("isFrom","app");
                startActivity(intentpass);
                break;

            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (resultCode == RESULT_CANCELED) {
                moSwPatternLock.setChecked(false);
                moLlChangePattern.setVisibility(View.GONE);
//                findViewById(R.id.view_change_pattern).setVisibility(View.GONE);
            }else {
                moLlChangePattern.setVisibility(View.VISIBLE);
//                findViewById(R.id.view_change_pattern).setVisibility(View.VISIBLE);
            }
        }
    }
}
