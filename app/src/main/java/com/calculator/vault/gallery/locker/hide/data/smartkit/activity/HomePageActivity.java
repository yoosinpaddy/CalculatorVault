package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.CustomDialogClass;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.smartkit.fragment.CommonFragment;
import com.calculator.vault.gallery.locker.hide.data.smartkit.fragment.HomeFragment;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.AdModel;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.CategoryModel;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.SubCatModel;
import com.calculator.vault.gallery.locker.hide.data.smartkit.webservice.Webservice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    // widget
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView ibtn_back, ibtn_moreapps;

    // static variables
    public static ArrayList<String> titles;

    // variables
    private ViewPagerAdapter adapter;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_homepage);

        Window window = HomePageActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        findViews();
        setListners();
        initViewAction();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.dark_green));
        }
    }

    private void findViews() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        ibtn_back =  findViewById(R.id.ibtn_back);
        ibtn_moreapps = findViewById(R.id.ibtn_moreapps);
    }

    private void setListners() {
        ibtn_back.setOnClickListener(this);
        ibtn_moreapps.setOnClickListener(this);
    }

    private void initViewAction() {

        setDimen();
        titles = new ArrayList<>();

        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("Home").setTag(0));
        titles.add("Home");
        for (int i = 0; i < Share.al_app_center_data.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(Share.al_app_center_data.get(i).getName()).setTag(i + 1));
            titles.add(Share.al_app_center_data.get(i).getName());
        }

        //set gravity for tab bar
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSmoothScrollingEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setDimen() {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Share.screenWidth = size.x;
        Share.screenHeight = size.y;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        HomeFragment fragment = new HomeFragment();
        fragment.setTitle("Home");
        adapter.addFragment(fragment, "Home");

        for (int i = 0; i < Share.al_app_center_data.size(); i++) {
            CommonFragment commonfragment = new CommonFragment();
            commonfragment.setTitle(Share.al_app_center_data.get(i).getName());
            adapter.addFragment(commonfragment, Share.al_app_center_data.get(i).getName());
        }
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

        if (view == ibtn_back) {
            /*Intent intent = new Intent(HomePageActivity.this, SplashMenuActivity.class);
            startActivity(intent);*/
            finish();

        } else if (view == ibtn_moreapps) {
          //  AccountRedirectActivity.get_url(HomePageActivity.this);
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                String sAux = "Download this amazing "+getString(R.string.app_name).toLowerCase()+" app from play store\n\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id="+getPackageName()+"\n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Share.al_app_center_data.size() == 0 || Share.al_app_center_home_data.size() == 0
                || Share.al_ad_data.size() == 0) {
            Share.al_app_center_data.clear();
            Share.al_app_center_home_data.clear();
            Share.al_ad_data.clear();

            getData();
        }
    }

    @Override
    public void onBackPressed() {
        CustomDialogClass cdd = new CustomDialogClass(HomePageActivity.this);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }

    public void getData() {
        if (NetworkManager.isInternetConnected(HomePageActivity.this)) {
            new GetAdData().execute();
        } else {
            Intent intent = new Intent(HomePageActivity.this, SplashHomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class GetAdData extends AsyncTask<String, Void, Void> {
        String response;


        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(HomePageActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Please wait..");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String temp = "http://vasundharaapps.com/artwork_apps/api/AdvertiseNewApplications/17/" + getPackageName();
                temp = temp.replaceAll(" ", "%20");
                URL sourceUrl = new URL(temp);
                response = Webservice.GET(sourceUrl);
                SharedPrefs.save(HomePageActivity.this, SharedPrefs.SPLASH_AD_DATA, response.toString());
            } catch (Exception e) {
                Intent intent = new Intent(HomePageActivity.this, SplashHomeActivity.class);
                startActivity(intent);
                finish();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (!response.equals("")) {
                    Share.al_ad_data.clear();
                    Share.al_app_center_home_data.clear();
                    Share.al_app_center_data.clear();

                    try {
                        JSONObject data = new JSONObject(response);

                        if (data.getString("status").equals("1")) {
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
                                    Share.al_ad_data.add(adModel);
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
                                    subCatModel.setBanner(sub_obj_data.getString("banner"));
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
                                    subCatModel.setBanner(sub_obj_data.getString("banner"));
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

                            Share.is_button_click = true;
                        } else {
                            Share.is_button_click = false;

                        }
                    } catch (Exception e) {
                        pd.dismiss();
                        e.printStackTrace();
                    }
                    if (Share.full_ad.size() == 0) {
                        new DownLoadFullAdData().execute("");
                    }
                } else {
                    pd.dismiss();
                    Share.is_button_click = false;
                }

            } catch (Exception e) {
                pd.dismiss();
                e.printStackTrace();
            }
        }
    }

    private class DownLoadFullAdData extends AsyncTask<String, Void, Void> {
        String response;
        ArrayList<Bitmap> b = new ArrayList<>();
        ArrayList<Integer> position = new ArrayList<>();

        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("pre", "pre");

        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                for (int i = 0; i < Share.al_ad_data.size(); i++) {

                    if (Share.al_ad_data.get(i).getFull_thumb_image() != null && !Share.al_ad_data.get(i).getFull_thumb_image().equalsIgnoreCase("")) {
                        final int finalI1 = i;
                        Glide.with(HomePageActivity.this)
                                .asBitmap()
                                .load(Share.al_ad_data.get(i).getFull_thumb_image())
                                .listener(new RequestListener<Bitmap>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                        b.add(resource);
                                        position.add(finalI1);
                                        return false;
                                    }
                                });

                       /* Picasso.with(HomePageActivity.this).
                                load(Share.al_ad_data.get(i).getFull_thumb_image()).
                                into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        Log.e("bitmap", "bitmap" + bitmap);
                                        b.add(bitmap);
                                        position.add(finalI1);
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                }); */
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            Share.full_ad.clear();
            for (int p = 0; p < position.size(); p++) {
                AdModel model = Share.al_ad_data.get(position.get(p));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                b.get(p).compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] bytedata = baos.toByteArray();

                String encodedImage = Base64.encodeToString(bytedata, Base64.DEFAULT);
                model.setFull_img(encodedImage);
                Share.full_ad.add(model);
            }
            Log.e("asaaa", "sdsadas" + Share.full_ad.size());
            Gson gson = new Gson();
            String jsonad = gson.toJson(Share.full_ad).toString();
            SharedPrefs.save(getApplicationContext(), "full_ad_img", jsonad);
            initViewAction();

        }
    }

    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            Log.e("position", "position" + position);
            if (position == 0) {
                HomeFragment fragment = new HomeFragment();
                fragment.setTitle(HomePageActivity.titles.get(position));
                return fragment;
            } else {
                Bundle args = new Bundle();
                args.putString("id", HomePageActivity.titles.get(position));
                CommonFragment fragment = new CommonFragment();
                fragment.setTitle(HomePageActivity.titles.get(position));
                fragment.setArguments(args);
                return fragment;
            }
        }

        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}