package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;
import com.calculator.vault.gallery.locker.hide.data.smartkit.custom.DynamicSineWaveView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RecorderActivity extends AppCompatActivity implements View.OnClickListener {


    private String TAG = "RecorderActivity";
    Button btn_start, btn_stop;
    DynamicSineWaveView wavesView;
    float stroke;
    RecorderActivity activity;

    //Recorder
    private int RECORD_AUDIO_REQUEST_CODE = 123;

    private Chronometer chronometer;
    // private ImageView  imageViewPlay;
    //private SeekBar seekBar;
    // private LinearLayout linearLayoutRecorder;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private String fileName = null;
    private int lastProgress = 0;
    private Handler mHandler = new Handler();
    private boolean isPlaying = false;
    ImageView iv_back;
    ImageView btn_list_show;
    private List<String> listPermissionsNeeded = new ArrayList<>();
    private final int STORAGE_PERMISSION_CODE = 23;
    private FirebaseAnalytics mFirebaseAnalytics;
    private boolean frecoderstop = false;
    private int total_record_duration = 0;
    long startTime;
    boolean doubleBackToExitPressedOnce = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_recorder);

        activity = RecorderActivity.this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);

        init();
        initAction();

        if (Share.isNeedToAdShow(activity)) {
            NativeAdvanceHelper.loadAdBannerSize(activity, (FrameLayout) findViewById(R.id.fl_adplaceholder));
        }
        //initializingViews

        stroke = dipToPixels(activity, 1);

    }

    private void init() {
        wavesView = findViewById(R.id.wavesView);
        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);
        btn_list_show = findViewById(R.id.btn_list_show);
        iv_back = findViewById(R.id.iv_back);


        // linearLayoutRecorder = (LinearLayout) findViewById(R.id.linearLayoutRecorder);
        chronometer = (Chronometer) findViewById(R.id.chronometerTimer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        // imageViewPlay = (ImageView) findViewById(R.id.imageViewPlay);
        // linearLayoutPlay = (LinearLayout) findViewById(R.id.linearLayoutPlay);
        // seekBar = (SeekBar) findViewById(R.id.seekBar);
    }

    private void initAction() {
        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_list_show.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }


    private boolean checkAndRequestPermissions(int code) {

        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, code);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_PERMISSION_CODE:
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {

                    } else {
                        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                            alertDialogBuilder.setTitle("Permissions Required")
                                    .setMessage("Please allow permission for storage")
                                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                    Uri.fromParts("package", getPackageName(), null));
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    })
                                    .setCancelable(false)
                                    .create()
                                    .show();
                        } else if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                            alertDialogBuilder.setTitle("Permissions Required")
                                    .setMessage("Please allow permission for microphone")
                                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                    Uri.fromParts("package", getPackageName(), null));
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    })
                                    .setCancelable(false)
                                    .create()
                                    .show();
                        }

                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                if (checkAndRequestPermissions(STORAGE_PERMISSION_CODE)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        prepareforRecording();
                    }
                    startRecording();

                    wavesView.addWave(0.4f, 0.4f, 0, 0, 0); // Fist wave is for the shape of other waves.
                    wavesView.addWave(0.5f, 2f, 0.5f, getResources().getColor(android.R.color.white), stroke);
                    wavesView.setBaseWaveAmplitudeScale(3);
                    wavesView.startAnimation();

                    btn_start.setVisibility(View.GONE);
                    btn_stop.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.btn_stop:

                long estimatedTime = System.nanoTime() - startTime;
                total_record_duration = (int) (estimatedTime / 1e+6);
                Log.e(TAG, "Recorder----> " + total_record_duration);

                if (total_record_duration > 1000) {
                    if (frecoderstop) {
                        frecoderstop = false;
                        stopRecording();
                        prepareforStop();
                        Toast.makeText(activity, "Recording saved successfully.", Toast.LENGTH_SHORT).show();
                        wavesView.stopAnimation();
                        btn_stop.setVisibility(View.GONE);
                        btn_start.setVisibility(View.VISIBLE);
                    }
                } else {
                    //Toast.makeText(activity, "Recording must be atleast 00:01 second.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(activity, "You must have atleast 00:01 second record data.", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.btn_list_show:
                if (mPlayer != null) {
                    stopPlaying();
                }

                Log.e(TAG, "onClick: frecoderstop-->" + frecoderstop);
                if (frecoderstop) {
                    Toast.makeText(activity, "Please first stop recording", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(activity, RecordingListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                break;

            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }


    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void prepareforRecording() {
        //TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        btn_start.setVisibility(View.GONE);
        btn_stop.setVisibility(View.VISIBLE);
        // linearLayoutPlay.setVisibility(View.GONE);
    }


    private void startRecording() {
        //we use the MediaRecorder class to record
        mRecorder = new MediaRecorder();

        startTime = System.nanoTime();

        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        /**In the lines below, we create a directory named VoiceRecorderSimplifiedCoding/Audios in the phone storage
         * and the audios are being stored in the Audios folder **/
        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/SmartKit360/Record");
        if (!file.exists()) {
            file.mkdirs();
        }

        fileName = root.getAbsolutePath() + "/SmartKit360/Record/" + (getDateTime() + ".mp3");
        Log.d("filename", fileName);
        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
            doubleBackToExitPressedOnce = false;
            frecoderstop = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastProgress = 0;
        //seekBar.setProgress(0);
        stopPlaying();
        //starting the chronometer
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    private String getDateTime() {
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        String dateToStr = format.format(today);
        System.out.println(dateToStr);
        return dateToStr;
    }


    private void stopPlaying() {
        try {

            if (mPlayer != null)
                mPlayer.release();
            doubleBackToExitPressedOnce = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPlayer = null;
        //showing the play button
        //imageViewPlay.setImageResource(R.drawable.ic_launcher_background);
        chronometer.stop();
    }

    @Override
    public void onBackPressed() {
        if (mRecorder != null) {
            if (frecoderstop) {
                activity.doubleBackToExitPressedOnce = false;
            } else {
                frecoderstop = false;
                stopRecording();
                activity.doubleBackToExitPressedOnce = true;
            }
        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        } else {

            Toast.makeText(activity, "Please first stop recording", Toast.LENGTH_SHORT).show();
        }

        if (mPlayer != null) {
            stopPlaying();
        }


    }

    private void prepareforStop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // TransitionManager.beginDelayedTransition(linearLayoutRecorder);
        }
        btn_start.setVisibility(View.VISIBLE);
        btn_stop.setVisibility(View.GONE);
        //linearLayoutPlay.setVisibility(View.VISIBLE);
    }

    private void stopRecording() {

        try {
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder.release();
                doubleBackToExitPressedOnce = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mRecorder = null;
        mRecorder = new MediaRecorder();
        //starting the chronometer
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        //showing the play button
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };

    private void seekUpdation() {
        if (mPlayer != null) {
            int mCurrentPosition = mPlayer.getCurrentPosition();
            //seekBar.setProgress(mCurrentPosition);
            lastProgress = mCurrentPosition;
        }
        mHandler.postDelayed(runnable, 100);
    }


}

