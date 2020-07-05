package com.calculator.vault.gallery.locker.hide.data.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.model.itemModel;

import java.util.ArrayList;
import java.util.HashSet;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.MyViewHolder> {
    private Activity moContext;
    private ArrayList<itemModel> moImageFILEList;
    private String TAG = this.getClass().getSimpleName();

    private ItemClickListener mClickListener;
    private HashSet<Integer> mSelected;
    private int mDataSize;


    public FilesAdapter(Activity foContext, ArrayList<itemModel> imagePathList) {
        this.moContext = foContext;
        this.moImageFILEList = imagePathList;

        mDataSize = imagePathList.size();
        mSelected = new HashSet<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.show_file_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder foHolder, int foPosition) {

        foHolder.loTvFileName.setText(moImageFILEList.get(foPosition).getOldFilename());

        if (moImageFILEList != null && !moImageFILEList.isEmpty()) {
            if (mSelected.contains(foPosition))
                foHolder.loCheckbox.setVisibility(View.VISIBLE);
            else
                foHolder.loCheckbox.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        Log.e("TAG", "getItemCount: " + moImageFILEList.size());
        return moImageFILEList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView loIvHiddenImage, loCheckbox;
        TextView loTvFileName;

        public MyViewHolder(View itemView) {
            super(itemView);
            loIvHiddenImage = itemView.findViewById(R.id.iv_hiddenimage);
            loTvFileName = itemView.findViewById(R.id.tv_file_name);
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

}
