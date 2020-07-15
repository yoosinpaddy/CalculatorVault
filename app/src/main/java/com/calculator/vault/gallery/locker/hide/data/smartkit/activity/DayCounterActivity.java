package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.calculator.vault.gallery.locker.hide.data.smartkit.MainApplication;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import static com.calculator.vault.gallery.locker.hide.data.smartkit.activity.SplashHomeActivity.activity;

//import static com.kit.tools.box.disk.news.shopping.activity.SplashHomeActivity.activity;

public class DayCounterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvStartDate, tvEndDate;
    Button btn_show_data, btn_clear_date_data;
    TextView tv_days, tv_hours, tv_minutes, tv_seconds, tv_years, tv_months, tv_weeks, tvDifferenceCustom;
    DatePickerDialog picker;
    private SimpleDateFormat mSimpleDateFormat;
    //private PeriodFormatter mPeriodFormat;

    private Date startDate;
    private Date endDate;
    String NewDateAns;
    ImageView iv_back, iv_currentdate, iv_enddate;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ImageView iv_more_app, iv_blast;
    static final int DATE_DIALOG_ID = 999;

    private int myear;
    private int mmonth;
    private int mday;

    private int msaveyear;
    private int msavemonth;
    private int msaveday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_day_counter);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        initView();
        initAction();

        if (Share.isNeedToAdShow(this)) {
//            NativeAdvanceHelper.loadAdBannerSize(activity, (FrameLayout) findViewById(R.id.fl_adplaceholder));
            setActionBar();
        }
    }

    private void initView() {

        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        btn_show_data = findViewById(R.id.btn_show_data);
        btn_clear_date_data = findViewById(R.id.btn_clear_date_data);
        iv_back = findViewById(R.id.iv_back);
        iv_currentdate = findViewById(R.id.iv_currentdate);
        iv_enddate = findViewById(R.id.iv_enddate);
        iv_more_app = findViewById(R.id.iv_more_app);
        iv_blast = findViewById(R.id.iv_blast);

        // tvDifferenceCustom = (TextView)findViewById(R.id.tvDifferenceCustom);

        tv_days = findViewById(R.id.tv_days);
        tv_hours = findViewById(R.id.tv_hours);
        tv_minutes = findViewById(R.id.tv_minutes);
        tv_seconds = findViewById(R.id.tv_seconds);

        tv_years = findViewById(R.id.tv_years);
        tv_months = findViewById(R.id.tv_months);
        tv_weeks = findViewById(R.id.tv_weeks);
    }

    private void initAction() {
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        btn_show_data.setOnClickListener(this);
        btn_clear_date_data.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        tv_days.setOnClickListener(this);
        tv_hours.setOnClickListener(this);
        tv_minutes.setOnClickListener(this);
        tv_seconds.setOnClickListener(this);

        tv_years.setOnClickListener(this);
        tv_months.setOnClickListener(this);
        tv_weeks.setOnClickListener(this);
        iv_currentdate.setOnClickListener(this);
        iv_enddate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_clear_date_data:
                tvStartDate.setText("");
                tvEndDate.setText("");
                break;

            case R.id.iv_currentdate:
                tvStartDate.performClick();
                break;

            case R.id.iv_enddate:
                tvEndDate.performClick();
                break;

            case R.id.tvStartDate:

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                // TODO: 2019-06-06 set date before "0"
                                String currentdate = String.valueOf(dayOfMonth);
                                if (currentdate.toString().length() == 1) {
                                    currentdate = "0" + currentdate;
                                    Log.e("currentMinutes", "currentdate-->" + currentdate);
                                }

                                String currentmonth = String.valueOf(monthOfYear + 1);
                                if (currentmonth.toString().length() == 1) {
                                    currentmonth = "0" + currentmonth;
                                    Log.e("currentMinutes", "currentMinutes-->" + currentmonth);
                                }

                                tvStartDate.setText(currentdate + "/" + currentmonth + "/" + year);
                                // et_start_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                                msaveyear = year;
                                msavemonth = Integer.parseInt(currentmonth) - 1;
                                msaveday = Integer.parseInt(currentdate);

                                myear = year;
                                mmonth = Integer.parseInt(currentmonth) - 1;
                                mday = Integer.parseInt(currentdate);
                            }
                        }, year, month, day);
                picker.show();

                break;
            case R.id.tvEndDate:
               /* final Calendar cldr1 = Calendar.getInstance();
                int day1 = cldr1.get(Calendar.DAY_OF_MONTH);
                int month1 = cldr1.get(Calendar.MONTH);
                int year1 = cldr1.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                // TODO: 2019-06-06 Date format change
                               *//* String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                SimpleDateFormat input = new SimpleDateFormat("dd/MM/yy");
                                SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    Date oneWayTripDate = input.parse(date);                 // parse input
                                    et_end_date.setText(output.format(oneWayTripDate));    // format output

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }*//*

                                // TODO: 2019-06-06 set date before "0"
                                String currentdate = String.valueOf(dayOfMonth);
                                if (currentdate.toString().length() == 1) {
                                    currentdate = "0" + currentdate;
                                    Log.e("currentMinutes", "currentdate-->" + currentdate);
                                }

                                String currentmonth = String.valueOf(monthOfYear + 1);
                                if (currentmonth.toString().length() == 1) {
                                    currentmonth = "0" + currentmonth;
                                    Log.e("currentMinutes", "currentMinutes-->" + currentmonth);
                                }

                                tvEndDate.setText(currentdate + "/" + currentmonth + "/" + year);
                                //et_end_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, year1, month1, day1);
*/

                // set current date into textview
             /*   tvDisplayDate.setText(new StringBuilder()
                        // Month is 0 based, just add 1
                        .append(mmonth + 1).append("-").append(mday).append("-")
                        .append(myear).append(" "));*/

                Log.e("daye", "onClick: date show----> " + new StringBuilder()
                        // Month is 0 based, just add 1
                        .append(mmonth + 1).append("-").append(mday).append("-")
                        .append(myear).append(" "));

                showDialog(DATE_DIALOG_ID);

                // picker.show();
                break;

            case R.id.btn_show_data:

                //components
                mSimpleDateFormat = new SimpleDateFormat("dd/MM/yy");
               /* mPeriodFormat = new PeriodFormatterBuilder().appendYears().appendSuffix(" year(s) ").appendMonths().appendSuffix(" month(s) ").appendDays().appendSuffix(" day(s) ").appendDays().appendSuffix(" week(s) ")
                        .appendDays().appendSuffix(" hour(s) ").appendDays().appendSuffix(" min(s) ").printZeroNever().toFormatter();*/

                String str = tvStartDate.getText().toString();
                String str1 = tvEndDate.getText().toString();

                if (!str.equals("")) {
                    if (!str1.equals("")) {
                        try {
                            startDate = mSimpleDateFormat.parse(str);
                            endDate = mSimpleDateFormat.parse(str1);

                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        Log.e("date", "onClick: startDate-->" + startDate);
                        Log.e("date", "onClick: endDate-->" + endDate);
                        //determine dateDiff
                        Period dateDiff = calcDiff(startDate, endDate);
                        // tvDifferenceStandard.setText(PeriodFormat.wordBased().print(dateDiff));

                        NewDateAns = PeriodFormat.wordBased().print(dateDiff);
                        String tmpString = NewDateAns.replace(" and ", ", ");
                        Log.e("tag", "tmpString-->" + tmpString);
                        Log.e("tag", "NewDateAns-->" + NewDateAns);

                        StringTokenizer tokens = new StringTokenizer(tmpString, ",");
                        String first = null, second = null, third = null, four = null, five = null, six = null;

                        String[] items = tmpString.split(",");
                        int itemCount = items.length;

                        if (itemCount == 1) {
                            first = tokens.nextToken();
                        } else if (itemCount == 2) {

                            first = tokens.nextToken();
                            second = tokens.nextToken();

                        } else if (itemCount == 3) {
                            first = tokens.nextToken();
                            second = tokens.nextToken();
                            third = tokens.nextToken();

                        } else if (itemCount == 4) {
                            first = tokens.nextToken();
                            second = tokens.nextToken();
                            third = tokens.nextToken();
                            four = tokens.nextToken();

                        }

                        tv_years.setText("0");
                        tv_months.setText("0");
                        tv_weeks.setText("0");
                        tv_days.setText("0");
                        tv_hours.setText("0");
                        tv_minutes.setText("0");
                        tv_seconds.setText("0");

                        if (first != null) {
                            StringTokenizer fFirst = new StringTokenizer(first, " ");
                            String startFirst = fFirst.nextToken();
                            String endFirst = fFirst.nextToken();

                            if (endFirst.equals("years") || endFirst.equals("year")) {
                                tv_years.setText(startFirst);
                            } else if (endFirst.equals("month") || endFirst.equals("months")) {
                                tv_months.setText(startFirst);
                            } else if (endFirst.equals("week") || endFirst.equals("weeks")) {
                                tv_weeks.setText(startFirst);
                            } else if (endFirst.equals("days") || endFirst.equals("day")) {
                                tv_days.setText(startFirst);
                            }
                        }

                        if (second != null) {
                            StringTokenizer sSecond = new StringTokenizer(second, " ");
                            String startSecond = sSecond.nextToken();
                            String endSecond = sSecond.nextToken();

                            if (endSecond.equals("months") || endSecond.equals("month")) {
                                tv_months.setText(startSecond);
                            } else if (endSecond.equals("weeks") || endSecond.equals("week")) {
                                tv_weeks.setText(startSecond);
                            } else if (endSecond.equals("days") || endSecond.equals("day")) {
                                tv_days.setText(startSecond);
                            }
                        }

                        if (third != null) {
                            StringTokenizer sThird = new StringTokenizer(third, " ");
                            String startThird = sThird.nextToken();
                            String endSThird = sThird.nextToken();

                            if (endSThird.equals("weeks") || endSThird.equals("week")) {
                                tv_weeks.setText(startThird);
                            } else if (endSThird.equals("days") || endSThird.equals("day")) {
                                tv_days.setText(startThird);
                            }
                        }

                        if (four != null) {
                            StringTokenizer sFour = new StringTokenizer(four, " ");
                            String startFour = sFour.nextToken();
                            String endFour = sFour.nextToken();

                            if (endFour.equals("days") || endFour.equals("day")) {
                                tv_days.setText(startFour);
                            }
                        }
                        diffTime();
                    } else {
                        Toast.makeText(this, "Please Select End Date", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Please Select Start Date", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                DatePickerDialog _date = new DatePickerDialog(this, datePickerListener, myear, mmonth, mday) {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (year < myear)
                            view.updateDate(myear, mmonth, mday);

                        if (monthOfYear < mmonth && year == myear)
                            view.updateDate(myear, mmonth, mday);

                        if (dayOfMonth < mday && year == myear && monthOfYear == mmonth)
                            view.updateDate(myear, mmonth, mday);

                    }
                };
                return _date;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {


            myear = selectedYear;
            mmonth = selectedMonth;
            mday = selectedDay;

            // set selected date into textview
           /* tvDisplayDate.setText(new StringBuilder().append(mmonth + 1)
                    .append("-").append(mday).append("-").append(myear)
                    .append(" "));*/

            Log.e("newdate", "onDateSet: new date shw--> " + new StringBuilder().append(mmonth)
                    .append("-").append(mday).append("-").append(myear)
                    .append(" "));

            String currentdate = String.valueOf(mday);
            if (currentdate.toString().length() == 1) {
                currentdate = "0" + currentdate;
                Log.e("currentMinutes", "currentdate-->" + currentdate);
            }

            String currentmonth = String.valueOf(mmonth + 1);
            if (currentmonth.toString().length() == 1) {
                currentmonth = "0" + currentmonth;
                Log.e("currentMinutes", "currentMinutes-->" + currentmonth);
            }

            tvEndDate.setText(currentdate + "/" + currentmonth + "/" + myear);

            myear = msaveyear;
            mmonth = msavemonth;
            mday = msaveday;

        }
    };

    public long diffTime() {
        long min = 0;
        long difference;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss aa"); // for 12-hour system, hh should be used instead of HH
            // There is no minute different between the two, only 8 hours difference. We are not considering Date, So minute will always remain 0
            Date date1 = simpleDateFormat.parse("12:00:00 AM");
            Date date2 = simpleDateFormat.parse(getCurrentTime());


            difference = (date2.getTime() - date1.getTime()) / 1000;
            long hours = difference % (24 * 3600) / 3600; // Calculating Hours
            long minute = difference % 3600 / 60; // Calculating minutes if there is any minutes difference
            long second = difference % 60;

            Log.e("11======= Hours", " :: " + hours);
            Log.e("11======= minutes", " :: " + minute);
            Log.e("11======= second", " :: " + second);
            Log.e("11======= currenttime", " :: " + getCurrentTime());

            tv_hours.setText("" + hours);
            tv_minutes.setText("" + minute);
            tv_seconds.setText("" + second);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return min;
    }


    private Period calcDiff(Date startDate, Date endDate) {
        DateTime START_DT = (startDate == null) ? null : new DateTime(startDate);
        DateTime END_DT = (endDate == null) ? null : new DateTime(endDate);

        Period period = new Period(START_DT, END_DT);

        return period;

    }

    private String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);
        int am_pm = c.get(Calendar.AM_PM);
        String ampm = null;

        Log.e("check1", "hour-->" + hour);
        Log.e("check1", "min -->" + min);
        Log.e("check1", "sec -->" + sec);

        if (am_pm == 0) {
            ampm = "AM";
        }
        if (am_pm == 1) {
            ampm = "PM";
        }

        Log.e("check1", "c.getTime() -->" + c.getTime());

        String strtime = hour + ":" + min + ":" + sec + " " + ampm;
        return strtime;
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
