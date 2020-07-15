package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.ads.AdListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.calculator.vault.gallery.locker.hide.data.smartkit.MainApplication;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.api.ApiClient;
import com.calculator.vault.gallery.locker.hide.data.smartkit.api.ApiInterface;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.CurrencyDetailModel;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.KeyModel;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.UnitConverterModel;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.UnitModel;
import com.calculator.vault.gallery.locker.hide.data.smartkit.webservice.Webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UnitConverterCalculationActivity extends BaseActivity implements View.OnTouchListener, View.OnClickListener {

    private boolean isSignedChanged = false;
    Boolean is_closed = true;
    public static int changeFromUnit = 0x01;
    public static int changeToUnit = 0x02;
    private String TAG = UnitConverterCalculationActivity.class.getSimpleName();
    private String fromValue = "1";
    private String title;
    private Double currencyValue = 0.0000;
    private int[] fullUnitNames2;
    private Double[][] unitValues;
    private String[] fullUnitNames, unitNames;

    private TextView tvFromUnit, tvToUnit;
    private EditText tvFrom, tvTo;
    private LinearLayout rlToUnit, rlFromUnit;
    private ImageView iv_seven, iv_four, iv_one, iv_zero, iv_eight, iv_five, iv_two, iv_dot, iv_nine, iv_six, iv_three, iv_plus_minus, iv_delete, iv_copy_text_to_clickBoard, iv_exchange_unit, iv_list;

    private UnitConverterCalculationActivity activity;
    private ArrayList<KeyModel> al_key = new ArrayList<>();
    private int key_index = 0;
    private ImageView iv_more_app, iv_blast;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_unit_converter_calculation);

        activity = UnitConverterCalculationActivity.this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        if (NetworkManager.isInternetConnected(activity)) {
            setToolbar();
            findViews();

            Log.e(TAG, "onCreate: key-->" + SharedPrefs.getString(activity, SharedPrefs.KEY));
            if (SharedPrefs.getString(activity, SharedPrefs.KEY).equals("")) {
                Log.e(TAG, "onCreate: key 2-->" + SharedPrefs.getString(activity, SharedPrefs.KEY));
                new Api().execute();
            }

            initArrays();
            initViews();
            iv_list.setEnabled(false);
            iv_list.setClickable(false);

            if (Share.isNeedToAdShow(this)) {
                setActionBar();
            }

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(UnitConverterCalculationActivity.this);
            builder.setMessage("").setTitle("No internet connection");
            builder.setMessage("Please check internet connection.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            finish();
                        }
                    });

            //Creating dialog box
            AlertDialog alert = builder.create();
            alert.show();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (NetworkManager.isInternetConnected(activity)) {
            if (SharedPrefs.getString(activity, SharedPrefs.KEY).equals("")) {
                new Api().execute();
            }

            initArrays();

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(UnitConverterCalculationActivity.this);
            builder.setMessage("").setTitle("No internet connection");
            builder.setMessage("Please check internet connection.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            finish();
                        }
                    });

            //Creating dialog box
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    private void setToolbar() {

        ImageView iv_back = findViewById(R.id.iv_back);
        TextView tv_title = findViewById(R.id.tv_title);

        title = getIntent().getStringExtra("title");

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onCli]k: onBackPressed");
                onBackPressed();
            }
        });
    }

    private void initArrays() {

        ArrayList<UnitConverterModel> unitConverterModels = new ArrayList<UnitConverterModel>();
        unitConverterModels = (ArrayList<UnitConverterModel>) getIntent().getSerializableExtra("UnitConverterModels");


        Share.currencyDetailModels.clear();
        try {
            JSONArray m_jArry = new JSONArray(loadJSONFromAsset());

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                Log.d("Details-->", jo_inside.getString("name"));

                //Add your values in your `ArrayList` as below:
                CurrencyDetailModel currencyDetailModel = new CurrencyDetailModel();
                currencyDetailModel.setCountry(jo_inside.getString("name"));
                currencyDetailModel.setCurrency_code(jo_inside.getString("currencyId"));
                currencyDetailModel.setSymbol(jo_inside.getString("currencySymbol"));
                currencyDetailModel.setName(jo_inside.getString("id"));
                currencyDetailModel.setFlag(jo_inside.getString("alpha3"));
                Share.currencyDetailModels.add(currencyDetailModel);
            }
            Log.e(TAG, "initArrays: Share.currencyDetailModels::" + Share.currencyDetailModels);

            fullUnitNames = new String[Share.currencyDetailModels.size()];
            fullUnitNames2 = new int[Share.currencyDetailModels.size()];
            unitNames = new String[Share.currencyDetailModels.size()];
            unitValues = new Double[Share.currencyDetailModels.size()][Share.currencyDetailModels.size()];

            for (int i = 0; i < Share.currencyDetailModels.size(); i++) {
                fullUnitNames[i] = Share.currencyDetailModels.get(i).getCountry();
//                        fullUnitNames2[i] = Integer.parseInt(Share.currencyDetailModels.get(i).getCurrency_code());
                unitNames[i] = Share.currencyDetailModels.get(i).getCurrency_code();

                       /* for (int j = 0; j < angleValues.length; j++) {
                            unitValues[i][j] = angleValues[i][j];

                        }*/
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //callCurrencyConverterAPI(unitNames[SharedPrefs.getInt(activity, title + getString(R.string._from))], unitNames[SharedPrefs.getInt(activity, title + getString(R.string._to))]);

        /*if (getIntent().getIntExtra("position", 0) != 8)
            getAnswer();*/
        callCurrencyConverterAPI(unitNames[SharedPrefs.getInt(activity, title + getString(R.string._from))], unitNames[SharedPrefs.getInt(activity, title + getString(R.string._to))]);


    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("countries_with_code.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void initializeArrays(String[] heatCapacityUnitList, int[] heatCapacityUnitList2, String[] heatCapacityUnits, double[][] angleValues) {
        fullUnitNames = new String[heatCapacityUnitList.length];
        fullUnitNames2 = new int[heatCapacityUnitList2.length];
        unitNames = new String[heatCapacityUnitList.length];
        unitValues = new Double[angleValues.length][angleValues.length];

        for (int i = 0; i < heatCapacityUnitList.length; i++) {
            fullUnitNames[i] = heatCapacityUnitList[i];
            fullUnitNames2[i] = heatCapacityUnitList2[i];
            unitNames[i] = heatCapacityUnits[i];

            for (int j = 0; j < angleValues.length; j++) {
                unitValues[i][j] = angleValues[i][j];

            }
        }

        for (int i = 0; i < fullUnitNames.length; i++) {
            Log.e(TAG, "initializeArrays: fullUnitNames i::" + fullUnitNames[i]);
        }

    }

    private void getAnswer() {
        Log.e(TAG, "getAnswer: " + getIntent().getIntExtra("position", 0));
        setCurrencyAnswer(currencyValue);
    }

    private void callCurrencyConverterAPI(final String from, final String to) {

        final Gson gson = new Gson();
        String str_key = SharedPrefs.getString(activity, SharedPrefs.KEY);
        Log.e(TAG, "callCurrencyConverterAPI str_key-->" + str_key);
        final KeyModel[] arr_key = gson.fromJson(str_key, KeyModel[].class);

        if (SharedPrefs.getInt(activity, SharedPrefs.KEY_INDEX) == -1) {
            SharedPrefs.save(activity, SharedPrefs.KEY_INDEX, 0);
        }
        key_index = SharedPrefs.getInt(activity, SharedPrefs.KEY_INDEX, 0);

        Log.e(TAG, "callCurrencyConverterAPI: val-->" + key_index);


        if (key_index >= Arrays.asList(arr_key).size()) {
            key_index = 0;
            SharedPrefs.save(activity, SharedPrefs.KEY_INDEX, key_index);
        }


        //  url = url.replace("","\n");

        //Log.e("Vimal", "key in api final:==>" + Arrays.asList(arr_key).get(SharedPrefs.getInt(activity, SharedPrefs.KEY_INDEX)).getApi_key());


        ShowProgressDialog(activity, getString(R.string.please_wait));
        try {
            Log.e(TAG, "callCurrencyConverterAPI: ::" + from + "_" + to);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            Call<JsonObject> stringCall = apiService.getCurrencyValue(from + "_" + to, "ultra", Arrays.asList(arr_key).get(SharedPrefs.getInt(activity, SharedPrefs.KEY_INDEX)).getApi_key());
            stringCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    key_index++;
                    SharedPrefs.save(activity, SharedPrefs.KEY_INDEX, key_index);

                    hideProgressDialog();
                    if (response.body() != null) {
                        //JsonObject gson = new JsonParser().parse(response.body().toString()).getAsJsonObject();
                        // try {
                        /*JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                        JSONObject fullResponse = jsonObject.getJSONObject(from + "_" + to);
                        Log.e(TAG, "onResponse: fullResponse " + fullResponse.getDouble("val"));

                        currencyValue = fullResponse.getDouble("val");*/
                        currencyValue = Double.parseDouble(String.valueOf(response.body().get(from + "_" + to)));
                        setCurrencyAnswer(currencyValue);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UnitConverterCalculationActivity.this);
                        builder.setMessage("").setTitle("Service Unavailable");
                        builder.setMessage("Please try later")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });

                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                    // } catch (JSONException e) {
                    //    e.printStackTrace();
                    //}
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "onFailure: stringCall ::" + t.getMessage());
                    hideProgressDialog();
                }
            });
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            hideProgressDialog();
        }
    }

    private void setCurrencyAnswer(Double fullResponse) {
        Log.e(TAG, "setCurrencyAnswer: ");
        if (!tvFrom.getText().toString().equals("")) {

            tvTo.setText(new DecimalFormat("#.####").format(Double.parseDouble(tvFrom.getText().toString()) * fullResponse));

            if (tvTo.getText().toString().equals("0")) {
                tvTo.setText("0.0000");
            }
        } else {
            tvFrom.setText("0");
            tvTo.setText("0.0000");
        }
        tvTo.setSelection(tvTo.getText().toString().length());
    }


    private void findViews() {

        tvFrom = findViewById(R.id.tvFrom);
        tvFromUnit = findViewById(R.id.tvFromUnit);
        tvTo = findViewById(R.id.tvTo);
        tvToUnit = findViewById(R.id.tvToUnit);
        iv_seven = findViewById(R.id.iv_seven);
        iv_four = findViewById(R.id.iv_four);
        iv_one = findViewById(R.id.iv_one);
        iv_zero = findViewById(R.id.iv_zero);
        iv_eight = findViewById(R.id.iv_eight);
        iv_five = findViewById(R.id.iv_five);
        iv_dot = findViewById(R.id.iv_dot);
        iv_zero = findViewById(R.id.iv_zero);
        iv_two = findViewById(R.id.iv_two);
        iv_nine = findViewById(R.id.iv_nine);
        iv_nine = findViewById(R.id.iv_nine);
        iv_six = findViewById(R.id.iv_six);
        iv_three = findViewById(R.id.iv_three);
        iv_plus_minus = findViewById(R.id.iv_plus_minus);
        iv_delete = findViewById(R.id.iv_delete);
        iv_copy_text_to_clickBoard = findViewById(R.id.iv_copy_text_to_clickBoard);
        iv_exchange_unit = findViewById(R.id.iv_exchange_unit);
        iv_list = findViewById(R.id.iv_list);
        iv_delete = findViewById(R.id.iv_delete);
        rlFromUnit = findViewById(R.id.rlFromUnit);
        rlToUnit = findViewById(R.id.rlToUnit);
        iv_more_app = findViewById(R.id.iv_more_app);
        iv_blast = findViewById(R.id.iv_blast);
    }

    private void initViews() {

        tvTo.setSingleLine();
        tvFrom.setSingleLine();

        iv_seven.setOnTouchListener(this);
        iv_four.setOnTouchListener(this);
        iv_one.setOnTouchListener(this);
        iv_eight.setOnTouchListener(this);
        iv_five.setOnTouchListener(this);
        iv_dot.setOnTouchListener(this);
        iv_zero.setOnTouchListener(this);
        iv_six.setOnTouchListener(this);
        iv_three.setOnTouchListener(this);
        iv_two.setOnTouchListener(this);
        iv_nine.setOnTouchListener(this);
        iv_plus_minus.setOnTouchListener(this);
        iv_delete.setOnTouchListener(this);
        iv_list.setOnTouchListener(this);
        iv_exchange_unit.setOnTouchListener(this);
        iv_copy_text_to_clickBoard.setOnTouchListener(this);
        tvFrom.setOnTouchListener(this);
        tvTo.setOnTouchListener(this);

        iv_seven.setOnClickListener(this);
        iv_four.setOnClickListener(this);
        iv_one.setOnClickListener(this);
        iv_eight.setOnClickListener(this);
        iv_five.setOnClickListener(this);
        iv_dot.setOnClickListener(this);
        iv_zero.setOnClickListener(this);
        iv_six.setOnClickListener(this);
        iv_three.setOnClickListener(this);
        iv_two.setOnClickListener(this);
        iv_nine.setOnClickListener(this);
        iv_plus_minus.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        iv_copy_text_to_clickBoard.setOnClickListener(this);
        iv_exchange_unit.setOnClickListener(this);
        iv_list.setOnClickListener(this);
//        tvFromUnit.setOnClickListener(this);
        tvFrom.setOnClickListener(this);
        tvTo.setOnClickListener(this);
        rlFromUnit.setOnClickListener(this);
        rlToUnit.setOnClickListener(this);


        setInitialValues(getIntent().getIntExtra("position", 0));

        tvFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e(TAG, "afterTextChanged: ");

                getAnswer();

            }
        });

    }

    private void setInitialValues(int position) {

        if (!SharedPrefs.contain(activity, title + getString(R.string._from))) {
            SharedPrefs.save(activity, title + getString(R.string._from), 0);
        } else {
            //save value which was selected by user
            tvFromUnit.setText(fullUnitNames[SharedPrefs.getInt(activity, title + getString(R.string._from))]);
        }

        if (!SharedPrefs.contain(activity, title + getString(R.string._to))) {
            SharedPrefs.save(activity, title + getString(R.string._to), 1);
        } else {
            //save value which was selected by user
            tvToUnit.setText(fullUnitNames[SharedPrefs.getInt(activity, title + getString(R.string._to))]);
        }

        setTextViews();
    }

    private void setTextViews() {
        tvFromUnit.setText(fullUnitNames[SharedPrefs.getInt(activity, title + getString(R.string._from))]);
        tvToUnit.setText(fullUnitNames[SharedPrefs.getInt(activity, title + getString(R.string._to))]);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (v.getId() == R.id.tvFrom || v.getId() == R.id.tvTo) {
            return false;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    v.setAlpha(.2f);
                    v.callOnClick();
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    v.setAlpha(1f);
                    break;
                }
            }
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClick: ");
        switch (v.getId()) {
            case R.id.iv_seven:
                setFromTextView("7");
                break;
            case R.id.iv_four:
                setFromTextView("4");
                break;
            case R.id.iv_one:
                setFromTextView("1");
                break;
            case R.id.iv_zero:
                setFromTextView("0");
                break;
            case R.id.iv_eight:
                setFromTextView("8");
                break;
            case R.id.iv_five:
                setFromTextView("5");
                break;
            case R.id.iv_dot:
                if (!tvFrom.getText().toString().contains("."))
                    setFromTextView(".");
                break;
            case R.id.iv_six:
                setFromTextView("6");
                break;
            case R.id.iv_three:
                setFromTextView("3");
                break;
            case R.id.iv_two:
                setFromTextView("2");
                break;
            case R.id.iv_nine:
                setFromTextView("9");
                break;
            case R.id.iv_plus_minus:
                if (!tvFrom.getText().toString().equals("0")) {
                    if (!isSignedChanged) {
                        isSignedChanged = true;
                        fromValue = "-" + tvFrom.getText().toString();
                        tvFrom.setText(fromValue);
                    } else {
                        if (tvFrom.getText().toString().contains("-")) {
                            isSignedChanged = false;
                            tvFrom.setText(tvFrom.getText().toString().replace("-", ""));
                        }
                    }
                }
                break;
            case R.id.iv_delete:
//                if (!tvFrom.getText().equals("1"))
                if (tvFrom.getText().toString().length() == 2 && tvFrom.getText().toString().contains("-")) {
                    isSignedChanged = false;
                    tvFrom.setText("0");
                    tvTo.setText("0.0000");
                } else {
                    tvFrom.setText(removeLastCharacter(tvFrom.getText().toString()));
                }
                break;
            case R.id.iv_copy_text_to_clickBoard:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", tvFrom.getText().toString() + " " + tvFromUnit.getText().toString() + " = " + tvTo.getText().toString() + " " + tvToUnit.getText().toString());
                clipboard.setPrimaryClip(clip);
                break;
            case R.id.iv_exchange_unit:

                // TODO: 7/31/2018 alternate values
                int fromValue = SharedPrefs.getInt(activity, title + getString(R.string._from));
                int toValue = SharedPrefs.getInt(activity, title + getString(R.string._to));
                SharedPrefs.save(activity, title + getString(R.string._from), toValue);
                SharedPrefs.save(activity, title + getString(R.string._to), fromValue);

                // TODO: 7/31/2018 set textviews again and get answer
                setTextViews();

             /*   if (getIntent().getIntExtra("position", 0) == 8)
                    callCurrencyConverterAPI(unitNames[SharedPrefs.getInt(activity, title + getString(R.string._from))], unitNames[SharedPrefs.getInt(activity, title + getString(R.string._to))]);
                else*/
                //  getAnswer();
                if (NetworkManager.isInternetConnected(activity)) {
                    if (SharedPrefs.getString(activity, SharedPrefs.KEY).equals("")) {
                        new Api().execute();
                    }
                    initArrays();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UnitConverterCalculationActivity.this);
                    builder.setMessage("").setTitle("No internet connection");
                    builder.setMessage("Please check internet connection.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                break;
            case R.id.iv_list:

                startNextListActivity();

                /*if (Share.isNeedToAdShow(getApplicationContext())) {
                    if (!MainApplication.getInstance().requestNewInterstitial()) {
                        startNextListActivity();
                    } else {
                        MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();

                                MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                                MainApplication.getInstance().mInterstitialAd = null;
                                MainApplication.getInstance().ins_adRequest = null;
                                MainApplication.getInstance().LoadAds();

                                startNextListActivity();

                            }

                            @Override
                            public void onAdFailedToLoad(int i) {
                                super.onAdFailedToLoad(i);
                                Log.e(TAG, "onAdFailedToLoad: " + "");
                            }

                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();

                            }
                        });
                    }
                } else {
                    startNextListActivity();
                }*/

                break;
            case R.id.rlFromUnit:
                changeUnitValue(getIntent().getIntExtra("position", 0), "fromUnit", "from");
                break;
            case R.id.rlToUnit:
                changeUnitValue(getIntent().getIntExtra("position", 0), "toUnit", "to");
                break;
            case R.id.iv_more_app:
                /*is_closed = false;
                iv_more_app.setVisibility(View.GONE);
                iv_blast.setVisibility(View.VISIBLE);
                ((AnimationDrawable) iv_blast.getBackground()).start();

                if (MainApplication.getInstance().requestNewInterstitial()) {

                    MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            Log.e("ad cloced", "ad closed");
                            iv_blast.setVisibility(View.GONE);
                            iv_more_app.setVisibility(View.GONE);
                            is_closed = true;
                        *//*iv_more_app.setBackgroundResource(R.drawable.animation_list_filling);
                        ((AnimationDrawable) iv_more_app.getBackground()).start();*//*
                            loadInterstialAd();
                        }

                        @Override
                        public void onAdFailedToLoad(int i) {
                            super.onAdFailedToLoad(i);
                            Log.e("fail", "fail");
                            iv_blast.setVisibility(View.GONE);
                            iv_more_app.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            Log.e("loaded", "loaded");
                            is_closed = false;
                            iv_blast.setVisibility(View.GONE);
                            iv_more_app.setVisibility(View.GONE);
                        }
                    });
                } else {
                    Log.e("else", "else");
                    iv_blast.setVisibility(View.GONE);
                    iv_more_app.setVisibility(View.GONE);
                }*/
                break;
            case R.id.tvFrom:
                hideKeyboard(tvFrom);
                break;
            case R.id.tvTo:
                hideKeyboard(tvTo);
                break;
        }

        tvFrom.setSelection(tvFrom.getText().toString().length());
    }

    private void hideKeyboard(TextView textView) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
    }

    private void startNextListActivity() {
        Intent intent = new Intent(activity, UnitConverterinListActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("UnitFullNames", fullUnitNames);
        intent.putExtra("UnitFullNames2", fullUnitNames2);
        intent.putExtra("UnitNames", unitNames);

        Bundle mBundle = new Bundle();
        mBundle.putSerializable("UnitValues", unitValues);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    private void changeUnitValue(int position, final String unit, final String button) {

        nextChooseUnitActivity(unit, button);
        /*if (Share.isNeedToAdShow(getApplicationContext())) {

            if (!MainApplication.getInstance().requestNewInterstitial()) {
                nextChooseUnitActivity(unit, button);
            } else {
                MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();

                        MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                        MainApplication.getInstance().mInterstitialAd = null;
                        MainApplication.getInstance().ins_adRequest = null;
                        MainApplication.getInstance().LoadAds();

                        nextChooseUnitActivity(unit, button);

                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        Log.e(TAG, "onAdFailedToLoad: " + "");
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();

                    }
                });
            }
        } else {
            nextChooseUnitActivity(unit, button);
        }*/
    }

    private void nextChooseUnitActivity(String unit, String button) {
        Intent intent = new Intent(activity, ChooseUnitActivity.class);

        intent.putExtra("title", title);
        intent.putExtra("button", button);
        intent.putExtra("UnitFullNames", fullUnitNames);
        intent.putExtra("UnitNames", unitNames);

        if (unit.equals("fromUnit")) {
            startActivityForResult(intent, changeFromUnit);
        } else {
            startActivityForResult(intent, changeToUnit);
        }
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    private void setFromTextView(String value) {
        // TODO: 7/31/2018 if 0 in textview then replace value that inserted by user otherwise concat string and if dot is entered by user then it should be concated
        if (!value.equals(".")) {
            if (tvFrom.getText().toString().equals("0")) {
                tvFrom.setText(value);
            } else {
                fromValue = tvFrom.getText().toString() + value;
                tvFrom.setText(fromValue);
            }
        } else {
            fromValue = tvFrom.getText().toString() + value;
            tvFrom.setText(fromValue);
        }
    }

    public String removeLastCharacter(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: ");

        if (data != null) {
            ArrayList<UnitModel> unitModels = new ArrayList<UnitModel>();
            unitModels = (ArrayList<UnitModel>) data.getSerializableExtra("UnitModels");

            int selectedPosition = data.getIntExtra("SelectedUnitPosition", 0);

            // TODO: 8/4/2018 for searched data click getting proper position
           /* for (int i = 0; i < fullUnitNames.length; i++) {
                if (data.getStringExtra("SelectedFullUnit").equals(fullUnitNames[i])) {
                    selectedPosition = i;
                }
            }*/
            Log.e(TAG, "onActivityResult: selectedPosition::" + selectedPosition);

            // TODO: 8/4/2018 set unit name textviews
            if (requestCode == changeFromUnit) {
//                setUnitTexts2(tvFromUnit, data, unitModels);
                setUnitTexts(tvFromUnit, fullUnitNames, selectedPosition);
                SharedPrefs.save(activity, title + getString(R.string._from), selectedPosition);
            } else if (requestCode == changeToUnit) {
//                setUnitTexts2(tvToUnit, data, unitModels);
                setUnitTexts(tvToUnit, fullUnitNames, selectedPosition);
                SharedPrefs.save(activity, title + getString(R.string._to), selectedPosition);
            }

            // TODO: 8/4/2018 change answer textviews when unit changes
            Log.e(TAG, "onActivityResult: position::: " + getIntent().getIntExtra("position", 0));
            if (getIntent().getIntExtra("position", 0) == 8)
                callCurrencyConverterAPI(unitNames[SharedPrefs.getInt(activity, title + getString(R.string._from))], unitNames[SharedPrefs.getInt(activity, title + getString(R.string._to))]);
            else
                getAnswer();
        }
    }

    private void setUnitTexts(TextView textView, String[] unitModels, int selectedPosition) {
        textView.setText(unitModels[selectedPosition]);
    }

    private void setUnitTexts2(TextView textView, Intent data, ArrayList<UnitModel> unitModels) {
        Log.e(TAG, "setUnitTexts: unitModels::" + unitModels);
        textView.setText(unitModels.get(data.getIntExtra("SelectedUnitPosition", 0)).getUnitFullName());
    }

    private void setAnswerValue(double[][] angleValues) {

        Log.e(TAG, "setAnswerValue: ");
        if (!tvFrom.getText().toString().equals("")) {
            Log.e(TAG, "setAnswerValue: if");
            if (tvFrom.getText().toString().equals("-")) {

                Log.e("calculas", "setAnswerValue: DecimalFormat" + Double.parseDouble(new DecimalFormat(".########").format(Double.parseDouble(tvFrom.getText().toString()) * angleValues[SharedPrefs.getInt(activity, title + getString(R.string._from))][SharedPrefs.getInt(activity, title + getString(R.string._to))])));

                tvTo.setText(new DecimalFormat("#.####").format(Double.parseDouble(tvFrom.getText().toString()) * angleValues[SharedPrefs.getInt(activity, title + getString(R.string._from))][SharedPrefs.getInt(activity, title + getString(R.string._to))]));
//                if (tvTo.getText().toString().equals("0")) {
//                tvTo.setText("0.0000");
//                }
            } else {
              /*  Double answer = Double.parseDouble(tvFrom.getText().toString()) * angleValues[SharedPrefs.getInt(activity, title + getString(R.string._from))][SharedPrefs.getInt(activity, title + getString(R.string._to))];
                tvTo.setText(answer.doubleValue() + "");*/

                tvTo.setText(new DecimalFormat("#.####").format(Double.parseDouble(tvFrom.getText().toString()) * angleValues[SharedPrefs.getInt(activity, title + getString(R.string._from))][SharedPrefs.getInt(activity, title + getString(R.string._to))]));
            }
        } else {
            Log.e(TAG, "setAnswerValue: else");
            tvFrom.setText("0");
            tvTo.setText("0.0000");
        }
        tvTo.setSelection(tvTo.getText().toString().length());
    }

    public class Api extends AsyncTask<String, Void, Void> {
        String jsonStr;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                String url = "";
                jsonStr = Webservice.GET("http://139.59.86.230/voicecalculator/public/api/showapi");
                Log.e("TAG", "-->>" + url);
            } catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("TAG", "Response from url: " + jsonStr);

            if (jsonStr != null && !jsonStr.isEmpty()) {
                try {
                    al_key.clear();
                    JSONObject jsonObject = new JSONObject(jsonStr);

                    if (jsonObject.getString("status").equals("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject key_object = jsonArray.getJSONObject(i);
                            KeyModel keyModel = new KeyModel();
                            //keyModel.setApi_key(Share.decrypt(Share.pas.getBytes(), key_object.getString("apikey")));
                            keyModel.setApi_key(key_object.getString("apikey"));
                            al_key.add(keyModel);
                        }

                        Gson gson = new Gson();
                        String jsonad = gson.toJson(al_key);
                        Log.e(TAG, "onPostExecute: jsonad " + jsonad);
                        SharedPrefs.save(activity, SharedPrefs.KEY, jsonad);

                        Log.e("TAG", "key:==>" + SharedPrefs.getString(activity, SharedPrefs.KEY));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
