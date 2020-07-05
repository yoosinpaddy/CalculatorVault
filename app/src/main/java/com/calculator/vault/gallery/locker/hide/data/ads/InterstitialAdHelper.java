package com.calculator.vault.gallery.locker.hide.data.ads;

import android.content.Context;
import android.util.Log;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class InterstitialAdHelper {

    private static InterstitialAdHelper instance;
    private String TAG = "Ads_123";

    public static InterstitialAdHelper getInstance() {

        if (instance == null) {
            synchronized (InterstitialAdHelper.class) {
                if (instance == null) {
                    instance = new InterstitialAdHelper();
                }
            }
        }
        return instance;
    }

    public InterstitialAd load(Context mContext, final onInterstitialAdListener adListener) {

        InterstitialAd interstitial = new InterstitialAd(mContext);
        interstitial.setAdUnitId(mContext.getString(R.string.inter_ad_unit_id));
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        if (Share.isNeedToAdShow(mContext)) {
            interstitial.loadAd(adRequest);
        }

        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.i(TAG, "onAdLoaded: 0");
                adListener.onLoad();
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.i(TAG, "onAdFailedToLoad: 0");
                adListener.onFailed();
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                Log.i(TAG, "onAdOpened: 0");
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                Log.i(TAG, "onAdLeftApplication: 0");
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                Log.i(TAG, "onAdClosed: 0");
                adListener.onClosed();
                // Code to be executed when the interstitial ad is closed.
            }
        });

        return interstitial;
    }

    public InterstitialAd load1(Context mContext, final onInterstitialAdListener adListener) {


        InterstitialAd interstitial = new InterstitialAd(mContext);
        interstitial.setAdUnitId(mContext.getString(R.string.inter_ad_unit_id1));
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        if (Share.isNeedToAdShow(mContext)) {
            interstitial.loadAd(adRequest);
        }

        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.i(TAG, "onAdLoaded: 1");
                adListener.onLoad();
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.i(TAG, "onAdFailedToLoad: 1");
                adListener.onFailed();
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                Log.i(TAG, "onAdOpened: 1");
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                Log.i(TAG, "onAdLeftApplication: 1");
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                Log.i(TAG, "onAdClosed: 1");
                adListener.onClosed();
                // Code to be executed when the interstitial ad is closed.
            }
        });

        return interstitial;
    }

    public InterstitialAd load2(Context mContext, final onInterstitialAdListener adListener) {

        InterstitialAd interstitial = new InterstitialAd(mContext);
        interstitial.setAdUnitId(mContext.getString(R.string.inter_ad_unit_id2));
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        if (Share.isNeedToAdShow(mContext)) {
            interstitial.loadAd(adRequest);
        }

        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.i(TAG, "onAdLoaded: 2");
                adListener.onLoad();
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.i(TAG, "onAdFailedToLoad: 2");
                adListener.onFailed();
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                Log.i(TAG, "onAdOpened: 2");
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                Log.i(TAG, "onAdLeftApplication: 2");
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                Log.i(TAG, "onAdClosed: 2");
                adListener.onClosed();
                // Code to be executed when the interstitial ad is closed.
            }
        });

        return interstitial;
    }


    public interface onInterstitialAdListener {
        void onLoad();

        void onFailed();

        void onClosed();
    }
}
