package com.calculator.vault.gallery.locker.hide.data.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.activity.AlbumActivity;
import com.calculator.vault.gallery.locker.hide.data.activity.AlbumImagesActivity;
import com.calculator.vault.gallery.locker.hide.data.activity.CropImageActivity;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.recycleview.FontUtils;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainFragment extends Fragment {

    private static final int REQUEST_PICK_IMAGE = 10011;
    private static final int REQUEST_SAF_PICK_IMAGE = 10012;
    private static final String PROGRESS_DIALOG = "ProgressDialog";
    public final int STORAGE_PERMISSION_CODE = 23;

    private static List<String> listPermissionsNeeded = new ArrayList<>();
    private static final int REQUEST_SETTINGS_PERMISSION = 102;
    // Views ///////////////////////////////////////////////////////////////////////////////////////
    private CropImageView mCropView;
    private LinearLayout mRootLayout;
    private SharedPreferences preferences;

    private View moView;

    // Note: only the system can call this constructor by reflection.
    public MainFragment() {
    }

    public static MainFragment getInstance() {

        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.e("MainFragment", "onCreate");
        showProgress();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, null, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // bind Views
        moView = view;
        bindViews(view);
        FontUtils.setFont(mRootLayout);
  /*      if (Share.imageUrl == null)
            Share.imageUrl = preferences.getString("tempimage", null);*/
        Log.e("TAG", "Fragment:==>" + Share.imageUrl);

        if (mCropView.getImageBitmap() == null) {
            if (Share.imageUrl != null && !Share.imageUrl.equals("")) {

                Glide.with(this)
                        .load(Share.imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .override(300, 300)
                        .into(mCropView);
                dismissProgress();
            } else {
                dismissProgress();
                Share.RestartApp((Activity) getContext());
            }
        }
    }


    // Bind views //////////////////////////////////////////////////////////////////////////////////

    private void bindViews(View view) {

        mCropView = (CropImageView) view.findViewById(R.id.cropImageView);
        view.findViewById(R.id.buttonDone).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonFitImage).setOnClickListener(btnListener);
        view.findViewById(R.id.button1_1).setOnClickListener(btnListener);
        view.findViewById(R.id.button3_4).setOnClickListener(btnListener);
        view.findViewById(R.id.button4_3).setOnClickListener(btnListener);
        view.findViewById(R.id.button9_16).setOnClickListener(btnListener);
        view.findViewById(R.id.button16_9).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonFree).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonRotateLeft).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonRotateRight).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonCustom).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonCircle).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonShowCircleButCropAsSquare).setOnClickListener(btnListener);
        view.findViewById(R.id.tv_back).setOnClickListener(btnListener);
        mRootLayout = (LinearLayout) view.findViewById(R.id.layout_root);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void pickImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), REQUEST_PICK_IMAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_SAF_PICK_IMAGE);
        }
    }


    public void cropImage() {
        Log.e("cropImage", "cropImage");
        showProgress();

        mCropView.startCrop(createSaveUri(), mCropCallback, mSaveCallback);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void showRationaleForPick(PermissionRequest request) {
        showRationaleDialog(R.string.permission_pick_rationale, request);
    }

    @SuppressLint("NoCorrespondingNeedsPermission")
    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void showRationaleForCrop(PermissionRequest request) {
        showRationaleDialog(R.string.permission_crop_rationale, request);
    }

    public void showProgress() {

        ProgressDialogFragment f = ProgressDialogFragment.getInstance();
        getFragmentManager()
                .beginTransaction()
                .add(f, PROGRESS_DIALOG)
                .commitAllowingStateLoss();
    }

    public void dismissProgress() {
        if (!isAdded()) return;
        FragmentManager manager = getFragmentManager();
        if (manager == null) return;
        ProgressDialogFragment f = (ProgressDialogFragment) manager.findFragmentByTag(PROGRESS_DIALOG);
        if (f != null) {
            getFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
        }
    }

    public Uri createSaveUri() {
        return Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    // Handle button event /////////////////////////////////////////////////////////////////////////
    private boolean checkAndRequestPermissions() {

        listPermissionsNeeded.clear();

        int storage = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readStorage = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (readStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }


        //Log ("TAG", "listPermissionsNeeded===>" + listPermissionsNeeded);
        if (!listPermissionsNeeded.isEmpty()) {
            return false;
        }

        return true;
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case STORAGE_PERMISSION_CODE:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        Log.e("TAG", "onRequestPermissionsResult: deny");

                    } else {
                        Log.e("TAG", "onRequestPermissionsResult: dont ask again");

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                        alertDialogBuilder.setTitle("Permissions Required")
                                .setMessage("Please allow permission for camera")
                                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", getActivity().getPackageName(), null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                .setCancelable(false)
                                .create()
                                .show();

                    }

                } else {

                    cropImage();
                }

                break;
        }

    }

    private final View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonDone:

                    if (checkAndRequestPermissions()) {


                        cropImage();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), STORAGE_PERMISSION_CODE);
                    }
                    //MainFragmentPermissionsDispatcher.cropImageWithCheck(MainFragment.this);
                    break;
                case R.id.buttonFitImage:
                    changecolor();
                    moView.findViewById(R.id.buttonFitImage).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCropMode(CropImageView.CropMode.FIT_IMAGE);
                    break;
                case R.id.button1_1:
                    changecolor();
                    moView.findViewById(R.id.button1_1).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCropMode(CropImageView.CropMode.SQUARE);
                    break;
                case R.id.button3_4:
                    changecolor();
                    moView.findViewById(R.id.button3_4).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_3_4);
                    break;
                case R.id.button4_3:
                    changecolor();
                    moView.findViewById(R.id.button4_3).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_4_3);
                    break;
                case R.id.button9_16:
                    changecolor();
                    moView.findViewById(R.id.button9_16).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_9_16);
                    break;
                case R.id.button16_9:
                    changecolor();
                    moView.findViewById(R.id.button16_9).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_16_9);
                    break;
                case R.id.buttonCustom:
                    changecolor();
                    moView.findViewById(R.id.buttonCustom).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCustomRatio(7, 5);
                    break;
                case R.id.buttonFree:
                    changecolor();
                    moView.findViewById(R.id.buttonFree).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCropMode(CropImageView.CropMode.FREE);
                    break;
                case R.id.buttonCircle:
                    changecolor();
                    moView.findViewById(R.id.buttonCircle).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCropMode(CropImageView.CropMode.CIRCLE);
                    break;
                case R.id.buttonShowCircleButCropAsSquare:
                    changecolor();
                    moView.findViewById(R.id.buttonShowCircleButCropAsSquare).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mCropView.setCropMode(CropImageView.CropMode.CIRCLE_SQUARE);
                    break;
                case R.id.buttonRotateLeft:
                    mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
                    break;
                case R.id.buttonRotateRight:
                    mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                    break;
                case R.id.tv_back:
                    Log.e("MainFragment", "--> tv_back");
                    ((CropImageActivity) getActivity()).finish();

                    break;
            }
        }

    };

    // Callbacks ///////////////////////////////////////////////////////////////////////////////////

    private final LoadCallback mLoadCallback = new LoadCallback() {

        @Override
        public void onSuccess() {
            dismissProgress();
        }

        @Override
        public void onError(Throwable e) {
            dismissProgress();
        }
    };

    private final CropCallback mCropCallback = new CropCallback() {
        @Override
        public void onSuccess(final Bitmap cropped) {
            Log.e("TAG", "mCropCallback:==>" + cropped);
            Log.e("mCropCallback", "mCropCallback");

            dismissProgress();

            final String cropfile = Share.saveFaceInternalStorage(getContext(),cropped);
            Log.e("cropfile", "onSuccess: cropimage "+cropfile );

            Share.croppedImage = cropfile;

            Log.e("cropfile", "onSuccess: Shrea.cropimage "+ Share.croppedImage );
            if (AlbumImagesActivity.activity != null) {
                AlbumImagesActivity.activity.finish();
            }
            if (AlbumActivity.activity != null) {
                AlbumActivity.activity.finish();
            }
            ((CropImageActivity) getActivity()).startResultActivity(cropped);


        }

        @Override
        public void onError(Throwable e) {
            dismissProgress();
        }
    };

    private final SaveCallback mSaveCallback = new SaveCallback() {
        @Override
        public void onSuccess(Uri outputUri) {
            Log.e("mSaveCallback", "mSaveCallback");
            dismissProgress();

        }

        @Override
        public void onError(Throwable e) {
            dismissProgress();
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        if (mCropView.getImageBitmap() == null) {

            if (Share.imageUrl != null && !Share.imageUrl.equals("")) {

                Log.e("TAG", "image uri1111:==>" + Share.imageUrl);
                Glide.with(this)
                        .load(Share.imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .override(300, 300)
                        .into(mCropView);
                dismissProgress();
            }else{
               // GlobalData.RestartifNull(getActivity());
            }
        }
    }

    private void changecolor() {

        moView.findViewById(R.id.buttonFitImage).setBackgroundColor(getResources().getColor(R.color.windowBackground));
        moView.findViewById(R.id.button1_1).setBackgroundColor(getResources().getColor(R.color.windowBackground));
        moView.findViewById(R.id.button3_4).setBackgroundColor(getResources().getColor(R.color.windowBackground));
        moView.findViewById(R.id.button4_3).setBackgroundColor(getResources().getColor(R.color.windowBackground));
        moView.findViewById(R.id.button9_16).setBackgroundColor(getResources().getColor(R.color.windowBackground));
        moView.findViewById(R.id.button16_9).setBackgroundColor(getResources().getColor(R.color.windowBackground));
        moView.findViewById(R.id.buttonFree).setBackgroundColor(getResources().getColor(R.color.windowBackground));
        moView.findViewById(R.id.buttonCustom).setBackgroundColor(getResources().getColor(R.color.windowBackground));
        moView.findViewById(R.id.buttonCircle).setBackgroundColor(getResources().getColor(R.color.windowBackground));
        moView.findViewById(R.id.buttonShowCircleButCropAsSquare).setBackgroundColor(getResources().getColor(R.color.windowBackground));
    }

    @Override
    public void onPause() {
        super.onPause();
        mCropView.setImageDrawable(null);
    }
}