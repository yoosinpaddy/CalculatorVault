package com.calculator.vault.gallery.locker.hide.data.smartkit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.calculator.vault.gallery.locker.hide.data.R;


/**
 * Created by Urvashi-vasundhara on 6/5/2017.
 */
public class CustomSpinnerAdapter extends RecyclerView.Adapter<CustomSpinnerAdapter.MyViewHolder> {

    private String[] ary_criterion;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public CustomSpinnerAdapter(Context context, String[] ary_criterion, OnItemClickListener onItemClickListener) {
        mContext = context;
        this.ary_criterion = ary_criterion;
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.s_row_spinner_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        try {
            holder.tv_popup_item.setText(ary_criterion[position]);

            /*if (ary_criterion.size() - 1 == position) {
                holder.view_split.setVisibility(View.GONE);
            }*/

            holder.ll_itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return ary_criterion.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_itemView;
        TextView tv_popup_item;
        View view_split;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_itemView = (LinearLayout) itemView.findViewById(R.id.ll_itemView);
            tv_popup_item = (TextView) itemView.findViewById(R.id.tv_popup_item);
//            view_split = (View) itemView.findViewById(R.id.view_split);
        }
    }
}
