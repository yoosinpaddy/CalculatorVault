package com.calculator.vault.gallery.locker.hide.data.ads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.appnext.ads.interstitial.Interstitial;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class IntAdsHelper {

    private static final String TAGADS = "Ads123456";
    @SuppressLint("StaticFieldLeak")
    private static Context moContext;
    private static Interstitial interstitialAppNext;
    private static InterstitialAd interstitialAdMob;
    private static AdsListener moAdsListener;

    public static void loadInterstitialAds(Context foContext, AdsListener foAdsListener) {
        if (!Share.isNeedToAdShow(foContext)) {
            return;
        }
        moContext = foContext;
        moAdsListener = foAdsListener;

        interstitialAdMob = new InterstitialAd(foContext);
        interstitialAdMob.setAdUnitId(foContext.getString(R.string.inter_ad_unit_id));

        final AdRequest loAdRequest = new AdRequest.Builder().build();

        interstitialAdMob.loadAd(loAdRequest);

        interstitialAppNext = new Interstitial(foContext, "48962139-b57c-4ccc-8b09-585fb61bdfd7");

        // TODO ADMOB ads Listener
        interstitialAdMob.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                moAdsListener.onLoad();
                super.onAdLoaded();
                Log.e(TAGADS, " :onAdLoaded:ADMOB");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                interstitialAppNext.loadAd();
                super.onAdFailedToLoad(i);
                Log.e(TAGADS, " :onAdError:ADMOB: " + i);
            }

            @Override
            public void onAdClosed() {
                moAdsListener.onClosed();
                interstitialAdMob.loadAd(loAdRequest);
                super.onAdClosed();
                Log.e(TAGADS, " :onAdClosed:ADMOB");
            }
        });

        // TODO APPNEXT ads Listener
        interstitialAppNext.setOnAdLoadedCallback((s, appnextAdCreativeType) -> {
            moAdsListener.onLoad();
            Log.e(TAGADS, " :onAdLoaded:APPNEXT");
        });

        interstitialAppNext.setOnAdClosedCallback(() -> {
            moAdsListener.onClosed();
            interstitialAdMob.loadAd(loAdRequest);
            Log.e(TAGADS, " :onAdClosed:APPNEXT");
        });

        interstitialAppNext.setOnAdErrorCallback(s -> {
            interstitialAdMob.loadAd(loAdRequest);
            Log.e(TAGADS, " :onAdError:APPNEXT");
        });
    }

    public static boolean isInterstitialLoaded() {
        if (Share.isNeedToAdShow(moContext)) {
            try {
                if (interstitialAppNext != null && interstitialAppNext.isAdLoaded()) {
                    return true;
                } else {
                    return interstitialAdMob != null && interstitialAdMob.isLoaded();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public static void showInterstitialAd() {
        if (Share.isNeedToAdShow(moContext)) {
            try {
                if (interstitialAppNext != null && interstitialAppNext.isAdLoaded()) {
                    interstitialAppNext.showAd();
                } else {
                    if (interstitialAdMob != null && interstitialAdMob.isLoaded()) {
                        interstitialAdMob.show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
