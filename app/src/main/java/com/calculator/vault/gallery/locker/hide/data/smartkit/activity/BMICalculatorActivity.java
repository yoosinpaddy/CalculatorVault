package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.calculator.vault.gallery.locker.hide.data.smartkit.MainApplication;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.adapter.BMIPagerCusAdapter;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;
import com.calculator.vault.gallery.locker.hide.data.smartkit.fragment.BMI_TAB;
import com.calculator.vault.gallery.locker.hide.data.smartkit.fragment.History_Tab;

public class BMICalculatorActivity extends FragmentActivity implements View.OnClickListener {
    boolean im = false;
    private TabLayout tab_lay;
    // private boolean isInForeGround = false;
    private ViewPager view_pager;
    private ImageView iv_back;
    private BMICalculatorActivity context;
    private Intent intent;
    private String TAG = BMICalculatorActivity.class.getSimpleName();
//    InterstitialAd mInterstitialAd;
private FirebaseAnalytics mFirebaseAnalytics;
    private ImageView iv_more_app, iv_blast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_bmi_calculator);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        initUI();


        BMIPagerCusAdapter adapter = new BMIPagerCusAdapter(getSupportFragmentManager());
        view_pager.setAdapter(adapter);
        setupViewPager(view_pager);

        tab_lay.setupWithViewPager(view_pager);


        if (Share.BMI_History){
            TabLayout.Tab tab = tab_lay.getTabAt(1);
            tab.select();
        }


        if (Share.isNeedToAdShow(this)){
            NativeAdvanceHelper.loadAdBannerSize(this, (FrameLayout) findViewById(R.id.fl_adplaceholder));
            setActionBar();
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        BMIPagerCusAdapter adapter = new BMIPagerCusAdapter(getSupportFragmentManager());
        adapter.addFragment(new BMI_TAB(), "BMI");
        adapter.addFragment(new History_Tab(), "HISTORY");
        viewPager.setAdapter(adapter);
    }

    private void initUI() {

        tab_lay = (TabLayout) findViewById(R.id.tab_lay);
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_more_app = findViewById(R.id.iv_more_app);
        iv_blast = findViewById(R.id.iv_blast);

        iv_back.setOnClickListener(this);
    }

    private void setActionBar() {
        try {
            iv_more_app.setVisibility(View.GONE);
            iv_more_app.setBackgroundResource(R.drawable.animation_list_filling);
            ((AnimationDrawable) iv_more_app.getBackground()).start();
            loadInterstialAd();

            iv_more_app.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_more_app.setVisibility(View.GONE);
                    iv_blast.setVisibility(View.VISIBLE);
                    ((AnimationDrawable) iv_blast.getBackground()).start();

                    if (MainApplication.getInstance().requestNewInterstitial()) {
                        MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                iv_blast.setVisibility(View.GONE);
                                iv_more_app.setVisibility(View.GONE);
                                loadInterstialAd();
                            }

                            @Override
                            public void onAdFailedToLoad(int i) {
                                super.onAdFailedToLoad(i);
                                iv_blast.setVisibility(View.GONE);
                                iv_more_app.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                iv_blast.setVisibility(View.GONE);
                                iv_more_app.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        iv_blast.setVisibility(View.GONE);
                        iv_more_app.setVisibility(View.GONE);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadInterstialAd() {
        try {
            if (Share.isNeedToAdShow(this)){
                if (MainApplication.getInstance().mInterstitialAd.isLoaded()) {
                    Log.e("if", "if");
//                    iv_more_app.setVisibility(View.VISIBLE);
                } else {
                    MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                    MainApplication.getInstance().mInterstitialAd = null;
                    MainApplication.getInstance().ins_adRequest = null;
                    MainApplication.getInstance().LoadAds();
                    MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            Log.e("load", "load");
//                            iv_more_app.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAdFailedToLoad(int i) {
                            super.onAdFailedToLoad(i);
                            Log.e("fail", "fail");
                            iv_more_app.setVisibility(View.GONE);
                            //ApplicationClass.getInstance().LoadAds();
                            loadInterstialAd();
                        }
                    });
                }
            }

        } catch (Exception e) {
//            iv_more_app.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_back:
                im = true;
                onBackPressed();
                break;
        }
    }
}