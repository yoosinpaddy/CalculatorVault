package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.vending.billing.IInAppBillingService;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.calculator.vault.gallery.locker.hide.data.smartkit.Database.DBHelperClass;
import com.calculator.vault.gallery.locker.hide.data.smartkit.MainApplication;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.billing.InAppBillingHandler;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.SharedPrefs;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import pl.tajchert.waitingdots.DotsTextView;


public class SplashScreenActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    private FirebaseAnalytics mFirebaseAnalytics;
    String TAG = "TAG";
    ProgressBar progressBar;
    //   LoadingDots process_dots;
    DotsTextView process_dots;
    TextView tv_loading;
    final DBHelperClass dba = new DBHelperClass(this);
    public static InputStream databaseInputStream1;


    private Handler timeoutHandler = new Handler();
    Runnable runnable;
    private boolean is_pause = false;

    // // TODO: 06/11/18  in-app purchase
    private IInAppBillingService mService;
    BillingProcessor billingProcessor;

    String ProductKey = "";
    String LicenseKey = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_splash_screen);

        Share.getDPI();
        // // TODO: 06/11/18  for in app purchase
        ProductKey = getString(R.string.ads_product_key);
        LicenseKey = getString(R.string.licenseKey);

        billingProcessor = new BillingProcessor(SplashScreenActivity.this, LicenseKey, this);
        billingProcessor.initialize();

        bindServices();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        progressBar = findViewById(R.id.progressBar);
        tv_loading = findViewById(R.id.tv_loading);
        process_dots = findViewById(R.id.process_dots);

    }

    private void nextScreen() {

        if (Share.isNeedToAdShow(getApplicationContext())) {
            if (!MainApplication.getInstance().requestNewInterstitial()) {
                startActivity(new Intent(SplashScreenActivity.this, SplashHomeActivity.class));

            } else {
                MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();

                        MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                        MainApplication.getInstance().mInterstitialAd = null;
                        MainApplication.getInstance().ins_adRequest = null;
                        MainApplication.getInstance().LoadAds();

                        startActivity(new Intent(SplashScreenActivity.this, SplashHomeActivity.class));
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        Log.e(TAG, "onAdFailedToLoad: " + "");
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        Log.e(TAG, "onAdLoaded: " + "");
                    }
                });
            }
        } else {

            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //new CopyDB().execute("");

        if (!MainApplication.getInstance().isLoaded()) {
            MainApplication.getInstance().LoadAds();
        }

        if (is_pause) {
            is_pause = false;
            //  nextScreen();
            nextScreen();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timeoutHandler != null)
            timeoutHandler.removeCallbacks(runnable);
        is_pause = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        is_pause = true;
        if (timeoutHandler != null)
            timeoutHandler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        is_pause = true;
        if (timeoutHandler != null)
            timeoutHandler.removeCallbacks(runnable);
    }


    // TODO: 03/11/18 in-app purchase
    private void bindServices() {
        try {
            bindService(InAppBillingHandler.getBindServiceIntent(), mServiceConn, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("checkLoadAds:", "onServiceDisconnected");
            mService = null;
            setAdDelay();
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);

//            if (Share.isNeedToAdShow(getApplicationContext())) {
            checkLoadAds();
//            }
        }
    };

    private void checkLoadAds() {

        boolean isRemovedAds = false;
        Log.e("checkLoadAds: ", "in-app purchase");

        if (SharedPrefs.contain(getApplicationContext(), SharedPrefs.IS_ADS_REMOVED)) {

            if (!SharedPrefs.getBoolean(getApplicationContext(), SharedPrefs.IS_ADS_REMOVED)) {
                checkInAppPurchases(isRemovedAds);
            } else {
                setPurchasedSplashDelay();
            }

        } else {
            checkInAppPurchases(isRemovedAds);
        }
    }

    private void checkInAppPurchases(boolean isRemovedAds) {

        try {
            Bundle ownedItems = mService.getPurchases(3, getPackageName(), "inapp", null);
            int response = ownedItems.getInt("RESPONSE_CODE");
            Log.e("checkLoadAds:", "response --> " + response);

            if (response == 0) {
                ArrayList<String> ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                ArrayList<String> purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                ArrayList<String> signatureList = ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
                String continuationToken = ownedItems.getString("INAPP_CONTINUATION_TOKEN");

                for (int i = 0; i < purchaseDataList.size(); i++) {
                    Log.e("checkLoadAds: ", "purchaseDataList --> " + purchaseDataList.get(i));
                }

                if (purchaseDataList.size() > 0) {
                    Log.e("checkLoadAds:", "load ads (purchased)");

                    for (int i = 0; i < purchaseDataList.size(); ++i) {
                        String purchaseData = purchaseDataList.get(i);
                        String signature = signatureList.get(i);
                        String sku = ownedSkus.get(i);

                        // TODO: 01/11/18 if matches, purchased for ads removal
                        if (sku.equals(getString(R.string.ads_product_key))) {
                            isRemovedAds = true;
                            break;
                        } else {
                            isRemovedAds = false;
                        }
                        Log.e("checkLoadAds: ", "sku --> " + sku);
                    }

                    if (isRemovedAds) {
                        SharedPrefs.savePref(this, SharedPrefs.IS_ADS_REMOVED, true);
                        setPurchasedSplashDelay();
                    } else {
                        SharedPrefs.savePref(this, SharedPrefs.IS_ADS_REMOVED, false);
                        setAdDelay();
                    }

                } else {
                    Log.e("checkLoadAds: ", "billingProcessor --> " + billingProcessor);

                    // TODO: 01/11/18 to solve issue occurred in in-app purchase if device restarts
                    if (billingProcessor != null) {
                        Log.e("checkLoadAds:", "isPurchased --> " + billingProcessor.isPurchased(ProductKey));

                        List<String> list = new ArrayList<>();
                        list.addAll(billingProcessor.listOwnedProducts());
                        Log.e("checkLoadAds: ", "listOwnedProducts list size --> " + list.size());

                        for (int i = 0; i < list.size(); i++) {
                            Log.e("checkLoadAds: ", "listOwnedProducts list --> " + list.get(i));
                        }

                        list.clear();
                        list.addAll(billingProcessor.listOwnedSubscriptions());
                        Log.e("checkLoadAds: ", "listOwnedSubscriptions list size --> " + list.size());

                        for (int i = 0; i < list.size(); i++) {
                            Log.e("checkLoadAds: ", "listOwnedSubscriptions list --> " + list.get(i));
                        }

                        if (billingProcessor.isPurchased(ProductKey)) {

                            SharedPrefs.savePref(this, SharedPrefs.IS_ADS_REMOVED, true);
                            setPurchasedSplashDelay();

                        } else {
                            Log.e("checkLoadAds:", "isPurchased else load ads");
                            SharedPrefs.savePref(this, SharedPrefs.IS_ADS_REMOVED, false);
                            setAdDelay();
                        }

                    } else {
                        Log.e("checkLoadAds:", "billingProcessor else load ads");
                        SharedPrefs.savePref(this, SharedPrefs.IS_ADS_REMOVED, false);
                        setAdDelay();
                    }
                }
            } else {
                setAdDelay();
            }
        } catch (Exception e) {
            e.printStackTrace();
            setAdDelay();
        }
    }

    private void setPurchasedSplashDelay() {
        runnable = new Runnable() {
            public void run() {
                nextScreen();
            }
        };
        timeoutHandler.postDelayed(runnable, 1000);
    }

    private void setAdDelay() {
        runnable = new Runnable() {
            public void run() {
                nextScreen();
            }
        };
        timeoutHandler.postDelayed(runnable, 8000);
    }

    // TODO: 03/11/18 in-app purchase complete

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
