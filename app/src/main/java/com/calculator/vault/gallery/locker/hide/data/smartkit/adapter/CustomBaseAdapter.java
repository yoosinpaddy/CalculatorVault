package com.calculator.vault.gallery.locker.hide.data.smartkit.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.Customlangmodel;

import java.util.List;

public class CustomBaseAdapter extends BaseAdapter {
    Context context;
    List<Customlangmodel> rowItems;
    private int PosLang ;

    private class ViewHolder {
        ImageView imageView,check_lang;
        TextView txtTitle;

        private ViewHolder() {
        }
    }

    public CustomBaseAdapter(Context context, List<Customlangmodel> items, int fromPosLang) {
        this.context = context;
        this.rowItems = items;
        this.PosLang = fromPosLang;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        @SuppressLint("WrongConstant") LayoutInflater mInflater = (LayoutInflater) this.context.getSystemService("layout_inflater");


        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.s_list_item, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title1);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon1);
            holder.check_lang = (ImageView) convertView.findViewById(R.id.check_lang);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        for (int i = 0; i <= position ; i++)
        {
            if (i == PosLang)
            {
                holder.check_lang.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.check_lang.setVisibility(View.INVISIBLE);
            }
        }



        Customlangmodel rowItem = (Customlangmodel) getItem(position);
        holder.txtTitle.setText(rowItem.getName());
        holder.imageView.setImageResource(rowItem.getImageId());
        return convertView;
    }

    public int getCount() {
        return this.rowItems.size();
    }

    public Object getItem(int position) {
        return this.rowItems.get(position);
    }

    public long getItemId(int position) {
        return (long) this.rowItems.indexOf(getItem(position));
    }
}
