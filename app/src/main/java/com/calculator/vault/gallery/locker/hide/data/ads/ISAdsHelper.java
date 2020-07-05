package com.calculator.vault.gallery.locker.hide.data.ads;

import android.app.Activity;
import android.util.Log;
import android.widget.FrameLayout;

import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.BannerListener;
import com.ironsource.mediationsdk.sdk.InterstitialListener;

public class ISAdsHelper {

    public static void loadBannerAd(final Activity foActivity, final FrameLayout bannerContainer){

        if (true){
            return;
        }

        if (!Share.isNeedToAdShow(foActivity)){
            return;
        }

        IronSource.init(foActivity, "8fb1d745", IronSource.AD_UNIT.BANNER);

        final IronSourceBannerLayout banner = IronSource.createBanner(foActivity, ISBannerSize.SMART);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        bannerContainer.removeAllViews();

        banner.setBannerListener(new BannerListener() {
            @Override
            public void onBannerAdLoaded() {
                // Called after a banner ad has been successfully loaded
                bannerContainer.addView(banner, 0, layoutParams);
                Log.e("ISBanner", "onBannerAdLoaded: ");
            }

            @Override
            public void onBannerAdLoadFailed(IronSourceError error) {
                // Called after a banner has attempted to load an ad but failed.
                Log.e("ISBanner", "onBannerAdLoadFailed: " + error.getErrorMessage() );
                foActivity.runOnUiThread(() -> bannerContainer.removeAllViews());
                IronSource.loadBanner(banner);
            }

            @Override
            public void onBannerAdClicked() {
                // Called after a banner has been clicked.
                Log.e("ISBanner", "onBannerAdClicked: ");
            }

            @Override
            public void onBannerAdScreenPresented() {
                // Called when a banner is about to present a full screen content.
                Log.e("ISBanner", "onBannerAdScreenPresented: ");
            }

            @Override
            public void onBannerAdScreenDismissed() {
                // Called after a full screen content has been dismissed
                Log.e("ISBanner", "onBannerAdScreenDismissed: ");
            }

            @Override
            public void onBannerAdLeftApplication() {
                // Called when a user would be taken out of the application context.
                Log.e("ISBanner", "onBannerAdLeftApplication: ");
            }
        });

        IronSource.loadBanner(banner);
    }

    public static void loadInterstitialAd(Activity foActivity, InterstitialListener foInterstitialListener){

        if (true){
            return;
        }

        if (!Share.isNeedToAdShow(foActivity)){
            return;
        }

        IronSource.init(foActivity, "8fb1d745", IronSource.AD_UNIT.INTERSTITIAL);

        IronSource.setInterstitialListener(new InterstitialListener() {
            /**
             Invoked when Interstitial Ad is ready to be shown after load function was called.
             */
            @Override
            public void onInterstitialAdReady() {
                IronSource.showInterstitial("8fb1d745");
                Log.e("ISInterstitial", "onInterstitialAdReady: ");
            }
            /**
             invoked when there is no Interstitial Ad available after calling load function.
             */
            @Override
            public void onInterstitialAdLoadFailed(IronSourceError error) {
                Log.e("ISInterstitial", "onInterstitialAdLoadFailed: " + error.getErrorMessage());
            }
            /**
             Invoked when the Interstitial Ad Unit is opened
             */
            @Override
            public void onInterstitialAdOpened() {
                Log.e("ISInterstitial", "onInterstitialAdOpened: ");
            }
            /*
             * Invoked when the ad is closed and the user is about to return to the application.
             */
            @Override
            public void onInterstitialAdClosed() {
                Log.e("ISInterstitial", "onInterstitialAdClosed: ");
            }
            /*
             * Invoked when the ad was opened and shown successfully.
             */
            @Override
            public void onInterstitialAdShowSucceeded() {
                Log.e("ISInterstitial", "onInterstitialAdShowSucceeded: ");
            }
            /**
             * Invoked when Interstitial ad failed to show.
             // @param error - An object which represents the reason of showInterstitial failure.
             */
            @Override
            public void onInterstitialAdShowFailed(IronSourceError error) {
                Log.e("ISInterstitial", "onInterstitialAdShowFailed: " + error.getErrorMessage());
            }
            /*
             * Invoked when the end user clicked on the interstitial ad.
             */
            @Override
            public void onInterstitialAdClicked() {
                Log.e("ISInterstitial", "onInterstitialAdClicked: ");
            }
        });

        IronSource.loadInterstitial();
    }

    public interface ISAdsListener{
        void onInterstitialAdShowFailed(IronSourceError error);
        void onInterstitialAdClosed();
        void onInterstitialAdLoadFailed(IronSourceError error);
    }

}
