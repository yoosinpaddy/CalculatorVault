package com.calculator.vault.gallery.locker.hide.data.smartkit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.BMIModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by ABC on 9/23/2017.
 */

public class BMIListAdapter extends BaseAdapter {

    Context context;

    ArrayList<BMIModel> list;

    public class Holder {

        TextView Bmi_TextView;
        TextView Weight_TextView;
        TextView Date_TextView;
        TextView Age_TextView;
        TextView Height_TextView;
        TextView Time_TextView;
    }

    public BMIListAdapter(Context context, ArrayList<BMIModel> list){
        this.context = context;
        this.list =list;
    }



    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View child, ViewGroup parent) {

        Holder holder;

        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            child = layoutInflater.inflate(R.layout.s_his_list_item, null);

            holder = new Holder();

            holder.Bmi_TextView = (TextView) child.findViewById(R.id.bmi);
            holder.Weight_TextView = (TextView) child.findViewById(R.id.weight);
            holder.Date_TextView = (TextView) child.findViewById(R.id.date);
            holder.Time_TextView = (TextView) child.findViewById(R.id.time);
            holder.Height_TextView = (TextView) child.findViewById(R.id.height);
            holder.Age_TextView = (TextView) child.findViewById(R.id.age);

            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }
        DecimalFormat precision = new DecimalFormat("0.00");

        //Collections.reverse(list);
        BMIModel model = list.get(position);
        holder.Bmi_TextView.setText(precision.format(model.getBmi()));
        holder.Weight_TextView.setText(""+model.getWeight());
        holder.Date_TextView.setText(""+model.getDate());
        holder.Time_TextView.setText(""+model.getTime());
        holder.Age_TextView.setText(""+model.getAge());
        holder.Height_TextView.setText(""+model.getHeight());
        return child;
    }
}
