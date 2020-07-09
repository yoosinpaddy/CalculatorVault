package com.calculator.vault.gallery.locker.hide.data.smartkit.common;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.calculator.vault.gallery.locker.hide.data.R;

import java.util.List;

public class NativeAdvanceHelper {

    public static String TAG = "Ads_123";

    public static UnifiedNativeAd nativeAd;


    // 1. Add container in xml+

   /* <FrameLayout
    android:id="@+id/fl_adplaceholder"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_centerInParent="true" />*/

    // 2. Add in onCreate() method
    //   NativeAdvanceHelper.loadAd(mContext, (FrameLayout) findViewById(R.id.fl_adplaceholder));

    // 3. Add in onDestroy() method
    //       NativeAdvanceHelper.onDestroy();


    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     */
    public static void loadAd(final Context mContext, final FrameLayout frameLayout) {
        if (!Share.isNeedToAdShow(mContext)){
            frameLayout.setVisibility(View.GONE);
            return;
        }
        AdLoader.Builder builder = new AdLoader.Builder(mContext, mContext.getString(R.string.native_ad_id));

        // OnUnifiedNativeAdLoadedListener implementation.
        builder.forUnifiedNativeAd(unifiedNativeAd -> {
            // You must call destroy on old ads when you are done with them,
            // otherwise you will have a memory leak.
            if (nativeAd != null) {
                nativeAd.destroy();
            }
            nativeAd = unifiedNativeAd;
            UnifiedNativeAdView adView = (UnifiedNativeAdView) ((Activity)mContext).getLayoutInflater().inflate(R.layout.s_ad_unified, null);
            populateUnifiedNativeAdView(unifiedNativeAd, adView);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        });


        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {

                Log.e(TAG, "onAdFailedToLoad: " + errorCode);

                //     Toast.makeText(mContext, "Failed to load native ad: " + errorCode, Toast
                // .LENGTH_SHORT).show();
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

        /*AdLoader.Builder builder = new AdLoader.Builder(mContext, mContext.getResources().getString(R.string.native_ad_unit_id));
        builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
            @Override
            public void onContentAdLoaded(NativeContentAd nativeContentAd) {
                NativeContentAdView adView = (NativeContentAdView) ((Activity)mContext).getLayoutInflater()
                        .inflate(R.layout.s_ad_unified, null);
                populateUnifiedNativeAdView(nativeContentAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("Failed Loadd native", "==>" + "Failed to load native ad: "
                        + errorCode);
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());*/

    }


    public static void loadAdMediumSize(final Context mContext, final FrameLayout frameLayout) {
        if (!Share.isNeedToAdShow(mContext)){
            frameLayout.setVisibility(View.GONE);
            return;
        }
        AdLoader.Builder builder = new AdLoader.Builder(mContext, mContext.getString(R.string.native_ad_id));
        // OnUnifiedNativeAdLoadedListener implementation.
        builder.forUnifiedNativeAd(unifiedNativeAd -> {
            // You must call destroy on old ads when you are done with them,
            // otherwise you will have a memory leak.
            if (nativeAd != null) {
                nativeAd.destroy();
            }
            nativeAd = unifiedNativeAd;
            UnifiedNativeAdView adView = (UnifiedNativeAdView) ((Activity)mContext).getLayoutInflater().inflate(R.layout.s_layout_nativead_grid_three, null);
            populateUnifiedNativeAdView(unifiedNativeAd, adView);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        });

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {

                Log.e(TAG, "onAdFailedToLoad: " + errorCode);

                //     Toast.makeText(mContext, "Failed to load native ad: " + errorCode, Toast
                // .LENGTH_SHORT).show();
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public static void loadAdBannerSize(final Context mContext, final FrameLayout frameLayout) {
        if (!Share.isNeedToAdShow(mContext)){
            frameLayout.setVisibility(View.GONE);
            return;
        }
        AdLoader.Builder builder = new AdLoader.Builder(mContext, mContext.getString(R.string.native_ad_id));
        // OnUnifiedNativeAdLoadedListener implementation.
        builder.forUnifiedNativeAd(unifiedNativeAd -> {
            // You must call destroy on old ads when you are done with them,
            // otherwise you will have a memory leak.
            if (nativeAd != null) {
                nativeAd.destroy();
            }
            nativeAd = unifiedNativeAd;
            UnifiedNativeAdView adView = (UnifiedNativeAdView) ((Activity)mContext).getLayoutInflater().inflate(R.layout.s_view_banner_size_native_ad, null);
            populateUnifiedNativeAdView(unifiedNativeAd, adView);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        });


        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {

                Log.e(TAG, "onAdFailedToLoad: " + errorCode);

                //     Toast.makeText(mContext, "Failed to load native ad: " + errorCode, Toast
                // .LENGTH_SHORT).show();
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }


    /**
     * Populates a {@link UnifiedNativeAd} object with data from a given
     * {@link UnifiedNativeAd}.
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView   the view to be populated
     */

    /* NEW */
    private static void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.

        // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
        // VideoController will call methods on this object when events occur in the video
        // lifecycle.


        MediaView mediaView = adView.findViewById(R.id.ad_media);
        ImageView mainImageView = adView.findViewById(R.id.ad_image);
        TextView tv_ad = adView.findViewById(R.id.tv_ad);

        // Apps can check the VideoController's hasVideoContent property to determine if the
        // NativeAppInstallAd has a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
        // VideoController will call methods on this object when events occur in the video
        // lifecycle.
        vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            public void onVideoEnd() {
                // Publishers should allow native ads to complete video playback before refreshing
                // or replacing them with another ad in the same UI location.
                super.onVideoEnd();
            }
        });
        if (vc.hasVideoContent()) {
            adView.setMediaView(mediaView);
            mainImageView.setVisibility(View.GONE);
        } else {
            adView.setImageView(mainImageView);
            mediaView.setVisibility(View.GONE);
            // At least one image is guaranteed.
            List<NativeAd.Image> images = nativeAd.getImages();
            if (images.size() != 0)
                mainImageView.setImageDrawable(images.get(0).getDrawable());
        }
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));

        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }

    /* OLD */
    /*private static void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {

        MediaView mediaView = adView.findViewById(R.id.ad_media);
        ImageView mainImageView = adView.findViewById(R.id.ad_image);

        VideoController vc = nativeAd.getVideoController();

        vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            public void onVideoEnd() {
                super.onVideoEnd();
            }
        });

        if (vc.hasVideoContent()) {
            adView.setMediaView(mediaView);
            mainImageView.setVisibility(View.GONE);
        } else {
            adView.setImageView(mainImageView);
            mediaView.setVisibility(View.GONE);

            List<NativeAd.Image> images = nativeAd.getImages();

            if (images.size() != 0) {
                mainImageView.setImageDrawable(images.get(0).getDrawable());
            }
        }

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }*/

