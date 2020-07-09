package com.calculator.vault.gallery.locker.hide.data.smartkit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.activity.ChooseUnitActivity;
import com.calculator.vault.gallery.locker.hide.data.smartkit.activity.NetworkManager;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.UnitModel;

import java.util.ArrayList;

public class UnitChangeListAdapter extends RecyclerView.Adapter<UnitChangeListAdapter.ViewHolder> implements Filterable {

    private String TAG = UnitChangeListAdapter.class.getSimpleName();

    Context mContext;
    String mTitle;
    String mButton;
    public ArrayList<UnitModel> mUnitModels;
    onItemCLickListener mOnItemCLickListener;

    YourFilterClass userFilter;

    public interface onItemCLickListener {
        public void onItemClick(View view, int position);
    }

    public UnitChangeListAdapter(Context applicationContext, String title, String button, ArrayList<UnitModel> UnitModels, onItemCLickListener onItemCLickListener) {
        mContext = applicationContext;
        mTitle = title;
        mButton = button;
        mUnitModels = UnitModels;
        mOnItemCLickListener = onItemCLickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.s_row_unit_change_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.tvUnitFullName.setText(mUnitModels.get(position).getUnitFullName());
        holder.tvUnitName.setText("[" + mUnitModels.get(position).getUnitName() + "]");

        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (NetworkManager.isInternetConnectedwithdialog(mContext)) {
                    nextActivity(position);
                } else {
                    Log.e(TAG, "onClick:  mUnitModels.get(position).getUnitFullName():: " + mUnitModels.get(position).getUnitFullName());
                    nextActivity(position);
                }
            }
        });
    }

    private void nextActivity(int position) {

        int selectedPosition = 0;
        for (int i = 0; i < ChooseUnitActivity.unitChangeModels.size(); i++) {
            Log.e(TAG, "nextActivity: mUnitModels.get(position).getUnitFullName()::" + ChooseUnitActivity.unitChangeModels.get(position).getUnitFullName());
            Log.e(TAG, "mUnitModels.get(i).getUnitFullName()::" + ChooseUnitActivity.unitChangeModels.get(i).getUnitFullName());
            if (mUnitModels.get(position).getUnitFullName().equals(ChooseUnitActivity.unitChangeModels.get(i).getUnitFullName())) {
                selectedPosition = i;
            }
        }

        Log.e(TAG, "nextActivity: selectedPosition:::" + selectedPosition);
        if (mButton.equals("from")) {
            Log.e(TAG, "nextActivity: position:::" + position);
            Log.e(TAG, "nextActivity: SharedPrefs to:::" + SharedPrefs.getInt(mContext, mTitle + mContext.getString(R.string._to)));
            if (selectedPosition == SharedPrefs.getInt(mContext, mTitle + mContext.getString(R.string._to))) {
                Toast.makeText(mContext, R.string.unit_already_selected, Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "nextActivity: else");
                nextConversationActivity(position, selectedPosition);
            }
        }

        if (mButton.equals("to")) {
            if (selectedPosition == SharedPrefs.getInt(mContext, mTitle + mContext.getString(R.string._from))) {
                Toast.makeText(mContext, R.string.unit_already_selected, Toast.LENGTH_SHORT).show();
            } else {
                nextConversationActivity(position, selectedPosition);
            }
        }

//        if (position == SharedPrefs.getInt(mContext, mTitle + mContext.getString(R.string._from)) || position == SharedPrefs.getInt(mContext, mTitle + mContext.getString(R.string._from)))

    }

    private void nextConversationActivity(int position, int selectedPosition) {
        Log.e(TAG, "nextConversationActivity: selectedPosition::" + selectedPosition);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("SelectedFullUnit", mUnitModels.get(position).getUnitFullName());
        returnIntent.putExtra("SelectedUnit", mUnitModels.get(position).getUnitName());
        returnIntent.putExtra("SelectedUnitPosition", selectedPosition);
        returnIntent.putExtra("UnitModels", mUnitModels);
        ((Activity) mContext).setResult(Activity.RESULT_OK, returnIntent);
        ((Activity) mContext).finish();
        ((Activity) mContext).overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    public Filter getFilter() {
        if (userFilter == null)
            userFilter = new YourFilterClass(this, mUnitModels);
        return userFilter;
    }

    @Override
    public int getItemCount() {
        return mUnitModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUnitFullName;
        TextView tvUnitName;
        LinearLayout ll_main;

        public ViewHolder(View itemView) {
            super(itemView);

            tvUnitName = itemView.findViewById(R.id.tvUnitName);
            tvUnitFullName = itemView.findViewById(R.id.tvUnitFullName);
            ll_main = itemView.findViewById(R.id.ll_main);
        }
    }

    private class YourFilterClass extends Filter {

        UnitChangeListAdapter adpater;

        ArrayList<UnitModel> filterList;

        public YourFilterClass(UnitChangeListAdapter addContactDetailAdpater, ArrayList<UnitModel> mAddContactDetailList) {
            super();
            this.adpater = addContactDetailAdpater;
            filterList = mAddContactDetailList;

        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            //CHECK CONSTRAINT VALIDITY
            if (constraint != null && constraint.length() > 0) {
                //CHANGE TO UPPER
                constraint = constraint.toString().toUpperCase();
                //STORE OUR FILTERED PLAYERS
                ArrayList<UnitModel> filteredPlayers = new ArrayList<>();

                for (int i = 0; i < filterList.size(); i++) {
                    //CHECK
                    if (filterList.get(i).getUnitFullName().toUpperCase().contains(constraint)) {
                        //ADD PLAYER TO FILTERED PLAYERS
                        filteredPlayers.add(filterList.get(i));
                    }
                }
                results.count = filteredPlayers.size();
                results.values = filteredPlayers;
            } else {
                results.count = filterList.size();
                results.values = filterList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adpater.mUnitModels = (ArrayList<UnitModel>) results.values;

            Log.e(TAG, "publishResults: adpater.mUnitModels ::" + adpater.mUnitModels);

            if (adpater.mUnitModels.size() == 0) {
                ChooseUnitActivity.rv_unit_list.setVisibility(View.GONE);
                ChooseUnitActivity.tv_no_match_found.setVisibility(View.VISIBLE);
            } else {
                ChooseUnitActivity.rv_unit_list.setVisibility(View.VISIBLE);
                ChooseUnitActivity.tv_no_match_found.setVisibility(View.GONE);
            }
            //REFRESH
            adpater.notifyDataSetChanged();
        }
    }
}
