package com.calculator.vault.gallery.locker.hide.data.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.CameraError;
import com.androidhiddencamera.HiddenCameraActivity;
import com.androidhiddencamera.HiddenCameraUtils;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;
import com.androidhiddencamera.config.CameraRotation;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.data.ImageVideoDatabase;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.model.BreakInImageModel;
import com.calculator.vault.gallery.locker.hide.data.model.UserModel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static com.calculator.vault.gallery.locker.hide.data.common.Share.databasewritepath;
import static com.calculator.vault.gallery.locker.hide.data.common.Share.snoopPicPath;

public class CheckPasswordActivity extends HiddenCameraActivity implements View.OnClickListener {

    EditText moEtPassword;
    String msGetPassword, msConfirmPassword;
    TextView moTvDone, moTvForgotPass;
    TextView moTvPasswordType;
    String msPasswordType;
    private List<String> listPermissionsNeeded = new ArrayList<>();
    public final int STORAGE_PERMISSION_CODE = 23;
    ImageVideoDatabase moDBimageVideoDatabase = new ImageVideoDatabase(this);
    private CameraConfig moCameraConfig;
    private String msSwitchActive;
    private String TAG = this.getClass().getSimpleName();
    private String EnterePAssword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_password);
        initView();

        initViewListener();
        initViewAction();

        msSwitchActive = SharedPrefs.getString(CheckPasswordActivity.this, SharedPrefs.isSwitchActive, "false");

        if (msSwitchActive.equals("true")) {
            Log.e(TAG, "onCreate: " + "true");
            moCameraConfig = new CameraConfig()
                    .getBuilder(this)
                    .setCameraFacing(CameraFacing.FRONT_FACING_CAMERA)
                    .setCameraResolution(CameraResolution.MEDIUM_RESOLUTION)
                    .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                    .setImageRotation(CameraRotation.ROTATION_270)
                    .build();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                //Start camera preview
                startCamera(moCameraConfig);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
            }
        } else {
            Log.e(TAG, "onCreate: " + "true");
        }
    }

    private boolean checkAndRequestPermissions() {

        listPermissionsNeeded.clear();
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readStorage = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);

        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (readStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_EXTERNAL_STORAGE);
        }


        return listPermissionsNeeded.isEmpty();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case STORAGE_PERMISSION_CODE:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        Log.e("TAG", "onRequestPermissionsResult: deny");

                    } else {
                        Log.e("TAG", "onRequestPermissionsResult: dont ask again");

                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setTitle("Permissions Required")
                                .setMessage("Please allow permission for storage")
                                .setPositiveButton("Cancel", (dialog, which) -> dialog.dismiss())
                                .setNegativeButton("Ok", (dialog, which) -> {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                })
                                .setCancelable(false)
                                .create()
                                .show();
                    }
                }
                break;
        }
    }

    private void initView() {
        moEtPassword = findViewById(R.id.et_pass);
        moTvDone = findViewById(R.id.btn_done);
        moTvPasswordType = findViewById(R.id.tv_passwordType);
        moTvForgotPass = findViewById(R.id.tv_forgotPass);
    }

    private void initViewListener() {
        moTvDone.setOnClickListener(this);
        moTvForgotPass.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        msSwitchActive = SharedPrefs.getString(CheckPasswordActivity.this, SharedPrefs.isSwitchActive, "false");

        if (msSwitchActive.equals("true")) {
            Log.e(TAG, "onCreate: " + "true");
            moCameraConfig = new CameraConfig()
                    .getBuilder(this)
                    .setCameraFacing(CameraFacing.FRONT_FACING_CAMERA)
                    .setCameraResolution(CameraResolution.MEDIUM_RESOLUTION)
                    .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                    .setImageRotation(CameraRotation.ROTATION_270)
                    .build();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                //Start camera preview
                startCamera(moCameraConfig);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
            }
        } else {
            Log.e(TAG, "onCreate: " + "true");
        }
        if (Share.ChangePassword) {
            Log.e("TAG:: ", "ChangePassword: " + "else");
            msPasswordType = getString(R.string.NewPassword);
            moTvPasswordType.setText(getString(R.string.newPass));
        }
    }

    private void initViewAction() {


        msGetPassword = moEtPassword.getText().toString();
        int maxLength = 4;
        moEtPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        if (Share.ChangePassword) {
            Log.e("TAG:: ", "ChangePassword: " + "else");
            msPasswordType = getString(R.string.NewPassword);
            moTvPasswordType.setText(getString(R.string.newPass));
        } else {
            Log.e("TAG:: ", "Oldpassword: " + "else");
            if (CheckFileExists() && (moDBimageVideoDatabase.getUserTableCount() > 0)) {
                msPasswordType = getString(R.string.OldPassword);
                moTvPasswordType.setText(getString(R.string.oldPass));
            } else {
                Log.e("TAG:: ", "initViewAction: " + "else");
                msPasswordType = getString(R.string.NewPassword);
                moTvPasswordType.setText(getString(R.string.newPass));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (msPasswordType.equals(getString(R.string.OldPassword))) {
            Log.e("TAG:: ", "onBackPressed: " + "OldPassword exists");
            finish();
        } else if (msPasswordType.equals(getString(R.string.NewPassword))) {
            finish();

        } else {
            msPasswordType = getString(R.string.NewPassword);
            moTvPasswordType.setText(getString(R.string.newPass));
            moEtPassword.setText("");
            moTvDone.setText(getString(R.string.DONE));
        }
    }

    public void showDialog() {
   /* final Dialog dialog = new Dialog(CheckPasswordActivity.this);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setContentView(R.layout.dialog_exit_editing);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
    Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
    btnNo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
        }
    });

    btnYes.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            dialog.dismiss();
            finish();

        }
    });



    if (dialog != null && !dialog.isShowing())
        dialog.show();*/
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_done:
                if (checkAndRequestPermissions()) {
                    Log.e("TAG:: ", "onClick: " + "Check Permission approved");
                    CheckButtonClick();
                } else {
                    Log.e("TAG:: ", "onClick: " + "Check Permission no permission");
                    ActivityCompat.requestPermissions(CheckPasswordActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);
                }


                break;

            case R.id.tv_forgotPass:
                createNewPassword();
                File file = new File(databasewritepath + "ImageVideoDatabase.db");
                if (file.exists()) {
                    file.delete();
                } else {

                }
                try {
                    Log.e("TAG:: ", "onClick: " + " Create New File");
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void CheckButtonClick() {
        if (msPasswordType.equals(getString(R.string.OldPassword))) {
            Log.e("TAG:: ", "onClick: " + "OldPassword exists");
            if (CheckFileExists()) {
                msGetPassword = moEtPassword.getText().toString();
                Log.e("TAg:: ", "onClick: " + " OldPassword FileExists");
                moDBimageVideoDatabase = new ImageVideoDatabase(CheckPasswordActivity.this);
                UserModel userModel = moDBimageVideoDatabase.getSingleUserData(1);
                String oldPass = userModel.getPassword();
                Log.e("TAG:: ", "onClick: old pass:: " + oldPass);
                Log.e("TAG:: ", "onClick: getPAssword:: " + msGetPassword);
                if (oldPass.equals(msGetPassword)) {
                    checkPassword();
                } else {
                    try {
                        if (msSwitchActive.equals("true")) {
                            Log.e(TAG, "CheckButtonClick: " + "Take picture true");
                            EnterePAssword = msGetPassword;
                            takePicture();
                        } else {
                            Log.e(TAG, "CheckButtonClick: " + "Take picture false");
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "CheckButtonClick: Exception:: " + e.getMessage());
                    }
                    Toast.makeText(this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (msPasswordType.equals(getString(R.string.NewPassword))) {
            Log.e("TAG:: ", "onClick: " + " New Password btn Clicked");
            File path = new File(databasewritepath);
            if (!path.exists())
                path.mkdirs();
            File loFile = new File(databasewritepath + "ImageVideoDatabase.db");
            if (!loFile.exists()) {
                Log.e("TAG:: ", "initViewAction: " + "FilenotExist");
                try {
                    loFile.createNewFile();
                    Log.e("TAG:: ", "initViewAction: " + " Created new File");
                } catch (IOException e) {
                    Log.e("TAG:: ", "initViewAction: " + " exception:: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            msGetPassword = moEtPassword.getText().toString();
            Log.e("TAG:: ", "CheckButtonClick: " + msGetPassword.length());
            if (msGetPassword.length() == 4) {
                moDBimageVideoDatabase = new ImageVideoDatabase(CheckPasswordActivity.this);
                msGetPassword = moEtPassword.getText().toString();
                Log.e("New Password:: ", "onClick: " + msGetPassword);
                msPasswordType = getString(R.string.ConfirmPassword);
                moTvPasswordType.setText(getString(R.string.confPass));
                moEtPassword.clearFocus();
                moEtPassword.setText("");
                moTvDone.setText(getString(R.string.ConfirmPassword));
            } else {
                Toast.makeText(this, "Please Enter a 4 Digit Password", Toast.LENGTH_SHORT).show();
                Log.e("TAG:: ", "checkPassword: " + "Enter 4 digit Password");
            }


        } else {

            msConfirmPassword = moEtPassword.getText().toString();
            Log.e("TAG:: ", "onClick: " + " Confirm Password:: " + msConfirmPassword);
            if (msGetPassword.equals(msConfirmPassword)) {

                UserModel userModel = new UserModel();
                userModel.setPassword(msGetPassword);
                userModel.setConfirm_password(msConfirmPassword);
                userModel.setDatabasePath(databasewritepath);

                if(Share.ChangePassword){
                userModel.setID(1);
                    moDBimageVideoDatabase.updateSingleDataPassword(userModel);
                    moEtPassword.setText("");
                    Share.ChangePassword=false;
                    Log.e("TAG:: ", "onClick: " + " Correct Password");
                    finish();
                }else{
                    moDBimageVideoDatabase.addUserData(userModel);
                    moEtPassword.setText("");
                    Log.e("TAG:: ", "onClick: " + " Correct Password");
                    Intent intent = new Intent(CheckPasswordActivity.this, SelectionActivity.class);
                    startActivity(intent);
                }


            } else {
                Toast.makeText(this, "Incorrect Password ", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void createNewPassword() {
        msPasswordType = getString(R.string.NewPassword);
        moTvPasswordType.setText(getString(R.string.newPass));
    }

    private boolean CheckFileExists() {
        File file = new File(databasewritepath + "ImageVideoDatabase.db");
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    private void checkPassword() {
        Log.e("TAG", "checkPassword: " + "called");
        if (msGetPassword.length() == 4) {
            Log.e("TAG:: ", "checkPassword: from Db:: ");
            moEtPassword.setText("");
            Intent intent = new Intent(CheckPasswordActivity.this, SelectionActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please Enter a 4 Digit Password", Toast.LENGTH_SHORT).show();
            Log.e("TAG:: ", "checkPassword: " + "Enter 4 digit Password");
        }
    }

    @Override
    public void onImageCapture(@NonNull File imageFile) {

        Log.e(TAG, "onImageCapture: " + imageFile.getAbsolutePath());
        Log.e(TAG, "onImageCapture: " + imageFile.getName());
        File path = new File(snoopPicPath);
        if (!path.exists())
            path.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_hhmmssSSS").format(new Date());

        File from = new File(imageFile.getParent(), imageFile.getName());

        //Log.e("TAG", "onActivityResult: name " +filename[0]);
        //  String fileNameWithOutExt = FilenameUtils.removeExtension(filename1);
        File to = new File(imageFile.getParent(), "snoopfile" + timeStamp + "." + FilenameUtils.getExtension(from.getName()));
        // File to = new File(file.getParent(), "file" + timeStamp + "." + fileNameExt);
        from.renameTo(to);
        Log.e("TAG:: ", "onActivityResult: " + to.getName());
        copyPhotoTo(to.getAbsolutePath(), snoopPicPath, to.getName());
        File file = new File(snoopPicPath + to.getName());

        String filename = file.getName();
        BreakInImageModel breakInImageModel = new BreakInImageModel();


        String dataTime = getDate(file.lastModified(), "dd/MM/yyyy hh:mm:ss a");
        breakInImageModel.setFilename(file.getName());
        breakInImageModel.setFilePath(file.getAbsolutePath());
        breakInImageModel.setDataTime(dataTime);
        breakInImageModel.setWrongPassword(EnterePAssword);

        moDBimageVideoDatabase.addBreakInReportData(breakInImageModel);

        File delFile = new File(imageFile.getAbsolutePath());
        try {
            delFile.delete();
            to.delete();
        } catch (Exception e) {
            Log.e(TAG, "onImageCapture: delete Exception" + e.getMessage());
        }
        Uri contentUri = Uri.fromFile(to);


        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);

        MediaScannerConnection.scanFile(CheckPasswordActivity.this, new String[]{to.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String s, Uri uri) {
                Log.e("TAG", "onScanCompleted: ");
            }
        });

        msSwitchActive = SharedPrefs.getString(CheckPasswordActivity.this, SharedPrefs.isSwitchActive, "false");

        if (msSwitchActive.equals("true")) {
            Log.e(TAG, "onCreate: " + "true");
            moCameraConfig = new CameraConfig()
                    .getBuilder(this)
                    .setCameraFacing(CameraFacing.FRONT_FACING_CAMERA)
                    .setCameraResolution(CameraResolution.MEDIUM_RESOLUTION)
                    .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                    .setImageRotation(CameraRotation.ROTATION_270)
                    .build();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                //Start camera preview
                startCamera(moCameraConfig);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
            }
        } else {
            Log.e(TAG, "onImageCapture: " + "true");
        }
    }

    public void copyPhotoTo(String pathToWatch, String pathToWrite, String str) {
        File path = new File(pathToWrite);
        if (!path.exists())
            path.mkdirs();
        // File file = new File(pathToWrite + str);
        //  if (!file.exists()) {
        try {
            File loFile = new File(pathToWrite + str);
            if (!loFile.exists())
                loFile.createNewFile();

            FileUtils.copyFile(new File(pathToWatch), new File(pathToWrite + str));
            Log.e("TAG", "CopyFileDone: " + str);
        } catch (IOException e) {
            Log.e("TAG", "onEvent: Error");
            e.printStackTrace();
        }
        // }
    }

    @Override
    public void onCameraError(int errorCode) {
        switch (errorCode) {
            case CameraError.ERROR_CAMERA_OPEN_FAILED:
                //Camera open failed. Probably because another application
                Log.e(TAG, "onCameraError: " + "ERROR_CAMERA_OPEN_FAILED");
                //is using the camera
                break;
            case CameraError.ERROR_IMAGE_WRITE_FAILED:
                Log.e(TAG, "onCameraError: " + "ERROR_IMAGE_WRITE_FAILED");
                //Image write failed. Please check if you have provided WRITE_EXTERNAL_STORAGE permission
                break;
            case CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE:
                Log.e(TAG, "onCameraError: " + "ERROR_CAMERA_PERMISSION_NOT_AVAILABLE");
                //camera permission is not available
                //Ask for the camra permission before initializing it.
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION:
                Log.e(TAG, "onCameraError: " + "ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION");
                //Display information dialog to the user with steps to grant "Draw over other app"
                //permission for the app.
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA:
                Log.e(TAG, "onCameraError: " + "ERROR_DOES_NOT_HAVE_FRONT_CAMERA");
                Toast.makeText(this, "Your device does not have front camera.", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        /*    System.out.println(getDate(82233213123L, "dd/MM/yyyy hh:mm:ss.SSS"));
         */
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
