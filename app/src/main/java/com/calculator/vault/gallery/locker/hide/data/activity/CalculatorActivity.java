package com.calculator.vault.gallery.locker.hide.data.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.vending.billing.IInAppBillingService;
import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.CameraError;
import com.androidhiddencamera.HiddenCameraActivity;
import com.androidhiddencamera.HiddenCameraUtils;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;
import com.androidhiddencamera.config.CameraRotation;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.ads.AdsListener;
import com.calculator.vault.gallery.locker.hide.data.ads.IntAdsHelper;
import com.calculator.vault.gallery.locker.hide.data.appLock.Preferences;
import com.calculator.vault.gallery.locker.hide.data.appLock.ThemeModel;
import com.calculator.vault.gallery.locker.hide.data.billing.InAppBillingHandler;
import com.calculator.vault.gallery.locker.hide.data.common.FingerprintHandler;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.data.DecoyDatabase;
import com.calculator.vault.gallery.locker.hide.data.data.ImageVideoDatabase;
import com.calculator.vault.gallery.locker.hide.data.model.BreakInImageModel;
import com.calculator.vault.gallery.locker.hide.data.model.UserModel;
import com.calculator.vault.gallery.locker.hide.data.presenter.CalculatorContract;
import com.calculator.vault.gallery.locker.hide.data.presenter.CalculatorPresenter;
import com.calculator.vault.gallery.locker.hide.data.subscription.AdsPrefs;
import com.calculator.vault.gallery.locker.hide.data.subscription.MonthlySubscriptionActivity;
import com.google.gson.Gson;
import com.greedygame.android.agent.GreedyGameAgent;
import com.greedygame.android.core.campaign.CampaignStateListener;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import static com.calculator.vault.gallery.locker.hide.data.common.Share.databasewritepath;
import static com.calculator.vault.gallery.locker.hide.data.common.Share.isFromSelection;
import static com.calculator.vault.gallery.locker.hide.data.common.Share.snoopPicPath;

