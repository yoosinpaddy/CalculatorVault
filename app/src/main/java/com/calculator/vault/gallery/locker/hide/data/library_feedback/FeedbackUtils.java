package com.calculator.vault.gallery.locker.hide.data.library_feedback;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.calculator.vault.gallery.locker.hide.data.BuildConfig;
import com.calculator.vault.gallery.locker.hide.data.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FeedbackUtils {

    private static final String emailId = "davidrpetroski@gmail.com";
    private static final String message = "Please take some time and give us your valuable feedback so we can provide better and better service in our next build.";


    /*
     * ToDo.. Confirmation Dialog
     */
    public static void FeedbackDialog(Context mContext) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(getAppLabel(mContext, false));
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                sendEmail(mContext);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private static void sendEmail(Context mContext) {

        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailId});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getAppLabel(mContext, true));


        String mailBody = "...";

        emailIntent.putExtra(Intent.EXTRA_TEXT, mailBody);

        ArrayList<Uri> uris = new ArrayList<>();


        Uri deviceInfoUri = createFileFromString(mContext, DeviceInfo.getAllDeviceInfo(mContext), "deviceInf.txt");
        uris.add(deviceInfoUri);

        Uri logUri = createFileFromString(mContext, SystemLog.extractLogToString(), "deviceLog.txt");
        uris.add(logUri);


        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mContext.startActivity(createEmailOnlyChooserIntent(mContext, emailIntent, "Send Feedback"));
    }

    private static Uri createFileFromString(Context mContext, String text, String name) {
        File file = new File(mContext.getExternalCacheDir(), name);
        //create the file if it didn't exist
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, false to overrite to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(file, false));
            buf.write(text);
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", file);
    }


    private static String getAppLabel(Context mContext, boolean isAppVersionRequired) {
        String subject = "Feedback of " + mContext.getResources().getString(R.string.app_name);
        if (isAppVersionRequired) {
            subject = subject + " " + BuildConfig.VERSION_NAME;
        }
        return subject;
    }


    private static Intent createEmailOnlyChooserIntent(Context context, Intent source,
                                                       CharSequence chooserTitle) {
        Stack<Intent> intents = new Stack<>();
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
                "info@domain.com", null));
        List<ResolveInfo> activities = context.getPackageManager()
                .queryIntentActivities(i, 0);

        for (ResolveInfo ri : activities) {
            Intent target = new Intent(source);
            target.setPackage(ri.activityInfo.packageName);
            intents.add(target);
        }

        if (!intents.isEmpty()) {
            Intent chooserIntent = Intent.createChooser(intents.remove(0),
                    chooserTitle);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    intents.toArray(new Parcelable[intents.size()]));

            return chooserIntent;
        } else {
            return Intent.createChooser(source, chooserTitle);
        }
    }


}
