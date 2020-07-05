package com.calculator.vault.gallery.locker.hide.data.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.adapter.PhoneAlbumImagesAdapter;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.customs.DragSelectTouchListener;
import com.calculator.vault.gallery.locker.hide.data.customs.DragSelectionProcessor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by admin on 11/18/2016.
 */
public class AlbumImagesActivity extends AppCompatActivity implements View.OnClickListener {
    public static AlbumImagesActivity activity;
    private GridLayoutManager moGridLayoutManager;
    private RecyclerView moRcvAlbumImages;

    //private FirebaseAnalytics mFirebaseAnalytics;
    //private AdView mAdView;
    private ImageView moIvMoreApp, moIvBlast;
    public static boolean isInForeGround;
    public static AlbumImagesActivity moActivity;
    private LinearLayout moBtnDoneclick;
    private ImageView iv_back;

    // Multi Select
    private DragSelectionProcessor.Mode mMode = DragSelectionProcessor.Mode.FirstItemDependent;
    private DragSelectTouchListener mDragSelectTouchListener;
    private DragSelectionProcessor mDragSelectionProcessor;
    private PhoneAlbumImagesAdapter moAlbumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albumimages);
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        moActivity = AlbumImagesActivity.this;


        if (Share.RestartApp(AlbumImagesActivity.this)) {
            initView();
            //TODO:Update
            /*if (NetworkManager.isInternetConnected(AlbumImagesActivity.this)) {
                adsBanner();
            } else {
                mAdView.setVisibility(View.GONE);
            }*/
        }
    }

    private void initView() {

        moBtnDoneclick = findViewById(R.id.btn_doneclick);
        iv_back = findViewById(R.id.iv_back);
        moBtnDoneclick.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        String gallerytype = SharedPrefs.getString(AlbumImagesActivity.this, SharedPrefs.GallerySelected);

        if (gallerytype.equals("gallery")) {
            moBtnDoneclick.setVisibility(View.INVISIBLE);
        } else {
            moBtnDoneclick.setVisibility(View.VISIBLE);
        }

        String albumName = "";
        try {
            albumName = getIntent().getExtras().getString(Share.KEYNAME.ALBUM_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            moRcvAlbumImages = findViewById(R.id.rcv_album_images);
            moGridLayoutManager = new GridLayoutManager(AlbumImagesActivity.this, 3);
            // mAdView = (AdView) findViewById(R.id.adView);
            moRcvAlbumImages.setLayoutManager(moGridLayoutManager);
//            ArrayList<String> al_album_images = getIntent().getStringArrayListExtra("image_list");
            ArrayList<String> al_album_images = Share.moImageList;
            Share.moImageList = new ArrayList<>();
            moAlbumAdapter = new PhoneAlbumImagesAdapter(this, al_album_images, albumName);
            moRcvAlbumImages.setAdapter(moAlbumAdapter);

            moAlbumAdapter.setClickListener(new PhoneAlbumImagesAdapter.ItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    String typeImage = SharedPrefs.getString(AlbumImagesActivity.this, SharedPrefs.GallerySelected);

                    if (typeImage.equals("gallery")) {
                        File f = new File(al_album_images.get(position));
                        if (f.exists()) {
                            Share.BG_GALLERY = Uri.parse("file:///" + al_album_images.get(position));
                            Share.imageUrl = al_album_images.get(position);
                            Intent intent = new Intent(AlbumImagesActivity.this, CropImageActivity.class);
                            intent.putStringArrayListExtra("image_list", al_album_images);
                            intent.putExtra(Share.KEYNAME.ALBUM_NAME, al_album_images);
                            if (((AlbumActivity.getFaceActivity()).getIntent().hasExtra("moActivity"))) {
                                intent.putExtra("moActivity", "PhotoAlbum");
                            }
                            intent.putExtra("is_from", true);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        moAlbumAdapter.toggleSelection(position);
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
                    return moAlbumAdapter.getSelection();
                }

                @Override
                public boolean isSelected(int index) {
                    return moAlbumAdapter.getSelection().contains(index);
                }

                @Override
                public void updateSelection(int start, int end, boolean isSelected, boolean calledFromOnStart) {
                    moAlbumAdapter.selectRange(start, end, isSelected);
                }
            }).withMode(mMode);

            mDragSelectTouchListener = new DragSelectTouchListener().withSelectListener(mDragSelectionProcessor);

            moRcvAlbumImages.addOnItemTouchListener(mDragSelectTouchListener);


            TextView tv_title = findViewById(R.id.tv_title);
            tv_title.setText(albumName);

            //ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
            iv_back.setOnClickListener(v -> onBackPressed());
        }
    }

    //TODO:Update
    public void initShareIntent(View view) {
        //AccountRedirectActivity.get_url(AlbumImagesActivity.this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Share.selected_image_list.clear();
        if (Share.RestartApp(AlbumImagesActivity.this)) {
            isInForeGround = true;
        }
    }

    @Override
    public void onDestroy() {
        isInForeGround = false;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Share.selected_image_list.clear();
        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();
        isInForeGround = false;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_doneclick:
                try {
                    Share.isclickedOnDone = true;
                    AlbumActivity.getFaceActivity().finish();
                    finish();
                } catch (Exception e) {
                    Log.e("TAG", "onClick: "+e.toString() );
                }
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }
}
