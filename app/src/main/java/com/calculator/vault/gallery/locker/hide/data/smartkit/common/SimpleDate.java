package com.calculator.vault.gallery.locker.hide.data.smartkit.common;

import android.util.Log;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class SimpleDate {
    private float MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;
    private int year;
    private int month;
    private int day;
    private int years;
    private int months;
    private int days;
    public static long delta;

    public SimpleDate(int year, int month, int day, int years, int months, int days) {
        this.setYear(year);
        this.setMonth(month);
        this.setDay(day);
        this.setYears(years);
        this.setMonths(months);
        this.setDays(days);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getDaysLeft() {
        Calendar target = getTargetCalendar();
        Log.e("TAG", "getDaysLeft: taregt   " + target);
        Calendar now = getStartCalendar();
        Log.e("TAG", "getDaysLeft: now   " + now);

        if (target.after(now)) {
            delta = (target.getTimeInMillis() - now.getTimeInMillis());

           // Log.e("TAG", "getDaysLeft: delta is  " + delta);
            long days = TimeUnit.MILLISECONDS.toDays(delta);
            long hours = TimeUnit.MILLISECONDS.toHours(delta);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(delta);

            // long seconds = (milliseconds / 1000);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(delta);

             Log.e("Simpledate", "getDaysLeft: delta is "+days+"  "+hours+"  "+minutes+"  "+seconds);
//           return Math.round(delta);

            return Math.round(delta / MILLISECONDS_PER_DAY);
        } else {
            return 0;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + day;
        result = prime * result + month;
        result = prime * result + year;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        SimpleDate other = (SimpleDate) obj;
        return (day == other.day && month == other.month && year == other.year);
    }

    public String toString() {
        return String.format("%d/%02d/%02d", day, month, year);
    }

    private Calendar getTargetCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar;
    }
    private Calendar getStartCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(years, months - 1, days);
        return calendar;
    }
}
