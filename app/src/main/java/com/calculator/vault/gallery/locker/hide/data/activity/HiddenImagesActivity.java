package com.calculator.vault.gallery.locker.hide.data.activity;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.adapter.ImagesMultiSelectAdapter;
import com.calculator.vault.gallery.locker.hide.data.ads.AdsListener;
import com.calculator.vault.gallery.locker.hide.data.ads.IntAdsHelper;
import com.calculator.vault.gallery.locker.hide.data.ads.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.common.RatingDialog;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.commonCode.utils.GlobalData;
import com.calculator.vault.gallery.locker.hide.data.customs.DragSelectTouchListener;
import com.calculator.vault.gallery.locker.hide.data.customs.DragSelectionProcessor;
import com.calculator.vault.gallery.locker.hide.data.data.DecoyDatabase;
import com.calculator.vault.gallery.locker.hide.data.data.ImageVideoDatabase;
import com.calculator.vault.gallery.locker.hide.data.model.itemModel;
import com.calculator.vault.gallery.locker.hide.data.recycleview.GridSpacingItemDecoration;
import com.google.android.gms.ads.AdView;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static com.calculator.vault.gallery.locker.hide.data.common.Share.msPathToWrite;
import static com.calculator.vault.gallery.locker.hide.data.common.Share.msPathToWriteDecoy;

