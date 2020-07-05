package com.calculator.vault.gallery.locker.hide.data.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.calculator.vault.gallery.locker.hide.data.R;

public class PermissionActivity extends Activity {

    private Context mContext;

    private RelativeLayout relativeMain;
    private TextView tvHint;
    private ImageView avFlash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        getWindow().setFlags(1024, 1024);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        mContext = PermissionActivity.this;
        initViews();
        initData();
        initAction();

        Glide.with(this)
                .load(R.drawable.ic_switch)
                .into((ImageView) findViewById(R.id.av_flash));

    }

    private void initViews() {
        relativeMain = findViewById(R.id.relative_main);
        tvHint = findViewById(R.id.tv_hint);
        avFlash = findViewById(R.id.av_flash);
    }

    private void initData() {
        switch (getIntent().getStringExtra("for")) {
            case "Accessibility":
                tvHint.setText(R.string.Accessibility);
                break;
            case "Usage_Access":
                tvHint.setText(R.string.Usage_Access);
                break;
            default:
                tvHint.setText(R.string.notification_access);
                break;
        }
    }

    private void initAction() {

        relativeMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
