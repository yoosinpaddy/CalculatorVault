package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.app.DatePickerDialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.google.android.gms.ads.AdListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.calculator.vault.gallery.locker.hide.data.smartkit.MainApplication;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class AgeCalculatorActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvBirthDate, tv_currentDate, tv_next_days, tv_next_month, btn_clear_data;
    private Button btn_show_data;
    private TextView tv_days, tv_year, tv_months;
    private DatePickerDialog picker;
    private Date birthDate;
    private SimpleDateFormat mSimpleDateFormat;
    //private PeriodFormatter mPeriodFormat;
    private String NewDateAns;
    private ImageView iv_currentDate, iv_birthDate;
    private String Birthdate;
    private String Birthmonth;
    String NextBirthDate;
    int currentyear;
    ImageView iv_back;
    private ImageView iv_more_app, iv_blast;
    private FirebaseAnalytics mFirebaseAnalytics;
    int DatelistSize1 = 0;
    public int daysInMonth;
    public int currYear;
    StringBuffer sb = new StringBuffer();
    Date currentDate;
    String dataCurrent, dateEndDate;
    String newEndDate, newStartDate;
    AgeCalculatorActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_birthday_counter);

        activity =AgeCalculatorActivity.this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
        initView();
        initAction();

       /* if (Share.isNeedToAdShow(this)) {
            NativeAdvanceHelper.loadAdBannerSize(activity, (FrameLayout) findViewById(R.id.fl_adplaceholder));
            setActionBar();
        }*/
    }

    private void initView() {
        tvBirthDate = findViewById(R.id.tvBirthDate);
        tv_currentDate = findViewById(R.id.tv_currentDate);
        iv_back = findViewById(R.id.iv_back);
        btn_show_data = findViewById(R.id.btn_show_data);

        tv_days = findViewById(R.id.tv_days);
        tv_year = findViewById(R.id.tv_year);
        tv_months = findViewById(R.id.tv_months);
        iv_currentDate = findViewById(R.id.iv_currentDate);
        iv_birthDate = findViewById(R.id.iv_birthDate);


        tv_next_days = findViewById(R.id.tv_next_days);
        tv_next_month = findViewById(R.id.tv_next_month);
        btn_clear_data = findViewById(R.id.btn_clear_data);
        iv_more_app = findViewById(R.id.iv_more_app);
        iv_blast = findViewById(R.id.iv_blast);
    }

    private void initAction() {
        tvBirthDate.setOnClickListener(this);
        btn_show_data.setOnClickListener(this);
        tv_currentDate.setOnClickListener(this);
        iv_currentDate.setOnClickListener(this);
        iv_birthDate.setOnClickListener(this);
        btn_clear_data.setOnClickListener(this);
        iv_back.setOnClickListener(this);


        final Calendar cldr1 = Calendar.getInstance();
        int day1 = cldr1.get(Calendar.DAY_OF_MONTH);
        int month1 = cldr1.get(Calendar.MONTH);
        int year1 = cldr1.get(Calendar.YEAR);

        // TODO: 2019-06-06 set date before "0"
        String currentdate = String.valueOf(day1);
        if (currentdate.toString().length() == 1) {
            currentdate = "0" + currentdate;
            Log.e("currentMinutes", "currentdate-->" + currentdate);
        }

        String currentmonth = String.valueOf(month1 + 1);
        if (currentmonth.toString().length() == 1) {
            currentmonth = "0" + currentmonth;
            Log.e("currentMinutes", "currentMinutes-->" + currentmonth);
        }

        newStartDate = currentmonth + "/" + currentdate + "/" + year1;
        tv_currentDate.setText(currentdate + "/" + currentmonth + "/" + year1);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_back:
                onBackPressed();
                break;
          /*  case R.id.et_start_date:

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

                                et_start_date.setText(currentdate + "/" + currentmonth + "/" + year);
                                // et_start_date.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                            }
                        }, year, month, day);
                picker.show();

                break;*/
            case R.id.tvBirthDate:
                final Calendar cldr1 = Calendar.getInstance();
                int day1 = cldr1.get(Calendar.DAY_OF_MONTH);
                int month1 = cldr1.get(Calendar.MONTH);
                int year1 = cldr1.get(Calendar.YEAR);
                // date picker dialog


                picker = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                // TODO: 2019-06-06 Date format change
                               /* String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                SimpleDateFormat input = new SimpleDateFormat("dd/MM/yy");
                                SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
                                try {
                                    Date oneWayTripDate = input.parse(date);                 // parse input
                                    et_end_date.setText(output.format(oneWayTripDate));    // format output

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }*/

                                // TODO: 2019-06-06 set date before "0"
                                Birthdate = String.valueOf(dayOfMonth);
                                if (Birthdate.toString().length() == 1) {
                                    Birthdate = "0" + Birthdate;
                                    Log.e("currentMinutes", "currentdate-->" + Birthdate);
                                }

                                Birthmonth = String.valueOf(monthOfYear + 1);
                                if (Birthmonth.toString().length() == 1) {
                                    Birthmonth = "0" + Birthmonth;
                                    Log.e("currentMinutes", "currentMinutes-->" + Birthmonth);
                                }

                                Calendar c = Calendar.getInstance();
                                currentyear = c.get(Calendar.YEAR);

                                NextBirthDate = Birthdate + "/" + Birthmonth + "/" + currentyear;
                                tvBirthDate.setText(Birthdate + "/" + Birthmonth + "/" + year);
                                //et_end_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                            }
                        }, year1, month1, day1);

                picker.getDatePicker().setMaxDate(System.currentTimeMillis());
                picker.show();
                break;

            case R.id.btn_show_data:

                tv_months.setText("00");
                tv_days.setText("00");
                tv_year.setText("00");
                tv_next_days.setText("00");
                tv_next_month.setText("00");

                mSimpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                String str = null;
                str = tvBirthDate.getText().toString();
                Log.e("str", "onClick: str-->" + str);
                if (!str.equals("") && !str.equals("Birth Date")) {

                    try {
                        birthDate = mSimpleDateFormat.parse(str);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //determine age
                    Period age = calcDiff(birthDate, new Date());
                    NewDateAns = PeriodFormat.wordBased().print(age);
                    Log.e("tag", "NewDateAns-->" + NewDateAns);


                    StringTokenizer tokens = new StringTokenizer(NewDateAns, ",");
                    String first = tokens.nextToken();
                    String second = tokens.nextToken();
                    String third = tokens.nextToken();
                    //String four = tokens.nextToken();

                    Log.e("dayss", "onClick:first--> " + first);
                    Log.e("dayss", "onClick:second--> " + second);
                    Log.e("dayss", "onClick:third--> " + third);

                    if (first != null) {
                        StringTokenizer fFirst = new StringTokenizer(first, " ");
                        String startFirst = fFirst.nextToken();
                        String endFirst = fFirst.nextToken();

                        if (endFirst.equals("years") || endFirst.equals("year")) {
                            tv_year.setText(startFirst);
                        } else if (endFirst.equals("month") || endFirst.equals("months")) {
                            tv_year.setText("00");
                            tv_months.setText(startFirst);
                        } else if (endFirst.equals("weeks") || endFirst.equals("week")) {
                            int weektodates = Integer.parseInt(startFirst);
                            tv_days.setText("" + weektodates * 7);
                        } else if (endFirst.equals("days") || endFirst.equals("day")) {
                            if (tv_months.toString() == null) {
                                tv_months.setText("00");
                            }
                            tv_days.setText(String.valueOf(Integer.parseInt(tv_days.getText().toString()) + Integer.parseInt(startFirst)));
                        }
                    }

                    if (second != null) {
                        StringTokenizer sSecond = new StringTokenizer(second, " ");
                        String startSecond = sSecond.nextToken();
                        String endSecond = sSecond.nextToken();

                        if (endSecond.equals("months") || endSecond.equals("month")) {
                            tv_months.setText(startSecond);
                        } else if (endSecond.equals("weeks") || endSecond.equals("week")) {
                            int weektodates = Integer.parseInt(startSecond);
                            tv_days.setText("" + weektodates * 7);
                        } else if (endSecond.equals("days") || endSecond.equals("day")) {
                            if (tv_months.toString() == null) {
                                tv_months.setText("00");
                            }
                            tv_days.setText(String.valueOf(Integer.parseInt(tv_days.getText().toString()) + Integer.parseInt(startSecond)));
                        }
                    }

                    if (third != null) {
                        StringTokenizer sThird = new StringTokenizer(third, " ");
                        String startThird = sThird.nextToken();
                        String endSThird = sThird.nextToken();

                        if (endSThird.equals("weeks") || endSThird.equals("week")) {
                            int weektodates = Integer.parseInt(startThird);
                            tv_days.setText("" + weektodates * 7);
                        } else if (endSThird.equals("days") || endSThird.equals("day")) {
                            Log.e("val", "onClick: show val-->" + Integer.parseInt(tv_days.getText().toString()) + Integer.parseInt(startThird));
                            tv_days.setText(String.valueOf(Integer.parseInt(tv_days.getText().toString()) + Integer.parseInt(startThird)));
                        }
                    }
                    GetTwoDatesDiffrance();
                } else {
                    Toast.makeText(activity, "Please Select Birth Date", Toast.LENGTH_SHORT).show();
                }


                break;

           /* case R.id.iv_currentDate:
                tv_currentDate.performClick();
                break;*/

            case R.id.iv_birthDate:
                tvBirthDate.performClick();
                break;

            case R.id.btn_clear_data:
                tv_months.setText("00");
                tv_days.setText("00");
                tv_year.setText("00");
                tvBirthDate.setText("Birth Date");
                tv_next_days.setText("00");
                tv_next_month.setText("00");
                break;


        }
    }

    private Period calcDiff(Date startDate, Date endDate) {
        DateTime START_DT = (startDate == null) ? null : new DateTime(startDate);
        DateTime END_DT = (endDate == null) ? null : new DateTime(endDate);

        Period period = new Period(START_DT, END_DT);

        return period;

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

    /*// TODO: 2019-06-26 Time Diffrance get from two times
    public void TwoDatesDiffranceGet() {
        try {
            Date userbirthdate = null;
            SimpleDateFormat dates1 = new SimpleDateFormat("dd/MM/yyyy");
            try {
                userbirthdate = dates1.parse(NextBirthDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            currentDate = new Date();
            Log.e("app", "userbirthdate currentDate" + currentDate);
            if (userbirthdate.compareTo(currentDate) > 0) {
                Log.i("app", "userbirthdate is after currentDate" + currentDate);

            } else if (userbirthdate.compareTo(currentDate) < 0) {
                Log.i("app", "userbirthdate is before currentDate");
                NextBirthDate = Birthdate + "/" + Birthmonth + "/" + (currentyear + 1);
            } else if (userbirthdate.compareTo(currentDate) == 0) {
                Log.i("app", "userbirthdate is equal to currentDate");
            }

            newEndDate = Birthmonth + "/" + Birthdate + "/" + (currentyear + 1);
            NextBirthDate = NextBirthDate + " 12:00:00 AM";

            monthsBetweenDates();

        } catch (Exception exception) {
            Log.e("TAG_5", "exception " + exception);
        }
    }
*/

    public void hello1() {
        try {
            String CurrentDate = newStartDate;
            String FinalDate = newEndDate;

            Log.e("val", "hello1: CurrentDate1-->" + CurrentDate);
            Log.e("val", "hello1: FinalDate1-->" + FinalDate);

            Date date1;
            Date date2;

            SimpleDateFormat dates = new SimpleDateFormat("MM/dd/yyyy");

            //Setting dates
            date1 = dates.parse(CurrentDate);
            date2 = dates.parse(FinalDate);

            Log.e("val", "hello1: date1" + date1);
            Log.e("val", "hello1: date2" + date2);

            long seconds = 1000;
            long minutes = 60 * seconds;
            long hours = 60 * minutes;
            long days = 24 * hours;
            long months = 30 * days;
            long year = 365 * days;

            //Comparing dates
            long difference = Math.abs(date1.getTime() - date2.getTime());

            difference = difference - (24 * 60 * 60 * 1000);

            long differenceDates = difference / (24 * 60 * 60 * 1000);

            //Convert long to String
            String dayDifference = Long.toString(differenceDates);

            Log.e("HERE", "HERE: " + dayDifference);


            /*long differenceInyear = difference / year;

            difference = difference - (differenceInyear * year);*/

            long differenceInMonth = difference / months;
            difference = difference - (differenceInMonth * months);

            long differenceInDays = difference / days;
            difference = difference - (differenceInDays * days);

            Log.e("daysss", "hello1: daysss-->" + differenceInMonth);
            Log.e("daysss", "hello1: daysss-->" + differenceInDays);

            int daydif = 0;
            int monthdif = 0;


            String[] curDateParts = CurrentDate.split("/");
            Log.e("vml", "hello: " + Integer.parseInt(curDateParts[0]));
            Log.e("vml", "hello: " + Integer.parseInt(curDateParts[1]));
            Log.e("vml", "hello: " + Integer.parseInt(curDateParts[2]));


            String[] finalDateParts = FinalDate.split("/");
            Log.e("vml", "hello: " + Integer.parseInt(finalDateParts[0]));
            Log.e("vml", "hello: " + Integer.parseInt(finalDateParts[1]));
            Log.e("vml", "hello: " + Integer.parseInt(finalDateParts[2]));

            int curDate = Integer.parseInt(curDateParts[1]);
            int curMonth = Integer.parseInt(curDateParts[0]);
            int curYear = Integer.parseInt(curDateParts[2]);

            int finalDate = Integer.parseInt(finalDateParts[1]);
            int finalMonth = Integer.parseInt(finalDateParts[0]);
            int finalYear = Integer.parseInt(finalDateParts[2]);

            int days1 = Integer.parseInt(dayDifference);
            Log.e("data", "hello: days1-->" + days1);

            if (days1 <= 31) {
                Log.e("data", "hello: days1 31-->" + days1);
                daydif = checkMonthDays(curMonth) - curDate;
                monthdif = 0;

            } else if (days1 <= 59) {
                Log.e("data", "hello: days1 59-->" + days1);
                monthdif = 1;
                daydif = checkMonthDays(curMonth) - curDate;

            } else if (days1 <= 90) {
                Log.e("data", "hello: days1 90-->" + days1);
                monthdif = 2;
                daydif = checkMonthDays(curMonth) - curDate;

            } else if (days1 <= 120) {

                Log.e("data", "hello: days1 120-->" + days1);
                monthdif = 3;
                daydif = checkMonthDays(curMonth) - curDate;

            } else if (days1 <= 151) {
                Log.e("data", "hello: days1 151-->" + days1);
                monthdif = 4;
                daydif = checkMonthDays(curMonth) - curDate;

            } else if (days1 <= 181) {
                Log.e("data", "hello: days1 181-->" + days1);
                monthdif = 5;
                daydif = checkMonthDays(curMonth) - curDate;

            } else if (days1 <= 212) {
                Log.e("data", "hello: days1 212-->" + days1);
                monthdif = 6;
                daydif = checkMonthDays(curMonth) - curDate;

            } else if (days1 <= 243) {

                Log.e("data", "hello: days1 243-->" + days1);
                monthdif = 7;
                daydif = checkMonthDays(curMonth) - curDate;

            } else if (days1 <= 273) {
                Log.e("data", "hello: days1 273-->" + days1);
                monthdif = 8;
                daydif = checkMonthDays(curMonth) - curDate;

            } else if (days1 <= 304) {
                Log.e("data", "hello: days1 304-->" + days1);
                monthdif = 9;
                daydif = checkMonthDays(curMonth) - curDate;

            } else if (days1 <= 334) {
                Log.e("data", "hello: days1 334-->" + days1);
                monthdif = 10;
                daydif = checkMonthDays(curMonth) - curDate;
            } else {
                Log.e("data", "hello: days1 defult-->" + days1);
                monthdif = 11;
                int i = 365 - days1;
                Log.e("i", "hello1: iiii-->" + i);
                daydif = checkMonthDays(curMonth) - curDate;
            }

            Log.e("HERE", "HERE: differenceInDays-->" + daydif);
            Log.e("HERE", "HERE: differenceInMonth-->" + monthdif);

        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);
        }
    }


    private int checkMonthDays(int month) {
        switch (month) {
            case 1:
                return 31;

            case 2:
                return 28;

            case 3:
                return 31;

            case 4:
                return 30;

            case 5:
                return 31;

            case 6:
                return 30;

            case 7:
                return 31;

            case 8:
                return 31;

            case 9:
                return 30;

            case 10:
                return 31;

            case 11:
                return 30;

            case 12:
                return 31;

            default:
                return 0;
        }
    }
 /*   public void hello() {
        try {
// String CurrentDate = "08/01/2019";
// String FinalDate = "07/31/2019";

            String CurrentDate = "08/01/2019";
            String FinalDate = "07/31/2019";

            SimpleDateFormat dates = new SimpleDateFormat("MM/dd/yyyy");


            String[] curDateParts = CurrentDate.split("/");
            Log.e("vml", "hello: " + Integer.parseInt(curDateParts[0]));
            Log.e("vml", "hello: " + Integer.parseInt(curDateParts[1]));
            Log.e("vml", "hello: " + Integer.parseInt(curDateParts[2]));


            String[] finalDateParts = FinalDate.split("/");
            Log.e("vml", "hello: " + Integer.parseInt(finalDateParts[0]));
            Log.e("vml", "hello: " + Integer.parseInt(finalDateParts[1]));
            Log.e("vml", "hello: " + Integer.parseInt(finalDateParts[2]));

            int curDate = Integer.parseInt(curDateParts[1]);
            int curMonth = Integer.parseInt(curDateParts[0]);
            int curYear = Integer.parseInt(curDateParts[2]);

            int finalDate = Integer.parseInt(finalDateParts[1]);
            int finalMonth = Integer.parseInt(finalDateParts[0]);
            int finalYear = Integer.parseInt(finalDateParts[2]);

            int daydif = 0;
            int monthdif = 0;

            //if (curYear == finalYear) {

            if (curMonth == finalMonth) {
                daydif = finalDate - curDate;

            } else {
                int tempMonthDIf = Math.abs(finalMonth - curMonth);

                if(tempMonthDIf == 1){
                    int remDayfinal = checkMonthDays(finalMonth) - finalDate;
                    int remDayCurrent = curDate;
                    daydif = remDayCurrent + remDayfinal;
                    monthdif = 0;
                }else{
                    int remDayfinal = checkMonthDays(finalMonth) - finalDate;
                    int remDayCurrent = curDate - 1;
                    daydif = remDayCurrent + remDayfinal;

                    monthdif = curMonth - finalMonth - 1;
                }

            }

            // }

            Log.d("vml", "hello() returned: " + daydif );
            Log.d("vml", "hello() returned: " + monthdif );

        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);
        }

    }
*/

    // TODO: 2019-08-06  get diffrance between two dates
    public void GetTwoDatesDiffrance() {
        int year = 0;
        int month = 0;
        int day = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Date birthDate = null;
        try {
            birthDate = simpleDateFormat.parse(tvBirthDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTimeInMillis(birthDate.getTime());
        Date todayDate = null;
        try {
            todayDate = simpleDateFormat.parse(tv_currentDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(todayDate.getTime());
        year = now.get(1) - birthDay.get(1);
        int currMonth = now.get(2) + 1;
        int birthMonth = birthDay.get(2) + 1;
        month = currMonth - birthMonth;
        if (month < 0) {
            year--;
            month = (12 - birthMonth) + currMonth;
            if (now.get(5) < birthDay.get(5)) {
                month--;
            }
        } else if (month == 0) {
            if (now.get(5) < birthDay.get(5)) {
                year--;
                month = 11;
            }
        }
        if (now.get(5) > birthDay.get(5)) {
            day = now.get(5) - birthDay.get(5);
        } else if (now.get(5) < birthDay.get(5)) {
            int today = now.get(5);
            now.add(1, -1);
            day = (now.getActualMaximum(5) - birthDay.get(5)) + today;
        } else {
            day = 0;
            if (month == 12) {
                year++;
                month = 0;
            }
        }
            /*if (month == 12) {
            nextMonth = 12 - month;

        } else {
            nextMonth = month;
        }*/

        int nextMonth = 12 - month;
        if (nextMonth > 0) {
            int nextMonth2 = nextMonth - 1;
            daysInMonth = new GregorianCalendar(currYear, currMonth, 0).getActualMaximum(5);
            int nextDay = daysInMonth - day;
            if (nextDay != 0) {
            }
            if (nextDay == daysInMonth) {
                nextMonth2++;
                nextDay = 0;
            }

            String CurrentDate = tv_currentDate.getText().toString();
            String FinalDate = tvBirthDate.getText().toString();

            SimpleDateFormat dates = new SimpleDateFormat("MM/dd/yyyy");


            String[] curDateParts = CurrentDate.split("/");

            String[] finalDateParts = FinalDate.split("/");

            int curDate = Integer.parseInt(curDateParts[0]);
            int curMonth = Integer.parseInt(curDateParts[1]);
            int curYear = Integer.parseInt(curDateParts[2]);

            int finalDate = Integer.parseInt(finalDateParts[0]);
            int finalMonth = Integer.parseInt(finalDateParts[1]);
            int finalYear = Integer.parseInt(finalDateParts[2]);
            Log.e("date", "GetTwoDatesDiffrance: "+curMonth);
            Log.e("date", "GetTwoDatesDiffrance: "+finalMonth);

            if (curMonth!= finalMonth){
                if ( curMonth > finalMonth ) {
                    nextMonth2 = nextMonth2 + 1;
                }
            }


            tv_next_month.setText("" + nextMonth2);
            tv_next_days.setText("" + nextDay);
        }

    }


    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
