package com.calculator.vault.gallery.locker.hide.data.smartkit.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.calculator.vault.gallery.locker.hide.data.smartkit.Database.DBHelperClass;
import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.calcnew.CalculateFactorial;
import com.calculator.vault.gallery.locker.hide.data.smartkit.calcnew.ExtendedDoubleEvaluator;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.SharedPrefs;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ScientificCalculatorActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    int screen_width;
    int screen_height;
    ImageView iv_clear, iv_seven, iv_four, iv_one, iv_zero;//row one
    ImageView iv_percent, iv_eight, iv_five, iv_two, iv_dot;// row two
    ImageView iv_divide, iv_nine, iv_six, iv_three, iv_plus_minus;// row three
    ImageView iv_multiply, iv_minus, iv_plus, iv_equals,iv_back;// row four
    //ImageView iv_square_root, iv_setting, iv_watch_history;
    EditText et_main;
    EditText tv_Display;
    boolean flag = false;
    DBHelperClass dbHelperClass;
    TextView tv_divide;
    public static TextView /*tv_edit, tv_delete_all,*/ dlg_message, tv_degree;
    LinearLayout ll_delete;
    LinearLayout ll_expand, ll_calc, ll_delet, ll_back;
    RelativeLayout rl_calc_layout;

    //.........................scientific calculator variables................//
    ImageView iv_10_raised_to_x, iv_2nd, iv_log10, iv_sin, iv_sinh, iv_cos, iv_cosh, iv_tan, iv_tanh, iv_x_square,
            iv_bracket_left, iv_bracket_right, iv_x_cube, iv_x_raised_to_y, iv_e_raised_to_x, iv_one_by_x, iv_under_root_x,
            iv_ln, iv_3_under_root_x, iv_y_under_root_x, iv_rad, iv_Rand, iv_pi, iv_e, iv_x_exclamation, iv_mplus, iv_mc, iv_m_minus, iv_mr, iv_EE;
    TextView tv_10_raised_to_x, tv_log10, tv_sin, tv_sinh, tv_cos, tv_cosh, tv_tan, tv_tanh, tv_rad;

    //........................................................................//
    Editable expression;
    Double Answer;
    String display = "";
    String string_inside_bracket = "";
    int count_dot = 0;
    int press = 0;
    int counter_left_bracket = 0;
    int counter_right_bracket = 0;
    Button btn_yes, btn_no;
    Boolean flag_equals = false;
    Boolean flag_random = false;
    Boolean flag_plus = false;
    Boolean flag_minus = false;
    Boolean flag_multiply = false;
    Boolean flag_divide = false;
    Boolean flag_moulo = false;
    Boolean flag_plus_minus = false;
    Boolean flag_bracket_left = false;
    Boolean flag_bracket_right = false;
    Boolean press_plus_minus = true;
    Boolean press_2nd = true;
    Boolean SWITCH = true;
    Boolean flag_radian = true;
    public static String Radian_Degree = "DEG";
    Boolean flag_string_inside_brackets = false;
    Boolean flag_string_inside_brackets_completed = false;
    Boolean Equal_pressed_Mode = false;
    Double Memory = 0.0;
    Boolean Flag_add_bracket = false;
    int Theme_button1, Theme_button2, Theme_button3, Theme_button_expand;
    int no_format;


    //Radian to degree conversion
    String trigo = "";

    //aNIMATION
    private AlphaAnimation click_anim;

    //variables
    Double m_instance;

    //interstial
    boolean isInForeGround;

    //Drawer
    public static LinearLayout llNavigationMenu;

    private FirebaseAnalytics mFirebaseAnalytics;
    private boolean equal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.s_activity_scientific_calculator_lanscape);
        initviews();
        initlisteners();

        dbHelperClass = new DBHelperClass(getApplicationContext());
        click_anim = new AlphaAnimation(1F, 0.5F);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }


    private void initviews() {
        //row one initialization
        iv_clear = (ImageView) findViewById(R.id.iv_clear);
        iv_seven = (ImageView) findViewById(R.id.iv_seven);
        iv_four = (ImageView) findViewById(R.id.iv_four);
        iv_one = (ImageView) findViewById(R.id.iv_one);
        iv_zero = (ImageView) findViewById(R.id.iv_zero);

        //row two initialization
        iv_percent = (ImageView) findViewById(R.id.iv_percent);
        iv_eight = (ImageView) findViewById(R.id.iv_eight);
        iv_five = (ImageView) findViewById(R.id.iv_five);
        iv_two = (ImageView) findViewById(R.id.iv_two);
        iv_dot = (ImageView) findViewById(R.id.iv_dot);

        //row three initialization
        iv_divide = (ImageView) findViewById(R.id.iv_divide);
        iv_nine = (ImageView) findViewById(R.id.iv_nine);
        iv_six = (ImageView) findViewById(R.id.iv_six);
        iv_three = (ImageView) findViewById(R.id.iv_three);
        iv_plus_minus = (ImageView) findViewById(R.id.iv_plus_minus);

        //row four initialization
        iv_multiply = (ImageView) findViewById(R.id.iv_multiply);
        iv_minus = (ImageView) findViewById(R.id.iv_minus);
        iv_plus = (ImageView) findViewById(R.id.iv_plus);
        iv_equals = (ImageView) findViewById(R.id.iv_equals);

        //edit text
        et_main = (EditText) findViewById(R.id.et_main);

        //textview
        tv_Display = (EditText) findViewById(R.id.tv_Display);

        //textview
        tv_degree = (TextView) findViewById(R.id.tv_degree);


        //linear layout
        ll_delete = (LinearLayout) findViewById(R.id.ll_delete);

        //linearlayout
        ll_calc = (LinearLayout) findViewById(R.id.ll_calc);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        iv_back =  findViewById(R.id.iv_back);

        //relative layout
        rl_calc_layout = (RelativeLayout) findViewById(R.id.rl_calc_layout);

        //ImageView of 2 icons

        //.................scientific calculator initialization.....................................//

        //ImageView

        iv_2nd = (ImageView) findViewById(R.id.iv_2nd);
        iv_log10 = (ImageView) findViewById(R.id.iv_log10);
        iv_sin = (ImageView) findViewById(R.id.iv_sin);
        iv_sinh = (ImageView) findViewById(R.id.iv_sinh);
        iv_cos = (ImageView) findViewById(R.id.iv_cos);
        iv_cosh = (ImageView) findViewById(R.id.iv_cosh);
        iv_tan = (ImageView) findViewById(R.id.iv_tan);
        iv_tanh = (ImageView) findViewById(R.id.iv_tanh);
        iv_x_square = (ImageView) findViewById(R.id.iv_x_square);
        iv_x_cube = (ImageView) findViewById(R.id.iv_x_cube);
        iv_x_raised_to_y = (ImageView) findViewById(R.id.iv_x_raised_to_y);
        iv_bracket_left = (ImageView) findViewById(R.id.iv_bracket_left);
        iv_bracket_right = (ImageView) findViewById(R.id.iv_bracket_right);
        iv_e_raised_to_x = (ImageView) findViewById(R.id.iv_e_raised_to_x);
        iv_10_raised_to_x = (ImageView) findViewById(R.id.iv_10_raised_to_x);
        iv_one_by_x = (ImageView) findViewById(R.id.iv_one_by_x);
        iv_under_root_x = (ImageView) findViewById(R.id.iv_under_root_x);
        iv_ln = (ImageView) findViewById(R.id.iv_ln);
        iv_3_under_root_x = (ImageView) findViewById(R.id.iv_3_under_root_x);
        iv_y_under_root_x = (ImageView) findViewById(R.id.iv_y_under_root_x);
        iv_rad = (ImageView) findViewById(R.id.iv_rad);
        iv_Rand = (ImageView) findViewById(R.id.iv_Rand);
        iv_pi = (ImageView) findViewById(R.id.iv_pi);
        iv_e = (ImageView) findViewById(R.id.iv_e);
        iv_x_exclamation = (ImageView) findViewById(R.id.iv_x_exclamation);
        iv_mplus = (ImageView) findViewById(R.id.iv_mplus);
        iv_m_minus = (ImageView) findViewById(R.id.iv_m_minus);
        iv_mc = (ImageView) findViewById(R.id.iv_mc);
        iv_mr = (ImageView) findViewById(R.id.iv_mr);
        iv_EE = (ImageView) findViewById(R.id.iv_EE);


        //TextView
        tv_10_raised_to_x = (TextView) findViewById(R.id.tv_10_raised_to_x);
        tv_log10 = (TextView) findViewById(R.id.tv_log10);
        tv_sin = (TextView) findViewById(R.id.tv_sin);
        tv_sinh = (TextView) findViewById(R.id.tv_sinh);
        tv_cos = (TextView) findViewById(R.id.tv_cos);
        tv_cosh = (TextView) findViewById(R.id.tv_cosh);
        tv_tan = (TextView) findViewById(R.id.tv_tan);
        tv_tanh = (TextView) findViewById(R.id.tv_tanh);
        tv_rad = (TextView) findViewById(R.id.tv_rad);

        //....................................................................................//

    }

    private void initlisteners() {
        iv_zero.setOnClickListener(this);
        iv_one.setOnClickListener(this);
        iv_two.setOnClickListener(this);
        iv_three.setOnClickListener(this);
        iv_four.setOnClickListener(this);
        iv_five.setOnClickListener(this);
        iv_six.setOnClickListener(this);
        iv_seven.setOnClickListener(this);
        iv_eight.setOnClickListener(this);
        iv_nine.setOnClickListener(this);
        iv_plus.setOnClickListener(this);
        iv_minus.setOnClickListener(this);
        iv_multiply.setOnClickListener(this);
        iv_divide.setOnClickListener(this);
        iv_percent.setOnClickListener(this);
        iv_clear.setOnClickListener(this);
        iv_equals.setOnClickListener(this);
        ll_delete.setOnClickListener(this);
        iv_plus_minus.setOnClickListener(this);
        iv_dot.setOnClickListener(this);
        iv_back.setOnClickListener(this);


        iv_zero.setOnTouchListener(this);
        iv_one.setOnTouchListener(this);
        iv_two.setOnTouchListener(this);
        iv_three.setOnTouchListener(this);
        iv_four.setOnTouchListener(this);
        iv_five.setOnTouchListener(this);
        iv_six.setOnTouchListener(this);
        iv_seven.setOnTouchListener(this);
        iv_eight.setOnTouchListener(this);
        iv_nine.setOnTouchListener(this);
        iv_plus.setOnTouchListener(this);
        iv_minus.setOnTouchListener(this);
        iv_multiply.setOnTouchListener(this);
        iv_divide.setOnTouchListener(this);
        iv_percent.setOnTouchListener(this);
        iv_clear.setOnTouchListener(this);
        iv_equals.setOnTouchListener(this);
        ll_delete.setOnTouchListener(this);
        iv_plus_minus.setOnTouchListener(this);
        iv_dot.setOnTouchListener(this);

        //.................scientific calculator initialization....................//

        iv_2nd.setOnClickListener(this);
        iv_x_square.setOnClickListener(this);
        iv_bracket_left.setOnClickListener(this);
        iv_bracket_right.setOnClickListener(this);
        iv_x_cube.setOnClickListener(this);
        iv_x_raised_to_y.setOnClickListener(this);
        iv_e_raised_to_x.setOnClickListener(this);
        iv_10_raised_to_x.setOnClickListener(this);
        iv_one_by_x.setOnClickListener(this);
        iv_under_root_x.setOnClickListener(this);
        iv_ln.setOnClickListener(this);
        iv_log10.setOnClickListener(this);
        iv_3_under_root_x.setOnClickListener(this);
        iv_y_under_root_x.setOnClickListener(this);
        iv_sin.setOnClickListener(this);
        iv_sinh.setOnClickListener(this);
        iv_cos.setOnClickListener(this);
        iv_cosh.setOnClickListener(this);
        iv_tan.setOnClickListener(this);
        iv_tanh.setOnClickListener(this);
        iv_rad.setOnClickListener(this);
        iv_Rand.setOnClickListener(this);
        iv_pi.setOnClickListener(this);
        iv_e.setOnClickListener(this);
        iv_x_exclamation.setOnClickListener(this);
        iv_mplus.setOnClickListener(this);
        iv_m_minus.setOnClickListener(this);
        iv_mc.setOnClickListener(this);
        iv_mr.setOnClickListener(this);
        iv_EE.setOnClickListener(this);

        //Touch listeners.............

        iv_2nd.setOnTouchListener(this);
        iv_x_square.setOnTouchListener(this);
        iv_bracket_left.setOnTouchListener(this);
        iv_bracket_right.setOnTouchListener(this);
        iv_x_cube.setOnTouchListener(this);
        iv_x_raised_to_y.setOnTouchListener(this);
        iv_e_raised_to_x.setOnTouchListener(this);
        iv_10_raised_to_x.setOnTouchListener(this);
        iv_one_by_x.setOnTouchListener(this);
        iv_under_root_x.setOnTouchListener(this);
        iv_ln.setOnTouchListener(this);
        iv_log10.setOnTouchListener(this);
        iv_3_under_root_x.setOnTouchListener(this);
        iv_y_under_root_x.setOnTouchListener(this);
        iv_sin.setOnTouchListener(this);
        iv_sinh.setOnTouchListener(this);
        iv_cos.setOnTouchListener(this);
        iv_cosh.setOnTouchListener(this);
        iv_tan.setOnTouchListener(this);
        iv_tanh.setOnTouchListener(this);
        iv_rad.setOnTouchListener(this);
        iv_Rand.setOnTouchListener(this);
        iv_pi.setOnTouchListener(this);
        iv_e.setOnTouchListener(this);
        iv_x_exclamation.setOnTouchListener(this);
        iv_mplus.setOnTouchListener(this);
        iv_m_minus.setOnTouchListener(this);
        iv_mc.setOnTouchListener(this);
        iv_mr.setOnTouchListener(this);
        iv_EE.setOnTouchListener(this);
        //......................................................................//

        //tv_log10.setText(Html.fromHtml("log<sub>10</sub>"));
        tv_log10.setText(Html.fromHtml("log<sub>10</sub>"));


      /*  if (SharedPrefs.getString(this, "theme2", "default").equalsIgnoreCase("default")) {
            setTheme(R.style.AppTheme);
            ll_back.setBackgroundColor(Color.parseColor(SharedPrefs.getString(this, "ll_back_sci", "#E9E5E4")));
            ll_calc.setBackgroundColor(Color.parseColor(SharedPrefs.getString(this, "ll_calc_sci", "#375D72")));

        } else if (SharedPrefs.getString(this, "theme2", "default").equalsIgnoreCase("theme1")) {
            setTheme(R.style.theme1);

            ll_back.setBackgroundColor(Color.parseColor(SharedPrefs.getString(this, "ll_back_sci", "#E9E5E4")));
            ll_calc.setBackgroundColor(Color.parseColor(SharedPrefs.getString(this, "ll_calc_sci", "#375D72")));

        } else if (SharedPrefs.getString(this, "theme2", "default").equalsIgnoreCase("theme2")) {
            setTheme(R.style.theme2);

            ll_back.setBackgroundColor(Color.parseColor(SharedPrefs.getString(this, "ll_back_sci", "#E9E5E4")));
            ll_calc.setBackgroundColor(Color.parseColor(SharedPrefs.getString(this, "ll_calc_sci", "#375D72")));

        } else if (SharedPrefs.getString(this, "theme2", "default").equalsIgnoreCase("theme3")) {
            setTheme(R.style.theme3);

            ll_back.setBackgroundColor(Color.parseColor(SharedPrefs.getString(this, "ll_back_sci", "#E9E5E4")));
            ll_calc.setBackgroundColor(Color.parseColor(SharedPrefs.getString(this, "ll_calc_sci", "#375D72")));

        } else if (SharedPrefs.getString(this, "theme2", "default").equalsIgnoreCase("theme4")) {
            setTheme(R.style.theme4);

            ll_back.setBackgroundColor(Color.parseColor(SharedPrefs.getString(this, "ll_back_sci", "#E9E5E4")));
            ll_calc.setBackgroundColor(Color.parseColor(SharedPrefs.getString(this, "ll_calc_sci", "#375D72")));

        } else if (SharedPrefs.getString(this, "theme2", "default").equalsIgnoreCase("theme5")) {
            setTheme(R.style.theme5);
            ll_back.setBackgroundColor(Color.parseColor(SharedPrefs.getString(this, "ll_back_sci", "#E9E5E4")));
            ll_calc.setBackgroundColor(Color.parseColor(SharedPrefs.getString(this, "ll_calc_sci", "#375D72")));
        }*/


        et_main.setSingleLine();
        tv_Display.setSingleLine();


        et_main.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                tv_Display.setSelection(tv_Display.length());
            }
        });
        tv_Display.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                tv_Display.setSelection(tv_Display.length());
            }
        });
    }


    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //Watch History


            case R.id.iv_back:
                onBackPressed();
                break;

            //Radian Degree conversion
            case R.id.iv_rad:
                if (flag_radian) {
                    tv_rad.setText("DEG");
                    Radian_Degree = "Rad";
                    flag_radian = false;
                    tv_degree.setText("Rad");
                } else {
                    tv_rad.setText("Rad");
                    Radian_Degree = "DEG";
                    flag_radian = true;
                    tv_degree.setText("DEG");
                }
                break;

            //Enter zero digit
            case R.id.iv_zero:

                /*//animation
                iv_zero.startAnimation(click_anim);*/

                //condition to start new calculation after equal is pressed
                if (flag_equals) {
                    et_main.setText("");
                    display = "";
                    //tv_Display.setText("");
                    flag_equals = false;
                }
                //condition to enter new digits every time after some operation
                if (flag_plus || flag_minus || flag_divide || flag_multiply || flag_moulo || flag_plus_minus) {
                    display = "";
                    flag_plus = false;
                    flag_minus = false;
                    flag_divide = false;
                    flag_multiply = false;
                    flag_moulo = false;
                    flag_plus_minus = false;
                }

                if (flag_string_inside_brackets) {
                    string_inside_bracket = string_inside_bracket + "0";
                }
                tv_Display.setText("");
                if (et_main.length() > 0) {
                    if (et_main.getText().charAt(et_main.length() - 1) == ')') {
                        et_main.append("*0");
                        display = display + "*0";
                        //tv_Display.setText(display);
                    } else {
                        et_main.append("0");
                        display = display + "0";
                        // tv_Display.setText(display);
                    }

                } else {
                    et_main.append("0");
                    display = display + "0";
                    // tv_Display.setText(display);
                }
                /*et_main.append("0");
                no_format= Integer.parseInt(et_main.getText().toString());
               display= NumberFormat.getNumberInstance(Locale.getDefault()).format(no_format) + "";
//                display = display + "0";
                tv_Display.setText(display);*/

                break;
            //Enter 1 digit
            case R.id.iv_one:
                if (flag_equals) {
                    et_main.setText("");
                    display = "";
                    tv_Display.setText("");
                    flag_equals = false;
                }

                if (flag_plus || flag_minus || flag_divide || flag_multiply || flag_moulo || flag_plus_minus) {
                    display = "";
                    flag_plus = false;
                    flag_minus = false;
                    flag_divide = false;
                    flag_multiply = false;
                    flag_moulo = false;
                    flag_plus_minus = false;
                }

                if (flag_string_inside_brackets) {
                    string_inside_bracket = string_inside_bracket + "1";
                }
                tv_Display.setText("");
                if (et_main.length() > 0) {
                    if (et_main.getText().charAt(et_main.length() - 1) == ')') {
                        et_main.append("*1");
                        display = display + "*1";
                        //tv_Display.setText(display);
                    } else {
                        et_main.append("1");
                        display = display + "1";
                        ////tv_Display.setText(display);
                    }
                } else {
                    et_main.append("1");
                    display = display + "1";
                    ////tv_Display.setText(display);
                }

                break;
            //Enter 2 digit
            case R.id.iv_two:
                if (flag_equals) {
                    et_main.setText("");
                    display = "";
                    tv_Display.setText("");
                    flag_equals = false;
                }

                if (flag_plus || flag_minus || flag_divide || flag_multiply || flag_moulo || flag_plus_minus) {
                    display = "";
                    flag_plus = false;
                    flag_minus = false;
                    flag_divide = false;
                    flag_multiply = false;
                    flag_moulo = false;
                    flag_plus_minus = false;
                }

                if (flag_string_inside_brackets) {
                    string_inside_bracket = string_inside_bracket + "2";
                }
                tv_Display.setText("");
                if (et_main.length() > 0) {
                    if (et_main.getText().charAt(et_main.length() - 1) == ')') {
                        et_main.append("*2");
                        display = display + "*2";
                        //tv_Display.setText(display);
                    } else {
                        et_main.append("2");
                        display = display + "2";
                        //tv_Display.setText(display);
                    }
                } else {
                    et_main.append("2");
                    display = display + "2";
                    //tv_Display.setText(display);
                }

                break;
            //Enter 3 digit
            case R.id.iv_three:
                if (flag_equals) {
                    et_main.setText("");
                    display = "";
                    tv_Display.setText("");
                    flag_equals = false;
                }

                if (flag_plus || flag_minus || flag_divide || flag_multiply || flag_moulo || flag_plus_minus) {
                    display = "";
                    flag_plus = false;
                    flag_minus = false;
                    flag_divide = false;
                    flag_multiply = false;
                    flag_moulo = false;
                    flag_plus_minus = false;
                    flag_bracket_right = false;
                }


                if (flag_string_inside_brackets) {
                    string_inside_bracket = string_inside_bracket + "3";
                }
                tv_Display.setText("");
                if (et_main.length() > 0) {
                    if (et_main.getText().charAt(et_main.length() - 1) == ')') {
                        et_main.append("*3");
                        display = display + "*3";
                        //tv_Display.setText(display);
                    } else {
                        et_main.append("3");
                        display = display + "3";
                        //tv_Display.setText(display);
                    }
                } else {
                    et_main.append("3");
                    display = display + "3";
                    //tv_Display.setText(display);
                }


                break;
            //Enter 4 digit
            case R.id.iv_four:
                if (flag_equals) {
                    et_main.setText("");
                    display = "";
                    tv_Display.setText("");
                    flag_equals = false;
                }

                if (flag_plus || flag_minus || flag_divide || flag_multiply || flag_moulo || flag_plus_minus) {
                    display = "";
                    flag_plus = false;
                    flag_minus = false;
                    flag_divide = false;
                    flag_multiply = false;
                    flag_moulo = false;
                    flag_plus_minus = false;
                    flag_bracket_right = false;
                }


                if (flag_string_inside_brackets) {
                    string_inside_bracket = string_inside_bracket + "4";
                }
                tv_Display.setText("");
                if (et_main.length() > 0) {
                    if (et_main.getText().charAt(et_main.length() - 1) == ')') {
                        et_main.append("*4");
                        display = display + "*4";
                        tv_Display.setText(display);
                    } else {
                        et_main.append("4");
                        display = display + "4";
                        tv_Display.setText(display);
                    }

                } else {
                    et_main.append("4");
                    display = display + "4";
                    tv_Display.setText(display);
                }
                break;
            //Enter 5 digit
            case R.id.iv_five:
                if (flag_equals) {
                    et_main.setText("");
                    display = "";
                    tv_Display.setText("");
                    flag_equals = false;
                }

                if (flag_plus || flag_minus || flag_divide || flag_multiply || flag_moulo || flag_plus_minus) {
                    display = "";
                    flag_plus = false;
                    flag_minus = false;
                    flag_divide = false;
                    flag_multiply = false;
                    flag_moulo = false;
                    flag_plus_minus = false;
                    flag_bracket_right = false;
                }

                if (flag_string_inside_brackets) {
                    string_inside_bracket = string_inside_bracket + "5";
                }
                tv_Display.setText("");
                if (et_main.length() > 0) {
                    if (et_main.getText().charAt(et_main.length() - 1) == ')') {
                        et_main.append("*5");
                        display = display + "*5";
                        //tv_Display.setText(display);
                    } else {
                        et_main.append("5");
                        display = display + "5";
                        //tv_Display.setText(display);
                    }
                } else {
                    et_main.append("5");
                    display = display + "5";
                    //tv_Display.setText(display);
                }

                break;
            //Enter digit 6
            case R.id.iv_six:
                if (flag_equals) {
                    et_main.setText("");
                    display = "";
                    tv_Display.setText("");
                    flag_equals = false;
                }

                if (flag_plus || flag_minus || flag_divide || flag_multiply || flag_moulo || flag_plus_minus) {
                    display = "";
                    flag_plus = false;
                    flag_minus = false;
                    flag_divide = false;
                    flag_multiply = false;
                    flag_moulo = false;
                    flag_plus_minus = false;
                    flag_bracket_right = false;
                }

                if (flag_string_inside_brackets) {
                    string_inside_bracket = string_inside_bracket + "6";
                }
                tv_Display.setText("");
                if (et_main.length() > 0) {
                    if (et_main.length() > 0) {
                        if (et_main.getText().charAt(et_main.length() - 1) == ')') {
                            et_main.append("*6");
                            display = display + "*6";
                            //tv_Display.setText(display);
                        } else {
                            et_main.append("6");
                            display = display + "6";
                            //tv_Display.setText(display);
                        }
                    }
                } else {
                    et_main.append("6");
                    display = display + "6";
                    //tv_Display.setText(display);
                }
                break;
            //Enter 7 digit
            case R.id.iv_seven:
                if (flag_equals) {
                    et_main.setText("");
                    display = "";
                    tv_Display.setText("");
                    flag_equals = false;
                }

                if (flag_plus || flag_minus || flag_divide || flag_multiply || flag_moulo || flag_plus_minus) {
                    display = "";
                    flag_plus = false;
                    flag_minus = false;
                    flag_divide = false;
                    flag_multiply = false;
                    flag_moulo = false;
                    flag_plus_minus = false;
                    flag_bracket_right = false;
                }


                if (flag_string_inside_brackets) {
                    string_inside_bracket = string_inside_bracket + "7";
                }
                tv_Display.setText("");
                if (et_main.length() > 0) {
                    if (et_main.getText().charAt(et_main.length() - 1) == ')') {
                        et_main.append("*7");
                        display = display + "*7";
                        //tv_Display.setText(display);
                    } else {
                        et_main.append("7");
                        display = display + "7";
                        //tv_Display.setText(display);
                    }
                } else {
                    et_main.append("7");
                    display = display + "7";
                    //tv_Display.setText(display);
                }


                break;
            //Enter 8 digit
            case R.id.iv_eight:
                if (flag_equals) {
                    et_main.setText("");
                    display = "";
                    tv_Display.setText("");
                    flag_equals = false;
                }

                if (flag_plus || flag_minus || flag_divide || flag_multiply || flag_moulo || flag_plus_minus) {
                    display = "";
                    flag_plus = false;
                    flag_minus = false;
                    flag_divide = false;
                    flag_multiply = false;
                    flag_moulo = false;
                    flag_plus_minus = false;
                    flag_bracket_right = false;
                }


                if (flag_string_inside_brackets) {
                    string_inside_bracket = string_inside_bracket + "8";
                }
                tv_Display.setText("");
                if (et_main.length() > 0) {
                    if (et_main.getText().charAt(et_main.length() - 1) == ')') {
                        et_main.append("*8");
                        display = display + "*8";
                        //tv_Display.setText(display);
                    } else {
                        et_main.append("8");
                        display = display + "8";
                        //tv_Display.setText(display);
                    }
                } else {
                    et_main.append("8");
                    display = display + "8";
                    //tv_Display.setText(display);
                }
                break;
            case R.id.iv_nine:
                //Enter 9 digit
                if (flag_equals) {
                    et_main.setText("");
                    display = "";
                    tv_Display.setText("");
                    flag_equals = false;
                }

                if (flag_plus || flag_minus || flag_divide || flag_multiply || flag_moulo || flag_plus_minus) {
                    display = "";
                    flag_plus = false;
                    flag_minus = false;
                    flag_divide = false;
                    flag_multiply = false;
                    flag_moulo = false;
                    flag_plus_minus = false;
                    flag_bracket_right = false;
                }


                if (flag_string_inside_brackets) {
                    string_inside_bracket = string_inside_bracket + "9";
                }

                tv_Display.setText("");
                if (et_main.length() > 0) {
                    if (et_main.getText().charAt(et_main.length() - 1) == ')') {
                        et_main.append("*9");
                        display = display + "*9";
                        //tv_Display.setText(display);
                    } else {
                        et_main.append("9");
                        display = display + "9";
                        //tv_Display.setText(display);
                    }
                } else {
                    et_main.append("9");
                    display = display + "9";
                    //tv_Display.setText(display);
                }

                break;
            //+ operation
            case R.id.iv_plus:
                Log.e("plus", et_main.getText().toString());

                if (flag_equals) {
                    et_main.setText(display);
                    display = Answer + "";
                    tv_Display.setText(display);
                    flag_equals = false;
                }

                //flag to notice that plus is pressed
                flag_plus = true;

                if (flag_string_inside_brackets) {
                    string_inside_bracket = string_inside_bracket + "+";
                }

                //condition to operate if  length is greater than 0
                if (et_main.length() > 0) {

                    tv_Display.setText(display);

                    //condition to not enter one symbol again
                    if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%')) {

                        expression = et_main.getText();

                        Log.e("if", "if");
                        //permform(et_main.getText().toString());
                        //  mid_calculation();
                        Log.e("str tv_Display", "et_main" + et_main.getText().toString());

                        if (et_main.getText().toString().equals("Can't divide by 0")) {
                            return;
                        }

                        /*if (tv_Display.getText().toString().charAt(tv_Display.getText().length() - 1) == '.') {
                            tv_Display.setText(tv_Display.getText().toString().substring(0, tv_Display.getText().length() - 1));
                            // tv_Display.setText(tv_Display.getText().toString().substring(0, tv_Display.getText().length() - 1));

                        }*/
                        Log.e("str tv_Display", "tv_Display" + tv_Display.getText().toString());
                        if (et_main.getText().toString().equals("Infinity")) {
                            return;
                        }
                        if (tv_Display.getText().toString().equals("null")) {
                            tv_Display.setText("");

                        }
                        mid_calculation();
                        et_main.append("+");

                    } else if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '+' ||
                            et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '-' ||
                            et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '*' ||
                            et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '/') {

                        String str = et_main.getText().toString();

                        et_main.setText(str.substring(0, et_main.getText().length() - 1));
                        expression = et_main.getText();
                        Log.e("else", "else");
                        // permform(et_main.getText().toString());


                        if (et_main.getText().toString().equals("Can't divide by 0")) {
                            return;
                        }
                        if (et_main.getText().toString().equals("Infinity")) {
                            return;
                        }
                        if (et_main.getText().toString().equals("Invalid Input")) {
                            return;
                        }


                        if (et_main.getText().toString().equals("Can't divide by 0")) {
                            return;
                        }
                        if (et_main.getText().toString().equals("Infinity")) {
                            return;
                        }
                        if (et_main.getText().toString().equals("Invalid Input")) {
                            return;
                        }
                        if (tv_Display.getText().toString().equals("null")) {
                            tv_Display.setText("");

                        }
                        mid_calculation();
                        et_main.append("+");


                    }
                }

                break;
            //- operation
            case R.id.iv_minus:
                Log.e("minus", et_main.getText().toString());

                if (flag_equals) {
                    et_main.setText(display);
                    display = Answer + "";
                    tv_Display.setText(display);
                    flag_equals = false;
                }

                //flag to notice that minus is pressed
                flag_minus = true;

                if (flag_string_inside_brackets) {
                    string_inside_bracket = string_inside_bracket + "-";
                }

                //condition to operate if  length is greater than 0
                if (et_main.length() > 0) {

                    tv_Display.setText(display);

                    //condition to not enter one symbol again
                    if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%')) {

                        expression = et_main.getText();
                        //mid_calculation();
                        if (et_main.getText().toString().equals("Can't divide by 0")) {
                            return;
                        }

                        if (et_main.getText().toString().equals("Can't divide by 0")) {
                            return;
                        }
                        if (et_main.getText().toString().equals("Infinity")) {
                            return;
                        }
                        if (et_main.getText().toString().equals("Invalid Input")) {
                            return;
                        }
                        if (tv_Display.getText().toString().equals("null")) {
                            tv_Display.setText("");

                        }
                        mid_calculation();
                        et_main.append("-");

                    } else if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '+' ||
                            et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '-' ||
                            et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '*' ||
                            et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '/') {

                        String str = et_main.getText().toString();

                        et_main.setText(str.substring(0, et_main.getText().length() - 1));
                        expression = et_main.getText();
                        //  mid_calculation();

                        if (et_main.getText().toString().equals("Can't divide by 0")) {
                            return;
                        }


                        if (et_main.getText().toString().equals("Can't divide by 0")) {
                            return;
                        }
                        if (et_main.getText().toString().equals("Infinity")) {
                            return;
                        }
                        if (et_main.getText().toString().equals("Invalid Input")) {
                            return;
                        }
                        if (tv_Display.getText().toString().equals("null")) {
                            tv_Display.setText("");

                        }
                        mid_calculation();
                        et_main.append("-");


                    }
                }
                break;
            //(/)operation
            case R.id.iv_divide:
                Log.e("divide", et_main.getText().toString());

                if (flag_equals) {
                    et_main.setText(display);
                    display = Answer + "";
                    tv_Display.setText(display);
                    flag_equals = false;
                }

                //flag to notice that divide is pressed
                flag_divide = true;

                if (flag_string_inside_brackets) {
                    string_inside_bracket = string_inside_bracket + "/";
                }

                //condition to operate if  length is greater than 0
                if (et_main.length() > 0) {

                    tv_Display.setText(display);

                    //condition to not enter one symbol again
                    if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%')) {

                        expression = et_main.getText();
                        //  mid_calculation();
                        if (et_main.getText().toString().equals("Can't divide by 0")) {
                            return;
                        }
                        if (et_main.getText().toString().equals("Infinity")) {
                            return;
                        }
                        if (et_main.getText().toString().equals("Invalid Input")) {
                            return;
                        }

                        if (tv_Display.getText().toString().equals("null")) {
                            tv_Display.setText("");

                        }
                        mid_calculation();
                        et_main.append("/");


                        if (flag_bracket_left) {
                            display = display + "/";
                        }
                    } else if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '+' ||
                            et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '-' ||
                            et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '*' ||
                            et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '/') {

                        String str = et_main.getText().toString();

                        et_main.setText(str.substring(0, et_main.getText().length() - 1));
                        expression = et_main.getText();
                        // mid_calculation();
                        if (et_main.getText().toString().equals("Can't divide by 0")) {
                            return;
                        }
                        if (et_main.getText().toString().equals("Infinity")) {
                            return;
                        }
                        if (et_main.getText().toString().equals("Invalid Input")) {
                            return;
                        }

                        if (tv_Display.getText().toString().equals("null")) {
                            tv_Display.setText("");

                        }
                        mid_calculation();
                        et_main.append("/");


                        if (flag_bracket_left) {
                            display = display + "/";
                        }
                    }
                }
                break;
            //  * operation
            case R.id.iv_multiply:
                Log.e("multiply", et_main.getText().toString());

                if (flag_equals) {
                    et_main.setText(display);
                    display = Answer + "";
                    tv_Display.setText(display);
                    flag_equals = false;
                }

                //flag to notice that plus is pressed
                flag_multiply = true;

                if (flag_string_inside_brackets) {
                    string_inside_bracket = string_inside_bracket + "*";
                }

                //condition to operate if  length is greater than 0
                if (et_main.length() > 0) {

                    if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%')) {

                        expression = et_main.getText();
                        // mid_calculation();
                        if (et_main.getText().toString().equals("Can't divide by 0")) {
                            return;
                        }
                        if (et_main.getText().toString().equals("Infinity")) {
                            return;
                        }
                        if (et_main.getText().toString().equals("Invalid Input")) {
                            return;
                        }

                        if (tv_Display.getText().toString().equals("null")) {
                            tv_Display.setText("");

                        }
                        mid_calculation();
                        et_main.append("*");

                    } else if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '+' ||
                            et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '-' ||
                            et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '*' ||
                            et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == '/') {

                        String str = et_main.getText().toString();

                        et_main.setText(str.substring(0, et_main.getText().length() - 1));
                        expression = et_main.getText();
                        // mid_calculation();
                        if (et_main.getText().toString().equals("Can't divide by 0")) {
                            return;
                        }
                        if (et_main.getText().toString().equals("Infinity")) {
                            return;
                        }
                        if (et_main.getText().toString().equals("Invalid Input")) {
                            return;
                        }

                        if (tv_Display.getText().toString().equals("null")) {
                            tv_Display.setText("");

                        }
                        mid_calculation();
                        et_main.append("*");

                    }

                }
                break;
            case R.id.ll_delete:
                int length = et_main.getText().length();
                if (length > 0) {

                    et_main.getText().delete(length - 1, length);
                    display = et_main.getText().toString();
                    tv_Display.setText(display);

                    /*if(et_main.getText().length()>0) {
                        if (flag_string_inside_brackets) {
                            string_inside_bracket = string_inside_bracket.substring(0, string_inside_bracket.length() - 1);
                        }


                    }*/
                }
                break;
            //Clear operation
            case R.id.iv_clear:

                et_main.setText("");
                tv_Display.setText("Start");
                display = "";
                flag = false;
                flag_equals = false;
                string_inside_bracket = "";
                break;
            //Function to call equal sign and do calculation
            case R.id.iv_equals:


                if (et_main.getText().length() > 0) {

                    if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%')) {

                        flag_plus = false;
                        flag_minus = false;
                        flag_divide = false;
                        flag_multiply = false;
                        flag_moulo = false;
                        flag_plus_minus = false;

                        expression = et_main.getText();
                        flag_equals = true;
                        flag = true;
                        equal = true;
                        mid_calculation();

//                        Equal_pressed_Mode = true;

                        string_inside_bracket = "";
                    }
                }
                break;
            //Dot function
            case R.id.iv_dot:
                try {
                    dot_operation();
                } catch (Exception e) {

                }
                break;

