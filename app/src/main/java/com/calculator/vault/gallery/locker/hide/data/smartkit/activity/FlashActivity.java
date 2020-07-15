package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.calculator.vault.gallery.locker.hide.data.smartkit.MainApplication;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;
import com.calculator.vault.gallery.locker.hide.data.smartkit.utils.StrobeRunner;
import com.triggertrap.seekarc.SeekArc;

public class FlashActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity activity;
    private static StrobeRunner runner;
    private static Thread thread;
    private TextView textViewOn, textViewOff, textViewFreq;
    private SeekBar seekbarOn, seekbarOff, seekbarFreq;
    private static ImageView iv_flash_off;
    private static ImageView iv_flash_on, iv_back;
    private static boolean isFlashOn = true;
    private double frequency;
    private final int maxSeekDelay = 1090;
    private final int maxSeekFreq = 109;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ImageView iv_more_app, iv_blast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_flash);

        activity = FlashActivity.this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        initView();
        initViewAction();

        if (Share.isNeedToAdShow(this)) {
            setActionBar();
        }

    }


    private void initView() {

        iv_flash_off = (ImageView) findViewById(R.id.iv_flash_off);
        iv_flash_on = (ImageView) findViewById(R.id.iv_flash_on);

        textViewOn = (TextView) findViewById(R.id.TextViewOn);
        textViewOff = (TextView) findViewById(R.id.TextViewOff);
        textViewFreq = (TextView) findViewById(R.id.textViewFreq);

        seekbarOn =  findViewById(R.id.SeekBarOn);
        seekbarOff = findViewById(R.id.SeekBarOff);
        seekbarFreq = findViewById(R.id.SeekBarFreq);
        iv_back = findViewById(R.id.iv_back);
        iv_more_app = findViewById(R.id.iv_more_app);
        iv_blast = findViewById(R.id.iv_blast);

        iv_flash_on.setOnClickListener(this);
        iv_flash_off.setOnClickListener(this);
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

    @SuppressLint("WrongConstant")
    private void initViewAction() {
        runner = StrobeRunner.getInstance();
        runner.controller = activity;

        seekbarOn.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    setSeekbarOnProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /*seekbarOn.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int progress, boolean fromUser) {
                if (fromUser) {
                    setSeekbarOnProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {

            }

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {

            }
        });*/
        setTextSpeedOn(runner.delayOn);
        seekbarOn.setProgress(delayToSeek(runner.delayOn));

        seekbarOff.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    setSeekbarOffProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /*seekbarOff.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int progress, boolean fromUser) {
                if (fromUser) {
                    setSeekbarOffProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {

            }

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {

            }
        });*/
        setTextSpeedOff(runner.delayOff);
        seekbarOff.setProgress(delayToSeek(runner.delayOff));

        seekbarFreq.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    setSeekbarFreqProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /*seekbarFreq.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int progress, boolean fromUser) {
                if (fromUser) {
                    setSeekbarFreqProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {

            }

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {

            }
        });*/
        // init
        frequency = freqFromDelays((float) runner.delayOff, (float) runner.delayOn);
        setTextFreq(frequency);
        seekbarFreq.setProgress(freqToSeek(frequency));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_flash_off:
                turnOffFlash();
                break;

            case R.id.iv_flash_on:
                turnOnFlash();
                break;

            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    public static void turnOnFlash() {
        iv_flash_off.setVisibility(View.VISIBLE);
        iv_flash_on.setVisibility(View.INVISIBLE);

        runner.requestStop = true;

        isFlashOn = true;

    }

    public static void turnOffFlash() {
        if (iv_flash_off != null) {
            iv_flash_off.setVisibility(View.INVISIBLE);
            iv_flash_on.setVisibility(View.VISIBLE);
        }

        thread = new Thread(runner);

        isFlashOn = false;

        thread.start();
    }

    private void setSeekbarFreqProgress(int progress) {
        frequency = seekToFreq(progress);

        // avoid divide by 0
        if (frequency <= 0)
            frequency = 1;
        if (runner.delayOn <= 0)
            runner.delayOn = 1;

        setTextFreq(frequency);

        final double prevRatio = runner.delayOff / runner.delayOn;
        final double newOffShare = (prevRatio / (prevRatio + 1));
        final double newOnShare = 1 - newOffShare;
        final double newTotalDelay = 1000 / frequency; // ms
        runner.delayOff = newTotalDelay * newOffShare;
        runner.delayOn = newTotalDelay * newOnShare;

        setTextSpeedOff(runner.delayOff);
        setTextSpeedOn(runner.delayOn);

        seekbarOff.setProgress(delayToSeek(runner.delayOff));
        seekbarOn.setProgress(delayToSeek(runner.delayOn));
    }

    private void setSeekbarOffProgress(int progress) {
        runner.delayOff = seekToDelay(progress);
        setTextSpeedOff(runner.delayOff);

        frequency = freqFromDelays(runner.delayOff, runner.delayOn);
        setTextFreq(frequency);
        seekbarFreq.setProgress(freqToSeek(frequency));
    }

    private void setSeekbarOnProgress(int progress) {
        runner.delayOn = seekToDelay(progress);
        setTextSpeedOn(runner.delayOn);

        frequency = freqFromDelays(runner.delayOff, runner.delayOn);
        setTextFreq(frequency);
        seekbarFreq.setProgress(freqToSeek(frequency));
    }

    private void setTextSpeedOff(double speed) {
        textViewOff.setText(String.format("%.1f ms", speed));
    }

    private void setTextSpeedOn(double speed) {
        textViewOn.setText(String.format("%.1f ms", speed));
    }

    private void setTextFreq(double freq) {
        textViewFreq.setText(String.format("%.1f Hz", freq));

    }

    private double seekToFreq(int seek) {
        double freq;

        // check
        if (seek < 0)
            seek = 0;

        // input 0 to 9
        // output 0.1 to 1
        if (seek <= 9) {
            freq = 0.1 + ((double) seek / 10);
        }
        // input 10 to 109
        // output 1 to 100
        else {
            freq = 1 + ((double) seek - 10);
        }

        return freq;
    }

    private int freqToSeek(double freq) {
        int seek;

        // input 0 to 1
        // output 0 to 9
        if (freq <= 0.94)
            seek = (int) Math.round(freq) * 10;
            // input 1 to 100
            // output 10 to 109
        else
            seek = (int) Math.round(freq) + 9;

        // check
        if (seek < 0)
            seek = 0;
        if (seek > maxSeekFreq)
            seek = maxSeekFreq;

        return seek;
    }

    private double seekToDelay(int seek) {
        double delay;

        // check
        if (seek < 0)
            seek = 0;

        // input 0 to 999
        // output 0 to 999
        if (seek <= 1000) {
            delay = seek;
        }
        // input 1000 to 1090
        // output 1000 to 10000
        else {
            delay = 1000 + ((seek - 1000) * 100);
        }

        return delay;
    }

    private int delayToSeek(double delay) {
        int seek;

        // input 0 to 999
        // output 0 to 999
        if (delay <= 1000) {
            seek = (int) Math.round(delay);
        }
        // input 1000 to 10000
        // output 1000 to 1090
        else {
            seek = 1000 + (((int) Math.round(delay) - 1000) / 100);
        }

        // check
        if (seek < 0)
            seek = 0;
        if (seek > maxSeekDelay)
            seek = maxSeekDelay;

        return seek;
    }

    private double freqFromDelays(double delayOff, double delayOn) {
        double freq;
        if ((delayOff + delayOn) <= 0)
            freq = 0;
        else
            freq = 1000 / (delayOff + delayOn);
        return freq;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("AlertService-4", "onResume -> ");
    }

    @Override
    public void onStop() {
        runner.requestStop = true;
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("AlertService-5", "onDestroy -> FlashFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        turnOnFlash();
        Log.e("AlertService-6", "onPause -> FlashFragment");
    }

}
