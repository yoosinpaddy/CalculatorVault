package com.calculator.vault.gallery.locker.hide.data.smartkit.modle;

public class CurrencyDetailModel {
    String country;
    String currency_code;
    String symbol;
    String name;
    String flag;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "CurrencyDetailModel{" +
                "country='" + country + '\'' +
                ", currency_code='" + currency_code + '\'' +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}
