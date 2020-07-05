package com.calculator.vault.gallery.locker.hide.data.commonCode.utils;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.StatFs;
import android.os.SystemClock;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.commonCode.models.AdModel;
import com.calculator.vault.gallery.locker.hide.data.commonCode.models.CategoryModel;
import com.calculator.vault.gallery.locker.hide.data.commonCode.models.SubCatModel;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Harshad Kathiriya on 11/12/2016.
 */

public class GlobalData {

    public static ArrayList<AdModel> al_ad_data = new ArrayList<>();
    public static ArrayList<AdModel> full_ad = new ArrayList<>();
    public static ArrayList<SubCatModel> fragment_data = new ArrayList<>();
    public static ArrayList<CategoryModel> al_app_center_data = new ArrayList<>();
    public static ArrayList<CategoryModel> al_app_center_home_data = new ArrayList<>();
    public static ArrayList<SubCatModel> more_apps_data = new ArrayList<>();
    public static int screenWidth;
    public static int screenHeight;
    public static boolean is_start = false;
    public static boolean is_more_apps = false;
    public static Boolean APD_FLAG = false;
    public static int AD_index;
    public static String selected_tab = "HOME";
    public static int position = 0;
    public static String current_pos;
    public static String ntv_img;
    public static String ntv_inglink;
    public static boolean is_button_click = false;

    public class KEYNAME {
        public static final String ALBUM_NAME = "album_name";
    }

    public static void callMoreApps(Activity activity) {
        if (SharedPrefs.getInt(activity, SharedPrefs.URL_INDEX) < Urls.EXIT_URLs.length) {
            Urls.EXIT_URL = Urls.EXIT_URLs[SharedPrefs.getInt(activity, SharedPrefs.URL_INDEX)];
            Intent ratingIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Urls.EXIT_URL));
            activity.startActivity(ratingIntent);
        }
        if (SharedPrefs.getInt(activity, SharedPrefs.URL_INDEX) < Urls.EXIT_URLs.length)
            SharedPrefs.save(activity, SharedPrefs.URL_INDEX, SharedPrefs.getInt(activity, SharedPrefs.URL_INDEX) + 1);
        else
            SharedPrefs.save(activity, SharedPrefs.URL_INDEX, 0);
    }

    public abstract class OnSingleClickListener implements View.OnClickListener {

        private static final long MIN_CLICK_INTERVAL = 1000;
        private long mLastClickTime;

        public abstract void onSingleClick(View v);

        @Override
        public final void onClick(View v) {
            long currentClickTime = SystemClock.uptimeMillis();
            long elapsedTime = currentClickTime - mLastClickTime;
            mLastClickTime = currentClickTime;
            if (elapsedTime <= MIN_CLICK_INTERVAL)
                return;
            onSingleClick(v);
        }

    }

    public static long kilobytesAvailable(File f) {
        try {
            StatFs stat = new StatFs(f.getPath());
            long bytesAvailable = (long)stat.getBlockSizeLong() * (long)stat.getAvailableBlocksLong();
            return bytesAvailable / (1024);
        }catch (Exception ex){
            return 0;
        }
    }

}