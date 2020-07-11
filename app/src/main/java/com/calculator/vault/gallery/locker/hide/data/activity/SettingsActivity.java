package com.calculator.vault.gallery.locker.hide.data.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.calculator.vault.gallery.locker.hide.data.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.mbroset).setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, BrowserSelectionActivity.class));
        });

        findViewById(R.id.m_iv_back).setOnClickListener(v -> {
            finish();
        });

        findViewById(R.id.mlosett).setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, ChangePasscodeActivity.class));
        });

        findViewById(R.id.mbreare).setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, BreakInReportActivity.class));
        });

        findViewById(R.id.mdecpass).setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, DecoyPasscodeActivity.class));
        });

        findViewById(R.id.mbarest).setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, BackupActivity.class));
        });

        findViewById(R.id.mrepass).setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, HowToRecoverPassActivity.class));
        });

    }

}