package com.calculator.vault.gallery.locker.hide.data.activity;

import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.calculator.vault.gallery.locker.hide.data.R;

public class ExitActivity extends AppCompatActivity {

    String TAG = "TAG";

    private Handler timeoutHandler = new Handler();
    Runnable runnable;
    private boolean is_pause;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_exit);
        setDelay();
    }

    private void setDelay() {
        runnable = this::nextScreen;
        timeoutHandler.postDelayed(runnable, 1500);
    }

    private void nextScreen() {
        finishAffinity();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (is_pause)
        {
            is_pause = false;
            nextScreen();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timeoutHandler != null)
            timeoutHandler.removeCallbacks(runnable);
        is_pause = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        is_pause = true;
        if (timeoutHandler != null)
            timeoutHandler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        is_pause = true;
        if (timeoutHandler != null)
            timeoutHandler.removeCallbacks(runnable);
    }

    @Override
    public void onBackPressed() {
        Log.e(TAG, "onBackPressed: Do Nothing!");
    }
}
