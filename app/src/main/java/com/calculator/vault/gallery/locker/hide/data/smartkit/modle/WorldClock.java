package com.calculator.vault.gallery.locker.hide.data.smartkit.modle;

public class WorldClock {
    private int displayFormat;
    private int id;
    private String timeZoneId;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeZoneId() {
        return this.timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public int getDisplayFormat() {
        return this.displayFormat;
    }

    public void setDisplayFormat(int displayFormat) {
        this.displayFormat = displayFormat;
    }
}
