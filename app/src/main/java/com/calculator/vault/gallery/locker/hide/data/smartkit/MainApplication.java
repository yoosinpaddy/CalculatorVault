package com.calculator.vault.gallery.locker.hide.data.smartkit;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.KeyModel;
import com.calculator.vault.gallery.locker.hide.data.smartkit.webservice.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Aesha on 2/8/2017.
 */

public class MainApplication extends MultiDexApplication {
    private static MainApplication singleton;

    private ArrayList<KeyModel> al_key = new ArrayList<>();

    public static final String APPLYING_URI = "defaultUriPath";
    public static String sAppName;
    public static Context sContext;
    public static String defaultUriPath;
    public InterstitialAd mInterstitialAd;
    public AdRequest ins_adRequest;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        singleton = this;
        new Api().execute();

        sContext = this;
        sAppName = getResources().getString(R.string.app_name);
        LoadAds();

        getDPI();
    }

    public static void getDPI() {

        float density = Resources.getSystem().getDisplayMetrics().density;
        if (density == 0.75) {
            Log.i("LDPI", "getDPI: ");
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
        }
    }

    public static Context getContext() {
        return sContext;
    }

    public static MainApplication getInstance() {
        return singleton;
    }

    public static String getAppName() {
        return sAppName;
    }

    public void LoadAds() {
        try {
            Log.e("HairColorApplication", "LoadAds Called");
            mInterstitialAd = new InterstitialAd(this);

            mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_full_screen));

            ins_adRequest = new AdRequest.Builder()
                    //.addTestDevice(deviceId)
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("E19949FB5E7C5A28C30A875934AC8181") //SWIPE
                    .addTestDevice("41E9C9F5D1F985FB36C9760EFC8F3916") //Lenovo
                    .addTestDevice("64A3A22A05D9DCDBEC68395FF5048CD1")  //Coolpad
                    .addTestDevice("51A49E7B1B359D1999E5C85CE4F54978") //XOLO
                    .addTestDevice("F9EBC1840023CB004A83005514278635") //MI 6otu (No SIM)
                    .addTestDevice("5E26E7F73E67A7B59A48307632145815") //Micromax New
                    .addTestDevice("413FAED40213710754F4D30AC4F60355")  //INTEX
                    .addTestDevice("A7A19E06342F7D3868ABA7863D707BD7") //Samsung Tab
                    .addTestDevice("78E289C0CB209B06541CB844A1744650") //LAVA
                    .addTestDevice("29CB61C62E053C3C348D8549D6DAAD47")//X-ZIOX
                    .addTestDevice("3C8E4AA9C3802D60B83603426D16E430") //Celkon
                    .addTestDevice("BEAA671BEA6C971FE461AC6E423B2ADE") //Karbonn
                    .addTestDevice("74527FD0DD7B0489CFB68BAED192733D") //Nexus TAB
                    .addTestDevice("BB5542D48765B65F516CF440C3545896") //Samsung j2
                    .addTestDevice("E56855A0C493CEF11A7098FE6EA840CB") //Videocon
                    .addTestDevice("390FED1AE343E9FF9D644C4085C3868E") //jivi
                    .addTestDevice("ACFC7B7082B3F3FD4E0AC8E92EA10D53") //MI Tab
                    .addTestDevice("863D8BAE88E209F38FF3C94A0403C776") //Samsung old
                    .addTestDevice("CF03A7085F307594629D95E17F811FB2") //Samsung new
                    .addTestDevice("517048997101BE4535828AC2360582C2") //motorola
                    .addTestDevice("8BB4BCB27396AB8ED222B7F902E13420") //micromax old
                    .addTestDevice("BC9575D90E179E4F362C526D155175E5") //Gionee
                    .addTestDevice("F7803FE72A2748F6028D87DC36D7C574") //Mi Chhotu JIO
                    .addTestDevice("CEF2CF599FA65D8072F04888C122999E") //iVoomi
                    .addTestDevice("BB5542D48765B65F516CF440C3545896") //samusung j2
                    .addTestDevice("DD0A309E21D1F24C324C107BE78C1B88") //Ronak Oreo
                    .addTestDevice("2BCBC7FAC3D404C0E93FC9800BD8E2A2") //Nokia Oreo
                    .addTestDevice("8402151A60DC983CF55F7ECB87044267") // Nokia new black
                    .build();

            mInterstitialAd.loadAd(ins_adRequest);
        } catch (Exception e) {
        }
    }

    public boolean requestNewInterstitial() {

        boolean show = false;

        if (Share.isNeedToAdShow(getApplicationContext())) {
            try {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    show = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                show = false;
            }
        } else {
            show = false;
        }
        return show;
    }

    public boolean isLoaded() {

        boolean isLoaded = false;

        if (Share.isNeedToAdShow(getApplicationContext())) {
            try {
                if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
                    isLoaded = true;
                } else {
                    isLoaded = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                isLoaded = true;
            }
        } else {
            isLoaded = true;
        }

        return isLoaded;

    }

    public class Api extends AsyncTask<String, Void, Void> {
        String jsonStr;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                String url = "";
                jsonStr = Webservice.GET("http://139.59.86.230/voicecalculator/public/api/showapi");
                Log.e("TAG", "-->>" + url);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("TAG", "Response from url: " + jsonStr);

            if (jsonStr != null && !jsonStr.isEmpty()) {
                try {
                    al_key.clear();
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    if (jsonObject.getString("status").equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject key_object = jsonArray.getJSONObject(i);
                            KeyModel keyModel = new KeyModel();
                            keyModel.setApi_key(Share.decrypt(Share.pas.getBytes(), key_object.getString("apikey")));
                            //keyModel.setApi_key(key_object.getString("apikey"));
                            al_key.add(keyModel);
                        }

                        Gson gson = new Gson();
                        String jsonad = gson.toJson(al_key);
                        SharedPrefs.save(singleton, SharedPrefs.KEY, jsonad);

                        Log.e("TAG", "key:==>" + SharedPrefs.getString(singleton, SharedPrefs.KEY));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
