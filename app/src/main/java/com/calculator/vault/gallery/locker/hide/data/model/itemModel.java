package com.calculator.vault.gallery.locker.hide.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

public class itemModel implements Parcelable {
    private boolean isSelected;
    private File inputFile;
    private String oldFilename;
    private String newFilename;
    private String oldFilepath;
    private String newFilepath;
    private String type;
    private String status;
    private String extension;
    private int ID;

    public itemModel(String type,String oldFilename, String newFilename, String oldFilepath, String newFilepath,String status) {
        this.oldFilename = oldFilename;
        this.newFilename = newFilename;
        this.oldFilepath = oldFilepath;
        this.newFilepath = newFilepath;
        this.type = type;
        this.status = status;
    }

    public itemModel() {

    }

    protected itemModel(Parcel in) {
        isSelected = in.readByte() != 0;
        oldFilename = in.readString();
        newFilename = in.readString();
        oldFilepath = in.readString();
        newFilepath = in.readString();
        type = in.readString();
        status = in.readString();
        extension = in.readString();
        ID = in.readInt();
    }

    public static final Creator<itemModel> CREATOR = new Creator<itemModel>() {
        @Override
        public itemModel createFromParcel(Parcel in) {
            return new itemModel(in);
        }

        @Override
        public itemModel[] newArray(int size) {
            return new itemModel[size];
        }
    };

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getOldFilename() {
        return oldFilename;
    }

    public void setOldFilename(String oldFilename) {
        this.oldFilename = oldFilename;
    }

    public String getNewFilename() {
        return newFilename;
    }

    public void setNewFilename(String newFilename) {
        this.newFilename = newFilename;
    }

    public String getOldFilepath() {
        return oldFilepath;
    }

    public void setOldFilepath(String oldFilepath) {
        this.oldFilepath = oldFilepath;
    }

    public String getNewFilepath() {
        return newFilepath;
    }

    public void setNewFilepath(String newFilepath) {
        this.newFilepath = newFilepath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeString(oldFilename);
        dest.writeString(newFilename);
        dest.writeString(oldFilepath);
        dest.writeString(newFilepath);
        dest.writeString(type);
        dest.writeString(status);
        dest.writeString(extension);
        dest.writeInt(ID);
    }
}
