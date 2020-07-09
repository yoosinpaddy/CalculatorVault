package com.calculator.vault.gallery.locker.hide.data.smartkit.modle;

import java.io.Serializable;

/**
 * Created by admin on 1/12/2017.
 */
public class AdModel implements Serializable {

    public String thumb_image;
    public String app_link;
    public String name;

    public String full_thumb_image;
    public String package_name;
    public String full_img;

    public String getFull_thumb_image() {
        return full_thumb_image;
    }

    public void setFull_thumb_image(String full_thumb_image) {
        this.full_thumb_image = full_thumb_image;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getFull_img() {
        return full_img;
    }

    public void setFull_img(String full_img) {
        this.full_img = full_img;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getApp_link() {
        return app_link;
    }

    public void setApp_link(String app_link) {
        this.app_link = app_link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AdModel{" +
                "thumb_image='" + thumb_image + '\'' +
                ", app_link='" + app_link + '\'' +
                ", name='" + name + '\'' +
                ", full_thumb_image='" + full_thumb_image + '\'' +
                ", package_name='" + package_name + '\'' +
                ", full_img='" + full_img + '\'' +
                '}';
    }
}
