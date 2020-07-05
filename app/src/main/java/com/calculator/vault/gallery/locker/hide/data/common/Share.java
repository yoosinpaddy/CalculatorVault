package com.calculator.vault.gallery.locker.hide.data.common;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Share {
    public static String msPathToWrite = Environment.getExternalStorageDirectory().getPath() + File.separator + ".androidData" + File.separator + ".log" + File.separator + ".check" + File.separator;
    public static String msPathToWriteDecoy =
            Environment.getExternalStorageDirectory().getPath() + File.separator + ".androidData" + File.separator + ".log" + File.separator + ".dup" + File.separator;
    public static final String databasewritepath =
            Environment.getExternalStorageDirectory().getPath() + File.separator + ".androidData" + File.separator + ".log" + File.separator + ".check" + File.separator;
    public static final String snoopPicPath =
            Environment.getExternalStorageDirectory().getPath() + File.separator + ".androidData" + File.separator + ".french" + File.separator + ".snoop" + File.separator;

    public static int screenHeight;
    public static int width = 0;
    public static int height = 0;
    public static int position;

    public static final String IS_RATED = "isRated";
    public static final String RATE_APP_OPEN_COUNT = "appOpenCount";
    public static final String RATE_APP_HIDE_COUNT = "appHideCount";

    public static boolean isRateDisplayed = false;

    public static Uri BG_GALLERY = null;
    public static ArrayList<String> moImageList = new ArrayList<>();

    public static boolean isclickedOnDone = false;
    public static String croppedImage;
    public static String msPathToWriteContact =
            Environment.getExternalStorageDirectory().getPath() + File.separator + ".androidData" + File.separator + ".con" + File.separator + ".make" + File.separator;
    public static boolean ChangePassword = false;
    public static boolean decoyPasscode = false;
    public static boolean changeDecoy = false;
    public static boolean isSwitchNeedToBeOn = false;

    public static String msCurrentLockedPackege = "";

    public static String imageUrl;

    public static SharedPreferences preferences;

    public static boolean isFromSelection = false;

    public static ArrayList<String> selected_image_list = new ArrayList<>();

    public class KEYNAME {
        public static final String ALBUM_ID = "album_id";
        public static final String ALBUM_NAME = "album_name";
        public static final String SELECTED_IMAGE = "selected_image";
        public static final String SELECTED_PHONE_IMAGE = "selected_phone_image";
    }

    public static MotionEvent event;

    public static Boolean RestartApp(Activity activity) {

        if (!Share.checkAndRequestPermissions(activity, 1)) {
            Intent i = activity.getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(i);
            return false;
        } else {
            return true;
        }
    }

    public static Dialog showProgress1(Activity mContext, String text) {

        Dialog mDialog = new Dialog(mContext, R.style.MyTheme);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View layout = mInflater.inflate(R.layout.dialog_progress, null);
        mDialog.setContentView(layout);

        TextView mTextView = (TextView) layout.findViewById(R.id.text);
        if (text.equals(""))
            mTextView.setVisibility(View.GONE);
        else
            mTextView.setText(text);

        mDialog.setCancelable(false);
        // aiImage.post(new Starter(activityIndicator));
        return mDialog;
    }

    public static String saveFaceInternalStorage(Context context, Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(context);

        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        if (bitmapImage != null) {
            File mypath = new File(directory, "profile.png");
            Log.e("TAG", "" + mypath);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.e("TAG", "Not Saved Image------------------------------------------------------->");
        }
        return directory.getAbsolutePath();
    }


    public static boolean checkAndRequestPermissions(Activity act, int code) {

        if (ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    //Load Banner for Google
    public static void loadAdsBanner(Activity activity, AdView adView) {

        if (!Share.isNeedToAdShow(activity)) {
            adView.setVisibility(View.GONE);
            return;
        } else {
            adView.setVisibility(View.GONE);
            return;
        }
    }

    public static boolean isNeedToAdShow(Context context) {
        boolean isNeedToShow = false;
        try {
            isNeedToShow = SharedPrefs.getBoolean(context, SharedPrefs.IS_ADS_REMOVED, false);
        } catch (Exception ex) {
            Log.e("TAG", "isNeedToAdShow: " + ex.toString());
        }
        Log.i("ABCCC", "isNeedToAdShow: " + isNeedToShow);
        return isNeedToShow;
    }

    public static void showAlert(final Activity activity, String title, String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.setCancelable(false);
        builder.show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
