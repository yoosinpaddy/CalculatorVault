package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.calculator.vault.gallery.locker.hide.data.smartkit.MainApplication;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.SharedPrefs;

import java.util.Locale;
import java.util.Objects;

public class WordToSpeakActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    protected WordToSpeakActivity activity;
    protected ImageView spellhelper_iv_back, spellhelper_iv_volume;
    protected EditText spellhelper_et_user_voice_text;
    TextToSpeech txttospeech;
    private SeekBar volumeSeekbar = null;
    private SeekBar skb_speed = null;
    private AudioManager audioManager = null;
    protected boolean isAdLoading = false;
    private float speed;
    private int speedval;
    private int prog = 50;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ImageView iv_more_app, iv_blast;
    private boolean isKeyboardShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_word_to_speak);
        activity = WordToSpeakActivity.this;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initView();
        initViewAction();
        initViewListener();

        final View rootView = findViewById(R.id.rl_view);


        rootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        rootView.getWindowVisibleDisplayFrame(r);
                        int screenHeight = rootView.getRootView().getHeight();

// r.bottom is the position above soft keypad or device button.
// if keypad is shown, the r.bottom is smaller than that before.
                        int keypadHeight = screenHeight - r.bottom;

                        Log.d("tag", "keypadHeight = " + keypadHeight);

                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
// keyboard is opened
                            // TODO: 2019-08-26 Check keybordopen or hide  
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = true;
                                rootView.findViewById(R.id.fl_adplaceholder).setVisibility(View.GONE);
                            }
                        } else {
// keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                rootView.findViewById(R.id.fl_adplaceholder).setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });


        prog = SharedPrefs.getInt(activity, "volume", 50);
        speed = SharedPrefs.getFloat(activity, "speed", 1.0f);
        speedval = SharedPrefs.getInt(activity, "speedval", 50);
        Log.e("speed", "onCreate: speed-->" + speed);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        initControls();

        if (Share.isNeedToAdShow(this)) {
            NativeAdvanceHelper.loadAdBannerSize(activity, (FrameLayout) findViewById(R.id.fl_adplaceholder));
            setActionBar();
        }
    }

    private void initControls() {
        try {

            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    prog, 0);

            skb_speed.setProgress(speedval);
            txttospeech.setSpeechRate(speed);


            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    prog = progress;
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            prog, 0);
                    SharedPrefs.save(activity, "volume", progress);

                }
            });

            skb_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //txttospeech.setSpeechRate(progress);
                    speed = (float) progress / (seekBar.getMax() / 2);
                    SharedPrefs.save(activity, "speed", speed);
                    SharedPrefs.save(activity, "speedval", progress);
                    txttospeech.setSpeechRate(speed);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        spellhelper_et_user_voice_text = findViewById(R.id.spellhelper_et_user_voice_text);
        spellhelper_iv_back = findViewById(R.id.iv_back);
        spellhelper_iv_volume = findViewById(R.id.spellhelper_iv_volume);
        volumeSeekbar = (SeekBar) findViewById(R.id.skb_volume);
        skb_speed = (SeekBar) findViewById(R.id.skb_speed);
        iv_more_app = findViewById(R.id.iv_more_app);
        iv_blast = findViewById(R.id.iv_blast);

    }

    private void initViewAction() {
        txttospeech = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != -1) {
                    txttospeech.setLanguage(Locale.getDefault());
                }
            }
        });
    }

    private void initViewListener() {
        spellhelper_iv_volume.setOnClickListener(this);
        spellhelper_iv_back.setOnClickListener(this);
        spellhelper_et_user_voice_text.setOnTouchListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.spellhelper_iv_volume:
                if (!spellhelper_et_user_voice_text.getText().toString().isEmpty()) {
                    spellhelper_et_user_voice_text.setVisibility(View.VISIBLE);
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {

                    }
                }
                onMicClicked();
                break;

            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }


    public void onMicClicked() {
        if (spellhelper_et_user_voice_text.getText().toString().isEmpty()) {
            Toast.makeText(activity, "Please Enter Some Word", Toast.LENGTH_SHORT).show();
//            txttospeech.speak("Please Enter Some Word", 0, null);
        } else {
            txttospeech.speak(spellhelper_et_user_voice_text.getText().toString(), 0, null);
        }
    }

    public void onCopyClicked() {
        CopyToClipboard(spellhelper_et_user_voice_text.getText().toString());
    }

    @SuppressLint({"WrongConstant", "NewApi"})
    private void CopyToClipboard(String str) {
        ((ClipboardManager) Objects.requireNonNull(getSystemService("clipboard"))).setPrimaryClip(ClipData.newPlainText("123 Correct Spelling", str));
        String builder = "'" + str + "' copied to clipboard";
        Toast.makeText(activity, builder, Toast.LENGTH_LONG).show();
    }

    private void ShareText() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = spellhelper_et_user_voice_text.getText().toString();
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        spellhelper_et_user_voice_text.getParent().requestDisallowInterceptTouchEvent(true);
        return false;
    }

    @Override
    public void onBackPressed() {
        txttospeech.stop();
        super.onBackPressed();

    }

    @Override
    protected void onPause() {
        txttospeech.stop();
        super.onPause();
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
