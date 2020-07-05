package com.calculator.vault.gallery.locker.hide.data.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.model.itemModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class ImagesMultiSelectAdapter extends RecyclerView.Adapter<ImagesMultiSelectAdapter.MyViewHolder> {
    private Activity moContext;
    private ArrayList<itemModel> moImageFILEList;
    private String TAG = this.getClass().getSimpleName();
    private String msPickFromGallery;

    private ItemClickListener mClickListener;
    private HashSet<Integer> mSelected;
    private int mDataSize;


    public ImagesMultiSelectAdapter(Activity foContext, ArrayList<itemModel> imagePathList) {
        this.moContext = foContext;
        this.moImageFILEList = imagePathList;
        msPickFromGallery = SharedPrefs.getString(moContext, SharedPrefs.PickFromGallery);

        mDataSize = imagePathList.size();
        mSelected = new HashSet<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.showimage, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder foHolder, int foPosition) {

        //foHolder.setIsRecyclable(true);

        if (msPickFromGallery.equalsIgnoreCase("pickimage")) {
            foHolder.loIvplay.setVisibility(View.GONE);
        } else {
            foHolder.loIvplay.setVisibility(View.VISIBLE);
        }

        File file = null;
        if (moImageFILEList != null && !moImageFILEList.isEmpty()) {
            file = new File(moImageFILEList.get(foPosition).getNewFilepath() + moImageFILEList.get(foPosition).getNewFilename());

            Glide.with(moContext)
                    .load(file)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .override(100, 100)
                    .centerCrop()
                    .into(foHolder.loIvHiddenImage);
        }

        if (moImageFILEList != null && !moImageFILEList.isEmpty()) {
            if (mSelected.contains(foPosition))
                foHolder.loCheckbox.setVisibility(View.VISIBLE);
            else
                foHolder.loCheckbox.setVisibility(View.INVISIBLE);
        }


        /*foHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null)
                    mClickListener.onItemClick(v, foPosition);
            }
        });

        foHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mClickListener != null)
                    mClickListener.onItemClick(v, foPosition);
                return false;
            }
        });*/
    }

    @Override
    public int getItemCount() {
        Log.e("TAG", "getItemCount: " + moImageFILEList.size());
        return moImageFILEList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView loIvHiddenImage, loIvplay, loCheckbox;

        public MyViewHolder(View itemView) {
            super(itemView);
            loIvHiddenImage = itemView.findViewById(R.id.iv_hiddenimage);
            loIvplay = itemView.findViewById(R.id.play_iv);
            loCheckbox = itemView.findViewById(R.id.checkbox);

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

    public void onUpdate(ArrayList<itemModel> itemModelArrayList) {
        Log.e(TAG, "onUpdate: " + "Notify");
        moImageFILEList.clear();
        moImageFILEList = itemModelArrayList;
        mSelected.clear();
        this.notifyDataSetChanged();
    }

    // Multi Select Methods

    public void toggleSelection(int pos) {
        if (mSelected.contains(pos)) {
            mSelected.remove(pos);
            moImageFILEList.get(pos).setSelected(false);
        } else {
            mSelected.add(pos);
            moImageFILEList.get(pos).setSelected(true);
        }
        notifyItemChanged(pos);
    }

    public void select(int pos, boolean selected) {
        if (selected) {
            mSelected.add(pos);
            moImageFILEList.get(pos).setSelected(true);
        } else {
            mSelected.remove(pos);
            moImageFILEList.get(pos).setSelected(false);
        }
        notifyItemChanged(pos);
    }

    public void selectRange(int start, int end, boolean selected) {
        for (int i = start; i <= end; i++) {
            if (selected) {
                mSelected.add(i);
                moImageFILEList.get(i).setSelected(true);
            } else {
                mSelected.remove(i);
                moImageFILEList.get(i).setSelected(false);
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

    @Override
    public long getItemId(int position) {
        return moImageFILEList.get(position).getID();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
