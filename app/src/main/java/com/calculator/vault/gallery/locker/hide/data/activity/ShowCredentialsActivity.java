package com.calculator.vault.gallery.locker.hide.data.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.ads.AdsListener;
import com.calculator.vault.gallery.locker.hide.data.ads.IntAdsHelper;
import com.calculator.vault.gallery.locker.hide.data.ads.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.common.Utils;
import com.calculator.vault.gallery.locker.hide.data.data.DecoyDatabase;
import com.calculator.vault.gallery.locker.hide.data.data.ImageVideoDatabase;
import com.calculator.vault.gallery.locker.hide.data.adapter.CredentialAdapter;
import com.calculator.vault.gallery.locker.hide.data.model.CredentialsModel;
import com.google.android.gms.ads.AdView;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import java.util.ArrayList;
import java.util.Collections;

public class ShowCredentialsActivity extends AppCompatActivity implements View.OnClickListener, CredentialAdapter.ItemOnClick
        /*MoPubInterstitial.InterstitialAdListener*//*InterstitialAdHelper.onInterstitialAdListener*/ {

    private static final String TAG = "ShowCredentialsActivity";
    private RecyclerView moRvCredentialList;
    private GridLayoutManager moGridLayoutManager;
    private CredentialAdapter moCredentialAdapter;
    private LinearLayout moLLaddCredential;
    private RelativeLayout moLLNoCreds;
    private ImageView moIvAddCred;
    private ArrayList<CredentialsModel> moCredentialsArray = new ArrayList<>();
    private ShowCredentialsActivity moActivity = ShowCredentialsActivity.this;
    ImageVideoDatabase mdbImageVideoDatabase = new ImageVideoDatabase(this);
    ImageView iv_back;
    private DecoyDatabase decoyDatabase = new DecoyDatabase(this);
    private String isDecode;
    private boolean isFirstTime = true;
    private AdView adView;
    private boolean isAdLoad = false;

//    private boolean isInterstitialAdLoaded = false;
//    private MoPubInterstitial mInterstitial;
//    TODO Admob Ads
//    private InterstitialAd interstitial;

//    @Override
//    public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {
//        isInterstitialAdLoaded = true;
//    }
//
//    @Override
//    public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {
//        Log.e(TAG, "onInterstitialFailed: "+ moPubErrorCode);
//        isInterstitialAdLoaded = false;
//        if (Utils.isNetworkConnected(ShowCredentialsActivity.this)) {
//            mInterstitial.load();
//        }
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
//        if (Utils.isNetworkConnected(ShowCredentialsActivity.this)) {
//            mInterstitial.load();
//        }
//        getCredential();
//    }

//    TODO Admob Ads
//    @Override
//    public void onLoad() {
//        isInterstitialAdLoaded = true;
//    }
//
//    @Override
//    public void onFailed() {
//        isInterstitialAdLoaded = false;
//        if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id1))){
//            interstitial = InterstitialAdHelper.getInstance().load2(ShowCredentialsActivity.this, this);
//        }else if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id2))){
//            interstitial = InterstitialAdHelper.getInstance().load(ShowCredentialsActivity.this, this);
//        }else {
//            interstitial = InterstitialAdHelper.getInstance().load1(ShowCredentialsActivity.this, this);
//        }
//    }
//
//    @Override
//    public void onClosed() {
//        isInterstitialAdLoaded = false;
//        interstitial = InterstitialAdHelper.getInstance().load1(ShowCredentialsActivity.this, this);
//        getCredential();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_credentials);

//        mInterstitial = new MoPubInterstitial(this, getString(R.string.mopub_int_id));
//        mInterstitial.setInterstitialAdListener(this);
//            mInterstitial.load();
            IntAdsHelper.loadInterstitialAds(ShowCredentialsActivity.this, new AdsListener() {
                @Override
                public void onLoad() {

                }

                @Override
                public void onClosed() {
                    getCredential();
                }
            });

//        TODO Admob Ads
//        interstitial = InterstitialAdHelper.getInstance().load1(ShowCredentialsActivity.this, this);

//        IronSource.init(this, "8fb1d745", IronSource.AD_UNIT.INTERSTITIAL);
//        IronSource.shouldTrackNetworkState(this, true);
//        IronSource.setInterstitialListener(this);
//        IronSource.loadInterstitial();

        initView();

        isDecode = SharedPrefs.getString(ShowCredentialsActivity.this, SharedPrefs.DecoyPassword, "false");
        setupList();
        initViewListener();

        initViewAction();
        adView = (AdView) findViewById(R.id.adView);
        Share.loadAdsBanner(ShowCredentialsActivity.this, adView);

        // IronSource Banner Ads
        //ISAdsHelper.loadBannerAd(this,(FrameLayout) findViewById(R.id.flBanner));

        if (Share.isNeedToAdShow(ShowCredentialsActivity.this)) {
//            NativeAdvanceHelper.loadGreedyGameAd(this, (FrameLayout) findViewById(R.id.fl_adplaceholder), "float-4028");
        }

    }

    private void setupList() {
        if (isDecode.equals("true")) {
            moCredentialsArray = decoyDatabase.getAllCredentials();
        } else {
            moCredentialsArray = mdbImageVideoDatabase.getAllCredentials();
        }

        Collections.reverse(moCredentialsArray);
        moCredentialAdapter = new CredentialAdapter(moActivity, moCredentialsArray, moActivity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(moActivity);

        moRvCredentialList.setLayoutManager(mLayoutManager);
        moRvCredentialList.setItemAnimator(new DefaultItemAnimator());
        moRvCredentialList.addItemDecoration(new DividerItemDecoration(ShowCredentialsActivity.this, DividerItemDecoration.VERTICAL));
        moRvCredentialList.setAdapter(moCredentialAdapter);
    }

    private void initViewAction() {
        //getCredential();
        loadInterAdAndGetCreds("init");
    }

    private void loadInterAdAndGetCreds(String fWhere) {

        if (fWhere.equalsIgnoreCase("init")) {
            getCredential();
        } else {
            if (Share.isNeedToAdShow(this)) {
                if (IntAdsHelper.isInterstitialLoaded()) {
                    IntAdsHelper.showInterstitialAd();
//                    TODO Admob Ads
//                    interstitial.show();
                } else {
                    getCredential();
                }
            } else {
                getCredential();
            }


//        if (fWhere.equalsIgnoreCase("init")) {
//            getCredential();
//        } else {
//
//            if (Share.isNeedToAdShow(this)) {
//                if (IronSource.isInterstitialReady()) {
//                    if (isAdLoad) {
//                        IronSource.showInterstitial();
//                    }else {
//                        getCredential();
//                    }
//                } else {
//                    getCredential();
//                }
//            }else {
//                getCredential();
//            }
        }
    }

    private void getCredential() {
        moCredentialsArray.clear();
        if (isDecode.equals("true")) {
            moCredentialsArray = decoyDatabase.getAllCredentials();
        } else {
            moCredentialsArray = mdbImageVideoDatabase.getAllCredentials();
        }

        if (moCredentialsArray.isEmpty()) {
            moLLNoCreds.setVisibility(View.VISIBLE);
            if (Share.isNeedToAdShow(ShowCredentialsActivity.this)) {
                NativeAdvanceHelper.loadNativeAd(this, findViewById(R.id.fl_adplaceholder), true);
            }
        } else {
            findViewById(R.id.fl_adplaceholder).setVisibility(View.GONE);
            moLLNoCreds.setVisibility(View.GONE);
        }

        Collections.reverse(moCredentialsArray);
        moCredentialAdapter.onUpdate(moCredentialsArray);
    }

    private void initViewListener() {
        moLLaddCredential.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        moIvAddCred.setOnClickListener(this);
    }

    private void initView() {
        moIvAddCred = findViewById(R.id.iv_add_creds);
        moRvCredentialList = findViewById(R.id.rv_credentialsList);
        moLLaddCredential = findViewById(R.id.ll_add_credentials);
        moLLNoCreds = findViewById(R.id.ll_noCreds);
        iv_back = findViewById(R.id.iv_back);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //IronSource.onResume(this);

        if (!isFirstTime) {
            loadInterAdAndGetCreds("onResume");
        } else {
            isFirstTime = false;
        }
        getCredential();
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

    @Override
    public void onClick(int position, CredentialsModel itemModel) {
        if (isDecode.equals("true")) {
            decoyDatabase.removeSingleCredential(moCredentialsArray.get(position).getID());
        } else {
            mdbImageVideoDatabase.removeSingleCredential(moCredentialsArray.get(position).getID());
        }
        loadInterAdAndGetCreds("onClick");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_add_credentials:
                Intent loIntent = new Intent(this, AddCredentialsActivity.class);
                startActivity(loIntent);
                break;
            case R.id.iv_add_creds:
                Intent iIntent = new Intent(this, AddCredentialsActivity.class);
                startActivity(iIntent);
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
