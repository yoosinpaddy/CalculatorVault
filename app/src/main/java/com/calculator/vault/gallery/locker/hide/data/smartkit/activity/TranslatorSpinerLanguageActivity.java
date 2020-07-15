package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.calculator.vault.gallery.locker.hide.data.smartkit.MainApplication;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.adapter.CustomBaseAdapter;
import com.calculator.vault.gallery.locker.hide.data.smartkit.adapter.TranslatorAdapter;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.Customlangmodel;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class TranslatorSpinerLanguageActivity extends AppCompatActivity implements View.OnClickListener {
    protected TranslatorSpinerLanguageActivity activity;
    protected ImageView iv_back;
    protected ListView listview_translang;


    List<Customlangmodel> list;
    private TranslatorAdapter mDbHelper;
    private List<Customlangmodel> languages;

    private String TextViewChack = null;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ImageView iv_more_app, iv_blast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_translator_spiner_language);
        activity = TranslatorSpinerLanguageActivity.this;


        TextViewChack = getIntent().getStringExtra("TextView");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        initView();
        initViewAction();
        initViewListener();

        if (Share.isNeedToAdShow(this)) {
            setActionBar();
        }
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        listview_translang = findViewById(R.id.listview_translang);
        iv_more_app = findViewById(R.id.iv_more_app);
        iv_blast = findViewById(R.id.iv_blast);
    }


    private void initViewAction() {

        mDbHelper = new TranslatorAdapter(this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        list = new ArrayList();

        languages = mDbHelper.getAllLanguages();

        Collections.sort(languages, new Comparator<Customlangmodel>() {
            public int compare(Customlangmodel one, Customlangmodel other) {
                Collator collator = Collator.getInstance(Locale.getDefault());
                collator.setStrength(1);
                return collator.compare(one.getName(), other.getName());
            }
        });

        int i = 0;
        for (Customlangmodel c : languages) {
            list.add(c);
            i++;
        }



        CustomBaseAdapter dataAdapter;

        switch (TextViewChack)
        {
            case "Tv1":
                dataAdapter = new CustomBaseAdapter(this, list, Share.FromPosLang);
                listview_translang.setAdapter(dataAdapter);
                break;

            case "Tv2":
                dataAdapter = new CustomBaseAdapter(this, list, Share.ToPosLang);
                listview_translang.setAdapter(dataAdapter);
                break;
        }


       /* CustomBaseAdapter dataAdapter = new CustomBaseAdapter(this, list);
        listview_translang.setAdapter(dataAdapter);*/

    }

    private void initViewListener() {
        iv_back.setOnClickListener(this);
        listview_translang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                /*switch (TextViewChack) {
                    case "Tv1":
                        Share.FromPosLang = position;
//                        SharedPrefs.FromPosLang = position;
                        SharedPrefs.save(activity, SharedPrefs.FromPosLang, position);
                        finish();
                        break;

                    case "Tv2":
                        Share.ToPosLang = position;
//                        SharedPrefs.ToPosLang = position;
                        SharedPrefs.save(activity, SharedPrefs.ToPosLang, position);
                        finish();
                        break;
                }*/
                switch (TextViewChack)
                {
                    case "Tv1":
                        if (Share.FromPosLang != position) {

                            Share.FromPosLang = position;
                            SharedPrefs.save(activity, SharedPrefs.FromPosLang, position);
                            finish();

                        }
                        else
                        {
                            Toast.makeText(activity, "Language already selected.", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case "Tv2":

                        if (Share.ToPosLang != position) {

                            Share.ToPosLang = position;
                            SharedPrefs.save(activity, SharedPrefs.ToPosLang, position);
                            finish();

                        }
                        else
                        {
                            Toast.makeText(activity, "Language already selected.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }

                Log.e("onItemClick: ", "position => " + position);
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

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
            if (Share.isNeedToAdShow(this)) {
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
}
