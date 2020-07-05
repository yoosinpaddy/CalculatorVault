package com.calculator.vault.gallery.locker.hide.data.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.adapter.BreakInImagesAdapter;
import com.calculator.vault.gallery.locker.hide.data.ads.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.data.DecoyDatabase;
import com.calculator.vault.gallery.locker.hide.data.data.ImageVideoDatabase;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.model.BreakInImageModel;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.lang.annotation.Native;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BreakInReportActivity extends AppCompatActivity implements View.OnClickListener, BreakInImagesAdapter.ItemOnClick {
    private SwitchCompat moSwBreakInReport;
    private String isswitchActive = "false";
    private String TAG = this.getClass().getSimpleName();
    private RecyclerView moRvBreakinpics;
    private LinearLayout ll_delete_all_images;
    private GridLayoutManager moGridLayoutManager;
    private BreakInImagesAdapter moImagesAdapter;
    private ArrayList<BreakInImageModel> moItemFileList = new ArrayList<>();
    private ImageView iv_back;
    private ImageVideoDatabase moImageVideoDatabase = new ImageVideoDatabase(this);
    private DecoyDatabase decoyDatabase = new DecoyDatabase(this);
    private String isDecode;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break_in_report);
        ll_delete_all_images = findViewById(R.id.ll_delete_all_images);
        initView();

        isDecode = SharedPrefs.getString(BreakInReportActivity.this, SharedPrefs.DecoyPassword, "false");

        isswitchActive = SharedPrefs.getString(BreakInReportActivity.this, SharedPrefs.isSwitchActive, "false");
        if (isswitchActive.equals("false")) {
            moSwBreakInReport.setChecked(false);
        } else {
            moSwBreakInReport.setChecked(true);
        }

        moSwBreakInReport.setOnCheckedChangeListener((compoundButton, b) -> {
            Log.e(TAG, "onCheckedChanged: " + String.valueOf(b));
            if (b) {
                Log.e(TAG, "onCheckedChanged: " + "Switch is Active");
            } else {
                Log.e(TAG, "onCheckedChanged: " + "Switch is Deactivated");
            }
            SharedPrefs.save(BreakInReportActivity.this, SharedPrefs.isSwitchActive, String.valueOf(b));
        });

        initViewListener();
        initViewAction();
        adView = (AdView) findViewById(R.id.adView);

    }

    private void initViewAction() {
        moItemFileList.clear();

        if (isDecode.equals("true")) {
            moItemFileList = decoyDatabase.getAllBreakInImages();
        } else {
            moItemFileList = moImageVideoDatabase.getAllBreakInImages();
        }

        if (moItemFileList != null && moItemFileList.size() == 0) {
            ll_delete_all_images.setVisibility(View.GONE);
            ll_delete_all_images.setEnabled(false);
            if (Share.isNeedToAdShow(BreakInReportActivity.this)) {
                NativeAdvanceHelper.loadNativeAd(this, findViewById(R.id.fl_adplaceholder), true);
            }
        } else {
            ll_delete_all_images.setVisibility(View.VISIBLE);
            ll_delete_all_images.setEnabled(true);
        }
        // Collections.sort(moItemFileList, Collections.reverseOrder());
        //Share.al_my_photos_photo.addAll(al_my_photos);
        // myPhotosAdapter = new MyPhotosAdapter(getActivity(), Share.al_my_photos_photo);
        moGridLayoutManager = new GridLayoutManager(BreakInReportActivity.this, 1);
        moImagesAdapter = new BreakInImagesAdapter(BreakInReportActivity.this, moItemFileList, this);
        moRvBreakinpics.setLayoutManager(moGridLayoutManager);
        moRvBreakinpics.setAdapter(moImagesAdapter);
    }

    private void initViewListener() {
        ll_delete_all_images.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void initView() {
        moSwBreakInReport = findViewById(R.id.sw_breakinreport);
        moRvBreakinpics = findViewById(R.id.rv_breakinpics);
        ll_delete_all_images = findViewById(R.id.ll_delete_all_images);
        iv_back = findViewById(R.id.iv_back);
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        /*    System.out.println(getDate(82233213123L, "dd/MM/yyyy hh:mm:ss.SSS"));
         */
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_delete_all_images:

                if (moItemFileList.size() != 0) {
                    File dir1 = new File(moItemFileList.get(0).getFilePath());
                    File dir = new File(dir1.getParent());

                    if (dir.isDirectory()) {
                        String[] children = dir.list();
                        for (int i = 0; i < children.length; i++) {
                            new File(dir, children[i]).delete();
                        }
                    }

                    if (isDecode.equals("true")) {
                        decoyDatabase.deleteAllBreakin();
                    } else {
                        moImageVideoDatabase.deleteAllBreakin();
                    }

                    moImagesAdapter.onUpdate(moItemFileList);
                    ll_delete_all_images.setVisibility(View.GONE);
                    if (Share.isNeedToAdShow(BreakInReportActivity.this)) {
                        NativeAdvanceHelper.loadNativeAd(this, findViewById(R.id.fl_adplaceholder), true);
                    }
                }
                break;

            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        moItemFileList.clear();
        if (isDecode.equals("true")) {
            moItemFileList = decoyDatabase.getAllBreakInImages();
        } else {
            moItemFileList = moImageVideoDatabase.getAllBreakInImages();
        }

        if (moItemFileList != null && moItemFileList.size() == 0) {
            ll_delete_all_images.setVisibility(View.GONE);
            if (Share.isNeedToAdShow(BreakInReportActivity.this)){
                NativeAdvanceHelper.loadNativeAd(this, findViewById(R.id.fl_adplaceholder), true);
            }
        } else {
            findViewById(R.id.fl_adplaceholder).setVisibility(View.GONE);
            ll_delete_all_images.setVisibility(View.VISIBLE);
            Share.loadAdsBanner(BreakInReportActivity.this, adView);
            // IronSource Banner Ads
            //ISAdsHelper.loadBannerAd(this,(FrameLayout) findViewById(R.id.flBanner));
        }
        moImagesAdapter.onUpdate(moItemFileList);
    }

    @Override
    public void onClick(int fiPosition, BreakInImageModel foBreakInImageModel, ImageView foIvcheckBox, boolean isselected) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NativeAdvanceHelper.onDestroy();
    }
}
