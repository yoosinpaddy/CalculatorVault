package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.calculator.vault.gallery.locker.hide.data.smartkit.Database.DBHelperClass;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.Receiver.AlarmReceiver;
import com.calculator.vault.gallery.locker.hide.data.smartkit.adapter.SplashAdImageAdapter;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.AdModel;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.CategoryModel;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.NotiModel;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.SubCatModel;
import com.calculator.vault.gallery.locker.hide.data.smartkit.webservice.AccountRedirectActivity;
import com.calculator.vault.gallery.locker.hide.data.smartkit.webservice.Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

public class SplashHomeActivity extends BaseActivity implements View.OnClickListener {

    public static SplashHomeActivity activity;
    private LinearLayout ll_adview, ll_ad_data, ll_no_connection;
    private TextView tv_retry_start;
    private Button start;
    private File[] allFiles;
    private RecyclerView rcv_ad_images;
    private ArrayList<AdModel> list = new ArrayList<>();
    private ArrayList<AdModel> offline_list = new ArrayList<>();
    private ImageView iv_half_logo;
    private List<String> listPermissionsNeeded = new ArrayList<>();
    private int STORAGE_PERMISSION_CODE = 23;
    private Handler handler;
    private Runnable runnable;

    final DBHelperClass dba = new DBHelperClass(this);

    boolean isInForeGround;
    private FirebaseAnalytics mFirebaseAnalytics;

//    public static InputStream databaseInputStream1;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //Todo: For full screen(Hide status bar)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Todo: For startAppAd
        //StartAppSDK.init(this, "201630055", false); //for import check StartSDK folder
        setContentView(R.layout.s_activity_splash_home);

        activity = this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Log.e("SplashHomeActivity", "onCreate");
        findView();
        initView();
        initViewListner();
        getDisplaySize();

