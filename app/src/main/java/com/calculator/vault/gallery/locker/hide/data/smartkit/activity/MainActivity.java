package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.SharedPrefs;

import example.zxing.ToolbarCaptureActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BillingProcessor.IBillingHandler {

    LinearLayout ll_qr_code, ll_bmi, ll_calculator, ll_currency, ll_flash, ll_text_to_speak, ll_transltor, ll_compass, ll_day_counter, ll_age_calculate, ll_recoder, ll_world_clock;
    Activity activity;
    private Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    // TODO: 13/04/19  for in-app purchase
    private Animation rotation;
    private ProgressDialog progressBar;
    private BillingProcessor billingProcessor;

    private String ProductKey = null;
    private String LicenseKey = null;
    private ProgressDialog upgradeDialog;
    public static String[] EXIT_URLs = new String[]{"https://play.google.com/store/apps/developer?id=Suit+Photo+Editor+Montage+Maker+%26+Face+Changer"};
    public static String EXIT_URL = EXIT_URLs[0];
    ImageView iv_remove_ad, iv_more_apps;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private final int STORAGE_PERMISSION_CODE = 23;
    private final int STORAGE_PERMISSION = 33;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_main_app);

        activity = MainActivity.this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        // TODO: 13/04/19  for in-app purchase
        billingProcessor = new BillingProcessor(activity, LicenseKey, this);
        billingProcessor.initialize();

        ProductKey = getString(R.string.ads_product_key);
        LicenseKey = getString(R.string.licenseKey);

        initview();
        initAction();
        setupToolbar();
        //DrawerMenuList();

    }

    private void initview() {
        ll_qr_code = findViewById(R.id.ll_qr_code);
        ll_bmi = findViewById(R.id.ll_bmi);
        ll_calculator = findViewById(R.id.ll_calculator);
        ll_currency = findViewById(R.id.ll_currency);
        ll_flash = findViewById(R.id.ll_flash);
        ll_text_to_speak = findViewById(R.id.ll_text_to_speak);
        ll_transltor = findViewById(R.id.ll_transltor);
        ll_compass = findViewById(R.id.ll_compass);
        ll_day_counter = findViewById(R.id.ll_day_counter);
        ll_age_calculate = findViewById(R.id.ll_age_calculate);
        ll_recoder = findViewById(R.id.ll_recoder);
        ll_world_clock = findViewById(R.id.ll_world_clock);
        iv_remove_ad = findViewById(R.id.iv_remove_ad);
        iv_more_apps = findViewById(R.id.iv_more_apps);

    }

    private void initAction() {
        ll_qr_code.setOnClickListener(this);
        ll_bmi.setOnClickListener(this);
        ll_calculator.setOnClickListener(this);
        ll_currency.setOnClickListener(this);
        ll_flash.setOnClickListener(this);
        ll_text_to_speak.setOnClickListener(this);
        ll_transltor.setOnClickListener(this);
        ll_compass.setOnClickListener(this);
        ll_day_counter.setOnClickListener(this);
        ll_age_calculate.setOnClickListener(this);
        ll_recoder.setOnClickListener(this);
        ll_world_clock.setOnClickListener(this);
        iv_remove_ad.setOnClickListener(this);
        iv_more_apps.setOnClickListener(this);
    }

    /*private void DrawerMenuList() {
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();
        DataModel[] drawerItem;

        if (Share.isNeedToAdShow(activity)) {
            drawerItem = new DataModel[4];
            drawerItem[0] = new DataModel(R.drawable.ic_rate, "Rate Us");
            drawerItem[1] = new DataModel(R.drawable.ic_share, "Share App");
            drawerItem[2] = new DataModel(R.drawable.ic_more_app, "More Apps");
            drawerItem[3] = new DataModel(R.drawable.ic_remove_ads, "Remove Ads");
        } else {
            drawerItem = new DataModel[2];
            drawerItem[0] = new DataModel(R.drawable.ic_rate, "Rate Us");
            drawerItem[1] = new DataModel(R.drawable.ic_share, "Share App");
        }

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        //setupDrawerToggle();
    }*/

    void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        /*{
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerToggle.setDrawerIndicatorEnabled(true);
            }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mDrawerToggle.setDrawerIndicatorEnabled(true);
            }
        };*/
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        //This is necessary to change the icon of the Drawer Toggle upon state change.
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        //Fragment fragment = null;

        mDrawerList.setItemChecked(-1, true);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        switch (position) {
            case 0:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case 1:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));

                String sAux = "Smart Kit is an all-in-one app and is half the size of a common standalone tool application. This way you'll save a lot of disk space, time and frustration searching for all the daily tools you need. Download and give us a review of "
                        + getResources().getString(R.string.app_name) + ". Check out the Apps at " + "https://play.google.com/store/apps/details?id=" + getPackageName();
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
                break;

            case 2:
                if (Share.al_app_center_data.size() > 0 || Share.al_app_center_home_data.size() > 0) {
                    Intent go2appcenter = new Intent(activity, HomePageActivity.class);
                    startActivity(go2appcenter);
                }
                break;

            case 3:
                purchaseItem();
                break;
        }

      /*  if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(GravityCompat.START);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }*/

    }

    void setupToolbar() {

        try {

            if (Share.isNeedToAdShow(getApplicationContext())) {
                iv_remove_ad.setVisibility(View.VISIBLE);
                iv_more_apps.setVisibility(View.VISIBLE);

                rotation = AnimationUtils.loadAnimation(this, R.anim.shake_anim);
                rotation.setRepeatCount(Animation.ABSOLUTE);
                iv_remove_ad.startAnimation(rotation);

            } else {
                iv_remove_ad.setVisibility(View.GONE);
                iv_more_apps.setVisibility(View.GONE);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_remove_ad:
                purchaseItem();
                break;

            case R.id.iv_more_apps:
                if (Share.al_app_center_data.size() > 0 || Share.al_app_center_home_data.size() > 0) {
                    Intent go2appcenter = new Intent(activity, HomePageActivity.class);
                    go2appcenter.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(go2appcenter);
                }
                break;

            case R.id.ll_calculator:
                Intent i1 = new Intent(this, ScientificCalculatorActivity.class);
                i1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i1);
                break;

            case R.id.ll_transltor:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkAndRequestPermissions(STORAGE_PERMISSION)) {
                        Intent i5 = new Intent(this, TranslatorActivity.class);
                        i5.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i5);
                    }
                } else {
                    Intent i5 = new Intent(this, TranslatorActivity.class);
                    i5.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i5);
                }
                break;

            case R.id.ll_compass:

                SensorManager mSensorManager;
                mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null){
                    Intent i6 = new Intent(this, CompassActivity.class);
                    i6.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i6);
                }
                else {
                    Toast.makeText(activity, "Not support this device", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.ll_recoder:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkAndRequestPermissionsRecoder(STORAGE_PERMISSION_CODE)) {
                        Intent i9 = new Intent(this, RecorderActivity.class);
                        i9.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i9);
                    }
                } else {
                    Intent i9 = new Intent(this, RecorderActivity.class);
                    i9.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i9);
                }

                break;

            case R.id.ll_qr_code:
                if (checkPermission()) {
                    new IntentIntegrator(MainActivity.this).setCaptureActivity(ToolbarCaptureActivity.class).initiateScan();
                } else {
                    requestPermission();
                }

                break;

            case R.id.ll_flash:
                Intent i3 = new Intent(this, FlashActivity.class);
                i3.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i3);
                break;

            case R.id.ll_currency:

                Intent i2 = new Intent(this, UnitConverterCalculationActivity.class);
                i2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i2);

                break;

            case R.id.ll_world_clock:
                Intent i10 = new Intent(this, WorldClockActivity.class);
                i10.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i10);

                break;

            case R.id.ll_age_calculate:
                Intent i8 = new Intent(this, AgeCalculatorActivity.class);
                i8.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i8);
                break;


            case R.id.ll_bmi:
                Intent i = new Intent(this, BMIMainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                break;

            case R.id.ll_text_to_speak:
                Intent i4 = new Intent(this, WordToSpeakActivity.class);
                i4.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i4);
                break;

            case R.id.ll_day_counter:
                Intent i7 = new Intent(this, DayCounterActivity.class);
                i7.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i7);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String toast;

            if (result.getContents() == null) {
                toast = "Cancelled from fragment";
//Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
            } else {
                if (URLUtil.isValidUrl(result.getContents())) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle(Html.fromHtml("<font color='#00CD2D'>Recognition Result</font>"));
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setMessage(result.getContents());
                    alertDialogBuilder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface alertDialog, int arg1) {
                                    alertDialog.dismiss();
                                }
                            });

                    alertDialogBuilder.setPositiveButton("Open URL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("" + result.getContents()));
                            startActivity(browserIntent);

                        }
                    });
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle(Html.fromHtml("<font color='#00CD2D'>Recognition Result</font>"));
                    alertDialogBuilder.setMessage(result.getContents());
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }
                            });

                    alertDialogBuilder.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            ClipData clipData = ClipData.newPlainText("Source Text", result.getContents());
                        }
                    });
                }
            }
        }
        // TODO: 13/04/19 in-app purches
        if (billingProcessor != null) {
            if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // TODO: 13/04/19 in-app purches
        if (Share.isNeedToAdShow(activity)) {
            iv_remove_ad.setVisibility(View.VISIBLE);
            iv_more_apps.setVisibility(View.VISIBLE);
        } else {
            iv_remove_ad.setVisibility(View.GONE);
            iv_more_apps.setVisibility(View.GONE);
        }
    }


    // TODO: 13/04/19  for in-app purchase
    private void purchaseItem() {

        if (billingProcessor != null) {


            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(R.string.app_name)
                    .setMessage(getString(R.string.remove_ad_msg))
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //set progress dialog and start the in app purchase process
                            upgradeDialog = ProgressDialog.show(activity, "Please wait", "", true);

                            /* TODO: for security, generate your payload here for verification. See the comments on
                             *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
                             *        an empty string, but on a production app you should carefully generate this. */
                            String payload = "";

                            billingProcessor.purchase(activity, ProductKey, payload);

                            upgradeDialog.dismiss();
                        }
                    })
                    .setNegativeButton(getString(R.string.no_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (upgradeDialog != null && upgradeDialog.isShowing())
                                upgradeDialog.dismiss();
                        }
                    })
                    .setCancelable(false);

            final android.app.AlertDialog dirsDialog1 = alertDialogBuilder.create();
            dirsDialog1.setCanceledOnTouchOutside(true);
            dirsDialog1.show();
        } else {

            Log.e("TAG", "onClick: billPr == null");
            if (upgradeDialog != null && upgradeDialog.isShowing()) {
                upgradeDialog.dismiss();
            }
            Share.showAlert(activity, getString(R.string.app_name), getString(R.string.something_wrong));
        }
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        if (upgradeDialog != null && upgradeDialog.isShowing()) {
            upgradeDialog.dismiss();
        }

        Log.e("onProductPurchased", "Purchased");
        SharedPrefs.savePref(this, SharedPrefs.IS_ADS_REMOVED, true);
        removeAds();
        Share.showAlert(activity, getString(R.string.app_name), getString(R.string.remove_ads_msg));
    }

    private void removeAds() {
        iv_remove_ad.setVisibility(View.GONE);
        iv_more_apps.setVisibility(View.GONE);
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void onBackPressed() {
        if (Share.isNeedToAdShow(getApplicationContext())) {
            finish();
        } else {
            openExitDialog();
        }
    }

    private void openExitDialog() {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.s_choose_category_alert1); //get layout from ExitDialog folder

        SmileRating smileRating = (SmileRating) dialog.findViewById(R.id.smile_rating);
        Button btn_yes = dialog.findViewById(R.id.btn_yes);
        Button btn_no = dialog.findViewById(R.id.btn_no);

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (SplashHomeActivity.activity != null) {
                    SplashHomeActivity.activity.finish();
                }

                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                System.exit(0);
                finish();
            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    dialog.dismiss();

                    if (SharedPrefs.getInt(activity, SharedPrefs.URL_INDEX) < EXIT_URLs.length) {
                        EXIT_URL = EXIT_URLs[SharedPrefs.getInt(activity, SharedPrefs.URL_INDEX)];
                        Intent ratingIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(EXIT_URL));
                        startActivity(ratingIntent);
                    }
                    if (SharedPrefs.getInt(activity, SharedPrefs.URL_INDEX) < (EXIT_URLs.length - 1))
                        SharedPrefs.save(activity, SharedPrefs.URL_INDEX, SharedPrefs.getInt(activity, SharedPrefs.URL_INDEX) + 1);
                    else
                        SharedPrefs.save(activity, SharedPrefs.URL_INDEX, 0);

                } catch (ActivityNotFoundException e) {
                    // If play store not found then it open in browser
                    Intent ratingIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(EXIT_URL));
                    activity.startActivity(ratingIntent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                switch (smiley) {
                    case SmileRating.BAD:
                        Toast.makeText(activity, "Thanks for review", Toast.LENGTH_SHORT).show();
                        break;
                    case SmileRating.GOOD:
                        rate_app();
                        break;
                    case SmileRating.GREAT:
                        rate_app();
                        break;
                    case SmileRating.OKAY:
                        Toast.makeText(activity, "Thanks for review", Toast.LENGTH_SHORT).show();
                        break;
                    case SmileRating.TERRIBLE:
                        Toast.makeText(activity, "Thanks for review", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        dialog.show();
    }

    private void rate_app() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }


    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    private boolean checkAndRequestPermissions(int code) {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, code);
            return false;
        } else {
            return true;
        }
    }

    private boolean checkAndRequestPermissionsRecoder(int code) {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
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
            case PERMISSION_REQUEST_CODE:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

                    } else {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                            alertDialogBuilder.setTitle("Permissions Required")
                                    .setMessage("Please allow permission for camera")
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

            case STORAGE_PERMISSION:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {

                    } else {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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

                    }
                }
                break;

            case STORAGE_PERMISSION_CODE:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {

                    } else {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
                        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
