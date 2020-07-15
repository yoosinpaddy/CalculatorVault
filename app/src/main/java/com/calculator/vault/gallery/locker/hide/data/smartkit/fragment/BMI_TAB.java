package com.calculator.vault.gallery.locker.hide.data.smartkit.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.smartkit.common.BMIShareData;

public class BMI_TAB extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.s_activity_bmi_tab, container, false);

        TextView tv_1 = (TextView) view.findViewById(R.id.tv1);
        TextView tv_2 = (TextView) view.findViewById(R.id.tv2);
        TextView tv_3 = (TextView) view.findViewById(R.id.tv3);
        TextView tv_4 = (TextView) view.findViewById(R.id.tv4);
        TextView tv_5 = (TextView) view.findViewById(R.id.tv5);
        TextView tv_6 = (TextView) view.findViewById(R.id.tv6);
        TextView tv_7 = (TextView) view.findViewById(R.id.tv7);
        TextView tv_8 = (TextView) view.findViewById(R.id.tv8);
        TextView tv_9 = (TextView) view.findViewById(R.id.tv9);
        TextView tv_10 = (TextView) view.findViewById(R.id.tv10);
        TextView tv_11 = (TextView) view.findViewById(R.id.tv11);
        TextView tv_12 = (TextView) view.findViewById(R.id.tv12);
        TextView tv_13 = (TextView) view.findViewById(R.id.tv13);
        TextView tv_14 = (TextView) view.findViewById(R.id.tv14);
        TextView tv_15 = (TextView) view.findViewById(R.id.tv15);
        TextView tv_16 = (TextView) view.findViewById(R.id.tv16);
        TextView tv_17 = (TextView) view.findViewById(R.id.tv17);
        TextView tv_18 = (TextView) view.findViewById(R.id.tv18);
        TextView tv_19 = (TextView) view.findViewById(R.id.tv19);
        TextView tv_20 = (TextView) view.findViewById(R.id.tv20);
        TableRow tr_1 = (TableRow) view.findViewById(R.id.tablerow1);

        //get data from s_activity_world_clock activity to fragment
        //tv_1.setText("" + BMIShareData.bmidata);
        tv_1.setText(String.format("%.2f", BMIShareData.bmidata));
        //set text color
        if (BMIShareData.bmidata < 16.0) {
            tv_1.setTextColor(getResources().getColor(R.color.bmi_text7));
            // tv_2.setTextColor(getResources().getColor(R.color.bmi_text7));
        } else if (BMIShareData.bmidata < 18.5) {
            tv_1.setTextColor(getResources().getColor(R.color.bmi_text2));
            //tv_2.setTextColor(getResources().getColor(R.color.bmi_text2));
        } else if (BMIShareData.bmidata < 25.0) {
            tv_1.setTextColor(getResources().getColor(R.color.bmi_text4));
            // tv_2.setTextColor(getResources().getColor(R.color.bmi_text4));
        } else {
            tv_1.setTextColor(getResources().getColor(R.color.bmi_text4));
            //tv_2.setTextColor(getResources().getColor(R.color.bmi_text4));
        }

        // set text color
        int bmiColor = colorBMI(BMIShareData.bmidata);


        // set index text
        int bmiInterpretation = interpretBMI(BMIShareData.bmidata);
        tv_2.setText(getResources().getString(bmiInterpretation));
        tv_19.setText(getResources().getString(bmiInterpretation));

        //bmi set index valyu(interpretBMIValyu)
        int BMIValyu = interpretBMIValyu(BMIShareData.bmidata);
        tv_20.setText(getResources().getString(BMIValyu));


        Animation myAnim = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_out_in_textview);
        if (BMIShareData.bmidata < 16.0) {
            tv_3.setTextColor(getResources().getColor(R.color.bmi_text7));
            tv_4.setTextColor(getResources().getColor(R.color.bmi_text7));
            tv_3.startAnimation(myAnim);
            tv_4.startAnimation(myAnim);
        } else if (BMIShareData.bmidata < 16.9) {
            tv_5.setTextColor(getResources().getColor(R.color.bmi_text7));
            tv_6.setTextColor(getResources().getColor(R.color.bmi_text7));
            tv_5.startAnimation(myAnim);
            tv_6.startAnimation(myAnim);
        } else if (BMIShareData.bmidata < 18.4) {
            tv_7.setTextColor(getResources().getColor(R.color.bmi_text7));
            tv_8.setTextColor(getResources().getColor(R.color.bmi_text7));
            tv_7.startAnimation(myAnim);
            tv_8.startAnimation(myAnim);
        } else if (BMIShareData.bmidata < 24.9) {
            tv_9.setTextColor(getResources().getColor(R.color.bmi_text2));
            tv_10.setTextColor(getResources().getColor(R.color.bmi_text2));
            tv_9.startAnimation(myAnim);
            tv_10.startAnimation(myAnim);
        } else if (BMIShareData.bmidata < 25.9) {
            tv_11.setTextColor(getResources().getColor(R.color.bmi_text2));
            tv_12.setTextColor(getResources().getColor(R.color.bmi_text2));
            tv_11.startAnimation(myAnim);
            tv_12.startAnimation(myAnim);
        } else if (BMIShareData.bmidata < 34.9) {
            tv_13.setTextColor(getResources().getColor(R.color.bmi_text4));
            tv_14.setTextColor(getResources().getColor(R.color.bmi_text4));
            tv_13.startAnimation(myAnim);
            tv_14.startAnimation(myAnim);
        } else if (BMIShareData.bmidata < 39.9) {
            tv_15.setTextColor(getResources().getColor(R.color.bmi_text4));
            tv_16.setTextColor(getResources().getColor(R.color.bmi_text4));
            tv_15.startAnimation(myAnim);
            tv_16.startAnimation(myAnim);
        } else {
            tv_17.setTextColor(getResources().getColor(R.color.bmi_text4));
            tv_18.setTextColor(getResources().getColor(R.color.bmi_text4));
            tv_17.startAnimation(myAnim);
            tv_18.startAnimation(myAnim);
        }
        return view;
    }


    private int colorBMI(float bmiValue) {
        if (BMIShareData.bmidata < 16.0) {
            return R.color.bmi_text3;
        } else if (bmiValue < 18.5) {
            return R.color.bmi_text7;
        } else if (bmiValue < 25.0) {
            return R.color.bmi_text4;
        } else {
            return R.color.bmi_text9;
        }
    }

    private int interpretBMI(float bmiValue) {
        if (BMIShareData.bmidata < 16.0) {
            return R.string.bmiUnderweight;
        } else if (BMIShareData.bmidata < 18.5) {
            return R.string.bminormal;
        } else if (BMIShareData.bmidata < 25) {
            return R.string.bmiOverweight;
        } else {
            return R.string.bmiOverweight;
        }
    }

    private int interpretBMIValyu(float bmiValue) {
        if (BMIShareData.bmidata < 16.0) {
            return R.string.bmiunder;
        } else if (BMIShareData.bmidata < 18) {
            return R.string.normal;
        } else if (BMIShareData.bmidata < 25) {
            return R.string.over;
        } else {
            return R.string.over;
        }
    }
}
