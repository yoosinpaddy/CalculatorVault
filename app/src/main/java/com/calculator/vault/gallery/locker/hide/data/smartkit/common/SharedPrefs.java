package com.calculator.vault.gallery.locker.hide.data.smartkit.common;

import android.content.Context;
import android.content.SharedPreferences;

//SharedPreferences manager class
public class SharedPrefs {

    public static final String URL_INDEX = "URL_INDEX";
    public static final String AD_INDEX = "ad_index";

    public static final String IS_ADS_REMOVED = "is_ads_removed";


    //TODO: Update SharedPref file name
    //SharedPreferences file name
    private static String SHARED_PREFS_FILE_NAME = "draw_tattoos_shared_prefs";

    //here you can centralize all your shared prefs keys
    public static final String ITEM_SIZE = "ITEM_SIZE";
    public static final String SPLASH_AD_DATA = "splash_ad_data";
    public static final String COUNT = "more_app_count";
    public static String Set_Title = "Set_Title";
    public static String title_category = "title_category";
    public static String FULL_AD_IMAGE = "FULL_AD_IMAGE";
    public static String Database_Creation = "DB_FLAG";
    public static String LIST_FROM_VALUE = "list_from_value";

    public static final String FromPosLang="FromPosLang";
    public static final String ToPosLang="ToPosLang";


//    public static String WATCHED_JSON_ARRAY="json_array";

    public static String MORE_APP_INDEX = "more_app_index";


    public static final String KEY = "key";
    public static final String KEY_INDEX = "key_index";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static boolean contain(Context context, String key) {
        return getPrefs(context).contains(key);
    }

    public static void clearPrefs(Context context) {
        getPrefs(context).edit().clear().commit();
    }

    //Save Booleans
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
    //Scientific Calculator
    public static void saveMemory(Context context, String key, String value) {
        getPrefs(context).edit().putString(key, value).commit();
    }


    public static String getStringMemory(Context context, String key) {
        return getPrefs(context).getString(key, "0");
    }

    public static String getStringMemory(Context context, String key, String defaultValue) {
        return getPrefs(context).getString(key, defaultValue);
    }

    public static void saveMemoryplus(Context context, String key, String value) {
        getPrefs(context).edit().putString(key, value).commit();
    }


    public static String getStringMemoryplus(Context context, String key) {
        return getPrefs(context).getString(key, "0");
    }

    public static String getStringMemoryplus(Context context, String key, String defaultValue) {
        return getPrefs(context).getString(key, defaultValue);
    }

    public static void saveMemoryminus(Context context, String key, String value) {
        getPrefs(context).edit().putString(key, value).commit();
    }


    public static String getStringMemoryminus(Context context, String key) {
        return getPrefs(context).getString(key, "0");
    }

    public static String getStringMemoryminus(Context context, String key, String defaultValue) {
        return getPrefs(context).getString(key, defaultValue);
    }

}