public class HiddenImagesActivity extends AppCompatActivity implements View.OnClickListener
        /*MoPubInterstitial.InterstitialAdListener*//*InterstitialAdHelper.onInterstitialAdListener*/ {

    private static final String TAG = "HiddenImagesActivity";
    private ArrayList<File> al_my_photos = new ArrayList<>();
    private File[] allFiles;
    private RecyclerView moRvHiddenPics;
    private GridLayoutManager moGridLayoutManager;
    private LinearLayout moLlAddPhotos, moLLeditControl, moLLDelete, moLLShare, moLLsave;
    private RelativeLayout moLLNoPhotos;
    private ImageView moIvAddPhoto;
    private static final int REQUEST_CODE = 732;
    private Button btn_showhidden;
    private TextView tvResults, moTvTitle;
    private ArrayList<String> moResultsArrayList = new ArrayList<>();
    private ArrayList<itemModel> moItemFileList = new ArrayList<>();
    private ArrayList<itemModel> itemFilemainList = new ArrayList<>();
    private RelativeLayout moRlShareDeleteSave;
    private TextView moTvEditControl;
    private static final String msDATABASE_NAME = "ImageVideoDatabase";
    private ImageVideoDatabase moImageVideoDatabase = new ImageVideoDatabase(this);
    private ImageView iv_back;
    public static Boolean imageLongClick;
    private String msDatabaseWritepath = Environment.getExternalStorageDirectory().getPath() + File.separator + ".androidData" + File.separator + ".log" + File.separator + ".check" + File.separator;
    private String databasepath;
    private Dialog dialog;
    private int count = 0;
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
    private ImagesMultiSelectAdapter moImagesAdapter;

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
//        Log.e(TAG, "onInterstitialFailed: " + moPubErrorCode);
//        isInterstitialAdLoaded = false;
//        if (Utils.isNetworkConnected(HiddenImagesActivity.this)) {
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
//        if (Utils.isNetworkConnected(HiddenImagesActivity.this)) {
//            mInterstitial.load();
//        }
//        if (isResume) {
//            new HiddenImagesActivity.loadImages().execute();
//        } else {
//            getPhotos();
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
//            interstitial = InterstitialAdHelper.getInstance().load2(HiddenImagesActivity.this, this);
//        }else if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id2))){
//            interstitial = InterstitialAdHelper.getInstance().load(HiddenImagesActivity.this, this);
//        }else {
//            interstitial = InterstitialAdHelper.getInstance().load1(HiddenImagesActivity.this, this);
//        }
//    }
//
//    @Override
//    public void onClosed() {
//        isInterstitialAdLoaded = false;
//        interstitial = InterstitialAdHelper.getInstance().load1(HiddenImagesActivity.this, this);
//        if (isResume) {
//            new HiddenImagesActivity.loadImages().execute();
//        } else {
//            getPhotos();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_images);

//        mInterstitial = new MoPubInterstitial(this, getString(R.string.mopub_int_id));
//        mInterstitial.setInterstitialAdListener(this);
//            mInterstitial.load();
        IntAdsHelper.loadInterstitialAds(HiddenImagesActivity.this, new AdsListener() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onClosed() {
                try {
                    if (isResume) {
                        new loadImages().execute();
                    } else {
                        getPhotos();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onClosed: " + e.toString());
                }
            }
        });

//        TODO Admob Ads
//        interstitial = InterstitialAdHelper.getInstance().load1(HiddenImagesActivity.this, this);

//        IronSource.init(this, "8fb1d745", IronSource.AD_UNIT.INTERSTITIAL);
//        IronSource.shouldTrackNetworkState(this, true);
//        IronSource.setInterstitialListener(this);
//        IronSource.loadInterstitial();

        imageLongClick = false;
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
        databasepath = msDatabaseWritepath + msDATABASE_NAME + ".db";
        dialog = Share.showProgress1(HiddenImagesActivity.this, "Loading...");
        Share.isclickedOnDone = false;

        Share.selected_image_list.clear();
        Log.e("TAG:: ", "onCreate: ");
        initView();
        isDecode = SharedPrefs.getString(this, SharedPrefs.DecoyPassword, "false");

        initViewListener();
        initViewAction();

        adView = (AdView) findViewById(R.id.adView);
        Share.loadAdsBanner(HiddenImagesActivity.this, adView);
        // IronSource Banner Ads
        //ISAdsHelper.loadBannerAd(this, (FrameLayout) findViewById(R.id.flBanner));

        if (Share.isNeedToAdShow(HiddenImagesActivity.this)) {
//            NativeAdvanceHelper.loadGreedyGameAd(this, (FrameLayout) findViewById(R.id.fl_adplaceholder), "float-4028");
        }

    }

    private void getPhotos() {

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


            if (isDecode.equals("true")) {
                moItemFileList = decoyDatabase.getAllDATA(msPathToWriteDecoy);
                Log.e("HiddenImagesActivity", "getPhotos: DECOY_LIST_SIZE " + moItemFileList.size());
            } else {
                moItemFileList = moImageVideoDatabase.getAllDATA(Share.msPathToWrite);
            }

            if (moItemFileList.isEmpty()) {
                moLLeditControl.setVisibility(View.GONE);
                moLLNoPhotos.setVisibility(View.VISIBLE);
                moRlShareDeleteSave.setVisibility(View.GONE);
                moTvEditControl.setText(getString(R.string.Edit));
            } else {
                moLLeditControl.setVisibility(View.VISIBLE);
                moLLNoPhotos.setVisibility(View.GONE);
            }
            moImagesAdapter = new ImagesMultiSelectAdapter(HiddenImagesActivity.this, moItemFileList);
            moRvHiddenPics.setLayoutManager(moGridLayoutManager);
            moRvHiddenPics.setAdapter(moImagesAdapter);
            multiSelect();
        }
    }

    private void initView() {

        moIvAddPhoto = findViewById(R.id.iv_add_photos);
        moRvHiddenPics = findViewById(R.id.rv_hiddenpics);
        moGridLayoutManager = new GridLayoutManager(HiddenImagesActivity.this, 3);
        moRvHiddenPics.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(8), true));
        moLlAddPhotos = findViewById(R.id.ll_add_photos);
        moLLNoPhotos = findViewById(R.id.ll_noPhotos);
        moTvTitle = findViewById(R.id.tv_title);
        moRlShareDeleteSave = findViewById(R.id.rl_ShareDeleteSave);
        moRlShareDeleteSave.setVisibility(View.GONE);
        moLLeditControl = findViewById(R.id.ll_editcontrol);
        moTvEditControl = findViewById(R.id.tv_editcontrol);
        moLLDelete = findViewById(R.id.ll_Delete);
        moLLShare = findViewById(R.id.ll_Share);
        moLLsave = findViewById(R.id.ll_Save);
        //moLlDeleteFromSystem = findViewById(R.id.ll_DeleteFromSystem);
        iv_back = findViewById(R.id.iv_back);
    }

    private void initViewListener() {
        moIvAddPhoto.setOnClickListener(this);
        moLlAddPhotos.setOnClickListener(this);
        moLLeditControl.setOnClickListener(this);
        moLLDelete.setOnClickListener(this);
        moLLShare.setOnClickListener(this);
        moLLsave.setOnClickListener(this);
        //moLlDeleteFromSystem.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void initViewAction() {
        moTvTitle.setText(getString(R.string.Photos));
        //getPhotos();
        loadInterAdAndGetPhotos("init");
    }

    private void loadInterAdAndGetPhotos(String fWhere) {

        if (fWhere.equalsIgnoreCase("init")) {
            getPhotos();
        } else {

            if (Share.isNeedToAdShow(this)) {
                if (IntAdsHelper.isInterstitialLoaded()) {
                    isFromNewFiles = false;
                    IntAdsHelper.showInterstitialAd();
//                    TODO Admob Ads
//                    interstitial.show();
                } else {
                    getPhotos();
//                    if (!SharedPrefs.getBoolean(HiddenImagesActivity.this, Share.IS_RATED)){
//                        if (SharedPrefs.getInt(HiddenImagesActivity.this, Share.RATE_APP_HIDE_COUNT) >= 2 ){
//                            RatingDialog.SmileyRatingDialog(HiddenImagesActivity.this);
//                        }
//                    }
                }
            } else {
                getPhotos();
//                if (!SharedPrefs.getBoolean(HiddenImagesActivity.this, Share.IS_RATED)){
//                    if (SharedPrefs.getInt(HiddenImagesActivity.this, Share.RATE_APP_HIDE_COUNT) >= 2 ){
//                        RatingDialog.SmileyRatingDialog(HiddenImagesActivity.this);
//                    }
//                }
            }
        }

    }

    private void multiSelect() {
        moImagesAdapter.setClickListener(new ImagesMultiSelectAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (moRlShareDeleteSave.getVisibility() == View.GONE) {
                    Intent intent = new Intent(HiddenImagesActivity.this, ViewFullScreenHiddenImageActivity.class);
                    intent.putExtra("showIntent", position);
                    startActivityForResult(intent, 7001);
                } else {
                    moImagesAdapter.toggleSelection(position);
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
                return moImagesAdapter.getSelection();
            }

            @Override
            public boolean isSelected(int index) {
                return moImagesAdapter.getSelection().contains(index);
            }

            @Override
            public void updateSelection(int start, int end, boolean isSelected, boolean calledFromOnStart) {
                moImagesAdapter.selectRange(start, end, isSelected);
            }
        }).withMode(mMode);

        mDragSelectTouchListener = new DragSelectTouchListener().withSelectListener(mDragSelectionProcessor);

        moRvHiddenPics.addOnItemTouchListener(mDragSelectTouchListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_add_photos:
                addPhotosFromGallery();
                break;
            case R.id.iv_add_photos:
                addPhotosFromGallery();
                break;
            case R.id.ll_editcontrol:
                if (moRlShareDeleteSave.getVisibility() == View.GONE) {
                    moTvEditControl.setText(getString(R.string.Cancel));
                    moRlShareDeleteSave.setVisibility(View.VISIBLE);
                } else {
                    moTvEditControl.setText(getString(R.string.Edit));
                    moRlShareDeleteSave.setVisibility(View.GONE);
                    moItemFileList.clear();
                    if (isDecode.equals("true")) {
                        moItemFileList = decoyDatabase.getAllDATA(msPathToWriteDecoy);
                    } else {
                        moItemFileList = moImageVideoDatabase.getAllDATA(Share.msPathToWrite);
                    }
                    moImagesAdapter = new ImagesMultiSelectAdapter(HiddenImagesActivity.this, moItemFileList);
                    moRvHiddenPics.setAdapter(moImagesAdapter);
                    multiSelect();
                }
                break;
            case R.id.ll_Delete:
                Log.e("DELETE", "onClick: SIZE " + moItemFileList.size());
//                int liSelecedItem = 0;
//                for (int i = 0; i < moItemFileList.size(); i++) {
//                    if (moItemFileList.get(i).isSelected()) {
//                        liSelecedItem++;
//                    }
//                }
                if (moImagesAdapter.getCountSelected() > 0) {
                    alertdialog();
                } else {
                    Toast.makeText(this, "Select the image you want to delete.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ll_Share:
                sharePhotosFromGallery();
                break;
            case R.id.ll_Save:
//                int liSelecedItem1 = 0;
//                for (int i = 0; i < moItemFileList.size(); i++) {
//                    if (moItemFileList.get(i).isSelected()) {
//                        liSelecedItem1++;
//                    }
//                }
                if (moImagesAdapter.getCountSelected() > 0) {
                    new saveImages().execute();
                } else {
                    Toast.makeText(this, "Select the image you want to save.", Toast.LENGTH_LONG).show();
                }
                break;
//            case R.id.ll_DeleteFromSystem:
//                DeleteFromSystem();
//                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private void alertdialog() {
        final Dialog dialog1 = new Dialog(HiddenImagesActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.setContentView(R.layout.alert_decoy_passcode);
        TextView mess = (TextView) dialog1.findViewById(R.id.tv_message1);
        mess.setText(getString(R.string.delete_image_message));
        Button yesbtn = (Button) dialog1.findViewById(R.id.btn_dialogOK1);
        Button nobtn = (Button) dialog1.findViewById(R.id.btn_dialogNo1);

        yesbtn.setOnClickListener(v -> {
            try {
                dialog1.dismiss();
                new deleteImages().execute();
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

    private class saveImages extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
            isStorageFull = false;
        }

        @Override
        protected Void doInBackground(String... strings) {
            savePhotosFromGallery();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent i = new Intent("android.intent.action.IMAGES").putExtra("some_msg", "2");
            HiddenImagesActivity.this.sendBroadcast(i);
        }
    }

    private void savePhotosFromGallery() {

        for (int i = 0; i < moItemFileList.size(); i++) {
            if (moItemFileList.get(i).isSelected() && !isStorageFull) {
                miItemCount++;
                Log.e("TAG", "deletePhotosFromGallery: " + moItemFileList.get(i).getNewFilename());
                itemModel model;

                if (isDecode.equals("true")) {
                    model = decoyDatabase.getSingleData(moItemFileList.get(i).getNewFilename());
                } else {
                    model = moImageVideoDatabase.getSingleData(moItemFileList.get(i).getNewFilename());
                }

                File loTempFile = new File(model.getNewFilepath() + model.getNewFilename());
                File loFile1 = new File(loTempFile.getAbsolutePath());
                int liFileSize = Integer.parseInt(String.valueOf(loFile1.length() / 1024));

                Log.e("FILE_SIZE", "FILE SPACE --> " + liFileSize);
                Log.e("FILE_SIZE", "FREE SPACE --> " + GlobalData.kilobytesAvailable(loFile1));

                if (GlobalData.kilobytesAvailable(loFile1) >= liFileSize) {
                    File from = new File(model.getOldFilepath() + model.getNewFilename());
                    File to = new File(model.getOldFilepath() + model.getOldFilename());
                    from.renameTo(to);

                    String oldpath = model.getOldFilepath();
                    String newpath = model.getNewFilepath();

                    copyPhotoTo(newpath + model.getNewFilename(), oldpath, model.getOldFilename());

                    model.setStatus("1");
                    if (isDecode.equals("true")) {
                        decoyDatabase.updateSingleData(model);
                    } else {
                        moImageVideoDatabase.updateSingleData(model);
                    }

                    Uri contentUri = Uri.fromFile(to);
                    Log.e("ATGA", "savePhotosFromGallery: rename file name::: " + to.getName());
                    Log.e("ATGA", "savePhotosFromGallery: rename file path::: " + to.getAbsolutePath());

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(contentUri);
                    sendBroadcast(mediaScanIntent);

                    MediaScannerConnection.scanFile(HiddenImagesActivity.this, new String[]{to.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String s, Uri uri) {
                            Log.e("TAG", "onScanCompleted: ");
                        }
                    });
                } else {
                    isStorageFull = true;
                }
            }
        }
        moItemFileList.clear();
        if (isDecode.equals("true")) {
            moItemFileList = decoyDatabase.getAllDATA(msPathToWriteDecoy);
        } else {
            moItemFileList = moImageVideoDatabase.getAllDATA(Share.msPathToWrite);
        }
    }

    private void sharePhotosFromGallery() {
        ArrayList<Integer> positions = new ArrayList<>();
        for (itemModel image : moItemFileList) {
            if (image.isSelected()) {
                positions.add(moItemFileList.indexOf(image));
            }
        }

        if (!positions.isEmpty()) {
            shareImages(positions);
        } else {
            Toast.makeText(this, "Select the image you want to share.", Toast.LENGTH_LONG).show();
        }
    }

    private class deleteImages extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
            isStorageFull = false;
        }

        @Override
        protected Void doInBackground(String... strings) {
            deletePhotosFromGallery();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
          //  dialog.dismiss();
            Intent i = new Intent("android.intent.action.IMAGES").putExtra("some_msg", "2");
            HiddenImagesActivity.this.sendBroadcast(i);
        }
    }

    private void deletePhotosFromGallery() {
        for (int i = 0; i < moItemFileList.size(); i++) {
            if (moItemFileList.get(i).isSelected() && !isStorageFull) {
                miItemCount++;
                Log.e("TAG", "deletePhotosFromGallery: " + moItemFileList.get(i).getNewFilename());

                itemModel model;

                if (isDecode.equals("true")) {
                    model = decoyDatabase.getSingleData(moItemFileList.get(i).getNewFilename());
                } else {
                    model = moImageVideoDatabase.getSingleData(moItemFileList.get(i).getNewFilename());
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

                    MediaScannerConnection.scanFile(HiddenImagesActivity.this, new String[]{file.getAbsolutePath()}, null, (s, uri) -> Log.e("TAG", "onScanCompleted: "));
                    to.delete();
                    if (isDecode.equals("true")) {
                        decoyDatabase.removeSingleData(model.getNewFilename());
                    } else {
                        moImageVideoDatabase.removeSingleData(model.getNewFilename());
                    }
                } else {
                    isStorageFull = true;
                }
            }
        }
        moItemFileList.clear();

        if (isDecode.equals("true")) {
            moItemFileList = decoyDatabase.getAllDATA(msPathToWriteDecoy);
        } else {
            moItemFileList = moImageVideoDatabase.getAllDATA(Share.msPathToWrite);
        }
    }

    private void addPhotosFromGallery() {

        SharedPrefs.save(HiddenImagesActivity.this, SharedPrefs.GallerySelected, "");
        Intent intent = new Intent(this, AlbumActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        Log.e("HiddenImagesActivity", "onActivityResult: REQ_CODE " + requestCode);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //moResultsArrayList = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert moResultsArrayList != null;
                HashMap<String, Integer> hashMap = new HashMap<>();
                List<Map.Entry<String, Integer>> entries =
                        new ArrayList<Map.Entry<String, Integer>>(hashMap.entrySet());
                Map.Entry<String, Integer> entry = entries.get(entries.size() - 1);

                // show results in textview
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("Totally %d images selected:", moResultsArrayList.size())).append("\n");

                Log.e("TAG:: ", "onActivityResult: " + "moResultsArrayList size:: " + moResultsArrayList.size());
                for (String result : moResultsArrayList) {
                    File file = new File(result);

                    Log.e("TAG", "onActivityResult: File Path " + result);
                    Log.e("TAG", "onActivityResult: File name :: " + file.getName());
                    Log.e("TAG", "onActivityResult: File abs path :: " + file.getParent());

                    String filename1 = file.getName();
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_hhmmssSSS").format(new Date());

                    File from = new File(file.getParent(), filename1);

                    //Log.e("TAG", "onActivityResult: name " +filename[0]);
                    //  String fileNameWithOutExt = FilenameUtils.removeExtension(filename1);
                    String fileNameExt = FilenameUtils.getExtension(filename1);
                    File to = new File(file.getParent(), "file" + timeStamp + "." + "zxcv");
                    // File to = new File(file.getParent(), "file" + timeStamp + "." + fileNameExt);
                    from.renameTo(to);
                    Log.e("TAG:: ", "onActivityResult: " + to.getName());
                    if (isDecode.equals("true")) {
                        copyPhotoTo(to.getAbsolutePath(), msPathToWriteDecoy, to.getName());
                    } else {
                        copyPhotoTo(to.getAbsolutePath(), Share.msPathToWrite, to.getName());
                    }

                    itemModel oldSingleData;

                    if (isDecode.equals("true")) {
                        oldSingleData = decoyDatabase.getOldSingleData(filename1);
                    } else {
                        oldSingleData = moImageVideoDatabase.getOldSingleData(filename1);
                    }

                    String s = oldSingleData.getStatus();
                    oldSingleData.setStatus("0");
                    oldSingleData.setNewFilename(to.getName());
                    if (s != null && s.equals("1")) {

                        if (isDecode.equals("true")) {
                            decoyDatabase.updateSingleData(oldSingleData);
                        } else {
                            moImageVideoDatabase.updateSingleData(oldSingleData);
                        }
                    } else {
                        if (isDecode.equals("true")) {
                            decoyDatabase.addData(new itemModel("image", filename1, to.getName(), file.getParent() + "/", msPathToWriteDecoy, "0"));
                        } else {
                            moImageVideoDatabase.addData(new itemModel("image", filename1, to.getName(), file.getParent() + "/", Share.msPathToWrite, "0"));
                        }
                    }
                    Log.e("TAG", "onActivityResult: " + " Old NAme:: " + filename1);
                    Log.e("TAG", "onActivityResult: " + " New NAme:: " + to.getName());
                    Log.e("TAG", "onActivityResult: " + " Old Path:: " + file.getParent());
                    Log.e("TAG", "onActivityResult: " + " New Path:: " + to.getParent());

                    to.delete();

                    Uri contentUri = Uri.fromFile(file);

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(contentUri);
                    sendBroadcast(mediaScanIntent);

                    MediaScannerConnection.scanFile(HiddenImagesActivity.this, new String[]{file.getAbsolutePath()}, null, (s1, uri) -> Log.e("TAG", "onScanCompleted: "));
                    sb.append(result).append("\n");

                }
                loadInterAdAndGetPhotos("activityResult");
            }
        }

        if (requestCode == 7001) {
            //moImagesAdapter.notifyDataSetChanged();
            //getPhotos();
            loadInterAdAndGetPhotos("activityResult7001");
        }

        super.onActivityResult(requestCode, resultCode, data);
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

//    @Override
//    public void onClick(int fiPosition, itemModel foItemModel, ImageView foIvcheckBox, boolean isselected) {
//        Log.e("CLICK", "onClick: ADAPTER_CLICK " + imageLongClick);
//        if (imageLongClick) {
//            if (isselected) {
//                if (count > 0) {
//                    count--;
//                }
//                if (count == 0) {
//                    imageLongClick = false;
//                }
//                Log.e("TAG::: ", "onClick: count:: dec " + count);
//            } else {
//                count++;
//                Log.e("TAG::: ", "onClick: count:: inc " + count);
//            }
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();

        System.gc();
        //IronSource.onResume(this);
        try {
            isDecode = SharedPrefs.getString(this, SharedPrefs.DecoyPassword, "false");
            moItemFileList.clear();
            if (isDecode.equals("true")) {
                moItemFileList = decoyDatabase.getAllDATA(msPathToWriteDecoy);
                Log.e("TAG:: ", "onResume: " + "Decoy::: " + moItemFileList.size());
            } else {
                moItemFileList = moImageVideoDatabase.getAllDATA(Share.msPathToWrite);
                Log.e("TAG:: ", "onResume: " + "Actual::: " + moItemFileList.size());
            }
        } catch (Exception e) {
            Log.e(TAG, "onResume: " + e.toString());
        }
//        showNative();
        try {
            if (moItemFileList.isEmpty()) {
                moLLeditControl.setVisibility(View.GONE);
                moLLNoPhotos.setVisibility(View.VISIBLE);
                moRlShareDeleteSave.setVisibility(View.GONE);
                moTvEditControl.setText(getString(R.string.Edit));
                showNative();
            } else {
                moLLeditControl.setVisibility(View.VISIBLE);
                moLLNoPhotos.setVisibility(View.GONE);
                findViewById(R.id.fl_adplaceholder).setVisibility(View.GONE);


            }
        } catch (Exception e) {
            Log.e(TAG, "onResume: " + e.toString());
        }


        try {
            if (moItemFileList != null && moItemFileList.size() != 0)
                moImagesAdapter.onUpdate(moItemFileList);
        } catch (Exception e) {
            Log.e(TAG, "onResume: " + e.toString());
        }


        try {
            if (Share.isclickedOnDone) {

                showNative();

                if (isDecode.equals("false")) {
                    if (!moResultsArrayList.isEmpty()) {
                        SharedPrefs.save(HiddenImagesActivity.this, Share.RATE_APP_HIDE_COUNT, SharedPrefs.getInt(HiddenImagesActivity.this, Share.RATE_APP_HIDE_COUNT) + 1);
                    }
                }
                if (Share.isNeedToAdShow(this)) {
                    if (IntAdsHelper.isInterstitialLoaded()) {
                        isFromNewFiles = true;
                        isResume = true;
                        IntAdsHelper.showInterstitialAd();
                        //                    TODO Admob Ads
                        //                    interstitial.show();
                    } else {
                        isFromNewFiles = true;
                        new loadImages().execute();
                    }
                } else {
                    isFromNewFiles = true;
                    new loadImages().execute();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "onResume: " + e.toString());
        }


        try {
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
                                        moTvEditControl.setText(getString(R.string.Edit));
                                        findViewById(R.id.fl_adplaceholder).setVisibility(View.VISIBLE);
                                    } else {
                                        moLLeditControl.setVisibility(View.VISIBLE);
                                        moLLNoPhotos.setVisibility(View.GONE);
                                        imageLongClick = false;
                                    }
                                    moImagesAdapter = new ImagesMultiSelectAdapter(HiddenImagesActivity.this, moItemFileList);
                                    moRvHiddenPics.setAdapter(moImagesAdapter);
                                    multiSelect();
                                } else {
                                    Toast.makeText(HiddenImagesActivity.this, "Please select a image first.", Toast.LENGTH_SHORT).show();
                                }
                                miItemCount = 0;
                                dialog.dismiss();
                                if (isStorageFull) {
                                    Toast.makeText(HiddenImagesActivity.this, "Your Storage is full.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.start();
                    }
                }
            };
            //registering receiver
            this.registerReceiver(mReceiver, intentFilter);
        } catch (Exception e) {
            Log.e(TAG, "onResume: " + e.toString());
        }

        Share.isclickedOnDone = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NativeAdvanceHelper.onDestroy();
