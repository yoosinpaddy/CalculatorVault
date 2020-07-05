package com.calculator.vault.gallery.locker.hide.data.appLock;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.common.OnSingleClickListener;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.commonCode.activities.HomePageActivity;
import com.calculator.vault.gallery.locker.hide.data.recycleview.GridSpacingItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class ThemeListActivity extends AppCompatActivity {

    private static final String TAG = "ThemeListActivity";
    private RecyclerView rvThemeList;
    private ArrayList<ThemeModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_list);

        rvThemeList = findViewById(R.id.theme_rv_list);
        rvThemeList.setLayoutManager(new GridLayoutManager(this, 2));
        rvThemeList.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(ThemeListActivity.this, 8), true));
        rvThemeList.setHasFixedSize(true);

        if (getIntent().getStringExtra("forWhat").equals("pattern")) {

            String savedList = Preferences.getStringPref(ThemeListActivity.this, Preferences.KEY_SAVED_THEME_LIST);
            list = new Gson().fromJson(savedList, new TypeToken<ArrayList<ThemeModel>>() {
            }.getType());
            ThemeListAdapter adapter = new ThemeListAdapter(ThemeListActivity.this, list, "pattern");
            rvThemeList.setAdapter(adapter);

        } else {

            String savedListPin = Preferences.getStringPref(ThemeListActivity.this, Preferences.KEY_SAVED_THEME_LIST_PIN);
            list = new Gson().fromJson(savedListPin, new TypeToken<ArrayList<ThemeModel>>() {
            }.getType());
            ThemeListAdapter adapter = new ThemeListAdapter(ThemeListActivity.this, list, "pin");
            rvThemeList.setAdapter(adapter);
        }

        findViewById(R.id.iv_back).setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onBackPressed();
            }
        });

        if (Share.isNeedToAdShow(ThemeListActivity.this)) {
            findViewById(R.id.iv_more_apps).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.iv_more_apps).setVisibility(View.GONE);
        }

        findViewById(R.id.iv_more_apps).setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent iMoreApps = new Intent(ThemeListActivity.this, HomePageActivity.class);
                startActivity(iMoreApps);
            }
        });

    }

    /**
     * Converting dp to pixel
     */
    public static int dpToPx(Activity activity, int dp) {
        Resources r = activity.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