//.....................................scientific calculator functions....................//
            //.................................................................Memory operations.............................................//
            //M plus Operation
            case R.id.iv_mplus:

                try {
                    Log.e("M-plus", "" + SharedPrefs.getStringMemory(ScientificCalculatorActivity.this, "Memory", Memory + ""));
//                if (Equal_pressed_Mode) {
                    m_instance = Double.valueOf(et_main.getText().toString());
                    Memory = Double.valueOf(SharedPrefs.getStringMemory(ScientificCalculatorActivity.this, "Memory", Memory + ""));
                    Memory = Memory + m_instance;
                    SharedPrefs.saveMemory(ScientificCalculatorActivity.this, "Memory", Memory + "");
                    Equal_pressed_Mode = false;
                    flag_equals = true;
                } catch (Exception e) {

                }
//                }
                break;
            //M minus operation
            case R.id.iv_m_minus:

                try {
                    Log.e("M-minus", "" + SharedPrefs.getStringMemory(ScientificCalculatorActivity.this, "Memory", Memory + ""));
//                if (Equal_pressed_Mode) {
                    m_instance = Double.valueOf(et_main.getText().toString());

                    Memory = Double.valueOf(SharedPrefs.getStringMemory(ScientificCalculatorActivity.this, "Memory", Memory + ""));
                    Memory = Memory - m_instance;
                    SharedPrefs.saveMemory(ScientificCalculatorActivity.this, "Memory", Memory + "");
                    Equal_pressed_Mode = false;
                    flag_equals = true;
                } catch (Exception e) {

                }