        if (NetworkManager.isInternetConnected(activity)) {
            ShowProgressDialog(activity, getString(R.string.please_wait));
            //// TODO: 3/07/2018 call ad api
            new GetAdData().execute();
        } else {
            Log.e("TAG", "else :");
            showNoInternet();
            if (!SharedPrefs.getString(activity, SharedPrefs.SPLASH_AD_DATA).equals("")) {
                Offline_Data();
            }
        }
    }

    private void findView() {
        rcv_ad_images = (RecyclerView) findViewById(R.id.rv_load_ads);
        ll_ad_data = (LinearLayout) findViewById(R.id.ll_ad_data);
        ll_adview = (LinearLayout) findViewById(R.id.ll_adview);
        ll_no_connection = (LinearLayout) findViewById(R.id.ll_no_connection);
        iv_half_logo = (ImageView) findViewById(R.id.iv_half_logo);
        tv_retry_start = (TextView) findViewById(R.id.tv_retry_start);
        start = (Button) findViewById(R.id.start);

    }

    private void initView() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 3);
        rcv_ad_images.setLayoutManager(gridLayoutManager);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ad_index;
                try {
                    // TODO: 03/07/2018 get index of full ad from sharepref
                    ad_index = SharedPrefs.getInt(getApplicationContext(), SharedPrefs.AD_INDEX);
                } catch (Exception e) {
                    ad_index = 0;
                }

                // TODO: 03/07/2018 check ad index grater and equal then from storage of ad arraylist
                if (ad_index >= Share.al_ad_full_image_from_storage.size()) {
                    // TODO: 03/07/2018 check ad index grater and equal then from api ad arraylist
                    if (ad_index >= Share.al_ad_full_image_from_api.size()) {
                        // TODO: 03/07/2018 if both are true then make ad_index=0 and save in sharepref
                        ad_index = 0;
                        SharedPrefs.save(getApplicationContext(), SharedPrefs.AD_INDEX, ad_index);
                    }
                }
                Log.e("index", "index" + ad_index);
                // TODO: 03/07/2018 Store ad index in global var so used in when show fulll ad
                Share.AD_index = ad_index;
                // TODO: 03/07/2018 and also increment ad index so ad show repeted
                ad_index++;
                // TODO: 03/07/2018 then store ad in sharepref
                SharedPrefs.save(getApplicationContext(), SharedPrefs.AD_INDEX, ad_index);

                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

              /*  // TODO: 03/07/2018 check full image from storage is not 0 then show full ad
                if (Share.al_ad_full_image_from_storage.size() > 0) {
                    Intent intent_ad = new Intent(activity, FullScreenAdActivity.class);
                    startActivity(intent_ad);
                }
                // TODO: 03/07/2018 and also check full image from api is not 0 then show full ad
                else if (Share.al_ad_full_image_from_api.size() > 0) {
                    Intent intent_ad = new Intent(activity, FullScreenAdActivity.class);
                    startActivity(intent_ad);
                }*/
            }
        });

    }

    private void initViewListner() {
        tv_retry_start.setOnClickListener(this);
    }

    // TODO: 03/07/2018 get screen height and width for further use
    private void getDisplaySize() {
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Share.screenWidth = size.x;
        Share.screenHeight = size.y;
    }

    private String saveImage(Bitmap image, int finalI, String title, ArrayList<String> offline_list) {
        String savedImagePath = null;
        String imageFileName;
        File folder;

        // TODO: 03/07/2018 for store full thumb image storage
        imageFileName = offline_list.get(finalI).replace(".", "_") + ".jpg";
        folder = new File(Environment.getExternalStorageDirectory() + "/" + ".AppsImages/SplashAdFull");
        if (!folder.exists()) {
            folder.mkdirs();//If there is no folder it will be created.
        }
        boolean success = true;
        if (success) {
            File imageFile = new File(folder, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // TODO: 03/07/2018 and get both images after save and sotre in arrrylist for further use
            getData("SplashAdFull");
        }
        return savedImagePath;
    }

    // TODO: 03/07/2018 Get both images from storage and store in seperate arraylist for further use.
    private void getData(String folder_name) {
        Share.al_ad_full_image_from_storage.clear();
        Share.al_ad_full_image_name.clear();
        File path = new File(Environment.getExternalStorageDirectory() + "/.AppsImages/" + folder_name);
        if (path.exists()) {

            allFiles = path.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"));
                }
            });

            Collections.sort(Arrays.asList(allFiles), new Comparator<File>() {
                public int compare(File o1, File o2) {
                    long lastModifiedO1 = o1.lastModified();
                    long lastModifiedO2 = o2.lastModified();

                    return (lastModifiedO1 < lastModifiedO2) ? -1 : ((lastModifiedO1 > lastModifiedO2) ? 1 : 0);
                }
            });

            if (allFiles.length > 0) {

                if (folder_name.equalsIgnoreCase("SplashAdFull")) {
                    // TODO: 03/07/2018 store full image of ad from sdcard and save in arraylist
                    for (int i = 0; i < allFiles.length; i++) {
                        if (!allFiles[i].getName().replace("_", ".").replace(".jpg", "").equals(getPackageName())) {
                            Share.al_ad_full_image_from_storage.add(allFiles[i]);
                        }
                    }
                }
            }

            for (int i = 0; i < Share.al_ad_full_image_from_storage.size(); i++) {
                Share.al_ad_full_image_name.add(Share.al_ad_full_image_from_storage.get(i).getName().replace(".jpg", ""));
            }

        }
    }

    // TODO: 03/07/2018 after getting run time permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new DownLoadFullAdDataWithPermisssion().execute("");
                // TODO: 03/07/2018 if permisssion granted then make a folder in storage

            } else {
                // TODO: 03/07/2018 same as if permission not get
                new DownLoadFullAdDataWithOutPermisssion().execute("");
                Toast.makeText(activity, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // TODO: 03/07/2018 if retry button click
            case R.id.tv_retry_start:
                if (NetworkManager.isInternetConnected(activity)) {
                    ShowProgressDialog(activity, getString(R.string.please_wait));
                    new GetAdData().execute();
                } else {
                    showNoInternet();
                }
                break;
        }
    }


    // TODO: 03/07/2018
    private class GetAdData extends AsyncTask<String, Void, Void> {
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String temp = "http://vasundharaapps.com/artwork_apps/api/AdvertiseNewApplications/17/" + getPackageName();
                temp = temp.replaceAll(" ", "%20");
                URL sourceUrl = new URL(temp);
                response = Webservice.GET(sourceUrl); //get class from Webservice folder
            } catch (Exception e) {
                e.printStackTrace();
                hideProgressDialog();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!response.equals("")) {
                try {
                    new LoadAdData(response).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    hideProgressDialog();
                }
            } else {
                hideProgressDialog();
                showNoInternet();
            }
        }
    }

    private class LoadAdData extends AsyncTask<String, Void, Void> {

        String response;

        public LoadAdData(String response) {
            this.response = response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("LoadAdData", "onPreExecute");
            list.clear();
            offline_list.clear();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                if (response.contains("status")) {
                    JSONObject data = new JSONObject(response);

                    if (data.getString("status").equals("1")) {

                        Share.is_button_click = true;
                        Share.al_ad_data.clear();

                        Share.al_app_center_home_data.clear();
                        Share.al_app_center_data.clear();
                        Share.al_ad_package_name.clear();
                        SharedPrefs.save(activity, SharedPrefs.SPLASH_AD_DATA, response.toString());

                        JSONArray arr_data = data.getJSONArray("data");
                        for (int i = 0; i < arr_data.length(); i++) {
                            JSONObject obj_data = arr_data.getJSONObject(i);

                            if (!getApplicationContext().getPackageName().equals(obj_data.getString("package_name"))) {
                                AdModel adModel = new AdModel();
                                adModel.setApp_link(obj_data.getString("app_link"));
                                adModel.setThumb_image(obj_data.getString("thumb_image"));
                                adModel.setFull_thumb_image(obj_data.getString("full_thumb_image"));
                                adModel.setPackage_name(obj_data.getString("package_name"));
                                adModel.setName(obj_data.getString("name"));
                                list.add(adModel);
                                offline_list.add(adModel);
                                Share.al_ad_data.add(adModel);

                                if (obj_data.getString("full_thumb_image") != null && !obj_data.getString("full_thumb_image").equalsIgnoreCase("")) {
                                    Share.al_ad_package_name.add(obj_data.getString("package_name"));
                                }

                            }
                        }

                        JSONArray arr_app_center = data.getJSONArray("app_center");
                        for (int i = 0; i < arr_app_center.length(); i++) {
                            JSONObject obj_data = arr_app_center.getJSONObject(i);
                            CategoryModel catModel = new CategoryModel();
                            catModel.setId(obj_data.getString("id"));
                            catModel.setName(obj_data.getString("name"));
                            catModel.setIs_active(obj_data.getString("is_active"));

                            ArrayList<SubCatModel> sub_list = new ArrayList<>();
                            JSONArray sub_arr_app_center = obj_data.getJSONArray("sub_category");
                            for (int j = 0; j < sub_arr_app_center.length(); j++) {
                                JSONObject sub_obj_data = sub_arr_app_center.getJSONObject(j);
                                SubCatModel subCatModel = new SubCatModel();
                                subCatModel.setId(sub_obj_data.getString("id"));
                                subCatModel.setApp_id(sub_obj_data.getString("app_id"));
                                subCatModel.setPosition(sub_obj_data.getString("position"));
                                subCatModel.setName(sub_obj_data.getString("name"));
                                subCatModel.setIcon(sub_obj_data.getString("icon"));
                                subCatModel.setStar(sub_obj_data.getString("star"));
                                subCatModel.setInstalled_range(sub_obj_data.getString("installed_range"));
                                subCatModel.setApp_link(sub_obj_data.getString("app_link"));
                                subCatModel.setBanner(sub_obj_data.getString("banner_image"));
                                subCatModel.setBanner_image(sub_obj_data.getString("banner_image"));
                                sub_list.add(subCatModel);
                            }
                            catModel.setSub_category(sub_list);
                            Share.al_app_center_data.add(catModel);
                        }

                        JSONArray arr_home = data.getJSONArray("home");
                        for (int i = 0; i < arr_home.length(); i++) {
                            JSONObject obj_data = arr_home.getJSONObject(i);
                            CategoryModel catModel = new CategoryModel();
                            catModel.setId(obj_data.getString("id"));
                            catModel.setName(obj_data.getString("name"));
                            catModel.setIs_active(obj_data.getString("is_active"));

                            ArrayList<SubCatModel> sub_list = new ArrayList<>();
                            JSONArray sub_arr_app_center = obj_data.getJSONArray("sub_category");
                            for (int j = 0; j < sub_arr_app_center.length(); j++) {
                                JSONObject sub_obj_data = sub_arr_app_center.getJSONObject(j);
                                SubCatModel subCatModel = new SubCatModel();
                                subCatModel.setId(sub_obj_data.getString("id"));
                                subCatModel.setApp_id(sub_obj_data.getString("app_id"));
                                subCatModel.setPosition(sub_obj_data.getString("position"));
                                subCatModel.setName(sub_obj_data.getString("name"));
                                subCatModel.setIcon(sub_obj_data.getString("icon"));
                                subCatModel.setStar(sub_obj_data.getString("star"));
                                subCatModel.setInstalled_range(sub_obj_data.getString("installed_range"));
                                subCatModel.setApp_link(sub_obj_data.getString("app_link"));
                                subCatModel.setBanner(sub_obj_data.getString("banner_image"));
                                sub_list.add(subCatModel);
                            }
                            catModel.setSub_category(sub_list);
                            Share.al_app_center_home_data.add(catModel);
                        }

                        JSONArray arr_more_app = data.getJSONArray("more_apps");
                        if (arr_more_app.length() > 0) {
                            JSONObject obj_data = arr_more_app.getJSONObject(0);

                            ArrayList<SubCatModel> sub_list = new ArrayList<>();
                            JSONArray sub_arr_app_center = obj_data.getJSONArray("sub_category");
                            for (int j = 0; j < sub_arr_app_center.length(); j++) {
                                JSONObject sub_obj_data = sub_arr_app_center.getJSONObject(j);
                                SubCatModel subCatModel = new SubCatModel();
                                subCatModel.setId(sub_obj_data.getString("id"));
                                subCatModel.setApp_id(sub_obj_data.getString("app_id"));
                                subCatModel.setPosition(sub_obj_data.getString("position"));
                                subCatModel.setName(sub_obj_data.getString("name"));
                                subCatModel.setIcon(sub_obj_data.getString("icon"));
                                subCatModel.setStar(sub_obj_data.getString("star"));
                                subCatModel.setInstalled_range(sub_obj_data.getString("installed_range"));
                                subCatModel.setApp_link(sub_obj_data.getString("app_link"));
                                subCatModel.setBanner(sub_obj_data.getString("banner"));
                                sub_list.add(subCatModel);
                            }
                            Share.more_apps_data.clear();
                            Share.more_apps_data.addAll(sub_list);
                        }
                        // TODO: 03/07/2018 parse native ad data and store in arraylist for further use
                        JSONObject arr_nativ_add = data.getJSONObject("native_add");

                        Share.ntv_img = arr_nativ_add.getString("image");
                        Share.ntv_inglink = arr_nativ_add.getString("playstore_link");

                    } else {
                        hideProgressDialog();
                        hideAllView();
                    }
                } else {
                    hideProgressDialog();
                    hideAllView();
                }

            } catch (Exception e) {
                hideProgressDialog();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (response.contains("status")) {
                    JSONObject data = new JSONObject(response);

                    if (data.getString("status").equals("1")) {

                        if (list.size() > 0) {

                            Share.is_button_click = true;
                            // TODO: 03/07/2018 set ad data in recyclerview
                            SplashAdImageAdapter adImageAdapter = new SplashAdImageAdapter(activity, list);
                            rcv_ad_images.setAdapter(adImageAdapter);

                            iv_half_logo.setVisibility(View.VISIBLE);
                            ll_adview.setVisibility(View.VISIBLE);
                            ll_ad_data.setVisibility(View.VISIBLE);
                            ll_no_connection.setVisibility(View.GONE);

                            // TODO: 03/07/2018  check storage runtime permisssion
                            if (checkAndRequestPermissions()) {
                                // TODO: 03/07/2018 if user granted the permssion then download full ad data
                                new DownLoadFullAdDataWithPermisssion().execute("");
                            } else {
                                ActivityCompat.requestPermissions(SplashHomeActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);
                            }
                        } else {
                            hideProgressDialog();
                            showNoInternet();
                        }
                    } else {
                        hideProgressDialog();
                        showNoInternet();
                    }
                }
            } catch (Exception e) {
                hideProgressDialog();
                e.printStackTrace();
            }

            // TODO: 3/10/2018 for local push notification
            SharedPrefs preff = new SharedPrefs();
            String open = "0";
            open = preff.getString(getApplicationContext(), "is_open", "0");
            if (open != null && open.equalsIgnoreCase("0") & Share.al_ad_data.size() > 0) {
                setAlarmforNotification();
            }
        }
    }

    private void setAlarmforNotification() {
        Log.e("SplashHomeActivity", "setAlarmforNotification");
        NotiModel model = new NotiModel(); //get class from LocalNotification folder
        model.setList(Share.al_ad_data);
        Gson gson = new Gson();
        String json = gson.toJson(model);
        Log.e("json", "json" + json);
        SharedPrefs preff = new SharedPrefs();
        preff.save(getApplicationContext(), "noti_list", json);
        preff.save(getApplicationContext(), "is_open", "1");
        preff.save(getApplicationContext(), "noti_count", 0);
        preff.save(getApplicationContext(), "m", 0);

        Calendar updateTime = Calendar.getInstance();
        updateTime.setTimeZone(TimeZone.getDefault());
        updateTime.set(Calendar.HOUR_OF_DAY, 12);
        updateTime.set(Calendar.MINUTE, 30);
        Intent downloader = new Intent(getApplicationContext(), AlarmReceiver.class); //get class from LocalNotification folder
        downloader.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, downloader, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AlarmManager.INTERVAL_DAY * 3, AlarmManager.INTERVAL_DAY * 3, pendingIntent);
    }

    private boolean checkAndRequestPermissions() {
        listPermissionsNeeded.clear();
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        Log.e("TAG", "listPermissionsNeeded===>" + listPermissionsNeeded);
        if (!listPermissionsNeeded.isEmpty()) {
            return false;
        }
        return true;
    }

    // TODO: 03/07/2018  this method is call when no internet connection
    private void Offline_Data() {
        String jsonFavorites = SharedPrefs.getString(activity, SharedPrefs.SPLASH_AD_DATA);
        offline_list.clear();
        Share.al_ad_data.clear();
        try {
            JSONObject data2 = new JSONObject(jsonFavorites.toString());
            JSONArray arr_data = data2.getJSONArray("data");

            for (int i = 0; i < arr_data.length(); i++) {
                JSONObject obj_data = arr_data.getJSONObject(i);
                if (!getApplicationContext().getPackageName().equals(obj_data.getString("package_name"))) {
                    AdModel adModel = new AdModel();
                    adModel.setApp_link(obj_data.getString("app_link"));
                    adModel.setThumb_image(obj_data.getString("thumb_image"));
                    adModel.setFull_thumb_image(obj_data.getString("full_thumb_image"));
                    adModel.setPackage_name(obj_data.getString("package_name"));
                    adModel.setName(obj_data.getString("name"));
                    offline_list.add(adModel);
                    Share.al_ad_data.add(adModel);
                }
            }

            // TODO: 03/07/2018  check storage runtime permisssion
            if (checkAndRequestPermissions()) {
                // TODO: 03/07/2018 if user granted the permssion then download full ad data
                new DownLoadFullAdDataWithPermisssion().execute("");
            } else {
                // TODO: 03/07/2018 show runtime permission dialog
                ActivityCompat.requestPermissions(SplashHomeActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class DownLoadFullAdDataWithPermisssion extends AsyncTask<String, Void, Void> {
        ArrayList<Bitmap> al_ad_bitmap = new ArrayList<>();
        ArrayList<String> al_ad_image_name = new ArrayList<>();

        protected void onPreExecute() {
            super.onPreExecute();
            ShowProgressDialog(activity, getString(R.string.please_wait));
            Share.al_ad_full_image_from_api.clear();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                // TODO: 03/07/2018 Get ad data from storage
                getData("SplashAdFull");
                al_ad_bitmap.clear();
                for (int i = 0; i < offline_list.size(); i++) {
                    // TODO: 03/07/2018 check data of full images in api response
                    if (offline_list.get(i).getFull_thumb_image() != null && !offline_list.get(i).getFull_thumb_image().equalsIgnoreCase("")) {
                        // TODO: 03/07/2018 also check full image is in storage if not then download new ad image bitmap
                        if (!Share.al_ad_full_image_name.contains(offline_list.get(i).getPackage_name().replace(".", "_"))) {
                            final int finalI = i;
                            final Bitmap theBitmap = Glide.
                                    with(activity).
                                    asBitmap().
                                    load(offline_list.get(i).getFull_thumb_image()).

                                    into(300, 300). // Width and height
                                    get();
                            al_ad_bitmap.add(theBitmap);
                            al_ad_image_name.add(offline_list.get(i).getPackage_name());
                        }
                    } else {
                        String imageFileName = offline_list.get(i).getPackage_name().replace(".", "_") + ".jpg";
                        File folder = new File(Environment.getExternalStorageDirectory() + "/" + ".AppsImages/SplashAdFull");
                        if (folder.exists()) {
                            File imageFile = new File(folder, imageFileName);
                            imageFile.delete();
                        }
                    }
                }

                // TODO: 03/07/2018 check bitmap array size is grater then 0 then save image bitmap in storage
                if (al_ad_bitmap.size() > 0) {
                    for (int p = 0; p < al_ad_bitmap.size(); p++) {
                        saveImage(al_ad_bitmap.get(p), p, "Full_thumb", al_ad_image_name);
                    }
                }
                getData("SplashAdFull");

            } catch (Exception e) {
                e.printStackTrace();
                hideProgressDialog();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideProgressDialog();
        }
    }

    // TODO: 03/07/2018 download full ad image if user have not granted runtime storage permission
    private class DownLoadFullAdDataWithOutPermisssion extends AsyncTask<String, Void, Void> {
        ArrayList<Bitmap> al_ad_bitmap = new ArrayList<>();
        ArrayList<Integer> al_ad_name = new ArrayList<>();

        protected void onPreExecute() {
            super.onPreExecute();
            ShowProgressDialog(activity, getString(R.string.please_wait));
            Share.al_ad_full_image_from_api.clear();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                // TODO: 03/07/2018 collect bitmap full image of ad
                for (int i = 0; i < Share.al_ad_data.size(); i++) {
                    if (Share.al_ad_data.get(i).getFull_thumb_image() != null && !Share.al_ad_data.get(i).getFull_thumb_image().equalsIgnoreCase("")) {
                        final int finalI = i;
                        final Bitmap theBitmap = Glide.
                                with(activity).
                                asBitmap().
                                load(Share.al_ad_data.get(i).getFull_thumb_image()).
                                into(300, 300). // Width and height
                                get();
                        al_ad_bitmap.add(theBitmap);
                        al_ad_name.add(i);
                    }
                }

                // TODO: 03/07/2018 set byte data of full image in arraylist for further used
                for (int p = 0; p < al_ad_name.size(); p++) {
                    AdModel model = Share.al_ad_data.get(al_ad_name.get(p));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    al_ad_bitmap.get(p).compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] bytedata = baos.toByteArray();

                    String encodedImage = Base64.encodeToString(bytedata, Base64.DEFAULT);
                    model.setFull_img(encodedImage);
                    Share.al_ad_full_image_from_api.add(model);
                }

                // TODO: 03/07/2018 store ad data in sharepref for offline load ad data
                Gson gson = new Gson();
                String jsonad = gson.toJson(Share.al_ad_full_image_from_api);
                SharedPrefs.save(getApplicationContext(), "full_ad_img", jsonad);

            } catch (Exception e) {
                e.printStackTrace();
                hideProgressDialog();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideProgressDialog();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final Dialog dialog = new Dialog(SplashHomeActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.s_choose_category_alert1); //get layout from ExitDialog folder

            SmileRating smileRating =  dialog.findViewById(R.id.smile_rating);
            Button btn_yes = dialog.findViewById(R.id.btn_yes);
            Button btn_no = dialog.findViewById(R.id.btn_no);

            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                    //  System.exit(0);
                    finish();
                }
            });

            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                       // dialog.dismiss();
                        AccountRedirectActivity.get_url(SplashHomeActivity.this); //get activity from MoreApps folder
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
                @Override
                public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                    switch (smiley) {
                        case SmileRating.BAD:
                            Toast.makeText(SplashHomeActivity.this, "Thanks for review", Toast.LENGTH_SHORT).show();
                            break;
                        case SmileRating.GOOD:
                            rate_app();
                            break;
                        case SmileRating.GREAT:
                            rate_app();
                            break;
                        case SmileRating.OKAY:
                            Toast.makeText(SplashHomeActivity.this, "Thanks for review", Toast.LENGTH_SHORT).show();
                            break;
                        case SmileRating.TERRIBLE:
                            Toast.makeText(SplashHomeActivity.this, "Thanks for review", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
            dialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void rate_app() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    private void showNoInternet() {
        Share.is_button_click = false;
        iv_half_logo.setVisibility(View.VISIBLE);
        ll_adview.setVisibility(View.VISIBLE);
        ll_ad_data.setVisibility(View.GONE);
        ll_no_connection.setVisibility(View.VISIBLE);
    }

    private void hideAllView() {
        Share.is_button_click = false;

        iv_half_logo.setVisibility(View.GONE);
        ll_adview.setVisibility(View.INVISIBLE);
        ll_ad_data.setVisibility(View.GONE);
        ll_no_connection.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if (handler != null) {
                handler.removeCallbacks(runnable);
                runnable = null;
                handler = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();

    }


}
