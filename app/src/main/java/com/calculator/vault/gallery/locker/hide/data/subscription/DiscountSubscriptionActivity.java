package com.calculator.vault.gallery.locker.hide.data.subscription;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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

public class DiscountSubscriptionActivity extends Activity implements BillingProcessor.IBillingHandler {


    private final String TAG = DiscountSubscriptionActivity.class.getSimpleName();
    private Activity mContext;


    private ImageView ivAvatar;
    private ImageView ivCancel;
    private ConstraintLayout constraintLayout;
    private RelativeLayout relContinue;
    private TextView tvContinue;
    private TextView tvDiscount;
    private TextView tvDesc;
    private TextView tvPlanHint;


    private String productKeyMonth = "", productKeyMonthDiscount = "", productKeyYear = "";
    private String licenseKey = "";
    private BillingProcessor billingProcessor;
    private ProgressDialog upgradeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = DiscountSubscriptionActivity.this;


        // TODO: Firebase event
        Log.i("Event_Tag", TAG);
        FirebaseEventUtils.AddEvent(mContext, TAG);

        setContentView(R.layout.activity_subscription_discount);
        getWindow().setFlags(1024, 1024);
        intiView();
        initActions();
        initBillingProcessor();


        String plan_msg = AdsPrefs.getString(mContext, AdsPrefs.TRAIL_MSG_DISCOUNT);
        tvPlanHint.setText(plan_msg);
        tvPlanHint.setSelected(true);


        String plan_btn_text = AdsPrefs.getString(mContext, AdsPrefs.TRAIL_BTN_DISCOUNT);
        tvContinue.setText(plan_btn_text);
        tvContinue.setSelected(true);

    }

    private void initActions() {
        ivCancel.setOnClickListener(v -> {
            onCancel();
        });
        relContinue.setOnClickListener(v -> {


            // TODO: Firebase event
            Log.i("Event_Click", FirebaseEventUtils.clickSubMonthlyDiscount);
            FirebaseEventUtils.AddEvent(mContext, FirebaseEventUtils.clickSubMonthlyDiscount);

            billingProcessor.consumePurchase(productKeyMonthDiscount);
            billingProcessor.subscribe(mContext, productKeyMonthDiscount, "");
        });
    }

    private void onCancel() {
        startActivity(new Intent(mContext, SelectionActivity.class));
        finish();
    }

    private void intiView() {
        ivAvatar = findViewById(R.id.iv_avatar);
        ivCancel = findViewById(R.id.iv_cancel);
        constraintLayout = findViewById(R.id.constraintLayout);
        relContinue = findViewById(R.id.rel_continue);
        tvContinue = findViewById(R.id.tv_continue);
        tvDiscount = findViewById(R.id.tv_discount);
        tvDesc = findViewById(R.id.tv_desc);
        tvPlanHint = findViewById(R.id.tv_plan_hint);

        Typeface tfBold = Typeface.createFromAsset(getAssets(), "app_fonts/NeoTech_Bold.otf");
        Typeface tfRegular = Typeface.createFromAsset(getAssets(), "app_fonts/NeoTech.otf");

        tvDesc.setTypeface(tfRegular);
        tvContinue.setTypeface(tfBold);
        tvDiscount.setTypeface(tfBold);
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
         *   when you submit your first beta app you need to set productKeyWeekDiscount as test ID and other
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
        String eventName = FirebaseEventUtils.subMonthlyDiscountDone;
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
//        SharedPrefs.save(mContext, Share.IS_ADS_REMOVED, false);
    }

    @Override
    public void onBillingInitialized() {
        Log.e(TAG, "onBillingInitialized: :::::");
    }

    @Override
    public void onBackPressed() {
        onCancel();
    }
}
