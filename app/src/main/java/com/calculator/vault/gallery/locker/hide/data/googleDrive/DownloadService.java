package com.calculator.vault.gallery.locker.hide.data.googleDrive;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.data.ImageVideoDatabase;
import com.calculator.vault.gallery.locker.hide.data.model.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.Collections;

import ir.mahdi.mzip.zip.ZipArchive;

public class DownloadService extends Service {

    private static final String TAG = "UploadService";
    private DriveServiceHelper mDriveServiceHelper;
    private String fileId;
    private NotificationManager mNotificationManager;
    private NotificationManagerCompat notificationManager;
    private Notification.Builder notification;
    private NotificationCompat.Builder builder;
    private UserModel moUserModel;
    public static boolean isDownloadRuinning = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isDownloadRuinning = true;
        // Use the authenticated account to sign in to the Drive service.
        GoogleAccountCredential credential =
                GoogleAccountCredential.usingOAuth2(this, Collections.singleton(DriveScopes.DRIVE_FILE));
        credential.setSelectedAccount(GoogleSignIn.getLastSignedInAccount(DownloadService.this).getAccount());
        Drive googleDriveService =
                new Drive.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        new GsonFactory(), credential)
                        .setApplicationName("Drive API Migration")
                        .build();

        // The DriveServiceHelper encapsulates all REST API and SAF functionality.
        // Its instantiation is required before handling any onClick actions.
        mDriveServiceHelper = new DriveServiceHelper(googleDriveService);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String CHANNEL_ID = "my_channel_02"; // The id of the channel.
            CharSequence name = getString(R.string.app_name); // The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notification = new Notification.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Download backup...")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setChannelId(CHANNEL_ID)
                    .setOngoing(true)
                    .setProgress(0,0,true)
                    .setAutoCancel(false)
                    .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0));

            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
                //mNotificationManager.notify(1, notification.build());
                Log.e(TAG, "onStartCommand1: ");
            }

            startForeground(4,notification.build());
        }else {

            notificationManager = NotificationManagerCompat.from(DownloadService.this);
            builder = new NotificationCompat.Builder(DownloadService.this, "notification_id");
            builder.setContentTitle(getString(R.string.app_name))
                    .setContentText("Downloading backup...")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0))
                    .setPriority(NotificationCompat.PRIORITY_LOW);

            // Issue the initial notification with zero progress
            int PROGRESS_MAX = 0;
            int PROGRESS_CURRENT = 0;
            builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, true);
            notificationManager.notify(4, builder.build());
            Log.e(TAG, "onStartCommand1: ");
        }
        download();
        return START_NOT_STICKY;
    }

    /**
     * Creates a new file via the Drive REST API.
     */
    private void download() {

        if (mDriveServiceHelper != null) {
            Log.d(TAG, "Creating a file.");

            mDriveServiceHelper.queryFiles()
                    .addOnSuccessListener(fileList -> {
                        StringBuilder builderS = new StringBuilder();
                        for (File file : fileList.getFiles()) {
                            builderS.append(file.getName()).append("\n");
                            fileId = file.getId();
                        }
                        if (fileId != null && !fileId.isEmpty()) {
                            mDriveServiceHelper.downloadfile(fileId).addOnSuccessListener(s -> {

                            new UnzipingTask().execute();

                            }).addOnFailureListener(e -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                                    notification.setOngoing(false);
                                    notification.setContentText("Backup Download Failed.");
                                    notification.setProgress(0,0,false);
                                    mNotificationManager.notify(4, notification.build());

                                }else {

                                    builder.setOngoing(false);
                                    builder.setContentText("Backup Download Failed.");
                                    builder.setProgress(0,0,false);
                                    notificationManager.notify(4, builder.build());

                                }

                                this.stopSelf();

                            });
                        }else {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

                                notification.setOngoing(false);
                                notification.setContentText("No Backup Found.");
                                notification.setProgress(0,0,false);
                                mNotificationManager.notify(4, notification.build());
                                this.stopSelf();

                            }else {

                                builder.setOngoing(false);
                                builder.setContentText("No Backup Found.");
                                builder.setProgress(0,0,false);
                                notificationManager.notify(4, builder.build());
                                this.stopSelf();

                            }
                        }

//                        Toast.makeText(this, "Download complete.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(exception -> {

                        Log.e(TAG, "Unable to query files.", exception);

//                        Toast.makeText(this, "Download failed.", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                            notification.setOngoing(false);
                            notification.setContentText("No Backup Found.");
                            notification.setProgress(0,0,false);
                            mNotificationManager.notify(4, notification.build());

                        }else {

                            builder.setOngoing(false);
                            builder.setContentText("No Backup Found.");
                            builder.setProgress(0,0,false);
                            notificationManager.notify(4, builder.build());

                        }

                        this.stopSelf();

                    });
        }
    }

    private class UnzipingTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ImageVideoDatabase loImageVideoDatabase = new ImageVideoDatabase(DownloadService.this);
            moUserModel = loImageVideoDatabase.getSingleUserData(1);

        }

        @Override
        protected Void doInBackground(String... strings) {

            java.io.File file = new java.io.File(Environment.getExternalStorageDirectory().getPath() + java.io.File.separator + ".androidData");
            if(file.exists()) {
                try {
                    file.delete();
                    file.getCanonicalFile().delete();
                    if(file.exists()){
                        getApplicationContext().deleteFile(file.getName());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ZipArchive zipArchive = new ZipArchive();

            zipArchive.unzip(Environment.getExternalStorageDirectory().getPath() + java.io.File.separator + "CalculatorBackupDownloaded.zip",
                    Environment.getExternalStorageDirectory().getPath() + java.io.File.separator, "");

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ImageVideoDatabase loImageVideoDatabase = new ImageVideoDatabase(DownloadService.this);
            loImageVideoDatabase.updateUserRaw(moUserModel);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                notification.setOngoing(false);
                notification.setContentText("Backup Downloaded.");
                notification.setProgress(0,0,false);
                mNotificationManager.notify(4, notification.build());

            }else {

                builder.setOngoing(false);
                builder.setContentText("Backup Downloaded.");
                builder.setProgress(0,0,false);
                notificationManager.notify(4, builder.build());

            }

            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDownloadRuinning = false;
    }
}
