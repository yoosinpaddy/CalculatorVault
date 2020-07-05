package com.calculator.vault.gallery.locker.hide.data.ads.appLovinSupport;

import android.app.Activity;

/**
 * Defines activity lifecycle methods which should be forwarded to an AppLovin view.
 */
public interface AppLovinActivityCallbacks
{
    /**
     * Override your activity or fragment's onResume method and invoke this method with a reference to the current activity.
     * @param activity Current activity.
     */
    public void onResume(final Activity activity);

    /**
     * Override your activity or fragment's onResume method and invoke this method with a reference to the current activity.
     * @param activity Current activity.
     */
    public void onStop(final Activity activity);
}
