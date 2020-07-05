package com.calculator.vault.gallery.locker.hide.data.appLock;

import android.graphics.drawable.Drawable;

public class AppListModel {

    private String appName;
    private String appPackage;
    private Drawable appIcon;
    private boolean isLocked;

    public AppListModel(String appName, String appPackage, Drawable appIcon, boolean isLocked) {
        this.appName = appName;
        this.appPackage = appPackage;
        this.appIcon = appIcon;
        this.isLocked = isLocked;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}
