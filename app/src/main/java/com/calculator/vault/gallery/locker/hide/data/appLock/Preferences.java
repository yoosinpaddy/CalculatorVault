package com.calculator.vault.gallery.locker.hide.data.appLock;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private static final String PREF_DRIVE_DEMO = "com.calculator.vault.gallery.locker.hide.data.appLock";
    public static final String KEY_LOCKED = "key_locked";
    public static final String KEY_PACKAGES = "key_packages";
    public static final String KEY_TOP = "key_top";
    public static final String KEY_SAVED_PATTERN = "key_saved_pattern";
    public static final String KEY_IS_PATTERN_LOCK_ENABLED = "key_is_pattern_lock_enabled";
    public static final String KEY_SERVICE_INITIAL_START = "key_service_initial_start";
    public static final String KEY_SERVICE_BLUETOOTH_START = "key_service_bluetooth_start";
    public static final String KEY_SERVICE_WIFI_START = "key_service_wifi_start";
    public static final String KEY_SAVED_THEME_LIST = "key_saved_theme_list";
    public static final String KEY_SAVED_THEME_SELECTION = "key_saved_theme_selection";
    public static final String KEY_SAVED_THEME_LIST_PIN = "key_saved_theme_list_pin";
    public static final String KEY_SAVED_THEME_SELECTION_PIN = "key_saved_theme_selection_pin";
    public static final String KEY_ENABLED_FROM_PATTERN = "key_enabled_from_pattern";
    public static final String KEY_DISABLED_FROM_PATTERN = "key_disabled_from_pattern";
    public static final String KEY_DATA_ENABLED_FROM_PATTERN = "key_data_enabled_from_pattern";
    public static final String KEY_DATA_DISABLED_FROM_PATTERN = "key_data_disabled_from_pattern";
    public static final String KEY_BLUETOOTH_ENABLED_FROM_PATTERN = "key_bluetooth_enabled_from_pattern";
    public static final String KEY_BLUETOOTH_DISABLED_FROM_PATTERN = "key_bluetooth_disabled_from_pattern";

    public static void setStringPref(Context context, String key, String value){
        SharedPreferences preferences = context.getSharedPreferences(PREF_DRIVE_DEMO,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,value);
        editor.apply();
        editor.commit();
    }

    public static String getStringPref(Context context, String key){
        SharedPreferences preferences = context.getSharedPreferences(PREF_DRIVE_DEMO,Context.MODE_PRIVATE);
        return preferences.getString(key,"");
    }

    public static void setIntPref(Context context, String key, int value){
        SharedPreferences preferences = context.getSharedPreferences(PREF_DRIVE_DEMO,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key,value);
        editor.apply();
        editor.commit();
    }

    public static int getIntPref(Context context, String key){
        SharedPreferences preferences = context.getSharedPreferences(PREF_DRIVE_DEMO,Context.MODE_PRIVATE);
        return preferences.getInt(key,-1);
    }

    public static void setBooleanPref(Context context, String key, boolean value){
        SharedPreferences preferences = context.getSharedPreferences(PREF_DRIVE_DEMO,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
        editor.commit();
    }

    public static boolean getBooleanPref(Context context, String key){
        SharedPreferences preferences = context.getSharedPreferences(PREF_DRIVE_DEMO,Context.MODE_PRIVATE);
        return preferences.getBoolean(key,false);
    }

    public static boolean getBooleanPref2(Context context, String key){
        SharedPreferences preferences = context.getSharedPreferences(PREF_DRIVE_DEMO,Context.MODE_PRIVATE);
        return preferences.getBoolean(key,true);
    }

}
