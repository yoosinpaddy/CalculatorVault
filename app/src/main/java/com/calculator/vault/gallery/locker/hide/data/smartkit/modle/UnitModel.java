package com.calculator.vault.gallery.locker.hide.data.smartkit.modle;

import java.io.Serializable;

public class UnitModel implements Serializable {
    String unitFullName;
    String unitName;

    public String getUnitFullName() {
        return unitFullName;
    }

    public void setUnitFullName(String unitFullName) {
        this.unitFullName = unitFullName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public String toString() {
        return "UnitModel{" +
                "unitFullName='" + unitFullName + '\'' +
                ", unitName='" + unitName + '\'' +
                '}';
    }
}
