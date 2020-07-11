package com.calculator.vault.gallery.locker.hide.data.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.appnext.ads.interstitial.Interstitial;
import com.appnext.base.Appnext;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.ads.AdsListener;
import com.calculator.vault.gallery.locker.hide.data.ads.IntAdsHelper;
import com.calculator.vault.gallery.locker.hide.data.ads.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.appLock.Accessibilty;
import com.calculator.vault.gallery.locker.hide.data.appLock.AppListActivity;
import com.calculator.vault.gallery.locker.hide.data.common.RatingDialog;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.common.Utils;
import com.calculator.vault.gallery.locker.hide.data.commonCode.activities.HomePageActivity;
import com.calculator.vault.gallery.locker.hide.data.commonCode.utils.DisplayMetricsHandler;
import com.calculator.vault.gallery.locker.hide.data.smartkit.Database.DBHelperClass;
import com.calculator.vault.gallery.locker.hide.data.smartkit.activity.AgeCalculatorActivity;
import com.calculator.vault.gallery.locker.hide.data.smartkit.activity.BMIMainActivity;
import com.calculator.vault.gallery.locker.hide.data.smartkit.activity.DayCounterActivity;
import com.calculator.vault.gallery.locker.hide.data.smartkit.activity.FlashActivity;
import com.calculator.vault.gallery.locker.hide.data.smartkit.activity.RecorderActivity;
import com.calculator.vault.gallery.locker.hide.data.smartkit.activity.ScientificCalculatorActivity;
import com.calculator.vault.gallery.locker.hide.data.smartkit.activity.TranslatorActivity;
import com.calculator.vault.gallery.locker.hide.data.smartkit.activity.WordToSpeakActivity;
import com.calculator.vault.gallery.locker.hide.data.subscription.SubscriptionActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.hsalf.smilerating.SmileRating;

import java.util.ArrayList;
import java.util.List;

import example.zxing.ToolbarCaptureActivity;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_CONTACTS;

public class SelectionActivity extends AppCompatActivity implements View.OnClickListener, BillingProcessor.IBillingHandler
        /*MoPubInterstitial.InterstitialAdListener*//*InterstitialAdHelper.onInterstitialAdListener*/ {/*, NavigationView.OnNavigationItemSelectedListener {*/

    private static final int PERMISSION_REQUEST_CODE = 200;
    private final int STORAGE_PERMISSION = 33;

    private SelectionActivity activity;
    private LinearLayout moLlPhotos, ll_videos, ll_contacts, ll_notes, ll_browser, ll_credentials,
            llRateApp, llRemoveAds, llBackToCalculator, view_more, llOtherFiles, llAppLock;
   // private View vBrowserSetting, vLockSetting, vBreakInReport, vDecoyPasscode, vRecoveryPasscode;
    private List<String> listPermissionsNeeded = new ArrayList<>();
    public final int STORAGE_PERMISSION_CODE = 23;
    private String image_name;
    private DrawerLayout mDrawerLayout;
    private String TAG = this.getClass().getSimpleName();
    //NavigationView navigationView;
    private SwitchCompat sw_breakinreport;

    private LinearLayout translaterTwo;
    private LinearLayout calcTwo;
    private LinearLayout recorderTwo;
    private LinearLayout qrTwo;
    private LinearLayout lightTwo;
    private LinearLayout bmiTwo;
    private LinearLayout ageCalcTwo;
    private LinearLayout textToSpeechTwo;
    private LinearLayout dayCounterTwo;

    private LinearLayout translatorTop;
    private LinearLayout calculatorTop;
    private LinearLayout recorderTop;
    private LinearLayout viewMoreActions;

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ImageView iv_back,babalao,babalao_dagote;
    // private ImageView iv_remove_ad;
    private List<String> listPermissionsNeededContact = new ArrayList<>();
    private static final int STORAGE_PERMISSION_CODE_Contact = 1345;
    //private AdView adView;
    private View vRemoveAds, vMoreApps, vShareApp, mCLMain, mCLSecond;
    private boolean isFromContacts = false;
    private boolean isAdmin = false;
    //private AdView adView;
    final DBHelperClass dba = new DBHelperClass(this);

    // TODO: 06/11/18 in app purchase

    Animation rotation;

    ProgressDialog progressBar;
    BillingProcessor billingProcessor;

    String ProductKey = "";
    String LicenseKey = "";

    ProgressDialog upgradeDialog;
    private Dialog moRatingDialog;

    //Exit URLs
    public static String[] EXIT_URLs = new String[]{/*"https://play.google.com/store/apps/developer?id=Suit+Photo+Editor+Montage+Maker+%26+Face+Changer",*/
            /*"https://play.google.com/store/apps/developer?id=Pic+Frame+Photo+Collage+Maker+%26+Picture+Editor",*/
            /*"https://play.google.com/store/apps/developer?id=Prank+App",
            "https://play.google.com/store/apps/developer?id=Vasundhara%20Game%20Studios",*/
            "https://play.google.com/store/apps/developer?id=Coloring+Games+and+Coloring+Book+for+Adults",
            /*https://play.google.com/store/apps/developer?id=Keyboard+Themes+App"*/};
    public static String EXIT_URL = EXIT_URLs[0];

//    private boolean isInterstitialAdLoaded = false;
//    private MoPubInterstitial mInterstitial;
//    TODO Admob Ads
//    private InterstitialAd interstitial;


    //APP_NEXT declarations...
    Interstitial interstitial_Ad;


    //GREEDY....
    private static FrameLayout flOne, flTwo;

//    @Override
//    public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {
//        isInterstitialAdLoaded = true;
//    }
//
//    @Override
//    public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {
//        Log.e(TAG, "onInterstitialFailed: "+ moPubErrorCode);
//        isInterstitialAdLoaded = false;
//        if (Utils.isNetworkConnected(SelectionActivity.this)) {
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
//        if (Utils.isNetworkConnected(SelectionActivity.this)) {
//            mInterstitial.load();
//        }
//        Intent startMain = new Intent(activity, ExitActivity.class);
//        startActivity(startMain);
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
//            interstitial = InterstitialAdHelper.getInstance().load2(activity, this);
//        }else if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id2))){
//            interstitial = InterstitialAdHelper.getInstance().load(activity, this);
//        }else {
//            interstitial = InterstitialAdHelper.getInstance().load1(activity, this);
//        }
//    }
//
//    @Override
//    public void onClosed() {
//        isInterstitialAdLoaded = false;
////        interstitial = InterstitialAdHelper.getInstance().load1(activity, this);
//        Intent startMain = new Intent(activity, ExitActivity.class);
//        startActivity(startMain);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        activity = this;
        initView();
        Share.ChangePassword = false;

//        mInterstitial = new MoPubInterstitial(this, getString(R.string.mopub_int_id));
//        mInterstitial.setInterstitialAdListener(this);
//            mInterstitial.load();
        IntAdsHelper.loadInterstitialAds(SelectionActivity.this, new AdsListener() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onClosed() {
                Intent startMain = new Intent(activity, ExitActivity.class);
                startActivity(startMain);
            }
        });

