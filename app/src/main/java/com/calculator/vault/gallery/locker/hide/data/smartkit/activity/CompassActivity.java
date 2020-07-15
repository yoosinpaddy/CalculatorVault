package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.calculator.vault.gallery.locker.hide.data.smartkit.MainApplication;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.SharedPrefs;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {

    ImageView compass_img,iv_back;
    int mAzimuth;
    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;
    boolean haveSensor = false, haveSensor2 = false;
    float[] rMat = new float[9];
    float[] orientation = new float[3];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ImageView iv_more_app, iv_blast;
    private ImageView iv_swipe_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_compass);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        compass_img = (ImageView) findViewById(R.id.imageViewCompass);
        iv_back = findViewById(R.id.iv_back);
        iv_more_app = findViewById(R.id.iv_more_app);
        iv_blast = findViewById(R.id.iv_blast);
        iv_swipe_show = findViewById(R.id.iv_swipe_show);


        Share.overCompass = SharedPrefs.getBoolean(CompassActivity.this,"overShowCompass",false);

        if (!Share.overCompass){
            iv_swipe_show.setVisibility(View.VISIBLE);
            SharedPrefs.savePref(CompassActivity.this,"overShowCompass",true);
        }else{
            iv_swipe_show.setVisibility(View.GONE);
            SharedPrefs.savePref(CompassActivity.this,"overShowCompass",true);
        }

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        start();

        if (Share.isNeedToAdShow(this)){
            NativeAdvanceHelper.loadAdBannerSize(this, (FrameLayout) findViewById(R.id.fl_adplaceholder));
            setActionBar();
        }


        iv_swipe_show.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                iv_swipe_show.setVisibility(View.GONE);
                return false;
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(rMat, orientation);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        mAzimuth = Math.round(mAzimuth);
        compass_img.setRotation(-mAzimuth);

        String where = "NW";

        if (mAzimuth >= 350 || mAzimuth <= 10)
            where = "N";
        if (mAzimuth < 350 && mAzimuth > 280)
            where = "NW";
        if (mAzimuth <= 280 && mAzimuth > 260)
            where = "W";
        if (mAzimuth <= 260 && mAzimuth > 190)
            where = "SW";
        if (mAzimuth <= 190 && mAzimuth > 170)
            where = "S";
        if (mAzimuth <= 170 && mAzimuth > 100)
            where = "SE";
        if (mAzimuth <= 100 && mAzimuth > 80)
            where = "E";
        if (mAzimuth <= 80 && mAzimuth > 10)
            where = "NE";


        Log.e("direction", "onSensorChanged: "+ where );
        //txt_compass.setText(mAzimuth + "° " + where);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
                noSensorsAlert();
            }
            else {
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
            }
        }
        else{
            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void noSensorsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device doesn't support the Compass.")
                .setCancelable(false)
                .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alertDialog.show();
    }

    public void stop() {
        if (haveSensor) {
            mSensorManager.unregisterListener(this, mRotationV);
        }
        else {
            mSensorManager.unregisterListener(this, mAccelerometer);
            mSensorManager.unregisterListener(this, mMagnetometer);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
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