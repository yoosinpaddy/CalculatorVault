package com.calculator.vault.gallery.locker.hide.data.smartkit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.City;

import java.util.List;

public class CityAdapter extends ArrayAdapter<City> {
    private LayoutInflater inflater;

    public CityAdapter(Context context, List<City> cityList) {
        super(context, 0, cityList);
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.s_list_item_city, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.text_view_city)).setText(((City) getItem(position)).getName());
        return convertView;
    }
}