//        interstitial = InterstitialAdHelper.getInstance().load1(activity, this);

        initViewListener();
        initViewAction();

        //adView = (AdView) findViewById(R.id.adView);
        //Share.loadAdsBanner(SelectionActivity.this, adView);

        billingProcessor = new BillingProcessor(SelectionActivity.this, LicenseKey, this);
        billingProcessor.initialize();

        ProductKey = getString(R.string.ads_product_key);
        LicenseKey = getString(R.string.licenseKey);

        // IronSource Banner Ads
        //ISAdsHelper.loadBannerAd(this, (FrameLayout) findViewById(R.id.flBanner));
        if (Utils.isNetworkConnected(SelectionActivity.this)) {
            Appnext.init(this);
        }

//        interstitial_Ad = new Interstitial(this, "59285138-0081-4ba3-b6c6-3d0ce0b42609");
//        interstitial_Ad.loadAd();
//
//        interstitial_Ad.setOnAdClosedCallback(() -> {
//            Intent iRecoverPass = new Intent(SelectionActivity.this, HowToRecoverPassActivity.class);
//            startActivity(iRecoverPass);
//            Log.e(TAG, "onNavigationItemSelected: " + "Ad closed  nav_recover_passcode");
//        });
//
//        interstitial_Ad.setOnAdErrorCallback(s -> Log.e(TAG, "adError: " + s));
//
//        interstitial_Ad.setOnAdLoadedCallback(s -> Log.e(TAG, "adLoaded: " + s));

