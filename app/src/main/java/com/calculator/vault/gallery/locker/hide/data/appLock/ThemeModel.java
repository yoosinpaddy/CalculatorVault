package com.calculator.vault.gallery.locker.hide.data.appLock;

public class ThemeModel {

    private boolean isSelected;
    private int themeRes;
    private int themeBgRes;
    private int shapeRes;
    private int lineColorRes;
    private int dashRes;
    private int dotRes;
    private int oneRes;
    private int twoRes;
    private int threeRes;
    private int fourRes;
    private int fiveRes;
    private int sixRes;
    private int sevenRes;
    private int eightRes;
    private int nineRes;
    private int zeroRes;
    private int delRes;

    public ThemeModel(boolean isSelected, int themeRes, int themeBgRes, int shapeRes, int lineColorRes) {
        this.isSelected = isSelected;
        this.themeRes = themeRes;
        this.themeBgRes = themeBgRes;
        this.shapeRes = shapeRes;
        this.lineColorRes = lineColorRes;
    }

    public ThemeModel(boolean isSelected, int themeRes, int themeBgRes, int dashRes, int dotRes,
                      int oneRes, int twoRes, int threeRes, int fourRes, int fiveRes, int sixRes,
                      int sevenRes, int eightRes, int nineRes, int zeroRes, int delRes) {
        this.isSelected = isSelected;
        this.themeRes = themeRes;
        this.themeBgRes = themeBgRes;
        this.dashRes = dashRes;
        this.dotRes = dotRes;
        this.oneRes = oneRes;
        this.twoRes = twoRes;
        this.threeRes = threeRes;
        this.fourRes = fourRes;
        this.fiveRes = fiveRes;
        this.sixRes = sixRes;
        this.sevenRes = sevenRes;
        this.eightRes = eightRes;
        this.nineRes = nineRes;
        this.zeroRes = zeroRes;
        this.delRes = delRes;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getThemeRes() {
        return themeRes;
    }

    public void setThemeRes(int themeRes) {
        this.themeRes = themeRes;
    }

    public int getThemeBgRes() {
        return themeBgRes;
    }

    public void setThemeBgRes(int themeBgRes) {
        this.themeBgRes = themeBgRes;
    }

    public int getShapeRes() {
        return shapeRes;
    }

    public void setShapeRes(int shapeRes) {
        this.shapeRes = shapeRes;
    }

    public int getLineColorRes() {
        return lineColorRes;
    }

    public void setLineColorRes(int lineColorRes) {
        this.lineColorRes = lineColorRes;
    }

    public int getDashRes() {
        return dashRes;
    }

    public void setDashRes(int dashRes) {
        this.dashRes = dashRes;
    }

    public int getDotRes() {
        return dotRes;
    }

    public void setDotRes(int dotRes) {
        this.dotRes = dotRes;
    }

    public int getOneRes() {
        return oneRes;
    }

    public void setOneRes(int oneRes) {
        this.oneRes = oneRes;
    }

    public int getTwoRes() {
        return twoRes;
    }

    public void setTwoRes(int twoRes) {
        this.twoRes = twoRes;
    }

    public int getThreeRes() {
        return threeRes;
    }

    public void setThreeRes(int threeRes) {
        this.threeRes = threeRes;
    }

    public int getFourRes() {
        return fourRes;
    }

    public void setFourRes(int fourRes) {
        this.fourRes = fourRes;
    }

    public int getFiveRes() {
        return fiveRes;
    }

    public void setFiveRes(int fiveRes) {
        this.fiveRes = fiveRes;
    }

    public int getSixRes() {
        return sixRes;
    }

    public void setSixRes(int sixRes) {
        this.sixRes = sixRes;
    }

    public int getSevenRes() {
        return sevenRes;
    }

    public void setSevenRes(int sevenRes) {
        this.sevenRes = sevenRes;
    }

    public int getEightRes() {
        return eightRes;
    }

    public void setEightRes(int eightRes) {
        this.eightRes = eightRes;
    }

    public int getNineRes() {
        return nineRes;
    }

    public void setNineRes(int nineRes) {
        this.nineRes = nineRes;
    }

    public int getZeroRes() {
        return zeroRes;
    }

    public void setZeroRes(int zeroRes) {
        this.zeroRes = zeroRes;
    }

    public int getDelRes() {
        return delRes;
    }

    public void setDelRes(int delRes) {
        this.delRes = delRes;
    }
}
