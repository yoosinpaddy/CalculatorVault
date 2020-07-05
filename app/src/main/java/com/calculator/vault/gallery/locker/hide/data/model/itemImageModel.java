package com.calculator.vault.gallery.locker.hide.data.model;

public class itemImageModel {
    private boolean isSelected;
    private String imagePath;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
