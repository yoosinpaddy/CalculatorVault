package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.calculator.vault.gallery.locker.hide.data.smartkit.MainApplication;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.adapter.WorldClockAdapter;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.DataStorage;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.DisplayMetricsHandler;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Util;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.WorldClock;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class WorldClockActivity extends AppCompatActivity implements OnItemClickListener, View.OnClickListener {
    Activity activity;
    public static final int REQUEST_CODE_ADD = 1000;
    public static final int REQUEST_CODE_DATE_FORMAT = 1003;
    public static final int REQUEST_CODE_DISPLAY_FORMAT = 1002;
    public static final int REQUEST_CODE_EDIT = 1001;
    public static final String TAG = "WorldClockActivity";
    private int itemPosition;
    private BroadcastReceiver mBroadcastReceiver = new DateChangeReciver();
    private int optionItemId;
    private WorldClockAdapter worldClockAdapter;
    private List<WorldClock> worldClockList;
    private TextView tv_date, tv_time, tv_name;
    private ImageView iv_add_country, iv_back;
    TextView tv_no_data;
    ListView listView;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ImageView iv_more_app, iv_blast;

    class DateChangeReciver extends BroadcastReceiver {
        DateChangeReciver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (worldClockAdapter != null) {
                WorldClockActivity.this.worldClockAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_world_clock);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        initAction();

        if (Share.isNeedToAdShow(this)) {
            NativeAdvanceHelper.loadAdBannerSize(activity, (FrameLayout) findViewById(R.id.fl_adplaceholder));
            setActionBar();
        }
    }

    private void initAction() {

        activity = WorldClockActivity.this;
        this.worldClockList = DataStorage.getWorldClockList(this);

        listView = (ListView) findViewById(R.id.list_view_world_clock);
        tv_time = findViewById(R.id.tv_time);
        tv_date = findViewById(R.id.tv_date);
        tv_name = findViewById(R.id.tv_name);
        iv_back = findViewById(R.id.iv_back);
        iv_more_app = findViewById(R.id.iv_more_app);
        iv_blast = findViewById(R.id.iv_blast);
        iv_add_country = findViewById(R.id.iv_add_country);
        tv_no_data = findViewById(R.id.tv_no_data);

        iv_add_country.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        if (this.worldClockList.size() != 0) {
            tv_no_data.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            this.worldClockAdapter = new WorldClockAdapter(this, this.worldClockList);

            tv_name.setText("Kolkata");

            String str = getDate();
            String[] items = str.split(", ");

            tv_date.setText("" + items[0]);
            tv_time.setText("" + items[1]);

            listView.setAdapter(this.worldClockAdapter);
            listView.setOnItemClickListener(this);
            registerForContextMenu(listView);

        } else {
            tv_no_data.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            tv_name.setText("Kolkata");
            String str = getDate();
            String[] items = str.split(", ");

            tv_date.setText("" + items[0]);
            tv_time.setText("" + items[1]);

        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                // TODO Auto-generated method stub

                Log.v("long clicked", "pos: " + pos);

                final Dialog dialog3 = new Dialog(activity);
                dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog3.setContentView(R.layout.s_custom_dailog_clear_country);
                dialog3.setCanceledOnTouchOutside(true);

                Button btn_no = dialog3.findViewById(R.id.btn_no);
                Button btn_yes = dialog3.findViewById(R.id.btn_yes);


                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog3.dismiss();
                    }
                });
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DataStorage.removeWorldClock(WorldClockActivity.this, (WorldClock) worldClockList.remove(pos));
                        worldClockAdapter.notifyDataSetChanged();

                        if (worldClockList.size() == 0) {
                            tv_no_data.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        }

                        tv_name.setText("Kolkata");

                        String str = getDate();
                        String[] items = str.split(", ");

                        tv_date.setText("" + items[0]);
                        tv_time.setText("" + items[1]);

                        dialog3.dismiss();
                    }
                });
                Window window3 = dialog3.getWindow();
                window3.setGravity(Gravity.CENTER);
                window3.setLayout((int) (0.9 * DisplayMetricsHandler.getScreenWidth()), Toolbar.LayoutParams.WRAP_CONTENT);

                if (dialog3 != null && !dialog3.isShowing())
                    dialog3.show();
                return true;
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.TIME_TICK");
        registerReceiver(this.mBroadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(this.mBroadcastReceiver);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        WorldClock worldClock = (WorldClock) this.worldClockList.get(position);
        tv_name.setText("" + Util.getDisplay(worldClock.getTimeZoneId()));

        TimeZone timeZone = TimeZone.getTimeZone(worldClock.getTimeZoneId());
        Calendar calendar = new GregorianCalendar(timeZone);
        SimpleDateFormat formatter = new SimpleDateFormat("EE dd MMMM, hh:mm a");
        formatter.setTimeZone(timeZone);

        String str = formatter.format(calendar.getTime());
        Log.e(TAG, "onActivityResult: str format-->"+ str);
        String[] items = str.split(", ");
        tv_date.setText("" + items[0]);
        tv_time.setText("" + items[1]);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_add_country:
                Log.e(TAG, "onClick iv_add_country");
                startActivityForResult(new Intent(WorldClockActivity.this, WorldClockEditActivity.class), REQUEST_CODE_EDIT);
                break;

            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            WorldClock worldClock;
            worldClock = new WorldClock();
            worldClock.setTimeZoneId(data.getStringExtra("2131034148"));
            worldClock.setDisplayFormat(0);
            worldClock.setId(DataStorage.addWorldClock(this, worldClock));
            this.worldClockList.add(worldClock);

            if (this.worldClockAdapter != null) {

                Log.e(TAG, "onActivityResult: iffffffffff" );
                if (this.worldClockList.size() == 0) {
                    tv_no_data.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    this.worldClockAdapter = new WorldClockAdapter(this, this.worldClockList);

                    tv_name.setText("Kolkata");

                    String str = getDate();
                    String[] items = str.split(", ");

                    tv_date.setText("" + items[0]);
                    tv_time.setText("" + items[1]);

                    listView.setAdapter(this.worldClockAdapter);
                    listView.setOnItemClickListener(this);
                    registerForContextMenu(listView);

                } else {
                    tv_no_data.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    this.worldClockAdapter.notifyDataSetChanged();
                }
            } else {

                Log.e(TAG, "onActivityResult: elseeeeeeee" );
                tv_no_data.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                this.worldClockAdapter = new WorldClockAdapter(this, this.worldClockList);

                tv_name.setText("Kolkata");

                String str = getDate();
                String[] items = str.split(", ");

                tv_date.setText("" + items[0]);
                tv_time.setText("" + items[1]);

                listView.setAdapter(this.worldClockAdapter);
                listView.setOnItemClickListener(this);
                registerForContextMenu(listView);
            }
        }
    }


    private String getDate() {
        Calendar c = Calendar.getInstance();
        Date dayOfWeek = c.getTime();

        String day = String.valueOf(new SimpleDateFormat("EE dd MMMM, hh:mm a", Locale.ENGLISH).format(dayOfWeek.getTime()));
        return day;
    }

    private void setActionBar() {
        try {
            iv_more_app.setVisibility(View.GONE);
            iv_more_app.setBackgroundResource(R.drawable.animation_list_filling);
            ((AnimationDrawable) iv_more_app.getBackground()).start();
            loadInterstialAd();

            iv_more_app.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_more_app.setVisibility(View.GONE);
                    iv_blast.setVisibility(View.VISIBLE);
                    ((AnimationDrawable) iv_blast.getBackground()).start();

                    if (MainApplication.getInstance().requestNewInterstitial()) {
                        MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                iv_blast.setVisibility(View.GONE);
                                iv_more_app.setVisibility(View.GONE);
                                loadInterstialAd();
                            }

                            @Override
                            public void onAdFailedToLoad(int i) {
                                super.onAdFailedToLoad(i);
                                iv_blast.setVisibility(View.GONE);
                                iv_more_app.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                iv_blast.setVisibility(View.GONE);
                                iv_more_app.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        iv_blast.setVisibility(View.GONE);
                        iv_more_app.setVisibility(View.GONE);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadInterstialAd() {
        try {
            if (Share.isNeedToAdShow(this)) {
                if (MainApplication.getInstance().mInterstitialAd.isLoaded()) {
                    Log.e("if", "if");
//                    iv_more_app.setVisibility(View.VISIBLE);
                } else {
                    MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                    MainApplication.getInstance().mInterstitialAd = null;
                    MainApplication.getInstance().ins_adRequest = null;
                    MainApplication.getInstance().LoadAds();
                    MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            Log.e("load", "load");
//                            iv_more_app.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAdFailedToLoad(int i) {
                            super.onAdFailedToLoad(i);
                            Log.e("fail", "fail");
                            iv_more_app.setVisibility(View.GONE);
                            //ApplicationClass.getInstance().LoadAds();
                            loadInterstialAd();
                        }
                    });
                }
            }

        } catch (Exception e) {
//            iv_more_app.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }

}