//    private static void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
//        // Set the media view. Media content will be automatically populated in the media view once
//        // adView.setNativeAd() is called.
//        MediaView mediaView = adView.findViewById(R.id.ad_media);
//        adView.setMediaView(mediaView);
//
//        // Set other ad assets.
//        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
//        adView.setBodyView(adView.findViewById(R.id.ad_body));
//        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
//        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
//        adView.setPriceView(adView.findViewById(R.id.ad_price));
//        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
//        adView.setStoreView(adView.findViewById(R.id.ad_store));
//        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
//
//        // The headline is guaranteed to be in every UnifiedNativeAd.
//        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
//
//        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
//        // check before trying to display them.
//        if (nativeAd.getBody() == null) {
//            adView.getBodyView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getBodyView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
//        }
//
//        if (nativeAd.getCallToAction() == null) {
//            adView.getCallToActionView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getCallToActionView().setVisibility(View.VISIBLE);
//            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
//        }
//
//        if (nativeAd.getIcon() == null) {
//            adView.getIconView().setVisibility(View.GONE);
//        } else {
//            ((ImageView) adView.getIconView()).setImageDrawable(
//                    nativeAd.getIcon().getDrawable());
//            adView.getIconView().setVisibility(View.VISIBLE);
//        }
//
////        if (nativeAd.getPrice() == null) {
////            adView.getPriceView().setVisibility(View.INVISIBLE);
////        } else {
////            adView.getPriceView().setVisibility(View.VISIBLE);
////            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
////        }
//
////        if (nativeAd.getStore() == null) {
////            adView.getStoreView().setVisibility(View.INVISIBLE);
////        } else {
////            adView.getStoreView().setVisibility(View.VISIBLE);
////            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
////        }
//
////        if (nativeAd.getStarRating() == null) {
////            adView.getStarRatingView().setVisibility(View.INVISIBLE);
////        } else {
////            ((RatingBar) adView.getStarRatingView())
////                    .setRating(nativeAd.getStarRating().floatValue());
////            adView.getStarRatingView().setVisibility(View.VISIBLE);
////        }
//
////        if (nativeAd.getAdvertiser() == null) {
////            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
////        } else {
////            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
////            adView.getAdvertiserView().setVisibility(View.VISIBLE);
////        }
//
//        // This method tells the Google Mobile Ads SDK that you have finished populating your
//        // native ad view with this native ad. The SDK will populate the adView's MediaView
//        // with the media content from this native ad.
//        adView.setNativeAd(nativeAd);
//
//        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
//        // have a video asset.
//        VideoController vc = nativeAd.getVideoController();
//
//    }

    public static void onDestroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }
    }
}