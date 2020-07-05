package com.calculator.vault.gallery.locker.hide.data.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import com.calculator.vault.gallery.locker.hide.data.adapter.NotesAdapter;
import com.calculator.vault.gallery.locker.hide.data.model.NoteListModel;
import com.google.android.gms.ads.AdView;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import java.util.ArrayList;
import java.util.Collections;

public class NoteActivity extends AppCompatActivity implements NotesAdapter.ItemOnClick, View.OnClickListener
        /*MoPubInterstitial.InterstitialAdListener*//*InterstitialAdHelper.onInterstitialAdListener*/ {

    private static final String TAG = "NoteActivity";
    private RecyclerView moRvNotesList;
    private NotesAdapter moNotesAdapter;
    private LinearLayout moLLaddNotes;
    private RelativeLayout moLLNoNotes;
    private ImageView moIvAdd;
    private ArrayList<NoteListModel> moNoteListArray = new ArrayList<>();
    private ImageVideoDatabase mdbImageVideoDatabase = new ImageVideoDatabase(this);
    private DecoyDatabase decoyDatabase = new DecoyDatabase(this);
    private String isDecode;
    private ImageView iv_back;
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
//        if (Utils.isNetworkConnected(NoteActivity.this)) {
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
//        if (Utils.isNetworkConnected(NoteActivity.this)) {
//            mInterstitial.load();
//        }
//        getNotes();
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
//            interstitial = InterstitialAdHelper.getInstance().load2(NoteActivity.this, this);
//        }else if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id2))){
//            interstitial = InterstitialAdHelper.getInstance().load(NoteActivity.this, this);
//        }else {
//            interstitial = InterstitialAdHelper.getInstance().load1(NoteActivity.this, this);
//        }
//    }
//
//    @Override
//    public void onClosed() {
//        isInterstitialAdLoaded = false;
//        interstitial = InterstitialAdHelper.getInstance().load1(NoteActivity.this, this);
//        getNotes();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

//        mInterstitial = new MoPubInterstitial(this, getString(R.string.mopub_int_id));
//        mInterstitial.setInterstitialAdListener(this);
//        if (Utils.isNetworkConnected(NoteActivity.this)) {
//            mInterstitial.load();
            IntAdsHelper.loadInterstitialAds(NoteActivity.this, new AdsListener() {
                @Override
                public void onLoad() {

                }

                @Override
                public void onClosed() {
                    getNotes();
                }
            });
//        }

//        TODO Admob Ads
//        interstitial = InterstitialAdHelper.getInstance().load1(NoteActivity.this, this);

//        IronSource.init(this, "8fb1d745", IronSource.AD_UNIT.INTERSTITIAL);
//        IronSource.shouldTrackNetworkState(this, true);
//        IronSource.setInterstitialListener(this);
//        IronSource.loadInterstitial();

        initView();
        isDecode = SharedPrefs.getString(NoteActivity.this, SharedPrefs.DecoyPassword, "false");
        setupList();
        initViewListener();

        //getNotes();
        initViewAction();
        adView = (AdView) findViewById(R.id.adView);
        Share.loadAdsBanner(NoteActivity.this, adView);

        // IronSource Banner Ads
        //ISAdsHelper.loadBannerAd(this,(FrameLayout) findViewById(R.id.flBanner));

        if (Share.isNeedToAdShow(NoteActivity.this)) {
//            NativeAdvanceHelper.loadGreedyGameAd(this, (FrameLayout) findViewById(R.id.fl_adplaceholder), "float-4028");
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void setupList() {

        if (isDecode.equals("true")) {
            moNoteListArray = decoyDatabase.getAllNotes();
        } else {
            moNoteListArray = mdbImageVideoDatabase.getAllNotes();
        }

        Collections.reverse(moNoteListArray);

        moNotesAdapter = new NotesAdapter(NoteActivity.this, moNoteListArray, NoteActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NoteActivity.this);

        moRvNotesList.setLayoutManager(mLayoutManager);
        moRvNotesList.setItemAnimator(new DefaultItemAnimator());
        moRvNotesList.addItemDecoration(new DividerItemDecoration(NoteActivity.this, DividerItemDecoration.VERTICAL));
        moRvNotesList.setAdapter(moNotesAdapter);
    }

    private void initViewAction() {
        //getNotes();
        loadInterAdAndGetNotes("init");
    }

    private void loadInterAdAndGetNotes(String fWhere) {

        if (fWhere.equalsIgnoreCase("init")) {
            getNotes();
        } else {
            if (Share.isNeedToAdShow(this)) {
                if (IntAdsHelper.isInterstitialLoaded()) {
                    IntAdsHelper.showInterstitialAd();
//                    TODO Admob Ads
//                    interstitial.show();
                } else {
                    getNotes();
                }
            } else {
                getNotes();
            }
        }
    }

    private void getNotes() {
        moNoteListArray.clear();
        if (isDecode.equals("true")) {
            moNoteListArray = decoyDatabase.getAllNotes();
        } else {
            moNoteListArray = mdbImageVideoDatabase.getAllNotes();
        }
        if (moNoteListArray.isEmpty()) {
            moLLNoNotes.setVisibility(View.VISIBLE);
            if (Share.isNeedToAdShow(NoteActivity.this)) {
                NativeAdvanceHelper.loadNativeAd(this, findViewById(R.id.fl_adplaceholder), true);
            }
        } else {
            findViewById(R.id.fl_adplaceholder).setVisibility(View.GONE);
            moLLNoNotes.setVisibility(View.GONE);
        }
        Collections.reverse(moNoteListArray);
        moNotesAdapter.onUpdate(moNoteListArray);
    }

    private void initViewListener() {
        moIvAdd.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        moLLaddNotes.setOnClickListener(this);
    }

    private void initView() {
        moIvAdd = findViewById(R.id.iv_add_notes);
        moRvNotesList = findViewById(R.id.rv_notesList);
        moLLaddNotes = findViewById(R.id.ll_add_notes);
        moLLNoNotes = findViewById(R.id.ll_noNotes);
        iv_back = findViewById(R.id.iv_back);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //IronSource.onResume(this);
        if (!isFirstTime) {
            loadInterAdAndGetNotes("onResume");
        } else {
            isFirstTime = false;
        }
        getNotes();
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
    public void onClick(int position, NoteListModel itemModel) {
        if (isDecode.equals("true")) {
            decoyDatabase.removeSingleNote(moNoteListArray.get(position).getID());
        } else {
            mdbImageVideoDatabase.removeSingleNote(moNoteListArray.get(position).getID());
        }
        //getNotes();
        loadInterAdAndGetNotes("onClick");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_add_notes:
                Intent loIntent = new Intent(this, NoteTextActivity.class);
                startActivity(loIntent);
                break;
            case R.id.iv_add_notes:
                Intent iIntent = new Intent(this, NoteTextActivity.class);
                startActivity(iIntent);
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }
}
