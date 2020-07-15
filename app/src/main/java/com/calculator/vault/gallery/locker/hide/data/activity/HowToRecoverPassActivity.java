package com.calculator.vault.gallery.locker.hide.data.activity;

import android.graphics.drawable.AnimationDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.ads.AdsListener;
import com.calculator.vault.gallery.locker.hide.data.ads.IntAdsHelper;
import com.calculator.vault.gallery.locker.hide.data.ads.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.common.OnSingleClickListener;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.common.Utils;
import com.google.android.gms.ads.AdView;
import com.inmobi.sdk.InMobiSdk;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

public class HowToRecoverPassActivity extends AppCompatActivity implements View.OnClickListener
        /*MoPubInterstitial.InterstitialAdListener*//*InterstitialAdHelper.onInterstitialAdListener*/ {

    private static final String TAG = "HowToRecoverPass";
    private ImageView mIVBack;
    private AdView adView;

    private ImageView moIvMoreApp, moIvBlast;

    /*private boolean isInterstitialAdLoaded = false;
    private MoPubInterstitial mInterstitial;*/
//    TODO Admob Ads
//    private InterstitialAd interstitial;
//    private GreedyGameAgent greedyGame;

//    @Override
//    public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {
//        if (Share.isNeedToAdShow(HowToRecoverPassActivity.this)) {
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
//        if (Utils.isNetworkConnected(HowToRecoverPassActivity.this)) {
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
//        if (Utils.isNetworkConnected(HowToRecoverPassActivity.this)) {
//            mInterstitial.load();
//        }
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
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
//            interstitial = InterstitialAdHelper.getInstance().load2(HowToRecoverPassActivity.this, this);
//        }else if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id2))){
//            interstitial = InterstitialAdHelper.getInstance().load(HowToRecoverPassActivity.this, this);
//        }else {
//            interstitial = InterstitialAdHelper.getInstance().load1(HowToRecoverPassActivity.this, this);
//        }
//
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onClosed() {
//        isInterstitialAdLoaded = false;
//        interstitial = InterstitialAdHelper.getInstance().load1(HowToRecoverPassActivity.this, this);
//
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_recover_pass);

//        mInterstitial = new MoPubInterstitial(this, getString(R.string.mopub_int_id));
//        mInterstitial.setInterstitialAdListener(this);
//        if (Utils.isNetworkConnected(HowToRecoverPassActivity.this)) {
//            mInterstitial.load();
            IntAdsHelper.loadInterstitialAds(HowToRecoverPassActivity.this, new AdsListener() {
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
//        interstitial = InterstitialAdHelper.getInstance().load1(HowToRecoverPassActivity.this, this);

        initViews();
//        adView = (AdView) findViewById(R.id.adView);
//        Share.loadAdsBanner(HowToRecoverPassActivity.this,adView);

        moIvMoreApp.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (Share.isNeedToAdShow(HowToRecoverPassActivity.this)) {
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

        if (Share.isNeedToAdShow(HowToRecoverPassActivity.this)){
            InMobiSdk.setLogLevel(InMobiSdk.LogLevel.DEBUG);
            NativeAdvanceHelper.loadNativeAd(this, findViewById(R.id.fl_adplaceholder), true);
        }

//        greedyGame = new GreedyGameAgent.Builder(this)
//                .enableAdmob(true) // To enable Admob
//                .setGameId("84360092")
//                .withAgentListener(new CampaignListener()) // To get Campaign State callbacks
//                .addUnitId("float-4028") // Adding Clickable and Non-Clickable units to be shown inside the game.
//                .addUnitId("unit-4257") // UNIT ID will be generated in next step
//                .setGameId("84360092")
//                .build();
//        greedyGame.init();

    }
//    public class CampaignListener implements CampaignStateListener {
//
//        @Override
//        public void onUnavailable() {
//            Log.e(TAG, "onUnavailable: ");
//        }
//
//        @Override
//        public void onAvailable(String s) {
//            Log.e(TAG, "onAvailable: " + s);
//
//            // You can get back the path to the branded texture for a
//            // non-clickable unit using the following call.
//            String path = greedyGame.getPath("unit-4257");
//            // Once you get back the path generate a bitmap from the file path as shown below.
//
//            if(path != null) {
//                File file = new File(path);
//                if (file.exists()) {
//                    Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
//                    if (bm != null) {
//                        // Use it on your element which needs to be branded.
//                        ImageView imageView = findViewById(R.id.greedyTest);
//                        imageView.setImageBitmap(bm);
//                    }
//                }
//            }
//
//        }
//
//        @Override
//        public void onError(String s) {
//            Log.e(TAG, "onError: " + s);
//        }
//
//    }

    private void initViews() {

        mIVBack = findViewById(R.id.iv_howToRecover_back);
        mIVBack.setOnClickListener(this);

        // Gift Ads
        moIvMoreApp = findViewById(R.id.iv_more_app);
        moIvBlast = findViewById(R.id.iv_blast);
        moIvMoreApp.setVisibility(View.GONE);
        moIvMoreApp.setBackgroundResource(R.drawable.animation_list_filling);
        ((AnimationDrawable) moIvMoreApp.getBackground()).start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.iv_howToRecover_back:
                onBackPressed();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HowToRecoverPassActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NativeAdvanceHelper.onDestroy();
    }
}
