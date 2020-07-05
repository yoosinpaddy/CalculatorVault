package com.calculator.vault.gallery.locker.hide.data.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.activity.ShowBrowserActivity;
import com.calculator.vault.gallery.locker.hide.data.common.OnSingleClickListener;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.google.gson.Gson;

import java.util.ArrayList;

public class BookmarkPopupAdapter extends RecyclerView.Adapter<BookmarkPopupAdapter.BookmarkPopupViewHolder>{

    private Activity context;
    private ArrayList<String> list;
    private OnBookmarkClickListener listener;

    public BookmarkPopupAdapter(Activity context, ArrayList<String> list, OnBookmarkClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookmarkPopupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_popup_dialog, parent, false);
        return new BookmarkPopupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookmarkPopupViewHolder holder, final int position) {

        holder.tvName.setText(list.get(position));
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (list.size()>position) {
//
//                }
//            }
//        });

        holder.itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                listener.onBookmarkClicked(list.get(holder.getAdapterPosition()));
            }
        });

        holder.ivRemove.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                list.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());

                String newList = new Gson().toJson(list);
                SharedPrefs.save(context, SharedPrefs.BOOKMARK_LIST, newList);
                if (list.isEmpty()) {
                    ((ShowBrowserActivity) context).showEmptyBookmark();
                }
            }
        });

//        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                list.remove(holder.getAdapterPosition());
//                notifyItemRemoved(holder.getAdapterPosition());
//
//                String newList = new Gson().toJson(list);
//                SharedPrefs.save(context, SharedPrefs.BOOKMARK_LIST, newList);
//                if (list.isEmpty()) {
//                    ((ShowBrowserActivity) context).showEmptyBookmark();
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class BookmarkPopupViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private ImageView ivRemove;
        public BookmarkPopupViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.itemPopup_tv_name);
            ivRemove = itemView.findViewById(R.id.itemPopup_iv_close);

        }
    }

    public interface OnBookmarkClickListener{
        void onBookmarkClicked(String url);
    }

}
