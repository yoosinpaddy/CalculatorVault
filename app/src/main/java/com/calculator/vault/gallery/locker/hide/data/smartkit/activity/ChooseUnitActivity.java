package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.calculator.vault.gallery.locker.hide.data.smartkit.MainApplication;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.adapter.UnitChangeListAdapter;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.UnitModel;

import java.util.ArrayList;

public class ChooseUnitActivity extends AppCompatActivity {

    Boolean is_closed = true;
    private String title, button;
    private String TAG = ChooseUnitActivity.class.getSimpleName();
    private String[] unitFullNamesArray;
    private String[] unitNamesArray;
    public static ArrayList<UnitModel> unitChangeModels;

    public static RecyclerView rv_unit_list;
    private EditText etSearch;
    private LinearLayout ll_main;
    public static TextView tv_no_match_found;

    private UnitChangeListAdapter unitChangeListAdapter;
    private ChooseUnitActivity activity;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ImageView iv_more_app, iv_blast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_choose_unit);

        activity = ChooseUnitActivity.this;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        title = getIntent().getStringExtra("title");
        button = getIntent().getStringExtra("button");

        setToolbar();
        findViews();

        if (Share.isNeedToAdShow(this)){
            setActionBar();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setToolbar() {
        ImageView iv_back = findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: onBackPressed");
                onBackPressed();
            }
        });
    }


    /*private void loadInterstialAd() {
        if (MainApplication.getInstance().mInterstitialAd.isLoaded()) {
            Log.e("if", "if");
            iv_more_app.setVisibility(View.VISIBLE);
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
                    iv_more_app.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    Log.e("fail", "fail");
                    iv_more_app.setVisibility(View.GONE);
                    //MainApplication.getInstance().LoadAds();
                    loadInterstialAd();
                }
            });
        }
    }*/

    private void findViews() {
        rv_unit_list = findViewById(R.id.rv_unit_list);
        etSearch = findViewById(R.id.etSearch);
        ll_main = findViewById(R.id.ll_main);
        tv_no_match_found = findViewById(R.id.tv_no_match_found);
        iv_more_app = findViewById(R.id.iv_more_app);
        iv_blast = findViewById(R.id.iv_blast);

        hideKeyboard(ll_main);

        rv_unit_list.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_unit_list.setLayoutManager(layoutManager);

        unitFullNamesArray = getIntent().getStringArrayExtra("UnitFullNames");
        unitNamesArray = getIntent().getStringArrayExtra("UnitNames");

        Log.e(TAG, "findViews: unitNamesArray::" + unitNamesArray);

        if (unitChangeModels != null)
            unitChangeModels.clear();
        unitChangeModels = prepareData();
        unitChangeListAdapter = new UnitChangeListAdapter(activity, title, button, unitChangeModels, new UnitChangeListAdapter.onItemCLickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        rv_unit_list.setAdapter(unitChangeListAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                unitChangeListAdapter.getFilter().filter(s.toString().trim());
                if (unitChangeListAdapter.mUnitModels.size() == 0) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }
            }
        });

    }

    private void hideKeyboard(LinearLayout linearLayout) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(linearLayout.getWindowToken(), 0);
    }


    private ArrayList<UnitModel> prepareData() {
        ArrayList<UnitModel> converterModels = new ArrayList<>();
        for (int i = 0; i < unitFullNamesArray.length; i++) {
            UnitModel UnitModel = new UnitModel();
            UnitModel.setUnitFullName(unitFullNamesArray[i]);
            UnitModel.setUnitName(unitNamesArray[i]);
            converterModels.add(UnitModel);
        }
        return converterModels;
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
}