//        if(Share.isNeedToAdShow(SelectionActivity.this)) {
//            NativeAdvanceHelper.loadGreedyGameAd(this, findViewById(R.id.fl_adplaceholder_one), findViewById(R.id.fl_adplaceholder_two), "float-4029");
//        NativeAdvanceHelper.loadGreedyGameAd(this, findViewById(R.id.fl_adplaceholder_two), "float-4029");
//        }

    }

    private void initView() {

        // iv_remove_ad = findViewById(R.id.iv_remove_ad);
        //  iv_remove_ad.setOnClickListener(this);

//        flOne = findViewById(R.id.fl_adplaceholder_one);
//        flTwo = findViewById(R.id.fl_adplaceholder_two);

        translaterTwo = (LinearLayout)findViewById( R.id.translater_two );
        calcTwo = (LinearLayout)findViewById( R.id.calc_two );
        recorderTwo = (LinearLayout)findViewById( R.id.recorder_two );
        qrTwo = (LinearLayout)findViewById( R.id.qr_two );
        lightTwo = (LinearLayout)findViewById( R.id.light_two );
        bmiTwo = (LinearLayout)findViewById( R.id.bmi_two );
        ageCalcTwo = (LinearLayout)findViewById( R.id.ageCalc_two );
        textToSpeechTwo = (LinearLayout)findViewById( R.id.textToSpeech_two );
        dayCounterTwo = (LinearLayout)findViewById( R.id.dayCounter_two );

        translatorTop = (LinearLayout)findViewById( R.id.translator_top );
        calculatorTop = (LinearLayout)findViewById( R.id.calculator_top );
        recorderTop = (LinearLayout)findViewById( R.id.recorder_top );
        moLlPhotos = findViewById(R.id.ll_photos);
        babalao = findViewById(R.id.babalao);
        babalao_dagote = findViewById(R.id.babalao_dagote);
        view_more = findViewById(R.id.view_more_actions);
        mCLSecond = findViewById(R.id.mCLSecond);
        mCLMain = findViewById(R.id.mCLMain);
        llOtherFiles = findViewById(R.id.ll_other);
        llAppLock = findViewById(R.id.ll_appLock);
        ll_videos = findViewById(R.id.ll_videos);
        ll_contacts = findViewById(R.id.ll_contacts);
        ll_notes = findViewById(R.id.ll_notes);
        ll_browser = findViewById(R.id.ll_browser);
        llRemoveAds = findViewById(R.id.ll_nav_removeAds);
        ll_credentials = findViewById(R.id.ll_credentials);
        sw_breakinreport = findViewById(R.id.sw_breakinreport);

        // iv_back = findViewById(R.id.iv_back);
       /* Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu1);

        moLlPhotos = findViewById(R.id.moLlPhotos);
        ll_videos = findViewById(R.id.ll_videos);
        ll_contacts = findViewById(R.id.ll_contacts);
        ll_notes = findViewById(R.id.ll_notes);
        ll_browser = findViewById(R.id.ll_browser);
        ll_credentials = findViewById(R.id.ll_credentials);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);*/

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        /*setSupportActionBar(toolbar);
//        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu1);*/
        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view

        //navigation items...

        llBackToCalculator = nvDrawer.findViewById(R.id.ll_back_to_calculator);
        //llContactUs = nvDrawer.findViewById(R.id.ll_nav_contactUs);
        llRateApp = nvDrawer.findViewById(R.id.ll_nav_rateApp);
        //llMoreApps = nvDrawer.findViewById(R.id.ll_nav_moreApps);
        llRemoveAds = nvDrawer.findViewById(R.id.ll_nav_removeAds);
        vShareApp = nvDrawer.findViewById(R.id.view_shareApp);
       // llAntiLostGuid = nvDrawer.findViewById(R.id.ll_nav_anti_lost);
//        toolbar.setNavigationIcon(R.drawable.ic_menu1);
        translatorTop.setOnClickListener(v -> {
            Intent i5 = new Intent(this, TranslatorActivity.class);
            i5.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i5);
        });
        calculatorTop.setOnClickListener(v -> {
            Intent i1 = new Intent(this, ScientificCalculatorActivity.class);
            i1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i1);
        });
        recorderTop.setOnClickListener(v -> {
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
        });
        view_more.setOnClickListener(v -> {
            mCLMain.setVisibility(View.GONE);
            mCLSecond.setVisibility(View.VISIBLE);
            babalao.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        });
        babalao.setOnClickListener(v1 -> {
            if (mCLMain.getVisibility() == View.GONE) {
                Log.e(TAG, "initView: visible" );
                mCLMain.setVisibility(View.VISIBLE);
                mCLSecond.setVisibility(View.GONE);
                babalao.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu1));
            }else {
                Log.e(TAG, "initView: invisible" );
                if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                    mDrawer.closeDrawer(GravityCompat.START);
                } else {
                    mDrawer.openDrawer(GravityCompat.START);
                }
            }
        });
        //llContactUs.setVisibility(View.GONE);

        if (Share.isNeedToAdShow(getApplicationContext())) {
            // iv_remove_ad.setVisibility(View.VISIBLE);
            llRemoveAds.setVisibility(View.VISIBLE);
            //vRemoveAds.setVisibility(View.VISIBLE);
//            llShareApp.setVisibility(View.GONE);
//            //vShareApp.setVisibility(View.GONE);
//            llMoreApps.setVisibility(View.VISIBLE);
            //vMoreApps.setVisibility(View.VISIBLE);

            rotation = AnimationUtils.loadAnimation(this, R.anim.shake_anim);
            rotation.setRepeatCount(Animation.ABSOLUTE);
            // iv_remove_ad.startAnimation(rotation);

        } else {
            //  iv_remove_ad.setVisibility(View.GONE);
            //llRemoveAds.setVisibility(View.GONE);
            //vRemoveAds.setVisibility(View.GONE);
//            llShareApp.setVisibility(View.VISIBLE);
//            //vShareApp.setVisibility(View.VISIBLE);
//            llMoreApps.setVisibility(View.GONE);
//            //vMoreApps.setVisibility(View.GONE);
        }


    }


    public void settings(View v){
        Log.e(TAG, "onNavigationItemSelected: " + " nav_lockSetting");
        Intent intentpass = new Intent(SelectionActivity.this, ChangePasscodeActivity.class);
        startActivity(intentpass);
    }

    private void mStartActivity(Intent mIntent){

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
    // ToDo: Drawer menu listener is set here...
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    //selectDrawerItem(menuItem);
                    return true;
                });
    }

    // ToDo: Drawer item clicks are handled here...
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        // Fragment fragment = null;
        switch (menuItem.getItemId()) {
            /*case R.id.back_to_calculator:
                Intent loIntent1 = new Intent(SelectionActivity.this, CalculatorActivity.class);
                loIntent1.putExtra("isFrom", "app");
                startActivity(loIntent1);
                finish();
                break;
            case R.id.nav_browser_settingm:
                Log.e(TAG, "onNavigationItemSelected: " + " nav_browser_setting");
                Intent browserintent = new Intent(SelectionActivity.this, BrowserSelectionActivity.class);
                startActivity(browserintent);
                break;
            case R.id.nav_lockSetting:
                Log.e(TAG, "onNavigationItemSelected: " + " nav_lockSetting");
                Intent intentpass = new Intent(SelectionActivity.this, ChangePasscodeActivity.class);
                startActivity(intentpass);
                break;
            case R.id.nav_break_in_Report:
                Intent intent = new Intent(SelectionActivity.this, BreakInReportActivity.class);
                startActivity(intent);
                Log.e(TAG, "onNavigationItemSelected: " + " nav_break_in_Report");
                break;
            case R.id.nav_decoy:
                Intent i = new Intent(SelectionActivity.this, DecoyPasscodeActivity.class);
                startActivity(i);
                Log.e(TAG, "onNavigationItemSelected: " + " nav_decoy");
                break;

            case R.id.nav_recover_passcode:
                Intent iRecoverPass = new Intent(SelectionActivity.this, HowToRecoverPassActivity.class);
                startActivity(iRecoverPass);
//                TODO AppNext Ads
//                interstitial_Ad.showAd();
                Log.e(TAG, "onNavigationItemSelected: " + " nav_recover_passcode");
                break;

            case R.id.nav_contact_us:
                Log.e(TAG, "onNavigationItemSelected: " + " nav_contact_us");
                break;
            case R.id.nav_rate_us:
                Log.e(TAG, "onNavigationItemSelected: " + " nav_rate_us");
                break;*/
        }


        try {
            //  fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        //FragmentManager fragmentManager = getSupportFragmentManager();
        // fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        //todo menuItem.setChecked(true);
        // Set action bar title
        //todo setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    private void initViewListener() {
        moLlPhotos.setOnClickListener(this);
        ll_videos.setOnClickListener(this);
        ll_contacts.setOnClickListener(this);
        ll_notes.setOnClickListener(this);
        ll_browser.setOnClickListener(this);
        ll_credentials.setOnClickListener(this);
        //llBackup.setOnClickListener(this);
        llAppLock.setOnClickListener(this);
        llOtherFiles.setOnClickListener(this);
        //llAntiLostGuid.setOnClickListener(this);
       /////**/ //

        translaterTwo.setOnClickListener(v->{
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
        });
        calcTwo.setOnClickListener(v->{
            Intent i1 = new Intent(this, ScientificCalculatorActivity.class);
            i1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i1);});
        recorderTwo.setOnClickListener(v->{
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
        });
        qrTwo.setOnClickListener(v->{
            if (checkPermission()) {
                new IntentIntegrator(SelectionActivity.this).setCaptureActivity(ToolbarCaptureActivity.class).initiateScan();
            } else {
                requestPermission();
            }});
        lightTwo.setOnClickListener(v->{
            Intent i3 = new Intent(this, FlashActivity.class);
            i3.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i3);});
        bmiTwo.setOnClickListener(v->{
            Intent i = new Intent(this, BMIMainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);});
        ageCalcTwo.setOnClickListener(v->{
            Intent i8 = new Intent(this, AgeCalculatorActivity.class);
            i8.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i8);});
        textToSpeechTwo.setOnClickListener(v->{
            Intent i4 = new Intent(this, WordToSpeakActivity.class);
            i4.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i4);});
        dayCounterTwo.setOnClickListener(v->{
            Intent i7 = new Intent(this, DayCounterActivity.class);
            i7.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i7);});
