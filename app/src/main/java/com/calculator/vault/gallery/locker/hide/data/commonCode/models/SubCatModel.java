package com.calculator.vault.gallery.locker.hide.data.commonCode.models;

/**
 * Created by Bansi on 11-10-2017.
 */
public class SubCatModel {

    public String id;
    public String app_id;
    public String position;
    public String icon;
    public String name;
    public String star;
    public String installed_range;
    public String app_link;
    public String banner;
    public String banner_image = "";

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getInstalled_range() {
        return installed_range;
    }

    public void setInstalled_range(String installed_range) {
        this.installed_range = installed_range;
    }

    public String getApp_link() {
        return app_link;
    }

    public void setApp_link(String app_link) {
        this.app_link = app_link;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }
}
