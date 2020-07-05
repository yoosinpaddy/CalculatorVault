package com.calculator.vault.gallery.locker.hide.data.subscription;

import android.content.Context;
import android.content.SharedPreferences;

//SharedPreferences manager class
public class AdsPrefs {


    public static final String URL_INDEX = "URL_INDEX";
    public static final String IS_ADS_REMOVED = "is_ads_removed";
    public static final String DEVICE_ID = "device_id";
    public static final String SPLASH_AD_DATA = "Ad_data";
    public static final String FEB_COUNTER = "feb_counter";

    public static final String HINT_COUNT = "hint_counts";
    public static final String COINS_COUNT = "coins_count";
    public static final String CATEGORY_LIST = "category_list";
    public static final String IS_FIRST_TIME = "is_first_time";
    public static final String PRICE_YEAR = "price_year";
    public static final String PRICE_WEEK = "price_week";
    public static final String PRICE_MONTH_DISCOUNT = "price_month_discount";
    public static final String PRICE_MONTH = "price_month";
    public static final String IS_AUTO_RENEW_YEAR = "is_auto_renew_year";
    public static final String IS_AUTO_RENEW_MONTH = "is_auto_renew_month";
    public static final String IS_AUTO_RENEW_WEEK = "is_auto_renew_week";
    public static final String IS_AUTO_RENEW_MONTH_DISCOUNT = "is_auto_renew_month_discount";
    public static final String PURCHASED_PLAN_ID = "purchased_plan_id";
    public static final String PLAN_ID = "plan_id";
    public static final String PLANE_PRICE = "plane_price";
    public static final String CLICKED_ITEM_LIST = "clicked_item_list";
    public static final String EMAIL = "dummy_email";

    public static final String DEFAULT_PRICE_YEAR = "₹2100.00";
    public static final String DEFAULT_PRICE_MONTH = "₹233.35";
    public static final String DEFAULT_PRICE_WEEK = "₹99.00";


    // Subscription msgs
    public static final String TRAIL_MSG_DISCOUNT = "trial_msg_discount";
    public static final String TRAIL_MSG_MONTH = "trial_msg_month";


    // Subscription btn text
    public static final String TRAIL_BTN_DISCOUNT = "trial_btn_discount";
    public static final String TRAIL_BTN_MONTH = "trial_btn_month";


    public final String appLaunchCount = "applaunchcount";


    //SharedPreferences file name
    private static String SHARED_PREFS_FILE_NAME = "app_center";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static boolean contain(Context context, String key) {
        return getPrefs(context).contains(key);
    }

    public static void clearPrefs(Context context) {
        String device_id = getString(context, DEVICE_ID);
        getPrefs(context).edit().clear().commit();
        save(context, DEVICE_ID, device_id);
    }

    //Save Booleans
    public static void save(Context context, String key, boolean value) {
        getPrefs(context).edit().putBoolean(key, value).commit();
    }


    public static void savePref(Context context, String key, boolean value) {
        getPrefs(context).edit().putBoolean(key, value).commit();
    }

    //Get Booleans
    public static boolean getBoolean(Context context, String key) {
        return getPrefs(context).getBoolean(key, false);
    }

    //Get Booleans if not found return a predefined default value
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getPrefs(context).getBoolean(key, defaultValue);
    }

    //Strings
    public static void save(Context context, String key, String value) {
        getPrefs(context).edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key) {
        return getPrefs(context).getString(key, "");
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getPrefs(context).getString(key, defaultValue);
    }

    //Integers
    public static void save(Context context, String key, int value) {
        getPrefs(context).edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key) {
        return getPrefs(context).getInt(key, 0);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return getPrefs(context).getInt(key, defaultValue);
    }

    //Floats
    public static void save(Context context, String key, float value) {
        getPrefs(context).edit().putFloat(key, value).commit();
    }

    public static float getFloat(Context context, String key) {
        return getPrefs(context).getFloat(key, 0);
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        return getPrefs(context).getFloat(key, defaultValue);
    }

    //Longs
    public static void save(Context context, String key, long value) {
        getPrefs(context).edit().putLong(key, value).commit();
    }

    public static long getLong(Context context, String key) {
        return getPrefs(context).getLong(key, 0);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return getPrefs(context).getLong(key, defaultValue);
    }

    public static void removeKey(Context context, String key) {
        getPrefs(context).edit().remove(key).commit();
    }


}