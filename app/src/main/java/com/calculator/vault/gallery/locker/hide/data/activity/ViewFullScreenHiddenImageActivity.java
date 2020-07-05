package com.calculator.vault.gallery.locker.hide.data.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.adapter.CustomHiddenImageAdapter;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.data.DecoyDatabase;
import com.calculator.vault.gallery.locker.hide.data.data.ImageVideoDatabase;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.model.itemModel;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ViewFullScreenHiddenImageActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_position, tv_total;
    private ArrayList<itemModel> modelArrayList;
    private ImageVideoDatabase imageVideoDatabase = new ImageVideoDatabase(this);
    CustomHiddenImageAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    String TAG = this.getClass().getSimpleName();
    int breakIntentpos = 0;
    int breakpagerpos = 0;
    LinearLayout ll_delete_pic, moLlSharePic;
    private ImageView iv_back;
    private DecoyDatabase decoyDatabase = new DecoyDatabase(this);
    private String isDecode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_full_screen_image);

        initView();
        isDecode= SharedPrefs.getString(ViewFullScreenHiddenImageActivity.this,SharedPrefs.DecoyPassword,"false");

        breakIntentpos = getIntent().getIntExtra("showIntent", 0);

        if(isDecode.equals("true")){
            modelArrayList = decoyDatabase.getAllDATA(Share.msPathToWriteDecoy);
        }else{
            modelArrayList = imageVideoDatabase.getAllDATA(Share.msPathToWrite);
        }


        if (modelArrayList.size() < 10) {
            tv_total.setText("0" + String.valueOf(modelArrayList.size()));
        } else {
            tv_total.setText(String.valueOf(modelArrayList.size()));
        }
        initViewListener();
        initViewAction();
    }

    private void initViewAction() {
        mCustomPagerAdapter = new CustomHiddenImageAdapter(this, modelArrayList);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
        Log.e(TAG, "initViewAction: setCurrentItem: " + breakIntentpos);
        mViewPager.setCurrentItem(breakIntentpos);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e(TAG, "onPageScrolled: " + position);
                breakIntentpos = position;
                if (position < 9) {
                    tv_position.setText("0" + String.valueOf(position + 1));
                } else {
                    tv_position.setText(String.valueOf(position + 1));
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "onPageSelected: " + position);
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
        moLlSharePic.setOnClickListener(this);
    }

    private void initView() {
        tv_total = findViewById(R.id.tv_total);
        tv_position = findViewById(R.id.tv_position);
        ll_delete_pic = findViewById(R.id.ll_delete_pic);
        iv_back = findViewById(R.id.iv_back);
        moLlSharePic = findViewById(R.id.ll_share_pic);
    }

    public void copyPhotoTo(String pathToWatch, String pathToWrite, String str) {
        File path = new File(pathToWrite);
        if (!path.exists())
            path.mkdirs();
        try {
            File loFile = new File(pathToWrite + str);
            if (!loFile.exists())
                loFile.createNewFile();

            FileUtils.copyFile(new File(pathToWatch), new File(pathToWrite + str));
            Log.e("TAG", "CopyFileDone: " + str);
        } catch (IOException e) {
            Log.e("TAG", "onEvent: Error");
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_delete_pic:
                alertdialog();
                break;
            case R.id.ll_share_pic:
                try {
                    File file = new File(modelArrayList.get(breakIntentpos).getNewFilepath() + modelArrayList.get(breakIntentpos).getNewFilename());
                    copyPhotoTo(file.getAbsolutePath(), "/storage/emulated/0/.androidData/.log/.dup/", modelArrayList.get(breakIntentpos).getOldFilename());
                    Uri loUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", new File("/storage/emulated/0/.androidData/.log/.dup/" + modelArrayList.get(breakIntentpos).getOldFilename()));
                    Intent Shareintent = new Intent()
                            .setAction(Intent.ACTION_SEND)
                            .setType("image/*")
                            .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            .putExtra(Intent.EXTRA_STREAM, loUri);
                    this.startActivity(Intent.createChooser(Shareintent, this.getString(R.string.share_intent_images)));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private void deleteImage(){
        if(isDecode.equals("true")){
            decoyDatabase.removeSingleBreakInReport(modelArrayList.get(breakIntentpos).getID());
        }else{
            imageVideoDatabase.removeSingleBreakInReport(modelArrayList.get(breakIntentpos).getID());
        }

        Log.e("TAG", "deletePhotosFromGallery: " + modelArrayList.get(breakIntentpos).getNewFilename());
        itemModel model ;

        if(isDecode.equals("true")){
            model= decoyDatabase.getSingleData(modelArrayList.get(breakIntentpos).getNewFilename());
        }else{
            model= imageVideoDatabase.getSingleData(modelArrayList.get(breakIntentpos).getNewFilename());
        }

        Log.e("TAG", "ll_delete_pic: getNewFilename " + model.getNewFilename());
        Log.e("TAG", "ll_delete_pic: getOldFilename " + model.getOldFilename());
        Log.e("TAG", "ll_delete_pic: getOldFilepath " + model.getOldFilepath());
        Log.e("TAG", "ll_delete_pic: getNewFilepath " + model.getNewFilepath());

        File from = new File(model.getNewFilepath() + model.getNewFilename());
        File to = new File(model.getNewFilepath() + model.getOldFilename());
        from.renameTo(to);

        File asd = new File(model.getOldFilepath() + model.getOldFilename());
        Log.e("TAG:: ", "onActivityResult: " + to.getName());
        String path = model.getOldFilepath();
        copyPhotoTo(to.getAbsolutePath(), path, to.getName());
        File file = new File(model.getOldFilepath() + model.getOldFilename());
        Uri contentUri = Uri.fromFile(file);
        Log.e("ATGA", "saveVideosFrGallery: rename file name::: " + file.getName());
        Log.e("ATGA", "saveVideosFromGallery: rename file path::: " + file.getAbsolutePath());

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);

        MediaScannerConnection.scanFile(ViewFullScreenHiddenImageActivity.this, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String s, Uri uri) {
                Log.e("TAG", "onScanCompleted: ");
            }
        });
        to.delete();
        if(isDecode.equals("true")){
            decoyDatabase.removeSingleData(model.getNewFilename());
        }else{
            imageVideoDatabase.removeSingleData(model.getNewFilename());
        }

        modelArrayList.remove(breakIntentpos);
        if (modelArrayList.size() < 10) {
            tv_total.setText("0" + String.valueOf(modelArrayList.size()));
        } else {
            tv_total.setText(String.valueOf(modelArrayList.size()));
        }
        mCustomPagerAdapter = new CustomHiddenImageAdapter(this, modelArrayList);
        mViewPager.setAdapter(mCustomPagerAdapter);
        Log.e(TAG, "initViewAction: setCurrentItem: " + breakIntentpos);
        if (modelArrayList != null && modelArrayList.size() > 0) {
            mViewPager.setCurrentItem(breakIntentpos + 1);
            setResult(7001);
        }
        else {
            setResult(7001);
            finish();
        }
    }

    private void alertdialog() {
        final Dialog dialog1 = new Dialog(ViewFullScreenHiddenImageActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.setContentView(R.layout.alert_decoy_passcode);
        TextView mess = (TextView) dialog1.findViewById(R.id.tv_message1);
        mess.setText(getString(R.string.delete_image_message));
        Button yesbtn = (Button) dialog1.findViewById(R.id.btn_dialogOK1);
        Button nobtn = (Button) dialog1.findViewById(R.id.btn_dialogNo1);

        yesbtn.setOnClickListener(v -> {
            try {
                deleteImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog1.dismiss();
        });
        nobtn.setOnClickListener(v -> dialog1.dismiss());

        if (dialog1 != null && !dialog1.isShowing()) {
            dialog1.show();
        }
    }
}
