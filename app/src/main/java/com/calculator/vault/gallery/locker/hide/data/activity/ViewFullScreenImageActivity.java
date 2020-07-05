package com.calculator.vault.gallery.locker.hide.data.activity;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.data.DecoyDatabase;
import com.calculator.vault.gallery.locker.hide.data.data.ImageVideoDatabase;
import com.calculator.vault.gallery.locker.hide.data.model.BreakInImageModel;
import com.calculator.vault.gallery.locker.hide.data.adapter.CustomPagerAdapter;

import java.io.File;
import java.util.ArrayList;

public class ViewFullScreenImageActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_position, tv_total;
    private ArrayList<BreakInImageModel> breakInImageModelArrayList;
    private ImageVideoDatabase imageVideoDatabase = new ImageVideoDatabase(this);
    private LinearLayout llSharePic;
    CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    String TAG = this.getClass().getSimpleName();
    int breakIntentpos = 0;
    int breakpagerpos = 0;
    LinearLayout ll_delete_pic;
    private ImageView iv_back;
    private String isDecode;
    private DecoyDatabase decoyDatabase = new DecoyDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_screen_image);

        initView();

        isDecode = SharedPrefs.getString(ViewFullScreenImageActivity.this,SharedPrefs.DecoyPassword,"false");
        breakIntentpos = getIntent().getIntExtra("breakIntent", 0);
        if (getIntent().getStringExtra("isFrom")!=null){
            llSharePic.setVisibility(View.GONE);
        }


        breakInImageModelArrayList = imageVideoDatabase.getAllBreakInImages();
        if (breakInImageModelArrayList.size() < 10) {
            tv_total.setText("0" + String.valueOf(breakInImageModelArrayList.size()));
        } else {
            tv_total.setText(String.valueOf(breakInImageModelArrayList.size()));
        }
        initViewListener();
        initViewAction();
    }

    private void initViewAction() {
        mCustomPagerAdapter = new CustomPagerAdapter(this, breakInImageModelArrayList);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
        Log.e(TAG, "initViewAction: setCurrentItem: " + breakIntentpos);
        mViewPager.setCurrentItem(breakIntentpos);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e(TAG, "onPageScrolled: " + position);
                breakIntentpos = position;
                if (position < 10) {
                    tv_position.setText("0" + String.valueOf(position + 1));
                } else {
                    tv_position.setText(String.valueOf(position + 1));
                }

            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "onPageSelected: " + position
                );
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e(TAG, "onPageScrollStateChanged: " + state);
            }
        });
    }


    private void initViewListener() {
        ll_delete_pic.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void initView() {
        tv_total = findViewById(R.id.tv_total);
        tv_position = findViewById(R.id.tv_position);
        ll_delete_pic = findViewById(R.id.ll_delete_pic);
        iv_back = findViewById(R.id.iv_back);
        llSharePic = findViewById(R.id.ll_share_pic);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_delete_pic:

                if(isDecode.equals("true")){
                    decoyDatabase.removeSingleBreakInReport(breakInImageModelArrayList.get(breakIntentpos).getID());
                }else{
                    imageVideoDatabase.removeSingleBreakInReport(breakInImageModelArrayList.get(breakIntentpos).getID());
                }

                // mCustomPagerAdapter.removeView(breakIntentpos);
                File file = new File(breakInImageModelArrayList.get(breakIntentpos).getFilePath());
                if (file.exists()) {
                    Log.e(TAG, "onClick: delete file exists ");
                    file.delete();
                    Log.e(TAG, "onClick: file Deleted:: ");
                } else {
                    Log.e(TAG, "onClick: delete file doesnt exist..");
                }
                breakInImageModelArrayList.remove(breakIntentpos);
                if (breakInImageModelArrayList.size() < 10) {
                    tv_total.setText("0" + String.valueOf(breakInImageModelArrayList.size()));
                } else {
                    tv_total.setText(String.valueOf(breakInImageModelArrayList.size()));
                }
                mCustomPagerAdapter = new CustomPagerAdapter(this, breakInImageModelArrayList);
                mViewPager.setAdapter(mCustomPagerAdapter);
                Log.e(TAG, "initViewAction: setCurrentItem: " + breakIntentpos);
                if (breakInImageModelArrayList != null && breakInImageModelArrayList.size() > 0)
                    mViewPager.setCurrentItem(breakIntentpos + 1);
                else {
                    finish();
                }

                break;

            case R.id.iv_back:
                onBackPressed();
                break;

        }
    }
}