//                }
                break;
            //M recall operation
            case R.id.iv_mr:
                Log.e("M-recall", "" + SharedPrefs.getStringMemory(ScientificCalculatorActivity.this, "Memory", Memory + ""));


                Memory = Double.valueOf(SharedPrefs.getStringMemory(ScientificCalculatorActivity.this, "Memory", Memory + ""));
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumIntegerDigits(20);
                nf.setMaximumFractionDigits(20);
                nf.setGroupingUsed(false);

                String ab = nf.format(Memory);
                if (!ab.equalsIgnoreCase("0")) {
                    if (et_main.getText().length() > 0) {
                        if (et_main.getText().toString().charAt(et_main.getText().length() - 1) == '/' || et_main.getText().toString().charAt(et_main.getText().length() - 1) == '-' || et_main.getText().toString().charAt(et_main.getText().length() - 1) == '*' || et_main.getText().toString().charAt(et_main.getText().length() - 1) == '+') {
                            et_main.append(ab);
                            tv_Display.setText(ab);
                        } else {
                            et_main.setText(ab);
                            tv_Display.setText(ab);

                        }
                    } else {
                        et_main.setText(ab);
                        tv_Display.setText(ab);

                    }
                }


                Equal_pressed_Mode = false;

                break;
            //M clear operation
            case R.id.iv_mc:
                Log.e("M-clear", "" + SharedPrefs.getStringMemory(ScientificCalculatorActivity.this, "Memory", Memory + ""));
                Memory = 0.0;
                SharedPrefs.saveMemory(ScientificCalculatorActivity.this, "Memory", Memory + "");
                break;
            //..................................................................................................................................................//
            //Toggle 2nd operation
            case R.id.iv_2nd:
                if (press_2nd) {
                    SWITCH = false;
                    tv_10_raised_to_x.setText("2\u02e3");

                    tv_log10.setText(Html.fromHtml("log<sub>2</sub>"));

                    tv_sin.setText("sin\u207b\u00b9");
                    tv_sinh.setText("sinh\u207b\u00b9");
                    tv_cos.setText("cos\u207b\u00b9");
                    tv_cosh.setText("cosh\u207b\u00b9");
                    tv_tan.setText("tan\u207b\u00b9");
                    tv_tanh.setText("tanh\u207b\u00b9");
                    press_2nd = false;
                } else {
                    SWITCH = true;
                    tv_10_raised_to_x.setText("10\u02e3");
                    tv_log10.setText(Html.fromHtml("log<sub>10</sub>"));
                    //tv_log10.setText("log\u2081"+"\u2080");
                    tv_sin.setText("sin");
                    tv_sinh.setText("sinh");
                    tv_cos.setText("cos");
                    tv_cosh.setText("cosh");
                    tv_tan.setText("tan");
                    tv_tanh.setText("tanh");
                    press_2nd = true;
                }
                break;
            //Left Bracket operation
            case R.id.iv_bracket_left:
                if (flag_equals) {
                    et_main.setText("");
                    display = "";
                    tv_Display.setText("");
                    flag_equals = false;
                }
                flag_string_inside_brackets = true;


                if (et_main.getText().length() > 0) {

                    char ch = et_main.getText().toString().charAt(et_main.getText().toString().length() - 1);

                    if ((ch >= '0' && ch <= '9')) //if it ends with no
                    {
                        et_main.append("*(");
                    } else if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == ')'))//if ends with right bracket
                    {
                        et_main.append("*(");
                    } else {
                        et_main.append("(");
                    }
                } else {
                    et_main.append("(");
                }

                tv_Display.setText(display);

                break;
            //Right Bracket Operation
            case R.id.iv_bracket_right:
                if (flag_equals) {
                    et_main.setText(Answer + "");
                    display = "";
                    flag_equals = false;
                }

                if (et_main.getText().length() > 0) {

                    if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%')) {

                        //Check the frequency of left bracket
                        String s = et_main.getText().toString();
                        for (int i = 0; i < s.length(); i++) {
                            if (s.charAt(i) == '(') {
                                counter_left_bracket++;
                            }
                        }
                        //Condition to enter right bracket only the times left bracket is present
                        if (counter_right_bracket <= counter_left_bracket) {


                            et_main.append(")");
                            counter_right_bracket++;

                            tv_Display.setText(display);


                            if (counter_left_bracket == counter_right_bracket) {
                                flag_string_inside_brackets_completed = true;
                            }
                        }
                    }
                }

                flag_string_inside_brackets = false;

                break;
            //X_square operation
            case R.id.iv_x_square:
                try {
                    //making et_main and tv_display to display ans after equal is pressed
                    if (flag_equals) {
                        et_main.setText(display + "");
                        flag_equals = false;
                    }

                    //condition to operate if  length is greater than 0
                    if (et_main.length() > 0) {

                        tv_Display.setText(display);

                        //condition to not enter one symbol again
                        if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+') &&
                                (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-') &&
                                (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/') &&
                                (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*') &&
                                (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%')) {


                            //Condition to check if not re_entering "^2"
                            if (et_main.length() > 2) {
                                if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 2) != '^') {
                                    Flag_add_bracket = true;
                                    expression = et_main.getText();
                                    et_main.append("^2");
                                    display = display + "^2";
                                }
                            } else {
                                Flag_add_bracket = true;
                                expression = et_main.getText();
                                et_main.append("^2");
                                display = display + "^2";
                            }

                        }
                    } else {
                        Toast.makeText(ScientificCalculatorActivity.this, "Add Number first", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }
                break;
            //X_cube operation
            case R.id.iv_x_cube:
                try {
                    //making et_main and tv_display to display ans after equal is pressed
                    if (flag_equals) {
                        et_main.setText(display + "");
                        flag_equals = false;
                    }

                    //condition to operate if  length is greater than 0
                    if (et_main.length() > 0) {

                        tv_Display.setText(display);

                        //condition to not enter one symbol again
                        if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+') &&
                                (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-') &&
                                (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/') &&
                                (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*') &&
                                (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%')) {

                            //Condition to check if not re_entering "^3"
                            if (et_main.length() > 2) {
                                if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 2) != '^') {
                                    Flag_add_bracket = true;
                                    expression = et_main.getText();
                                    et_main.append("^3");

                                    display = display + "^3";
                                }
                            } else {
                                Flag_add_bracket = true;
                                expression = et_main.getText();
                                et_main.append("^3");
                                display = display + "^3";
                            }

                        }
                    } else {
                        Toast.makeText(ScientificCalculatorActivity.this, "Add Number first", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }
                break;
            //X^y operation
            case R.id.iv_x_raised_to_y:
                try {
                    //making et_main and tv_display to display ans after equal is pressed
                    if (flag_equals) {
                        et_main.setText(display + "");
                        flag_equals = false;
                    }

                    //condition to operate if  length is greater than 0
                    if (et_main.length() > 0) {

                        tv_Display.setText(display);

                        //condition to not enter one symbol again
                        if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+') &&
                                (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-') &&
                                (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/') &&
                                (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*') &&
                                (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%')) {


                            //Condition to check if not re_entering "^y"
                            if (et_main.length() > 2) {
                                if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 2) != '^') {
                                    Flag_add_bracket = true;
                                    expression = et_main.getText();
                                    et_main.append("^");
                                    display = display + "^";
                                }
                            } else {
                                Flag_add_bracket = true;
                                expression = et_main.getText();
                                et_main.append("^");
                                display = display + "^";
                            }
                        }
                    } else {
                        Toast.makeText(ScientificCalculatorActivity.this, "Add Number first", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }
                break;
            //operation e_raised_to_x
            case R.id.iv_e_raised_to_x:
                //making et_main and tv_display to display ans after equal is pressed
                if (flag_equals) {
                    et_main.setText(display + "");
                    flag_equals = false;
                }


                if (et_main.length() > 0) {

                    //condition if it ends with numeric character
                    char ch = et_main.getText().toString().charAt(et_main.getText().toString().length() - 1);

                    if ((ch >= '0' && ch <= '9')) {

                        tv_Display.setText(display);

                        //Condition to check if not re_entering "^y"
                        if (et_main.length() > 2) {
                            if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 2) != '^') {
                                //condition to not enter one symbol again
                                Flag_add_bracket = true;
                                expression = et_main.getText();
                                et_main.append("*e^");
                                display = display + "e^";
                            }
                        } else {
                            Flag_add_bracket = true;
                            expression = et_main.getText();
                            et_main.append("*e^");
                            display = display + "e^";
                        }

                    } else {
                        tv_Display.setText(display);

                        //Condition to check if not re_entering "^y"
                        if (et_main.length() > 2) {
                            if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 2) != '^') {
                                Flag_add_bracket = true;
                                expression = et_main.getText();
                                et_main.append("e^");
                                display = display + "e^";
                            }
                        } else {
                            Flag_add_bracket = true;
                            expression = et_main.getText();
                            et_main.append("e^");
                            display = display + "e^";
                        }
                    }
                } else {
                    Flag_add_bracket = true;
                    expression = et_main.getText();
                    et_main.append("e^");
                    display = display + "e^";
                }
                break;
            //operation 10^x
            case R.id.iv_10_raised_to_x:
                //making et_main and tv_display to display ans after equal is pressed
                if (flag_equals) {
                    et_main.setText(display + "");
                    flag_equals = false;
                }

                if (et_main.length() > 0) {
                    //condition if it ends with numeric character

                    char chh = et_main.getText().toString().charAt(et_main.getText().toString().length() - 1);

                    if ((chh >= '0' && chh <= '9')) {

                        tv_Display.setText(display);

                        //Condition to check if not re_entering "^y"
                        if (et_main.length() > 2) {
                            if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 2) != '^') {

                                if (SWITCH) {
                                    Flag_add_bracket = true;
                                    expression = et_main.getText();
                                    et_main.append("*10^");
                                    display = display + "10^";
                                } else {
                                    Flag_add_bracket = true;
                                    expression = et_main.getText();
                                    et_main.append("*2^");
                                    display = display + "2^";
                                }
                            }
                        } else {
                            if (SWITCH) {
                                Flag_add_bracket = true;
                                expression = et_main.getText();
                                et_main.append("*10^");
                                display = display + "10^";
                            } else {
                                Flag_add_bracket = true;
                                expression = et_main.getText();
                                et_main.append("*2^");
                                display = display + "2^";
                            }
                        }
                    } else {
                        tv_Display.setText(display);

                        //Condition to check if not re_entering "^y"
                        if (et_main.length() > 2) {
                            if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 2) != '^') {
                                if (SWITCH) {
                                    Flag_add_bracket = true;
                                    expression = et_main.getText();
                                    et_main.append("10^");
                                    display = display + "10^";
                                } else {
                                    expression = et_main.getText();
                                    et_main.append("2^");
                                    display = display + "2^";
                                }
                            }
                        } else {
                            if (SWITCH) {
                                Flag_add_bracket = true;
                                expression = et_main.getText();
                                et_main.append("10^");
                                display = display + "10^";
                            } else {
                                Flag_add_bracket = true;
                                expression = et_main.getText();
                                et_main.append("2^");
                                display = display + "2^";
                            }
                        }
                    }
                } else {
                    if (SWITCH) {
                        Flag_add_bracket = true;
                        expression = et_main.getText();
                        et_main.append("10^");
                        display = display + "10^";
                    } else {
                        Flag_add_bracket = true;
                        expression = et_main.getText();
                        et_main.append("2^");
                        display = display + "2^";
                    }
                }
                break;
            //Operation ln
            case R.id.iv_ln:
                if (flag_equals) {
                    et_main.setText("");
                    display = "";
                    tv_Display.setText("");
                    flag_equals = false;
                }

                if (et_main.length() > 0) {

                    //condition if it ends with numeric character

                    char chhh = et_main.getText().toString().charAt(et_main.getText().toString().length() - 1);

                    tv_Display.setText(display);

                    if ((chhh >= '0' && chhh <= '9')) {
                        Flag_add_bracket = true;
                        et_main.append("*ln(");
                        display = "ln(" + "";
                        tv_Display.setText(display);

                    } else if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == ')')) {
                        Flag_add_bracket = true;
                        et_main.append("*ln(");
                        display = "ln(" + "";
                        tv_Display.setText(display);
                    } else {
                        Flag_add_bracket = true;
                        et_main.append("ln(");
                        display = "ln(" + "";
                        tv_Display.setText(display);

                    }
                } else {
                    Flag_add_bracket = true;
                    et_main.append("ln(");
                    display = "ln(" + "";
                    tv_Display.setText(display);
                }

                break;
            //Operation log10
            case R.id.iv_log10:
                if (flag_equals) {
                    et_main.setText("");
                    display = "";
                    tv_Display.setText("");
                    flag_equals = false;
                }
                if (et_main.length() > 0) {

                    //condition if it ends with numeric character
                    char chhh = et_main.getText().toString().charAt(et_main.getText().toString().length() - 1);
                    tv_Display.setText(display);

                    //condition to check end character
                    if ((chhh >= '0' && chhh <= '9')) {

                        if (SWITCH) {
                            Flag_add_bracket = true;
                            et_main.append("*" + "log(");
                            display = "log(" + "";
                            tv_Display.setText(display);
                        } else {
                            Flag_add_bracket = true;
                            et_main.append("*" + "log₂(");
                            display = "log₂(" + "";
                            tv_Display.setText(display);
                        }

                    } else if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == ')')) {

                        if (SWITCH) {
                            Flag_add_bracket = true;
                            et_main.append("*" + "log(");
                            display = "log(" + "";
                            tv_Display.setText(display);
                        } else {
                            Flag_add_bracket = true;
                            et_main.append("*" + "log₂(");
                            display = "log₂(" + "";
                            tv_Display.setText(display);
                        }
                    } else {
                        if (SWITCH) {
                            Flag_add_bracket = true;
                            et_main.append("log(");
                            display = "log(" + "";
                            tv_Display.setText(display);
                        } else {
                            Flag_add_bracket = true;
                            et_main.append("log₂(");
                            display = "log₂(" + "";
                            tv_Display.setText(display);
                        }

                    }
                } else {
                    if (SWITCH) {
                        Flag_add_bracket = true;
                        et_main.append("log(");
                        display = "log(" + "";
                        tv_Display.setText(display);
                    } else {
                        Flag_add_bracket = true;
                        et_main.append("log₂(");
                        display = "log₂(" + "";
                        tv_Display.setText(display);
                    }

                }

                break;
            //Operation √(
            case R.id.iv_under_root_x:
                if (flag_equals) {
                    et_main.setText("");
                    display = "";
                    tv_Display.setText("");
                    flag_equals = false;
                }

                if (et_main.length() > 0) {

                    //condition if it ends with numeric character
                    char chhh = et_main.getText().toString().charAt(et_main.getText().toString().length() - 1);
                    tv_Display.setText(display);
                    if ((chhh >= '0' && chhh <= '9')) {
                        Flag_add_bracket = true;
                        et_main.append("*" + "\u221A(");
                        display = "√(" + "";
                        tv_Display.setText(display);

                    } else if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == ')')) {
                        Flag_add_bracket = true;
                        et_main.append("*" + "\u221A(");
                        display = "√(" + "";
                        tv_Display.setText(display);
                    } else {
                        Flag_add_bracket = true;
                        et_main.append("\u221A(");
                        display = "√(" + "";
                        tv_Display.setText(display);

                    }
                } else {
                    Flag_add_bracket = true;
                    et_main.append("\u221A(");
                    display = "√(" + "";
                    tv_Display.setText(display);
                }

                break;
            //operation ³√(
            case R.id.iv_3_under_root_x:
                if (flag_equals) {
                    et_main.setText("");
                    display = "";
                    tv_Display.setText("");
                    flag_equals = false;
                }
                if (et_main.length() > 0) {

                    //condition if it ends with numeric character
                    char chhh = et_main.getText().toString().charAt(et_main.getText().toString().length() - 1);
                    tv_Display.setText(display);
                    if ((chhh >= '0' && chhh <= '9')) {
                        Flag_add_bracket = true;
                        et_main.append("*" + "\u00B3\u221A(");
                        display = "³√(" + "";
                        tv_Display.setText(display);

                    } else if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == ')')) {
                        Flag_add_bracket = true;
                        et_main.append("*" + "\u00B3\u221A(");
                        display = "³√(" + "";
                        tv_Display.setText(display);
                    } else {
                        Flag_add_bracket = true;
                        et_main.append("\u00B3\u221A(");
                        display = "³√(" + "";
                        tv_Display.setText(display);

                    }
                } else {
                    Flag_add_bracket = true;
                    et_main.append("\u00B3\u221A(");
                    display = "³√(" + "";
                    tv_Display.setText(display);
                }

                break;
            //operation 1/(x)
            case R.id.iv_one_by_x:
                if (flag_equals) {
                    et_main.setText("");
                    display = "";
                    tv_Display.setText("");
                    flag_equals = false;
                }
                if (et_main.length() > 0) {

                    //condition if it ends with numeric character
                    char chhh = et_main.getText().toString().charAt(et_main.getText().toString().length() - 1);
                    tv_Display.setText(display);
                    if ((chhh >= '0' && chhh <= '9')) {
                        Flag_add_bracket = true;
                        et_main.append("*" + "1/(");
                        display = "1/(" + "";
                        tv_Display.setText(display);

                    } else if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == ')')) {
                        Flag_add_bracket = true;
                        et_main.append("*" + "1/(");
                        display = "1/(" + "";
                        tv_Display.setText(display);
                    } else {
                        Flag_add_bracket = true;
                        et_main.append("1/(");
                        display = "1/(" + "";
                        tv_Display.setText(display);

                    }
                } else {
                    Flag_add_bracket = true;
                    et_main.append("1/(");
                    display = "1/(" + "";
                    tv_Display.setText(display);
                }

                break;
            //Operation sin sin⁻¹
            case R.id.iv_sin:

                try {
                    flag_equals = true;
                    if (SWITCH) {
                        sin_operation();
                    } else {
                        sin_inverse_operation();
                    }

                    if (flag_equals) {
                        /*share.history.add(new History_Model(trigo, Answer + ""));
                        navigation_history_adapter.notifyDataSetChanged();*/
                    }
                } catch (Exception e) {
                }

                break;
            //cos cos⁻¹ operation
            case R.id.iv_cos:
                try {
                    flag_equals = true;
                    if (SWITCH) {
                        cos_operation();
                    } else {
                        cos_inverse_operation();
                    }

                    if (flag_equals) {
                        /*share.history.add(new History_Model(trigo, Answer + ""));
                        navigation_history_adapter.notifyDataSetChanged();*/
                    }
                } catch (Exception e) {
                }

                break;
            //tan tan⁻¹ operation
            case R.id.iv_tan:

                try {
                    flag_equals = true;

                    if (SWITCH) {
                        tan_operation();
                    } else {
                        tan_inverse_operation();
                    }
                    if (flag_equals) {
                       /* share.history.add(new History_Model(trigo, Answer + ""));
                        navigation_history_adapter.notifyDataSetChanged();*/
                    }
                } catch (Exception e) {
                }
                break;
            //operation sinh
            case R.id.iv_sinh:
                try {
                    flag_equals = true;

                    if (SWITCH) {
                        sinh_operation();
                    } else {
                        sinh_inverse_operation();
                    }
                    if (flag_equals) {
                        /*share.history.add(new History_Model(trigo, Answer + ""));
                        navigation_history_adapter.notifyDataSetChanged();*/
                    }
                } catch (Exception e) {
                }
                break;
            //operation cosh
            case R.id.iv_cosh:
                try {
                    flag_equals = true;
                    if (SWITCH) {
                        cosh_operation();
                    } else {
                        cosh_inverse_operation();
                    }
                    if (flag_equals) {
                        /*share.history.add(new History_Model(trigo, Answer + ""));
                        navigation_history_adapter.notifyDataSetChanged();*/
                    }
                } catch (Exception e) {
                }
                break;
            //operation tanh
            case R.id.iv_tanh:
                try {
                    flag_equals = true;
                    if (SWITCH) {
                        tanh_operation();
                    } else {
                        tanh_inverse_operation();
                    }
                    if (flag_equals) {
                       /* share.history.add(new History_Model(trigo, Answer + ""));
                        navigation_history_adapter.notifyDataSetChanged();*/
                    }
                } catch (Exception e) {
                }
                break;
            //operation random
            case R.id.iv_Rand:
                if (flag_equals) {
                    et_main.setText("");
                    display = "";
                    tv_Display.setText("");
                    flag_equals = false;
                }
                Double d = Math.random();
                display = d.toString();

                et_main.setText(display);
                tv_Display.setText(display);
                flag_equals = true;
                flag_random = true;
                break;
            //operation pi
            case R.id.iv_pi:

                try {
                    if (flag_equals) {
                        et_main.setText("");
                        display = "";
                        tv_Display.setText("");
                        flag_equals = false;
                    }

                    if (flag_plus || flag_minus || flag_divide || flag_multiply || flag_moulo || flag_plus_minus) {
                        display = "";
                        flag_plus = false;
                        flag_minus = false;
                        flag_divide = false;
                        flag_multiply = false;
                        flag_moulo = false;
                        flag_plus_minus = false;
                    }

                    if (flag_string_inside_brackets) {
                        string_inside_bracket = string_inside_bracket + "3.141592653589793";
                    }

                    if (et_main.length() > 0) {

                        //condition if it ends with numeric character
                        char chhh = et_main.getText().toString().charAt(et_main.getText().toString().length() - 1);
                        tv_Display.setText(display);
                        if ((chhh >= '0' && chhh <= '9')) {

                            et_main.append("*" + "3.141592653589793");
                            display = display + "3.141592653589793";
                            tv_Display.setText(display);

                        } else if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == ')')) {

                            et_main.append("*" + "3.141592653589793");
                            display = display + "3.141592653589793";
                            tv_Display.setText(display);
                        } else {
                            et_main.append("3.141592653589793");
                            display = display + "3.141592653589793";
                            tv_Display.setText(display);

                        }
                    } else {
                        et_main.append("3.141592653589793");
                        display = display + "3.141592653589793";
                        tv_Display.setText(display);
                    }
                } catch (Exception e) {

                }
                flag_equals = true;
        /*        if (flag_equals) {
                    et_main.setText("");
                    display = "";
                    tv_Display.setText("");
                    flag_equals = false;
                }

                et_main.append( "pi");
                display = "pi" + "";
                tv_Display.setText(display);*/
                break;
            //operation exponential
            case R.id.iv_e:
                try {
                    if (flag_equals) {
                        et_main.setText("");
                        display = "";
                        tv_Display.setText("");
                        flag_equals = false;
                    }

                    if (flag_plus || flag_minus || flag_divide || flag_multiply || flag_moulo || flag_plus_minus) {
                        display = "";
                        flag_plus = false;
                        flag_minus = false;
                        flag_divide = false;
                        flag_multiply = false;
                        flag_moulo = false;
                        flag_plus_minus = false;
                    }

                    if (flag_string_inside_brackets) {
                        string_inside_bracket = string_inside_bracket + "2.718281828459045";
                    }

                    if (et_main.length() > 0) {

                        //condition if it ends with numeric character
                        char chhh = et_main.getText().toString().charAt(et_main.getText().toString().length() - 1);
                        tv_Display.setText(display);
                        if ((chhh >= '0' && chhh <= '9')) {

                            et_main.append("*" + "2.718281828459045");
                            display = display + "2.718281828459045";
                            tv_Display.setText(display);

                        } else if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == ')')) {

                            et_main.append("*" + "2.718281828459045");
                            display = display + "2.718281828459045";
                            tv_Display.setText(display);
                        } else {
                            et_main.append("2.718281828459045");
                            display = display + "2.718281828459045";
                            tv_Display.setText(display);

                        }
                    } else {
                        et_main.append("2.718281828459045");
                        display = display + "2.718281828459045";
                        tv_Display.setText(display);
                    }

                } catch (Exception e) {

                }
                flag_equals = true;
               /* Log.e("display", display);
                if (flag_equals) {
                    et_main.setText("");
                    display = "";
                    tv_Display.setText("");
                    flag_equals = false;
                }
                et_main.append(  "2.718281828459045");
                display = "e" + "";
                tv_Display.setText(display);*/
                break;

            //...........................................Functions that are yet to syn with  brackets................................................//
            case R.id.iv_x_exclamation:
                if (et_main.length() != 0) {
                    Flag_add_bracket = true;
                    String text;
                    Boolean flag = false;
                    Log.e("string", string_inside_bracket);


                    if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == ')')) {
                        text = string_inside_bracket;
                        flag = true;
                    } else {
                        text = et_main.getText().toString();
                        flag = false;
                    }

                    String res = "";
                    try {
                        if (text.charAt(0) == '-') {
                            tv_Display.setText("Invalid Input");
                            flag_equals = true;
                            return;
                        }

                        Log.e("text", text);


                        Expression expression2 = new ExpressionBuilder(text).build();


                        try {
                            // Calculate the result and display
                            Answer = expression2.evaluate();
                            Log.e("result", "" + Answer);
                        } catch (ArithmeticException ex) {

                            Log.e("Exception", ex.getMessage());
                        }
                        NumberFormat nf2 = NumberFormat.getInstance();
                        nf2.setMaximumIntegerDigits(20);
                        nf2.setMaximumFractionDigits(20);
                        nf2.setGroupingUsed(false);
                        if (Answer.toString().contains("E")) {
                            et_main.setText("Result too big!");
                            flag_equals = true;
                            return;
                        }
                        String re = nf2.format(Answer);
                        Log.e("Res", "re==>" + re);


                        if (re.contains(".")) {

//                            List<Double> doubles = new ArrayList<>();
//                            Double dd = Double.parseDouble(re);
//                            Double temp = Double.parseDouble(re);
//
//                            doubles.clear();
//                            doubles.add(dd);
//                            int size = dd.intValue();
//                            Log.e("size", "size==>" + size);
//
//                            for (int i = 0; i < size; i++) {
//                                temp = temp - 1.0;
//                                doubles.add(temp);
//                                Log.e("DD", "dd==>" + (temp));
//
//                            }
//                            String exp = "";
//                            Double ans = 0.0;
//                            Double pians = 0.0;
//                            Double mainans = 0.0;
//
//                            for (int j = 0; j < doubles.size(); j++) {
//
//
//                                exp = exp + doubles.get(j) + "*";
//
//
//                            }
//
//                            if (size == 0) {
//                                exp = re + "*";
//                            }
//                            exp = exp.substring(0, exp.length() - 1);
//
//                            Log.e("expression", "ecp==>" + exp);
//
//
//                            try {
//                                Expression expression = new ExpressionBuilder(exp).build();
//
//                                ans = expression.evaluate();
//                                 Double.parseDouble(new DecimalFormat(".########").format(doubles.get(doubles.size() - 1)));
//                                  String piexp = "(3.14159265359^0.5" +Double.parseDouble(new DecimalFormat(".#").format(doubles.get(doubles.size() - 1))) + ")/2";
//                                Log.e("piexp","piexp==>"+piexp);
//                                Expression expr = new ExpressionBuilder(piexp).build();
//                                pians = expr.evaluate();
//                                Log.e("piexp", "piexpans==>" + pians);
//
//                                String mexp = ans.toString() + "*1.77245385091";
//
//                                Expression exmain = new ExpressionBuilder(mexp).build();
//                                mainans = exmain.evaluate();
//
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                            Log.e("expression", "ans==>" + ans);
//                            Log.e("expression", "pians==>" + pians);
//                            Log.e("expression", "main ans==>" + mainans);
//                            et_main.setText("" + mainans);
//                            tv_Display.setText("" + mainans);
//                            display = "" + mainans;
//                            flag_equals = true;
                            return;

                        } else {


                            CalculateFactorial cf = new CalculateFactorial();
                            int[] arr = cf.factorial((int) Double.parseDouble(String.valueOf(new ExtendedDoubleEvaluator().evaluate(text))));
                            int res_size = cf.getRes();
                            if (res_size > 20) {
                                for (int i = res_size - 1; i >= res_size - 20; i--) {
                                    if (i == res_size - 2)
                                        res += ".";
                                    res += arr[i];
                                }
                                res += "E" + (res_size - 1);
                            } else {
                                for (int i = res_size - 1; i >= 0; i--) {
                                    res += arr[i];
                                }
                            }
                        }


                        Log.e("Res", "res==>" + res);
                        if (flag) {
                            String s = et_main.getText().toString();
                            s = s.replace(string_inside_bracket, res);
                            s = new ExtendedDoubleEvaluator().evaluate(s).toString();
                            et_main.setText(s);
                            tv_Display.setText(s);
                            string_inside_bracket = "";
                            flag_string_inside_brackets_completed = false;

                        } else {
                            et_main.setText(res);
                            tv_Display.setText(res);
                        }

                    } catch (Exception e) {
                        if (e.toString().contains("ArrayIndexOutOfBoundsException")) {
                            et_main.setText("Result too big!");
                            flag_equals = true;
                        } else {
                            et_main.setText("Invalid");
                            flag_equals = true;
                        }
                        e.printStackTrace();
                    }
                }

                break;

            //%percent operation
            case R.id.iv_percent:

                try {

                    if (et_main.length() > 0) {
                        Flag_add_bracket = true;
                        String s1 = et_main.getText().toString();


                        if (!s1.contains("E")) {

                            Double s = new ExtendedDoubleEvaluator().evaluate(et_main.getText().toString());
                            String a = String.valueOf(s / 100);
                            Answer = Double.valueOf(a);
                            et_main.setText(a + "");
                            display = a + "";
                            tv_Display.setText(a + "");
                        } else {
                            String x = et_main.getText().toString();
                            x = x.replace("E", "*10^");
                            Double s = new ExtendedDoubleEvaluator().evaluate(x);
                            String a = String.valueOf(s / 100);
                            et_main.setText(a + "");
                            display = a + "";
                            tv_Display.setText(a + "");
                            flag_equals = true;
                        }

                    /*    if (!s1.contains("E")) {
                            String x = et_main.getText().toString();
                            x = x.replace("E", "*10^");
                            Double s = new ExtendedDoubleEvaluator().evaluate(x);
                            String a = String.valueOf(s / 100);
                            Answer = Double.valueOf(a);
                            et_main.setText(a + "");
                            display = a + "";
                            tv_Display.setText(a + "");
                        } else {
                            et_main.setText("");
                            if (flag_plus || flag_minus || flag_divide || flag_multiply)
                                display = "Result Too Big" + "";
                            tv_Display.setText(display + "");
                            flag_equals = true;
                        }*/
                    }
                } catch (Exception e) {

                }


    /*            if (et_main.length() > 0) {

                    //Get display value to find percentage of it.
                    String two = display;
                    //Get length of variable in display
                    int count = display.length();
                    //Extract String subtrating string two
                    String one = et_main.getText().toString().substring(0, et_main.getText().toString().length() - count);

                    tv_Display.setText(two);

                    if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '(')
                            ) {

                        if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == ')')) {
                            String s1 = new ExtendedDoubleEvaluator().evaluate(string_inside_bracket).toString();
                            String a = String.valueOf(Double.valueOf(s1) / 100);
                            s1 = et_main.getText().toString();
                            s1 = s1.replace(string_inside_bracket, a);
                            et_main.setText(s1);
                            display = a;
                            string_inside_bracket = "";
                            flag_string_inside_brackets_completed = false;
                        } else {
                            String a = String.valueOf(Double.valueOf(two) / 100);
                            tv_Display.setText(a);
                            et_main.setText(one + a);
                            display = a;
                        }

                    }
                }*/
                break;
            //plus_minus operation
            case R.id.iv_plus_minus:
                try {
                    operation();
                } catch (Exception e) {

                }

                break;
            case R.id.iv_EE:

                Log.e("EE", et_main.getText().toString());

                //flag to notice that plus is pressed
                flag_plus = true;

                if (flag_string_inside_brackets) {
                    string_inside_bracket = string_inside_bracket + "EE";
                }


                //condition to operate if  length is greater than 0
                if (et_main.length() > 0) {

                    tv_Display.setText(display);

                    //condition to not enter one symbol again
                    if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*') &&
                            (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%')) {

                        expression = et_main.getText();
                        mid_calculation();
                        et_main.append("EE");
                    }
                }


                break;
            case R.id.iv_y_under_root_x:
                try {

                    Log.e("ʸ√x", et_main.getText().toString());

                    //flag to notice that plus is pressed
                    flag_plus = true;

                    if (flag_string_inside_brackets) {
                        string_inside_bracket = string_inside_bracket + "ʸ√x";
                    }

                    //condition to operate if  length is greater than 0
                    if (et_main.length() > 0) {
                        Flag_add_bracket = true;
                        tv_Display.setText(display);

                        //condition to not enter one symbol again
                        if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+') &&
                                (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-') &&
                                (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/') &&
                                (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*') &&
                                (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%')) {

                            expression = et_main.getText();
                            mid_calculation();
                            et_main.append("ʸ√x");
                        }
                    } else {
                        Toast.makeText(ScientificCalculatorActivity.this, "Add Number first", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }
                break;

        }

    }

    private void dot_operation() {

        if (et_main.getText().length() > 0) {
            count_dot = 0;

            if (flag_random) {
                flag_equals = false;
            }
            if (flag_equals) {
                et_main.setText("");
                display = "";
                tv_Display.setText("");
                flag_equals = false;
            }

            char[] array = display.toCharArray();

            for (int i = array.length - 1; i >= 0; i--) {

                if (array[i] == '.') {
                    count_dot++;
                }
                if (count_dot == 1) {
                    break;
                }
            }

            if (count_dot == 0) {
                if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '.') &&
                        (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+') &&
                        (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-') &&
                        (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/') &&
                        (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*') &&
                        (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%')) {

                    char chhh = et_main.getText().toString().charAt(et_main.getText().toString().length() - 1);

                    tv_Display.setText(display);
                    Log.e("dot", "" + chhh);
                    if ((chhh >= '0' && chhh <= '9')) {


                        if (flag_string_inside_brackets) {
                            string_inside_bracket = string_inside_bracket + ".";
                        }

                        Log.e("dot", "if" + chhh);
                        et_main.append(".");
                        display = display + ".";
                        tv_Display.setText(display);


                    } else {
                        Log.e("dot", "else" + chhh);
                        et_main.append("*0" + ".");
                        display = display + "";
                        tv_Display.setText(display);
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

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
        return true;
    }

  /*  private void Watch_History() {
        drawer_layout.openDrawer(Gravity.LEFT);
//        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            //drawer is open
        } else {
//            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }*/


    private void sin_operation() {
        if (et_main.length() > 0) {

            Double s = new ExtendedDoubleEvaluator().evaluate(et_main.getText().toString());


            trigo = "sin(" + s + ")";
            if (Radian_Degree.equalsIgnoreCase("DEG")) {

                if (s == 180.0 || s == 360.0) {
                    s = 0.0;
                } else {
                    s = Math.sin(Math.toRadians(s));
                }
            } else {
                s = Math.sin(s);
            }
            Answer = s;
            et_main.setText(s + "");
            display = s + "";
            tv_Display.setText(s + "");
        } else {
            Toast.makeText(ScientificCalculatorActivity.this, "Add Number first", Toast.LENGTH_SHORT).show();
        }
    }

    private void cos_operation() {

        if (et_main.length() > 0) {

            Double s = new ExtendedDoubleEvaluator().evaluate(et_main.getText().toString());
            trigo = "cos(" + s + ")";
            if (Radian_Degree.equalsIgnoreCase("DEG")) {

                if (s == 90.0) {
                    s = 0.0;
                } else if (s == 180.0) {
                    s = -1.0;
                } else if (s == 360.0) {
                    s = 1.0;
                } else {
                    s = Math.cos(Math.toRadians(s));
                }


            } else {
                s = Math.cos(s);
            }
            Answer = s;
            et_main.setText(s + "");
            display = s + "";
            tv_Display.setText(s + "");
        } else {
            Toast.makeText(ScientificCalculatorActivity.this, "Add Number first", Toast.LENGTH_SHORT).show();
        }
    }

    private void tan_operation() {


        if (et_main.length() > 0) {

            Double s = new ExtendedDoubleEvaluator().evaluate(et_main.getText().toString());
            trigo = "tan(" + s + ")";
            if (Radian_Degree.equalsIgnoreCase("DEG")) {

                if (s == 45.0) {
                    s = 1.0;
                } else if (s == 90.0) {
                    s = -9649380295141.232;
                } else if (s == 180.0) {
                    s = 0.0;
                } else if (s == 360.0) {
                    s = 0.0;
                } else {
                    s = Math.tan(Math.toRadians(s));
                }
            } else {
                s = Math.tan(s);
            }

            if (s == -9649380295141.232) {

                display = "Not Defined";
                et_main.setText(display + "");
                Answer = 0.0;
                tv_Display.setText(display + "");
            } else {

                Answer = s;
                et_main.setText(s + "");
                display = s + "";
                tv_Display.setText(s + "");
            }
        } else {
            Toast.makeText(ScientificCalculatorActivity.this, "Add Number first", Toast.LENGTH_SHORT).show();
        }
    }

    private void sin_inverse_operation() {

        if (et_main.length() > 0) {

            Double s = new ExtendedDoubleEvaluator().evaluate(et_main.getText().toString());
            trigo = "sin⁻¹(" + s + ")";
            if (Radian_Degree.equalsIgnoreCase("DEG")) {

                s = Math.toDegrees(Math.asin(s));
            } else {
                s = Math.asin(s);
            }
            Answer = s;
            et_main.setText(s + "");
            display = s + "";
            tv_Display.setText(s + "");
        } else {
            Toast.makeText(ScientificCalculatorActivity.this, "Add Number first", Toast.LENGTH_SHORT).show();
        }
    }

    private void tan_inverse_operation() {

        if (et_main.length() > 0) {

            Double s = new ExtendedDoubleEvaluator().evaluate(et_main.getText().toString());
            trigo = "tan⁻¹(" + s + ")";
            if (Radian_Degree.equalsIgnoreCase("DEG")) {

                s = Math.toDegrees(Math.atan(s));
            } else {
                s = Math.atan(s);
            }
            Answer = s;
            et_main.setText(s + "");
            display = s + "";
            tv_Display.setText(s + "");
        } else {
            Toast.makeText(ScientificCalculatorActivity.this, "Add Number first", Toast.LENGTH_SHORT).show();
        }
    }

    private void cos_inverse_operation() {

        if (et_main.length() > 0) {

            Double s = new ExtendedDoubleEvaluator().evaluate(et_main.getText().toString());
            trigo = "cos⁻¹(" + s + ")";
            if (Radian_Degree.equalsIgnoreCase("DEG")) {

                s = Math.toDegrees(Math.acos(s));
            } else {
                s = Math.acos(s);
            }
            Answer = s;
            et_main.setText(s + "");
            display = s + "";
            tv_Display.setText(s + "");
        } else {
            Toast.makeText(ScientificCalculatorActivity.this, "Add Number first", Toast.LENGTH_SHORT).show();
        }
    }


    private void sinh_operation() {

        if (et_main.length() > 0) {
            Double s = new ExtendedDoubleEvaluator().evaluate(et_main.getText().toString());
            trigo = "sinh(" + s + ")";
            s = Math.sinh(s);
            Answer = s;
            et_main.setText(s + "");
            display = s + "";
            tv_Display.setText(s + "");
        } else {
            Toast.makeText(ScientificCalculatorActivity.this, "Add Number first", Toast.LENGTH_SHORT).show();
        }
    }

    private void cosh_operation() {
        if (et_main.length() > 0) {
            Double s = new ExtendedDoubleEvaluator().evaluate(et_main.getText().toString());
            trigo = "cosh(" + s + ")";
            s = Math.cosh(s);
            Answer = s;
            et_main.setText(s + "");
            display = s + "";
            tv_Display.setText(s + "");
        } else {
            Toast.makeText(ScientificCalculatorActivity.this, "Add Number first", Toast.LENGTH_SHORT).show();
        }
    }

    private void tanh_operation() {
        if (et_main.length() > 0) {
            Double s = new ExtendedDoubleEvaluator().evaluate(et_main.getText().toString());
            trigo = "tanh(" + s + ")";
            s = Math.tanh(s);
            Answer = s;
            et_main.setText(s + "");
            display = s + "";
            tv_Display.setText(s + "");
        } else {
            Toast.makeText(ScientificCalculatorActivity.this, "Add Number first", Toast.LENGTH_SHORT).show();
        }
    }

    private void sinh_inverse_operation() {

        if (et_main.length() > 0) {

            Double s = new ExtendedDoubleEvaluator().evaluate(et_main.getText().toString());
            trigo = "sinh⁻¹(" + s + ")";
            s = Math.log(s + Math.sqrt((s * s) + 1));
            Answer = s;
            et_main.setText(s + "");
            display = s + "";
            tv_Display.setText(s + "");
        } else {
            Toast.makeText(ScientificCalculatorActivity.this, "Add Number first", Toast.LENGTH_SHORT).show();
        }
    }

    private void cosh_inverse_operation() {
        if (et_main.length() > 0) {

            Double s = new ExtendedDoubleEvaluator().evaluate(et_main.getText().toString());
            trigo = "cosh⁻¹(" + s + ")";
            s = Math.log(s + Math.sqrt((s * s) - 1));
            Answer = s;
            et_main.setText(s + "");
            display = s + "";
            tv_Display.setText(s + "");
        } else {
            Toast.makeText(ScientificCalculatorActivity.this, "Add Number first", Toast.LENGTH_SHORT).show();
        }

    }

    private void tanh_inverse_operation() {
        if (et_main.length() > 0) {

            Double s = new ExtendedDoubleEvaluator().evaluate(et_main.getText().toString());
            trigo = "tanh⁻¹(" + s + ")";
            s = 0.5 * Math.log((1 + s) / (1 - s));
            Answer = s;
            et_main.setText(s + "");
            display = s + "";
            tv_Display.setText(s + "");
        } else {
            Toast.makeText(ScientificCalculatorActivity.this, "Add Number first", Toast.LENGTH_SHORT).show();
        }
    }

    private void operation() {

        if (et_main.getText().toString().length() != 0) {

            if (flag_equals) {
//                et_main.setText("");
//                display = "";
//                tv_Display.setText("");
                flag_equals = false;
            }

            if (press_plus_minus) {

                if ((et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '+') &&
                        (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '-') &&
                        (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '/') &&
                        (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '*') &&
                        (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) != '%')) {

                    if (et_main.getText().toString().charAt(et_main.getText().toString().length() - 1) == ')') {
                        Log.e("inside", "inside");

                        String s = et_main.getText().toString();
                        s = s.replace(display + ")", "-" + display + ")");
                        et_main.setText(s);
                        tv_Display.setText("-" + display);
                        press_plus_minus = false;

                    } else {

                        int count = display.length();
                        String one = et_main.getText().toString().substring(0, et_main.getText().toString().length() - count);
                        et_main.setText(one + "-" + display);
                        tv_Display.setText("-" + display);
                        press_plus_minus = false;
                    }
                }

            } else {

                String s = et_main.getText().toString();
                s = s.replace("-" + display, display);
                et_main.setText(s);
                tv_Display.setText("" + display);
                press_plus_minus = true;

            }
        }
    }

    private void mid_calculation() {

        int count_right_bracket = 0, count_left_bracket = 0;
        try {
            // Create an object of DoubleEvaluator for getting ans in double type
            ExtendedDoubleEvaluator evaluator = new ExtendedDoubleEvaluator();
            String s = String.valueOf(expression);
            if (s.contains("e^"))
                s = s.replace("e", "2.71828");
            Log.e("Str", s);
            if (Flag_add_bracket) {
                //Check the frequency of left bracket
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == '(') {
                        count_left_bracket++;
                        Log.e("count_left_bracket", "" + count_left_bracket);
                    }
                    if (s.charAt(i) == ')') {
                        count_right_bracket++;
                        Log.e("count_right_bracket", "" + count_right_bracket);
                    }
                }

                int diff = count_left_bracket - count_right_bracket;
                Log.e("diff", "" + diff);

                for (int i = 0; i < diff; i++) {
                    s = s + ")";
                }
                Flag_add_bracket = false;
            }
            tv_Display.setText(s);

            if (s.contains("EE")) {
                s = s.replace("EE", "*10^");
            }

            if (s.contains("ʸ√x")) {
                Log.e("inside", "inside");
                s = s.replace("ʸ√x", "^(1/");
                s = s + ")";
            }


            if (s.contains("√") || s.contains("log") || s.contains("ln")) {
                Answer = evaluator.evaluate(s);
            } else {
                Expression expression2 = new ExpressionBuilder(s).build();

                try {
                    // Calculate the result and display
                    Answer = expression2.evaluate();
                    Log.e("result", "" + Answer);
                } catch (ArithmeticException ex) {
                    // Display an error message
                    ex.printStackTrace();
                    Log.e("exp", "exp==>" + ex.getMessage());
                    if (ex.getMessage().equalsIgnoreCase("Division by zero!")) ;
                    {
                        tv_Display.setText("Cannot divide by zero");
                        // flag_equals=false;
                        return;
                    }

                }
            }
            Log.e("Answer", Answer + "");

            if (flag_equals) {
               /* share.history.add(new History_Model(s, Answer + ""));
                navigation_history_adapter.notifyDataSetChanged();*/

                Log.e("histroy", s + "==>" + Answer);
            }
            Log.e("Display=Answer", "Answer==>" + Answer);
            Answer = Double.parseDouble(new DecimalFormat(".########").format(Answer));


            if (Answer.toString().charAt(Answer.toString().length() - 1) == '0' && Answer.toString().charAt(Answer.toString().length() - 2) == '.') {
                display = Answer.toString().replace(".0", "");
            } else {
                display = Answer.toString();
            }


            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumIntegerDigits(20);
            nf.setMaximumFractionDigits(20);
            nf.setGroupingUsed(false);
            String re = nf.format(Answer);
            if (re.length() > 17) {
                tv_Display.setText(display);

                et_main.setText(display);
                //   et_main.setSelection(et_main.getText().length());
                //  et_main.setCursorVisible(true);
            } else {
                tv_Display.setText(re);

                display = re;
                et_main.setText(re);
                // et_main.setSelection(et_main.getText().length());
                // et_main.setCursorVisible(true);
            }

            if (equal) {
                equal = false;
                dbHelperClass.myhelper.insertData("Scientific Calculator", String.valueOf(expression), tv_Display.getText().toString(), String.valueOf(expression) + "=" + tv_Display.getText().toString());

            }


        }
        // Catching error for wrong expressions otherwise app will crash
        catch (Exception e) {
            if (flag) {
                flag = false;
                tv_Display.setText("Syntax Error");

            } else {
                tv_Display.setText("");

            }
            flag_equals = false;
            e.printStackTrace();
            Log.e("Exception", e + "data" + flag);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        isInForeGround = true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*private void back() {
        final Dialog dialog = new Dialog(ScientificCalculatorActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit_editing);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
        Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
        Button btnRating = (Button) dialog.findViewById(R.id.btnRating);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout((int) (0.9 * DisplayMetricsHandler.getScreenWidth()), Toolbar.LayoutParams.WRAP_CONTENT);
        if (dialog != null && !dialog.isShowing())
            dialog.show();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isInForeGround = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isInForeGround = false;
    }


}