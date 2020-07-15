package com.calculator.vault.gallery.locker.hide.data.fragment;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.adapter.PhoneAlbumAdapter;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.model.PhoneAlbum;
import com.calculator.vault.gallery.locker.hide.data.model.PhonePhoto;

import java.io.File;
import java.util.Objects;
import java.util.Vector;

import static com.applovin.sdk.AppLovinSdkUtils.runOnUiThread;

/**
 * Created by Harshad Kathiriya on 11/11/2016.
 */
public class PhotoFragment extends Fragment {
    private static final String TAG = PhotoFragment.class.getSimpleName();

    Vector<PhoneAlbum> moVectorPhoneAlbums = new Vector<>();
    Vector<String> msVecAlbumsNames = new Vector<>();
    private GridLayoutManager moGridLayoutManager;
    private RecyclerView moRcvAlbum;
    private PhoneAlbumAdapter moAlbumAdapter;
    private String msPickFromGallery;
    private TextView moTvError;
    private Dialog dialog;
//    private FirebaseAnalytics mFirebaseAnalytics;

    public static PhotoFragment newInstance() {
        Bundle args = new Bundle();
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
/*        if (Share.RestartApp(getActivity())) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());*/
        initView(view);
        if (msPickFromGallery.equalsIgnoreCase("pickimage")) {
            moTvError.setText("No Images Available.");
            //initViewActionImages();
        } else {
            moTvError.setText("No Videos Available.");
//            initViewActionVideos();
        }
        new loadImages().execute();

//        }
        return view;
    }