//        iv_back.setOnClickListener(this);
        //navigationView.setNavigationItemSelectedListener(this);
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
    private void initViewAction() {
        Log.e(TAG, "initViewAction: " + nvDrawer.getMenu().size());

//        final Menu menu = nvDrawer.getMenu();
//
//        MenuItem menu_settings = menu.findItem(R.id.menu_settings);
//        MenuItem menu_security = menu.findItem(R.id.menu_security);
//        MenuItem menu_help = menu.findItem(R.id.menu_help);
//        MenuItem menu_browser = menu.findItem(R.id.nav_browser_setting);
//        MenuItem menu_lock = menu.findItem(R.id.nav_lockSetting);
//        MenuItem menu_breakin = menu.findItem(R.id.nav_break_in_Report);
//        MenuItem menu_Decoy = menu.findItem(R.id.nav_decoy);
//        MenuItem menu_recover_passcode = menu.findItem(R.id.nav_recover_passcode);

        llBackToCalculator.setOnClickListener(this);
//        llBrowserSetting.setOnClickListener(this);
//        llLockSetting.setOnClickListener(this);
//        llBreakInReport.setOnClickListener(this);
//        llDecoyPass.setOnClickListener(this);
//        llRecoverPass.setOnClickListener(this);
        //llContactUs.setOnClickListener(this);
        llRateApp.setOnClickListener(this);
        //llMoreApps.setOnClickListener(this);
        llRemoveAds.setOnClickListener(this);
        //llShareApp.setOnClickListener(this);


        String isDecoy = SharedPrefs.getString(SelectionActivity.this, SharedPrefs.DecoyPassword, "false");

        if (isDecoy.equals("true")) {

//            llBrowserSetting.setVisibility(View.GONE);
//            llLockSetting.setVisibility(View.GONE);
//            llBreakInReport.setVisibility(View.GONE);
//            llDecoyPass.setVisibility(View.GONE);
//            llRecoverPass.setVisibility(View.GONE);
//            llBackup.setVisibility(View.GONE);
//            llUnintallProtection.setVisibility(View.GONE);
            llRemoveAds.setVisibility(View.GONE);
//
//            vBrowserSetting.setVisibility(View.GONE);
//            vLockSetting.setVisibility(View.GONE);
//            vBreakInReport.setVisibility(View.GONE);
//            vDecoyPasscode.setVisibility(View.GONE);
//            vRecoveryPasscode.setVisibility(View.GONE);

        } else {

//            llBrowserSetting.setVisibility(View.VISIBLE);
//            llLockSetting.setVisibility(View.VISIBLE);
//            llBreakInReport.setVisibility(View.VISIBLE);
//            llDecoyPass.setVisibility(View.VISIBLE);
//            llRecoverPass.setVisibility(View.VISIBLE);
            llRemoveAds.setVisibility(View.VISIBLE);
//            llBackup.setVisibility(View.VISIBLE);
//            llUnintallProtection.setVisibility(View.VISIBLE);
//
//            vBrowserSetting.setVisibility(View.VISIBLE);
//            vLockSetting.setVisibility(View.VISIBLE);
//            vBreakInReport.setVisibility(View.VISIBLE);
//            vDecoyPasscode.setVisibility(View.VISIBLE);
//            vRecoveryPasscode.setVisibility(View.VISIBLE);
        }

        mDrawer.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        mDrawer.bringToFront();
                        Log.e(TAG, "onDrawerOpened: ");
                        mDrawer.requestLayout();
                        nvDrawer.bringToFront();
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        //    Log.e(TAG, "onDrawerClosed: " );
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        //   Log.e(TAG, "onDrawerStateChanged: " );
                        // Respond when the drawer motion state changes
                    }
                }
        );
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

    private boolean checkAndRequestPermissionsContact() {

        listPermissionsNeededContact.clear();
        int writeContact = ContextCompat.checkSelfPermission(this, WRITE_CONTACTS);
        int readContact = ContextCompat.checkSelfPermission(this, READ_CONTACTS);

        if (writeContact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeededContact.add(Manifest.permission.WRITE_CONTACTS);
        }
        if (readContact != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeededContact.add(READ_CONTACTS);
        }

        if (!listPermissionsNeededContact.isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    protected void onResume() {
        //  Share.RestartApp(this);
        super.onResume();
        llRemoveAds = nvDrawer.findViewById(R.id.ll_nav_removeAds);
        if (Share.isNeedToAdShow(SelectionActivity.this)) {
            //   iv_remove_ad.setVisibility(View.VISIBLE);
            llRemoveAds.setVisibility(View.VISIBLE);
            //llShareApp.setVisibility(View.GONE);
        } else {
            //   iv_remove_ad.setVisibility(View.GONE);
            llRemoveAds.setVisibility(View.GONE);
            //llShareApp.setVisibility(View.VISIBLE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
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


            case STORAGE_PERMISSION_CODE_Contact:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CONTACTS)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

                        Log.e("TAG", "onRequestPermissionsResult: deny");

                    } else {
                        Log.e("TAG", "onRequestPermissionsResult: dont ask again");

                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setTitle("Permissions Required")
                                .setMessage("Please allow permission for Contacts")
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

                } else {
                    // Permission has already been granted
                    if (isFromContacts) {
                        image_name = "contacts";
                        Intent i = new Intent(SelectionActivity.this, AddContactActivity.class);
                        startActivity(i);
                    } else {
                        if (!Share.isNeedToAdShow(activity)) {
                            startActivity(new Intent(SelectionActivity.this, BackupActivity.class));
                        } else {
                            purchaseDialog();
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

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                    mDrawer.closeDrawer(GravityCompat.START);
                } else {
                    mDrawer.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_photos:
                if (checkAndRequestPermissions()) {
                    image_name = "gallery";
          /*  image_name = "gallery";
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, SelectPicture);*/
                    SharedPrefs.save(this, SharedPrefs.PickFromGallery, "pickimage");
                    Intent i = new Intent(SelectionActivity.this, HiddenImagesActivity.class);
                    startActivity(i);

                } else {
                    ActivityCompat.requestPermissions(SelectionActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);
                }
                break;
            case R.id.ll_videos:
                if (checkAndRequestPermissions()) {
                    image_name = "gallery";
          /*  image_name = "gallery";
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, SelectPicture);*/
                    SharedPrefs.save(this, SharedPrefs.PickFromGallery, "pickvideo");
                    Intent i = new Intent(SelectionActivity.this, HiddenVideosActivity.class);
                    startActivity(i);
                } else {
                    ActivityCompat.requestPermissions(SelectionActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);
                }
                break;

            case R.id.ll_appLock:
                String isDecoy = SharedPrefs.getString(SelectionActivity.this, SharedPrefs.DecoyPassword, "false");
                if (isDecoy.equals("true")) {
                    Toast.makeText(activity, "AppLock is not available in decoy.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!isAccessibilitySettingsOn(SelectionActivity.this)) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(SelectionActivity.this);
                    builder.setTitle("Accessibility Permission");
                    builder.setMessage("To use this feature, please allow access to Accessibility service permission.");
                    builder.setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                    }).setPositiveButton("Ok", (dialog, which) -> {
                        dialog.dismiss();
                        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));

                        new Handler().postDelayed(() -> {
                            Intent act = new Intent(SelectionActivity.this, PermissionActivity.class);
                            act.putExtra("for", "Accessibility");
                            startActivity(act);
                        }, 0);
                    });
                    builder.setCancelable(false);
                    builder.show();

                } else if (!hasPermissionToReadNetworkHistory()) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(SelectionActivity.this);
                    builder.setTitle("Usage Access Permission");
                    builder.setMessage("To use this feature, please allow access to usage access permission.");
                    builder.setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                    }).setPositiveButton("Ok", (dialog, which) -> {
                        dialog.dismiss();
                        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));

                        new Handler().postDelayed(() -> {
                            Intent act = new Intent(SelectionActivity.this, PermissionActivity.class);
                            act.putExtra("for", "Usage_Access");
                            startActivity(act);
                        }, 0);

                    });
                    builder.setCancelable(false);
                    builder.show();
                } else {
                    Intent i = new Intent(SelectionActivity.this, AppListActivity.class);
                    startActivity(i);
                }

                break;

            case R.id.ll_other:
                if (checkAndRequestPermissions()) {
                    image_name = "gallery";
          /*  image_name = "gallery";
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, SelectPicture);*/
                    SharedPrefs.save(this, SharedPrefs.PickFromGallery, "pickimage");
                    Intent i = new Intent(SelectionActivity.this, HiddenFilesActivity.class);
                    startActivity(i);

                } else {
                    ActivityCompat.requestPermissions(SelectionActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);
                }
                break;

            case R.id.ll_notes:
                if (checkAndRequestPermissions()) {
                    image_name = "notes";
                    Intent i = new Intent(SelectionActivity.this, NoteActivity.class);
                    startActivity(i);
                } else {
                    ActivityCompat.requestPermissions(SelectionActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);
                }
                break;
            case R.id.ll_contacts:
                isFromContacts = true;
                if (checkAndRequestPermissionsContact()) {
                    image_name = "contacts";
                    Intent i = new Intent(SelectionActivity.this, AddContactActivity.class);
                    startActivity(i);
                } else {
                    ActivityCompat.requestPermissions(SelectionActivity.this, listPermissionsNeededContact.toArray(new String[listPermissionsNeededContact.size()]), STORAGE_PERMISSION_CODE_Contact);
                }
                break;
            case R.id.ll_browser:
                Intent i = new Intent(SelectionActivity.this, ShowBrowserActivity.class);
                startActivity(i);
                break;
            case R.id.ll_credentials:
                Intent intent = new Intent(SelectionActivity.this, ShowCredentialsActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_back:
                onBackPressed();
                break;


            //navigation items click events are listed here...

            case R.id.ll_back_to_calculator:
                Share.isFromSelection = true;
                Intent loIntent1 = new Intent(SelectionActivity.this, CalculatorActivity.class);
                loIntent1.putExtra("isFrom", "app");
                startActivity(loIntent1);
                finish();
//                mDrawer.closeDrawer(Gravity.START,true);
                break;

            /*case R.id.ll_nav_browserSetting:
                Log.e(TAG, "onNavigationItemSelected: " + " nav_browser_setting");
                Intent browserintent = new Intent(SelectionActivity.this, BrowserSelectionActivity.class);
                startActivity(browserintent);
                try {
                    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        mDrawerLayout.closeDrawer(GravityCompat.START, true);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onClick: ll_nav_breakIn " + e.toString());
                }
                break;

            case R.id.ll_nav_lockSetting:
                Log.e(TAG, "onNavigationItemSelected: " + " nav_lockSetting");
                Intent intentpass = new Intent(SelectionActivity.this, ChangePasscodeActivity.class);
                startActivity(intentpass);
                try {
                    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        mDrawerLayout.closeDrawer(GravityCompat.START, true);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onClick: ll_nav_breakIn " + e.toString());
                }
                break;

            case R.id.ll_nav_breakIn:
                Intent iBreakIn = new Intent(SelectionActivity.this, BreakInReportActivity.class);
                startActivity(iBreakIn);
                Log.e(TAG, "onNavigationItemSelected: " + " nav_break_in_Report");

                try {
                    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        mDrawerLayout.closeDrawer(GravityCompat.START, true);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onClick: ll_nav_breakIn " + e.toString());
                }
                break;

            case R.id.ll_nav_decoyPass:
                Intent iDecoy = new Intent(SelectionActivity.this, DecoyPasscodeActivity.class);
                startActivity(iDecoy);
                Log.e(TAG, "onNavigationItemSelected: " + " nav_decoy");
                try {
                    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        mDrawerLayout.closeDrawer(GravityCompat.START, true);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onClick: ll_nav_breakIn " + e.toString());
                }
                break;

            case R.id.ll_nav_recoveryPass:
                Intent iRecoverPass = new Intent(SelectionActivity.this, HowToRecoverPassActivity.class);
                startActivity(iRecoverPass);
                // TODO Appnext Ads
//                interstitial_Ad.showAd();
                Log.e(TAG, "onNavigationItemSelected: " + " nav_recover_passcode");
                try {
                    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        mDrawerLayout.closeDrawer(GravityCompat.START, true);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onClick: ll_nav_breakIn " + e.toString());
                }
                break;

            case R.id.ll_nav_uninstall_protection:
                DevicePolicyManager mDevicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
                ComponentName mComponentName = new ComponentName(this, MyAdminReceiver.class);

                this.isAdmin = mDevicePolicyManager.isAdminActive(mComponentName);
                if (!this.isAdmin) {
                    Intent intent1 = new Intent("android.app.action.ADD_DEVICE_ADMIN");
                    intent1.putExtra("android.app.extra.DEVICE_ADMIN", mComponentName);
                    intent1.putExtra("android.app.extra.ADD_EXPLANATION", getResources().getString(R.string.description));
                    startActivityForResult(intent1, 111);
                } else {
                    Toast.makeText(activity, "Already activated.", Toast.LENGTH_SHORT).show();
//                    ComponentName devAdminReceiver = new ComponentName(SelectionActivity.this, DeviceAdminReceiver.class);
//                    DevicePolicyManager mDPM = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
//                    mDPM.removeActiveAdmin(devAdminReceiver);
                }
                break;*/

//            case R.id.ll_nav_contactUs:
//                Log.e(TAG, "onNavigationItemSelected: " + " nav_contact_us");
//                mDrawer.closeDrawer(Gravity.START, true);
//                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "info@vasundharavision.com"));
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
//                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
//                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//                break;

            case R.id.ll_nav_rateApp:
                Log.e(TAG, "onNavigationItemSelected: " + " nav_rate_us");
                try {
                    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        mDrawerLayout.closeDrawer(GravityCompat.START, true);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onClick: ll_nav_breakIn " + e.toString());
                }
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;

            case R.id.ll_nav_moreApps:
                Log.e(TAG, "onNavigationItemSelected: " + " nav_more_apps");
                mDrawer.closeDrawer(Gravity.LEFT, true);
                Intent iMoreApps = new Intent(SelectionActivity.this, HomePageActivity.class);
                startActivity(iMoreApps);
                break;

            /*case R.id.ll_nav_shareApp:
                Log.e("onClick", "onClick: onClick share ");
                try {
                    Intent iShare = new Intent(Intent.ACTION_SEND);
                    iShare.setType("text/plain");
                    iShare.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                    String sAux = "Download and give review for " + getString(R.string.app_name) + " app from play store\n\n\n https://play.google.com/store/apps/details?id=" + getPackageName() +
                            "\n\n";
                    iShare.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(iShare, "choose one"));
                } catch (Exception e) {
                    Log.e("Exception", "Exception in Share App" + e.getMessage());
                    e.toString();
                }
                break;

            case R.id.ll_nav_backup:
                isFromContacts = false;
                if (checkAndRequestPermissionsContact()) {
                    if (!Share.isNeedToAdShow(SelectionActivity.this)) {
                        startActivity(new Intent(SelectionActivity.this, BackupActivity.class));
                    } else {
                        purchaseDialog();
                    }
                } else {
                    ActivityCompat.requestPermissions(SelectionActivity.this, listPermissionsNeededContact.toArray(new String[listPermissionsNeededContact.size()]), STORAGE_PERMISSION_CODE_Contact);
                }
                break;*/

            case R.id.ll_nav_anti_lost:
                startActivity(new Intent(SelectionActivity.this, AntiLostGuidActivity.class));
                break;

            case R.id.ll_nav_removeAds:
                // purchaseItem();

                startActivity(new Intent(SelectionActivity.this, SubscriptionActivity.class));

                break;

            case R.id.iv_remove_ad:
                purchaseItem();
                break;
        }
    }

    private void purchaseDialog() {


        startActivity(new Intent(SelectionActivity.this, SubscriptionActivity.class));


       /* final DisplayMetrics displayMetrics = new DisplayMetrics();

        View view = (View) LayoutInflater.from(activity).inflate(R.layout.dialog_in_app_purchase, null, false);
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(view);
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        dialog.getWindow().setLayout(displayMetrics.widthPixels - 30, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);

        ImageView ivClose = view.findViewById(R.id.dPurchase_iv_close);
        ivClose.setOnClickListener(v -> dialog.dismiss());
        Button btnPurchase = view.findViewById(R.id.dPurchase_btn_purchase);
        btnPurchase.setOnClickListener(v -> {
            //set progress dialog and start the in app purchase process
            dialog.dismiss();
            upgradeDialog = ProgressDialog.show(SelectionActivity.this, "Please wait", "", true);

            *//* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. *//*
            String payload = "";

            billingProcessor.purchase(SelectionActivity.this, ProductKey, payload);

            upgradeDialog.dismiss();
        });

        dialog.show();*/

    }

    private void purchaseItem() {

        if (billingProcessor != null) {

            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(R.string.app_name)
                    .setMessage(getString(R.string.remove_ad_msg))
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        //set progress dialog and start the in app purchase process
                        upgradeDialog = ProgressDialog.show(SelectionActivity.this, "Please wait", "", true);

                        /* TODO: for security, generate your payload here for verification. See the comments on
                         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
                         *        an empty string, but on a production app you should carefully generate this. */
                        String payload = "";

                        billingProcessor.purchase(SelectionActivity.this, ProductKey, payload);

                        upgradeDialog.dismiss();
                    })
                    .setNegativeButton(getString(R.string.no), (dialog, which) -> {
                        dialog.cancel();
                        if (upgradeDialog != null && upgradeDialog.isShowing())
                            upgradeDialog.dismiss();
                    })
                    .setCancelable(false);

            final android.app.AlertDialog dirsDialog1 = alertDialogBuilder.create();
            dirsDialog1.show();
        } else {

            Log.e("TAG", "onClick: billPr == null");
            if (upgradeDialog != null && upgradeDialog.isShowing()) {
                upgradeDialog.dismiss();
            }
            Share.showAlert(SelectionActivity.this, getString(R.string.app_name), getString(R.string.something_wrong));
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (billingProcessor != null) {
            if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void removeAds() {
        Log.e(TAG, "removeAds: ads disable");
        //// TODO: 02/11/18 ramove banner ads

        //findViewById(R.id.adView).setVisibility(View.GONE);

        // iv_remove_ad.setVisibility(View.GONE);
        llRemoveAds.setVisibility(View.GONE);
        vRemoveAds.setVisibility(View.GONE);
       // llShareApp.setVisibility(View.VISIBLE);
        vShareApp.setVisibility(View.VISIBLE);
       // llMoreApps.setVisibility(View.GONE);
        vMoreApps.setVisibility(View.GONE);
//        findViewById(R.id.fl_adplaceholder_one).setVisibility(View.GONE);
//        findViewById(R.id.fl_adplaceholder_two).setVisibility(View.GONE);
        //findViewById(R.id.flBanner).setVisibility(View.GONE);
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        if (upgradeDialog != null && upgradeDialog.isShowing()) {
            upgradeDialog.dismiss();
        }

        Log.e("onProductPurchased", "Purchased");
        SharedPrefs.savePref(this, SharedPrefs.IS_ADS_REMOVED, true);
        removeAds();
        Share.showAlert(SelectionActivity.this, getString(R.string.app_name), getString(R.string.remove_ads_msg));
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

    private void openExitDialog() {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_finish_alert); //get layout from ExitDialog folder

        SmileRating smileRating = (SmileRating) dialog.findViewById(R.id.smile_rating);
        Button btn_yes = dialog.findViewById(R.id.btn_yes);
        Button btn_no = dialog.findViewById(R.id.btn_no);

        btn_no.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btn_yes.setOnClickListener(v -> {
            if (Share.isNeedToAdShow(activity)) {
                if (IntAdsHelper.isInterstitialLoaded()) {
                    IntAdsHelper.showInterstitialAd();
//                    TODO Admob Ads
//                    interstitial.show();
                } else {
                    Intent startMain = new Intent(activity, ExitActivity.class);
                    startActivity(startMain);
                }
            } else {
                Intent startMain = new Intent(activity, ExitActivity.class);
                startActivity(startMain);
            }

//            try {
//                dialog.dismiss();
//
//                if (SharedPrefs.getInt(activity, SharedPrefs.URL_INDEX) < EXIT_URLs.length) {
//                    EXIT_URL = EXIT_URLs[SharedPrefs.getInt(activity, SharedPrefs.URL_INDEX)];
//                    Intent ratingIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(EXIT_URL));
//                    activity.startActivity(ratingIntent);
//                }
//                if (SharedPrefs.getInt(activity, SharedPrefs.URL_INDEX) < (EXIT_URLs.length - 1))
//                    SharedPrefs.save(activity, SharedPrefs.URL_INDEX, SharedPrefs.getInt(activity, SharedPrefs.URL_INDEX) + 1);
//                else
//                    SharedPrefs.save(activity, SharedPrefs.URL_INDEX, 0);
//
//            } catch (ActivityNotFoundException e) {
//                // If play store not found then it open in browser
//                Intent ratingIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(EXIT_URL));
//                startActivity(ratingIntent);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        });

        smileRating.setOnSmileySelectionListener((smiley, reselected) -> {
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

    @Override
    public void onBackPressed() {
//        openExitDialog();
        if (!Share.isRateDisplayed) {
            if (moRatingDialog != null) {
                if (!SharedPrefs.getBoolean(SelectionActivity.this, Share.IS_RATED)) {
                    if (SharedPrefs.getInt(SelectionActivity.this, Share.RATE_APP_OPEN_COUNT) >= 4) {
                        if (!moRatingDialog.isShowing()) {
                            moRatingDialog = RatingDialog.SmileyExitDialog(SelectionActivity.this);
                            moRatingDialog.show();
                        }
                    } else {
                        exitDialog();
                    }
                } else {
                    exitDialog();
                }
            } else {
                if (!SharedPrefs.getBoolean(SelectionActivity.this, Share.IS_RATED)) {
                    if (SharedPrefs.getInt(SelectionActivity.this, Share.RATE_APP_OPEN_COUNT) >= 4) {
                        moRatingDialog = RatingDialog.SmileyExitDialog(SelectionActivity.this);
                        moRatingDialog.show();
                    } else {
                        exitDialog();
                    }
                } else {
                    exitDialog();
                }
            }
        } else {
            exitDialog();
        }
    }

    private void exitDialog() {

        final Dialog dialog = new Dialog(SelectionActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.exit_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnNo = dialog.findViewById(R.id.btn_no);
        Button btnYes = dialog.findViewById(R.id.btn_yes);

        if (Share.isNeedToAdShow(SelectionActivity.this)) {
            NativeAdvanceHelper.loadNativeAd(this, dialog.findViewById(R.id.fl_nativeAd), true);
        }

        btnNo.setOnClickListener(v -> dialog.dismiss());

        btnYes.setOnClickListener(v -> {

            dialog.dismiss();
            SharedPrefs.save(activity, Share.RATE_APP_OPEN_COUNT, SharedPrefs.getInt(activity, Share.RATE_APP_OPEN_COUNT) + 1);
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.finishAffinity();
            System.exit(0);

        });

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout((int) (0.9 * DisplayMetricsHandler.getScreenWidth()), Toolbar.LayoutParams.WRAP_CONTENT);

        if (dialog != null && !dialog.isShowing())
            dialog.show();

    }

    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + Accessibilty.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean hasPermissionToReadNetworkHistory() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        final AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        appOps.startWatchingMode(AppOpsManager.OPSTR_GET_USAGE_STATS,
                getApplicationContext().getPackageName(),
                new AppOpsManager.OnOpChangedListener() {
                    @Override
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    public void onOpChanged(String op, String packageName) {
                        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                                android.os.Process.myUid(), getPackageName());
                        if (mode != AppOpsManager.MODE_ALLOWED) {
                            return;
                        }
                        appOps.stopWatchingMode(this);
//                        Intent intent = new Intent(AppListActivity.this, MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                        getApplicationContext().startActivity(intent);
                    }
                });
        return false;
    }

}

