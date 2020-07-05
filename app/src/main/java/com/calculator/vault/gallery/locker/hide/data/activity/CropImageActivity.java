package com.calculator.vault.gallery.locker.hide.data.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;


import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.fragment.MainFragment;

import java.util.ArrayList;
import java.util.List;

public class CropImageActivity extends AppCompatActivity {

    private static final String TAG = CropImageActivity.class.getSimpleName();
    String imageUrl;
    //private FirebaseAnalytics mFirebaseAnalytics;
    private int STORAGE_PERMISSION_CODE = 23;
    private static final int REQUEST_SETTINGS_PERMISSION = 102;
    private List<String> listPermissionsNeeded = new ArrayList<>();
    public static CropImageActivity activity;
//    private AdView fb_adView;

   // AdView adView;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        //if (Share.RestartApp(this)) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //loadFBAdsBanner();

        /* if (getIntent().hasExtra(Share.KEYNAME.SELECTED_IMAGE)) {
            imageUrl = getIntent().getExtras().getString(Share.KEYNAME.SELECTED_IMAGE);
        } else {
            imageUrl = getIntent().getStringExtra(Share.KEYNAME.SELECTED_PHONE_IMAGE);
        }*/

        Log.e(TAG, "CropImageActivity==>  " + Share.imageUrl);
        //Share.imageUrl = imageUrl;

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction().add(R.id.container, MainFragment.getInstance()).commit();
        }
    }

    /*private void loadFBAdsBanner() {

        fb_adView = new com.facebook.ads.AdView(CropImageActivity.this, getString(R.string.fb_banner_placement), AdSize.BANNER_HEIGHT_50);
        //AdSettings.addTestDevice("5de1d7f9ad822be9013726e7ee8c0578");

        // Find the Ad Container
        final LinearLayout fb_banner_container = (LinearLayout) findViewById(R.id.fb_banner_container);

        fb_adView.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e("FB banner", "--> " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.e("FB banner", "--> onAdLoaded");
                fb_banner_container.removeAllViews();
                fb_banner_container.addView(fb_adView);
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.e("FB banner", "--> onAdClicked");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.e("FB banner", "--> onLoggingImpression");
            }
        });
        // Request an ad
        fb_adView.loadAd();
    }*/

    public static void getCropActivity(){

    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
      /*  if (Share.imageUrl == null) {
            try {
                String ut = preferences.getString("tempimage", null);
                if (ut != null) {
                    Share.imageUrl = ut;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }*/

       /* if (GlobalData.RestartApp(this)) {

        }*/
    }

    @Override
    protected void onDestroy() {

       /* if (fb_adView != null) {
            fb_adView.destroy();
        }*/

        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        //  Share.isFromHomeAgain = false;
        super.onBackPressed();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

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

        } else {

            //saveImage();
        }

    }

    public void startResultActivity(final Bitmap uri) {


        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();

    }
}
