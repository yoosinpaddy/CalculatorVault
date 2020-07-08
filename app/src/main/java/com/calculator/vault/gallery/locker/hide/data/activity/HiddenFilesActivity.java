package com.calculator.vault.gallery.locker.hide.data.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calculator.vault.gallery.locker.hide.data.BuildConfig;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.UFileUtils;
import com.calculator.vault.gallery.locker.hide.data.adapter.FilesAdapter;
import com.calculator.vault.gallery.locker.hide.data.ads.AdsListener;
import com.calculator.vault.gallery.locker.hide.data.ads.IntAdsHelper;
import com.calculator.vault.gallery.locker.hide.data.ads.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.common.RatingDialog;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.common.Utils;
import com.calculator.vault.gallery.locker.hide.data.commonCode.utils.GlobalData;
import com.calculator.vault.gallery.locker.hide.data.customs.DragSelectTouchListener;
import com.calculator.vault.gallery.locker.hide.data.customs.DragSelectionProcessor;
import com.calculator.vault.gallery.locker.hide.data.data.DecoyDatabase;
import com.calculator.vault.gallery.locker.hide.data.data.ImageVideoDatabase;
import com.calculator.vault.gallery.locker.hide.data.model.itemModel;
import com.google.android.gms.ads.AdView;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static com.calculator.vault.gallery.locker.hide.data.common.Share.msPathToWrite;
import static com.calculator.vault.gallery.locker.hide.data.common.Share.msPathToWriteDecoy;

public class HiddenFilesActivity extends AppCompatActivity implements View.OnClickListener
        /*MoPubInterstitial.InterstitialAdListener*//*InterstitialAdHelper.onInterstitialAdListener*/ {

    private static final String TAG = "HiddenFilesActivity";
    private File[] allFiles;
    private RecyclerView moRvHiddenPics;
    private LinearLayoutManager moLinearLayoutManager;
    private LinearLayout moLlAddFiles, moLLeditControl, moLLDelete, moLLShare, moLLsave;
    private RelativeLayout moLLNoPhotos;
    private ImageView moIvAddPhoto;
    private static final int REQUEST_CODE = 732;
    private static final int SELECT_FILES = 733;
    private TextView moTvTitle;
    private ArrayList<String> moResultsArrayList = new ArrayList<>();
    private ArrayList<itemModel> moItemFileList = new ArrayList<>();
    private RelativeLayout moRlShareDeleteSave;
    private ImageView moTvEditControl;
    private TextView jTvCancel;
    private static final String msDATABASE_NAME = "ImageVideoDatabase";
    private ImageVideoDatabase moImageVideoDatabase = new ImageVideoDatabase(this);
    private ImageView iv_back;
    private String msDatabaseWritepath = Environment.getExternalStorageDirectory().getPath() + File.separator + ".androidData" + File.separator + ".log" + File.separator + ".check" + File.separator;
    private Dialog dialog;
    private DecoyDatabase decoyDatabase = new DecoyDatabase(this);
    private String isDecode;
    private AdView adView;
    private BroadcastReceiver mReceiver;
    int miItemCount = 0;
    private boolean isStorageFull = false;
    private boolean isAdLoad = true;
    private boolean isResume = false;
    private boolean isFromNewFiles = false;

    //Multi Select Adapter
    private DragSelectionProcessor.Mode mMode = DragSelectionProcessor.Mode.FirstItemDependent;
    private DragSelectTouchListener mDragSelectTouchListener;
    private DragSelectionProcessor mDragSelectionProcessor;
    private FilesAdapter moFilesAdapter;
    private List<Uri> moUriList = new ArrayList<>();

//    private boolean isInterstitialAdLoaded = false;
//    private MoPubInterstitial mInterstitial;
//    TODO Admob Ads
//    private InterstitialAd interstitial;

//    @Override
//    public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {
//        isInterstitialAdLoaded = true;
//    }
//
//    @Override
//    public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {
//        Log.e(TAG, "onInterstitialFailed: "+ moPubErrorCode);
//        isInterstitialAdLoaded = false;
//        if (Utils.isNetworkConnected(HiddenFilesActivity.this)) {
//            mInterstitial.load();
//        }
//    }
//
//    @Override
//    public void onInterstitialShown(MoPubInterstitial moPubInterstitial) {
//
//    }
//
//    @Override
//    public void onInterstitialClicked(MoPubInterstitial moPubInterstitial) {
//
//    }
//
//    @Override
//    public void onInterstitialDismissed(MoPubInterstitial moPubInterstitial) {
//        isInterstitialAdLoaded = false;
//        if (Utils.isNetworkConnected(HiddenFilesActivity.this)) {
//            mInterstitial.load();
//        }
//        if (isResume) {
//            new HiddenFilesActivity.loadFiles().execute();
//        } else {
//            getFiles();
//        }
//    }

//    TODO Admob Ads
//    @Override
//    public void onLoad() {
//        isInterstitialAdLoaded = true;
//    }
//
//    @Override
//    public void onFailed() {
//        isInterstitialAdLoaded = false;
//        if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id1))){
//            interstitial = InterstitialAdHelper.getInstance().load2(HiddenFilesActivity.this, this);
//        }else if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id2))){
//            interstitial = InterstitialAdHelper.getInstance().load(HiddenFilesActivity.this, this);
//        }else {
//            interstitial = InterstitialAdHelper.getInstance().load1(HiddenFilesActivity.this, this);
//        }
//    }
//
//    @Override
//    public void onClosed() {
//        isInterstitialAdLoaded = false;
//        interstitial = InterstitialAdHelper.getInstance().load1(HiddenFilesActivity.this, this);
//        if (isResume) {
//            new HiddenFilesActivity.loadFiles().execute();
//        } else {
//            getFiles();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_files);

//        mInterstitial = new MoPubInterstitial(this, getString(R.string.mopub_int_id));
//        mInterstitial.setInterstitialAdListener(this);
//        if (Utils.isNetworkConnected(HiddenFilesActivity.this)) {
//            mInterstitial.load();
        IntAdsHelper.loadInterstitialAds(HiddenFilesActivity.this, new AdsListener() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onClosed() {
                if (isResume) {
                    new HiddenFilesActivity.loadFiles().execute();
                } else {
                    getFiles();
                    if (isDecode.equals("false")) {
                        if (!SharedPrefs.getBoolean(HiddenFilesActivity.this, Share.IS_RATED)) {
                            if (SharedPrefs.getInt(HiddenFilesActivity.this, Share.RATE_APP_HIDE_COUNT) > 2) {
                                RatingDialog.SmileyRatingDialog(HiddenFilesActivity.this);
                            }
                        }
                    }
                }
            }
        });
