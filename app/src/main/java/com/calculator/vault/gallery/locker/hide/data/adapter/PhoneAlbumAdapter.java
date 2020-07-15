package com.calculator.vault.gallery.locker.hide.data.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.calculator.vault.gallery.locker.hide.data.activity.AlbumActivity;
import com.calculator.vault.gallery.locker.hide.data.activity.AlbumImagesActivity;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.model.PhoneAlbum;
import com.calculator.vault.gallery.locker.hide.data.common.Share;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by admin on 11/18/2016.
 */
public class PhoneAlbumAdapter extends RecyclerView.Adapter<PhoneAlbumAdapter.ViewHolder> {
    Context context;
    ArrayList<String> al_image = new ArrayList<>();
    private List<PhoneAlbum> al_album = new ArrayList<>();
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    Activity act;

    public PhoneAlbumAdapter(Context context, Vector<PhoneAlbum> al_album) {
        this.context = context;
        this.al_album = al_album;
        act = (Activity) context;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.progress_animation)
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_album_name;
        ImageView iv_album_image;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_album_name = (TextView) itemView.findViewById(R.id.tv_album_name);
            iv_album_image = (ImageView) itemView.findViewById(R.id.iv_album_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phone_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

        DisplayMetrics dm = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        Share.screenHeight = dm.heightPixels;

//        ImageLoader.getInstance().displayImage("file:///" + al_album.get(position).getCoverUri(), holder.iv_album_image, options);

        Glide.with(context).load(al_album.get(position).getCoverUri()).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.iv_album_image);

        //holder.iv_album_image.getLayoutParams().height = Share.screenHeight / 4;
        holder.iv_album_image.getLayoutParams().height =  holder.iv_album_image.getLayoutParams().width;
        holder.tv_album_name.setText(al_album.get(position).getName());

        holder.itemView.setOnClickListener(view -> {
            al_image.clear();
            Log.e("TAG", "Images====>" + al_album.get(position).getAlbumPhotos().size());
            for (int i = 0; i < al_album.get(position).getAlbumPhotos().size(); i++) {
                al_image.add(al_album.get(position).getAlbumPhotos().get(i).getPhotoUri());
                Log.e("uri===>", "onClick: " + al_album.get(position).getAlbumPhotos().get(i).getPhotoUri());
            }
            Log.e("TAG", "acrtivity from1111:==>" + ((AlbumActivity.getFaceActivity()).getIntent().hasExtra("moActivity")));

            Intent intent = new Intent(context, AlbumImagesActivity.class);
            Share.moImageList = al_image;
            //intent.putStringArrayListExtra("image_list", al_image);
            intent.putExtra(Share.KEYNAME.ALBUM_NAME, al_album.get(position).getName());
            if (((AlbumActivity.getFaceActivity()).getIntent().hasExtra("moActivity"))) {
                intent.putExtra("moActivity", "PhotoAlbum");
            }
//                act.finish();
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return al_album.size();
    }
}
