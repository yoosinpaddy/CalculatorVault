package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.calculator.vault.gallery.locker.hide.data.smartkit.MainApplication;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.adapter.CustomSpinnerAdapter;
import com.calculator.vault.gallery.locker.hide.data.smartkit.adapter.UnitConverterInListAdapter;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.Share;


public class UnitConverterinListActivity extends AppCompatActivity implements View.OnClickListener {

    Boolean is_closed = true;
    public static int selectedFromPosition = 0;
    private String TAG = UnitConverterinListActivity.class.getSimpleName();
    private String[] unitFullNamesArray;
    private int[] unitFullNamesArray2;
    private String[] unitNamesArray;
    private Double[][] unitValues;

    public static EditText etFrom;
    private LinearLayout flFromUnit;
    private TextView tvFromUnit;
    private RecyclerView rv_unit_list;
    private RecyclerView rv_spinner_items;
    private ImageView iv_more_app, iv_blast, iv_down_arrow;

    PopupWindow popupWindow;
    ViewTreeObserver vto;

    UnitConverterInListAdapter adapter;
    CustomSpinnerAdapter mAdapter;

    private UnitConverterinListActivity activity;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_unit_converterin_list);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        activity = UnitConverterinListActivity.this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setToolbar();
        findViews();
        initViews();
        if (Share.isNeedToAdShow(this)) {
            setActionBar();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private void setToolbar() {
        ImageView iv_back = findViewById(R.id.iv_back);
        TextView tv_title = findViewById(R.id.tv_title);


        tv_title.setText(getIntent().getStringExtra("title"));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void findViews() {
        rv_unit_list = findViewById(R.id.rv_unit_list);
        etFrom = findViewById(R.id.etFrom);
        flFromUnit = findViewById(R.id.flFromUnit);
        tvFromUnit = findViewById(R.id.tvFromUnit);
        iv_down_arrow = findViewById(R.id.iv_down_arrow);
        iv_more_app = findViewById(R.id.iv_more_app);
        iv_blast = findViewById(R.id.iv_blast);
    }

    private void initViews() {

        final InputFilter inputFilter1 = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                try {
                    Character c = charSequence.charAt(0);
                    if (!Character.isDigit(c)) {
                        if (c == '.') {
                            if (etFrom.getText().toString().contains(".")) {
                                return "0.";
                            } else {
                                return charSequence;
                            }

                        } else {
                            return "";
                        }
                    } else {
                        return charSequence;
                    }
                } catch (Exception e) {
                }
                return null;
            }
        };

        etFrom.setFilters(new InputFilter[]{inputFilter1});
        etFrom.setSingleLine();
        etFrom.setLongClickable(false);
        etFrom.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        rv_unit_list.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_unit_list.setLayoutManager(layoutManager);

       /* LayoutMarginDecoration layoutMargin = new LayoutMarginDecoration(0, 0);

        layoutMargin.setPadding(rv_unit_list, 10);*/

        unitFullNamesArray = getIntent().getStringArrayExtra("UnitFullNames");
        unitFullNamesArray2 = getIntent().getIntArrayExtra("UnitFullNames2");
        unitNamesArray = getIntent().getStringArrayExtra("UnitNames");

        tvFromUnit.setText(unitFullNamesArray[0]);

        try {
            Object[] objectArray = (Object[]) getIntent().getExtras().getSerializable("UnitValues");
            if (objectArray != null) {
                unitValues = new Double[objectArray.length][];
                for (int i = 0; i < objectArray.length; i++) {
                    unitValues[i] = (Double[]) objectArray[i];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e(TAG, "initViews: unitValues::" + unitValues);
        adapter = new UnitConverterInListAdapter(getApplicationContext(), getIntent().getStringExtra("title"), selectedFromPosition, unitFullNamesArray, unitFullNamesArray2, unitNamesArray, unitValues);
        rv_unit_list.setAdapter(adapter);

        tvFromUnit.setOnClickListener(this);
        iv_down_arrow.setOnClickListener(this);

        etFrom.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do here your stuff f
                    Log.e(TAG, "onEditorAction: done ");
                    if (etFrom.getText().toString().equals(".")) {
                        Log.e(TAG, "onEditorAction: if");
                        etFrom.setText("0.");
                    } else {
                        Log.e(TAG, "onEditorAction: else");
                        return true;
                    }
                }
                return false;
            }
        });

        etFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 1) {
                    String str = s.toString();
                    String strold = str.substring(0, str.length() - 2);
                    String lastchar = str.substring(str.length() - 1);
                    if (strold.contains(".") && lastchar.equals("."))
                        etFrom.setText(str.substring(0, str.length() - 2));
                }

                if (!s.toString().equals(".")) {
                    adapter.notifyDataSetChanged();
                } else {
                    etFrom.setText("0.");
                }
            }
        });


        View popupLayout = getLayoutInflater().inflate(R.layout.s_spinner_items, null);
        popupWindow = new PopupWindow(activity);
        popupWindow.setContentView(popupLayout);

        vto = flFromUnit.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {

                flFromUnit.getViewTreeObserver().removeOnPreDrawListener(this);
                int imgFinalWidth = flFromUnit.getMeasuredWidth();
                popupWindow.setWidth(imgFinalWidth);
                return true;
            }
        });

        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        rv_spinner_items = (RecyclerView) popupLayout.findViewById(R.id.rv_spinner_items);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        rv_spinner_items.setLayoutManager(mLayoutManager);
        rv_spinner_items.setItemAnimator(new DefaultItemAnimator());
        rv_spinner_items.setHasFixedSize(true);

        //Close the popup when touch outside
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

       /* ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, Share.stateArrayList);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_state.setAdapter(stateAdapter);

        cityArrayList = Arrays.asList(getResources().getStringArray(R.array.indian_cities));*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvFromUnit:
                openPopUp(v);
                break;
            case R.id.iv_down_arrow:
                openPopUp(v);
                break;
        }
    }

    private void openPopUp(View v) {
        Log.e(TAG, "onClick:flFromUnit ");
        InputMethodManager imm1 = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm1.hideSoftInputFromWindow(tvFromUnit.getWindowToken(), 0);
        openPopupWindow(v);
    }

    private void openPopupWindow(View v) {
        mAdapter = new CustomSpinnerAdapter(activity, unitFullNamesArray, new CustomSpinnerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Log.e(TAG, "onItemClick: position::" + position);

                selectedFromPosition = position;
                adapter.notifyDataSetChanged();
                tvFromUnit.setText(unitFullNamesArray[position]);
                popupWindow.dismiss();
            }
        });
        rv_spinner_items.setAdapter(mAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            popupWindow.showAsDropDown(v, 0, 0, Gravity.CENTER_HORIZONTAL);
        }

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                rootLayout.setAlpha(1.0f);
//                rootLayout.setBackgroundColor(getResources().getColor(R.color.trasparent2));
            }
        });
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
            if (Share.isNeedToAdShow(this)){
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