//        }

//        TODO Admob Ads
//        interstitial = InterstitialAdHelper.getInstance().load1(HiddenFilesActivity.this, this);

//        IronSource.init(this, "8fb1d745", IronSource.AD_UNIT.INTERSTITIAL);
//        IronSource.shouldTrackNetworkState(this, true);
//        IronSource.setInterstitialListener(this);
//        IronSource.loadInterstitial();

        File path = new File(msDatabaseWritepath);
        if (!path.exists())
            path.mkdirs();
        File loFile = new File(msDatabaseWritepath + msDATABASE_NAME + ".db");
        if (!loFile.exists()) {
            try {
                loFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        dialog = Share.showProgress1(HiddenFilesActivity.this, "Loading...");
        Share.isclickedOnDone = false;

        Share.selected_image_list.clear();
        Log.e("TAG:: ", "onCreate: ");
        initView();
        isDecode = SharedPrefs.getString(this, SharedPrefs.DecoyPassword, "false");

        initViewListener();
        initViewAction();

        adView = (AdView) findViewById(R.id.adView);
        Share.loadAdsBanner(HiddenFilesActivity.this, adView);
        // IronSource Banner Ads
        //ISAdsHelper.loadBannerAd(this, (FrameLayout) findViewById(R.id.flBanner));

    }

    private void getFiles() {
        runOnUiThread(() -> {

            File path;
            if (isDecode.equals("true")) {
                path = new File(msPathToWriteDecoy);
            } else {
                path = new File(msPathToWrite);
            }

            moItemFileList.clear();
            //Share.al_my_photos_photo.clear();
            allFiles = null;

            if (path.exists()) {

                Log.e("if1", "if1");
                allFiles = path.listFiles((dir, name) -> (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")));

                Log.e("array_size", "" + allFiles.length);

                moItemFileList.clear();
                if (isDecode.equals("true")) {
                    moItemFileList = decoyDatabase.getAllFilesData(msPathToWriteDecoy);
                    Log.e("HiddenImagesActivity", "getPhotos: DECOY_LIST_SIZE " + moItemFileList.size());
                } else {
                    moItemFileList = moImageVideoDatabase.getAllFilesData(Share.msPathToWrite);
                }

                if (moItemFileList.isEmpty()) {
                    moLLeditControl.setVisibility(View.GONE);
                    moLLNoPhotos.setVisibility(View.VISIBLE);
                    moRlShareDeleteSave.setVisibility(View.GONE);
                    jTvCancel.setText(getString(R.string.Edit));
                } else {
                    moLLeditControl.setVisibility(View.VISIBLE);
                    moLLNoPhotos.setVisibility(View.GONE);
                }
                moFilesAdapter = new FilesAdapter(HiddenFilesActivity.this, moItemFileList);
                moRvHiddenPics.setLayoutManager(moLinearLayoutManager);
                moRvHiddenPics.setAdapter(moFilesAdapter);
                multiSelect();
            }
        });
    }

    private void initView() {

        moIvAddPhoto = findViewById(R.id.iv_add_photos);
        moRvHiddenPics = findViewById(R.id.rv_hiddenpics);
        moLinearLayoutManager = new LinearLayoutManager(HiddenFilesActivity.this, RecyclerView.VERTICAL, false);
        moLlAddFiles = findViewById(R.id.ll_add_files);
        moLLNoPhotos = findViewById(R.id.ll_noPhotos);
        moTvTitle = findViewById(R.id.tv_title);
        moRlShareDeleteSave = findViewById(R.id.rl_ShareDeleteSave);
        moRlShareDeleteSave.setVisibility(View.GONE);
        moLLeditControl = findViewById(R.id.ll_editcontrol);
        moTvEditControl = findViewById(R.id.tv_editcontrol);
        jTvCancel = findViewById(R.id.jTvCancel);
        moLLDelete = findViewById(R.id.ll_Delete);
        moLLShare = findViewById(R.id.ll_Share);
        moLLsave = findViewById(R.id.ll_Save);
        iv_back = findViewById(R.id.iv_back);
    }

    private void initViewListener() {
        moIvAddPhoto.setOnClickListener(this);
        moLlAddFiles.setOnClickListener(this);
        moLLeditControl.setOnClickListener(this);
        moLLDelete.setOnClickListener(this);
        moLLShare.setOnClickListener(this);
        moLLsave.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void initViewAction() {
        moTvTitle.setText(getString(R.string.files));
        //getPhotos();
        loadInterAdAndGetFiles("init");
    }

    private void loadInterAdAndGetFiles(String fWhere) {

        if (fWhere.equalsIgnoreCase("init")) {
            getFiles();
        } else {
            if (Share.isNeedToAdShow(this)) {
                if (IntAdsHelper.isInterstitialLoaded()) {
                    isFromNewFiles = true;
                    IntAdsHelper.showInterstitialAd();
//                    TODO Admob Ads
//                    interstitial.show();
                } else {
                    isFromNewFiles = true;
                    getFiles();
                    if (isDecode.equals("false")) {
                        if (!SharedPrefs.getBoolean(HiddenFilesActivity.this, Share.IS_RATED)) {
                            if (SharedPrefs.getInt(HiddenFilesActivity.this, Share.RATE_APP_HIDE_COUNT) > 2) {
                                RatingDialog.SmileyRatingDialog(HiddenFilesActivity.this);
                            }
                        }
                    }
                }
            } else {
                isFromNewFiles = true;
                getFiles();
                if (isDecode.equals("false")) {
                    if (!SharedPrefs.getBoolean(HiddenFilesActivity.this, Share.IS_RATED)) {
                        if (SharedPrefs.getInt(HiddenFilesActivity.this, Share.RATE_APP_HIDE_COUNT) > 2) {
                            RatingDialog.SmileyRatingDialog(HiddenFilesActivity.this);
                        }
                    }
                }
            }
        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    private void multiSelect() {
        moFilesAdapter.setClickListener(new FilesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (moRlShareDeleteSave.getVisibility() == View.GONE) {

                    File loNewFile = new File(moItemFileList.get(position).getNewFilepath() + moItemFileList.get(position).getNewFilename());
                    openFile(loNewFile);
                } else {
                    moFilesAdapter.toggleSelection(position);
                }
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
                // if one item is long pressed, we start the drag selection like following:
                // we just call this function and pass in the position of the first selected item
                // the selection processor does take care to update the positions selection mode correctly
                // and will correctly transform the touch events so that they can be directly applied to your adapter!!!
                mDragSelectTouchListener.startDragSelection(position);
                return true;
            }
        });

        // 2) Add the DragSelectListener
        mDragSelectionProcessor = new DragSelectionProcessor(new DragSelectionProcessor.ISelectionHandler() {
            @Override
            public HashSet<Integer> getSelection() {
                return moFilesAdapter.getSelection();
            }

            @Override
            public boolean isSelected(int index) {
                return moFilesAdapter.getSelection().contains(index);
            }

            @Override
            public void updateSelection(int start, int end, boolean isSelected, boolean calledFromOnStart) {
                moFilesAdapter.selectRange(start, end, isSelected);
            }
        }).withMode(mMode);

        mDragSelectTouchListener = new DragSelectTouchListener().withSelectListener(mDragSelectionProcessor);

        moRvHiddenPics.addOnItemTouchListener(mDragSelectTouchListener);
    }

    private void openFile(File url) {

        String FilePath = String.valueOf(url);
        FilePath.replace(" ", "");
        try {

            Uri uri = Uri.fromFile(url);

            Log.e("FilePath", "openFile: " + FilePath);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else if (url.toString().contains(".apk")) {
                // Video files
                Uri uri1 = FileProvider.getUriForFile(HiddenFilesActivity.this, BuildConfig.APPLICATION_ID + ".provider", url);
                intent.setData(uri1);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(HiddenFilesActivity.this, "No application found which can open the file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_add_files:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILES);

                //addPhotosFromGallery();
                break;
            case R.id.iv_add_photos:
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setType("*/*");
                intent1.addCategory(Intent.CATEGORY_OPENABLE);
                intent1.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent1.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(Intent.createChooser(intent1, "Select File"), SELECT_FILES);
                break;
            case R.id.ll_editcontrol:
                if (moRlShareDeleteSave.getVisibility() == View.GONE) {
                    jTvCancel.setText(getString(R.string.Cancel));
                    moRlShareDeleteSave.setVisibility(View.VISIBLE);
                } else {
                    jTvCancel.setText(getString(R.string.Edit));
                    moRlShareDeleteSave.setVisibility(View.GONE);
                    moItemFileList.clear();
                    if (isDecode.equals("true")) {
                        moItemFileList = decoyDatabase.getAllFilesData(msPathToWriteDecoy);
                    } else {
                        moItemFileList = moImageVideoDatabase.getAllFilesData(Share.msPathToWrite);
                    }
                    moFilesAdapter = new FilesAdapter(HiddenFilesActivity.this, moItemFileList);
                    moRvHiddenPics.setAdapter(moFilesAdapter);
                    multiSelect();
                }
                break;
            case R.id.ll_Delete:
                Log.e("DELETE", "onClick: SIZE " + moItemFileList.size());
                if (moFilesAdapter.getCountSelected() > 0) {
                    alertdialog();
                } else {
                    Toast.makeText(this, "Select the file you want to delete.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ll_Share:
                shareFiles();
                break;
            case R.id.ll_Save:
                if (moFilesAdapter.getCountSelected() > 0) {
                    new saveFiles().execute();
                } else {
                    Toast.makeText(this, "Select the file you want to save.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private void alertdialog() {
        final Dialog dialog1 = new Dialog(HiddenFilesActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.setContentView(R.layout.alert_decoy_passcode);
        TextView mess = dialog1.findViewById(R.id.tv_message1);
        mess.setText(getString(R.string.delete_image_message));
        Button yesbtn = dialog1.findViewById(R.id.btn_dialogOK1);
        Button nobtn = dialog1.findViewById(R.id.btn_dialogNo1);

        yesbtn.setOnClickListener(v -> {
            try {
                dialog1.dismiss();
                new deleteFiles().execute();
            } catch (Exception e) {
                dialog1.dismiss();
                e.printStackTrace();
            }
        });

        nobtn.setOnClickListener(v -> dialog1.dismiss());

        if (dialog1 != null && !dialog1.isShowing()) {
            dialog1.show();
        }
    }

    private class saveFiles extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
            isStorageFull = false;
        }

        @Override
        protected Void doInBackground(String... strings) {
            saveFiles();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent i = new Intent("android.intent.action.IMAGES").putExtra("some_msg", "2");
            HiddenFilesActivity.this.sendBroadcast(i);
        }
    }

    private void saveFiles() {

        for (int i = 0; i < moItemFileList.size(); i++) {
            if (moItemFileList.get(i).isSelected() && !isStorageFull) {
                miItemCount++;
                Log.e("TAG", "deletePhotosFromGallery: " + moItemFileList.get(i).getNewFilename());
                itemModel model;

                if (isDecode.equals("true")) {
                    model = decoyDatabase.getSingleFileData(moItemFileList.get(i).getNewFilename());
                } else {
                    model = moImageVideoDatabase.getSingleFileData(moItemFileList.get(i).getNewFilename());
                }

                File loTempFile = new File(model.getNewFilepath() + model.getNewFilename());
                File loFile1 = new File(loTempFile.getAbsolutePath());
                int liFileSize = Integer.parseInt(String.valueOf(loFile1.length() / 1024));

                Log.e("FILE_SIZE", "FILE SPACE --> " + liFileSize);
                Log.e("FILE_SIZE", "FREE SPACE --> " + GlobalData.kilobytesAvailable(loFile1));

                if (GlobalData.kilobytesAvailable(loFile1) >= liFileSize) {
                    File from = new File(model.getOldFilepath() + model.getNewFilename());
                    //File to = new File(model.getOldFilepath() + model.getOldFilename());
                    //from.renameTo(to);

                    String oldpath = model.getOldFilepath();
                    String newpath = model.getNewFilepath();

                    copyPhotoTo(newpath + model.getNewFilename(), oldpath, model.getOldFilename());

                    model.setStatus("1");
                    if (isDecode.equals("true")) {
                        decoyDatabase.updateFileSingleData(model);
                    } else {
                        moImageVideoDatabase.updateFileSingleData(model);
                    }

                    Uri contentUri = Uri.fromFile(from);
                    Log.e("ATGA", "savePhotosFromGallery: rename file name::: " + from.getName());
                    Log.e("ATGA", "savePhotosFromGallery: rename file path::: " + from.getAbsolutePath());

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(contentUri);
                    sendBroadcast(mediaScanIntent);

                    MediaScannerConnection.scanFile(HiddenFilesActivity.this, new String[]{from.getAbsolutePath()}, null, (s, uri) -> Log.e("TAG", "onScanCompleted: "));
                } else {
                    isStorageFull = true;
                }
            }
        }
        moItemFileList.clear();
        if (isDecode.equals("true")) {
            moItemFileList = decoyDatabase.getAllFilesData(msPathToWriteDecoy);
        } else {
            moItemFileList = moImageVideoDatabase.getAllFilesData(Share.msPathToWrite);
        }
    }

    private void shareFiles() {
        ArrayList<Integer> positions = new ArrayList<>();
        for (itemModel image : moItemFileList) {
            if (image.isSelected()) {
                positions.add(moItemFileList.indexOf(image));
            }
        }
        if (!positions.isEmpty()) {
            shareFilesIntent(positions);
        } else {
            Toast.makeText(this, "Select the file you want to share.", Toast.LENGTH_LONG).show();
        }
    }

    private class deleteFiles extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
            isStorageFull = false;
        }

        @Override
        protected Void doInBackground(String... strings) {
            deleteFilesMethod();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent i = new Intent("android.intent.action.IMAGES").putExtra("some_msg", "2");
            HiddenFilesActivity.this.sendBroadcast(i);
        }
    }

    private void deleteFilesMethod() {
        for (int i = 0; i < moItemFileList.size(); i++) {
            if (moItemFileList.get(i).isSelected() && !isStorageFull) {
                miItemCount++;
                Log.e("TAG", "deletePhotosFromGallery: " + moItemFileList.get(i).getNewFilename());

                itemModel model;

                if (isDecode.equals("true")) {
                    model = decoyDatabase.getSingleFileData(moItemFileList.get(i).getNewFilename());
                } else {
                    model = moImageVideoDatabase.getSingleFileData(moItemFileList.get(i).getNewFilename());
                }

                File loTempFile = new File(model.getNewFilepath() + model.getNewFilename());
                File loFile1 = new File(loTempFile.getAbsolutePath());
                int liFileSize = Integer.parseInt(String.valueOf(loFile1.length() / 1024));
                Log.e("FILE_SIZE", "FILE SPACE --> " + liFileSize);
                Log.e("FILE_SIZE", "FREE SPACE --> " + GlobalData.kilobytesAvailable(loFile1));

                if (GlobalData.kilobytesAvailable(loFile1) >= liFileSize) {
                    File from = new File(model.getNewFilepath() + model.getNewFilename());
                    File to = new File(model.getNewFilepath() + model.getOldFilename());
                    from.renameTo(to);
                    Log.e("TAG:: ", "onActivityResult: " + to.getName());
                    String path = model.getOldFilepath();
                    copyPhotoTo(to.getAbsolutePath(), path, to.getName());
                    File file = new File(model.getOldFilepath() + model.getOldFilename());
                    Uri contentUri = Uri.fromFile(file);

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(contentUri);
                    sendBroadcast(mediaScanIntent);

                    MediaScannerConnection.scanFile(HiddenFilesActivity.this, new String[]{file.getAbsolutePath()}, null, (s, uri) -> Log.e("TAG", "onScanCompleted: "));
                    to.delete();
                    if (isDecode.equals("true")) {
                        decoyDatabase.removeSingleFileData(model.getNewFilename());
                    } else {
                        moImageVideoDatabase.removeSingleFileData(model.getNewFilename());
                    }
                } else {
                    isStorageFull = true;
                }
            }
        }
        moItemFileList.clear();

        if (isDecode.equals("true")) {
            moItemFileList = decoyDatabase.getAllFilesData(msPathToWriteDecoy);
        } else {
            moItemFileList = moImageVideoDatabase.getAllFilesData(Share.msPathToWrite);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        Log.e("HiddenImagesActivity", "onActivityResult: REQ_CODE " + requestCode);
        if (requestCode == SELECT_FILES) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getClipData() != null) {


                    Log.e("GotFiles", "Before: If");

                    int count = data.getClipData().getItemCount();
                    int currentItem = 0;
                    while (currentItem < count) {
                        Uri imageUri = data.getClipData().getItemAt(currentItem).getUri();
                        Log.e("GotFiles", "Before: " + imageUri);
                        if (!imageUri.equals(null)) {
                            moUriList.add(imageUri);
                            //Log.e("GotFiles", "getClipData: " + FileUtilsNew.getPath(HiddenFilesActivity.this, imageUri));
                            Log.e("GotFiles", "onActivityResult: " + imageUri);
                        } else {
                            Toast.makeText(this, "File is not available in storage.", Toast.LENGTH_SHORT).show();
                        }
                        currentItem++;
                    }
                    findViewById(R.id.fl_adplaceholder).setVisibility(View.GONE);
                    new addNewFiles().execute();
                } else if (data.getData() != null) {

                    Log.e("GotFiles", "Before: Else");

                    if (!data.getData().equals(null)) {
                        Log.e("GotFiles", "Before: " + data.getData());


                        String path = UFileUtils.getPath(this, data.getData());

                        Log.e("GotFiles", "Before11: " + path);
//                        Log.e("GotFiles", "getData: " + FileUtilsNew.getPath(HiddenFilesActivity.this, data.getData()));

                        Uri uri = Uri.parse(path);
                        if (uri != null) {
                            moUriList.add(uri);
                            findViewById(R.id.fl_adplaceholder).setVisibility(View.GONE);
                            new addNewFiles().execute();
                        } else {
                            Toast.makeText(this, "Something went wrong, Please try again...", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(this, "File is not available in storage.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class addNewFiles extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            for (int i = 0; i < moUriList.size(); i++) {

                Uri foUri = moUriList.get(i);

                File loTempFile;
//                String uriPath = foUri.toString().replace("primary:","");
//                if (foUri.getPath().contains("primary:")){
//                    foUri = new Uri(uriPath);
//                }else {
//
//                }

                try {
                    //String lsFilePath = foUri.getPath();
//                    if (lsFilePath.contains("primary:")){
//                        lsFilePath = lsFilePath.replace("primary:","");
//                    }
//                    if (foUri.getPath().endsWith(".apk") || foUri.getPath().endsWith(".zip")) {
//                        loTempFile = new File(lsFilePath);
//                    } else {
                    loTempFile = new File(Utils.getPathFromUri(HiddenFilesActivity.this, foUri));
//                    }
                } catch (Exception ex) {
                    String lsFilePath = foUri.getPath();
                    if (lsFilePath.contains("primary:")) {
                        lsFilePath = lsFilePath.replace("primary:", "");
                    }
                    loTempFile = new File(lsFilePath);
                }


                String lsFileName = loTempFile.getName();
                String lsFilePath = loTempFile.getParent() + "/";
                String timeStamp = new SimpleDateFormat("yyyyMMdd_hhmmssSSS").format(new Date());

                Log.e("GotFiles", "File Name: " + lsFileName);
                Log.e("GotFiles", "File Parent: " + lsFilePath);

                File loFileFrom = new File(loTempFile.getParent(), loTempFile.getName());
                //File loFileTo = new File(loTempFile.getParent(), "file" + timeStamp + "." + "zxcv");

                //loFileFrom.renameTo(loFileTo);

                if (isDecode.equals("true")) {
                    copyPhotoTo(lsFilePath + lsFileName, msPathToWriteDecoy, loFileFrom.getName());
                } else {
                    copyPhotoTo(lsFilePath + lsFileName, Share.msPathToWrite, loFileFrom.getName());
                }

                itemModel loItemModel;

                if (isDecode.equals("true")) {
                    loItemModel = decoyDatabase.getSingleFileData(lsFileName);
                } else {
                    loItemModel = moImageVideoDatabase.getSingleFileData(lsFileName);
                }

                String lsStatus = loItemModel.getStatus();
                loItemModel.setStatus("0");
                loItemModel.setNewFilename(loFileFrom.getName());

                if (lsStatus != null && lsStatus.equals("1")) {
                    if (isDecode.equals("true")) {
                        decoyDatabase.updateFileSingleData(loItemModel);
                    } else {
                        moImageVideoDatabase.updateFileSingleData(loItemModel);
                    }
                } else {
                    if (isDecode.equals("true")) {
                        decoyDatabase.addFileData(new itemModel("file", lsFileName, loFileFrom.getName(), loTempFile.getParent() + "/", msPathToWriteDecoy, "0"));
                    } else {
                        moImageVideoDatabase.addFileData(new itemModel("file", lsFileName, loFileFrom.getName(), loTempFile.getParent() + "/", Share.msPathToWrite, "0"));
                    }
                }

                loTempFile.delete();

                Uri contentUri = Uri.fromFile(loTempFile);

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(contentUri);
                sendBroadcast(mediaScanIntent);

                MediaScannerConnection.scanFile(HiddenFilesActivity.this, new String[]{loTempFile.getAbsolutePath()}, null, (s1, uri) -> Log.e("TAG", "onScanCompleted: "));

            }

            if (isDecode.equals("false")) {
                if (!moUriList.isEmpty()) {
                    SharedPrefs.save(HiddenFilesActivity.this, Share.RATE_APP_HIDE_COUNT, SharedPrefs.getInt(HiddenFilesActivity.this, Share.RATE_APP_HIDE_COUNT) + 1);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            moUriList.clear();
            dialog.dismiss();
            showNative();
            loadInterAdAndGetFiles("activityResult7001");
        }
    }

    public void copyPhotoTo(String pathToWatch, String pathToWrite, String str) {
        Log.e("copyPhotoTo: ", "Source: " + pathToWatch);
        Log.e("copyPhotoTo: ", "Desti: " + pathToWrite + str);
        File path = new File(pathToWrite);
        if (!path.exists())
            path.mkdirs();
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        //IronSource.onResume(this);
        isDecode = SharedPrefs.getString(this, SharedPrefs.DecoyPassword, "false");
        moItemFileList.clear();
        if (isDecode.equals("true")) {
            moItemFileList = decoyDatabase.getAllFilesData(msPathToWriteDecoy);
            Log.e("TAG:: ", "onResume: " + "Decoy::: " + moItemFileList.size());
        } else {
            moItemFileList = moImageVideoDatabase.getAllFilesData(Share.msPathToWrite);
            Log.e("TAG:: ", "onResume: " + "Actual::: " + moItemFileList.size());
        }

        if (moItemFileList.isEmpty()) {
            showNative();
            moLLeditControl.setVisibility(View.GONE);
            moLLNoPhotos.setVisibility(View.VISIBLE);
            moRlShareDeleteSave.setVisibility(View.GONE);
            jTvCancel.setText(getString(R.string.Edit));
        } else {
            moLLeditControl.setVisibility(View.VISIBLE);
            moLLNoPhotos.setVisibility(View.GONE);
        }
        if (moItemFileList != null && moItemFileList.size() != 0) {
            moFilesAdapter.onUpdate(moItemFileList);
        }
        if (Share.isclickedOnDone) {
            if (Share.isNeedToAdShow(this)) {
                if (IntAdsHelper.isInterstitialLoaded()) {
                    isResume = true;
                    IntAdsHelper.showInterstitialAd();
//                    TODO Admob Ads
//                    interstitial.show();
                } else {
                    new loadFiles().execute();
                }
            } else {
                new loadFiles().execute();
            }
        }

        IntentFilter intentFilter = new IntentFilter("android.intent.action.IMAGES");
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
                String msg_for_me = intent.getStringExtra("some_msg");
                //log our message valu
                if (msg_for_me.equals("2")) {
                    new CountDownTimer(1500, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            if (miItemCount != 0) {
                                if (moItemFileList.isEmpty()) {
                                    moLLeditControl.setVisibility(View.GONE);
                                    moLLNoPhotos.setVisibility(View.VISIBLE);
                                    moRlShareDeleteSave.setVisibility(View.GONE);
                                    jTvCancel.setText(getString(R.string.Edit));
                                    findViewById(R.id.fl_adplaceholder).setVisibility(View.VISIBLE);
                                } else {
                                    moLLeditControl.setVisibility(View.VISIBLE);
                                    moLLNoPhotos.setVisibility(View.GONE);
                                }
                                moFilesAdapter = new FilesAdapter(HiddenFilesActivity.this, moItemFileList);
                                moRvHiddenPics.setAdapter(moFilesAdapter);
                                multiSelect();
                            } else {
                                Toast.makeText(HiddenFilesActivity.this, "Please select a file first.", Toast.LENGTH_SHORT).show();
                            }
                            miItemCount = 0;
                            dialog.dismiss();
                            if (isStorageFull) {
                                Toast.makeText(HiddenFilesActivity.this, "Your Storage is full.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.start();
                }
            }
        };
        this.registerReceiver(mReceiver, intentFilter);

        Share.isclickedOnDone = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NativeAdvanceHelper.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        if (mReceiver != null) {
            this.unregisterReceiver(this.mReceiver);
        }
        super.onPause();
        //IronSource.onPause(this);
        //unregister our receiver
    }

    private void callSelectedPhotos() {
        moResultsArrayList = Share.selected_image_list;
        assert moResultsArrayList != null;

        Log.e("TAG:: ", "callSelectedPhotos: " + "moResultsArrayList size:: " + moResultsArrayList.size());
        int i = 0;
        for (String result : moResultsArrayList) {
            File file = new File(result);

            Log.e("TAG", "onActivityResult: File Path " + result);
            Log.e("TAG", "onActivityResult: File name :: " + file.getName());
            Log.e("TAG", "onActivityResult: File abs path :: " + file.getParent());

            String filename1 = file.getName();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_hhmmssSSS").format(new Date());

            File from = new File(file.getParent(), filename1);

            String fileNameExt = FilenameUtils.getExtension(filename1);
            //File to = new File(file.getParent(), "file" + timeStamp + "." + "zxcv");
            //from.renameTo(to);
            Log.e("TAG:: ", "onActivityResult: " + from.getName());
            if (isDecode.equals("true")) {
                copyPhotoTo(from.getAbsolutePath(), msPathToWriteDecoy, from.getName());
            } else {
                copyPhotoTo(from.getAbsolutePath(), Share.msPathToWrite, from.getName());
            }

            itemModel oldSingleData = null;
            String s = null;
            try {

                if (isDecode.equals("true")) {
                    oldSingleData = decoyDatabase.getOldSingleData(filename1);
                } else {
                    oldSingleData = moImageVideoDatabase.getOldSingleData(filename1);
                }
                s = oldSingleData.getStatus();
                oldSingleData.setStatus("0");
                oldSingleData.setNewFilename(from.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (s != null && s.equals("1")) {
                Log.e("TAG:: ", "callSelectedPhotos: " + " oldsingledata present:: ");
                if (isDecode.equals("true")) {
                    decoyDatabase.updateSingleData(oldSingleData);
                } else {
                    moImageVideoDatabase.updateSingleData(oldSingleData);
                }

            } else {
                Log.e("TAG:: ", "callSelectedPhotos: " + "Add Data:: ");
                if (isDecode.equals("true")) {
                    decoyDatabase.addFileData(new itemModel("image", filename1, from.getName(), file.getParent() + "/", msPathToWriteDecoy, "0"));
                } else {
                    moImageVideoDatabase.addFileData(new itemModel("image", filename1, from.getName(), file.getParent() + "/", Share.msPathToWrite, "0"));
                }
            }
            Log.e("TAG", "onActivityResult: " + " Old NAme:: " + filename1);
            Log.e("TAG", "onActivityResult: " + " New NAme:: " + from.getName());
            Log.e("TAG", "onActivityResult: " + " Old Path:: " + file.getParent());
            Log.e("TAG", "onActivityResult: " + " New Path:: " + from.getParent());

            from.delete();

            Uri contentUri = Uri.fromFile(file);

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);

            MediaScannerConnection.scanFile(HiddenFilesActivity.this, new String[]{file.getAbsolutePath()}, null, (s1, uri) -> Log.e("TAG", "onScanCompleted: "));
        }
    }

    public class loadFiles extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e("TAG:: ", "doInBackground: " + "Share.selected_image_list:: " + Share.selected_image_list.size());
            callSelectedPhotos();
            Share.selected_image_list.clear();
            moItemFileList.clear();
            if (isDecode.equals("true")) {
                moItemFileList = decoyDatabase.getAllFilesData(msPathToWriteDecoy);
            } else {
                moItemFileList = moImageVideoDatabase.getAllFilesData(Share.msPathToWrite);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (moItemFileList.isEmpty()) {
                moLLeditControl.setVisibility(View.GONE);
                moLLNoPhotos.setVisibility(View.VISIBLE);
                moRlShareDeleteSave.setVisibility(View.GONE);
                jTvCancel.setText(getString(R.string.Edit));
            } else {
                moLLeditControl.setVisibility(View.VISIBLE);
                moLLNoPhotos.setVisibility(View.GONE);
            }
            moFilesAdapter = new FilesAdapter(HiddenFilesActivity.this, moItemFileList);
            moRvHiddenPics.setLayoutManager(moLinearLayoutManager);
            moRvHiddenPics.setAdapter(moFilesAdapter);
            multiSelect();
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void shareFilesIntent(ArrayList<Integer> positions) {
        ArrayList<Uri> imageList = new ArrayList<>();
        for (int position : positions) {

            File file = new File(moItemFileList.get(position).getNewFilepath() + moItemFileList.get(position).getNewFilename());
            copyPhotoTo(file.getAbsolutePath(), "/storage/emulated/0/.androidData/.log/.dup/", moItemFileList.get(position).getOldFilename());
            imageList.add(FileProvider.getUriForFile(this, getPackageName() + ".provider",
                    new File("/storage/emulated/0/.androidData/.log/.dup/" + moItemFileList.get(position).getOldFilename())));
            Log.e("HiddenImagesActivity", "shareFiles: SHARE " + moItemFileList.get(position).getOldFilepath() +
                    "|||" + moItemFileList.get(position).getOldFilename() + "|||"
                    + FileProvider.getUriForFile(this, getPackageName()
                    + ".provider", new File("/storage/emulated/0/.androidData/.log/.dup/"
                    + moItemFileList.get(position).getOldFilename())).toString());
        }
        Intent Shareintent = new Intent()
                .setAction(Intent.ACTION_SEND_MULTIPLE)
                .setType("image/*")
                .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageList);
        this.startActivity(Intent.createChooser(Shareintent, this.getString(R.string.share_intent_images)));
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void showNative() {
        if (moItemFileList.isEmpty()) {
            if (Share.isNeedToAdShow(HiddenFilesActivity.this)) {
                NativeAdvanceHelper.loadNativeAd(HiddenFilesActivity.this, findViewById(R.id.fl_adplaceholder), false);
            }
        } else {
            findViewById(R.id.fl_adplaceholder).setVisibility(View.GONE);
        }
    }
}

