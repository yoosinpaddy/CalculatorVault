package com.calculator.vault.gallery.locker.hide.data.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.google.zxing.integration.android.IntentIntegrator;

import example.zxing.ToolbarCaptureActivity;

public class JQrActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;
    private boolean hasRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_j_qr);

        findViewById(R.id.imgMyQR).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    openRealQRActivity();
                } else {
                    requestPermission();
                }
            }
        });

    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {
        hasRequested = true;
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && hasRequested) {
                openRealQRActivity();
            } else {
                Toast.makeText(this, "Camera permission is required for QR code to work", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openRealQRActivity() {
        new IntentIntegrator(JQrActivity.this).setCaptureActivity(ToolbarCaptureActivity.class).initiateScan();
        finish();
    }

}