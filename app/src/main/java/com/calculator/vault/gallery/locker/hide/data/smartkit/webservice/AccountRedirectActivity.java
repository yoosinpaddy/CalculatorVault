package com.calculator.vault.gallery.locker.hide.data.smartkit.webservice;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.SharedPrefs;


/**
 * Created by Vishal2.vasundhara on 27-Jul-17.
 */
public class AccountRedirectActivity {

    private static String[] url_array = new String[]{};

    public static void get_url(Context context) {
        int flag1 = SharedPrefs.getInt(context, SharedPrefs.COUNT);
        Log.e("TAG", "flag1 => " + flag1);
        if (flag1 == 0) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(Share.more_apps_data.get(flag1).app_link));
            context.startActivity(intent);
            flag1 = flag1 + 1;
            SharedPrefs.save(context, SharedPrefs.COUNT, flag1);
        } else {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(Share.more_apps_data.get(flag1).app_link));
            context.startActivity(intent);
            flag1 = flag1 + 1;
            if (flag1 == Share.more_apps_data.size()) {
                flag1 = 0;
            }
            SharedPrefs.save(context, SharedPrefs.COUNT, flag1);
        }
    }
}
