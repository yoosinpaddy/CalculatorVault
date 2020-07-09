package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.adapter.WorldClockAdapter;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.WorldClock;

import java.util.List;
import java.util.Vector;

public class DisplayFormatActivity extends Activity implements OnItemClickListener {
    public static final String TAG = "DisplayFormatActivity";
    private BroadcastReceiver mBroadcastReceiver = new C03021();
    private WorldClockAdapter worldClockAdapter;
    private List<WorldClock> worldClockList;

    /* renamed from: net.whph.android.worldclock.DisplayFormatActivity$1 */
    class C03021 extends BroadcastReceiver {
        C03021() {
        }

        public void onReceive(Context context, Intent intent) {
            DisplayFormatActivity.this.worldClockAdapter.notifyDataSetChanged();
        }
    }

    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.TIME_TICK");
        registerReceiver(this.mBroadcastReceiver, filter);
    }

    protected void onStop() {
        super.onStop();
        unregisterReceiver(this.mBroadcastReceiver);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String timeZoneId = getIntent().getStringExtra("2131427354");
        this.worldClockList = new Vector();
        for (int i = 0; i < 34; i++) {
            WorldClock worldClock = new WorldClock();
            worldClock.setDisplayFormat(i);
            worldClock.setTimeZoneId(timeZoneId);
            this.worldClockList.add(worldClock);
        }
        this.worldClockAdapter = new WorldClockAdapter(this, this.worldClockList);
        setContentView(R.layout.s_display_format);
        ListView listView = (ListView) findViewById(R.id.list_view_display_format);
        TextView titleView = (TextView) LayoutInflater.from(this).inflate(R.layout.s_list_item_title, null);
        titleView.setText(R.string.display_format);
        listView.addHeaderView(titleView, null, false);
        listView.setAdapter(this.worldClockAdapter);
        listView.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("2131427354", position - 1);
        setResult(-1, intent);
        finish();
    }
}