    public class loadImages extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = Share.showProgress1(getActivity(), "Loading...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (msPickFromGallery.equalsIgnoreCase("pickimage")) {
                initViewActionImages();
            } else {
                initViewActionVideos();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if (moVectorPhoneAlbums.size() == 0) {
                moRcvAlbum.setVisibility(View.GONE);
                moTvError.setVisibility(View.VISIBLE);
            } else {
                moRcvAlbum.setVisibility(View.VISIBLE);
                moTvError.setVisibility(View.GONE);
            }

            try{
                dialog.dismiss();
            }catch (Exception ignored){}
        }
    }

    private void initView(View view) {
        msPickFromGallery = SharedPrefs.getString(getActivity(), SharedPrefs.PickFromGallery);

        Log.e(TAG, "initView: phonealbum" + moVectorPhoneAlbums.size());
        moVectorPhoneAlbums.clear();
        msVecAlbumsNames.clear();
        moTvError = view.findViewById(R.id.tvError);
        moRcvAlbum = view.findViewById(R.id.rcv_album);
        moGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        moRcvAlbum.setLayoutManager(moGridLayoutManager);
        moAlbumAdapter = new PhoneAlbumAdapter(getActivity(), moVectorPhoneAlbums);
        moRcvAlbum.setAdapter(moAlbumAdapter);
        if (moVectorPhoneAlbums.size() == 0) {
            moRcvAlbum.setVisibility(View.GONE);
            moTvError.setVisibility(View.VISIBLE);
        } else {
            moRcvAlbum.setVisibility(View.VISIBLE);
            moTvError.setVisibility(View.GONE);
        }
    }

    private void initViewActionImages() {

        try {
            // which image properties are we querying
            String BUCKET_ORDER_BY = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
//            String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
            String BUCKET_GROUP_BY = "";
            // which image properties are we querying
            String[] projection = new String[]{
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID
            };

            // content: style URI for the "primary" external storage volume
            Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            // Make the query.
            Cursor cur = getActivity().getContentResolver().query(images,
                    projection, // Which columns to return
                    BUCKET_GROUP_BY,       // Which rows to return (all rows)
                    null,       // Selection arguments (none)
                    BUCKET_ORDER_BY         // Ordering
            );

            if (cur != null && cur.getCount() > 0) {
                Log.i("DeviceImageManager", " query count=" + cur.getCount());

                if (cur.moveToFirst()) {
                    String bucketName;
                    String data;
                    String imageId;
                    int bucketNameColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                    int imageUriColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);
                    int imageIdColumn = cur.getColumnIndex(MediaStore.Images.Media._ID);

                    do {
                        // Get the field values
                        bucketName = cur.getString(bucketNameColumn);
                        data = cur.getString(imageUriColumn);
                        imageId = cur.getString(imageIdColumn);

                        // Adding a new PhonePhoto object to phonePhotos vector
                        PhonePhoto phonePhoto = new PhonePhoto();
                        phonePhoto.setAlbumName(bucketName);
                        phonePhoto.setPhotoUri(data);
                        phonePhoto.setId(Integer.valueOf(imageId));

                        if (msVecAlbumsNames.contains(bucketName)) {
                            for (PhoneAlbum album : moVectorPhoneAlbums) {
                                if (album.getName().equals(bucketName)) {

                                    // TODO: 15/11/18 if condition is for hide deleted images from our app gallery
                                    if (new File(data).length() != 0) {

                                        // TODO: 29/03/19 To prevent to add corrupted photo files to our app gallery
                                        BitmapFactory.Options options = new BitmapFactory.Options();
                                        options.inJustDecodeBounds = true;

                                        BitmapFactory.decodeFile(new File(data).getAbsolutePath(), options);
                                        int imageHeight = options.outHeight;
                                        int imageWidth = options.outWidth;

                                        if (imageHeight > 0 && imageWidth > 0)
                                            album.getAlbumPhotos().add(phonePhoto);

                                        Log.i("DeviceImageManager", "A photo was added to album => " + bucketName);
                                    } else {
                                        Log.e("initViewAction: ", "data --> " + data + " size --> " + new File(data).length());
                                    }
                                    break;
                                }
                            }
                        } else {

                            // TODO: 15/11/18 if condition is for prevent to add deleted images from our app gallery
                            if (new File(data).length() != 0) {

                                PhoneAlbum album = new PhoneAlbum();
                                Log.i("DeviceImageManager", "A new album was created => " + bucketName);

                                album.setId(phonePhoto.getId());
                                album.setName(bucketName);
                                album.setCoverUri(phonePhoto.getPhotoUri());

                                album.getAlbumPhotos().add(phonePhoto);
                                Log.i("DeviceImageManager", "A photo was added to album => " + bucketName);

                                moVectorPhoneAlbums.add(album);
                                msVecAlbumsNames.add(bucketName);
                            }
                        }

                    } while (cur.moveToNext());

                    /*if (phoneAlbums != null && phoneAlbums.size() > 0) {
                        albumAdapter.notifyDataSetChanged();
                    } else {
                        rcv_album.setVisibility(View.GONE);
                        iv_no_photo.setVisibility(View.VISIBLE);
                    }*/
                }

                if (cur != null)
                    cur.close();

            } else {

                /*rcv_album.setVisibility(View.GONE);
                iv_no_photo.setVisibility(View.VISIBLE);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    private void initViewActionImages() {
//        try {
//            // which image properties are we querying
//            String loBUCKET_ORDER_BY = MediaStore.Images.Media.DATE_MODIFIED + " DESC";
//            String loBUCKET_GROUP_BY = "1) GROUP BY 1,(2";
//            // which image properties are we querying
//            String[] loProjection = new String[]{MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
//
//            // content: style URI for the "primary" external storage volume
//            Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//
//            // Make the query.
//            Cursor cur = getActivity().getContentResolver().query(images, loProjection, // Which columns to return
//                    loBUCKET_GROUP_BY,       // Which rows to return (all rows)
//                    null,       // Selection arguments (none)
//                    loBUCKET_ORDER_BY         // Ordering
//            );
//
//            if (cur != null && cur.getCount() > 0) {
//                Log.i("DeviceImageManager", " query count=" + cur.getCount());
//
//                if (cur.moveToFirst()) {
//                    String bucketName;
//                    String data;
//                    String imageId;
//                    int bucketNameColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
//                    int imageUriColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);
//                    int imageIdColumn = cur.getColumnIndex(MediaStore.Images.Media._ID);
//
//                    do {
//                        // Get the field values
//                        bucketName = cur.getString(bucketNameColumn);
//                        data = cur.getString(imageUriColumn);
//                        imageId = cur.getString(imageIdColumn);
//
//                        // Adding a new PhonePhoto object to phonePhotos vector
//                        PhonePhoto phonePhoto = new PhonePhoto();
//                        phonePhoto.setAlbumName(bucketName);
//                        phonePhoto.setPhotoUri(data);
//                        phonePhoto.setId(Integer.valueOf(imageId));
//
//                        if (msVecAlbumsNames.contains(bucketName)) {
//                            for (PhoneAlbum album : moVectorPhoneAlbums) {
//                                if (album.getName().equals(bucketName)) {
//                                    album.getAlbumPhotos().add(phonePhoto);
//                                    Log.i("DeviceImageManager", "A photo was added to album => " + bucketName + "PATH===>" + data);
//                                    break;
//                                }
//                            }
//                        } else {
//                            PhoneAlbum album = new PhoneAlbum();
//                            Log.i("DeviceImageManager", "A new album was created => " + bucketName + "PATH==>" + data);
//                            album.setId(phonePhoto.getId());
//                            album.setName(bucketName);
//                            album.setCoverUri(phonePhoto.getPhotoUri());
//                            album.getAlbumPhotos().add(phonePhoto);
//                            Log.i("DeviceImageManager", "A photo was added to album => " + bucketName + "PATH==>" + data);
//                            moVectorPhoneAlbums.add(album);
//                            msVecAlbumsNames.add(bucketName);
//                        }
//                        if (moVectorPhoneAlbums.size() == 0){
//                            moRcvAlbum.setVisibility(View.GONE);
//                            moTvError.setVisibility(View.VISIBLE);
//                        }else {
//                            moRcvAlbum.setVisibility(View.VISIBLE);
//                            moTvError.setVisibility(View.GONE);
//                            moAlbumAdapter.notifyDataSetChanged();
//                        }
//                    } while (cur.moveToNext());
//                }
//
//                cur.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    private void initViewActionVideos() {
        try {
            // which image properties are we querying
            String BUCKET_ORDER_BY = MediaStore.Video.Media.DATE_MODIFIED + " DESC";
//            String BUCKET_GROUP_BY = "1) GROUP BY 1,(2";
            String BUCKET_GROUP_BY = "";
            // which image properties are we querying
            String[] projection = new String[]{MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID};

            // content: style URI for the "primary" external storage volume
            Uri images = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

            // Make the query.
            Cursor cur = getActivity().getContentResolver().query(images, projection, // Which columns to return
                    BUCKET_GROUP_BY,       // Which rows to return (all rows)
                    null,       // Selection arguments (none)
                    BUCKET_ORDER_BY         // Ordering
            );

            if (cur != null && cur.getCount() > 0) {
                Log.i("DeviceImageManager", " query count=" + cur.getCount());

                if (cur.moveToFirst()) {
                    String bucketName;
                    String data;
                    String imageId;
                    int bucketNameColumn = cur.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
                    int imageUriColumn = cur.getColumnIndex(MediaStore.Video.Media.DATA);
                    int imageIdColumn = cur.getColumnIndex(MediaStore.Video.Media._ID);

                    do {
                        // Get the field values
                        bucketName = cur.getString(bucketNameColumn);
                        data = cur.getString(imageUriColumn);
                        imageId = cur.getString(imageIdColumn);

                        // Adding a new PhonePhoto object to phonePhotos vector
                        PhonePhoto phonePhoto = new PhonePhoto();
                        phonePhoto.setAlbumName(bucketName);
                        phonePhoto.setPhotoUri(data);
                        phonePhoto.setId(Integer.valueOf(imageId));

                        if (msVecAlbumsNames.contains(bucketName)) {
                            for (PhoneAlbum album : moVectorPhoneAlbums) {
                                if (album.getName().equals(bucketName)) {

                                    album.getAlbumPhotos().add(phonePhoto);
                                    Log.i("DeviceImageManager", "A photo was added to album => " + bucketName + "PATH===>" + data);
                                    break;
                                }
                            }
                        } else {
                            PhoneAlbum album = new PhoneAlbum();
                            Log.i("DeviceImageManager", "A new album was created => " + bucketName + "PATH==>" + data);
                            album.setId(phonePhoto.getId());
                            album.setName(bucketName);
                            album.setCoverUri(phonePhoto.getPhotoUri());
                            album.getAlbumPhotos().add(phonePhoto);
                            Log.i("DeviceImageManager", "A photo was added to album => " + bucketName + "PATH==>" + data);
                            moVectorPhoneAlbums.add(album);
                            msVecAlbumsNames.add(bucketName);
                        }
                        if (moVectorPhoneAlbums.size() == 0) {
                            moRcvAlbum.setVisibility(View.GONE);
                            moTvError.setVisibility(View.VISIBLE);
                        } else {
                            moRcvAlbum.setVisibility(View.VISIBLE);
                            moTvError.setVisibility(View.GONE);
                            moAlbumAdapter.notifyDataSetChanged();
                        }

                    } while (cur.moveToNext());
                }

                cur.close();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (Share.RestartApp(getActivity())) {
        }*/
    }
}
