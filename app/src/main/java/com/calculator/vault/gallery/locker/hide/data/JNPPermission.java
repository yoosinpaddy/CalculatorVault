package com.calculator.vault.gallery.locker.hide.data;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class JNPPermission {


    // messages
    public static String FAILED_SINGLE = "Required Permission not granted";
    public static String FAILED_MULTIPLE = "Required Permissions not granted";

    /**
     * ToDo.. Check if permission is already exist
     *
     * @param mContext   The context
     * @param permission The name of the permission being checked.
     * @return {@link PackageManager#PERMISSION_GRANTED} if you have the
     * permission, or {@link PackageManager#PERMISSION_DENIED} if not.
     */
    public static boolean hasPermission(Context mContext, String permission) {
        if (ActivityCompat.checkSelfPermission((Activity) mContext, permission)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    /**
     * ToDo.. Check if permissions are already exist
     *
     * @param mContext    The context
     * @param permissions The name of the permission being checked.
     * @return {@link PackageManager#PERMISSION_GRANTED} if you have the
     * permission, or {@link PackageManager#PERMISSION_DENIED} if not.
     */
    public static boolean hasPermission(Context mContext, String[] permissions) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            result = ContextCompat.checkSelfPermission(mContext, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            return false;
        }
        return true;
    }


    /**
     * ToDo.. Show alert dialog if user denied permission
     *
     * @param mContext   The context
     * @param message    The message to display
     * @param okListener The okListener to handle user response
     */
    public static void showDialog(Context mContext, String message, DialogInterface
            .OnClickListener okListener) {
        new AlertDialog.Builder((Activity) mContext)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


}
