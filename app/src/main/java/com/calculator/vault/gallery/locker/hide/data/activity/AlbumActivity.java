package com.calculator.vault.gallery.locker.hide.data.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.fragment.PhotoFragment;

public class AlbumActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AlbumActivity.class.getSimpleName();

    public static Activity activity;

    private TextView moTvTitle;
    private ImageView iv_back;

    public static AlbumActivity moAlbumActivity;

    public static Boolean moBoolIsStart = true;
    private String msPickFromGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        Log.e(TAG, "onCreate: AlbumActivity");
        if (Share.RestartApp(AlbumActivity.this)) {
            activity = AlbumActivity.this;
            setToolbar();
            initView();
            initViewAction();
        }
    }

    private void setToolbar() {
        moTvTitle = findViewById(R.id.tv_title);
        msPickFromGallery = SharedPrefs.getString(AlbumActivity.this, SharedPrefs.PickFromGallery);
        if (msPickFromGallery.equalsIgnoreCase("pickvideo")) {
            moTvTitle.setText(getString(R.string.ChooseVideo));
        } else{
            moTvTitle.setText(getString(R.string.ChoosePhoto));
        }
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        moAlbumActivity = this;
    }

    private void initViewAction() {
        Intent i = getIntent();
        if (i != null) {
            AlbumActivity.moBoolIsStart = i.getBooleanExtra("start", true);
        }
        updateFragment(PhotoFragment.newInstance());
    }

    private void updateFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.simpleFrameLayout, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }


    public static AlbumActivity getFaceActivity() {
        return moAlbumActivity;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: AlbumActivity");
    }
}
