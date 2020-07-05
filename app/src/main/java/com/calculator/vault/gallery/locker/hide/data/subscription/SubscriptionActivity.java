package com.calculator.vault.gallery.locker.hide.data.subscription;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.calculator.vault.gallery.locker.hide.data.FirebaseEventUtils;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.subscription.sliderviewlibrary.SliderView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SubscriptionActivity extends Activity implements BillingProcessor.IBillingHandler {

    private final String TAG = SubscriptionActivity.class.getSimpleName();
    private SliderView sliderView;
    private ImageView ivCancel;
    private ConstraintLayout constraintLayout;
    private TextView tvChoose;
    private TextView tvYour;
    private TextView tvPlan;
    private TextView tvFreeTrial;
    private ConstraintLayout clMonth;
    private TextView tvStandard;
    private TextView tvMonth;
    private ImageView ivMonth;
    private TextView tvMonthPrice;
    private TextView tvMonthDisc;
    private ConstraintLayout clYear;
    private TextView tvValuePlan;
    private TextView tvYear;
    private ImageView ivYearCrown;
    private TextView tvYearPrice;
    private TextView tvYearDesc;
    private TextView tvWeeklyDesc;
    private TextView tvRenewHint,tv_hint;
    private String productKeyMonth = "", productKeyMonthDiscount = "", productKeyYear = "";
    private String licenseKey = "";
    private BillingProcessor billingProcessor;
    private ProgressDialog upgradeDialog;
    private Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        getWindow().setFlags(1024, 1024);
        mContext = SubscriptionActivity.this;


        // TODO: Firebase event
        Log.i("Event_Log", TAG);
        FirebaseEventUtils.AddEvent(mContext, TAG);

        initView();
        initBillingProcessor();
        setPlanPrice();
        initActions();


        String plan_btn_text = AdsPrefs.getString(mContext, AdsPrefs.TRAIL_BTN_MONTH);
        tvFreeTrial.setText(plan_btn_text);
        tvFreeTrial.setSelected(true);

    }


    private void initView() {
        sliderView = findViewById(R.id.sliderView);
        ivCancel = findViewById(R.id.iv_cancel);
        constraintLayout = findViewById(R.id.constraintLayout);
        tvChoose = findViewById(R.id.tv_choose);
        tvYour = findViewById(R.id.tv_your);
        tvPlan = findViewById(R.id.tv_plan);
        tvFreeTrial = findViewById(R.id.tv_sevenday_free_trial);
        clMonth = findViewById(R.id.cl_monthly_plan);
        tvStandard = findViewById(R.id.tv_standard);
        tvMonth = findViewById(R.id.tv_month);
        ivMonth = findViewById(R.id.iv_month);
        tvMonthPrice = findViewById(R.id.tv_month_price);
        tvMonthDisc = findViewById(R.id.tv_month_disc);
        clYear = findViewById(R.id.cl_yearly_plan);
        tvValuePlan = findViewById(R.id.iv_value_plan);
        tvYear = findViewById(R.id.tv_year);
        ivYearCrown = findViewById(R.id.iv_year_crown);
        tvYearPrice = findViewById(R.id.tv_year_price);
        tvYearDesc = findViewById(R.id.tv_year_desc);
        tvWeeklyDesc = findViewById(R.id.tv_weekly_desc);
        tvRenewHint = findViewById(R.id.tv_renew_hint);
        tv_hint = findViewById(R.id.tv_hint);


        Typeface tfBold = Typeface.createFromAsset(getAssets(), "app_fonts/NeoTech_Bold.otf");
        Typeface tfRegular = Typeface.createFromAsset(getAssets(), "app_fonts/NeoTech.otf");

        tvFreeTrial.setTypeface(tfBold);
        tvChoose.setTypeface(tfBold);
        tvYour.setTypeface(tfBold);
        tvPlan.setTypeface(tfBold);
        tvStandard.setTypeface(tfBold);
        tvValuePlan.setTypeface(tfBold);
        tvMonthPrice.setTypeface(tfBold);
        tvYearPrice.setTypeface(tfBold);


        tvMonth.setTypeface(tfRegular);
        tvYear.setTypeface(tfRegular);
        tvMonthDisc.setTypeface(tfRegular);
        tvYearDesc.setTypeface(tfRegular);
        tvWeeklyDesc.setTypeface(tfBold);
        tvRenewHint.setTypeface(tfRegular);

        tvWeeklyDesc.setSelected(true);
        tvRenewHint.setSelected(true);

        int DELAY_MS = 2000, PERIOD_MS = 2000;
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.ic_sub_1);
        images.add(R.drawable.ic_sub_2);
        images.add(R.drawable.ic_sub_3);
        sliderView.setImages(images);
        TimerTask task = sliderView.getTimerTask();
        Timer timer = new Timer();
        timer.schedule(task, DELAY_MS, PERIOD_MS);
    }


    private void initActions() {


        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvFreeTrial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: Firebase event
                Log.i("Event_Log", TAG);
                FirebaseEventUtils.AddEvent(mContext, FirebaseEventUtils.clickSubMonthly);

                billingProcessor.consumePurchase(productKeyMonth);
                billingProcessor.subscribe(mContext, productKeyMonth, "");


            }
        });

        clMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: Firebase event
                Log.i("Event_Log", TAG);
                FirebaseEventUtils.AddEvent(mContext, FirebaseEventUtils.clickSubMonthly);

                billingProcessor.consumePurchase(productKeyMonth);
                billingProcessor.subscribe(mContext, productKeyMonth, "");
            }
        });

        clYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // TODO: Firebase event
                Log.i("Event_Click", FirebaseEventUtils.clickSubYearly);
                FirebaseEventUtils.AddEvent(mContext, FirebaseEventUtils.clickSubYearly);

                billingProcessor.consumePurchase(productKeyYear);
                billingProcessor.subscribe(mContext, productKeyYear, "");
            }
        });

    }


    private void setPlanPrice() {

        String plan_year = AdsPrefs.getString(mContext, AdsPrefs.PRICE_YEAR, AdsPrefs.DEFAULT_PRICE_YEAR);
        String plan_month = AdsPrefs.getString(mContext, AdsPrefs.PRICE_MONTH, AdsPrefs.DEFAULT_PRICE_MONTH);
        String plan_week = AdsPrefs.getString(mContext, AdsPrefs.PRICE_WEEK, AdsPrefs.DEFAULT_PRICE_WEEK);


        tvYearPrice.setText(plan_year);
        try {
            String symbol = plan_year.substring(0, 1);
            plan_year = plan_year.replace(",", "");
            plan_year = plan_year.substring(1);
            float amt = Float.parseFloat(plan_year);
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            plan_year = symbol + df.format(amt / 12);
            tvYearPrice.setText(plan_year);


            plan_year = AdsPrefs.getString(mContext, AdsPrefs.PRICE_YEAR, AdsPrefs.DEFAULT_PRICE_YEAR);
            String planYearDesc = "Bill Yearly Total " + plan_year + " Save 50%";
            tvYear.setText(planYearDesc);

        } catch (NumberFormatException ex) {
            tv_hint.setVisibility(View.GONE);
            ex.printStackTrace();
        } catch (Exception ex) {
            tv_hint.setVisibility(View.GONE);
            ex.printStackTrace();
        }


        tvMonthPrice.setText(plan_month);
        tvWeeklyDesc.setText("Monthly cost of plan is " + plan_month);

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

        Log.e(TAG, "onProductPurchased:  developerPayload :: -> " + details.purchaseInfo.purchaseData.developerPayload);
        Log.e(TAG, "onProductPurchased:  orderId :: -> " + details.purchaseInfo.purchaseData.orderId);
        Log.e(TAG, "onProductPurchased:  packageName :: -> " + details.purchaseInfo.purchaseData.packageName);
        Log.e(TAG, "onProductPurchased:  purchaseToken :: -> " + details.purchaseInfo.purchaseData.purchaseToken);
        Log.e(TAG, "onProductPurchased:  autoRenewing :: -> " + details.purchaseInfo.purchaseData.autoRenewing);
        Log.e(TAG, "onProductPurchased:  purchaseTime :: -> " + details.purchaseInfo.purchaseData.purchaseTime);
        Log.e(TAG, "onProductPurchased:  purchaseState :: -> " + details.purchaseInfo.purchaseData.purchaseState);


        // TODO: Firebase event
        String eventName = FirebaseEventUtils.subMonthlyDone;

        if (productId.equals(productKeyMonthDiscount)) {
            eventName = FirebaseEventUtils.subMonthlyDiscountDone;
        } else if (productId.equals(productKeyMonth)) {
            eventName = FirebaseEventUtils.subMonthlyDone;
        } else if (productId.equals(productKeyYear)) {
            eventName = FirebaseEventUtils.subYearlyDone;
        }


        if (productId.equals(productKeyMonthDiscount) || productId.equals(productKeyMonth) || productId.equals(productKeyYear)) {
            AdsPrefs.save(mContext, AdsPrefs.IS_ADS_REMOVED, true);
            SharedPrefs.save(mContext, SharedPrefs.IS_ADS_REMOVED, true);
        }


        Log.i("Event_Click", eventName);
        FirebaseEventUtils.AddEvent(mContext, eventName);
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


    /*@Override
    public void onBackPressed() {
        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
        if (taskList.get(0).numActivities == 1 && taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
            Log.i(TAG, "This is last activity in the stack");
            Intent intent = new Intent(mContext, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }*/


}
