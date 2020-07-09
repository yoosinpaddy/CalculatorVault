package com.calculator.vault.gallery.locker.hide.data.smartkit.modle;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Bansi on 11-10-2017.
 */
public class CategoryModel implements Serializable {

    public String id;
    public String name;
    public String is_active;

    public ArrayList<SubCatModel> getSub_category() {
        return sub_category;
    }

    public void setSub_category(ArrayList<SubCatModel> sub_category) {
        this.sub_category = sub_category;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<SubCatModel> sub_category = new ArrayList<SubCatModel>();

}
