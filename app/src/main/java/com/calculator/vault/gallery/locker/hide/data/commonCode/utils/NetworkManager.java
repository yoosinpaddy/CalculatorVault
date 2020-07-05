package com.calculator.vault.gallery.locker.hide.data.commonCode.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

public class NetworkManager {

    @SuppressWarnings("deprecation")
    public static boolean isInternetConnected(final Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {

            return true;
        }
        else {

            return false;
        }
    }

    public static boolean isWiFiConnected(Context context) {
        // Create object for ConnectivityManager class which returns network
        // related info
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // If connectivity object is not null
        if (connectivity != null) {
            // Get network info - WIFI internet access
            NetworkInfo info = connectivity
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                // Look for whether device is currently connected to WIFI
                // network
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isGPSConnected(Context context) {
        LocationManager service = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        boolean status = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        return status;
    }

    public static void showGPSSettingsAlert(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS Settings");

        // Setting Dialog Message
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * Display a dialog that user has no internet connection lauch Settings
     * Options
     */
    public static void showInternetSettingsAlert(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("Internet Settings");

        // Setting Dialog Message
        alertDialog
                .setMessage("Internet is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_WIRELESS_SETTINGS);
                        context.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        // Showing Alert Message
        alertDialog.show();
    }

}
