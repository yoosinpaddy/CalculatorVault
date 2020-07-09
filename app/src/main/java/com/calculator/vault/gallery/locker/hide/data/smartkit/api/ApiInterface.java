package com.calculator.vault.gallery.locker.hide.data.smartkit.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("convert")
    Call<JsonObject> getCurrencyValue(@Query("q") String INR_USD, @Query("compact") String y, @Query("apiKey") String key);
}
