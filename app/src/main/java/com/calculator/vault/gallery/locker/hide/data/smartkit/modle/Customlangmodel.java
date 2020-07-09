package com.calculator.vault.gallery.locker.hide.data.smartkit.modle;

public class Customlangmodel
{
    private int id;
    private int imageId;
    private String lang;
    private String name;

    public Customlangmodel(int _id, int _imageId, String _lang, String _name) {
        this.id = _id;
        this.imageId = _imageId;
        this.name = _name;
        this.lang = _lang;
    }

    public int getId() {
        return this.id;
    }

    public int getImageId() {
        return this.imageId;
    }

    public String getName() {
        return this.name;
    }

    public String getLang() {
        return this.lang;
    }

    public void setId(int _id) {
        this.id = _id;
    }

    public void setImageId(int _imageid) {
        this.imageId = _imageid;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    public void setLang(String _lang) {
        this.lang = _lang;
    }

    public String toString() {
        return this.name;
    }
}
