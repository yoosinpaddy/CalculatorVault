package com.calculator.vault.gallery.locker.hide.data.smartkit.modle;

import java.io.Serializable;

/**
 * Created by admin on 3/29/2018.
 */
public class KeyModel implements Serializable {

    public String api_key;
    public String id;

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "KeyModel{" +
                "api_key='" + api_key + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
