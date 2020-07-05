package com.calculator.vault.gallery.locker.hide.data.appLock;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppListViewHolder> {

    private Activity activity;
    private ArrayList<AppListModel> list;
    private ArrayList<String> lockedPackages;
    private boolean isBluetoothLocked, isWifiLocked;

    public AppListAdapter(Activity activity, ArrayList<AppListModel> list, ArrayList<String> loackedPackages) {
        this.activity = activity;
        this.list = list;
        this.lockedPackages = loackedPackages;
    }

    @NonNull
    @Override
    public AppListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_app_list, viewGroup, false);
        return new AppListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppListViewHolder holder, int pos) {

        holder.setIsRecyclable(false);
        holder.ivIcon.setImageDrawable(list.get(pos).getAppIcon());
        holder.tvName.setText(list.get(pos).getAppName());
        if (list.get(pos).isLocked()) {
            holder.ivLock.setImageResource(R.drawable.ic_locked);


        } else {
            holder.ivLock.setImageResource(R.drawable.ic_unlock);


        }


        if (pos == 0) {
            isBluetoothLocked = list.get(pos).isLocked();
            Preferences.setBooleanPref(activity, Preferences.KEY_SERVICE_BLUETOOTH_START, isBluetoothLocked);
        }

        if (pos == 1) {
            isWifiLocked = list.get(pos).isLocked();
            Preferences.setBooleanPref(activity, Preferences.KEY_SERVICE_WIFI_START, isWifiLocked);
        }

        holder.ivLock.setOnClickListener(v -> {

            try {
                if (list.get(holder.getAdapterPosition()).isLocked()) {
                    for (int i = 0; i < lockedPackages.size(); i++) {
                        if (list.get(holder.getAdapterPosition()).getAppPackage().equals("com.google.android.gm")) {
                            if (lockedPackages.get(i).equals("com.google.android.gsf")) {
                                lockedPackages.remove(i);
                            }
                        }
                        if (lockedPackages.get(i).equals(list.get(holder.getAdapterPosition()).getAppPackage())) {
                            lockedPackages.remove(i);
                        }
                    }
                    String packageList = new Gson().toJson(lockedPackages);
                    Preferences.setStringPref(activity, Preferences.KEY_PACKAGES, packageList);
                    list.get(holder.getAdapterPosition()).setLocked(false);
                    //Toast.makeText(activity, holder.tvName.getText().toString().trim() + " is Unlocked!", Toast.LENGTH_LONG).show();
                } else {
                    //com.google.android.gsf
                    if (list.get(holder.getAdapterPosition()).getAppPackage().equals("com.google.android.gm")) {
                        lockedPackages.add("com.google.android.gsf");
                    }
                    lockedPackages.add(list.get(holder.getAdapterPosition()).getAppPackage());
                    String packageList = new Gson().toJson(lockedPackages);
                    Preferences.setStringPref(activity, Preferences.KEY_PACKAGES, packageList);
                    list.get(holder.getAdapterPosition()).setLocked(true);
                    //Toast.makeText(activity, holder.tvName.getText().toString().trim() + " is Locked!", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(activity, "Something went wrong, Please try again...", Toast.LENGTH_SHORT).show();
            }

            try {
                if (list.get(holder.getAdapterPosition()).getAppPackage().equalsIgnoreCase("bluetooth")
                        || list.get(holder.getAdapterPosition()).getAppPackage().equalsIgnoreCase("wifi")) {


                    if (!list.get(holder.getAdapterPosition()).isLocked()) {
                        if (!isWifiLocked || !isBluetoothLocked) {
                            activity.stopService(new Intent(activity, QuickSettingsLockService.class));
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            activity.startForegroundService(new Intent(activity, QuickSettingsLockService.class));
                            Preferences.setBooleanPref(activity, Preferences.KEY_SERVICE_INITIAL_START, true);
                        } else {
                            activity.startService(new Intent(activity, QuickSettingsLockService.class));

                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(activity, "Something went wrong, Please try again...", Toast.LENGTH_SHORT).show();
            }
            notifyDataSetChanged();

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class AppListViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivIcon, ivLock;
        private TextView tvName;

        public AppListViewHolder(@NonNull View itemView) {
            super(itemView);

            ivIcon = itemView.findViewById(R.id.iv_appIcon);
            ivLock = itemView.findViewById(R.id.iv_appLocked);
            tvName = itemView.findViewById(R.id.tv_appName);

        }
    }

}
