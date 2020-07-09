package com.calculator.vault.gallery.locker.hide.data.smartkit.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.AdModel;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.CategoryModel;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.CurrencyDetailModel;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.SubCatModel;

import java.io.File;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by abc on 8/11/2017.
 */

public class Share {
    public static String imgs = "";
    public static Integer position;

    public static Boolean flag;

    public static ArrayList<File> allImages;

    public static TextView bt_date, bt_month, bt_year;
    public static String d, m, y, sd, sm, sy;
    public static int clickfdate = 0;
    public static int clicksdate = 0;
    public static int AD_index;
    public static boolean isAdShow = false;
    public static boolean BMI_History = false;
    public static String nfname, nsname;

    public static int isclick;
    public static int bisclick;
    public static TextView bt_veer, bt_bdate1, bt_zara, bt_bdate2;
    public static EditText ed_name;
    public static String sday, smon, syear, fday, fmon, fyear;
    public static String fname, fdate, sname, sdate;
    private static String key;
    public static String pas = "passwordpassword";

    public static int FromPosLang = 17;
    public static int ToPosLang = 26;
    public static boolean overCompass;
    //Calculator
    public static ArrayList<CurrencyDetailModel> currencyDetailModels = new ArrayList<>();

    public static int screenWidth = 0;
    public static int screenHeight = 0;

    public static String BASE_URL_APPS = "http://vasundharaapps.com/artwork_apps/api";

    // Drawer
    public static Fragment selectedFragment;
    public static int selected_menu_position;

    // ------- Advertise --------
    public static ArrayList<AdModel> full_ad = new ArrayList<>();

    public static ArrayList<AdModel> al_ad_data = new ArrayList<>();
    public static ArrayList<CategoryModel> al_app_center_data = new ArrayList<>();
    public static ArrayList<CategoryModel> al_app_center_home_data = new ArrayList<>();
    public static ArrayList<SubCatModel> more_apps_data = new ArrayList<>();

    public static boolean is_start = false;
    public static Boolean APD_FLAG = false;

    public static String selected_tab = "HOME";
    public static boolean is_more_apps = false;
    public static boolean is_button_click = false;

    public static String ntv_img;
    public static String ntv_inglink;

    //-------------

    public static String insta_id, insta_access_token;

    public class KEYNAME {
        public static final String ALBUM_ID = "album_id";
        public static final String ALBUM_NAME = "album_name";
        public static final String SELECTED_IMAGE = "selected_image";
        public static final String SELECTED_PHONE_IMAGE = "selected_phone_image";
    }

    //SplashContent
    public static ArrayList<File> al_ad_full_image_from_storage = new ArrayList<>();
    public static ArrayList<AdModel> al_ad_full_image_from_api = new ArrayList<>();
    public static ArrayList<String> al_ad_full_image_name = new ArrayList<>();
    public static ArrayList<String> al_ad_package_name = new ArrayList<>();

    public static void getDisplaySize(Activity activity) {
        Display display = activity.getWindow().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }


