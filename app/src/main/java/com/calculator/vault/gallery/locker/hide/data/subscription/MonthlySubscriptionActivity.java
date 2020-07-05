package com.calculator.vault.gallery.locker.hide.data.subscription;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.calculator.vault.gallery.locker.hide.data.FirebaseEventUtils;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.activity.SelectionActivity;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;

public class MonthlySubscriptionActivity extends Activity implements BillingProcessor.IBillingHandler {


    private final String TAG = MonthlySubscriptionActivity.class.getSimpleName();
    private Activity mContext;

    private ImageView ivAvatar;
    private ImageView ivCancel;
    private ConstraintLayout constraintLayout;
    private TextView tvFull;
    private TextView tvAccess;
    private TextView tvFree;
    private ImageView ivEmoji;
    private TextView tvEmoji;
    private ImageView ivGif;
    private TextView tvGif;
    private ImageView ivAds;
    private TextView tvAds;
    private RelativeLayout relContinue;
    private TextView tvContinue;
    private TextView tvPlanHint;

    private String productKeyMonth = "", productKeyMonthDiscount = "", productKeyYear = "";
    private String licenseKey = "";
    private BillingProcessor billingProcessor;
    private ProgressDialog upgradeDialog;


    private Handler handler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            billingProcessor.consumePurchase(productKeyMonth);
            billingProcessor.subscribe(mContext, productKeyMonth, "");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_monthly);
        getWindow().setFlags(1024, 1024);

        mContext = MonthlySubscriptionActivity.this;

        // TODO: Firebase event
        Log.i("Event_Log", TAG);
        FirebaseEventUtils.AddEvent(mContext, TAG);

        initView();
        initActions();
        initBillingProcessor();
        AdsPrefs.save(mContext, AdsPrefs.FEB_COUNTER, (AdsPrefs.getInt(mContext, AdsPrefs.FEB_COUNTER) + 1));
        handler = new Handler();
        handler.postDelayed(runnable, 2000);


        String plan_msg = AdsPrefs.getString(mContext, AdsPrefs.TRAIL_MSG_MONTH);
        tvPlanHint.setText(plan_msg);
        tvPlanHint.setSelected(true);


        String plan_btn_text = AdsPrefs.getString(mContext, AdsPrefs.TRAIL_BTN_MONTH);
        tvContinue.setText(plan_btn_text);
        tvContinue.setSelected(true);


    }

    private void initActions() {
        ivCancel.setOnClickListener(v -> {
            Log.i(TAG, "COUNTER: " + AdsPrefs.getInt(mContext, AdsPrefs.FEB_COUNTER));
            onCancel();

        });
        relContinue.setOnClickListener(v -> {


            // TODO: Firebase event
            Log.i("Event_Log", FirebaseEventUtils.clickSubMonthly);
            FirebaseEventUtils.AddEvent(mContext, FirebaseEventUtils.clickSubMonthly);

            removeHandler();
            billingProcessor.consumePurchase(productKeyMonth);
            billingProcessor.subscribe(mContext, productKeyMonth, "");

        });


    }

    private void onCancel() {

        int counter = AdsPrefs.getInt(mContext, AdsPrefs.FEB_COUNTER);
        if (AdsUtil.isFibonacci(counter) && counter != 2) {
            startActivity(new Intent(this, DiscountSubscriptionActivity.class));
        } else {
            startActivity(new Intent(this, SelectionActivity.class));
        }
        finish();
    }

    private void initView() {
        ivAvatar = findViewById(R.id.iv_avatar);
        ivCancel = findViewById(R.id.iv_cancel);
        constraintLayout = findViewById(R.id.constraintLayout);
        tvFull = findViewById(R.id.tv_full);
        tvAccess = findViewById(R.id.tv_access);
        tvFree = findViewById(R.id.tv_free);
        ivEmoji = findViewById(R.id.iv_emoji);
        tvEmoji = findViewById(R.id.tv_emoji);
        ivGif = findViewById(R.id.iv_gif);
        tvGif = findViewById(R.id.tv_gif);
        ivAds = findViewById(R.id.iv_ads);
        tvAds = findViewById(R.id.tv_ads);
        relContinue = findViewById(R.id.rel_continue);
        tvContinue = findViewById(R.id.tv_continue);
        tvPlanHint = findViewById(R.id.tv_plan_hint);

        Typeface tfBold = Typeface.createFromAsset(getAssets(), "app_fonts/NeoTech_Bold.otf");
        Typeface tfRegular = Typeface.createFromAsset(getAssets(), "app_fonts/NeoTech.otf");

        tvFull.setTypeface(tfBold);
        tvAccess.setTypeface(tfBold);
        tvContinue.setTypeface(tfBold);

        tvFree.setTypeface(tfRegular);
        tvEmoji.setTypeface(tfRegular);
        tvGif.setTypeface(tfRegular);
        tvAds.setTypeface(tfRegular);


        // init Data

    }


    private void initBillingProcessor() {

        /*
         *  You need 4 product key for this and you have to create this 4 key and give in office
         * with
         *  your email address, and they will join you in beta tester after uploading app in play
         *  store
         *   1 -> for week testing,  test Key
         *   2 -> week live key
         *   3 - > month live key
         *   4 - > year live key
         *
         *   when you submit your first beta app you need to set productKeyWeek as test ID and other
         *   2 are live and also live licence key
         *
         * */
        productKeyMonthDiscount = getString(R.string.ads_product_key_monthlydiscount);
        productKeyMonth = getString(R.string.ads_product_key_month);
        productKeyYear = getString(R.string.ads_product_key_year);

        /*
         * Live licence Key
         * */
        licenseKey = getString(R.string.licenseKey);

        billingProcessor = new BillingProcessor(mContext, licenseKey, this);
        billingProcessor.initialize();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 32459) {
            if (billingProcessor != null) {
                if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
                    super.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    @Override
    public void onProductPurchased(@NonNull String productId, TransactionDetails details) {

        if (upgradeDialog != null && upgradeDialog.isShowing()) {
            upgradeDialog.dismiss();
        }


        // TODO: Firebase event
        String eventName = FirebaseEventUtils.subMonthlyDone;
        Log.i("Event_Click", eventName);
        FirebaseEventUtils.AddEvent(mContext, eventName);


        Log.e(TAG, "onProductPurchased:  developerPayload :: -> " + details.purchaseInfo.purchaseData.developerPayload);
        Log.e(TAG, "onProductPurchased:  orderId :: -> " + details.purchaseInfo.purchaseData.orderId);
        Log.e(TAG, "onProductPurchased:  packageName :: -> " + details.purchaseInfo.purchaseData.packageName);
        Log.e(TAG, "onProductPurchased:  purchaseToken :: -> " + details.purchaseInfo.purchaseData.purchaseToken);
        Log.e(TAG, "onProductPurchased:  autoRenewing :: -> " + details.purchaseInfo.purchaseData.autoRenewing);
        Log.e(TAG, "onProductPurchased:  purchaseTime :: -> " + details.purchaseInfo.purchaseData.purchaseTime);
        Log.e(TAG, "onProductPurchased:  purchaseState :: -> " + details.purchaseInfo.purchaseData.purchaseState);

        if (productId.equals(productKeyMonthDiscount) || productId.equals(productKeyMonth) || productId.equals(productKeyYear)) {
            AdsPrefs.save(mContext, AdsPrefs.IS_ADS_REMOVED, true);
            SharedPrefs.save(mContext, SharedPrefs.IS_ADS_REMOVED, true);
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {
        Log.e(TAG, "onPurchaseHistoryRestored: :: ");
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        try {
            Log.e(TAG, "onBillingError: errorCode : " + errorCode);
            Log.e(TAG, "onBillingError: getCause : " + error.getCause().getMessage());
            Log.e(TAG, "onBillingError: getMessage : " + error.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "onBillingError: Exception Cause");
        }
    }

    @Override
    public void onBillingInitialized() {
        Log.e(TAG, "onBillingInitialized: :::::");
    }

    @Override
    public void onBackPressed() {
        onCancel();
    }

    @Override
    protected void onPause() {
        removeHandler();
        super.onPause();
    }

    public void removeHandler() {
        if (handler != null) {
            try {
                handler.removeCallbacksAndMessages(null);
                handler.removeCallbacks(runnable);
            } catch (Exception ignored) {

            }

        }
    }
}
