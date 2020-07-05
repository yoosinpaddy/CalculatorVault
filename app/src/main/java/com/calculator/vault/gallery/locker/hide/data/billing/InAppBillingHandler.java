package com.calculator.vault.gallery.locker.hide.data.billing;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Created by Naresh Hirpara on 18-02-2017.
 */
public class InAppBillingHandler {

    private static InAppBillingHandler mInAppBillingHandlerInstance;
    private Context mContext = null;

    public static InAppBillingHandler getInstance(Context context) {
        if (mInAppBillingHandlerInstance == null) {
            if (null != context) {
                mInAppBillingHandlerInstance = new InAppBillingHandler(context);
            } else {
                mInAppBillingHandlerInstance = new InAppBillingHandler();
            }
        }
        return mInAppBillingHandlerInstance;
    }

    private InAppBillingHandler() {
        mContext = null;
    }

    private InAppBillingHandler(Context context) {
        mContext = context;
    }

    public static Intent getBindServiceIntent() {
        Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        intent.setPackage("com.android.vending");
        return intent;
    }

    public static boolean isIabServiceAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentServices(getBindServiceIntent(), 0);
        return list != null && list.size() > 0;
    }

}
