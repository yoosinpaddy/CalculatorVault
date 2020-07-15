package com.calculator.vault.gallery.locker.hide.data.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.ads.AdsListener;
import com.calculator.vault.gallery.locker.hide.data.ads.IntAdsHelper;
import com.calculator.vault.gallery.locker.hide.data.ads.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.common.OnSingleClickListener;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.common.Utils;
import com.calculator.vault.gallery.locker.hide.data.data.DecoyDatabase;
import com.calculator.vault.gallery.locker.hide.data.data.ImageVideoDatabase;
import com.calculator.vault.gallery.locker.hide.data.model.UserModel;
import com.google.android.gms.ads.AdView;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static com.calculator.vault.gallery.locker.hide.data.common.Share.databasewritepath;

public class DecoyPasscodeActivity extends AppCompatActivity implements View.OnClickListener
        /*MoPubInterstitial.InterstitialAdListener*//*InterstitialAdHelper.onInterstitialAdListener*/ {

    private SwitchCompat sw_decoypasscode;
    private String isswitchActive = "false";
    private String TAG = this.getClass().getSimpleName();
    private List<String> listPermissionsNeeded = new ArrayList<>();
    public final int STORAGE_PERMISSION_CODE = 23;
    private DecoyDatabase decoyDatabase = new DecoyDatabase(this);
    ImageVideoDatabase moDBimageVideoDatabase = new ImageVideoDatabase(this);
    private RelativeLayout moRlChangePassword;

    private ImageView iv_back;
    private AdView adView;
    private boolean isFromNo = false;
    private boolean isFromChanegPass = false;
    private ImageView moIvMoreApp, moIvBlast;

//    private boolean isInterstitialAdLoaded = false;
//    private MoPubInterstitial mInterstitial;
    //    TODO Admob Ads
//    private InterstitialAd interstitial;
    private boolean fromAdToResume = false;

    //    TODO Mopub Ads
//    @Override
//    public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {
//        if (Share.isNeedToAdShow(DecoyPasscodeActivity.this)) {
//            isInterstitialAdLoaded = true;
//            moIvBlast.setVisibility(View.GONE);
//            moIvMoreApp.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {
//        Log.e(TAG, "onInterstitialFailed: " + moPubErrorCode);
//        isInterstitialAdLoaded = false;
//        if (Utils.isNetworkConnected(DecoyPasscodeActivity.this)) {
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
//        if (Utils.isNetworkConnected(DecoyPasscodeActivity.this)) {
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
//            interstitial = InterstitialAdHelper.getInstance().load2(DecoyPasscodeActivity.this, this);
//        }else if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id2))){
//            interstitial = InterstitialAdHelper.getInstance().load(DecoyPasscodeActivity.this, this);
//        }else {
//            interstitial = InterstitialAdHelper.getInstance().load1(DecoyPasscodeActivity.this, this);
//        }
//
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onClosed() {
//        isInterstitialAdLoaded = false;
//        interstitial = InterstitialAdHelper.getInstance().load1(DecoyPasscodeActivity.this, this);
//
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//
//    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode_passcode);

//        TODO Mopub Ads
//        mInterstitial = new MoPubInterstitial(this, getString(R.string.mopub_int_id));
//        mInterstitial.setInterstitialAdListener(this);

//            mInterstitial.load();
            IntAdsHelper.loadInterstitialAds(DecoyPasscodeActivity.this, new AdsListener() {
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


//        TODO Admob Ads
//        interstitial = InterstitialAdHelper.getInstance().load1(DecoyPasscodeActivity.this, this);

        System.gc();
        Runtime.getRuntime().gc();

        initView();

        isswitchActive = SharedPrefs.getString(DecoyPasscodeActivity.this, SharedPrefs.isDecodeSwitchActive, "false");
        Log.e(TAG, "onCreate: " + isswitchActive);

        File file = new File(Share.msPathToWriteDecoy);
        if (file.exists()) {
            if (isswitchActive.equals("false")) {
                sw_decoypasscode.setChecked(false);
                moRlChangePassword.setVisibility(View.GONE);
                findViewById(R.id.view_change_pass).setVisibility(View.GONE);
                Share.isSwitchNeedToBeOn = false;
            } else {
                sw_decoypasscode.setChecked(true);
                Share.isSwitchNeedToBeOn = true;
                moRlChangePassword.setVisibility(View.VISIBLE);
                findViewById(R.id.view_change_pass).setVisibility(View.VISIBLE);
            }
        } else {
            if (isswitchActive.equals("false")) {
                sw_decoypasscode.setChecked(false);
                Share.isSwitchNeedToBeOn = false;
                moRlChangePassword.setVisibility(View.GONE);
                findViewById(R.id.view_change_pass).setVisibility(View.GONE);
            } else {
                sw_decoypasscode.setChecked(true);
                Share.isSwitchNeedToBeOn = true;
                moRlChangePassword.setVisibility(View.VISIBLE);
                findViewById(R.id.view_change_pass).setVisibility(View.VISIBLE);
            }
        }

        sw_decoypasscode.setOnCheckedChangeListener((compoundButton, b) -> {
            Log.e(TAG, "onCheckedChanged: " + b);
//            SharedPrefs.save(DecoyPasscodeActivity.this, SharedPrefs.isDecodeSwitchActive, String.valueOf(b));
            if (b) {
                if (!isFromNo) {
                    if (checkAndRequestPermissions()) {
                        Intent i = new Intent(DecoyPasscodeActivity.this, CalculatorActivity.class);
                        i.putExtra("isFrom","app");
                        startActivity(i);
                        Share.decoyPasscode = true;
                    } else {
                        ActivityCompat.requestPermissions(DecoyPasscodeActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);
                    }
                    Log.e(TAG, "onCheckedChanged: " + "Switch is Active");
                }
            } else {
                if (!Share.decoyPasscode && compoundButton.isPressed()) {
                    alertdialog();
                }
            }
        });

        initViewListener();
        initViewAction();
        if (Share.isNeedToAdShow(DecoyPasscodeActivity.this)) {
            NativeAdvanceHelper.loadNativeAd(this, findViewById(R.id.fl_adplaceholder), true);
        }
    }

    public void showChangePasswordDialog(){

        final DisplayMetrics displayMetrics = new DisplayMetrics();

        View view = (View) LayoutInflater.from(this).inflate(R.layout.dialog_change_password, null, false);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(view);
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        dialog.getWindow().setLayout(displayMetrics.widthPixels - 50, Toolbar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        final EditText edtEmail = view.findViewById(R.id.et_name);
        TextView tvReset = view.findViewById(R.id.tv_save);
        tvReset.setOnClickListener(v -> {
            String pass = edtEmail.getText().toString().trim();

            if (pass.length() != 4){
                Toast.makeText(getApplicationContext(), "Invalid Passcode.", Toast.LENGTH_LONG).show();
            }else if (pass.equals("0000")){
                Toast.makeText(getApplicationContext(), "Passcode can not be 0000.", Toast.LENGTH_LONG).show();
            }else if (moDBimageVideoDatabase.getSingleUserData(1).getPassword().equals(pass)){
                Toast.makeText(getApplicationContext(), "Passcode matches with main passcode.", Toast.LENGTH_LONG).show();
            }else {
                UserModel userModel = new UserModel();
                userModel.setID(1);
                userModel.setPassword(pass);
                userModel.setConfirm_password(pass);
                userModel.setDatabasePath(databasewritepath);

                decoyDatabase.updateSingleDataPassword(userModel);
                dialog.dismiss();
            }

        });
        TextView tvCancel = view.findViewById(R.id.tv_cancle);
        tvCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!fromAdToResume) {
            if (Share.decoyPasscode) {
                if (!isFromChanegPass) {
                    sw_decoypasscode.setChecked(false);
                    moRlChangePassword.setVisibility(View.GONE);
                    findViewById(R.id.view_change_pass).setVisibility(View.GONE);
                    Share.decoyPasscode = false;
                } else {
                    isFromChanegPass = false;
                }
            }

            if (SharedPrefs.getString(DecoyPasscodeActivity.this, SharedPrefs.isDecodeSwitchActive, "false").equalsIgnoreCase("false")) {
                Share.isSwitchNeedToBeOn = false;
            } else {
                Share.isSwitchNeedToBeOn = true;
            }

            if (Share.isSwitchNeedToBeOn) {
                moRlChangePassword.setVisibility(View.VISIBLE);
                findViewById(R.id.view_change_pass).setVisibility(View.VISIBLE);
            } else {
                sw_decoypasscode.setChecked(false);
            }
        } else {
            fromAdToResume = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NativeAdvanceHelper.onDestroy();
    }

    private void deleteDecoydata() {
        decoyDatabase.dropAllTables();
        File file = new File(Share.msPathToWriteDecoy);
        if (file.exists()) {
            Log.e(TAG, "onCheckedChanged: Decoy " + " Path Deleted");
            file.delete();
        }
        Log.e(TAG, "onCheckedChanged: " + "Switch is Deactivated");
    }

    private void initViewAction() {
    }

    private void initViewListener() {
        sw_decoypasscode.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        moRlChangePassword.setOnClickListener(this);

        moIvMoreApp.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (Share.isNeedToAdShow(DecoyPasscodeActivity.this)) {
                    if (IntAdsHelper.isInterstitialLoaded()) {
                        moIvMoreApp.setVisibility(View.GONE);
                        moIvBlast.setVisibility(View.VISIBLE);
                        ((AnimationDrawable) moIvBlast.getBackground()).start();
                        IntAdsHelper.showInterstitialAd();
//                    TODO Admob Ads
//                    interstitial.show();
                        fromAdToResume = true;
                    }
                }
            }
        });

    }

    private void initView() {
        sw_decoypasscode = findViewById(R.id.sw_decoypasscode);
        iv_back = findViewById(R.id.iv_back);
        moRlChangePassword = findViewById(R.id.view_change_pass);

        // Gift Ads
        moIvMoreApp = findViewById(R.id.iv_more_app);
        moIvBlast = findViewById(R.id.iv_blast);
        moIvMoreApp.setVisibility(View.GONE);
        moIvMoreApp.setBackgroundResource(R.drawable.animation_list_filling);
        ((AnimationDrawable) moIvMoreApp.getBackground()).start();
    }

    private boolean checkAndRequestPermissions() {

        listPermissionsNeeded.clear();
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readStorage = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);

        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (readStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_EXTERNAL_STORAGE);
        }

        return listPermissionsNeeded.isEmpty();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case STORAGE_PERMISSION_CODE:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        Log.e("TAG", "onRequestPermissionsResult: deny");

                    } else {
                        Log.e("TAG", "onRequestPermissionsResult: dont ask again");

                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setTitle("Permissions Required")
                                .setMessage("Please allow permission for storage")
                                .setPositiveButton("Cancel", (dialog, which) -> {
                                })
                                .setNegativeButton("Ok", (dialog, which) -> {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                })
                                .setCancelable(false)
                                .create()
                                .show();
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.view_change_pass:
                if (checkAndRequestPermissions()) {
                    showChangePasswordDialog();
//                    Intent i = new Intent(DecoyPasscodeActivity.this, CalculatorActivity.class);
//                    startActivity(i);
//                    isFromChanegPass = true;
//                    Share.decoyPasscode = true;
//                    Share.changeDecoy = true;
                } else {
                    ActivityCompat.requestPermissions(DecoyPasscodeActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);
                }
                break;
        }
    }

    private void alertdialog() {
        final Dialog dialog1 = new Dialog(DecoyPasscodeActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.setContentView(R.layout.alert_decoy_passcode);
        dialog1.setCancelable(false);
        TextView mess = (TextView) dialog1.findViewById(R.id.tv_message1);
        mess.setText(getString(R.string.decoy_message));
        Button yesbtn = (Button) dialog1.findViewById(R.id.btn_dialogOK1);
        Button nobtn = (Button) dialog1.findViewById(R.id.btn_dialogNo1);

        yesbtn.setOnClickListener(v -> {
            moRlChangePassword.setVisibility(View.GONE);
            findViewById(R.id.view_change_pass).setVisibility(View.GONE);
            isFromNo = false;
            sw_decoypasscode.setChecked(false);
            SharedPrefs.save(DecoyPasscodeActivity.this, SharedPrefs.isDecodeSwitchActive, String.valueOf(false));
            try {
                deleteDecoydata();
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog1.dismiss();
        });
        nobtn.setOnClickListener(v -> {
            dialog1.dismiss();
            isFromNo = true;
            sw_decoypasscode.setChecked(true);
            SharedPrefs.save(DecoyPasscodeActivity.this, SharedPrefs.isDecodeSwitchActive, String.valueOf(true));
        });

        if (dialog1 != null && !dialog1.isShowing()) {
            sw_decoypasscode.setChecked(true);
            SharedPrefs.save(DecoyPasscodeActivity.this, SharedPrefs.isDecodeSwitchActive, String.valueOf(true));
            dialog1.show();
        }
    }

}
