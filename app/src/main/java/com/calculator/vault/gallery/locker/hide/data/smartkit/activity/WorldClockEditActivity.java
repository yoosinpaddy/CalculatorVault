package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.adapter.CityAdapter;
import com.calculator.vault.gallery.locker.hide.data.smartkit.adapter.EmptyAdapter;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.DataStorage;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Util;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.City;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.Continent;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.WorldClock;

import java.util.ArrayList;
import java.util.List;

public class WorldClockEditActivity extends Activity implements OnItemSelectedListener, OnItemClickListener {
    public static final String TAG = "WorldClockEditActivity";
    private CityAdapter cityAdapter;
    private ListView cityListView;
    private List<Continent> continentList;
    private Spinner mSpinnerContinent;
    private ImageView iv_spinner_open;
    private List<WorldClock> worldClockList;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ArrayList<WorldClock> worldClocksl = new ArrayList<>();
    boolean iflagsave = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_world_clock_edit);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        this.continentList = Util.getContinentList(this);
        ListView listView = (ListView) findViewById(R.id.list_view_world_clock_edit);
        LayoutInflater inflater = LayoutInflater.from(this);
        TextView titleView = (TextView) inflater.inflate(R.layout.s_list_item_title, null);
        titleView.setText(R.string.choose_a_location);
        listView.addHeaderView(titleView, null, false);
        View view = inflater.inflate(R.layout.s_list_item_continent_spinner, null);
        this.mSpinnerContinent = (Spinner) view.findViewById(R.id.spinner_continent);
        this.iv_spinner_open = (ImageView) view.findViewById(R.id.iv_spinner_open);

        this.iv_spinner_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpinnerContinent.performClick();
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.continent, 17367048);
        adapter.setDropDownViewResource(17367049);

        this.mSpinnerContinent.setAdapter(adapter);
        this.mSpinnerContinent.setOnItemSelectedListener(this);
        this.mSpinnerContinent.setSelection(1);
        listView.addHeaderView(view);
        listView.setAdapter(new EmptyAdapter());
        this.cityListView = (ListView) findViewById(R.id.list_view_city);
        this.cityListView.setOnItemClickListener(this);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        this.cityAdapter = new CityAdapter(this, ((Continent) this.continentList.get(position)).getCityList());
        this.cityListView.setAdapter(this.cityAdapter);
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        City city = (City) this.cityAdapter.getItem(position);

        worldClockList = DataStorage.getWorldClockList(this);

        if (worldClockList.size() != 0) {
            for (int i = 0; i < worldClockList.size(); i++) {
                WorldClock worldClock = (WorldClock) worldClockList.get(i);
                worldClocksl.add(worldClock);
                if (city.getTimeZoneId().equalsIgnoreCase(worldClock.getTimeZoneId())) {
                    iflagsave = true;
                    break;
                } else {

                    iflagsave = false;
                }
            }
        } else {
            Intent intent = new Intent();
            intent.putExtra("2131034148", city.getTimeZoneId());
            setResult(-1, intent);
            finish();
        }

        if (!iflagsave) {
            Log.e(TAG, "onItemClick: flagsave-->" + iflagsave);
            Intent intent = new Intent();
            intent.putExtra("2131034148", city.getTimeZoneId());
            setResult(-1, intent);
            finish();
        }else{
            Toast.makeText(this, "You have already selected this country..", Toast.LENGTH_SHORT).show();
        }
    }
}
