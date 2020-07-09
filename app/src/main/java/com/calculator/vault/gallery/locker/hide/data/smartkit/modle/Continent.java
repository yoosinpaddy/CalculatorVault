package com.calculator.vault.gallery.locker.hide.data.smartkit.modle;

import java.util.List;

public class Continent {
    private List<City> cityList;
    private int id;
    private String name;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getCityList() {
        return this.cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }
}
