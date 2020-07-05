package com.calculator.vault.gallery.locker.hide.data.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.ads.AdsListener;
import com.calculator.vault.gallery.locker.hide.data.ads.IntAdsHelper;
import com.calculator.vault.gallery.locker.hide.data.ads.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.common.OnSingleClickListener;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.common.Utils;
import com.calculator.vault.gallery.locker.hide.data.googleDrive.DownloadService;
import com.calculator.vault.gallery.locker.hide.data.googleDrive.DriveServiceHelper;
import com.calculator.vault.gallery.locker.hide.data.googleDrive.UploadService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class BackupActivity extends AppCompatActivity /*implements MoPubInterstitial.InterstitialAdListener*//*InterstitialAdHelper.onInterstitialAdListener*/ {

    private static final String TAG = "BackupActivity";

    private static final int REQUEST_CODE_SIGN_IN = 1;

    private DriveServiceHelper mDriveServiceHelper;

    private boolean isFrombackup = true;
    private ImageView moIvMoreApp, moIvBlast;

//    private boolean isInterstitialAdLoaded = false;
//    private MoPubInterstitial mInterstitial;
    //TODO Admob Ads
//    private InterstitialAd interstitial;

//    @Override
//    public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {
//        if (Share.isNeedToAdShow(BackupActivity.this)) {
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
//        if (Utils.isNetworkConnected(BackupActivity.this)) {
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
//        if (Utils.isNetworkConnected(BackupActivity.this)) {
//            mInterstitial.load();
//        }
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//    }

    //TODO Admob Ads
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
//            interstitial = InterstitialAdHelper.getInstance().load2(BackupActivity.this, this);
//        }else if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id2))){
//            interstitial = InterstitialAdHelper.getInstance().load(BackupActivity.this, this);
//        }else {
//            interstitial = InterstitialAdHelper.getInstance().load1(BackupActivity.this, this);
//        }
//
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onClosed() {
//        isInterstitialAdLoaded = false;
//        interstitial = InterstitialAdHelper.getInstance().load1(BackupActivity.this, this);
//
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

//        mInterstitial = new MoPubInterstitial(this, getString(R.string.mopub_int_id));
//        mInterstitial.setInterstitialAdListener(this);

//            mInterstitial.load();
            IntAdsHelper.loadInterstitialAds(BackupActivity.this, new AdsListener() {
                @Override
                public void onLoad() {
                    moIvBlast.setVisibility(View.GONE);
                    moIvMoreApp.setVisibility(View.VISIBLE);
                }

                @Override
                public void onClosed() {
                    moIvBlast.setVisibility(View.GONE);
                    moIvMoreApp.setVisibility(View.GONE);
                }
            });



        //TODO Admob Ads
//        interstitial = InterstitialAdHelper.getInstance().load1(BackupActivity.this, this);

        // Gift Ads
        moIvMoreApp = findViewById(R.id.iv_more_app);
        moIvBlast = findViewById(R.id.iv_blast);
        moIvMoreApp.setVisibility(View.GONE);
        moIvMoreApp.setBackgroundResource(R.drawable.animation_list_filling);
        ((AnimationDrawable) moIvMoreApp.getBackground()).start();

        moIvMoreApp.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (Share.isNeedToAdShow(BackupActivity.this)) {
                    if (IntAdsHelper.isInterstitialLoaded()) {
                        moIvMoreApp.setVisibility(View.GONE);
                        moIvBlast.setVisibility(View.VISIBLE);
                        ((AnimationDrawable) moIvBlast.getBackground()).start();
                        IntAdsHelper.showInterstitialAd();
                        //TODO Admob Ads
//                    interstitial.show();
                    }
                }
            }
        });

        findViewById(R.id.ll_take_backup).setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (Utils.isNetworkConnected(BackupActivity.this)) {
                    if (!Utils.isServiceRunning(BackupActivity.this, DownloadService.class)) {
                        if (!Utils.isServiceRunning(BackupActivity.this, UploadService.class)) {
                            isFrombackup = true;
                            alertDialogUpload();
                        } else {
                            Toast.makeText(BackupActivity.this, "Backup is already running please wait.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(BackupActivity.this, "Restore is running please wait.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(BackupActivity.this, "No internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.ll_restore_backup).setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (Utils.isNetworkConnected(BackupActivity.this)) {
                    if (!Utils.isServiceRunning(BackupActivity.this, UploadService.class)) {
                        if (!Utils.isServiceRunning(BackupActivity.this, DownloadService.class)) {
                            isFrombackup = false;
                            alertDialogDownload();
                        } else {
                            Toast.makeText(BackupActivity.this, "Restore is already running please wait.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(BackupActivity.this, "Backup is running please wait.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(BackupActivity.this, "No internet connection.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.iv_back).setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onBackPressed();
            }
        });

        if (Share.isNeedToAdShow(this)) {
            NativeAdvanceHelper.loadNativeAd(this, findViewById(R.id.fl_adplaceholder), true);
        }

    }

    private void requestSignIn() {
        Log.e(TAG, "Requesting sign-in");

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestScopes(new Scope(DriveScopes.DRIVE_FILE)).build();
        GoogleSignInClient client = GoogleSignIn.getClient(this, signInOptions);

        // The result of the sign-in Intent is handled in onActivityResult.
        startActivityForResult(client.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        Log.e(TAG, "onActivityResult: " + requestCode + "///" + resultCode);
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                if (resultCode == Activity.RESULT_OK && resultData != null) {
                    handleSignInResult(resultData);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, resultData);
    }

    private void handleSignInResult(Intent result) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(googleAccount -> {
                    Log.e(TAG, "Signed in as " + googleAccount.getEmail());

                    // Use the authenticated account to sign in to the Drive service.
                    GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(this, Collections.singleton(DriveScopes.DRIVE_FILE));
                    credential.setSelectedAccount(googleAccount.getAccount());
                    Drive googleDriveService = new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential).setApplicationName("Drive API Migration").build();

                    // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                    // Its instantiation is required before handling any onClick actions.
                    mDriveServiceHelper = new DriveServiceHelper(googleDriveService);
                    if (isFrombackup){
                        upload();
                    }else {
                        download();
                    }
                    //mDriveServiceHelper.fileinfolder();
                })
                .addOnFailureListener(exception -> Log.e(TAG, "Unable to sign in.", exception));
    }

    private void upload() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(BackupActivity.this, UploadService.class));
        }else {
            startService(new Intent(BackupActivity.this, UploadService.class));
        }
    }

    private void download() {

        java.io.File file = new java.io.File(Environment.getExternalStorageDirectory().getPath() + java.io.File.separator + "CalculatorBackupDownloaded.zip");
        if(file.exists()) {
            try {
                file.delete();
                file.getCanonicalFile().delete();
                if(file.exists()){
                    getApplicationContext().deleteFile(file.getName());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(BackupActivity.this, DownloadService.class));
        }else {
            startService(new Intent(BackupActivity.this, DownloadService.class));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

//    public static long getFileFolderSize(File dir) {
//        long size = 0;
//        if (dir.isDirectory()) {
//            for (File file : dir.listFiles()) {
//                if (file.isFile()) {
//                    size += file.length();
//                } else
//                    size += getFileFolderSize(file);
//            }
//        } else if (dir.isFile()) {
//            size += dir.length();
//        }
//        return size;
//    }

    private static String dirSize(String path) {

        File dir = new File(path);

        if(dir.exists()) {
            long bytes = getFolderSize(dir);
            if (bytes < 1024) return bytes + " B";
            int exp = (int) (Math.log(bytes) / Math.log(1024));
            String pre = ("KMGTPE").charAt(exp-1) + "";

            return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
        }

        return "0";
    }

    public static long getFolderSize(File dir) {
        if (dir.exists()) {
            long result = 0;
            File[] fileList = dir.listFiles();
            for(int i = 0; i < fileList.length; i++) {
                // Recursive call if it's a directory
                if(fileList[i].isDirectory()) {
                    result += getFolderSize(fileList[i]);
                } else {
                    // Sum the file size in bytes
                    result += fileList[i].length();
                }
            }
            return result; // return the file size
        }
        return 0;
    }

    private void alertDialogDownload() {

        //String lsFileSize;
        //if (new java.io.File(Environment.getExternalStorageDirectory().getPath() + java.io.File.separator + ".androidData").exists()){
        String lsFileSize = dirSize(Environment.getExternalStorageDirectory().getPath() + java.io.File.separator + ".androidData");
        //}

        final Dialog dialog1 = new Dialog(BackupActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.setContentView(R.layout.alert_decoy_passcode);
        TextView mess = dialog1.findViewById(R.id.tv_message1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mess.setText(Html.fromHtml(getString(R.string.success_restore_msg_dialog) + "<br/><span style='color:#ef9a9a'>Please do not remove this application from background until the backup restore completed.</span>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            mess.setText(Html.fromHtml(getString(R.string.success_restore_msg_dialog)) + "\nPlease do not remove this application from background until the backup restore completed.");
        }
        Button yesbtn = dialog1.findViewById(R.id.btn_dialogOK1);
        yesbtn.setText("OK");
        Button nobtn = dialog1.findViewById(R.id.btn_dialogNo1);
        nobtn.setText("CANCEL");
        //nobtn.setVisibility(View.GONE);

        yesbtn.setOnClickListener(v -> {
            dialog1.dismiss();
            requestSignIn();
        });

        nobtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                isFrombackup = true;
                dialog1.dismiss();
            }
        });

        if (dialog1 != null && !dialog1.isShowing()) {
            dialog1.show();
        }
    }

    private void alertDialogUpload() {

        //String lsFileSize;
        //if (new java.io.File(Environment.getExternalStorageDirectory().getPath() + java.io.File.separator + ".androidData").exists()){
        String lsFileSize = dirSize(Environment.getExternalStorageDirectory().getPath() + java.io.File.separator + ".androidData");
        //}

        final Dialog dialog1 = new Dialog(BackupActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.setContentView(R.layout.alert_decoy_passcode);
        TextView mess = dialog1.findViewById(R.id.tv_message1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mess.setText(Html.fromHtml(getString(R.string.success_backup_msg_dialog) + " BackUp File size is " + lsFileSize +". <br/><span style='color:#ef9a9a'>Please do not remove this application from background until the backup upload completed.</span>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            mess.setText(Html.fromHtml(getString(R.string.success_backup_msg_dialog)) + " BackUp File size is " + lsFileSize +". Please do not remove this application from background until the backup upload completed.");
        }
        Button yesbtn = dialog1.findViewById(R.id.btn_dialogOK1);
        yesbtn.setText("OK");
        Button nobtn = dialog1.findViewById(R.id.btn_dialogNo1);
        nobtn.setText("CANCEL");
        //nobtn.setVisibility(View.GONE);

        yesbtn.setOnClickListener(v -> {
            dialog1.dismiss();
            requestSignIn();
        });

        nobtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                isFrombackup = false;
                dialog1.dismiss();
            }
        });

        if (dialog1 != null && !dialog1.isShowing()) {
            dialog1.show();
        }
    }
}
