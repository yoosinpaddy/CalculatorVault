package com.calculator.vault.gallery.locker.hide.data.appLock;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.activity.ChangePasscodeActivity;
import com.calculator.vault.gallery.locker.hide.data.activity.HiddenVideosActivity;
import com.calculator.vault.gallery.locker.hide.data.ads.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.common.OnSingleClickListener;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class AppListActivity extends AppCompatActivity {

    private static final String TAG = "AppListActivity";
    private RecyclerView rvAppList;
    private ArrayList<AppListModel> appListModels = new ArrayList<>();
    private ArrayList<String> lockedPackages;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        rvAppList = findViewById(R.id.rv_appList);
        rvAppList.setLayoutManager(new LinearLayoutManager(AppListActivity.this));
        rvAppList.setHasFixedSize(true);

        String savedP = Preferences.getStringPref(AppListActivity.this, Preferences.KEY_PACKAGES);
        if (savedP != null && !savedP.isEmpty()) {
            lockedPackages = new Gson().fromJson(savedP, new TypeToken<ArrayList<String>>() {
            }.getType());
        } else {
            lockedPackages = new ArrayList<>();
        }


        PackageManager pm = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.PERMISSION_GRANTED);
        list.get(0).activityInfo.applicationInfo.loadLabel(pm).toString();
        list.get(0).activityInfo.applicationInfo.loadIcon(pm);
        String packageName = list.get(0).activityInfo.applicationInfo.packageName;

        AppListModel modelB;
        if (lockedPackages.contains("bluetooth")) {
            modelB = new AppListModel("Bluetooth", "bluetooth", ActivityCompat.getDrawable(AppListActivity.this, R.drawable.ic_blueetooth_app_list), true);
        } else {
            modelB = new AppListModel("Bluetooth", "bluetooth", ActivityCompat.getDrawable(AppListActivity.this, R.drawable.ic_blueetooth_app_list), false);
        }
        appListModels.add(modelB);

        AppListModel modelW;
        if (lockedPackages.contains("wifi")) {
            modelW = new AppListModel("WiFi", "wifi", ActivityCompat.getDrawable(AppListActivity.this, R.drawable.ic_wifi_app_list), true);
        } else {
            modelW = new AppListModel("WiFi", "wifi", ActivityCompat.getDrawable(AppListActivity.this, R.drawable.ic_wifi_app_list), false);
        }
        appListModels.add(modelW);

        for (int i = 0; i < list.size(); i++) {

            AppListModel model;
            if (lockedPackages.contains(list.get(i).activityInfo.applicationInfo.packageName)) {
                    model = new AppListModel(list.get(i).activityInfo.applicationInfo.loadLabel(pm).toString(),
                            list.get(i).activityInfo.applicationInfo.packageName,
                            list.get(i).activityInfo.applicationInfo.loadIcon(pm),
                            true);
            } else {

                model = new AppListModel(list.get(i).activityInfo.applicationInfo.loadLabel(pm).toString(),
                        list.get(i).activityInfo.applicationInfo.packageName,
                        list.get(i).activityInfo.applicationInfo.loadIcon(pm),
                        false);
            }
            if (!model.getAppPackage().equalsIgnoreCase("com.calculator.vault.gallery.locker.hide.data")) {
                appListModels.add(model);
            }
        }

        AppListAdapter adapter = new AppListAdapter(AppListActivity.this, appListModels, lockedPackages);
        rvAppList.setAdapter(adapter);


        findViewById(R.id.iv_back).setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.iv_setting).setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startActivity(new Intent(AppListActivity.this, ChangePasscodeActivity.class));
            }
        });

        if (Share.isNeedToAdShow(AppListActivity.this)) {
//            NativeAdvanceHelper.loadGreedyGameAd(this, (FrameLayout) findViewById(R.id.fl_adplaceholder), "float-4028");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isAccessibilitySettingsOn(AppListActivity.this)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(AppListActivity.this);
            builder.setTitle("Accessibility Permission");
            builder.setMessage("Need accessibility permission to run this app.");
            builder.setNegativeButton("Deny", (dialog, which) -> {
                dialog.dismiss();
                finish();
            }).setPositiveButton("Allow", (dialog, which) -> {
                dialog.dismiss();
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            });
            builder.setCancelable(false);
            builder.show();

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean hasPermissionToReadNetworkHistory() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        final AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        appOps.startWatchingMode(AppOpsManager.OPSTR_GET_USAGE_STATS,
                getApplicationContext().getPackageName(),
                new AppOpsManager.OnOpChangedListener() {
                    @Override
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    public void onOpChanged(String op, String packageName) {
                        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                                android.os.Process.myUid(), getPackageName());
                        if (mode != AppOpsManager.MODE_ALLOWED) {
                            return;
                        }
                        appOps.stopWatchingMode(this);
//                        Intent intent = new Intent(AppListActivity.this, MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                        getApplicationContext().startActivity(intent);
                    }
                });
        return false;
    }

    // To check if service is enabled
    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + Accessibilty.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }

        return false;
    }

}
