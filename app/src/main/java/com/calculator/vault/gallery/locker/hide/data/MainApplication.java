package com.calculator.vault.gallery.locker.hide.data;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;

import androidx.multidex.MultiDexApplication;

import com.appnext.base.Appnext;
import com.calculator.vault.gallery.locker.hide.data.appLock.Accessibilty;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.common.Utils;
import com.calculator.vault.gallery.locker.hide.data.googleDrive.DownloadService;
import com.calculator.vault.gallery.locker.hide.data.googleDrive.UploadService;
import com.crashlytics.android.Crashlytics;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.inmobi.sdk.InMobiSdk;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.logging.MoPubLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;

import static com.mopub.common.logging.MoPubLog.LogLevel.DEBUG;
import static com.mopub.common.logging.MoPubLog.LogLevel.INFO;

/*
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
*/

/**
 * Created by Harshad Kathiriya on 11/12/2016.
 */

public class MainApplication extends MultiDexApplication {

    private static MainApplication singleton;
    public AdRequest ins_adRequest;
    public InterstitialAd mInterstitialAd;
    public static int currentVolume;
    private FirebaseAnalytics mFirebaseAnalytics;

  /*  public static void setMediaVolume(int vol) {
        vol=  vol*3;
        AudioManager am = (AudioManager) singleton.getSystemService(Context.AUDIO_SERVICE);                     //MimeTypes.BASE_TYPE_AUDIO);
        am.setStreamVolume(3, (int) (((float) am.getStreamMaxVolume(3)) * (((float) vol) / 100.0f)), 0);
    }

    public static void saveCurrentVolume() {
        currentVolume = ((AudioManager) singleton.getSystemService(Context.AUDIO_SERVICE)).getStreamVolume(3);
    }*/

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName(this);
            String packageName = this.getPackageName();
            if (!packageName.equals(processName)) {
                WebView.setDataDirectorySuffix(processName);
            }
        }

        Fabric.with(this, new Crashlytics());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        MobileAds.initialize(this);

        if (!Utils.isServiceRunning(this, UploadService.class)
                && !Utils.isServiceRunning(this, DownloadService.class)
                && !Utils.isServiceRunning(this, Accessibilty.class)) {
            MobileAds.initialize(this);
        }

        Appnext.init(this);

        AudienceNetworkAds.isInAdsProcess(this);

        //Initialize Inmobi SDK before any API call.
        InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG);
        JSONObject consent = new JSONObject();
        try {
            // Provide correct consent value to sdk which is obtained by User
            consent.put(InMobiSdk.IM_GDPR_CONSENT_AVAILABLE, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        InMobiSdk.init(this, "31284d7f88914200a7d0859d43efa042", consent);

//        TODO Mopub Ads Init
        final SdkConfiguration.Builder configBuilder = new SdkConfiguration.Builder(getString(R.string.mopub_int_id));
        configBuilder.withLogLevel(MoPubLog.LogLevel.DEBUG);
        configBuilder.withLegitimateInterestAllowed(false);
        if (BuildConfig.DEBUG) {
            configBuilder.withLogLevel(DEBUG);
        } else {
            configBuilder.withLogLevel(INFO);
        }

        MoPub.initializeSdk(this, configBuilder.build(), () -> {

        });

//        if (!getProcessName().equals("YOUR_SECOND_PROCESS_NAME")) {
//            MobileAds.initialize(this, getString(R.string.app_id));
//        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                WebView.setDataDirectorySuffix("admobWebview");
//            }
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            WebView.setDataDirectorySuffix("admobWebview");
//        }

        try {

            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

            //TODO URI ISSUE
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder()
                            .detectDiskReads()
                            .detectDiskWrites()
                            .detectNetwork()
                            .penaltyLog()
                            .build());
            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder()
                            .detectLeakedSqlLiteObjects()
                            .penaltyLog()
                            .build());

            //LoadAds();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void LoadAds() {

        try {
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getApplicationContext().getResources().getString(R.string.inter_ad_unit_id));

            ins_adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("E19949FB5E7C5A28C30A875934AC8181") //SWIPE
                    .addTestDevice("41E9C9F5D1F985FB36C9760EFC8F3916") //Lenovo
                    .addTestDevice("64A3A22A05D9DCDBEC68395FF5048CD1")  //Coolpad
                    .addTestDevice("51A49E7B1B359D1999E5C85CE4F54978") //XOLO
                    .addTestDevice("F9EBC1840023CB004A83005514278635") //MI 6otu (No SIM)
                    .addTestDevice("4C9C29EFCCC3AFE714623A702F482AEE") //Micromax New
                    .addTestDevice("C741815DEAC857EED58BC3D5D8CAE8A2")  //INTEX
                    .addTestDevice("A7A19E06342F7D3868ABA7863D707BD7") //Samsung Tab
                    .addTestDevice("78E289C0CB209B06541CB844A1744650") //LAVA
                    .addTestDevice("C458AB4E076325EA5FE91458A1A1FDC3")//X-ZIOX
                    .addTestDevice("0A011B632C4A66447406B1A0340A175E") // Oppo
                    .addTestDevice("567DB1C5EC4A5D581176C2652228829D") //Celkon
                    .addTestDevice("BEAA671BEA6C971FE461AC6E423B2ADE") //Karbonn
                    .addTestDevice("74527FD0DD7B0489CFB68BAED192733D") //Nexus TAB
                    .addTestDevice("BB5542D48765B65F516CF440C3545896") //Samsung j2
                    .addTestDevice("E56855A0C493CEF11A7098FE6EA840CB") //Videocon
                    .addTestDevice("390FED1AE343E9FF9D644C4085C3868E") //jivi
                    .addTestDevice("ACFC7B7082B3F3FD4E0AC8E92EA10D53") //MI Tab
                    .addTestDevice("863D8BAE88E209F38FF3C94A0403C776") //Samsung old
                    .addTestDevice("C458AB4E076325EA5FE91458A1A1FDC3")//Samsung new
                    .addTestDevice("517048997101BE4535828AC2360582C2") //motorola
                    .addTestDevice("8BB4BCB27396AB8ED222B7F902E13420") //micromax old
                    .addTestDevice("BFAE6D8DB020BF475077F41CED4D4B5B") //Gionee
                    .addTestDevice("EB3DAD0B99C5B3658E0C2ACB31F8BE5B") //Mi Chhotu JIO
                    .addTestDevice("CEF2CF599FA65D8072F04888C122999E") //iVoomi
                    .addTestDevice("BB5542D48765B65F516CF440C3545896") //samusung j2
                    .addTestDevice("DD0A309E21D1F24C324C107BE78C1B88") //Ronak Oreo
                    .addTestDevice("B05DBFFC98F6E3E7A8E75E2FE96C2D65") //Nokia Oreo
                    .addTestDevice("E19949FB5E7C5A28C30A875934AC8181") //HiTech
                    .addTestDevice("D0D8EF8424553C916FABBFD4B38DA503") // Mi white
                    .addTestDevice("1969289F3928DDBAA65020B884860E7A") //lava
                    .addTestDevice("EB3DAD0B99C5B3658E0C2ACB31F8BE5B") //Mi Chhotu JIO new
                    .addTestDevice("8272BDC546AD7129DAAF0D4DB89D3B3F") //Jivi

                    .build();

            mInterstitialAd.loadAd(ins_adRequest);
        } catch (Exception e) {
        }
    }

    public boolean requestNewInterstitial() {

        boolean show = false;

        if (Share.isNeedToAdShow(getApplicationContext())) {
            try {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    show = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                show = false;
            }
        } else {
            show = false;
        }
        return show;
    }

    public boolean isLoaded() {

        boolean isLoaded = false;

        if (Share.isNeedToAdShow(getApplicationContext())) {
            try {
                if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
                    isLoaded = true;
                } else {
                    isLoaded = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                isLoaded = true;
            }
        } else {
            isLoaded = true;
        }

        return isLoaded;

    }

    public static MainApplication getInstance() {
        return singleton;
    }


    private String getProcessName(Context context) {
        if (context == null) return null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }

}
