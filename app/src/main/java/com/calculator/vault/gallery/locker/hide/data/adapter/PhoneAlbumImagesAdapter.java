package com.calculator.vault.gallery.locker.hide.data.adapter;

import android.app.Activity;
import android.graphics.Bitmap;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.model.itemImageModel;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by admin on 11/18/2016.
 */
public class PhoneAlbumImagesAdapter extends RecyclerView.Adapter<PhoneAlbumImagesAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<String> al_album = new ArrayList<>();
    private ArrayList<itemImageModel> itemImages = new ArrayList<>();
    private DisplayImageOptions options;
    private String album_name;
    private String pickFromGallery;

    private int mDataSize;
    private ItemClickListener mClickListener;
    private HashSet<Integer> mSelected;

    public PhoneAlbumImagesAdapter(Activity activity, ArrayList<String> al_album, String albumName) {
        this.activity = activity;
        this.al_album = al_album;
        this.album_name = albumName;
        pickFromGallery = SharedPrefs.getString(activity, SharedPrefs.PickFromGallery);

        mDataSize = al_album.size();
        mSelected = new HashSet<>();

        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.progress_animation).cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tv_album_name;
        ImageView iv_album_image;
        ImageView checkbox, play_iv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_album_name = itemView.findViewById(R.id.tv_album_name);
            iv_album_image = itemView.findViewById(R.id.iv_album_image);
            checkbox = itemView.findViewById(R.id.checkbox);
            play_iv = itemView.findViewById(R.id.play_iv);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if (mClickListener != null)
                return mClickListener.onItemLongClick(view, getAdapterPosition());
            return false;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phone_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //holder.setIsRecyclable(false);
        holder.tv_album_name.setVisibility(View.GONE);

        if (mSelected.contains(position))
            holder.checkbox.setVisibility(View.VISIBLE);
        else
            holder.checkbox.setVisibility(View.GONE);

        if (pickFromGallery.equalsIgnoreCase("pickimage")) {
            holder.play_iv.setVisibility(View.GONE);
        } else {
            holder.play_iv.setVisibility(View.VISIBLE);
        }
        try {
            ImageLoader.getInstance().displayImage("file:///" + al_album.get(position), holder.iv_album_image, options);
        } catch (Exception e) {
            Log.e("PhoneAlbumImagesAdapter", "onBindViewHolder: "+e.toString() );
            holder.iv_album_image.setImageResource(R.drawable.img_placeholder);
        }

//        if (Share.selected_image_list.contains(al_album.get(position))){
//            holder.checkbox.setVisibility(View.VISIBLE);
//        }else {
//            holder.checkbox.setVisibility(View.GONE);
//        }

//        DisplayMetrics dm = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;
//        Share.screenHeight = dm.heightPixels;
//        final itemImageModel itemImageModel = new itemImageModel();
//        itemImageModel.setImagePath(al_album.get(position));
//        itemImages.add(itemImageModel);
//        holder.iv_album_image.getLayoutParams().height = Share.screenHeight / 5;

//        holder.itemView.setOnClickListener(view -> {
//
//            String typeImage = SharedPrefs.getString(activity, SharedPrefs.GallerySelected);
//
//            if (typeImage.equals("gallery")) {
//                File f = new File(al_album.get(position));
//                if (f.exists()) {
//                    Share.BG_GALLERY = Uri.parse("file:///" + al_album.get(position));
//                    Share.imageUrl = al_album.get(position);
//                    Intent intent = new Intent(activity, CropImageActivity.class);
//                    intent.putStringArrayListExtra("image_list", al_album);
//                    intent.putExtra(Share.KEYNAME.ALBUM_NAME, album_name);
//                    if (((AlbumActivity.getFaceActivity()).getIntent().hasExtra("moActivity"))) {
//                        intent.putExtra("moActivity", "PhotoAlbum");
//                    }
//                    intent.putExtra("is_from", true);
//                    activity.startActivity(intent);
//                    activity.finish();
//                }
//            } else {
//                if (itemImages.get(position).isSelected()) {
//                    holder.checkbox.setVisibility(View.INVISIBLE);
//                    itemImages.get(position).setSelected(false);
//                    Share.selected_image_list.remove(al_album.get(position));
//                    Log.e("HELLO", "onClick: REMOVE: " + Share.selected_image_list.toString());
//                } else {
//                    holder.checkbox.setVisibility(View.VISIBLE);
//                    itemImages.get(position).setSelected(true);
//                    Share.selected_image_list.add(al_album.get(position));
//                    Log.e("HELLO", "onClick: ADD: " + Share.selected_image_list.toString());
//                }
//
//                File f = new File(al_album.get(position));
//                if (f.exists()) {
//
//                    Share.BG_GALLERY = Uri.parse("file:///" + al_album.get(position));
//                    Log.e("TAG:: ", "onClick: size::" + Share.selected_image_list.size());
//                } else {
//                    Toast.makeText(activity, "Item Not Found", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return al_album.size();
    }

    public void toggleSelection(int pos) {
        if (mSelected.contains(pos)) {
            mSelected.remove(pos);
            //itemImages.get(pos).setSelected(false);
            Share.selected_image_list.remove(al_album.get(pos));
        } else {
            mSelected.add(pos);
            //itemImages.get(pos).setSelected(true);
            Share.selected_image_list.add(al_album.get(pos));
        }
        notifyItemChanged(pos);
    }

    public void select(int pos, boolean selected) {
        if (selected) {
            mSelected.add(pos);
           // itemImages.get(pos).setSelected(true);
            Share.selected_image_list.add(al_album.get(pos));
        } else {
            mSelected.remove(pos);
            //itemImages.get(pos).setSelected(false);
            Share.selected_image_list.remove(al_album.get(pos));
        }
        notifyItemChanged(pos);
    }

    public void selectRange(int start, int end, boolean selected) {
        for (int i = start; i <= end; i++) {
            if (selected) {
                mSelected.add(i);
                //itemImages.get(i).setSelected(true);
                Share.selected_image_list.add(al_album.get(i));
            } else {
                mSelected.remove(i);
               // itemImages.get(i).setSelected(false);
                Share.selected_image_list.remove(al_album.get(i));
            }
        }
        notifyItemRangeChanged(start, end - start + 1);
    }

    public void deselectAll() {
        // this is not beautiful...
        mSelected.clear();
        notifyDataSetChanged();
    }

    public void selectAll() {
        for (int i = 0; i < mDataSize; i++)
            mSelected.add(i);
        notifyDataSetChanged();
    }

    public int getCountSelected() {
        return mSelected.size();
    }

    public HashSet<Integer> getSelection() {
        return mSelected;
    }

    // ----------------------
    // Click Listener
    // ----------------------

    public void setClickListener(ItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);

        boolean onItemLongClick(View view, int position);
    }
}