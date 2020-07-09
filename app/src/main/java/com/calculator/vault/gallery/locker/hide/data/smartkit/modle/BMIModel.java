package com.calculator.vault.gallery.locker.hide.data.smartkit.modle;

/**
 * Created by ABC on 9/23/2017.
 */

public class BMIModel {
    Float Bmi;
    String date,Age,Weight,Height,Time;


    public BMIModel(String weight, Float bmi, String date, String time, String age, String height ) {
        Weight = weight;
        Height = height;
        Bmi = bmi;
        Time = time;
        Age = age;
        this.date = date;
    }
    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }



    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }




    public Float getBmi() {
        return Bmi;
    }

    public void setBmi(Float bmi) {
        Bmi = bmi;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
