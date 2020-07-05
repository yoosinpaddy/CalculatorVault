package com.calculator.vault.gallery.locker.hide.data.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import java.util.HashSet;

import static com.calculator.vault.gallery.locker.hide.data.activity.HiddenImagesActivity.imageLongClick;
import static com.calculator.vault.gallery.locker.hide.data.common.Share.msPathToWrite;
import static com.calculator.vault.gallery.locker.hide.data.common.Share.msPathToWriteDecoy;

public class HiddenVideosActivity extends AppCompatActivity implements View.OnClickListener
        /*MoPubInterstitial.InterstitialAdListener*//*InterstitialAdHelper.onInterstitialAdListener*/ {
    private static final String TAG = "HiddenVideosActivity";
    // private String msPathToWrite = Environment.getExternalStorageDirectory().getPath() + File.separator + ".mytmp" + File.separator + ".log" + File.separator + ".last" + File.separator;
    //private ArrayList<File> al_my_Videos = new ArrayList<>();
    private File[] allFiles;
    RecyclerView moRvHiddenVideos;
    GridLayoutManager moGridLayoutManager;
    private LinearLayout moLLAddVideos, moLlEditControl, moLlDelete, moLlShare, moLlSave;
    private RelativeLayout moLLNoVideos;
    private ImageView moIvAddVideos;
    private static final int REQUEST_CODE = 732;
    private TextView moTvTitleVideos;
    private ArrayList<String> moResultsArralList = new ArrayList<>();
    private ArrayList<itemModel> moItemFileList = new ArrayList<>();
    private ArrayList<itemModel> itemFilemainList = new ArrayList<>();
    private RelativeLayout moRlShareDeleteSave;
    private TextView moTvEditControl;
    private static final String DATABASE_NAME = "ImageVideoDatabase";
    ImageVideoDatabase moImageVideoDatabase = new ImageVideoDatabase(this);
    private Dialog moDialog;
    private ImageView iv_back;
    private int count = 0;
    private DecoyDatabase decoyDatabase = new DecoyDatabase(this);
    private String isDecoy;
    private AdView adView;
    private Dialog dialog;
    private BroadcastReceiver mReceiver;
    int miItemCount = 0;
    private ArrayList<Integer> moPositions;
    private ArrayList<Uri> moVideoList;
    private boolean isStorageFull = false;
    private boolean isAdLoad = true;
    private boolean isFromVideoClick = false;
    private int miClickPos = 0;
    private boolean isFromNewFiles = false;

    //Multi Select Adapter
    private ImagesMultiSelectAdapter moImagesAdapter;
    private DragSelectionProcessor.Mode mMode = DragSelectionProcessor.Mode.FirstItemDependent;
    private DragSelectTouchListener mDragSelectTouchListener;
    private DragSelectionProcessor mDragSelectionProcessor;


    private LoadVideos loadVideos = null;

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
//        if (Utils.isNetworkConnected(HiddenVideosActivity.this)) {
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
//        if (Utils.isNetworkConnected(HiddenVideosActivity.this)) {
//            mInterstitial.load();
//        }
//        if (isFromVideoClick) {
//            Intent intent = new Intent(HiddenVideosActivity.this, VideoPlayerActivity.class);
//            intent.putExtra("videoURL", moItemFileList.get(miClickPos).getNewFilepath() + moItemFileList.get(miClickPos).getNewFilename());
//            startActivityForResult(intent, 7001);
//        } else {
//            new HiddenVideosActivity.LoadVideos().execute();
//        }
//        isFromVideoClick = false;
//
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
//            interstitial = InterstitialAdHelper.getInstance().load2(HiddenVideosActivity.this, this);
//        }else if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id2))){
//            interstitial = InterstitialAdHelper.getInstance().load(HiddenVideosActivity.this, this);
//        }else {
//            interstitial = InterstitialAdHelper.getInstance().load1(HiddenVideosActivity.this, this);
//        }
//    }
//
//    @Override
//    public void onClosed() {
//        isInterstitialAdLoaded = false;
//        interstitial = InterstitialAdHelper.getInstance().load1(HiddenVideosActivity.this, this);
//        if (isFromVideoClick) {
//            Intent intent = new Intent(HiddenVideosActivity.this, VideoPlayerActivity.class);
//            intent.putExtra("videoURL", moItemFileList.get(miClickPos).getNewFilepath() + moItemFileList.get(miClickPos).getNewFilename());
//            startActivityForResult(intent, 7001);
//        } else {
//            new HiddenVideosActivity.LoadVideos().execute();
//        }
//        isFromVideoClick = false;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_videos);

//        mInterstitial = new MoPubInterstitial(this, getString(R.string.mopub_int_id));
//        mInterstitial.setInterstitialAdListener(this);
//        if (Utils.isNetworkConnected(HiddenVideosActivity.this)) {
//            mInterstitial.load();
        IntAdsHelper.loadInterstitialAds(HiddenVideosActivity.this, new AdsListener() {
            @Override
            public void onLoad() {

            }

            @Override
            public void onClosed() {
                if (isFromVideoClick) {
                    Intent intent = new Intent(HiddenVideosActivity.this, VideoPlayerActivity.class);
                    intent.putExtra("videoURL", moItemFileList.get(miClickPos).getNewFilepath() + moItemFileList.get(miClickPos).getNewFilename());
                    startActivityForResult(intent, 7001);
                } else {


                    if (loadVideos != null && loadVideos.getStatus() == android.os.AsyncTask.Status.RUNNING) {
                        return;
                    }
                    loadVideos = new LoadVideos();
                    loadVideos.execute();
                }
                isFromVideoClick = false;
            }
        });
//        }

//        TODO Admob Ads
//        interstitial = InterstitialAdHelper.getInstance().load1(HiddenVideosActivity.this, this);

//        IronSource.init(this, "8fb1d745", IronSource.AD_UNIT.INTERSTITIAL);
//        IronSource.shouldTrackNetworkState(this, true);
//        IronSource.setInterstitialListener(this);
//        IronSource.loadInterstitial();

        moPositions = new ArrayList<>();
        dialog = Share.showProgress1(HiddenVideosActivity.this, "Loading...");
        Share.isclickedOnDone = false;
        imageLongClick = false;
        Share.selected_image_list.clear();
        Log.e("TAG:: ", "onCreate: ");
        initView();
        isDecoy = SharedPrefs.getString(HiddenVideosActivity.this, SharedPrefs.DecoyPassword, "false");
        initViewListener();
        initViewAction();
        adView = (AdView) findViewById(R.id.adView);
        Share.loadAdsBanner(HiddenVideosActivity.this, adView);

        // IronSource Banner Ads
        //ISAdsHelper.loadBannerAd(this,(FrameLayout) findViewById(R.id.flBanner));

    }

    private void getVideos() {

        File path;
        if (isDecoy.equals("true")) {
            path = new File(msPathToWriteDecoy);
        } else {
            path = new File(msPathToWrite);
        }

        moItemFileList.clear();
        allFiles = null;

        if (path.exists()) {

            Log.e("if1", "if1");
            allFiles = path.listFiles((dir, name) -> (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")));

            Log.e("array_size", "" + allFiles.length);

            if (isDecoy.equals("true")) {
                moItemFileList = decoyDatabase.getAllDATAVideo(msPathToWriteDecoy);
            } else {
                moItemFileList = moImageVideoDatabase.getAllDATAVideos(msPathToWrite);
            }

            if (moItemFileList.isEmpty()) {
                moLlEditControl.setVisibility(View.GONE);
                moLLNoVideos.setVisibility(View.VISIBLE);
                moRlShareDeleteSave.setVisibility(View.GONE);
                moTvEditControl.setText(getString(R.string.Edit));
            } else {
                moLlEditControl.setVisibility(View.VISIBLE);
                moLLNoVideos.setVisibility(View.GONE);
            }

            moImagesAdapter = new ImagesMultiSelectAdapter(HiddenVideosActivity.this, moItemFileList);
            moRvHiddenVideos.setLayoutManager(moGridLayoutManager);
            moRvHiddenVideos.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(8), true));
            moRvHiddenVideos.setAdapter(moImagesAdapter);
            multiSelect();
        }
    }

    private void initView() {

        moIvAddVideos = findViewById(R.id.iv_add_videos);
        moRvHiddenVideos = findViewById(R.id.rv_hiddenvideos);
        moGridLayoutManager = new GridLayoutManager(HiddenVideosActivity.this, 3);
        moLLAddVideos = findViewById(R.id.ll_add_videos);
        moLLNoVideos = findViewById(R.id.ll_noVideos);
        moTvTitleVideos = findViewById(R.id.tv_titleVideos);
        moRlShareDeleteSave = findViewById(R.id.rl_ShareDeleteSave);
        moLlEditControl = findViewById(R.id.ll_editcontrol);
        moTvEditControl = findViewById(R.id.tv_editcontrol);
        moLlDelete = findViewById(R.id.ll_Delete);
        moLlShare = findViewById(R.id.ll_Share);
        moLlSave = findViewById(R.id.ll_Save);
        //moLlDeleteFromSystem = findViewById(R.id.ll_DeleteFromSystem);
        iv_back = findViewById(R.id.iv_back);
    }

    private void initViewListener() {
        moIvAddVideos.setOnClickListener(this);
        moLLAddVideos.setOnClickListener(this);
        moLlEditControl.setOnClickListener(this);
        moLlDelete.setOnClickListener(this);
        moLlShare.setOnClickListener(this);
        moLlSave.setOnClickListener(this);
        //moLlDeleteFromSystem.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void initViewAction() {
        moTvTitleVideos.setText(getString(R.string.Videos));
        //getVideos();
        loadInterAdAndGetVideos("init");
    }

    private void loadInterAdAndGetVideos(String fWhere) {

        if (fWhere.equalsIgnoreCase("init")) {
            getVideos();
        } else {
            if (Share.isNeedToAdShow(this)) {
                if (IntAdsHelper.isInterstitialLoaded()) {
                    isFromNewFiles = false;
                    IntAdsHelper.showInterstitialAd();
//                            TODO Admob Ads
//                            interstitial.show();
                } else {
                    isFromNewFiles = false;
                    getVideos();
//                    if (!SharedPrefs.getBoolean(HiddenVideosActivity.this, Share.IS_RATED)){
//                        if (SharedPrefs.getInt(HiddenVideosActivity.this, Share.RATE_APP_HIDE_COUNT) >= 2 ){
//                            RatingDialog.SmileyRatingDialog(HiddenVideosActivity.this);
//                        }
//                    }
                }
            } else {
                isFromNewFiles = false;
                getVideos();
//                if (!SharedPrefs.getBoolean(HiddenVideosActivity.this, Share.IS_RATED)){
//                    if (SharedPrefs.getInt(HiddenVideosActivity.this, Share.RATE_APP_HIDE_COUNT) >= 2 ){
//                        RatingDialog.SmileyRatingDialog(HiddenVideosActivity.this);
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
                    Share.isclickedOnDone = false;
                    if (Share.isNeedToAdShow(HiddenVideosActivity.this)) {
                        if (IntAdsHelper.isInterstitialLoaded()) {
                            isFromNewFiles = false;
                            IntAdsHelper.showInterstitialAd();
//                            TODO Admob Ads
//                            interstitial.show();
                            miClickPos = position;
                            isFromVideoClick = true;
                        } else {
                            isFromNewFiles = false;
                            Intent intent = new Intent(HiddenVideosActivity.this, VideoPlayerActivity.class);
                            intent.putExtra("videoURL", moItemFileList.get(position).getNewFilepath() + moItemFileList.get(position).getNewFilename());
                            startActivityForResult(intent, 7001);
                        }
                    } else {
                        isFromNewFiles = false;
                        Intent intent = new Intent(HiddenVideosActivity.this, VideoPlayerActivity.class);
                        intent.putExtra("videoURL", moItemFileList.get(position).getNewFilepath() + moItemFileList.get(position).getNewFilename());
                        startActivityForResult(intent, 7001);
                    }
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

        moRvHiddenVideos.addOnItemTouchListener(mDragSelectTouchListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_add_videos:
                addVideosFromGallery();
                break;
            case R.id.iv_add_videos:
                addVideosFromGallery();
                break;
            case R.id.ll_editcontrol:
                if (moRlShareDeleteSave.getVisibility() == View.GONE) {
                    moTvEditControl.setText(getString(R.string.Cancel));
                    moRlShareDeleteSave.setVisibility(View.VISIBLE);
                } else {
                    moTvEditControl.setText(getString(R.string.Edit));
                    moRlShareDeleteSave.setVisibility(View.GONE);
                    moItemFileList.clear();
                    if (isDecoy.equals("true")) {
                        moItemFileList = decoyDatabase.getAllDATAVideo(msPathToWriteDecoy);
                    } else {
                        moItemFileList = moImageVideoDatabase.getAllDATAVideos(msPathToWrite);
                    }
                    moImagesAdapter = new ImagesMultiSelectAdapter(HiddenVideosActivity.this, moItemFileList);
                    moRvHiddenVideos.setAdapter(moImagesAdapter);
                    multiSelect();
                }
                break;
            case R.id.ll_Delete:
                if (moImagesAdapter.getCountSelected() > 0) {
                    alertdialog();
                } else {
                    Toast.makeText(this, "Select the video you want to delete.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ll_Share:
                shareVideosFromGallery();
                break;
            case R.id.ll_Save:
                if (moImagesAdapter.getCountSelected() > 0) {
                    new saveVideos().execute();
                } else {
                    Toast.makeText(this, "Select the video you want to save.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private void alertdialog() {
        final Dialog dialog1 = new Dialog(HiddenVideosActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.setContentView(R.layout.alert_decoy_passcode);
        TextView mess = dialog1.findViewById(R.id.tv_message1);
        mess.setText(getString(R.string.delete_image_message));
        Button loBtnYes = dialog1.findViewById(R.id.btn_dialogOK1);
        Button loBtnNo = dialog1.findViewById(R.id.btn_dialogNo1);

        loBtnYes.setOnClickListener(v -> {
            try {
                new deleteVideos().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog1.dismiss();
        });
        loBtnNo.setOnClickListener(v -> dialog1.dismiss());

        if (!dialog1.isShowing()) {
            dialog1.show();
        }

    }

    private class saveVideos extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
            isStorageFull = false;
        }

        @Override
        protected Void doInBackground(String... strings) {
            saveVideosFromGallery();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "1");
            HiddenVideosActivity.this.sendBroadcast(i);
        }
    }

    private void saveVideosFromGallery() {
        miItemCount = 0;
        for (int i = 0; i < moItemFileList.size(); i++) {
            if (moItemFileList.get(i).isSelected()) {
                miItemCount++;
                Log.e("TAG", "deleteVideosFromGallery: " + moItemFileList.get(i).getNewFilename());
                itemModel model;
                if (isDecoy.equals("true")) {
                    model = decoyDatabase.getSingleDataVideo(moItemFileList.get(i).getNewFilename());
                } else {
                    model = moImageVideoDatabase.getSingleDataVideo(moItemFileList.get(i).getNewFilename());
                }

                File loTempFile = new File(model.getNewFilepath() + model.getNewFilename());
                File loFile1 = new File(loTempFile.getAbsolutePath());
                int liFileSize = Integer.parseInt(String.valueOf(loFile1.length() / 1024));

                Log.e("FILE_SIZE", "FILE SPACE --> " + liFileSize);
                Log.e("FILE_SIZE", "FREE SPACE --> " + GlobalData.kilobytesAvailable(loFile1));

                if (GlobalData.kilobytesAvailable(loFile1) >= liFileSize) {
                    File from = new File(model.getOldFilepath() + model.getNewFilename());
                    File to = new File(model.getOldFilepath() + model.getOldFilename());

                    String oldpath = model.getOldFilepath();
                    String newpath = model.getNewFilepath();

                    copyPhotoTo(newpath + model.getNewFilename(), oldpath, model.getOldFilename());

                    from.renameTo(to);

                    model.setStatus("1");
                    if (isDecoy.equals("true")) {
                        decoyDatabase.updateSingleDataVideo(model);
                    } else {
                        moImageVideoDatabase.updateSingleDataVideo(model);
                    }

                    Uri contentUri = Uri.fromFile(to);
                    Log.e("ATGA", "saveVideosFromGallery: rename file name::: " + to.getName());
                    Log.e("ATGA", "saveVideosFromGallery: rename file path::: " + to.getAbsolutePath());

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(contentUri);
                    sendBroadcast(mediaScanIntent);

                    MediaScannerConnection.scanFile(HiddenVideosActivity.this, new String[]{to.getAbsolutePath()}, null, (s, uri) -> Log.e("TAG", "onScanCompleted: "));
                } else {
                    isStorageFull = true;
                }
            }
        }
        moItemFileList.clear();
        if (isDecoy.equals("true")) {
            moItemFileList = decoyDatabase.getAllDATAVideo(msPathToWriteDecoy);
        } else {
            moItemFileList = moImageVideoDatabase.getAllDATAVideos(msPathToWrite);
        }
    }

    private void shareVideosFromGallery() {
        moPositions = new ArrayList<>();
        for (itemModel image : moItemFileList) {
            if (image.isSelected()) {
                moPositions.add(moItemFileList.indexOf(image));
            }
        }
        Log.e("HiddenVideoActivity", "shareVideosFromGallery: POS_SIZE " + moPositions.size());
        if (!moPositions.isEmpty()) {
            new shareVideos().execute();
        } else {
            Toast.makeText(this, "Select the video you want to share.", Toast.LENGTH_LONG).show();
        }
    }

    private class deleteVideos extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (dialog != null && !dialog.isShowing()) {
                dialog.dismiss();
                dialog.show();
                isStorageFull = false;
            }
            Log.e("TAG123", "onPreExecute: -------------->");
        }

        @Override
        protected Void doInBackground(String... strings) {
            deleteVideosFromGallery();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "1");
            HiddenVideosActivity.this.sendBroadcast(i);
        }
    }

    private void deleteVideosFromGallery() {
        for (int i = 0; i < moItemFileList.size(); i++) {
            if (moItemFileList.get(i).isSelected()) {
                miItemCount++;
                Log.e("TAG", "deleteVideosFromGallery: " + moItemFileList.get(i).getNewFilename());

                itemModel model;
                if (isDecoy.equals("true")) {
                    model = decoyDatabase.getSingleDataVideo(moItemFileList.get(i).getNewFilename());
                } else {
                    model = moImageVideoDatabase.getSingleDataVideo(moItemFileList.get(i).getNewFilename());
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
                    File asd = new File(model.getOldFilepath(), model.getOldFilename());
                    Uri contentUri = Uri.fromFile(to);
                    Log.e("ATGA", "saveVideosFromGallery: rename file name::: " + asd.getName());
                    Log.e("ATGA", "saveVideosFromGallery: rename file path::: " + asd.getAbsolutePath());

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(contentUri);
                    sendBroadcast(mediaScanIntent);

                    MediaScannerConnection.scanFile(HiddenVideosActivity.this, new String[]{asd.getAbsolutePath()}, null, (s, uri) -> Log.e("TAG", "onScanCompleted: "));
                    to.delete();
                    if (isDecoy.equals("true")) {
                        decoyDatabase.removeSingleDataVideo(model.getNewFilename());
                    } else {
                        moImageVideoDatabase.removeSingleDataVideo(model.getNewFilename());
                    }
                } else {
                    isStorageFull = true;
                }
            }
        }
        moItemFileList.clear();
        if (isDecoy.equals("true")) {
            moItemFileList = decoyDatabase.getAllDATAVideo(msPathToWriteDecoy);
        } else {
            moItemFileList = moImageVideoDatabase.getAllDATAVideos(msPathToWrite);
        }
    }

    private void addVideosFromGallery() {
        moResultsArralList.clear();

        SharedPrefs.save(HiddenVideosActivity.this, SharedPrefs.GallerySelected, "");
        Intent intent = new Intent(this, AlbumActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //  moResultsArralList = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert moResultsArralList != null;

                // show results in textview
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("Totally %d images selected:", moResultsArralList.size())).append("\n");

                Log.e("TAG:: ", "onActivityResult: " + "moResultsArralList size:: " + moResultsArralList.size());
                for (String result : moResultsArralList) {
                    File file = new File(result);

                    Log.e("TAG", "onActivityResult: File Path " + result);
                    Log.e("TAG", "onActivityResult: File name :: " + file.getName());
                    Log.e("TAG", "onActivityResult: File abs path :: " + file.getParent());

                    String filename1 = file.getName();
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_hhmmssSSS").format(new Date());

                    File from = new File(file.getParent(), filename1);

                    String fileNameExt = FilenameUtils.getExtension(filename1);
                    File to = new File(file.getParent(), "file" + timeStamp + "." + "zxcv");

                    from.renameTo(to);
                    Log.e("TAG:: ", "onActivityResult: " + to.getName());
                    if (isDecoy.equals("true")) {
                        copyPhotoTo(to.getAbsolutePath(), msPathToWriteDecoy, to.getName());
                    } else {
                        copyPhotoTo(to.getAbsolutePath(), msPathToWrite, to.getName());
                    }

                    itemModel oldSingleData;
                    if (isDecoy.equals("true")) {
                        oldSingleData = decoyDatabase.getOldSingleDataVideo(filename1);
                    } else {
                        oldSingleData = moImageVideoDatabase.getOldSingleDataVideo(filename1);
                    }

                    String s = oldSingleData.getStatus();
                    oldSingleData.setStatus("0");
                    oldSingleData.setNewFilename(to.getName());
                    if (s != null && s.equals("1")) {
                        if (isDecoy.equals("true")) {
                            decoyDatabase.updateSingleDataVideo(oldSingleData);
                        } else {
                            moImageVideoDatabase.updateSingleDataVideo(oldSingleData);
                        }
                    } else {
                        if (isDecoy.equals("true")) {
                            decoyDatabase.addDataVideo(new itemModel("video", filename1, to.getName(), file.getParent() + "/", msPathToWriteDecoy, "0"));
                        } else {
                            moImageVideoDatabase.addDataVideos(new itemModel("video", filename1, to.getName(), file.getParent() + "/", msPathToWrite, "0"));
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

                    MediaScannerConnection.scanFile(HiddenVideosActivity.this, new String[]{file.getAbsolutePath()}, null, (s1, uri) -> Log.e("TAG", "onScanCompleted: "));

                    sb.append(result).append("\n");
                }

                loadInterAdAndGetVideos("activityResult");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void copyPhotoTo(String pathToWatch, String pathToWrite, String str) {
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
        // }
    }

//    @Override
//    public void onClick(int fiPosition, itemModel foItemModel, ImageView foIvcheckBox, boolean isselected) {
//        if (imageLongClick) {
//            if (isselected) {
//                if (count > 0) {
//                    count--;
//                }
//                if (count == 0) {
//                    imageLongClick = false;
//                }
//                Log.e("TAG::: ", "onClick: count:: dec " + count);
//
//            } else {
//                count++;
//                Log.e("TAG::: ", "onClick: count:: inc " + count);
//            }
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();

        //IronSource.onResume(this);

        isDecoy = SharedPrefs.getString(this, SharedPrefs.DecoyPassword, "false");
        moItemFileList.clear();
        if (isDecoy.equals("true")) {
            moItemFileList = decoyDatabase.getAllDATAVideo(msPathToWriteDecoy);
        } else {
            moItemFileList = moImageVideoDatabase.getAllDATAVideos(msPathToWrite);
        }

        if (moItemFileList.isEmpty()) {
            showNative();
            moLlEditControl.setVisibility(View.GONE);
            moLLNoVideos.setVisibility(View.VISIBLE);
            moRlShareDeleteSave.setVisibility(View.GONE);
            moTvEditControl.setText(getString(R.string.Edit));
        } else {
            findViewById(R.id.fl_adplaceholder).setVisibility(View.GONE);
            moLlEditControl.setVisibility(View.VISIBLE);
            moLLNoVideos.setVisibility(View.GONE);
        }

        if (moItemFileList != null && moImagesAdapter != null) {
            moImagesAdapter.onUpdate(moItemFileList);
        }

        if (Share.isclickedOnDone) {
            showNative();
            if (isDecoy.equals("false")) {
                if (!moResultsArralList.isEmpty()) {
                    SharedPrefs.save(HiddenVideosActivity.this, Share.RATE_APP_HIDE_COUNT, SharedPrefs.getInt(HiddenVideosActivity.this, Share.RATE_APP_HIDE_COUNT) + 1);
                }
            }
            if (Share.isNeedToAdShow(this)) {
                if (IntAdsHelper.isInterstitialLoaded()) {
                    isFromNewFiles = true;
                    IntAdsHelper.showInterstitialAd();
//                            TODO Admob Ads
//                            interstitial.show();
                } else {
                    isFromNewFiles = true;
                    if (loadVideos != null && loadVideos.getStatus() == android.os.AsyncTask.Status.RUNNING) {
                        return;
                    }
                    loadVideos = new LoadVideos();
                    loadVideos.execute();
                }
            } else {
                isFromNewFiles = true;
                if (loadVideos != null && loadVideos.getStatus() == android.os.AsyncTask.Status.RUNNING) {
                    return;
                }
                loadVideos = new LoadVideos();
                loadVideos.execute();
            }
        }

        IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
                String msg_for_me = intent.getStringExtra("some_msg");
                //log our message value
                Log.i("InchooTutorial", msg_for_me);
                if (msg_for_me.equals("1")) {
                    new CountDownTimer(1500, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            if (miItemCount != 0) {
                                if (moItemFileList.isEmpty()) {
                                    moLlEditControl.setVisibility(View.GONE);
                                    moLLNoVideos.setVisibility(View.VISIBLE);
                                    moRlShareDeleteSave.setVisibility(View.GONE);
                                    moTvEditControl.setText(getString(R.string.Edit));
                                    findViewById(R.id.fl_adplaceholder).setVisibility(View.VISIBLE);
                                } else {
                                    moLlEditControl.setVisibility(View.VISIBLE);
                                    moLLNoVideos.setVisibility(View.GONE);
                                }

                                moImagesAdapter = new ImagesMultiSelectAdapter(HiddenVideosActivity.this, moItemFileList);
                                moRvHiddenVideos.setAdapter(moImagesAdapter);
                                multiSelect();
                            } else {
                                Toast.makeText(HiddenVideosActivity.this, "Please select a video first.", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                            miItemCount = 0;
                            if (isStorageFull) {
                                Toast.makeText(HiddenVideosActivity.this, "Your Storage is full.", Toast.LENGTH_SHORT).show();
                            }
                            showNative();
                        }
                    }.start();
                } else if (msg_for_me.equals("3")) {
                    dialog.dismiss();
                    Intent Shareintent = new Intent()
                            .setAction(Intent.ACTION_SEND_MULTIPLE)
                            .setType("video/*")
                            .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            .putParcelableArrayListExtra(Intent.EXTRA_STREAM, moVideoList);
                    startActivity(Intent.createChooser(Shareintent, HiddenVideosActivity.this.getString(R.string.share_intent_images)));
                }
            }
        };
        //registering receiver
        this.registerReceiver(mReceiver, intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NativeAdvanceHelper.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub

        try {

            if (mReceiver != null) {
                this.unregisterReceiver(this.mReceiver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.onPause();

        //IronSource.onPause(this);

        //unregister our receiver

    }

    private void callSelectedVideos() {
        try {
            moResultsArralList = Share.selected_image_list;
            assert moResultsArralList != null;

            Log.e("TAG:: ", "callSelectedVideos: " + "moResultsArralList size:: " + moResultsArralList.size());
            int i = 0;
            for (String result : moResultsArralList) {
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

                if (isDecoy.equals("true")) {
                    copyPhotoTo(to.getAbsolutePath(), msPathToWriteDecoy, to.getName());
                } else {
                    copyPhotoTo(to.getAbsolutePath(), msPathToWrite, to.getName());
                }

                // Creating Reminder
                itemModel oldSingleData = null;
                String s = null;
                try {
                    if (isDecoy.equals("true")) {
                        oldSingleData = decoyDatabase.getOldSingleDataVideo(filename1);
                    } else {
                        oldSingleData = moImageVideoDatabase.getOldSingleDataVideo(filename1);
                    }
                    s = oldSingleData.getStatus();
                    oldSingleData.setStatus("0");
                    oldSingleData.setNewFilename(to.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (s != null && s.equals("1")) {
                    Log.e("TAG:: ", "callSelectedVideos: " + " oldsingledata present:: ");
                    if (isDecoy.equals("true")) {
                        decoyDatabase.updateSingleDataVideo(oldSingleData);
                    } else {
                        moImageVideoDatabase.updateSingleDataVideo(oldSingleData);
                    }
                } else {
                    Log.e("TAG:: ", "callSelectedVideos: " + "Add Data:: ");
                    if (isDecoy.equals("true")) {
                        decoyDatabase.addDataVideo(new itemModel("image", filename1, to.getName(), file.getParent() + "/", msPathToWriteDecoy, "0"));
                    } else {
                        moImageVideoDatabase.addDataVideos(new itemModel("video", filename1, to.getName(), file.getParent() + "/", msPathToWrite, "0"));
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

                MediaScannerConnection.scanFile(HiddenVideosActivity.this, new String[]{file.getAbsolutePath()}, null, (s1, uri) -> Log.e("TAG", "onScanCompleted: "));
            }
        } catch (Exception e) {
            Log.e(TAG, "callSelectedVideos: " + e.toString());
        }
    }

    public class LoadVideos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e("TAG:: ", "doInBackground: " + "Share.selected_image_list:: " + Share.selected_image_list.size());
            try {
                callSelectedVideos();
                Share.selected_image_list.clear();
                moItemFileList.clear();
                if (isDecoy.equals("true")) {
                    moItemFileList = decoyDatabase.getAllDATAVideo(msPathToWriteDecoy);
                } else {
                    moItemFileList = moImageVideoDatabase.getAllDATAVideos(msPathToWrite);
                }
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (moItemFileList.isEmpty()) {
                moLlEditControl.setVisibility(View.GONE);
                moLLNoVideos.setVisibility(View.VISIBLE);
                moRlShareDeleteSave.setVisibility(View.GONE);
                moTvEditControl.setText(getString(R.string.Edit));
            } else {
                moLlEditControl.setVisibility(View.VISIBLE);
                moLLNoVideos.setVisibility(View.GONE);
            }

            if (isDecoy.equals("false")) {
                if (isFromNewFiles) {
                    if (!SharedPrefs.getBoolean(HiddenVideosActivity.this, Share.IS_RATED)) {
                        if (SharedPrefs.getInt(HiddenVideosActivity.this, Share.RATE_APP_HIDE_COUNT) >= 2) {
                            RatingDialog.SmileyRatingDialog(HiddenVideosActivity.this);
                        }
                    }
                }
            }

            if (isFromNewFiles) {
                findViewById(R.id.fl_adplaceholder).setVisibility(View.GONE);
            } else {
                showNative();
            }

            moImagesAdapter = new ImagesMultiSelectAdapter(HiddenVideosActivity.this, moItemFileList);
            moRvHiddenVideos.setLayoutManager(moGridLayoutManager);
            moRvHiddenVideos.setAdapter(moImagesAdapter);
            multiSelect();
            dialog.dismiss();
        }
    }

    public class deelteVideos extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (moDialog != null && !moDialog.isShowing()) {
                moDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e("TAG:: ", "doInBackground: " + "Share.selected_image_list:: " + Share.selected_image_list.size());
            deleteVideosFromGallery();
            Share.selected_image_list.clear();
            moItemFileList.clear();
            if (isDecoy.equals("true")) {
                moItemFileList = decoyDatabase.getAllDATAVideo(msPathToWriteDecoy);
            } else {
                moItemFileList = moImageVideoDatabase.getAllDATAVideos(msPathToWrite);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            moImagesAdapter = new ImagesMultiSelectAdapter(HiddenVideosActivity.this, moItemFileList);
            moRvHiddenVideos.setLayoutManager(moGridLayoutManager);
            moRvHiddenVideos.setAdapter(moImagesAdapter);
            multiSelect();
            if (moDialog != null && moDialog.isShowing()) {
                moDialog.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private class shareVideos extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (dialog != null && !dialog.isShowing()) {
                dialog.dismiss();
                dialog.show();
            }
            Log.e("TAG123", "onPreExecute: -------------->");
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            return shareVideos(moPositions);
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            if (aVoid) {
                Intent i = new Intent("android.intent.action.MAIN").putExtra("some_msg", "3");
                HiddenVideosActivity.this.sendBroadcast(i);
            } else {
                Toast.makeText(HiddenVideosActivity.this, "Something went wrong, Please try again...", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private boolean shareVideos(ArrayList<Integer> positions) {
        moVideoList = new ArrayList<>();


        try {
            for (int position : positions) {

                File file = new File(moItemFileList.get(position).getNewFilepath() + moItemFileList.get(position).getNewFilename());
                copyPhotoTo(file.getAbsolutePath(), "/storage/emulated/0/.androidData/.log/.dup/", moItemFileList.get(position).getOldFilename());
                moVideoList.add(FileProvider.getUriForFile(
                        this, getPackageName() +
                                ".provider",
                        new File("/storage/emulated/0/.androidData/.log/.dup/" + moItemFileList.get(position).getOldFilename())
                ));
            }

            return true;
        } catch (Exception e) {
            Log.e(TAG, "shareVideos: " + e.toString());
        }

        return false;

    }

    // Converting dp to pixel
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void showNative() {
        if (moItemFileList.isEmpty()) {
            if (Share.isNeedToAdShow(HiddenVideosActivity.this)) {
                NativeAdvanceHelper.loadNativeAd(HiddenVideosActivity.this, findViewById(R.id.fl_adplaceholder), false);
            }
        } else {
            findViewById(R.id.fl_adplaceholder).setVisibility(View.GONE);
        }
    }
}