package com.calculator.vault.gallery.locker.hide.data.smartkit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Util;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.WorldClock;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class WorldClockAdapter extends ArrayAdapter<WorldClock> {
    private LayoutInflater inflater;

    public WorldClockAdapter(Context context, List<WorldClock> worldClockList) {
        super(context, 0, worldClockList);
        this.inflater = LayoutInflater.from(context);
    }

    SimpleDateFormat formatter = new SimpleDateFormat("EE dd MMMM, hh:mm a");

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.s_list_item_world_clock, parent, false);
        }
        WorldClock worldClock = (WorldClock) getItem(position);
        TimeZone timeZone = TimeZone.getTimeZone(worldClock.getTimeZoneId());
        Calendar calendar = new GregorianCalendar(timeZone);
        formatter.setTimeZone(timeZone);
        TextView textView = (TextView) convertView.findViewById(R.id.text_view_world_clock);
        TextView text_view_world_clock_time = (TextView) convertView.findViewById(R.id.text_view_world_clock_time);
        TextView text_view_world_clock_date = (TextView) convertView.findViewById(R.id.text_view_world_clock_date);

        textView.setText(Util.getDisplay(worldClock.getTimeZoneId()));

        String str = formatter.format(calendar.getTime());

        String[] items = str.split(", ");
        text_view_world_clock_time.setText("" + items[0]);
        text_view_world_clock_date.setText("" + items[1]);

        return convertView;
    }
}
