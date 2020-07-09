package com.calculator.vault.gallery.locker.hide.data.smartkit.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    //https://free.currconv.com/api/v7/convert?q=USD_INR&compact=ultra&apiKey=12d1948ab837d409f084
    //public static final String BASE_URL = "http://free.currencyconverterapi.com/api/v5/";
    public static final String BASE_URL = "https://free.currconv.com/api/v7/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
