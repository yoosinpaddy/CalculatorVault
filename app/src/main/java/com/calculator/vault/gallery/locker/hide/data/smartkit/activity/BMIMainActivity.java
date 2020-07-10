package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.BMIShareData;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.NativeAdvanceHelper;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.TinyDB;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.BMIModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BMIMainActivity extends AppCompatActivity implements View.OnClickListener {

    Activity activity;
    ImageView iv_back, iv_history;
    ImageView iv_female, iv_male;
    LinearLayout ll_calculate, ll_clear;
    String gen;
    AlertDialog alertDialog1;
    static Dialog d;
    private float selectedHeight;
    private float selectedWeight;
    String Tag;
    public static TinyDB tinyDB;
    public static ArrayList<BMIModel> arraydata = new ArrayList<>();
    private boolean isInForeGround = false;
    Spinner sp_age, sp_height, sp_weight;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_main_bmi);
        activity = BMIMainActivity.this;
        tinyDB = new TinyDB(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        tinyDB.putInt(BMIShareData.key_ml, 0);
        BMIShareData.mf = tinyDB.getInt(BMIShareData.key_ml);
        BMIShareData.hei = tinyDB.getInt(BMIShareData.key_he);
        BMIShareData.wei = tinyDB.getInt(BMIShareData.key_we);

        initView();
        initAction();

        iv_male.setAlpha(1f);
        iv_female.setAlpha(0.5f);

        if (Share.isNeedToAdShow(this)) {
            NativeAdvanceHelper.loadAdBannerSize(activity, (FrameLayout) findViewById(R.id.fl_adplaceholder));
        }

    }


    private void initView() {
        iv_male = findViewById(R.id.iv_male);
        iv_female = findViewById(R.id.iv_female);
        ll_calculate = findViewById(R.id.ll_calculate);
        ll_clear = findViewById(R.id.ll_clear);
        iv_back = findViewById(R.id.iv_back);
        iv_history = findViewById(R.id.iv_history);

        //et_age = findViewById(R.id.et_age);
        sp_height = findViewById(R.id.sp_height);
        sp_weight = findViewById(R.id.sp_weight);
        sp_age = (Spinner) findViewById(R.id.sp_age);
    }

    private void initAction() {
        iv_male.setOnClickListener(this);
        iv_female.setOnClickListener(this);
        ll_calculate.setOnClickListener(this);
        ll_clear.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_history.setOnClickListener(this);

        getAge();
        getWeight();
        geteHight();

    }

    private void geteHight() {
        List heightlist = new ArrayList<Integer>();
        for (int i = 1; i <= 274; i++) {
            heightlist.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, heightlist);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_height.setAdapter(adapter);

        sp_height.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);

                tinyDB.putInt(BMIShareData.key_he, BMIShareData.hei);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getWeight() {
        List weightlist = new ArrayList<Integer>();
        for (int i = 1; i <= 300; i++) {
            weightlist.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, weightlist);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_weight.setAdapter(adapter);

        sp_weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                tinyDB.putInt(BMIShareData.key_we, BMIShareData.wei);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    void getAge() {
        List age = new ArrayList<Integer>();
        for (int i = 1; i <= 110; i++) {
            age.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, age);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_age.setAdapter(adapter);

        sp_age.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private boolean isFirstTime() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }


    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void calculate_bni() {
        if ((sp_age.getSelectedItem().toString().length() == 0) && (sp_height.getSelectedItem().toString().length() == 0) && (sp_weight.getSelectedItem().toString().length() == 0)) {
            Toast.makeText(getApplicationContext(), "Please select valid field", Toast.LENGTH_LONG).show();
        } else if (sp_age.getSelectedItem().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please select your age", Toast.LENGTH_LONG).show();
        } else if (sp_height.getSelectedItem().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please select your height", Toast.LENGTH_LONG).show();
        } else if (sp_weight.getSelectedItem().toString().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please select your weight", Toast.LENGTH_LONG).show();
        } else {
            //>>>>>>>>>>>>>Calculate BMI value
            String age = getSelectedAge();
            float weight = getSelectedWeight();
            String height1 = getSelectedHeight_data();
            String weight1 = getSelectedweight_data();
            float height = getSelectedHeight();
            float bmiValue = calculateBMI(weight, height);
            // tv_1.setText(String.format("%.2f", BMIBMIShareData.bmidata));

            //>>>>>>>>>>>>>date
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            //new SimpleDateFormat ("yyyy.MM.dd  'at' hh:mm:ss a ");
            Date currentTime_1 = new Date();
            String dateString = formatter.format(currentTime_1);

            //>>>>>>>>>>>>>>>>>time
            SimpleDateFormat time = new SimpleDateFormat("hh:mm a");
            //new SimpleDateFormat ("yyyy.MM.dd  'at' hh:mm:ss a ");
            Date currentTime_2 = new Date();
            String timeString = time.format(currentTime_2);

            //>>>>>>>>>>>>> store data in share file
            BMIShareData.bmidata = bmiValue;
            BMIShareData.age = age;
            BMIShareData.height = height1;
            BMIShareData.weightdata = weight1;
            BMIShareData.date = dateString;
            BMIShareData.time = timeString;

            arraydata = tinyDB.getListObject(BMIShareData.model);
            BMIModel model = new BMIModel(BMIShareData.weightdata, BMIShareData.bmidata, BMIShareData.date, BMIShareData.time, BMIShareData.age, BMIShareData.height);
            arraydata.add(model);
            tinyDB.putListObject(BMIShareData.model, arraydata);

            Share.BMI_History = false;
            Intent intent = new Intent(this, BMICalculatorActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }

    private String getSelectedweight_data() {
        String str3 = sp_weight.getSelectedItem().toString();
        return (str3 + " kg");
    }

    private String getSelectedHeight_data() {
        String str2 = sp_height.getSelectedItem().toString();

        return (str2 + " cm");
    }


    private String getSelectedAge() {
        String str1 = sp_age.getSelectedItem().toString();
        BMIShareData.mf = tinyDB.getInt(BMIShareData.key_ml);
        Log.e("spage", "getSelectedAge: mf-->" + BMIShareData.mf);
        if (BMIShareData.mf == 0) {
            return (str1 + " (Male)");
        } else {
            return (str1 + " (Female)");
        }
    }

    private float calculateBMI(float weight, float height) {
        return (float) (weight / (height * height));
    }

    public float getSelectedHeight() {
        String str2 = sp_height.getSelectedItem().toString();
        return (float) (Float.parseFloat(str2) / 100);
    }

    public float getSelectedWeight() {
        String str3 = sp_weight.getSelectedItem().toString();
        return (float) (Float.parseFloat(str3));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_male:
                iv_male.setAlpha(1f);
                iv_female.setAlpha(0.5f);
                BMIShareData.mf = 0;
                tinyDB.putInt(BMIShareData.key_ml, BMIShareData.mf);
                break;

            case R.id.iv_female:
                iv_male.setAlpha(0.5f);
                iv_female.setAlpha(1f);
                BMIShareData.mf = 1;
                tinyDB.putInt(BMIShareData.key_ml, BMIShareData.mf);
                break;

            case R.id.ll_calculate:
                calculate_bni();
                break;

            case R.id.ll_clear:
                sp_age.setSelection(0);
                sp_height.setSelection(0);
                sp_weight.setSelection(0);
                break;

            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.iv_history:
                Share.BMI_History = true;
                Intent intent = new Intent(this, BMICalculatorActivity.class);
                startActivity(intent);
                break;
        }
    }
}