public class CalculatorActivity extends HiddenCameraActivity implements View.OnClickListener, CalculatorContract.View, BillingProcessor.IBillingHandler
        /*MoPubInterstitial.InterstitialAdListener*//*InterstitialAdHelper.onInterstitialAdListener*/, CampaignStateListener {

    private CalculatorActivity activity;
    private Button btn_0;
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    private Button btn_7;
    private Button btn_8;
    private Button btn_9;
    private Button btn_divide;
    private Button btn_multiply;
    private Button btn_minus;
    private Button btn_plus;
    private Button btn_equals;
    private Button btn_decimal;
    private Button btn_clearAll;
    private Button btn_sign;
    private Button btn_percent;
    private TextView tv_result;
    private TextView moTvFormula;
    String wholevalue = "";
    String wholevalue2 = "";
    Boolean isoperatorclicked = false;
    String TAG = this.getClass().getSimpleName();
    String operator;
    private CalculatorPresenter mCalculatorPresenter;
    private CameraConfig moCameraConfig;
    private String msSwitchActive;
    String msPasswordType;
    private List<String> listPermissionsNeeded = new ArrayList<>();
    public final int STORAGE_PERMISSION_CODE = 23;
    ImageVideoDatabase moDBimageVideoDatabase = new ImageVideoDatabase(this);
    DecoyDatabase decoyDatabase = new DecoyDatabase(this);
    private String EnterePassword;
    String msGetPassword, msConfirmPassword, msNewPassword;
    private TextView moTvPasswordType;

    // TODO: 02/02/19  In-app


    //Finger Print

    private KeyStore keyStore;
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "com.calculatorvault.backup";
    private Cipher cipher;

//    private boolean isInterstitialAdLoaded = false;
//    private MoPubInterstitial mInterstitial;
    // TODO Admob Ads
//    private InterstitialAd interstitial;

    //Todo:: GREEDY GAME INITIALIZATION...
    public static GreedyGameAgent greedyGame;

    // Mopub Ads Listener
    /*@Override
    public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {
        isInterstitialAdLoaded = true;
    }

    @Override
    public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {
        Log.e(TAG, "onInterstitialFailed: "+ moPubErrorCode);
        isInterstitialAdLoaded = false;
        if (Utils.isNetworkConnected(CalculatorActivity.this)) {
            mInterstitial.load();
        }
    }

    @Override
    public void onInterstitialShown(MoPubInterstitial moPubInterstitial) {

    }

    @Override
    public void onInterstitialClicked(MoPubInterstitial moPubInterstitial) {

    }

    @Override
    public void onInterstitialDismissed(MoPubInterstitial moPubInterstitial) {
        isInterstitialAdLoaded = false;
        Share.ChangePassword = false;
        Share.decoyPasscode = false;
        Intent intent = new Intent(CalculatorActivity.this, SelectionActivity.class);
        startActivity(intent);
        finish();
    }*/

    // TODO Admob Ads
//    @Override
//    public void onLoad() {
//        isInterstitialAdLoaded = true;
//    }
//
//    @Override
//    public void onFailed() {
//        isInterstitialAdLoaded = false;
//        if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id1))){
//            interstitial = InterstitialAdHelper.getInstance().load2(CalculatorActivity.this, this);
//        }else if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id2))){
//            interstitial = InterstitialAdHelper.getInstance().load(CalculatorActivity.this, this);
//        }else {
//            interstitial = InterstitialAdHelper.getInstance().load1(CalculatorActivity.this, this);
//        }
//    }
//
//    @Override
//    public void onClosed() {
//        isInterstitialAdLoaded = false;
//        //interstitial = InterstitialAdHelper.getInstance().load1(CalculatorActivity.this, this);
//        Share.ChangePassword = false;
//        Share.decoyPasscode = false;
//        Intent intent = new Intent(CalculatorActivity.this, SelectionActivity.class);
//        startActivity(intent);
//        finish();
//    }


    //TODO: In App Purchase
    BillingProcessor billingProcessor;
    String LicenseKey = "";
    private IInAppBillingService mService;
    private String productKeyMonth = "", productKeyMonthDiscount = "", productKeyYear = "";
    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
            checkSkuDetails();
        }
    };
    private AdsPrefs spHelper;

    private void checkSkuDetails() {

        try {


            // Description Msg
            String trialMsgDiscount = "Start monthly subscription with 3 days free trial for ₹500.00";
            String trialMsgMonth = "Start monthly subscription with 3 days free trial for ₹500.00";

            // Button Msg
            String trialBtnDiscount = "Start 3-Days FREE Trial";
            String trialBtnMonth = "Start 3-Days FREE Trial";


            // By default save default text for msgs and btn text
            AdsPrefs.save(activity, AdsPrefs.TRAIL_MSG_DISCOUNT, trialMsgDiscount);
            AdsPrefs.save(activity, AdsPrefs.TRAIL_MSG_MONTH, trialMsgMonth);
            AdsPrefs.save(activity, AdsPrefs.TRAIL_BTN_DISCOUNT, trialBtnDiscount);
            AdsPrefs.save(activity, AdsPrefs.TRAIL_BTN_MONTH, trialBtnMonth);


            /*
             *   Put all product key into list so that we can get details about purchase and
             * subscription
             * */
            ArrayList<String> skuList = new ArrayList<>();

            skuList.add(getResources().getString(R.string.ads_product_key_monthlydiscount));
            skuList.add(getResources().getString(R.string.ads_product_key_month));
            skuList.add(getResources().getString(R.string.ads_product_key_year));

            Bundle querySkus = new Bundle();
            querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

            Bundle owned_sub = mService.getSkuDetails(3, getPackageName(), "subs", querySkus);
            int responseSub = owned_sub.getInt("RESPONSE_CODE");

            /* Set up price */
            if (responseSub == 0) {
                ArrayList<String> responseList = owned_sub.getStringArrayList("DETAILS_LIST");

                Log.i(TAG, "checkSkuDetails: 11: " + responseList.toString());

                for (int i = 0; i < responseList.size(); i++) {
                    try {
                        JSONObject object = new JSONObject(responseList.get(i));
                        /* Month Discount Match */
                        if (object.getString("productId").equalsIgnoreCase(productKeyMonthDiscount)) {

                            /*
                             *   This price will use in subscribe screen to display the price
                             * country wise
                             * */
                            Log.e(TAG, "checkSkuDetails: MONTH DISCOUNT PRICE : " + object.getString("price"));
                            String priceMonthDiscount = object.getString("price");
                            AdsPrefs.save(activity, AdsPrefs.PRICE_MONTH_DISCOUNT, priceMonthDiscount);


                        }

                        /* Month Match */
                        if (object.getString("productId").equalsIgnoreCase(productKeyMonth)) {
                            Log.e(TAG, "checkSkuDetails: MONTH PRICE : " + object.getString("price"));
                            String priceYear = object.getString("price");
                            AdsPrefs.save(activity, AdsPrefs.PRICE_MONTH, priceYear);


                        }

                        /* Year Match */
                        if (object.getString("productId").equalsIgnoreCase(productKeyYear)) {
                            Log.e(TAG, "checkSkuDetails: YEAR PRICE : " + object.getString("price"));
                            String priceYear = object.getString("price");
                            AdsPrefs.save(activity, AdsPrefs.PRICE_YEAR, priceYear);


                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            if (billingProcessor != null) {

                /* Get Own Product it means in-app purchase
                 * if you have life time subscription then it is in-app purchase
                 *  */
                List<String> listBuyProducts = billingProcessor.listOwnedProducts();

                /*
                 * if you have any in-app purchase product then it gives size otherwise it will be 0
                 * */
                if (listBuyProducts.size() > 0) {
                    TransactionDetails purchasedTransactionDetails = billingProcessor.getPurchaseTransactionDetails(listBuyProducts.get(0));
                    if (purchasedTransactionDetails != null) {
                        if (purchasedTransactionDetails.productId.equalsIgnoreCase(productKeyYear)) if (billingProcessor.isPurchased(productKeyYear)) {
                            AdsPrefs.save(activity, AdsPrefs.IS_AUTO_RENEW_YEAR, purchasedTransactionDetails.purchaseInfo.purchaseData.autoRenewing);
                            AdsPrefs.save(activity, AdsPrefs.PURCHASED_PLAN_ID, "4");
                        }
                    } else {
                        AdsPrefs.save(activity, AdsPrefs.IS_AUTO_RENEW_YEAR, false);
                        AdsPrefs.save(activity, AdsPrefs.PURCHASED_PLAN_ID, "1");
                    }
                } else {

                    /*
                     *  To check user's subscription
                     * */
                    List<String> listBuySubscriptions = billingProcessor.listOwnedSubscriptions();
                    Log.e(TAG, "checkSkuDetails: listBuySubscriptions size ::  " + listBuySubscriptions.size());

                    /*
                     *   If user subscribed any product then it return size
                     * */
                    if (listBuySubscriptions.size() > 0) {

                        TransactionDetails subscriptionTransactionDetails = billingProcessor.getSubscriptionTransactionDetails(listBuySubscriptions.get(listBuySubscriptions.size() - 1));


                        /*if (billingProcessor.isSubscribed(listBuySubscriptions.get(listBuySubscriptions.size() - 1)) && billingProcessor.loadOwnedPurchasesFromGoogle()) {
                            Toast.makeText(activity, "true", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity, "false", Toast.LENGTH_SHORT).show();
                        }
*/

                        if (subscriptionTransactionDetails != null) {
                            Log.e(TAG, "checkSkuDetails:  developerPayload :: -> " + subscriptionTransactionDetails.purchaseInfo.purchaseData.developerPayload);
                            Log.e(TAG, "checkSkuDetails:  orderId :: -> " + subscriptionTransactionDetails.purchaseInfo.purchaseData.orderId);
                            Log.e(TAG, "checkSkuDetails:  productId :: -> " + subscriptionTransactionDetails.purchaseInfo.purchaseData.productId);
                            Log.e(TAG, "checkSkuDetails:  packageName :: -> " + subscriptionTransactionDetails.purchaseInfo.purchaseData.packageName);
                            Log.e(TAG, "checkSkuDetails:  purchaseToken :: -> " + subscriptionTransactionDetails.purchaseInfo.purchaseData.purchaseToken);
                            Log.e(TAG, "checkSkuDetails:  autoRenewing :: -> " + subscriptionTransactionDetails.purchaseInfo.purchaseData.autoRenewing);
                            Log.e(TAG, "checkSkuDetails:  purchaseTime :: -> " + subscriptionTransactionDetails.purchaseInfo.purchaseData.purchaseTime.toGMTString());
                            Log.e(TAG, "checkSkuDetails:  purchaseState :: -> " + subscriptionTransactionDetails.purchaseInfo.purchaseData.purchaseState);
                            Log.e(TAG, "checkSkuDetails:  responseData :: -> " + subscriptionTransactionDetails.purchaseInfo.responseData);
                            Log.e(TAG, "checkSkuDetails:  signature :: -> " + subscriptionTransactionDetails.purchaseInfo.signature);
                            Log.e(TAG, "checkSkuDetails:  describeContents :: -> " + subscriptionTransactionDetails.purchaseInfo.purchaseData.describeContents());
                        }


                        /*
                         *   If anu subscription is available it will return its details
                         * */
                        if (billingProcessor.isSubscribed(listBuySubscriptions.get(listBuySubscriptions.size() - 1)) && billingProcessor.loadOwnedPurchasesFromGoogle()) {


                            Log.e(TAG, "checkSkuDetails:  isSubscribed :: -> true");


                            Log.e(TAG, "checkSkuDetails: subscriptionTransactionDetails is not " + "null---- ");
                            if (subscriptionTransactionDetails.purchaseInfo.purchaseData.productId.equalsIgnoreCase(productKeyMonthDiscount)) {
                                Log.e(TAG, "checkSkuDetails: product week discount match ");
                                if (billingProcessor.isSubscribed(productKeyMonthDiscount)) {
                                    Log.e(TAG, "checkSkuDetails:  product week discount is subscribed --");
                                    // TODO Maulik: 2019-08-06 12:50  for check autorenuing is
                                    //  true or not for year
                                    AdsPrefs.save(activity, AdsPrefs.IS_AUTO_RENEW_MONTH_DISCOUNT, subscriptionTransactionDetails.purchaseInfo.purchaseData.autoRenewing);
                                    AdsPrefs.save(activity, AdsPrefs.PURCHASED_PLAN_ID, "5");
                                    SharedPrefs.savePref(this, SharedPrefs.IS_ADS_REMOVED, false);
                                } else {
                                    SharedPrefs.savePref(this, SharedPrefs.IS_ADS_REMOVED, true);
                                    Log.e(TAG, "checkSkuDetails:  Not Subscribe  week discount----");
                                }
                            } else if (subscriptionTransactionDetails.purchaseInfo.purchaseData.productId.equalsIgnoreCase(productKeyMonth)) {
                                Log.e(TAG, "checkSkuDetails: product month match ");
                                if (billingProcessor.isSubscribed(productKeyMonth)) {
                                    Log.e(TAG, "checkSkuDetails:  month is subscribed --");
                                    // TODO Maulik: 2019-08-06 12:50  for check autorenuing is
                                    //  true or not for year
                                    AdsPrefs.save(activity, AdsPrefs.IS_AUTO_RENEW_MONTH, subscriptionTransactionDetails.purchaseInfo.purchaseData.autoRenewing);
                                    AdsPrefs.save(activity, AdsPrefs.PURCHASED_PLAN_ID, "3");
                                    SharedPrefs.savePref(this, SharedPrefs.IS_ADS_REMOVED, false);
                                } else {
                                    SharedPrefs.savePref(this, SharedPrefs.IS_ADS_REMOVED, true);
                                    Log.e(TAG, "checkSkuDetails:  Not Subscribe Month----");
                                }
                            } else if (subscriptionTransactionDetails.purchaseInfo.purchaseData.productId.equalsIgnoreCase(productKeyYear)) {
                                Log.e(TAG, "checkSkuDetails: product year match ");
                                if (billingProcessor.isSubscribed(productKeyYear)) {
                                    Log.e(TAG, "checkSkuDetails:  year is subscribed --");
                                    // TODO Maulik: 2019-08-06 12:50  for check autorenuing is
                                    //  true or not for year
                                    AdsPrefs.save(activity, AdsPrefs.IS_AUTO_RENEW_YEAR, subscriptionTransactionDetails.purchaseInfo.purchaseData.autoRenewing);
                                    AdsPrefs.save(activity, AdsPrefs.PURCHASED_PLAN_ID, "4");
                                    SharedPrefs.savePref(this, SharedPrefs.IS_ADS_REMOVED, false);
                                } else {
                                    SharedPrefs.savePref(this, SharedPrefs.IS_ADS_REMOVED, true);
                                    Log.e(TAG, "checkSkuDetails:  Not Subscribe Year----");
                                }
                            }
                        } else {
                            Log.e(TAG, "checkSkuDetails:  isSubscribed :: -> false");
                            Log.e(TAG, "checkSkuDetails: subscriptionTransactionDetails is NULL " + "WHAT TO DO NOW !!!");
                            AdsPrefs.save(activity, AdsPrefs.IS_AUTO_RENEW_WEEK, false);
                            AdsPrefs.save(activity, AdsPrefs.IS_AUTO_RENEW_MONTH_DISCOUNT, false);
                            AdsPrefs.save(activity, AdsPrefs.IS_AUTO_RENEW_MONTH, false);
                            AdsPrefs.save(activity, AdsPrefs.IS_AUTO_RENEW_YEAR, false);
                            AdsPrefs.save(activity, AdsPrefs.PURCHASED_PLAN_ID, "1");
                            SharedPrefs.savePref(this, SharedPrefs.IS_ADS_REMOVED, true);
                        }
                    } else {
                        Log.e(TAG, "checkSkuDetails:  isSubscribed :: -> false");
                        Log.e(TAG, "checkSkuDetails: SIZE  zero of subscribe ------------");
                        AdsPrefs.save(activity, AdsPrefs.IS_AUTO_RENEW_WEEK, false);
                        AdsPrefs.save(activity, AdsPrefs.IS_AUTO_RENEW_MONTH_DISCOUNT, false);
                        AdsPrefs.save(activity, AdsPrefs.IS_AUTO_RENEW_MONTH, false);
                        AdsPrefs.save(activity, AdsPrefs.IS_AUTO_RENEW_YEAR, false);
                        AdsPrefs.save(activity, AdsPrefs.PURCHASED_PLAN_ID, "1");
                        SharedPrefs.savePref(this, SharedPrefs.IS_ADS_REMOVED, true);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        // TODO Mopub Mediation And Ads
        /*mInterstitial = new MoPubInterstitial(this, getString(R.string.mopub_int_id));
        mInterstitial.setInterstitialAdListener(this);

        // APPNEXT MEDIATION
        InterstitialConfig config = new InterstitialConfig();
        config.setButtonColor("#6AB344");
        config.setCategories("Categories");
        config.setBackButtonCanClose(false);
        config.setSkipText("Skip");
        config.setMute(false);
        config.setAutoPlay(true);
        config.setCreativeType(Interstitial.TYPE_STATIC);
        config.setOrientation(Ad.ORIENTATION_PORTRAIT);

        Map<String, Object> extras = new HashMap();
        extras.put(AppnextMoPubCustomEvent.AppnextConfigurationExtraKey, config);
        mInterstitial.setLocalExtras(extras);*/

//            mInterstitial.load();


//        TODO AdMob Ads
//        interstitial = InterstitialAdHelper.getInstance().load1(CalculatorActivity.this, this);

        activity = CalculatorActivity.this;
        spHelper = new AdsPrefs();

        IntAdsHelper.loadInterstitialAds(CalculatorActivity.this, new AdsListener() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onClosed() {
                Share.ChangePassword = false;
                Share.decoyPasscode = false;
                Log.i(TAG, "Launch Count: " + spHelper.getInt(activity, spHelper.appLaunchCount));
                if (spHelper.getInt(activity, spHelper.appLaunchCount) > 3) {
                    Intent intent = new Intent(CalculatorActivity.this, MonthlySubscriptionActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CalculatorActivity.this, SelectionActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });


        initView();
        mCalculatorPresenter = new CalculatorPresenter(this);
        initViewListener();
        initViewAction();
        loadThemes();
        //Finger Print
        if (!Share.changeDecoy && !Share.ChangePassword && !Share.decoyPasscode) {
            if (SharedPrefs.getBoolean(CalculatorActivity.this, SharedPrefs.isFingerLockOn)) {
                checkForFingerPrint();
            }
        }

        // TODO: 02/02/19 In-app
        initBillings();
        msSwitchActive = SharedPrefs.getString(CalculatorActivity.this, SharedPrefs.isSwitchActive, "false");

        if (msSwitchActive.equals("true")) {
            Log.e(TAG, "onCreate: " + "true");
            moCameraConfig = new CameraConfig()
                    .getBuilder(this)
                    .setCameraFacing(CameraFacing.FRONT_FACING_CAMERA)
                    .setCameraResolution(CameraResolution.MEDIUM_RESOLUTION)
                    .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                    .setImageRotation(CameraRotation.ROTATION_270)
                    .build();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                //Start camera preview
                startCamera(moCameraConfig);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
            }
        } else {
            Log.e(TAG, "onCreate: " + "true");
        }

        //Todo:: GREEDY GAME INITIALIZATION...
        greedyGame = new GreedyGameAgent.Builder(this)
                .enableAdmob(true) // To enable Admob
                .setGameId("84360092")
                .withAgentListener(this) // To get Campaign State callbacks// For News module (in between news list)
                .addUnitId("float-4028") // For Game module (at the bottom of game list)
                .addUnitId("float-4029") // For Learning Module
                .addUnitId("float-4035") // For Learning Module
//                .addUnitId("float-4030") // For Learning Module
//                .addUnitId("float-4031") // For Learning Module
                .build();
        greedyGame.init();

        Timer T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(() -> {
                    Log.e(TAG, "Refresh");
                    if (Share.isNeedToAdShow(CalculatorActivity.this)) {
                        greedyGame.startEventRefresh();
                    }
                });
            }
        }, 10000, 65000);


        String isFrom = getIntent().getStringExtra("isFrom");
        if (isFrom == null) {
            Log.e(TAG, "onCreate:  null");
            if (spHelper.getInt(activity, spHelper.appLaunchCount) <= 3) {
                spHelper.save(activity, spHelper.appLaunchCount, (spHelper.getInt(activity, spHelper.appLaunchCount) + 1));
            }
        } else {
            Log.e(TAG, "onCreate: not null");
        }

    }

    private void initBillings() {
        productKeyMonthDiscount = getString(R.string.ads_product_key_monthlydiscount);
        productKeyMonth = getString(R.string.ads_product_key_month);
        productKeyYear = getString(R.string.ads_product_key_year);
        LicenseKey = getString(R.string.licenseKey);

        billingProcessor = new BillingProcessor(activity, LicenseKey, this);
        billingProcessor.initialize();

        bindServices();
    }

    // TODO: 03/11/18 in-app purchase
    private void bindServices() {
        Log.e(TAG, "onCreate: ::::::::::::::::::::::::::::::::::");
        try {
//            bindService(InAppBillingHandler.getBindServiceIntent(), mServiceConn, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void checkForFingerPrint() {

        //Finger Print.....
        // Initializing both Android Keyguard Manager and Fingerprint Manager
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!fingerprintManager.isHardwareDetected()) {
                /**
                 * An error message will be displayed if the device does not contain the fingerprint hardware.
                 * However if you plan to implement a default authentication method,
                 * you can redirect the user to a default authentication activity from here.
                 * Example:
                 * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
                 * startActivity(intent);
                 */
                Log.e("FINGER_PRINT", "Your Device does not have a Fingerprint Sensor");
            } else {
                // Checks whether fingerprint permission is set on manifest
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                    Log.e("FINGER_PRINT", "Fingerprint authentication permission not enabled");
                } else {
                    // Check whether at least one fingerprint is registered
                    if (!fingerprintManager.hasEnrolledFingerprints()) {
                        Log.e("FINGER_PRINT", "Register at least one fingerprint in Settings");
                    } else {
                        // Checks whether lock screen security is enabled or not
                        if (!keyguardManager.isKeyguardSecure()) {
                            Log.e("FINGER_PRINT", "Lock screen security not enabled in Settings");
                        } else {
                            generateKey();
                            if (cipherInit()) {
                                FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                FingerprintHandler helper = new FingerprintHandler(this, false);
                                helper.startAuth(fingerprintManager, cryptoObject);
                            }
                        }
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    private void initViewAction() {
        msGetPassword = tv_result.getText().toString();

        if (Share.decoyPasscode) {
            //If Decoy Switch is activated new Set Password
            Log.e("TAG:: ", "decoyPasscode: " + "else");
            moTvPasswordType.setText(getString(R.string.newPass));
            moTvPasswordType.setVisibility(View.VISIBLE);
            moTvFormula.setVisibility(View.INVISIBLE);
            msPasswordType = getString(R.string.NewPassword);
            btn_percent.setText("Done\n%");
        } else {
            if (Share.ChangePassword) {
                //Change password clicked to set new Password
                Log.e("TAG:: ", "ChangePassword: " + "else");
                moTvPasswordType.setVisibility(View.VISIBLE);
                moTvFormula.setVisibility(View.INVISIBLE);
                msPasswordType = getString(R.string.NewPassword);
                moTvPasswordType.setText(getString(R.string.newPass));
                btn_percent.setText("Done\n%");
            } else {
                if (checkAndRequestPermissions()) {
                    if (CheckFileExists() && (moDBimageVideoDatabase.getUserTableCount() > 0)) {
                        msPasswordType = getString(R.string.OldPassword);
                        moTvPasswordType.setVisibility(View.INVISIBLE);
                        moTvFormula.setVisibility(View.VISIBLE);
                        moTvPasswordType.setText(getString(R.string.oldPass));
                        btn_percent.setText("Done\n%");
                    } else {
                        Log.e("TAG:: ", "initViewAction: " + "else");
                        moTvPasswordType.setVisibility(View.VISIBLE);
                        moTvFormula.setVisibility(View.INVISIBLE);
                        msPasswordType = getString(R.string.NewPassword);
                        moTvPasswordType.setText(getString(R.string.newPass));
                        moTvFormula.setText("");
                        btn_percent.setText("Done\n%");
                    }
                } else {
                    ActivityCompat.requestPermissions(CalculatorActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 159);
                }
            }
        }
    }

    private void initViewListener() {
        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_divide.setOnClickListener(this);
        btn_multiply.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_plus.setOnClickListener(this);
        btn_decimal.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_equals.setOnClickListener(this);
        btn_percent.setOnClickListener(this);
        btn_sign.setOnClickListener(this);
        btn_clearAll.setOnClickListener(this);
    }

    private void initView() {

        btn_0 = findViewById(R.id.btn_0);
        btn_1 = findViewById(R.id.btn_1);
        btn_2 = findViewById(R.id.btn_2);
        btn_3 = findViewById(R.id.btn_3);
        btn_4 = findViewById(R.id.btn_4);
        btn_5 = findViewById(R.id.btn_5);
        btn_6 = findViewById(R.id.btn_6);
        btn_7 = findViewById(R.id.btn_7);
        btn_8 = findViewById(R.id.btn_8);
        btn_9 = findViewById(R.id.btn_9);
        btn_divide = findViewById(R.id.btn_divide);
        btn_multiply = findViewById(R.id.btn_multiply);
        btn_minus = findViewById(R.id.btn_minus);
        btn_plus = findViewById(R.id.btn_plus);
        btn_decimal = findViewById(R.id.btn_decimal);
        btn_percent = findViewById(R.id.btn_percent);
        btn_equals = findViewById(R.id.btn_equals);
        btn_sign = findViewById(R.id.btn_sign);
        btn_clearAll = findViewById(R.id.btn_clearAll);
        moTvFormula = findViewById(R.id.tv_formula);
        tv_result = findViewById(R.id.tv_result);
        moTvPasswordType = findViewById(R.id.tv_passwordType);

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (permissions.length == 0) {
                return;
            }
            boolean allPermissionsGranted = true;
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false;
                        break;
                    }
                }
            }
            if (!allPermissionsGranted) {
                boolean somePermissionsForeverDenied = false;
                for (String permission : permissions) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        //denied
                        if (requestCode == 2) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 2);
                            break;
                        }
                    } else {
                        if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                            //allowed
                            Log.e("allowed", permission);
                        } else {
                            //set to never ask again
                            Log.e("set to never ask again", permission);
                            somePermissionsForeverDenied = true;
                        }
                    }
                }
                if (somePermissionsForeverDenied) {

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Permissions Required");
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        alertDialogBuilder.setMessage("Please allow permission for " + "Storage");
                    } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        alertDialogBuilder.setMessage("Please allow permission for " + "Camera");
                    }
                    alertDialogBuilder.setPositiveButton("Cancel", (dialog, which) -> {
                    }).setNegativeButton("Ok", (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }).setCancelable(false).create().show();
                }
            }
        }

        if (requestCode == 159) {
            ArrayList<Boolean> perm = new ArrayList<>();
            ArrayList<Boolean> perm1 = new ArrayList<>();
            boolean storageForceDeny = false;
            boolean recordForceDeny = false;
            for (int grantResult : grantResults) {
                perm.add(grantResult == PackageManager.PERMISSION_GRANTED);
            }
            System.out.println("==========permissiopn--->>>>>>> " + perm);
            if (!perm.contains(false)) {
                // accecpt
            } else {

                for (int j = 0; j < listPermissionsNeeded.size(); j++) {
                    perm1.add(shouldShowRequestPermissionRationale(listPermissionsNeeded.get(j)));
                }
                if (perm1.contains(true)) {
                    Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        System.out.println("=======" + perm.get(1) + "....." + perm1.get(1));
                        System.out.println("=======" + perm.get(0) + "....." + perm1.get(0));

                        if (perm.get(1) == perm1.get(1)) {
                            storageForceDeny = true;
                        }
                        if (perm.get(0) == perm1.get(0)) {
                            recordForceDeny = true;
                        }
                    } catch (Exception e) {
                        if (listPermissionsNeeded.get(0).equals("android.permission.WRITE_EXTERNAL_STORAGE")) {
                            storageForceDeny = true;
                        } else if (listPermissionsNeeded.get(0).equals("android.permission.CAMERA")) {
                            recordForceDeny = true;
                        }
                    }
                    dontAskAgain(storageForceDeny, recordForceDeny);
                    storageForceDeny = false;
                    recordForceDeny = false;
                }
            }
        }
    }

    private void dontAskAgain(boolean storageForceDeny, boolean recordForceDeny) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Permissions Required");

        if (storageForceDeny && recordForceDeny) {
            builder.setMessage("Please allow permission for Camera and Storage.");
        } else if (storageForceDeny) {
            builder.setMessage("Please allow permission for Storage.");
        } else if (recordForceDeny) {
            builder.setMessage("Please allow permission for Camera.");
        }

        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(intent);
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_0:
                if (!isoperatorclicked) {
                    wholevalue = wholevalue + getString(R.string.zero);
                    tv_result.setText(wholevalue);
                } else {
                    wholevalue2 = wholevalue2 + getString(R.string.zero);
                    tv_result.setText(wholevalue2);
                }
                mCalculatorPresenter.onOperatorAdd(getString(R.string.zero));
                break;
            case R.id.btn_1:
                if (!isoperatorclicked) {
                    wholevalue = wholevalue + getString(R.string.one);
                    tv_result.setText(wholevalue);
                } else {
                    wholevalue2 = wholevalue2 + getString(R.string.one);
                    tv_result.setText(wholevalue2);
                }
                mCalculatorPresenter.onOperatorAdd(getString(R.string.one));
                break;
            case R.id.btn_2:
                if (!isoperatorclicked) {
                    wholevalue = wholevalue + getString(R.string.two);
                    tv_result.setText(wholevalue);
                } else {
                    wholevalue2 = wholevalue2 + getString(R.string.two);
                    tv_result.setText(wholevalue2);
                }
                mCalculatorPresenter.onOperatorAdd(getString(R.string.two));
                break;
            case R.id.btn_3:
                if (!isoperatorclicked) {
                    wholevalue = wholevalue + getString(R.string.three);
                    tv_result.setText(wholevalue);
                } else {
                    wholevalue2 = wholevalue2 + getString(R.string.three);
                    tv_result.setText(wholevalue2);
                }
                mCalculatorPresenter.onOperatorAdd(getString(R.string.three));
                break;
            case R.id.btn_4:
                if (!isoperatorclicked) {
                    wholevalue = wholevalue + getString(R.string.four);
                    tv_result.setText(wholevalue);
                } else {
                    wholevalue2 = wholevalue2 + getString(R.string.four);
                    tv_result.setText(wholevalue2);
                }
                mCalculatorPresenter.onOperatorAdd(getString(R.string.four));
                break;
            case R.id.btn_5:
                if (!isoperatorclicked) {
                    wholevalue = wholevalue + getString(R.string.five);
                    tv_result.setText(wholevalue);
                } else {
                    wholevalue2 = wholevalue2 + getString(R.string.five);
                    tv_result.setText(wholevalue2);
                }
                mCalculatorPresenter.onOperatorAdd(getString(R.string.five));
                break;
            case R.id.btn_6:
                if (!isoperatorclicked) {
                    wholevalue = wholevalue + getString(R.string.six);
                    tv_result.setText(wholevalue);
                } else {
                    wholevalue2 = wholevalue2 + getString(R.string.six);
                    tv_result.setText(wholevalue2);
                }
                mCalculatorPresenter.onOperatorAdd(getString(R.string.six));
                break;
            case R.id.btn_7:
                if (!isoperatorclicked) {
                    wholevalue = wholevalue + getString(R.string.seven);
                    tv_result.setText(wholevalue);
                } else {
                    wholevalue2 = wholevalue2 + getString(R.string.seven);
                    tv_result.setText(wholevalue2);
                }
                mCalculatorPresenter.onOperatorAdd(getString(R.string.seven));
                break;
            case R.id.btn_8:
                if (!isoperatorclicked) {
                    wholevalue = wholevalue + getString(R.string.eight);
                    tv_result.setText(wholevalue);
                } else {
                    wholevalue2 = wholevalue2 + getString(R.string.eight);
                    tv_result.setText(wholevalue2);
                }
                mCalculatorPresenter.onOperatorAdd(getString(R.string.eight));
                break;
            case R.id.btn_9:
                if (!isoperatorclicked) {
                    wholevalue = wholevalue + getString(R.string.nine);
                    tv_result.setText(wholevalue);
                } else {
                    wholevalue2 = wholevalue2 + getString(R.string.nine);
                    tv_result.setText(wholevalue2);
                }
                mCalculatorPresenter.onOperatorAdd(getString(R.string.nine));
                break;
            case R.id.btn_divide:
                if (!Pattern.matches(".*\\/0([^.]|$|\\.(0{4,}.*|0{1,4}([^0-9]|$))).*", moTvFormula.getText().toString().trim())) {
                    isoperatorclicked = true;
                    wholevalue = "";
                    wholevalue2 = "";
                    if (!moTvFormula.getText().toString().trim().isEmpty()) {
                        mCalculatorPresenter.onOperatorAdd(getString(R.string.divide_operator));
                    }
                } else if (!moTvFormula.getText().toString().trim().isEmpty()) {
                    Toast.makeText(activity, "Invalid Expression.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_multiply:
                if (!Pattern.matches(".*\\/0([^.]|$|\\.(0{4,}.*|0{1,4}([^0-9]|$))).*", moTvFormula.getText().toString().trim())) {
                    isoperatorclicked = true;
                    wholevalue = "";
                    wholevalue2 = "";
                    if (!moTvFormula.getText().toString().trim().isEmpty()) {
                        mCalculatorPresenter.onOperatorAdd(getString(R.string.multiply_expression));
                    }
                } else if (!moTvFormula.getText().toString().trim().isEmpty()) {
                    Toast.makeText(activity, "Invalid Expression.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_plus:
                if (!Pattern.matches(".*\\/0([^.]|$|\\.(0{4,}.*|0{1,4}([^0-9]|$))).*", moTvFormula.getText().toString().trim())) {
                    isoperatorclicked = true;
                    wholevalue = "";
                    wholevalue2 = "";
                    if (!moTvFormula.getText().toString().trim().isEmpty()) {
                        mCalculatorPresenter.onOperatorAdd(getString(R.string.plus));
                    }
                } else if (!moTvFormula.getText().toString().trim().isEmpty()) {
                    Toast.makeText(activity, "Invalid Expression.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_minus:
                if (!Pattern.matches(".*\\/0([^.]|$|\\.(0{4,}.*|0{1,4}([^0-9]|$))).*", moTvFormula.getText().toString().trim())) {
                    isoperatorclicked = true;
                    wholevalue = "";
                    wholevalue2 = "";
                    if (!moTvFormula.getText().toString().trim().isEmpty()) {
                        mCalculatorPresenter.onOperatorAdd(getString(R.string.minus));
                    }
                } else if (!moTvFormula.getText().toString().trim().isEmpty()) {
                    Toast.makeText(activity, "Invalid Expression.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_percent:
                isoperatorclicked = true;
                wholevalue = "";
                wholevalue2 = "";
                msGetPassword = tv_result.getText().toString().trim();
                Log.e(TAG, "onClick: msGetPassword:: " + msGetPassword);
                Log.e(TAG, "onClick: length:: " + msGetPassword.length());
                Log.e(TAG, "Formula: " + moTvFormula.getText().toString().trim());

                if (!Pattern.matches(".*\\/0([^.]|$|\\.(0{4,}.*|0{1,4}([^0-9]|$))).*", moTvFormula.getText().toString().trim())) {

                    if (moTvFormula.getText().toString().trim().length() == 4) {
                        if (msGetPassword.length() == 4) {
                            if (checkAndRequestPermissions()) {
                                if (Share.ChangePassword || Share.decoyPasscode) {
                                    if (msGetPassword.equalsIgnoreCase("0000")) {
                                        mCalculatorPresenter.onClearExpression();
                                        Toast.makeText(activity, "This password is not valid.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    if (msGetPassword.length() > 4) {
                                        mCalculatorPresenter.onClearExpression();
                                        Toast.makeText(activity, "Enter 4 digit password only", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    UserModel loDecoyModel = decoyDatabase.getSingleUserData(1);
                                    if (msGetPassword.equals(loDecoyModel.getPassword())) {
                                        if (Share.ChangePassword) {
                                            mCalculatorPresenter.onClearExpression();
                                            Toast.makeText(activity, "You can not set decoy password as main password.", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                    }

                                    moDBimageVideoDatabase = new ImageVideoDatabase(CalculatorActivity.this);
                                    UserModel loUserModel = moDBimageVideoDatabase.getSingleUserData(1);
                                    if (msGetPassword.equals(loUserModel.getPassword())) {
                                        if (Share.ChangePassword) {
                                            mCalculatorPresenter.onClearExpression();
                                            Toast.makeText(activity, "You can not set old password.", Toast.LENGTH_LONG).show();
                                        } else {
                                            mCalculatorPresenter.onClearExpression();
                                            Toast.makeText(activity, "You can not set main password as decoy password.", Toast.LENGTH_LONG).show();
                                        }
                                        return;
                                    }

                                    Log.e("TAG:: ", "btn_percent: " + "else");
                                    boolean isConfirm = false;
                                    if (msPasswordType.equals(getString(R.string.ConfirmPassword))) {
                                        Log.e(TAG, "onClick: btn_percent: if" + msPasswordType);
                                        if (!msNewPassword.equals(msGetPassword)) {
                                            tv_result.setText("");
                                            moTvFormula.setText("");
                                            msGetPassword = "";
                                            mCalculatorPresenter.onClearExpression();
                                            Toast.makeText(activity, "Password must be same.", Toast.LENGTH_SHORT).show();
                                            return;
                                        } else {
                                            isConfirm = true;
                                        }
                                    } else { //Change Password
                                        Log.e(TAG, "onClick: btn_percent: else " + msPasswordType);
                                        msPasswordType = getString(R.string.ConfirmPassword);
                                        tv_result.setText("");
                                        msNewPassword = msGetPassword;
                                    }
                                    moTvPasswordType.setText(getString(R.string.confPass));
                                    moTvFormula.setText("");

                                    if (isConfirm) {
                                        msConfirmPassword = tv_result.getText().toString();
                                        CheckButtonClick();
                                    } else {
                                        mCalculatorPresenter.onClearExpression();
                                    }
                                } else {
                                    // Login Time
                                    if (CheckFileExists() && (moDBimageVideoDatabase.getUserTableCount() > 0)) {
                                        msPasswordType = getString(R.string.OldPassword);
                                        moTvPasswordType.setText(getString(R.string.oldPass));
                                        if (msGetPassword.equalsIgnoreCase("0000")) {
                                            Intent iForgotPasscode = new Intent(CalculatorActivity.this, SecurityActivity.class);
                                            iForgotPasscode.putExtra("fromWhere", "forgotPass");
                                            startActivity(iForgotPasscode);
                                        }
                                    } else {
                                        if (msGetPassword.equalsIgnoreCase("0000")) {
                                            Toast.makeText(activity, "This password is not valid.", Toast.LENGTH_SHORT).show();
                                            return;
                                        } else {
                                            Log.e("TAG:: ", "btn_percent: " + "else");
                                            if (msPasswordType.equals(getString(R.string.ConfirmPassword))) {
                                                Log.e(TAG, "onClick: btn_percent: if" + msPasswordType);
                                                if (!msGetPassword.equals(msNewPassword)) {
                                                    tv_result.setText("");
                                                    moTvFormula.setText("");
                                                    msGetPassword = "";
                                                    mCalculatorPresenter.onClearExpression();
                                                    Toast.makeText(activity, "Password must be same.", Toast.LENGTH_SHORT).show();
                                                    return;
                                                } else {
                                                    msConfirmPassword = msGetPassword;
                                                }
                                            } else {
                                                Log.e(TAG, "onClick: btn_percent: else " + msPasswordType);
                                                msPasswordType = getString(R.string.NewPassword);
                                                msNewPassword = msGetPassword;
                                            }
                                            moTvPasswordType.setText(getString(R.string.newPass));
                                        }
                                    }
                                    CheckButtonClick();
                                }
                            } else {
                                ActivityCompat.requestPermissions(CalculatorActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 159);
                            }
                        } else {
                            if (Share.ChangePassword || Share.decoyPasscode) {
                                tv_result.setText("");
                                mCalculatorPresenter.onClearExpression();
                                Toast.makeText(activity, "Enter 4 digit password only", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!msGetPassword.isEmpty()) {
                                    mCalculatorPresenter.onOperatorAdd(getString(R.string.percentage));
                                    mCalculatorPresenter.onCalculateResult();
                                }
                            }
                        }
                    } else {
                        if (Share.ChangePassword || Share.decoyPasscode) {
                            tv_result.setText("");
                            mCalculatorPresenter.onClearExpression();
                            Toast.makeText(activity, "Enter 4 digit password only", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!msGetPassword.isEmpty()) {
                                mCalculatorPresenter.onOperatorAdd(getString(R.string.percentage));
                                mCalculatorPresenter.onCalculateResult();
                            }
                        }
                    }

                } else {
                    Toast.makeText(activity, "Invalid Expression.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_decimal:
                mCalculatorPresenter.onOperatorAdd(getString(R.string.comma_expression));
                break;
            case R.id.btn_clearAll:
                wholevalue = "";
                wholevalue2 = "";
                moTvFormula.setText("");
                mCalculatorPresenter.onClearExpression();
                break;
            case R.id.btn_equals:
                try {
                    if (!Pattern.matches(".*\\/0([^.]|$|\\.(0{4,}.*|0{1,4}([^0-9]|$))).*", moTvFormula.getText().toString().trim()) && !moTvFormula.getText().toString().trim().isEmpty()) {
                        wholevalue = "";
                        wholevalue2 = "";
                        mCalculatorPresenter.onCalculateResult();
                    } else if (!moTvFormula.getText().toString().trim().isEmpty()) {
                        Toast.makeText(activity, "Invalid Expression.", Toast.LENGTH_SHORT).show();
                    }
                } catch (ArithmeticException ax) {
                    if (ax.getMessage().equalsIgnoreCase("Division by zero")) {
                        tv_result.setText("Can't divide by 0");
                    }
                    Log.e(TAG, "onResult: " + ax.getMessage());
                }
                break;
            case R.id.btn_sign:
                if (!Pattern.matches(".*\\/0([^.]|$|\\.(0{4,}.*|0{1,4}([^0-9]|$))).*", moTvFormula.getText().toString().trim())) {
                    if (!tv_result.getText().toString().trim().isEmpty()) {
                        isoperatorclicked = true;
                        mCalculatorPresenter.onExpressionSignChange();
                        mCalculatorPresenter.onCalculateResult();
                        wholevalue = tv_result.getText().toString();
                        wholevalue2 = tv_result.getText().toString();
                    }
                } else if (!moTvFormula.getText().toString().trim().isEmpty()) {
                    Toast.makeText(activity, "Invalid Expression.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkAndRequestPermissions() {
        listPermissionsNeeded.clear();

        int audio = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (audio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (write != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        Log.e("TAG", "listPermissionsNeeded===>" + listPermissionsNeeded);
        return listPermissionsNeeded.isEmpty();
    }

    private double calculateString(String valueEntered) {
        Log.e(TAG, "calculateString: valueEntered: " + valueEntered);
        String value[] = valueEntered.split(operator);
        double total = 0;

        int a = Integer.parseInt(value[0]);
        int b = Integer.parseInt(value[1]);
        switch (operator) {
            case "+":
                total = a + b;
                break;
            case "-":
                total = a - b;
                break;
            case "/":
                total = a / b;
                break;
            case "*":
                total = a * b;
                break;
        }

        return total;
    }

    @Override
    public void showResult(String stringResult) {
        tv_result.setText(stringResult);
    }

    @Override
    public void updateCurrentExpression(String currentStringExpression) {
        moTvFormula.setText(currentStringExpression);
    }

    @Override
    public void showInvalidExpressionMessage() {
        Toast.makeText(this, getString(R.string.invalid_expression_message), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onImageCapture(@NonNull File imageFile) {

        Log.e(TAG, "onImageCapture: " + imageFile.getAbsolutePath());
        Log.e(TAG, "onImageCapture: " + imageFile.getName());
        //if(isDecode.equals())

        File path = new File(snoopPicPath);
        if (!path.exists())
            path.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_hhmmssSSS").format(new Date());

        File from = new File(imageFile.getParent(), imageFile.getName());

        //Log.e("TAG", "onActivityResult: name " +filename[0]);
        //  String fileNameWithOutExt = FilenameUtils.removeExtension(filename1);
        File to = new File(imageFile.getParent(), "snoopfile" + timeStamp + "." + FilenameUtils.getExtension(from.getName()));
        // File to = new File(file.getParent(), "file" + timeStamp + "." + fileNameExt);
        from.renameTo(to);
        Log.e("TAG:: ", "onActivityResult: " + to.getName());
        copyPhotoTo(to.getAbsolutePath(), snoopPicPath, to.getName());
        File file = new File(snoopPicPath + to.getName());

        String filename = file.getName();
        BreakInImageModel breakInImageModel = new BreakInImageModel();


        String dataTime = getDate(file.lastModified(), "dd/MM/yyyy hh:mm:ss a");
        breakInImageModel.setFilename(file.getName());
        breakInImageModel.setFilePath(file.getAbsolutePath());
        breakInImageModel.setDataTime(dataTime);
        breakInImageModel.setWrongPassword(EnterePassword);

        moDBimageVideoDatabase.addBreakInReportData(breakInImageModel);

        File delFile = new File(imageFile.getAbsolutePath());
        try {
            delFile.delete();
            to.delete();
        } catch (Exception e) {
            Log.e(TAG, "onImageCapture: delete Exception" + e.getMessage());
        }
        Uri contentUri = Uri.fromFile(to);

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);

        MediaScannerConnection.scanFile(CalculatorActivity.this, new String[]{to.getAbsolutePath()}, null, (s, uri) -> Log.e("TAG", "onScanCompleted: "));
    }

    public void copyPhotoTo(String pathToWatch, String pathToWrite, String str) {
        File path = new File(pathToWrite);
        if (!path.exists())
            path.mkdirs();
        // File file = new File(pathToWrite + str);
        //  if (!file.exists()) {
        try {
            File loFile = new File(pathToWrite + str);
            if (!loFile.exists())
                loFile.createNewFile();

            FileUtils.copyFile(new File(pathToWatch), new File(pathToWrite + str));
            Log.e("TAG", "CopyFileDone: " + str);
        } catch (IOException e) {
            Log.e("TAG", "onEvent: Error");
            e.printStackTrace();
        }
        // }
    }

    @Override
    public void onCameraError(int errorCode) {
        switch (errorCode) {
            case CameraError.ERROR_CAMERA_OPEN_FAILED:
                //Camera open failed. Probably because another application
                Log.e(TAG, "onCameraError: " + "ERROR_CAMERA_OPEN_FAILED");
                //is using the camera
                break;
            case CameraError.ERROR_IMAGE_WRITE_FAILED:
                Log.e(TAG, "onCameraError: " + "ERROR_IMAGE_WRITE_FAILED");
                //Image write failed. Please check if you have provided WRITE_EXTERNAL_STORAGE permission
                break;
            case CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE:
                Log.e(TAG, "onCameraError: " + "ERROR_CAMERA_PERMISSION_NOT_AVAILABLE");
                //camera permission is not available
                //Ask for the camera permission before initializing it.
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION:
                Log.e(TAG, "onCameraError: " + "ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION");
                //Display information dialog to the user with steps to grant "Draw over other app"
                //permission for the app.
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA:
                Log.e(TAG, "onCameraError: " + "ERROR_DOES_NOT_HAVE_FRONT_CAMERA");
                Toast.makeText(this, "Your device does not have front camera.", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        /*    System.out.println(getDate(82233213123L, "dd/MM/yyyy hh:mm:ss.SSS"));
         */
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private void checkPassword(boolean isDecoy) {
        Log.e("TAG", "checkPassword: " + "called");
        if (msGetPassword.length() == 4) {
            if (!isDecoy) {
                ImageVideoDatabase loDBImageVideo = new ImageVideoDatabase(CalculatorActivity.this);
                UserModel loUserModel = loDBImageVideo.getSingleUserData(1);
                Log.e(TAG, "UserModel: " + loUserModel.getPassword());
                if (loUserModel.getAnswer() == null) {
                    moTvFormula.setText("");
                    tv_result.setText("");
                    Intent intent = new Intent(CalculatorActivity.this, SecurityActivity.class);
                    intent.putExtra("fromWhere", "init");
                    startActivity(intent);
                } else {
                    Log.e("TAG:: ", "checkPassword: from Db:: ");
                    moTvFormula.setText("");
                    tv_result.setText("");
                    loadAd();
                }
            } else {
                if (SharedPrefs.getString(CalculatorActivity.this, SharedPrefs.isDecodeSwitchActive, "false").equalsIgnoreCase("true")) {
                    Log.e("TAG:: ", "checkPassword: from Db:: ");
                    moTvFormula.setText("");
                    tv_result.setText("");
                    loadAd();
                } else {
                    mCalculatorPresenter.onOperatorAdd(getString(R.string.percentage));
                    mCalculatorPresenter.onCalculateResult();
                }
            }
        } else {
            Toast.makeText(this, "Please Enter a 4 Digit Password", Toast.LENGTH_SHORT).show();
            Log.e("TAG:: ", "checkPassword: " + "Enter 4 digit Password");
        }
    }

    private void loadAd() {
        if (Share.isNeedToAdShow(this)) {
            if (IntAdsHelper.isInterstitialLoaded()) {
                IntAdsHelper.showInterstitialAd();
            } else {
                Share.ChangePassword = false;
                Share.decoyPasscode = false;
                Log.i(TAG, "Launch Count: " + spHelper.getInt(activity, spHelper.appLaunchCount));
                if (spHelper.getInt(activity, spHelper.appLaunchCount) > 3) {
                    Intent intent = new Intent(CalculatorActivity.this, MonthlySubscriptionActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(CalculatorActivity.this, SelectionActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        } else {
            Share.ChangePassword = false;
            Share.decoyPasscode = false;
            Intent intent = new Intent(CalculatorActivity.this, SelectionActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private boolean CheckFileExists() {
        File file = new File(databasewritepath + "ImageVideoDatabase.db");
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPrefs.save(CalculatorActivity.this, SharedPrefs.isDecodeSwitchActive, "false");
        Share.isSwitchNeedToBeOn = false;
        Share.ChangePassword = false;
        Share.decoyPasscode = false;
    }

    @Override
    protected void onDestroy() {

        Log.i("TESTTTT", "onPause: ");


        super.onDestroy();
        Share.isSwitchNeedToBeOn = false;
        Share.ChangePassword = false;
        Share.decoyPasscode = false;
    }

    private void CheckButtonClick() {
        Log.e(TAG, "CheckButtonClick: msPasswordType:: " + msPasswordType);
        if (msPasswordType.equals(getString(R.string.OldPassword))) {
            Log.e("TAG:: ", "onClick: " + "OldPassword exists");
            if (CheckFileExists()) {
                msGetPassword = tv_result.getText().toString();
                Log.e("TAg:: ", "onClick: " + " OldPassword FileExists");
                moDBimageVideoDatabase = new ImageVideoDatabase(CalculatorActivity.this);
                UserModel userModel = moDBimageVideoDatabase.getSingleUserData(1);
                String oldPass = userModel.getPassword();

                userModel = decoyDatabase.getSingleUserData(1);
                String decoyOldPass = userModel.getPassword();
                Log.e("TAG:: ", "onClick: old pass:: " + oldPass);
                Log.e("TAG:: ", "onClick: getPAssword:: " + msGetPassword);
                if (oldPass != null && oldPass.equals(msGetPassword)) {
                    SharedPrefs.save(CalculatorActivity.this, SharedPrefs.DecoyPassword, "false");
                    checkPassword(false);
                } else if (decoyOldPass != null && decoyOldPass.equals(msGetPassword)) {
                    SharedPrefs.save(CalculatorActivity.this, SharedPrefs.DecoyPassword, "true");
                    checkPassword(true);
                } else {
                    mCalculatorPresenter.onOperatorAdd(getString(R.string.percentage));
                    mCalculatorPresenter.onCalculateResult();
                    try {
                        if (msSwitchActive.equals("true")) {
                            Log.e(TAG, "CheckButtonClick: " + "Take picture true");
                            EnterePassword = msGetPassword;
                            takePicture();
                            new Handler().postDelayed(() -> {
//                                stopCamera();
                            },1500);

                        } else {
                            Log.e(TAG, "CheckButtonClick: " + "Take picture false");
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "CheckButtonClick: Exception:: " + e.getMessage());
                    }
                    // Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (msPasswordType.equals(getString(R.string.NewPassword))) {
            Log.e("TAG:: ", "onClick: " + " New Password btn Clicked");
            File path = new File(databasewritepath);
            if (!path.exists())
                path.mkdirs();
            File loFile = new File(databasewritepath + "ImageVideoDatabase.db");
            if (!loFile.exists()) {
                Log.e("TAG:: ", "initViewAction: " + "FilenotExist");
                try {
                    loFile.createNewFile();
                    Log.e("TAG:: ", "initViewAction: " + " Created new File");
                } catch (IOException e) {
                    Log.e("TAG:: ", "initViewAction: " + " exception:: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            msGetPassword = tv_result.getText().toString();
            Log.e("TAG:: ", "CheckButtonClick: " + msGetPassword.length());
            if (msGetPassword.length() == 4) {
                moDBimageVideoDatabase = new ImageVideoDatabase(CalculatorActivity.this);
                msGetPassword = tv_result.getText().toString();
                Log.e("New Password:: ", "onClick: " + msGetPassword);
                msPasswordType = getString(R.string.ConfirmPassword);
                moTvPasswordType.setText(getString(R.string.confPass));
                // moEtPassword.clearFocus();
                moTvFormula.setText("");
                tv_result.setText("");
                wholevalue = "";
                wholevalue2 = "";
                mCalculatorPresenter.onClearExpression();
                //  moTvDone.setText(getString(R.string.ConfirmPassword));
            } else {
                Toast.makeText(this, "Please Enter a 4 Digit Password", Toast.LENGTH_SHORT).show();
                Log.e("TAG:: ", "checkPassword: " + "Enter 4 digit Password");
            }
        } else {
            Log.e(TAG, "Decoy Password: 1");
            //msConfirmPassword = tv_result.getText().toString();
            Log.e("TAG:: ", "onClick: " + " Confirm Password:: " + msConfirmPassword);
            if (msGetPassword.equals(msConfirmPassword)) {
                Log.e(TAG, "Decoy Password: 2");
                UserModel userModel = new UserModel();
                userModel.setID(1);
                userModel.setPassword(msGetPassword);
                userModel.setConfirm_password(msConfirmPassword);
                userModel.setDatabasePath(databasewritepath);

                if (Share.decoyPasscode) {
                    if (Share.changeDecoy) {
                        decoyDatabase.updateSingleDataPassword(userModel);
                        Share.changeDecoy = false;
                    } else {
                        decoyDatabase.addUserData(userModel);
                    }
                    moTvFormula.setText("");
                    tv_result.setText("");
                    wholevalue = "";
                    wholevalue2 = "";
                    mCalculatorPresenter.onClearExpression();
                    Share.decoyPasscode = false;
                    Share.isSwitchNeedToBeOn = true;
                    SharedPrefs.save(CalculatorActivity.this, SharedPrefs.isDecodeSwitchActive, "true");

                    Log.e("TAG:: ", "Decoy Password: 3");
                    finish();
                    //Share.RestartApp(CalculatorActivity.this);

                } else if (Share.ChangePassword) {
                    Log.e(TAG, "ChangePassword: 1 Update");
                    moTvFormula.setText("");
                    tv_result.setText("");
                    wholevalue = "";
                    wholevalue2 = "";
                    mCalculatorPresenter.onClearExpression();

                    Log.e("TAG:: ", "onClick: " + " Correct Password");
                    userModel.setID(1);
                    moDBimageVideoDatabase.updateSingleDataPassword(userModel);
                    Share.ChangePassword = false;
                    Log.e("TAG:: ", "onClick: " + " Correct Password");
                    finish();
                } else {
                    Log.e(TAG, "ChangePassword: 1 Add");
                    moDBimageVideoDatabase.addUserData(userModel);
                    moTvFormula.setText("");
                    tv_result.setText("");
                    wholevalue = "";
                    wholevalue2 = "";
                    mCalculatorPresenter.onClearExpression();

                    //ToDo: changes are made here for security questions...
                    Log.e("TAG:: ", "onClick: " + " Correct Password");
                    Intent intent = new Intent(CalculatorActivity.this, SecurityActivity.class);
                    intent.putExtra("fromWhere", "init");
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(this, "Incorrect Password ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (Share.ChangePassword) {
                Log.e("TAG:: ", "ChangePassword: " + "else");
                moTvPasswordType.setVisibility(View.VISIBLE);
                moTvFormula.setVisibility(View.INVISIBLE);
                msPasswordType = getString(R.string.NewPassword);
                moTvPasswordType.setText(getString(R.string.newPass));
                btn_percent.setText("Done\n%");
            } else {
                if (Share.isFromSelection) {
                    isFromSelection = false;
                    msPasswordType = getString(R.string.OldPassword);
                    moTvPasswordType.setVisibility(View.INVISIBLE);
                    moTvFormula.setVisibility(View.VISIBLE);
                    btn_percent.setText("%");
                } else {
                    if (checkAndRequestPermissions()) {
                        if (!Share.decoyPasscode) {
                            if (CheckFileExists() && (moDBimageVideoDatabase.getUserTableCount() > 0)) {
                                msPasswordType = getString(R.string.OldPassword);
                                moTvPasswordType.setVisibility(View.INVISIBLE);
                                moTvFormula.setVisibility(View.VISIBLE);
                                btn_percent.setText("%");
                                //moTvPasswordType.setText(getString(R.string.oldPass));
                            } else {
                                Log.e("TAG:: ", "initViewAction: " + "else");
                                moTvPasswordType.setVisibility(View.VISIBLE);
                                moTvFormula.setVisibility(View.INVISIBLE);
                                msPasswordType = getString(R.string.NewPassword);
                                btn_percent.setText("Done\n%");
                                // moTvPasswordType.setText(getString(R.string.newPass));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onResume: " + e.toString());
        }
    }


    private void loadThemes() {

        int savedList = Preferences.getIntPref(CalculatorActivity.this, Preferences.KEY_SAVED_THEME_SELECTION);
        ArrayList<ThemeModel> list;
        list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {

            boolean v;
            switch (i) {

                case 0:
                    if (savedList == 0) {
                        v = true;
                    } else {
                        v = false;
                    }
                    ThemeModel modelONE = new ThemeModel(v, R.drawable.theme_one,
                            R.drawable.bg_pattern_theme_one, R.drawable.shape_pattern_theme_one,
                            R.color.theme_one_line);
                    list.add(modelONE);
                    break;

                case 1:
                    if (savedList == 1) {
                        v = true;
                    } else {
                        v = false;
                    }
                    ThemeModel modelTWO = new ThemeModel(v, R.drawable.theme_two,
                            R.drawable.bg_pattern_theme_two, R.drawable.shape_pattern_theme_two,
                            R.color.theme_two_line);
                    list.add(modelTWO);
                    break;

                case 2:
                    if (savedList == 2) {
                        v = true;
                    } else {
                        v = false;
                    }
                    ThemeModel modelTHREE = new ThemeModel(v, R.drawable.theme_three,
                            R.drawable.bg_pattern_theme_three, R.drawable.shape_pattern_theme_three,
                            R.color.theme_three_line);
                    list.add(modelTHREE);
                    break;

                case 3:
                    if (savedList == 3 || savedList == -1) {
                        v = true;
                    } else {
                        v = false;
                    }
                    ThemeModel modelFOUR = new ThemeModel(v, R.drawable.theme_four,
                            R.drawable.bg_pin, -1, R.color.theme_four_line);
                    list.add(modelFOUR);
                    break;

            }

        }

        String newList = new Gson().toJson(list);
        Preferences.setStringPref(CalculatorActivity.this, Preferences.KEY_SAVED_THEME_LIST, newList);


        int savedListPin = Preferences.getIntPref(CalculatorActivity.this, Preferences.KEY_SAVED_THEME_SELECTION_PIN);
        //PIN.....
        list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            boolean v;
            switch (i) {

                case 0:

                    if (savedListPin == 0) {
                        v = true;
                    } else {
                        v = false;
                    }

                    ThemeModel modelOne = new ThemeModel(v, R.drawable.theme_pin_one,
                            R.drawable.bg_pin_theme_one,
                            R.drawable.ic_dash_pin_theme_one,
                            R.drawable.ic_dot_pin_theme_one,
                            R.drawable.ic_one_pin_theme_one,
                            R.drawable.ic_two_pin_theme_one,
                            R.drawable.ic_three_pin_theme_one,
                            R.drawable.ic_four_pin_theme_one,
                            R.drawable.ic_five_pin_theme_one,
                            R.drawable.ic_six_pin_theme_one,
                            R.drawable.ic_seven_pin_theme_one,
                            R.drawable.ic_eight_pin_theme_one,
                            R.drawable.ic_nine_pin_theme_one,
                            R.drawable.ic_zero_pin_theme_one,
                            R.drawable.ic_del_pin_theme_one);

                    list.add(modelOne);

                    break;

                case 1:

                    if (savedListPin == 1) {
                        v = true;
                    } else {
                        v = false;
                    }

                    ThemeModel modelTwo = new ThemeModel(v, R.drawable.theme_pin_two,
                            R.drawable.bg_pattern_theme_three,
                            R.drawable.ic_dash_pin_theme_two,
                            R.drawable.ic_dot_pin_theme_two,
                            R.drawable.ic_one_pin_theme_two,
                            R.drawable.ic_two_pin_theme_two,
                            R.drawable.ic_three_pin_theme_two,
                            R.drawable.ic_four_pin_theme_two,
                            R.drawable.ic_five_pin_theme_two,
                            R.drawable.ic_six_pin_theme_two,
                            R.drawable.ic_seven_pin_theme_two,
                            R.drawable.ic_eight_pin_theme_two,
                            R.drawable.ic_nine_pin_theme_two,
                            R.drawable.ic_zero_pin_theme_two,
                            R.drawable.ic_del_pin_theme_two);

                    list.add(modelTwo);

                    break;

                case 2:

                    if (savedListPin == 2) {
                        v = true;
                    } else {
                        v = false;
                    }

                    ThemeModel modelThree = new ThemeModel(v, R.drawable.theme_pin_three,
                            R.drawable.bg_pattern_theme_two,
                            R.drawable.ic_dash_pin_theme_three,
                            R.drawable.ic_dot_pin_theme_three,
                            R.drawable.ic_one_pin_theme_three,
                            R.drawable.ic_two_pin_theme_three,
                            R.drawable.ic_three_pin_theme_three,
                            R.drawable.ic_four_pin_theme_three,
                            R.drawable.ic_five_pin_theme_three,
                            R.drawable.ic_six_pin_theme_three,
                            R.drawable.ic_seven_pin_theme_three,
                            R.drawable.ic_eight_pin_theme_three,
                            R.drawable.ic_nine_pin_theme_three,
                            R.drawable.ic_zero_pin_theme_three,
                            R.drawable.ic_del_pin_theme_three);

                    list.add(modelThree);

                    break;

                case 3:

                    if (savedListPin == 3 || savedListPin == -1) {
                        v = true;
                    } else {
                        v = false;
                    }

                    ThemeModel modelFour = new ThemeModel(v, R.drawable.theme_pin_four,
                            R.drawable.bg_pin,
                            R.drawable.ic_dash,
                            R.drawable.ic_circle_filled,
                            R.drawable.ic_one_pin_theme_four,
                            R.drawable.ic_two_pin_theme_four,
                            R.drawable.ic_three_pin_theme_four,
                            R.drawable.ic_four_pin_theme_four,
                            R.drawable.ic_five_pin_theme_four,
                            R.drawable.ic_six_pin_theme_four,
                            R.drawable.ic_seven_pin_theme_four,
                            R.drawable.ic_eight_pin_theme_four,
                            R.drawable.ic_nine_pin_theme_four,
                            R.drawable.ic_zero_pin_theme_four,
                            R.drawable.ic_del_pin_theme_four);

                    list.add(modelFour);
                    break;
            }
        }
        String newList1 = new Gson().toJson(list);
        Preferences.setStringPref(CalculatorActivity.this, Preferences.KEY_SAVED_THEME_LIST_PIN, newList1);
    }


    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {

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
    public void onUnavailable() {
        Log.e("GreedyGame", "OnUnAvailable");
    }

    @Override
    public void onAvailable(String s) {
        Log.e("GreedyGame", "OnAvailable:: " + s);
    }

    @Override
    public void onError(String s) {
        Log.e("GreedyGame", "OnError:: " + s);
    }


    @Override
    public void onPause() {
        super.onPause();

        Log.i("TESTTTT", "onPause: ");

        // System.exit(0);
        // finishAffinity();
    }
}