//        if (mReceiver != null) {
//            this.unregisterReceiver(this.mReceiver);
//        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        try {
            if (mReceiver != null) {
                this.unregisterReceiver(this.mReceiver);
            }
        } catch (Exception e) {
            Log.e(TAG, "onPause: " + e.toString());
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
            try {
                File file = new File(result);

                Log.e("TAG", "onActivityResult: File Path " + result);
                Log.e("TAG", "onActivityResult: File name :: " + file.getName());
                Log.e("TAG", "onActivityResult: File abs path :: " + file.getParent());

                String filename1 = file.getName();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_hhmmssSSS").format(new Date());

                File from = new File(file.getParent(), filename1);

                String fileNameExt = FilenameUtils.getExtension(filename1);
                File to = new File(file.getParent(), "file" + timeStamp + "." + "zxcv");
                // File to = new File(file.getParent(), "file" + timeStamp + "." + fileNameExt);
                from.renameTo(to);
                Log.e("TAG:: ", "onActivityResult: " + to.getName());
                if (isDecode.equals("true")) {
                    copyPhotoTo(to.getAbsolutePath(), msPathToWriteDecoy, to.getName());
                } else {
                    copyPhotoTo(to.getAbsolutePath(), Share.msPathToWrite, to.getName());
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
                    oldSingleData.setNewFilename(to.getName());
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
                        decoyDatabase.addData(new itemModel("image", filename1, to.getName(), file.getParent() + "/", msPathToWriteDecoy, "0"));
                    } else {
                        moImageVideoDatabase.addData(new itemModel("image", filename1, to.getName(), file.getParent() + "/", Share.msPathToWrite, "0"));
                    }

                }
                Log.e("TAG", "onActivityResult: " + " Old NAme:: " + filename1);
                Log.e("TAG", "onActivityResult: " + " New NAme:: " + to.getName());
                Log.e("TAG", "onActivityResult: " + " Old Path:: " + file.getParent());
                Log.e("TAG", "onActivityResult: " + " New Path:: " + to.getParent());

                to.delete();

                Uri contentUri = Uri.fromFile(file);

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(contentUri);
                sendBroadcast(mediaScanIntent);

                MediaScannerConnection.scanFile(HiddenImagesActivity.this, new String[]{file.getAbsolutePath()}, null, (s1, uri) -> Log.e("TAG", "onScanCompleted: "));
            } catch (Exception e) {
                Log.e(TAG, "callSelectedPhotos: " + e.toString());
                e.printStackTrace();
            }
        }
    }

    public class loadImages extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e("TAG:: ", "doInBackground: " + "Share.selected_image_list:: " + Share.selected_image_list.size());
            try {
                callSelectedPhotos();
                Share.selected_image_list.clear();
                moItemFileList.clear();
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: " + e.toString());
                e.printStackTrace();
            }


            try {
                if (isDecode.equals("true")) {
                    moItemFileList = decoyDatabase.getAllDATA(msPathToWriteDecoy);
                } else {
                    moItemFileList = moImageVideoDatabase.getAllDATA(Share.msPathToWrite);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "doInBackground: " + e.toString());
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
                moTvEditControl.setText(getString(R.string.Edit));
            } else {
                moLLeditControl.setVisibility(View.VISIBLE);
                moLLNoPhotos.setVisibility(View.GONE);
            }

            if (isDecode.equals("false")) {
                if (isFromNewFiles) {
                    if (!SharedPrefs.getBoolean(HiddenImagesActivity.this, Share.IS_RATED)) {
                        if (SharedPrefs.getInt(HiddenImagesActivity.this, Share.RATE_APP_HIDE_COUNT) >= 2) {
                            RatingDialog.SmileyRatingDialog(HiddenImagesActivity.this);
                        }
                    }
                }
            }

            if (isFromNewFiles) {
                findViewById(R.id.fl_adplaceholder).setVisibility(View.GONE);
            } else {
                showNative();
            }
            moImagesAdapter = new ImagesMultiSelectAdapter(HiddenImagesActivity.this, moItemFileList);
            moRvHiddenPics.setLayoutManager(moGridLayoutManager);
            moRvHiddenPics.setAdapter(moImagesAdapter);
            multiSelect();
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void shareImages(ArrayList<Integer> positions) {
        try {
            ArrayList<Uri> imageList = new ArrayList<>();
            for (int position : positions) {

                File file = new File(moItemFileList.get(position).getNewFilepath() + moItemFileList.get(position).getNewFilename());
                copyPhotoTo(file.getAbsolutePath(), "/storage/emulated/0/.androidData/.log/.dup/", moItemFileList.get(position).getOldFilename());
                imageList.add(FileProvider.getUriForFile(this, getPackageName() + ".provider",
                        new File("/storage/emulated/0/.androidData/.log/.dup/" + moItemFileList.get(position).getOldFilename())));
                Log.e("HiddenImagesActivity", "shareImages: SHARE " + moItemFileList.get(position).getOldFilepath() +
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
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong, Please try again...", Toast.LENGTH_SHORT).show();
        }
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
            if (Share.isNeedToAdShow(HiddenImagesActivity.this)) {
                NativeAdvanceHelper.loadNativeAd(HiddenImagesActivity.this, findViewById(R.id.fl_adplaceholder), false);
            }
        } else {
            findViewById(R.id.fl_adplaceholder).setVisibility(View.GONE);
        }
    }

}

