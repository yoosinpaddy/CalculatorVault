package com.calculator.vault.gallery.locker.hide.data.smartkit.modle;

import java.io.Serializable;

public class UnitConverterModel implements Serializable {
    int unitIcon;
    String unitName;

    public int getUnitIcon() {
        return unitIcon;
    }

    public void setUnitIcon(int unitIcon) {
        this.unitIcon = unitIcon;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
