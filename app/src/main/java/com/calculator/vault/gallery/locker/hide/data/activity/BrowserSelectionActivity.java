package com.calculator.vault.gallery.locker.hide.data.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.ads.AdsListener;
import com.calculator.vault.gallery.locker.hide.data.ads.IntAdsHelper;
import com.calculator.vault.gallery.locker.hide.data.ads.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.common.OnSingleClickListener;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.common.Utils;
import com.google.android.gms.ads.AdView;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

public class BrowserSelectionActivity extends AppCompatActivity implements View.OnClickListener /*MoPubInterstitial.InterstitialAdListener*//*InterstitialAdHelper.onInterstitialAdListener*/ {
    private static final String TAG = "BrowserSelection";
    private RelativeLayout moRlGoogle;
    private RelativeLayout moRlYahoo;
    private RelativeLayout moRlBing;
    private ImageView moIvGoogleTick;
    private ImageView moIvYahooTick;
    private ImageView moIvBingTick;
    private ImageView iv_back;
    private AdView adView;
    private boolean isAdLoad = false;
    private String msSelection;
    private ImageView moIvMoreApp, moIvBlast;

//    private boolean isInterstitialAdLoaded = false;
//    private MoPubInterstitial mInterstitial;
    // TODO Admob Ads
//    private InterstitialAd interstitial;

//    @Override
//    public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {
//        if (Share.isNeedToAdShow(BrowserSelectionActivity.this)) {
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
//        if (Utils.isNetworkConnected(BrowserSelectionActivity.this)) {
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
//        if (Utils.isNetworkConnected(BrowserSelectionActivity.this)) {
//            mInterstitial.load();
//        }
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//        loadBrowser();
//    }

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
//            interstitial = InterstitialAdHelper.getInstance().load2(BrowserSelectionActivity.this, this);
//        }else if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id2))){
//            interstitial = InterstitialAdHelper.getInstance().load(BrowserSelectionActivity.this, this);
//        }else {
//            interstitial = InterstitialAdHelper.getInstance().load1(BrowserSelectionActivity.this, this);
//        }
//
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onClosed() {
//        isInterstitialAdLoaded = false;
//        interstitial = InterstitialAdHelper.getInstance().load1(BrowserSelectionActivity.this, this);
//
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//
//        loadBrowser();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_selection);

//        mInterstitial = new MoPubInterstitial(this, getString(R.string.mopub_int_id));
//        mInterstitial.setInterstitialAdListener(this);
//            mInterstitial.load();
            IntAdsHelper.loadInterstitialAds(BrowserSelectionActivity.this, new AdsListener() {
                @Override
                public void onLoad() {
                    moIvBlast.setVisibility(View.GONE);
//                    moIvMoreApp.setVisibility(View.VISIBLE);
                }

                @Override
                public void onClosed() {
                    moIvBlast.setVisibility(View.GONE);
                    moIvMoreApp.setVisibility(View.GONE);
                    loadBrowser();
                }
            });

//        TODO Admob Ads
//        interstitial = InterstitialAdHelper.getInstance().load1(BrowserSelectionActivity.this, this);

//        IronSource.init(this, "8fb1d745", IronSource.AD_UNIT.INTERSTITIAL);
//        IronSource.shouldTrackNetworkState(this, true);
//        IronSource.setInterstitialListener(this);
//        IronSource.loadInterstitial();

        initView();
        initViewListener();
        initViewAction();

        if (Share.isNeedToAdShow(BrowserSelectionActivity.this)){
            NativeAdvanceHelper.loadNativeAd(this, findViewById(R.id.fl_adplaceholder), true);
        }

    }

    private void initViewAction() {
        String loBrowserSelection = SharedPrefs.getString(this, SharedPrefs.BROWSER_SELECTED, null);
        if (loBrowserSelection == null) {
            makeGoogleImageVisible();
        } else {
            checkSelection(loBrowserSelection);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //IronSource.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //IronSource.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NativeAdvanceHelper.onDestroy();
    }

    private void checkSelection(String loBrowserSelection) {
        switch (loBrowserSelection) {
            case "Google":
                makeGoogleImageVisible();
                break;
            case "Yahoo":
                makeYahooImageVisible();
                break;
            case "Bing":
                makeBingImageVisible();
                break;
        }
    }

    private void initViewListener() {
        moRlGoogle.setOnClickListener(this);
        moRlYahoo.setOnClickListener(this);
        moRlBing.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        moIvMoreApp.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (Share.isNeedToAdShow(BrowserSelectionActivity.this)) {
                    if (IntAdsHelper.isInterstitialLoaded()) {
                        moIvMoreApp.setVisibility(View.GONE);
                        moIvBlast.setVisibility(View.VISIBLE);
                        ((AnimationDrawable) moIvBlast.getBackground()).start();
                        IntAdsHelper.showInterstitialAd();
//                    TODO Admob Ads
//                    interstitial.show();

                    }
                }
            }
        });
    }

    private void initView() {
        moRlGoogle = findViewById(R.id.rl_google);
        moRlYahoo = findViewById(R.id.rl_yahoo);
        moRlBing = findViewById(R.id.rl_bing);
        moIvGoogleTick = findViewById(R.id.iv_googletick_image);
        moIvYahooTick = findViewById(R.id.iv_yahootick_image);
        moIvBingTick = findViewById(R.id.iv_bingtick_image);
        iv_back = findViewById(R.id.iv_back);

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
            case R.id.rl_google:
                msSelection = "Google";
                showInterAd();
                break;

            case R.id.rl_yahoo:
                msSelection = "Yahoo";
                showInterAd();
                break;

            case R.id.rl_bing:
                msSelection = "Bing";
                showInterAd();
                break;

            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private void showInterAd() {

        if (Share.isNeedToAdShow(this)) {
            if (IntAdsHelper.isInterstitialLoaded()) {
                IntAdsHelper.showInterstitialAd();
//                    TODO Admob Ads
//                    interstitial.show();
            } else {
                loadBrowser();
            }
        } else {
            loadBrowser();
        }

    }

    private void loadBrowser(){
        switch (msSelection){
            case "Google":
                makeGoogleImageVisible();
                break;
            case "Yahoo":
                makeYahooImageVisible();
                break;
            case "Bing":
                makeBingImageVisible();
                break;
        }
    }

    private void makeBingImageVisible() {
        msSelection = "Bing";
        SharedPrefs.save(BrowserSelectionActivity.this, SharedPrefs.BROWSER_SELECTED, getString(R.string.Bing));
        moIvGoogleTick.setVisibility(View.INVISIBLE);
        moIvYahooTick.setVisibility(View.INVISIBLE);
        moIvBingTick.setVisibility(View.VISIBLE);
    }

    private void makeYahooImageVisible() {
        msSelection = "Yahoo";
        SharedPrefs.save(BrowserSelectionActivity.this, SharedPrefs.BROWSER_SELECTED, getString(R.string.Yahoo));
        moIvGoogleTick.setVisibility(View.INVISIBLE);
        moIvYahooTick.setVisibility(View.VISIBLE);
        moIvBingTick.setVisibility(View.INVISIBLE);
    }

    private void makeGoogleImageVisible() {
        msSelection = "Google";
        SharedPrefs.save(BrowserSelectionActivity.this, SharedPrefs.BROWSER_SELECTED, getString(R.string.Google));
        moIvGoogleTick.setVisibility(View.VISIBLE);
        moIvYahooTick.setVisibility(View.INVISIBLE);
        moIvBingTick.setVisibility(View.INVISIBLE);
    }
}