package com.calculator.vault.gallery.locker.hide.data.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

//SharedPreferences manager class
public class SharedPrefs {

    public static String TAG = SharedPrefs.class.getSimpleName();

    public static final String COUNT = "more_app_count";
    //SharedPreferences file name
    private static String SHARED_PREFS_FILE_NAME = "beggar_suit_shared_prefs";

    public static final String IS_MEDIA_PROJECTION = "is_media_projection";
    public static final String BROWSER_SELECTED = "BROWSER_SELECTED";

    // use for getting current device id
    public static final String DEVICE_ID = "device_id";
    // save facebook accesstoken
    public static final String AccessToken = "AccessToken";
    // save facebool profile id
    public static final String ProfileId = "ProfileId";
    public static final String PickFromGallery = "PickFromGallery";
    public static final String isSwitchActive = "isSwitchActive";
    public static final String isDecodeSwitchActive = "isDecodeSwitchActive";
    public static final String typotutorial = "typotutorial";
    public static final String isFingerLockOn = "isFingerLockOn";

    // use for face image selected or not
    public static final String SHOWFACE = "showface";
    // use for photo background selected or not
    public static final String BACKGROUND_IMAGE = "background_image";
    // use for select photo background from camera
    public static final String BACKGROUND_CAMERA = "camera_file";
    // use for editimg image earse brush size
    public static final String ERASER_SIZE = "eraser_size";
    // use for editing image repaire brush size
    public static final String REPAIR_SIZE = "repair_size";
    // use editing image for space between brash and touch circle
    public static final String ERASER_OFFSET = "eraser_offset";
    public static final String SPLASH_AD_DATA = "Ad_data";
    //use for offline data
    public static final String SUITJSONDATA = "suitjsondata";
    public static final String ITEM_SIZE = "item_size";
    /*
     *    @since  show 1st time Home scree dialog instruction
     */
    public static final String SHOW_S1 = "show_s1";
    /*
     *    @since  show 1st time EditImage dialog instruction
     */
    public static final String SHOW_S2 = "show_s2";
    /*
     *    @since  show after EditImage show Home screen dialog instruction
     */
    public static final String SHOW_S3 = "show_s3";

    public static final String AD_INDEX = "ad_index";
    public static final String FULL_AD_IMAGE = "full_ad_image";
    public static final String URL_INDEX = "URL_INDEX";

    public static String wigetinentshare = "widgetintent";
    public static String wigetinentresult = "widgetresult";
    public static String GallerySelected = "gallerySelected";
    public static String DecoyPassword = "decoypassword";


    public static String screen_hight = "screen_hight";
    public static String screen_width = "screen_width";

    public static final String IS_ADS_REMOVED = "is_ads_removed";

    public static final String BOOKMARK_LIST = "bookmark_list";


    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_MULTI_PROCESS);
    }

    public static boolean contain(Context context, String key) {
        try {
            return getPrefs(context).contains(key);
        } catch (Exception e) {
            Log.e(TAG, "contain: " + e.toString());

        }
        return false;
    }

    public static void clearPrefs(Activity context) {
        String device_id = getString(context, DEVICE_ID);
        getPrefs(context).edit().clear().commit();
        save(context, DEVICE_ID, device_id);
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

    public static void save(Context context, String key, Boolean value) {
        getPrefs(context).edit().putBoolean(key, value).commit();
    }

    public static String getString(Activity context, String key) {
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