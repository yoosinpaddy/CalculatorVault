package com.calculator.vault.gallery.locker.hide.data.subscription;

import android.content.Context;

public class AdsUtil {


    // for in-app purchase
    public static boolean isNeedToAdShow(Context context) {
        boolean isNeedToShow = true;

        if (!AdsPrefs.contain(context, AdsPrefs.IS_ADS_REMOVED)) {
            isNeedToShow = true;
        } else {
            if (!AdsPrefs.getBoolean(context, AdsPrefs.IS_ADS_REMOVED))
                isNeedToShow = true;
            else
                isNeedToShow = false;
        }
        return isNeedToShow;
    }

    public static boolean isFibonacci(int num) {
        int a = 0;
        int b = 1;
        int f = 1;
        while (b < num) {
            f = a + b;
            a = b;
            b = f;
        }
        return num == f;
    }
}
