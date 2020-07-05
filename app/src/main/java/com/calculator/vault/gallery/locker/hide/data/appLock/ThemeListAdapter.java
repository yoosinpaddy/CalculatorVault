package com.calculator.vault.gallery.locker.hide.data.appLock;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ThemeListAdapter extends RecyclerView.Adapter<ThemeListAdapter.ThemeListViewHolder>{

    private Activity activity;
    private ArrayList<ThemeModel> list;
    private String fromWhere;

    public ThemeListAdapter(Activity activity, ArrayList<ThemeModel> list, String fromWhere) {
        this.activity = activity;
        this.list = list;
        this.fromWhere = fromWhere;
    }

    @NonNull
    @Override
    public ThemeListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_theme, viewGroup, false);
        return new ThemeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThemeListViewHolder themeListViewHolder, int i) {

        Glide.with(activity).load(list.get(i).getThemeRes()).override(360,640).placeholder(R.drawable.ic_place_holder).into(themeListViewHolder.ivTheme);
        //Picasso.with(activity).load(list.get(i).getThemeRes()).resize(360, 640).into(themeListViewHolder.ivTheme);

        Log.e("TAG", "onBindViewHolder: RES_ID " + list.get(i).getOneRes());

        if (list.get(i).isSelected()){
            themeListViewHolder.ivSelection.setVisibility(View.VISIBLE);
        }else {
            themeListViewHolder.ivSelection.setVisibility(View.GONE);
        }

        themeListViewHolder.itemView.setOnClickListener(v -> {

            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getThemeRes() == list.get(i).getThemeRes()){
                    list.get(j).setSelected(true);
                }else {
                    list.get(j).setSelected(false);
                }
            }
            String newList = new Gson().toJson(list);
            if (fromWhere.equalsIgnoreCase("pattern")) {
                Preferences.setStringPref(activity, Preferences.KEY_SAVED_THEME_LIST, newList);
                Preferences.setIntPref(activity, Preferences.KEY_SAVED_THEME_SELECTION, themeListViewHolder.getAdapterPosition());
            }else {
                Preferences.setStringPref(activity, Preferences.KEY_SAVED_THEME_LIST_PIN, newList);
                Preferences.setIntPref(activity, Preferences.KEY_SAVED_THEME_SELECTION_PIN, themeListViewHolder.getAdapterPosition());
            }
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ThemeListViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivTheme, ivSelection;

        public ThemeListViewHolder(@NonNull View itemView) {
            super(itemView);

            ivTheme = itemView.findViewById(R.id.itemTheme_iv_theme);
            ivSelection = itemView.findViewById(R.id.itemTheme_iv_selection);

        }
    }

}
