package com.calculator.vault.gallery.locker.hide.data.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.ads.AdsListener;
import com.calculator.vault.gallery.locker.hide.data.ads.IntAdsHelper;
import com.calculator.vault.gallery.locker.hide.data.common.OnSingleClickListener;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.common.Utils;
import com.calculator.vault.gallery.locker.hide.data.data.DecoyDatabase;
import com.calculator.vault.gallery.locker.hide.data.data.ImageVideoDatabase;
import com.calculator.vault.gallery.locker.hide.data.model.NoteListModel;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteEditActivity extends AppCompatActivity implements View.OnClickListener
        /*MoPubInterstitial.InterstitialAdListener*//*InterstitialAdHelper.onInterstitialAdListener*/ {

    private static final String TAG = "NoteEditActivity";
    EditText moetNoteText, moEtNoteTitle;
    String msNoteText;
    TextView moLlSaveNote,btnClearNotes;
    NoteListModel moNoteListModel;
    private int miID;
    ImageView iv_back;
    ImageVideoDatabase imageVideoDatabase = new ImageVideoDatabase(this);
    private DecoyDatabase decoyDatabase= new DecoyDatabase(this);
    private String isDecode;

    private ImageView moIvMoreApp, moIvBlast;

//    private boolean isInterstitialAdLoaded = false;
//    private MoPubInterstitial mInterstitial;
//    TODO Admob Ads
//    private InterstitialAd interstitial;

//    @Override
//    public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {
//        if (Share.isNeedToAdShow(NoteEditActivity.this)) {
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
//        if (Utils.isNetworkConnected(NoteEditActivity.this)) {
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
//        if (Utils.isNetworkConnected(NoteEditActivity.this)) {
//            mInterstitial.load();
//        }
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//    }

//    TODO Admob Ads
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
//            interstitial = InterstitialAdHelper.getInstance().load2(NoteEditActivity.this, this);
//        }else if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id2))){
//            interstitial = InterstitialAdHelper.getInstance().load(NoteEditActivity.this, this);
//        }else {
//            interstitial = InterstitialAdHelper.getInstance().load1(NoteEditActivity.this, this);
//        }
//
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onClosed() {
//        isInterstitialAdLoaded = false;
//        interstitial = InterstitialAdHelper.getInstance().load1(NoteEditActivity.this, this);
//
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

//        mInterstitial = new MoPubInterstitial(this, getString(R.string.mopub_int_id));
//        mInterstitial.setInterstitialAdListener(this);
//        if (Utils.isNetworkConnected(NoteEditActivity.this)) {
//            mInterstitial.load();
            IntAdsHelper.loadInterstitialAds(this, new AdsListener() {
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
//        interstitial = InterstitialAdHelper.getInstance().load1(NoteEditActivity.this, this);

        initView();
        isDecode= SharedPrefs.getString(NoteEditActivity.this,SharedPrefs.DecoyPassword,"false");
        initViewListener();
        initViewAction();
    }

    private void initViewAction() {
        miID = getIntent().getIntExtra("editIntentID", 0);

        if(isDecode.equals("true")){
            moNoteListModel = decoyDatabase.getSingleNoteData(miID);
        }else{
            moNoteListModel = imageVideoDatabase.getSingleNoteData(miID);
        }

        moEtNoteTitle.setText(moNoteListModel.getNoteTitle());
        moetNoteText.setText(moNoteListModel.getNote_text());
        moetNoteText.setSelection(moetNoteText.getText().length());

        findViewById(R.id.iv_copy_title).setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!moEtNoteTitle.getText().toString().trim().isEmpty())
                Utils.copyToClipBoard(NoteEditActivity.this, "Title", moEtNoteTitle.getText().toString());
            }
        });

        findViewById(R.id.iv_copy_notes).setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!moetNoteText.getText().toString().trim().isEmpty())
                Utils.copyToClipBoard(NoteEditActivity.this, "Notes", moetNoteText.getText().toString());
            }
        });

    }

    private void initViewListener() {
        moLlSaveNote.setOnClickListener(this);
        btnClearNotes.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        moIvMoreApp.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (Share.isNeedToAdShow(NoteEditActivity.this)) {
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
        moEtNoteTitle = findViewById(R.id.et_note_title);
        moetNoteText = findViewById(R.id.et_editnoteText);
        moLlSaveNote = findViewById(R.id.ll_save_notes);
        btnClearNotes = findViewById(R.id.btnClearNotesE);
        iv_back = findViewById(R.id.iv_back);
        moNoteListModel = new NoteListModel();

        // Gift Ads
        moIvMoreApp = findViewById(R.id.iv_more_app);
        moIvBlast = findViewById(R.id.iv_blast);
        moIvMoreApp.setVisibility(View.GONE);
        moIvMoreApp.setBackgroundResource(R.drawable.animation_list_filling);
        ((AnimationDrawable) moIvMoreApp.getBackground()).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_save_notes:

                if (TextUtils.isEmpty(moEtNoteTitle.getText().toString().trim())) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Note title field cannot be Empty");
                    alertDialogBuilder.setNegativeButton("OK", (dialog, which) -> dialog.dismiss());

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else if (TextUtils.isEmpty(moetNoteText.getText().toString().trim())) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Note description cannot be Empty");
                    alertDialogBuilder.setNegativeButton("OK", (dialog, which) -> dialog.dismiss());

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    UpdateNotes();
                    finish();
                }
                break;

            case R.id.btnClearNotesE:
                moEtNoteTitle.setText("");
                moetNoteText.setText("");
                break;

                case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private void UpdateNotes() {
        Date date = new Date();
        String sdate = date.toString();
        SimpleDateFormat loSimpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String format = loSimpleDateFormat.format(new Date());
        Log.e("TAG:: ", "initViewAction: " + format);
        moNoteListModel.setTimestamp(format);
        moNoteListModel.setNoteTitle(moEtNoteTitle.getText().toString().trim());
        moNoteListModel.setNote_text(moetNoteText.getText().toString().trim());
        if(isDecode.equals("true")){
            decoyDatabase.updateSingleNote(moNoteListModel);
        }else{
            imageVideoDatabase.updateSingleNote(moNoteListModel);
        }
        onBackPressed();

    }
}
