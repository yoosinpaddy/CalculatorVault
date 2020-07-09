package com.calculator.vault.gallery.locker.hide.data.smartkit.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.activity.UnitConverterinListActivity;

import java.text.DecimalFormat;

public class UnitConverterInListAdapter extends RecyclerView.Adapter<UnitConverterInListAdapter.ViewHolder> {

    Context mContext;
    //    int mSelectedFromPosition;
    String TAG = UnitConverterInListAdapter.class.getSimpleName();
    String mTitle;
    String[] mUnitFullNamesArray;
    int[] mUnitFullNamesArray2;
    String[] mUnitNamesArray;
    Double[][] mUnitValues;

    public UnitConverterInListAdapter(Context applicationContext, String title, int selectedFromPosition, String[] unitFullNamesArray, int[] unitFullNamesArray2, String[] unitNamesArray, Double[][] unitValues) {
        mContext = applicationContext;
//        mSelectedFromPosition = selectedFromPosition;
        mTitle = title;
        mUnitFullNamesArray = unitFullNamesArray;
        mUnitFullNamesArray2 = unitFullNamesArray2;
        mUnitNamesArray = unitNamesArray;
        mUnitValues = unitValues;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.s_row_unit_list, parent, false);
        return new UnitConverterInListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!mTitle.equals("Currency"))
            holder.tvFullUnitname2.setText(mUnitFullNamesArray2[position]);
        holder.tvUnitName.setText(mUnitNamesArray[position]);

        try {
            Log.e(TAG, "onBindViewHolder: mSelectedFromPosition::" + UnitConverterinListActivity.selectedFromPosition);
            if (!UnitConverterinListActivity.etFrom.getText().toString().equals("")) {
                holder.tvValue.setText(new DecimalFormat("#.####").format(Double.parseDouble(UnitConverterinListActivity.etFrom.getText().toString()) * mUnitValues[UnitConverterinListActivity.selectedFromPosition][position]));
                if (holder.tvValue.getText().toString().equals("0")) {
                    holder.tvValue.setText("0.0000");
                }
            } else {
                /*UnitConverterinListActivity.etFrom.setText("0");
                holder.tvValue.setText("0.000");*/
                holder.tvValue.setText(new DecimalFormat("#.####").format(0 * mUnitValues[UnitConverterinListActivity.selectedFromPosition][position]));
                if (holder.tvValue.getText().toString().equals("0")) {
                    holder.tvValue.setText("0.0000");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tvValue.setSelection(holder.tvValue.getText().toString().length());
        UnitConverterinListActivity.etFrom.setSelection(UnitConverterinListActivity.etFrom.getText().toString().length());
    }

    @Override
    public int getItemCount() {
        return mUnitFullNamesArray.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUnitName;
        TextView tvFullUnitname2;
        EditText tvValue;

        public ViewHolder(View itemView) {
            super(itemView);

            tvUnitName = itemView.findViewById(R.id.tvUnitName);
            tvFullUnitname2 = itemView.findViewById(R.id.tvFullUnitname2);
            tvValue = itemView.findViewById(R.id.tvValue);
        }
    }
}