    public static Boolean RestartApp(Activity activity) {

        if (!Share.checkAndRequestPermissionss(activity, 1)) {
            Intent i = activity.getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(i);
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkAndRequestPermissionss(Activity act, int code) {

        if (ContextCompat.checkSelfPermission(act, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(act, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(act, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public static String decrypt(byte[] key, String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] cipherText = android.util.Base64.decode(encryptedText.getBytes("UTF8"), android.util.Base64.DEFAULT);
            Share.key = new String(cipher.doFinal(cipherText), "UTF-8");

            //Log.e("TAG", "decryptedString:==>" + Share.key);
            return Share.key;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //TODO In App Purchase
    public static boolean isNeedToAdShow(Context context) {
        boolean isNeedToShow = true;

        if (!SharedPrefs.contain(context, SharedPrefs.IS_ADS_REMOVED)) {
            isNeedToShow = true;
        } else {
            if (!SharedPrefs.getBoolean(context, SharedPrefs.IS_ADS_REMOVED))
                isNeedToShow = true;

            else
                isNeedToShow = false;
        }
        return isNeedToShow;
    }

    //TODO In App Purchase
    public static void showAlert(final Activity activity, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    // TODO: 23/11/18 Inapp purchase for baneer add

    public static void loadAdsBanner(Activity activity, AdView adView) {

        boolean isNeedToShow = false;

        if (!SharedPrefs.contain(activity, SharedPrefs.IS_ADS_REMOVED)) {
            isNeedToShow = true;
        } else {
            if (!SharedPrefs.getBoolean(activity, SharedPrefs.IS_ADS_REMOVED))
                isNeedToShow = true;
            else
                isNeedToShow = false;
        }

        if (isNeedToShow) {

            try {
                // adView = activity.findViewById(R.id.adView);

                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .addTestDevice("E19949FB5E7C5A28C30A875934AC8181") //SWIPE
                        .addTestDevice("41E9C9F5D1F985FB36C9760EFC8F3916") //Lenovo
                        .addTestDevice("65814FD488106C8EE380A962812E0AE4")  //Coolpad
                        .addTestDevice("9553140774085061E51D99BE4FBB3C5E") //XOLO
                        .addTestDevice("F9EBC1840023CB004A83005514278635") //MI 6otu (No SIM)
                        .addTestDevice("4C9C29EFCCC3AFE714623A702F482AEE") //Micromax New
                        .addTestDevice("553B57A7B0422031839D1F2CC0607EB8")  //INTEX
                        .addTestDevice("A7A19E06342F7D3868ABA7863D707BD7") //Samsung Tab
                        .addTestDevice("78E289C0CB209B06541CB844A1744650") //LAVA
                        .addTestDevice("C458AB4E076325EA5FE91458A1A1FDC3")//X-ZIOX
                        .addTestDevice("567DB1C5EC4A5D581176C2652228829D") //Celkon
                        .addTestDevice("BEAA671BEA6C971FE461AC6E423B2ADE") //Karbonn
                        .addTestDevice("74527FD0DD7B0489CFB68BAED192733D") //Nexus TAB
                        .addTestDevice("63868CBC57BD84059B25ED8EF9970C9F") //Samsung j2
                        .addTestDevice("E56855A0C493CEF11A7098FE6EA840CB") //Videocon
                        .addTestDevice("86FCAEF9B8F88A7136E69ED879B12CE8") //jivi
                        .addTestDevice("ACFC7B7082B3F3FD4E0AC8E92EA10D53") //MI Tab
                        .addTestDevice("863D8BAE88E209F38FF3C94A0403C776") //Samsung old
                        .addTestDevice("C458AB4E076325EA5FE91458A1A1FDC3")//Samsung new
                        .addTestDevice("517048997101BE4535828AC2360582C2") //motorola
                        .addTestDevice("8BB4BCB27396AB8ED222B7F902E13420") //micromax old
                        .addTestDevice("BFAE6D8DB020BF475077F41CED4D4B5B") //Gionee
                        .addTestDevice("EB3DAD0B99C5B3658E0C2ACB31F8BE5B") //Mi Chhotu JIO
                        .addTestDevice("CEF2CF599FA65D8072F04888C122999E") //iVoomi
                        .addTestDevice("BB5542D48765B65F516CF440C3545896") //samusung j2
                        .addTestDevice("DD0A309E21D1F24C324C107BE78C1B88") //Ronak Oreo
                        .addTestDevice("B05DBFFC98F6E3E7A8E75E2FE96C2D65") //Nokia Oreo
                        .addTestDevice("E19949FB5E7C5A28C30A875934AC8181") //HiTech
                        .addTestDevice("1969289F3928DDBAA65020B884860E7A") //lava
                        .addTestDevice("EB3DAD0B99C5B3658E0C2ACB31F8BE5B") //Mi Chhotu JIO new
                        .addTestDevice("1CF5E374F11F517A8A5C3F26BFD9A14A")
                        .addTestDevice("355890BDA9D8DF2D87AD2B53BFCA2B2B")
                        .addTestDevice("C048D4FE66E2D9E7B4F84A3FA9C4CF51")
                        .addTestDevice("A8640CC0F3136BBBADA3A846485CD7C0")
                        .addTestDevice("2A6E3914633DA48BB7E9B7E5BE42E0A3")
                        .addTestDevice("DD838FA2B53F6627A623F956FC91650F")
                        .addTestDevice("EB1A57EEA195EC174059452619812426")//coolpad
                        .addTestDevice("810307A6F9374FF2E5474135A931F8E6")//LENOVO
                        .addTestDevice("63868CBC57BD84059B25ED8EF9970C9F")//samsung j2
                        .addTestDevice("2C8C750E8DA3A361411636901E87430E")//nokia
                        .addTestDevice("F599D1A9A67703BC9BB3DEEC5F694D05")//nokia 1
                        .addTestDevice("217721D34C4B2D4BD20BE6077C153A5C")//Realme
                        .addTestDevice("55105A85E87A86A66780A77237BD0D1C")//nokia
                        .addTestDevice("35BFFAB134DE8961215FD8F37C935429")//mi red 5
                        .addTestDevice("D57CF216E4C2571A23FF17C844AF84E2")//mi red 5
                        .addTestDevice("28527928B6F382CAD3670528DEEBBCD8")//COMIO
                        .addTestDevice("8699F1F7370FE8E03FBB19307602D58B")
                        .addTestDevice("8402151A60DC983CF55F7ECB87044267") // nokia new
                        .build();
                adView.loadAd(adRequest);

                final AdView finalAdView = adView;
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        finalAdView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdLeftApplication() {
                        super.onAdLeftApplication();
                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        finalAdView.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public static void getDPI() {

        float density = Resources.getSystem().getDisplayMetrics().density;
        if (density == 0.75) {
            Log.e("LDPI", "getDPI: ");
            //Log.e("LDPI ", "0.75");
        } else if (density == 1.0) {
            Log.e("MDPI ", "1.0");
        } else if (density == 1.5) {
            Log.e("HDPI ", "1.5");
        } else if (density == 2.0) {
            Log.e("xhdpi ", "2.0");
        } else if (density == 3.0) {
            Log.e("xxhdpi", "3.0");
        } else if (density == 4.0) {
            Log.e("xxxhdpi", "4.0");
        }else{
            Log.e("density", "getDPI: density"+density );
        }
    }
}
