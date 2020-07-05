package com.calculator.vault.gallery.locker.hide.data.ads;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.applovin.nativeAds.AppLovinNativeAd;
import com.applovin.nativeAds.AppLovinNativeAdLoadListener;
import com.applovin.sdk.AppLovinErrorCodes;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkUtils;
import com.appnext.core.AppnextAdCreativeType;
import com.appnext.core.AppnextError;
import com.appnext.nativeads.NativeAdListener;
import com.appnext.nativeads.NativeAdRequest;
import com.appnext.nativeads.NativeAdView;
import com.appnext.nativeads.PrivacyIcon;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.activity.CalculatorActivity;
import com.calculator.vault.gallery.locker.hide.data.ads.appLovinSupport.AppLovinCarouselViewSettings;
import com.calculator.vault.gallery.locker.hide.data.ads.appLovinSupport.InlineCarouselCardMediaView;
import com.calculator.vault.gallery.locker.hide.data.ads.appLovinSupport.InlineCarouselCardState;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.greedygame.android.agent.GreedyGameAgent;
import com.greedygame.android.core.campaign.CampaignStateListener;
import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiNative;
import com.inmobi.ads.listeners.NativeAdEventListener;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.logging.MoPubLog;
import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.FacebookAdRenderer;
import com.mopub.nativeads.GooglePlayServicesAdRenderer;
import com.mopub.nativeads.MediaViewBinder;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.RequestParameters;
import com.mopub.nativeads.ViewBinder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class NativeAdvanceHelper {

    public static String TAG = "Ads_123";

    public static UnifiedNativeAd nativeAd;

    public static int miAdNumber = 1;

    //APP_NEXT Declarations...
    private static com.appnext.nativeads.NativeAd nativeAdAN;

    private static AppLovinNativeAd appLovinNativeAd;

    // 1. Add container in xml+

    /* add this dependency in app level gradle
     * implementation 'com.google.android.gms:play-services-ads:17.2.1'
     * */

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

    public static void loadNativeAd(final Context mContext, final FrameLayout frameLayout, boolean isNeedToShow){
        loadAd(mContext, frameLayout, isNeedToShow);
    }

    public static void loadNativeSmallAd(final Context mContext, final FrameLayout frameLayout){
        loadAdSmall(mContext, frameLayout);
    }

    private static void loadAd(final Context mContext, final FrameLayout frameLayout, boolean isNeedToShow) {

        if (!Share.isNeedToAdShow(mContext)) {
            return;
        }
        AdLoader.Builder builder;
        if (miAdNumber == 1) {
            builder = new AdLoader.Builder(mContext, mContext.getString(R.string.native_ad_unit_id));
        } else if (miAdNumber == 2) {
            builder = new AdLoader.Builder(mContext, mContext.getString(R.string.native_ad_unit_id));
        } else {
            builder = new AdLoader.Builder(mContext, mContext.getString(R.string.native_ad_unit_id));
        }

        builder.forUnifiedNativeAd(unifiedNativeAd -> {
            if (nativeAd != null) {
                nativeAd.destroy();
            }
            nativeAd = unifiedNativeAd;
            UnifiedNativeAdView adView = (UnifiedNativeAdView) ((Activity) mContext).getLayoutInflater().inflate(R.layout.layout_nativead, null);
            populateUnifiedNativeAdView(unifiedNativeAd, adView);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        });

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.i(TAG, "on Native AdFailedToLoad: " + miAdNumber + "  errorCode: " + errorCode);
                appNextLoadAd((Activity) mContext, frameLayout);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.i(TAG, "on Native AdLoaded: " + miAdNumber);
                if (isNeedToShow) {
                    frameLayout.setVisibility(View.VISIBLE);
                }
                miAdNumber = 1;
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }

    private static void loadAdSmall(final Context mContext, final FrameLayout frameLayout) {

        if (!Share.isNeedToAdShow(mContext)) {
            return;
        }
        AdLoader.Builder builder;
        if (miAdNumber == 1) {
            builder = new AdLoader.Builder(mContext, mContext.getString(R.string.native_ad_unit_id));
        } else if (miAdNumber == 2) {
            builder = new AdLoader.Builder(mContext, mContext.getString(R.string.native_ad_unit_id));
        } else {
            builder = new AdLoader.Builder(mContext, mContext.getString(R.string.native_ad_unit_id));
        }

        builder.forUnifiedNativeAd(unifiedNativeAd -> {
            if (nativeAd != null) {
                nativeAd.destroy();
            }
            nativeAd = unifiedNativeAd;
            UnifiedNativeAdView adView = (UnifiedNativeAdView) ((Activity) mContext).getLayoutInflater().inflate(R.layout.layout_native_small, null);
            populateUnifiedNativeAdView(unifiedNativeAd, adView);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        });

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.i(TAG, "on Native AdFailedToLoad: " + miAdNumber + "  errorCode: " + errorCode);
                appNextLoadAdSmall((Activity) mContext, frameLayout);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.i(TAG, "on Native AdLoaded: " + miAdNumber);
                miAdNumber = 1;
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }

    private static void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {

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
    }

//    TODO APP NEXT NATIVE AD

    public static void appNextLoadAd(Activity activity, FrameLayout frameLayout) {

        if (!Share.isNeedToAdShow(activity)) {
            return;
        }

        NativeAdView nativeAdView;
        ImageView imageView;
        TextView textView, rating, description;
        com.appnext.nativeads.MediaView mediaView;
        ProgressBar progressBar;
        Button button;
        ArrayList<View> viewArrayList;

        View adView = activity.getLayoutInflater().inflate(R.layout.appnext_native_ad_layout, null);
        nativeAdView = adView.findViewById(R.id.na_view);
        imageView = adView.findViewById(R.id.na_icon);
        textView = adView.findViewById(R.id.na_title);
        mediaView = adView.findViewById(R.id.na_media);
        progressBar = adView.findViewById(R.id.progressBar);
        button = adView.findViewById(R.id.install);
        rating = adView.findViewById(R.id.rating);
        description = adView.findViewById(R.id.description);
        viewArrayList = new ArrayList<>();
        viewArrayList.add(button);
        viewArrayList.add(mediaView);

        nativeAdAN = new com.appnext.nativeads.NativeAd(activity, "59285138-0081-4ba3-b6c6-3d0ce0b42609");
        nativeAdAN.setPrivacyPolicyColor(PrivacyIcon.PP_ICON_COLOR_LIGHT);

        nativeAdAN.setAdListener(new NativeAdListener() {
            @Override
            public void onAdLoaded(final com.appnext.nativeads.NativeAd nativeAd, AppnextAdCreativeType adType) {
                super.onAdLoaded(nativeAd, adType);
                progressBar.setVisibility(View.GONE);
                nativeAd.downloadAndDisplayImage(imageView, nativeAd.getIconURL());
                textView.setText(nativeAd.getAdTitle());
                nativeAd.setMediaView(mediaView);
                rating.setText(nativeAd.getStoreRating());
                description.setText(nativeAd.getAdDescription());
                nativeAd.registerClickableViews(viewArrayList);
                nativeAd.setNativeAdView(nativeAdView);
                frameLayout.addView(adView);
            }

            @Override
            public void onAdClicked(com.appnext.nativeads.NativeAd nativeAd) {
                super.onAdClicked(nativeAd);
            }

            @Override
            public void onError(com.appnext.nativeads.NativeAd nativeAd, AppnextError appnextError) {
                super.onError(nativeAd, appnextError);
                progressBar.setVisibility(View.GONE);
                Log.e("TAG", "onError: " + appnextError.getErrorMessage());
            }

            @Override
            public void adImpression(com.appnext.nativeads.NativeAd nativeAd) {
                super.adImpression(nativeAd);
            }
        });

        nativeAdAN.loadAd(new NativeAdRequest()
                // optional - config your ad request:
                .setCachingPolicy(NativeAdRequest.CachingPolicy.STATIC_ONLY)
                .setCreativeType(NativeAdRequest.CreativeType.ALL)
                .setVideoLength(NativeAdRequest.VideoLength.SHORT)
                .setVideoQuality(NativeAdRequest.VideoQuality.LOW)
        );

    }

//    TODO APP NEXT SMALL NATIVE AD

    public static void appNextLoadAdSmall(Activity activity, FrameLayout frameLayout) {

        if (!Share.isNeedToAdShow(activity)) {
            return;
        }

        NativeAdView nativeAdView;
        ImageView imageView;
        TextView textView, rating, description;
        com.appnext.nativeads.MediaView mediaView;
        ProgressBar progressBar;
        Button button;
        ArrayList<View> viewArrayList;

        View adView = activity.getLayoutInflater().inflate(R.layout.appnext_native_ad_layout_small, null);
        nativeAdView = adView.findViewById(R.id.na_view);
        imageView = adView.findViewById(R.id.na_icon);
        textView = adView.findViewById(R.id.na_title);
        mediaView = adView.findViewById(R.id.na_media);
        progressBar = adView.findViewById(R.id.progressBar);
        button = adView.findViewById(R.id.install);
        rating = adView.findViewById(R.id.rating);
        description = adView.findViewById(R.id.description);
        viewArrayList = new ArrayList<>();
        viewArrayList.add(button);
        viewArrayList.add(mediaView);

        nativeAdAN = new com.appnext.nativeads.NativeAd(activity, "59285138-0081-4ba3-b6c6-3d0ce0b42609");
        nativeAdAN.setPrivacyPolicyColor(PrivacyIcon.PP_ICON_COLOR_LIGHT);
        nativeAdAN.setAdListener(new NativeAdListener());
        nativeAdAN.setAdListener(new NativeAdListener() {
            @Override
            public void onAdLoaded(final com.appnext.nativeads.NativeAd nativeAd, AppnextAdCreativeType adType) {
                super.onAdLoaded(nativeAd, adType);
                progressBar.setVisibility(View.GONE);
                nativeAd.downloadAndDisplayImage(imageView, nativeAd.getIconURL());
                textView.setText(nativeAd.getAdTitle());
                nativeAd.setMediaView(mediaView);
                rating.setText(nativeAd.getStoreRating());
                description.setText(nativeAd.getAdDescription());
                nativeAd.registerClickableViews(viewArrayList);
                nativeAd.setNativeAdView(nativeAdView);
                frameLayout.addView(adView);
            }

            @Override
            public void onAdClicked(com.appnext.nativeads.NativeAd nativeAd) {
                super.onAdClicked(nativeAd);
            }

            @Override
            public void onError(com.appnext.nativeads.NativeAd nativeAd, AppnextError appnextError) {
                super.onError(nativeAd, appnextError);
                progressBar.setVisibility(View.GONE);
                Log.e("TAG", "onError: " + appnextError.getErrorMessage());
            }

            @Override
            public void adImpression(com.appnext.nativeads.NativeAd nativeAd) {
                super.adImpression(nativeAd);
            }
        });

        nativeAdAN.loadAd(new NativeAdRequest()
                // optional - config your ad request:
                .setCachingPolicy(NativeAdRequest.CachingPolicy.STATIC_ONLY)
                .setCreativeType(NativeAdRequest.CreativeType.ALL)
                .setVideoLength(NativeAdRequest.VideoLength.SHORT)
                .setVideoQuality(NativeAdRequest.VideoQuality.LOW)
        );

    }

    public static void moPubAd(Activity activity, FrameLayout frameLayout) {

        if (!Share.isNeedToAdShow(activity)){
            frameLayout.setVisibility(View.GONE);
            return;
        }

        ViewBinder viewBinder = new ViewBinder.Builder(R.layout.layout_native_mopub)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        final GooglePlayServicesAdRenderer googlePlayServicesAdRenderer = new GooglePlayServicesAdRenderer(
                new MediaViewBinder.Builder(R.layout.layout_native_mopub)
                        .mediaLayoutId(R.id.native_media_layout) // bind to your `com.mopub.nativeads.MediaLayout` element
                        .iconImageId(R.id.native_icon_image)
                        .titleId(R.id.native_title)
                        .textId(R.id.native_text)
                        .callToActionId(R.id.native_cta)
                        .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                        .build());

        FacebookAdRenderer facebookAdRenderer = new FacebookAdRenderer(
                new FacebookAdRenderer.FacebookViewBinder.Builder(R.layout.layout_native_mopub)
                        .titleId(R.id.native_title)
                        .textId(R.id.native_text)
                        .mediaViewId(R.id.native_ad_main_image)
                        .adIconViewId(R.id.native_ad_icon_image)
                        .advertiserNameId(R.id.native_title) // Bind either the titleId or advertiserNameId depending on the FB SDK version
                        .callToActionId(R.id.native_cta)
                        .build());

        MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

        MoPubNative moPubNative;
        moPubNative = new MoPubNative(activity, "933538166e234ec1874feb5b2697d937", new MoPubNative.MoPubNativeNetworkListener() {
            //        moPubNative = new MoPubNative(activity, "11a17b188668469fb0412708c3d16813", new MoPubNative.MoPubNativeNetworkListener() {
            @Override
            public void onNativeLoad(com.mopub.nativeads.NativeAd nativeAd) {
                Log.d("MoPub", "Native ad has loaded.");
                AdapterHelper adapterHelper = new AdapterHelper(activity, 0, 3); // When standalone, any range will be fine.
                // Retrieve the pre-built ad view that AdapterHelper prepared for us.
                View v = adapterHelper.getAdView(null, null, nativeAd, new ViewBinder.Builder(0).build());
                // Set the native event listeners (onImpression, and onClick).
                nativeAd.setMoPubNativeEventListener(new com.mopub.nativeads.NativeAd.MoPubNativeEventListener() {
                    @Override
                    public void onImpression(View view) {
                        Log.e("MoPub", "onImpression: ");
                    }

                    @Override
                    public void onClick(View view) {
                        Log.e("MoPub", "onClick: ");
                    }
                });
                // Add the ad view to our view hierarchy
                frameLayout.addView(v);
//                frameLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNativeFail(NativeErrorCode errorCode) {
                frameLayout.setVisibility(View.GONE);
                Log.d("MoPub", "Native ad failed to load with error: " + errorCode.toString() + "N:" + errorCode.name() + "C:" + errorCode.getIntCode());
            }
        });

        moPubNative.registerAdRenderer(moPubStaticNativeAdRenderer);
        moPubNative.registerAdRenderer(googlePlayServicesAdRenderer);
        moPubNative.registerAdRenderer(facebookAdRenderer);

        EnumSet<RequestParameters.NativeAdAsset> desiredAssets = EnumSet.of(
                RequestParameters.NativeAdAsset.TITLE,
                RequestParameters.NativeAdAsset.TEXT,
                RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT,
                RequestParameters.NativeAdAsset.MAIN_IMAGE,
                RequestParameters.NativeAdAsset.ICON_IMAGE,
                RequestParameters.NativeAdAsset.STAR_RATING
        );

        RequestParameters mRequestParameters = new RequestParameters.Builder()
                .desiredAssets(desiredAssets)
                .build();
        moPubNative.makeRequest(mRequestParameters);

//        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder("933538166e234ec1874feb5b2697d937")
//                .withLogLevel(MoPubLog.LogLevel.DEBUG)
//                .withLegitimateInterestAllowed(false)
//                .build();
//
//        MoPub.initializeSdk(activity, sdkConfiguration, () -> {
//            Log.e(TAG, "onInitializationFinished: ");
//
//        });
    }

    public static void moPubAdSmall(Activity activity, FrameLayout frameLayout) {

        if (!Share.isNeedToAdShow(activity)){
            frameLayout.setVisibility(View.GONE);
            return;
        }

        ViewBinder viewBinder = new ViewBinder.Builder(R.layout.layout_native_mopub_small)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .callToActionId(R.id.native_cta)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        final GooglePlayServicesAdRenderer googlePlayServicesAdRenderer = new GooglePlayServicesAdRenderer(
                new MediaViewBinder.Builder(R.layout.layout_native_mopub_small)
                        .mediaLayoutId(R.id.native_media_layout) // bind to your `com.mopub.nativeads.MediaLayout` element
                        .iconImageId(R.id.native_icon_image)
                        .titleId(R.id.native_title)
                        .textId(R.id.native_text)
                        .callToActionId(R.id.native_cta)
                        .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                        .build());

        FacebookAdRenderer facebookAdRenderer = new FacebookAdRenderer(
                new FacebookAdRenderer.FacebookViewBinder.Builder(R.layout.layout_native_mopub_small)
                        .titleId(R.id.native_title)
                        .textId(R.id.native_text)
                        .mediaViewId(R.id.native_ad_main_image)
                        .adIconViewId(R.id.native_ad_icon_image)
                        .advertiserNameId(R.id.native_title) // Bind either the titleId or advertiserNameId depending on the FB SDK version
                        .callToActionId(R.id.native_cta)
                        .build());

        MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

        MoPubNative moPubNative;
        moPubNative = new MoPubNative(activity, "933538166e234ec1874feb5b2697d937", new MoPubNative.MoPubNativeNetworkListener() {
            //        moPubNative = new MoPubNative(activity, "11a17b188668469fb0412708c3d16813", new MoPubNative.MoPubNativeNetworkListener() {
            @Override
            public void onNativeLoad(com.mopub.nativeads.NativeAd nativeAd) {
                Log.d("MoPub", "Native ad has loaded.");
                AdapterHelper adapterHelper = new AdapterHelper(activity, 0, 3); // When standalone, any range will be fine.
                // Retrieve the pre-built ad view that AdapterHelper prepared for us.
                View v = adapterHelper.getAdView(null, null, nativeAd, new ViewBinder.Builder(0).build());
                // Set the native event listeners (onImpression, and onClick).
                nativeAd.setMoPubNativeEventListener(new com.mopub.nativeads.NativeAd.MoPubNativeEventListener() {
                    @Override
                    public void onImpression(View view) {
                        Log.e("MoPub", "onImpression: ");
                    }

                    @Override
                    public void onClick(View view) {
                        Log.e("MoPub", "onClick: ");
                    }
                });
                // Add the ad view to our view hierarchy
                frameLayout.addView(v);
            }

            @Override
            public void onNativeFail(NativeErrorCode errorCode) {
                Log.d("MoPub", "Native ad failed to load with error: " + errorCode.toString() + "N:" + errorCode.name() + "C:" + errorCode.getIntCode());
            }
        });

        moPubNative.registerAdRenderer(moPubStaticNativeAdRenderer);
        moPubNative.registerAdRenderer(googlePlayServicesAdRenderer);
        moPubNative.registerAdRenderer(facebookAdRenderer);

        EnumSet<RequestParameters.NativeAdAsset> desiredAssets = EnumSet.of(
                RequestParameters.NativeAdAsset.TITLE,
                RequestParameters.NativeAdAsset.TEXT,
                RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT,
                RequestParameters.NativeAdAsset.MAIN_IMAGE,
                RequestParameters.NativeAdAsset.ICON_IMAGE,
                RequestParameters.NativeAdAsset.STAR_RATING
        );

        RequestParameters mRequestParameters = new RequestParameters.Builder()
                .desiredAssets(desiredAssets)
                .build();

        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder("933538166e234ec1874feb5b2697d937")
                .withLogLevel(MoPubLog.LogLevel.DEBUG)
                .withLegitimateInterestAllowed(false)
                .build();

        MoPub.initializeSdk(activity, sdkConfiguration, () -> {
            Log.e(TAG, "onInitializationFinished: ");
            moPubNative.makeRequest(mRequestParameters);
        });
    }

    public static void inMobiAd(Activity activity, FrameLayout frameLayout) {

        InMobiNative inMobiNative = new InMobiNative(activity, Long.parseLong("1561661899567"), new NativeAdEventListener() {
            @Override
            public void onAdLoadSucceeded(InMobiNative inMobiNative) {
                super.onAdLoadSucceeded(inMobiNative);

                Log.e(TAG, "onAdLoadSucceeded: ");

                View adView = LayoutInflater.from(activity).inflate(R.layout.inmobi_native_layout, null);

                ImageView icon = (ImageView) adView.findViewById(R.id.adIcon);
                TextView title = (TextView) adView.findViewById(R.id.adTitle);
                TextView description = (TextView) adView.findViewById(R.id.adDescription);
                Button action = (Button) adView.findViewById(R.id.adAction);
                FrameLayout content = (FrameLayout) adView.findViewById(R.id.adContent);
                RatingBar ratingBar = (RatingBar) adView.findViewById(R.id.adRating);

                Picasso.with(activity)
                        .load(inMobiNative.getAdIconUrl())
                        .into(icon);
                title.setText(inMobiNative.getAdTitle());
                description.setText(inMobiNative.getAdDescription());
                action.setText(inMobiNative.getAdCtaText());

                DisplayMetrics displayMetrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                content.addView(inMobiNative.getPrimaryViewOfWidth(activity, content, (ViewGroup) frameLayout, displayMetrics.widthPixels));

                float rating = inMobiNative.getAdRating();
                if (rating != 0) {
                    ratingBar.setRating(rating);
                }
                ratingBar.setVisibility(rating != 0 ? View.VISIBLE : View.GONE);

                action.setOnClickListener(v -> inMobiNative.reportAdClickAndOpenLandingPage());

                frameLayout.addView(adView);

            }

            @Override
            public void onAdLoadFailed(InMobiNative inMobiNative, InMobiAdRequestStatus inMobiAdRequestStatus) {
                super.onAdLoadFailed(inMobiNative, inMobiAdRequestStatus);
                Log.e(TAG, "onAdLoadFailed: " + inMobiAdRequestStatus.getMessage());
            }
        });

        inMobiNative.load();

    }

    public static void appLovinAd(Activity activity, FrameLayout frameLayout) {

        final AppLovinSdk sdk = AppLovinSdk.getInstance(activity);
        View adView = activity.getLayoutInflater().inflate(R.layout.applovin_native_ad_layout, null);

        TextView appTitleTextView = adView.findViewById(R.id.appTitleTextView);
        TextView appDescriptionTextView = adView.findViewById(R.id.appDescriptionTextView);
        Button appDownloadButton = adView.findViewById(R.id.appDownloadButton);
        FrameLayout mediaViewPlaceholder = adView.findViewById(R.id.mediaViewPlaceholder);


        sdk.getNativeAdService().loadNativeAds(1, new AppLovinNativeAdLoadListener() {
            @Override
            public void onNativeAdsLoaded(final List list) {
                appLovinNativeAd = (AppLovinNativeAd) list.get(0);
                appTitleTextView.setText(appLovinNativeAd.getTitle());
                appDescriptionTextView.setText(appLovinNativeAd.getDescriptionText());
                adView.findViewById(R.id.mediaViewPlaceholder);
                AppLovinSdkUtils.safePopulateImageView(adView.findViewById(R.id.appIcon),
                        Uri.parse(appLovinNativeAd.getIconUrl()), AppLovinSdkUtils.dpToPx(activity, AppLovinCarouselViewSettings.ICON_IMAGE_MAX_SCALE_SIZE));

                //final Drawable starRatingDrawable = getStarRatingDrawable(activity, appLovinNativeAd.getStarRating());
                //adView.findViewById(R.id.appRating).setImageDrawable(starRatingDrawable);

                appDownloadButton.setText(appLovinNativeAd.getCtaText());

                InlineCarouselCardMediaView mediaView = new InlineCarouselCardMediaView(activity);
                mediaView.setAd(appLovinNativeAd);
                mediaView.setCardState(new InlineCarouselCardState());
                mediaView.setSdk(AppLovinSdk.getInstance(activity));
                mediaView.setUiHandler(new Handler(Looper.getMainLooper()));
                mediaView.setUpView();
                mediaView.autoplayVideo();

                mediaViewPlaceholder.removeAllViews();
                mediaViewPlaceholder.addView(mediaView);
                frameLayout.addView(adView);
                Log.e(TAG, "onAppLovinNativeAdsLoaded: ");
            }

            @Override
            public void onNativeAdsFailedToLoad(final int errorCode) {
                // Native ads failed to load for some reason, likely a network error.
                // Compare errorCode to the available constants in AppLovinErrorCodes.
                Log.e(TAG, "onAppLovinNativeAdsFailedToLoad: " + errorCode);

                if (errorCode == AppLovinErrorCodes.NO_FILL) {
                    // No ad was available for this placement
                }
            }
        });

        adView.findViewById(R.id.appDownloadButton).setOnClickListener(v -> {
            if (nativeAd != null) {
                appLovinNativeAd.launchClickTarget(adView.findViewById(android.R.id.content).getContext());
            }
        });
    }


    //ToDo:: GREEDY GAME INTEGRATION...
    public static void loadGreedyGameAd(final Activity activity, final FrameLayout adView, final String adID) {
        ImageView ggAdView = new ImageView(activity);
        ggAdView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        ggAdView.setAdjustViewBounds(true);
        ggAdView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (Share.isNeedToAdShow(activity)) {

            /*GreedyGameAgent greedyGame = new GreedyGameAgent.Builder(activity)
                    .enableAdmob(true) // To enable Admob
                    .setGameId("28098400")
//                .withAgentListener(activity) // To get Campaign State callbacks
//                    .addUnitId("unit-2452") // Adding Clickable and Non-Clickable units to be shown inside the game.
                    .addUnitId(adID)
                    .build();*/

            final GreedyGameAgent greedyGame = CalculatorActivity.greedyGame;
//            Log.e("loadGreedyGameAd: ", "greedyGame.isCampaignAvailable() ==> " + greedyGame.isCampaignAvailable());


            if (greedyGame != null && greedyGame.isCampaignAvailable()) {
                adView.addView(ggAdView);
                renderGGAds(null, ggAdView, greedyGame, adID);
                adView.setVisibility(View.VISIBLE);
            } else {
                adView.setVisibility(View.GONE);
            }

            if (greedyGame != null) {

                greedyGame.setCampaignStateListener(new CampaignStateListener() {
                    @Override
                    public void onUnavailable() {
                        Log.e("GreedyGame", "OnUnAvailable");
                        renderGGAds(null, ggAdView, greedyGame, null);
                        adView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAvailable(String s) {
                        Log.e("GreedyGame", "OnAvailable");
                        if (Share.isNeedToAdShow(activity)) {
                            renderGGAds(null, ggAdView, greedyGame, adID);
                            adView.removeAllViews();
                            adView.addView(ggAdView);
                            adView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(String s) {
                        Log.e("GreedyGame", "OnError ==> " + s);
                        adView.setVisibility(View.GONE);
                    }
                });
//            greedyGame.init();

//                final GreedyGameAgent finalGreedyGame = greedyGame;
                ggAdView.setOnClickListener(v -> greedyGame.showUII(adID));

//                Timer T = new Timer();
//                T.scheduleAtFixedRate(new TimerTask() {
//                    @Override
//                    public void run() {
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.e("GG[", "Refresh");
//                                finalGreedyGame.startEventRefresh();
//                            }
//                        });
//                    }
//                }, 10000, 65000);

            } else {
                adView.setVisibility(View.GONE);
            }

        } else {
            adView.setVisibility(View.GONE);
        }
    }

    public static void loadGreedyGameAd(final Activity activity, final FrameLayout adViewOne, final FrameLayout adViewTwo, final String adID) {
        ImageView ggAdView = new ImageView(activity);
        ggAdView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        ggAdView.setAdjustViewBounds(true);
        ggAdView.setScaleType(ImageView.ScaleType.FIT_XY);
        ImageView ggAdViewTwo = new ImageView(activity);
        ggAdViewTwo.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        ggAdViewTwo.setAdjustViewBounds(true);
        ggAdViewTwo.setScaleType(ImageView.ScaleType.FIT_XY);
        if (Share.isNeedToAdShow(activity)) {

            /*GreedyGameAgent greedyGame = new GreedyGameAgent.Builder(activity)
                    .enableAdmob(true) // To enable Admob
                    .setGameId("28098400")
//                .withAgentListener(activity) // To get Campaign State callbacks
//                    .addUnitId("unit-2452") // Adding Clickable and Non-Clickable units to be shown inside the game.
                    .addUnitId(adID)
                    .build();*/

            final GreedyGameAgent greedyGame = CalculatorActivity.greedyGame;
//            Log.e("loadGreedyGameAd: ", "greedyGame.isCampaignAvailable() ==> " + greedyGame.isCampaignAvailable());


            if (greedyGame != null && greedyGame.isCampaignAvailable()) {
                renderGGAds(null, ggAdView, greedyGame, adID);
                renderGGAds(null, ggAdViewTwo, greedyGame, adID);
                adViewOne.addView(ggAdView);
                adViewTwo.addView(ggAdViewTwo);
                adViewOne.setVisibility(View.VISIBLE);
                adViewTwo.setVisibility(View.VISIBLE);
            } else {
                adViewOne.setVisibility(View.GONE);
                adViewTwo.setVisibility(View.GONE);
            }

            if (greedyGame != null) {

                greedyGame.setCampaignStateListener(new CampaignStateListener() {
                    @Override
                    public void onUnavailable() {
                        Log.e("GreedyGame", "OnUnAvailable");
                        renderGGAds(null, ggAdView, greedyGame, null);
                        renderGGAds(null, ggAdViewTwo, greedyGame, null);
                        adViewOne.setVisibility(View.GONE);
                        adViewTwo.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAvailable(String s) {
                        Log.e("GreedyGame", "OnAvailable");
                        if (Share.isNeedToAdShow(activity)) {
                            renderGGAds(null, ggAdView, greedyGame, adID);
                            renderGGAds(null, ggAdViewTwo, greedyGame, adID);
                            adViewOne.removeAllViews();
                            adViewTwo.removeAllViews();
                            adViewOne.addView(ggAdView);
                            adViewTwo.addView(ggAdViewTwo);
                            adViewOne.setVisibility(View.VISIBLE);
                            adViewTwo.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(String s) {
                        Log.e("GreedyGame", "OnError ==> " + s);
                        adViewOne.setVisibility(View.GONE);
                        adViewTwo.setVisibility(View.GONE);
                    }
                });
//            greedyGame.init();
                ggAdView.setOnClickListener(v -> greedyGame.showUII(adID));
                ggAdViewTwo.setOnClickListener(v -> greedyGame.showUII(adID));


            } else {
                adViewOne.setVisibility(View.GONE);
                adViewTwo.setVisibility(View.GONE);
            }

        } else {
            adViewOne.setVisibility(View.GONE);
            adViewTwo.setVisibility(View.GONE);
        }
    }

    public static void loadGreedyGameAdNative(final Activity activity, final FrameLayout adView, final String adID) {
        ImageView ggAdView = new ImageView(activity);
        ggAdView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) activity.getResources().getDimension(R.dimen._200sdp)));
        ggAdView.setAdjustViewBounds(true);
        ggAdView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (Share.isNeedToAdShow(activity)) {

            /*GreedyGameAgent greedyGame = new GreedyGameAgent.Builder(activity)
                    .enableAdmob(true) // To enable Admob
                    .setGameId("28098400")
//                .withAgentListener(activity) // To get Campaign State callbacks
//                    .addUnitId("unit-2452") // Adding Clickable and Non-Clickable units to be shown inside the game.
                    .addUnitId(adID)
                    .build();*/

            final GreedyGameAgent greedyGame = CalculatorActivity.greedyGame;
//            Log.e("loadGreedyGameAd: ", "greedyGame.isCampaignAvailable() ==> " + greedyGame.isCampaignAvailable());


            if (greedyGame != null && greedyGame.isCampaignAvailable()) {
                adView.addView(ggAdView);
                renderGGAds(null, ggAdView, greedyGame, adID);
                adView.setVisibility(View.VISIBLE);
            } else {
                adView.setVisibility(View.GONE);
            }

            if (greedyGame != null) {

                greedyGame.setCampaignStateListener(new CampaignStateListener() {
                    @Override
                    public void onUnavailable() {
                        Log.e("GreedyGame", "OnUnAvailable");
                        renderGGAds(null, ggAdView, greedyGame, null);
                        adView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAvailable(String s) {
                        Log.e("GreedyGame", "OnAvailable");
                        if (Share.isNeedToAdShow(activity)) {
                            renderGGAds(null, ggAdView, greedyGame, adID);
                            adView.removeAllViews();
                            adView.addView(ggAdView);
                            adView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(String s) {
                        Log.e("GreedyGame", "OnError ==> " + s);
                        adView.setVisibility(View.GONE);
                    }
                });
//            greedyGame.init();

//                final GreedyGameAgent finalGreedyGame = greedyGame;
                ggAdView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        greedyGame.showUII(adID);
                    }
                });

//                Timer T = new Timer();
//                T.scheduleAtFixedRate(new TimerTask() {
//                    @Override
//                    public void run() {
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.e("GG[", "Refresh");
//                                finalGreedyGame.startEventRefresh();
//                            }
//                        });
//                    }
//                }, 10000, 65000);

            } else {
                adView.setVisibility(View.GONE);
            }

        } else {
            adView.setVisibility(View.GONE);
        }
    }

    private static void renderGGAds(CardView cardView, ImageView ggAdView, GreedyGameAgent greedyGame, final String adID) {

        String path = greedyGame.getPath(adID);
        // Once you get back the path generate a bitmap from the file path as shown below.

        if (path != null) {
            File file = new File(path);

            if (file.exists()) {
                Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());

                if (bm != null && ggAdView != null) {
                    // Use it on your element which needs to be branded.
                    Log.e("renderGGAds: ", "setImageBitmap");
                    ggAdView.setImageBitmap(bm);
                    Log.e("renderGGAds: ", "setImageBitmap");

                    if (cardView != null) {
                        Log.e("renderGGAds: ", "cardView visible");

                        cardView.setVisibility(View.VISIBLE);
                        ggAdView.setVisibility(View.VISIBLE);

//                        cardView.getLayoutParams().height = (int) (NewsListActivity.rv_news.getHeight() / 5.5);

                    } else {
                        Log.e("renderGGAds: ", "ggAdView visible");
                        ggAdView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    // TODO: 06/06/19 To show/hide parent view GreedyGame view in list
//    public static void loadGreedyGameAd(final Activity activity, final NewsListAdapter.GreedyGameViewHolder holder, final CardView cardView, final ImageView ggAdView, final String adID) {
//
//        if (Share.isNeedToAdShow(activity)) {
//
//            /*GreedyGameAgent greedyGame = new GreedyGameAgent.Builder(activity)
//                    .enableAdmob(true) // To enable Admob
//                    .setGameId("28098400")
////                .withAgentListener(activity) // To get Campaign State callbacks
//                    .addUnitId("unit-2452") // Adding Clickable and Non-Clickable units to be shown inside the game.
//                    .addUnitId(adID)
//                    .build();*/
//
//            final GreedyGameAgent greedyGame = CalculatorActivity.greedyGame;
//
//            if (greedyGame != null && greedyGame.isCampaignAvailable()) {
//                renderGGAds(cardView, ggAdView, greedyGame, adID);
//            }
//
//            if (greedyGame != null) {
//                greedyGame.setCampaignStateListener(new CampaignStateListener() {
//                    @Override
//                    public void onUnavailable() {
//                        Log.e("GreedyGame", "OnUnAvailable");
//                    }
//
//                    @Override
//                    public void onAvailable(String s) {
//                        Log.e("GreedyGame", "OnAvailable");
//                        renderGGAds(cardView, ggAdView, greedyGame, adID);
//                    }
//
//                    @Override
//                    public void onError(String s) {
//                        Log.e("GreedyGame", "OnError ==> " + s);
//                    }
//                });
////            greedyGame.init();
//
//                final GreedyGameAgent finalGreedyGame = greedyGame;
//                ggAdView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        finalGreedyGame.showUII(adID);
//                    }
//                });
//
//                Timer T = new Timer();
//                T.scheduleAtFixedRate(new TimerTask() {
//                    @Override
//                    public void run() {
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.e("GG[", "Refresh");
//                                finalGreedyGame.startEventRefresh();
//                            }
//                        });
//                    }
//                }, 10000, 70000);
//
//            } else {
//                holder.hideAdsLayout();
//                cardView.setVisibility(View.GONE);
//            }
//
//        } else {
//            holder.hideAdsLayout();
//            cardView.setVisibility(View.GONE);
//        }
//    }

    public static void onDestroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }
//        if (nativeAdAN != null){
//            nativeAdAN.destroy();
//        }
    }

    private Drawable getStarRatingDrawable(Activity foActivity, final float starRating) {
        final String sanitizedRating = Float.toString(starRating).replace(".", "_");
        final String resourceName = "applovin_star_sprite_" + sanitizedRating;
        final int drawableId = foActivity.getResources().getIdentifier(resourceName, "drawable", foActivity.getPackageName());

        return foActivity.getResources().getDrawable(drawableId);
    }
}
