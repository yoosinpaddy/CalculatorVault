/*
 * Copyright 2015 Blanyal D'Souza.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.calculator.vault.gallery.locker.hide.data.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.calculator.vault.gallery.locker.hide.data.model.BreakInImageModel;
import com.calculator.vault.gallery.locker.hide.data.model.ContactModel;
import com.calculator.vault.gallery.locker.hide.data.model.CredentialsModel;
import com.calculator.vault.gallery.locker.hide.data.model.NoteListModel;
import com.calculator.vault.gallery.locker.hide.data.model.UserModel;
import com.calculator.vault.gallery.locker.hide.data.model.itemModel;

import java.io.File;
import java.util.ArrayList;

public class ImageVideoDatabase extends SQLiteOpenHelper {
    // Database Version
    private String pathtoWrite = Environment.getExternalStorageDirectory().getPath() + File.separator + ".androidData" + File.separator + ".log" + File.separator + ".check" + File.separator;
    private static final int DATABASE_VERSION = 3;
    private static final String databasewritepath =
            Environment.getExternalStorageDirectory().getPath() + File.separator + ".androidData" + File.separator + ".log" + File.separator + ".check" + File.separator;

    // Database Name
    private static final String DATABASE_NAME = "ImageVideoDatabase";

    // Table name
    private static final String IMAGES_VIDEOS_TABLE = "ImagesVideosTable";
    private static final String VIDEOS_TABLE = "VideosTable";
    private static final String USER_TABLE = "userTable";
    private static final String Notes_TABLE = "notesTable";
    private static final String Contact_Table = "contactTable";
    private static final String Credentialts_Table = "credentialsTable";
    private static final String BreakinReportTable = "breakInReportTable";
    private static final String OTHER_FILES_TABLE = "OtherFiles";

    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NEW_PATH = "newpath";
    private static final String KEY_OLD_PATH = "oldpath";
    private static final String KEY_TYPE = "type";
    private static final String KEY_OLD_NAME = "oldname";
    private static final String KEY_NEW_NAME = "newname";
    private static final String KEY_STATUS = "status";
    private static final String KEY_User = "user_id";
    private static final String KEY_password = "user_password";
    private static final String KEY_Confirmpassword = "confirm_password";
    private static final String KEY_DatabasePath = "user_databasePath";
    private static final String KEY_notes_title = "notesTitle";
    private static final String KEY_notes_text = "notestext";
    private static final String KEY_timeStamp = "timeStamp";
    private static final String KEY_FirstName = "firstname";
    private static final String KEY_LastNAme = "lastname";
    private static final String KEY_PhoneNumber = "phonenumber";
    private static final String KEY_Email = "email";
    private static final String KEY_Birthdate = "birthdate";
    private static final String KEY_FilePAth = "filepath";
    private static final String KEY_DisplayFullname = "fullname";
    private static final String KEY_color = "color";
    private static final String KEY_Title = "title";
    private static final String KEY_Website = "website";
    private static final String KEY_Username = "username";
    private static final String KEY_QUESTION = "question";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_Password = "password";
    private static final String KEY_Notes = "notes";
    private static final String KEY_FileName = "filename";
    private static final String KEY_DateTime = "datetime";
    private static final String KEY_WrongPass = "wrongpass";
    private static final String KEY_ContactID = "contactID";
    private static final String KEY_time = "time";
    private static final String KEY_Date = "date";

    private static final String KEY_EXTENSION = "extension";
    private String TAG = "ImageVideoDatabase";

    private static final String databasepath = databasewritepath + DATABASE_NAME + ".db";

    public ImageVideoDatabase(Context context) {
        // super(context, DATABASE_NAME, null, DATABASE_VERSION);
        super(context, databasepath, null, DATABASE_VERSION);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_IMAGES_VIDEOS_TABLE = "CREATE TABLE " + IMAGES_VIDEOS_TABLE +
                "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TYPE + " TEXT,"
                + KEY_OLD_NAME + " TEXT ,"
                + KEY_NEW_NAME + " TEXT UNIQUE,"
                + KEY_OLD_PATH + " TEXT,"
                + KEY_NEW_PATH + " TEXT,"
                + KEY_STATUS + " TEXT" + ")";
        db.execSQL(CREATE_IMAGES_VIDEOS_TABLE);

        String CREATE_VIDEOS_TABLE = "CREATE TABLE " + VIDEOS_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TYPE + " TEXT,"
                + KEY_OLD_NAME + " TEXT ,"
                + KEY_NEW_NAME + " TEXT UNIQUE,"
                + KEY_OLD_PATH + " TEXT,"
                + KEY_NEW_PATH + " TEXT,"
                + KEY_STATUS + " TEXT" + ")";
        db.execSQL(CREATE_VIDEOS_TABLE);

        String Create_USER_TABLE = "CREATE TABLE " + USER_TABLE +
                "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_password + " TEXT,"
                + KEY_Confirmpassword + " TEXT,"
                + KEY_DatabasePath + " TEXT,"
                + KEY_QUESTION + " INTEGER,"
                + KEY_ANSWER + " TEXT" + ")";
        db.execSQL(Create_USER_TABLE);

        String Create_Notes_Table = "CREATE TABLE " + Notes_TABLE +
                "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_notes_title + " TEXT,"
                + KEY_notes_text + " TEXT,"
                + KEY_timeStamp + " TEXT" + ")";

        db.execSQL(Create_Notes_Table);

        String Create_Contact_Table = "CREATE TABLE " + Contact_Table +
                "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ContactID + " TEXT UNIQUE,"
                + KEY_FirstName + " TEXT,"
                + KEY_LastNAme + " TEXT,"
                + KEY_PhoneNumber + " TEXT,"
                + KEY_Email + " TEXT,"
                + KEY_Birthdate + " TEXT,"
                + KEY_FilePAth + " TEXT , "
                + KEY_DisplayFullname + " TEXT,"
                + KEY_color + " INTEGER" + ")";

        db.execSQL(Create_Contact_Table);
        String Create_Credentials_Table = "CREATE TABLE " + Credentialts_Table +
                "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_Title + " TEXT,"
                + KEY_Website + " TEXT,"
                + KEY_Username + " TEXT,"
                + KEY_Password + " TEXT,"
                + KEY_Notes + " TEXT,"
                + KEY_color + " INTEGER" + ")";

        db.execSQL(Create_Credentials_Table);


        String CREATE_Break_in_TABLE = "CREATE TABLE " + BreakinReportTable +
                "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FileName + " TEXT,"
                + KEY_FilePAth + " TEXT ,"
                + KEY_DateTime + " TEXT ,"
                + KEY_WrongPass + " TEXT" + ")";
        db.execSQL(CREATE_Break_in_TABLE);

        String CreateOtherFilesTable = "CREATE TABLE " + OTHER_FILES_TABLE +
                "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TYPE + " TEXT,"
                + KEY_OLD_NAME + " TEXT ,"
                + KEY_NEW_NAME + " TEXT UNIQUE,"
                + KEY_OLD_PATH + " TEXT,"
                + KEY_NEW_PATH + " TEXT,"
                + KEY_STATUS + " TEXT" + ")";
        db.execSQL(CreateOtherFilesTable);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Notes Module Added Title
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + Notes_TABLE + " ADD COLUMN " + KEY_notes_title + " TEXT;");
        }

        // New Table For Other Files
        if (oldVersion < 3) {
            String CreateOtherFilesTable = "CREATE TABLE " + OTHER_FILES_TABLE +
                    "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_TYPE + " TEXT,"
                    + KEY_OLD_NAME + " TEXT ,"
                    + KEY_NEW_NAME + " TEXT UNIQUE,"
                    + KEY_OLD_PATH + " TEXT,"
                    + KEY_NEW_PATH + " TEXT,"
                    + KEY_STATUS + " TEXT" + ")";
            db.execSQL(CreateOtherFilesTable);
        }
    }

    //Add new data to database

    public ArrayList<itemModel> getAllDATA(String filenewpath) {
        ArrayList<itemModel> itemModelList = new ArrayList<>();

        // Select all Query
        String selectQuery = "SELECT * FROM " + IMAGES_VIDEOS_TABLE + " WHERE " + KEY_NEW_PATH + " ='" + filenewpath + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (db.isOpen()) {
                if (cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            itemModel itemModel1 = new itemModel();
                            itemModel1.setID(Integer.parseInt(cursor.getString(0)));
                            itemModel1.setType(cursor.getString(1));
                            itemModel1.setOldFilename(cursor.getString(2));
                            itemModel1.setNewFilename(cursor.getString(3));
                            itemModel1.setOldFilepath(cursor.getString(4));
                            itemModel1.setNewFilepath(cursor.getString(5));
                            itemModelList.add(itemModel1);
                        } while (cursor.moveToNext());
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, "getAllDATA: " + ex.getMessage());
        } finally {
            cursor.close();
            db.close();
        }
        return itemModelList;
    }

    public ArrayList<itemModel> getAllFilesData(String filenewpath) {
        ArrayList<itemModel> itemModelList = new ArrayList<>();

        // Select all Query
        String selectQuery = "SELECT * FROM " + OTHER_FILES_TABLE + " WHERE " + KEY_NEW_PATH + " ='" + filenewpath + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (db.isOpen()) {
                if (cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            itemModel itemModel1 = new itemModel();
                            itemModel1.setID(Integer.parseInt(cursor.getString(0)));
                            itemModel1.setType(cursor.getString(1));
                            itemModel1.setOldFilename(cursor.getString(2));
                            itemModel1.setNewFilename(cursor.getString(3));
                            itemModel1.setOldFilepath(cursor.getString(4));
                            itemModel1.setNewFilepath(cursor.getString(5));
                            itemModelList.add(itemModel1);
                        } while (cursor.moveToNext());
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, "getAllDATA: " + ex.getMessage());
        } finally {
            cursor.close();
            db.close();
        }
        return itemModelList;
    }

    public ArrayList<itemModel> getAllDATAVideos(String filenewpath) {
        ArrayList<itemModel> itemModelList = new ArrayList<>();
        // Select all Query
        String selectQuery = "SELECT * FROM " + VIDEOS_TABLE + " WHERE " + KEY_NEW_PATH + " ='" + filenewpath + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // Looping through all rows and adding to list
            if (db.isOpen()) {
                if (cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            itemModel itemModel1 = new itemModel();
                            itemModel1.setID(Integer.parseInt(cursor.getString(0)));
                            itemModel1.setType(cursor.getString(1));
                            itemModel1.setOldFilename(cursor.getString(2));
                            itemModel1.setNewFilename(cursor.getString(3));
                            itemModel1.setOldFilepath(cursor.getString(4));
                            itemModel1.setNewFilepath(cursor.getString(5));

                            // Adding Reminders to list
                            itemModelList.add(itemModel1);
                        } while (cursor.moveToNext());
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getAllDATAVideos: " + e.toString());
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }


        return itemModelList;
    }

    //GEt all Break in Images
    public ArrayList<BreakInImageModel> getAllBreakInImages() {
        ArrayList<BreakInImageModel> itemModelList = new ArrayList<>();

        // Select all Query
        String selectQuery = "SELECT * FROM " + BreakinReportTable;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if (db.isOpen()) {
            if (cursor.moveToFirst()) {
                do {
                    BreakInImageModel itemModel1 = new BreakInImageModel();
                    itemModel1.setID(Integer.parseInt(cursor.getString(0)));
                    itemModel1.setFilename(cursor.getString(1));
                    itemModel1.setFilePath(cursor.getString(2));
                    itemModel1.setDataTime(cursor.getString(3));
                    itemModel1.setWrongPassword(cursor.getString(4));
                    // Adding Reminders to list
                    itemModelList.add(itemModel1);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
//        db.close();
        return itemModelList;
    }

    //Add Break in Report data
    public int addBreakInReportData(BreakInImageModel itemModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_FileName, itemModel.getFilename());
        values.put(KEY_FilePAth, itemModel.getFilePath());
        values.put(KEY_DateTime, itemModel.getDataTime());
        values.put(KEY_WrongPass, itemModel.getWrongPassword());

        // Inserting Row
        long ID = db.insert(BreakinReportTable, null, values);
        db.close();
        return (int) ID;
    }

    //Get Single Break in Report
    public BreakInImageModel getSingleBreakInData(int ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        BreakInImageModel breakInImageModel = new BreakInImageModel();
        String str_ID = String.valueOf(ID);
        String selectQuery = "SELECT * FROM " + BreakinReportTable + " WHERE " + KEY_ID + " ='" + ID + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    breakInImageModel.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                    breakInImageModel.setFilename(cursor.getString(cursor.getColumnIndex(KEY_FileName)));
                    breakInImageModel.setFilePath(cursor.getString(cursor.getColumnIndex(KEY_FilePAth)));
                    breakInImageModel.setDataTime(cursor.getString(cursor.getColumnIndex(KEY_DateTime)));
                    breakInImageModel.setWrongPassword(cursor.getString(cursor.getColumnIndex(KEY_WrongPass)));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return breakInImageModel;
    }

    // Delete single Break in Report Image
    public void removeSingleBreakInReport(int ID) {
        Log.e(TAG, "removeSingleData: BreakInReport:: " + ID);
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "DELETE FROM " + BreakinReportTable + " WHERE " + KEY_ID + " ='" + ID + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (db.isOpen()) {
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                    } while (cursor.moveToNext());
                }
            }
        }
        cursor.close();
        Log.e(TAG, "removeSingleBreakInReport: " + cursor.getCount());
        Log.e(TAG, "removeSingleBreakInReport: data Removed");
        db.close();
    }

    //Clear whole data from break in table
    public void deleteAllBreakin() {
        //SQLiteDatabase db = this.getWritableDatabase();
        // db.delete(TABLE_NAME,null,null);
        //db.execSQL("delete * from"+ TABLE_NAME);
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(BreakinReportTable, null, null);
        db.close();
    }


    // Adding new Data
    public int addData(itemModel itemModel) {

        SQLiteDatabase db = this.getWritableDatabase();

        long ID = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TYPE, itemModel.getType());
            values.put(KEY_OLD_NAME, itemModel.getOldFilename());
            values.put(KEY_NEW_NAME, itemModel.getNewFilename());
            values.put(KEY_OLD_PATH, itemModel.getOldFilepath());
            values.put(KEY_NEW_PATH, itemModel.getNewFilepath());
            values.put(KEY_STATUS, itemModel.getStatus());
            // Inserting Row
            ID = db.insert(IMAGES_VIDEOS_TABLE, null, values);
        } catch (Exception e) {
            Log.e(TAG, "addData: " + e.toString());
        } finally {
            db.close();
        }

        return (int) ID;
    }

    public int addFileData(itemModel itemModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        long ID = -1;
        try {
            ContentValues values = new ContentValues();

            values.put(KEY_TYPE, itemModel.getType());
            values.put(KEY_OLD_NAME, itemModel.getOldFilename());
            values.put(KEY_NEW_NAME, itemModel.getNewFilename());
            values.put(KEY_OLD_PATH, itemModel.getOldFilepath());
            values.put(KEY_NEW_PATH, itemModel.getNewFilepath());
            values.put(KEY_STATUS, itemModel.getStatus());

            // Inserting Row
            ID = db.insert(OTHER_FILES_TABLE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return (int) ID;
    }

    // Adding new Data
    public int addDataVideos(itemModel itemModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        long ID = 0;
        try {
            ContentValues values = new ContentValues();

            values.put(KEY_TYPE, itemModel.getType());
            values.put(KEY_OLD_NAME, itemModel.getOldFilename());
            values.put(KEY_NEW_NAME, itemModel.getNewFilename());
            values.put(KEY_OLD_PATH, itemModel.getOldFilepath());
            values.put(KEY_NEW_PATH, itemModel.getNewFilepath());
            values.put(KEY_STATUS, itemModel.getStatus());

            // Inserting Row
            ID = db.insert(VIDEOS_TABLE, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return (int) ID;
    }


    //Add new Credential
    public int addCredentialData(CredentialsModel credentialsModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_Title, credentialsModel.getTitleText());
        values.put(KEY_Website, credentialsModel.getWebsite());
        values.put(KEY_Username, credentialsModel.getUsername());
        values.put(KEY_Password, credentialsModel.getPassword());
        values.put(KEY_Notes, credentialsModel.getNotes());
        values.put(KEY_color, credentialsModel.getColor());

        // Inserting Row
        long ID = db.insert(Credentialts_Table, null, values);
        db.close();
        return (int) ID;
    }

    //Get all Credentials
    public ArrayList<CredentialsModel> getAllCredentials() {
        ArrayList<CredentialsModel> itemModelList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            // Select all Query
            String selectQuery = "SELECT * FROM " + Credentialts_Table;

            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // Looping through all rows and adding to list
            if (db.isOpen()) {
                if (cursor.moveToFirst()) {
                    do {
                        CredentialsModel itemModel1 = new CredentialsModel();
                        itemModel1.setID(Integer.parseInt(cursor.getString(0)));
                        itemModel1.setTitleText(cursor.getString(1));
                        itemModel1.setWebsite(cursor.getString(2));
                        itemModel1.setUsername(cursor.getString(3));
                        itemModel1.setPassword(cursor.getString(4));
                        itemModel1.setNotes(cursor.getString(5));
                        itemModel1.setColor(Integer.parseInt(cursor.getString(6)));
                        itemModelList.add(itemModel1);
                    } while (cursor.moveToNext());
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return itemModelList;
    }
    //get Single Note

    public CredentialsModel getSingleCredentialData(int ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        CredentialsModel credentialsModel = new CredentialsModel();
        String str_ID = String.valueOf(ID);

        String selectQuery = "SELECT * FROM " + Credentialts_Table + " WHERE " + KEY_ID + " ='" + ID + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (db.isOpen()) {
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        credentialsModel.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                        credentialsModel.setTitleText(cursor.getString(cursor.getColumnIndex(KEY_Title)));
                        credentialsModel.setWebsite(cursor.getString(cursor.getColumnIndex(KEY_Website)));
                        credentialsModel.setUsername(cursor.getString(cursor.getColumnIndex(KEY_Username)));
                        credentialsModel.setPassword(cursor.getString(cursor.getColumnIndex(KEY_Password)));
                        credentialsModel.setNotes(cursor.getString(cursor.getColumnIndex(KEY_Notes)));
                        credentialsModel.setColor(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_color))));
                    } while (cursor.moveToNext());
                }
            }
        }
        cursor.close();
        db.close();
        return credentialsModel;

    }

    //Update Single Credential
    public int updateSingleCredential(CredentialsModel credentialsModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Log.e(TAG, "updateSingleCredential: +title:: " + credentialsModel.getTitleText());
        Log.e(TAG, "updateSingleCredential: +KEY_Website:: " + credentialsModel.getWebsite());
        Log.e(TAG, "updateSingleCredential: +KEY_Username:: " + credentialsModel.getUsername());
        Log.e(TAG, "updateSingleCredential: +KEY_Password:: " + credentialsModel.getPassword());
        Log.e(TAG, "updateSingleCredential: +KEY_Notes:: " + credentialsModel.getColor());
        Log.e(TAG, "updateSingleCredential: +KEY_ID:: " + credentialsModel.getID());
        values.put(KEY_Title, credentialsModel.getTitleText());
        values.put(KEY_Website, credentialsModel.getWebsite());
        values.put(KEY_Username, credentialsModel.getUsername());
        values.put(KEY_Password, credentialsModel.getPassword());
        values.put(KEY_Notes, credentialsModel.getNotes());
        values.put(KEY_color, credentialsModel.getColor());
        // Updating row
        return db.update(Credentialts_Table, values, KEY_ID + "=?",
                new String[]{String.valueOf(credentialsModel.getID())});

    }


    //Remove Single Credential
    public void removeSingleCredential(int ID) {
        Log.e(TAG, "removeSingleData: Credentials:: " + ID);
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "DELETE FROM " + Credentialts_Table + " WHERE " + KEY_ID + " ='" + ID + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (db.isOpen()) {
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                    } while (cursor.moveToNext());
                }
            }
        }
        cursor.close();
        Log.e(TAG, "removeSingleNote: " + cursor.getCount());
        Log.e(TAG, "removeSingleNote: data Removed");
        cursor.close();
        db.close();
    }


    //Add new Note
    public int addNoteData(NoteListModel noteListModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_notes_title, noteListModel.getNoteTitle());
        values.put(KEY_notes_text, noteListModel.getNote_text());
        values.put(KEY_timeStamp, noteListModel.getTimestamp());

        // Inserting Row
        long ID = db.insert(Notes_TABLE, null, values);
        db.close();
        return (int) ID;
    }

    //Get all notes
    public ArrayList<NoteListModel> getAllNotes() {
        ArrayList<NoteListModel> itemModelList = new ArrayList<>();

        // Select all Query
        String selectQuery = "SELECT * FROM " + Notes_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if (db.isOpen()) {
            if (cursor.moveToFirst()) {
                do {
                    NoteListModel itemModel1 = new NoteListModel();
                    itemModel1.setID(Integer.parseInt(cursor.getString(0)));
                    itemModel1.setNoteTitle(cursor.getString(cursor.getColumnIndex(KEY_notes_title)));
                    itemModel1.setNote_text(cursor.getString(cursor.getColumnIndex(KEY_notes_text)));
                    itemModel1.setTimestamp(cursor.getString(cursor.getColumnIndex(KEY_timeStamp)));

                    // Adding Reminders to list
                    itemModelList.add(itemModel1);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        return itemModelList;
    }

    //get Single Note

    public NoteListModel getSingleNoteData(int ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        NoteListModel noteListModel = new NoteListModel();
        String str_ID = String.valueOf(ID);
        String selectQuery = "SELECT * FROM " + Notes_TABLE + " WHERE " + KEY_ID + " ='" + ID + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (db.isOpen()) {
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        noteListModel.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                        noteListModel.setNoteTitle(cursor.getString(cursor.getColumnIndex(KEY_notes_title)));
                        noteListModel.setNote_text(cursor.getString(cursor.getColumnIndex(KEY_notes_text)));
                        noteListModel.setTimestamp(cursor.getString(cursor.getColumnIndex(KEY_timeStamp)));
                    } while (cursor.moveToNext());
                }
            }
        }
        cursor.close();
        db.close();
        return noteListModel;

    }

    //Update Single Note
    public int updateSingleNote(NoteListModel noteListModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_notes_title, noteListModel.getNoteTitle());
        values.put(KEY_notes_text, noteListModel.getNote_text());
        values.put(KEY_timeStamp, noteListModel.getTimestamp());

        // Updating row
        return db.update(Notes_TABLE, values, KEY_ID + "=?",
                new String[]{String.valueOf(noteListModel.getID())});
    }

    //Remove note item

    public void removeSingleNote(int ID) {
        Log.e(TAG, "removeSingleData: notename:: " + ID);
        SQLiteDatabase db = this.getReadableDatabase();
        itemModel itemModel = new itemModel();
        String selectQuery = "DELETE FROM " + Notes_TABLE + " WHERE " + KEY_ID + " ='" + ID + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (db.isOpen()) {
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                    } while (cursor.moveToNext());
                }
            }
        }
        cursor.close();
        Log.e(TAG, "removeSingleNote: " + cursor.getCount());
        Log.e(TAG, "removeSingleNote: data Removed");
        cursor.close();
        db.close();
    }

    //GEt all Contacts
    public ArrayList<ContactModel> getallContacts() {
        ArrayList<ContactModel> itemModelList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            // Select all Query
            String selectQuery = "SELECT * FROM " + Contact_Table;

            db = this.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            // Looping through all rows and adding to list
            if (db.isOpen()) {
                if (cursor.moveToFirst()) {
                    do {
                        ContactModel itemModel1 = new ContactModel();
                        itemModel1.setID(Integer.parseInt(cursor.getString(0)));
                        itemModel1.setContactID(cursor.getString(1));
                        itemModel1.setContactFirstName(cursor.getString(2));
                        itemModel1.setContactLastName(cursor.getString(3));
                        itemModel1.setContactPhoneNumber(cursor.getString(4));
                        itemModel1.setContactEmail(cursor.getString(5));
                        itemModel1.setContactBirthDate(cursor.getString(6));
                        itemModel1.setContactFilePath(cursor.getString(7));
                        itemModel1.setContactFullName(cursor.getString(8));
                        itemModel1.setContactColor(Integer.parseInt(cursor.getString(9)));

                        // Adding Reminders to list
                        itemModelList.add(itemModel1);
                    } while (cursor.moveToNext());
                }

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (db != null)
                db.close();
        }


        return itemModelList;
    }

    //Add Single contact
    public int addContactData(ContactModel itemModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ContactID, itemModel.getContactID());
        values.put(KEY_FirstName, itemModel.getContactFirstName());
        values.put(KEY_LastNAme, itemModel.getContactLastName());
        values.put(KEY_PhoneNumber, itemModel.getContactPhoneNumber());
        values.put(KEY_Email, itemModel.getContactEmail());
        values.put(KEY_Birthdate, itemModel.getContactBirthDate());
        values.put(KEY_FilePAth, itemModel.getContactFilePath());
        values.put(KEY_DisplayFullname, itemModel.getContactFullName());
        values.put(KEY_color, itemModel.getContactColor());

        // Inserting Row
        long ID = db.insert(Contact_Table, null, values);
        db.close();
        return (int) ID;
    }

    //update Single contact Data
    public int updateSingleContactData(ContactModel itemModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_color, itemModel.getContactColor());

        // Updating row
        return db.update(Contact_Table, values, KEY_NEW_NAME + "=?",
                new String[]{String.valueOf(itemModel.getID())});
    }

    //get Single Contact
    public ContactModel getSingleContactData(int ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContactModel contactModel = new ContactModel();
        String str_ID = String.valueOf(ID);
        String selectQuery = "SELECT * FROM " + Contact_Table + " WHERE " + KEY_ID + " ='" + ID + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (db.isOpen()) {
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        contactModel.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                        contactModel.setContactFirstName(cursor.getString(cursor.getColumnIndex(KEY_FirstName)));
                        contactModel.setContactID(cursor.getString(cursor.getColumnIndex(KEY_ContactID)));
                        contactModel.setContactLastName(cursor.getString(cursor.getColumnIndex(KEY_LastNAme)));
                        contactModel.setContactFullName(cursor.getString(cursor.getColumnIndex(KEY_DisplayFullname)));
                        contactModel.setContactPhoneNumber(cursor.getString(cursor.getColumnIndex(KEY_PhoneNumber)));
                        contactModel.setContactBirthDate(cursor.getString(cursor.getColumnIndex(KEY_Birthdate)));
                        contactModel.setContactEmail(cursor.getString(cursor.getColumnIndex(KEY_Email)));
                        contactModel.setContactFilePath(cursor.getString(cursor.getColumnIndex(KEY_FilePAth)));
                        contactModel.setContactColor(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_color))));
                    } while (cursor.moveToNext());
                }
            }
        }
        cursor.close();
        db.close();
        return contactModel;

    }

    //Single Contact Data through contact ID
    public ContactModel getSingleContactDataByID(String contactID) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContactModel contactModel = new ContactModel();
        // String str_ID = String.valueOf(ID);
        String selectQuery = "SELECT * FROM " + Contact_Table + " WHERE " + KEY_ContactID + " ='" + contactID + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (db.isOpen()) {
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        contactModel.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                        contactModel.setContactFirstName(cursor.getString(cursor.getColumnIndex(KEY_FirstName)));
                        contactModel.setContactID(cursor.getString(cursor.getColumnIndex(KEY_ContactID)));
                        contactModel.setContactLastName(cursor.getString(cursor.getColumnIndex(KEY_LastNAme)));
                        contactModel.setContactFullName(cursor.getString(cursor.getColumnIndex(KEY_DisplayFullname)));
                        contactModel.setContactPhoneNumber(cursor.getString(cursor.getColumnIndex(KEY_PhoneNumber)));
                        contactModel.setContactBirthDate(cursor.getString(cursor.getColumnIndex(KEY_Birthdate)));
                        contactModel.setContactEmail(cursor.getString(cursor.getColumnIndex(KEY_Email)));
                        contactModel.setContactFilePath(cursor.getString(cursor.getColumnIndex(KEY_FilePAth)));
                        contactModel.setContactColor(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_color))));
                    } while (cursor.moveToNext());
                }
            }
        }
        cursor.close();
        db.close();
        return contactModel;

    }

    //Remove Single Contact Data
    public void removeSingleContactData(int ID) {
        Log.e(TAG, "removeSingleContactData: ID:: " + ID);
        SQLiteDatabase db = this.getReadableDatabase();
        itemModel itemModel = new itemModel();
        String selectQuery = "DELETE FROM " + Contact_Table + " WHERE " + KEY_ID + " ='" + ID + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        Log.e(TAG, "removeSingleContactData: " + cursor.getCount());
        Log.e(TAG, "removeSingleContactData: data Removed");
        cursor.close();
        db.close();
    }

    //Add new User DATA
    public int addUserData(UserModel userModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        long ID = -1;
        try {

            if (!db.isOpen()) {
                return (int) ID;
            }

            ContentValues values = new ContentValues();
            values.put(KEY_password, userModel.getPassword());
            values.put(KEY_Confirmpassword, userModel.getConfirm_password());
            values.put(KEY_DatabasePath, userModel.getDatabasePath());
            values.put(KEY_QUESTION, userModel.getQuestion());
            values.put(KEY_ANSWER, userModel.getAnswer());
            // Inserting Row
            ID = db.insert(USER_TABLE, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return (int) ID;
    }

    //Get Single USer Data

    public UserModel getSingleUserData(int ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        UserModel userModel = null;
        Cursor cursor = null;
        try {
            userModel = new UserModel();
            String str_ID = String.valueOf(ID);
            String selectQuery = "SELECT * FROM " + USER_TABLE + " WHERE " + KEY_ID + " ='" + ID + "'";
            cursor = db.rawQuery(selectQuery, null);
            if (db.isOpen()) {
                if (cursor != null && cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            userModel.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                            userModel.setPassword(cursor.getString(cursor.getColumnIndex(KEY_password)));
                            userModel.setConfirm_password(cursor.getString(cursor.getColumnIndex(KEY_Confirmpassword)));
                            userModel.setDatabasePath(cursor.getString(cursor.getColumnIndex(KEY_DatabasePath)));
                            userModel.setQuestion(cursor.getInt(cursor.getColumnIndex(KEY_QUESTION)));
                            userModel.setAnswer(cursor.getString(cursor.getColumnIndex(KEY_ANSWER)));
                        } while (cursor.moveToNext());
                    }
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return userModel;
    }

    public int getUserTableCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int uCount = 0;
        Cursor cursor = null;
        try {
            String selectQuery = "SELECT * FROM " + USER_TABLE;
            cursor = db.rawQuery(selectQuery, null);
            if (db.isOpen()) {
                if (cursor != null && cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        do {

                        } while (cursor.moveToNext());
                    }
                }
            }
            uCount = cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }


        return uCount;
    }

    public int updateSingleData(itemModel itemModel) {

        SQLiteDatabase db = null;
        int id = -1;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_STATUS, itemModel.getStatus());
            values.put(KEY_NEW_NAME, itemModel.getNewFilename());

            id = db.update(IMAGES_VIDEOS_TABLE, values, KEY_NEW_NAME + "=?", new String[]{itemModel.getNewFilename()});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null)
                db.close();
        }

        // Updating row
        return id;
    }

    public int updateFileSingleData(itemModel itemModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, itemModel.getStatus());
        values.put(KEY_NEW_NAME, itemModel.getNewFilename());

        // Updating row
        return db.update(OTHER_FILES_TABLE, values, KEY_NEW_NAME + "=?",
                new String[]{itemModel.getNewFilename()});
    }

    public int updateSingleDataVideo(itemModel itemModel) {
        int id = 0;
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_STATUS, itemModel.getStatus());
            values.put(KEY_NEW_NAME, itemModel.getNewFilename());


            id = db.update(VIDEOS_TABLE, values, KEY_NEW_NAME + "=?", new String[]{itemModel.getNewFilename()});


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (db != null)
                db.close();
        }
        // Updating row
        return id;
    }

    public int updateSingleDataPassword(UserModel itemModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_password, itemModel.getPassword());

        // Updating row
        return db.update(USER_TABLE, values, KEY_ID + "=?",
                new String[]{String.valueOf(itemModel.getID())});
    }

    public int updateUserQuestionAnswer(UserModel itemModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUESTION, itemModel.getQuestion());
        values.put(KEY_ANSWER, itemModel.getAnswer());

        // Updating row
        return db.update(USER_TABLE, values, KEY_ID + "=?",
                new String[]{String.valueOf(itemModel.getID())});
    }

    public int updateUserRaw(UserModel itemModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_password, itemModel.getPassword());
        values.put(KEY_QUESTION, itemModel.getQuestion());
        values.put(KEY_ANSWER, itemModel.getAnswer());
        values.put(KEY_Confirmpassword, itemModel.getConfirm_password());
        values.put(KEY_DatabasePath, itemModel.getDatabasePath());

        // Updating row
        return db.update(USER_TABLE, values, KEY_ID + "=?",
                new String[]{String.valueOf(itemModel.getID())});
    }

    public itemModel getSingleData(String fileNewName) {
        SQLiteDatabase db = null;
        itemModel itemModel = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            itemModel = new itemModel();
            String selectQuery = "SELECT * FROM " + IMAGES_VIDEOS_TABLE + " WHERE " + KEY_NEW_NAME + " ='" + fileNewName + "'";
            cursor = db.rawQuery(selectQuery, null);
            if (db.isOpen()) {
                if (cursor != null && cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        do {

                            itemModel.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
                            itemModel.setOldFilename(cursor.getString(cursor.getColumnIndex(KEY_OLD_NAME)));
                            itemModel.setNewFilename(cursor.getString(cursor.getColumnIndex(KEY_NEW_NAME)));
                            itemModel.setOldFilepath(cursor.getString(cursor.getColumnIndex(KEY_OLD_PATH)));
                            itemModel.setNewFilepath(cursor.getString(cursor.getColumnIndex(KEY_NEW_PATH)));
                            itemModel.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                        } while (cursor.moveToNext());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (db != null)
                db.close();
        }

        return itemModel;

    }

    public itemModel getSingleDataVideo(String fileNewName) {
        SQLiteDatabase db = this.getReadableDatabase();
        itemModel itemModel = new itemModel();
        String selectQuery = "SELECT * FROM " + VIDEOS_TABLE + " WHERE " + KEY_NEW_NAME + " ='" + fileNewName + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (db.isOpen()) {
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        itemModel.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
                        itemModel.setOldFilename(cursor.getString(cursor.getColumnIndex(KEY_OLD_NAME)));
                        itemModel.setNewFilename(cursor.getString(cursor.getColumnIndex(KEY_NEW_NAME)));
                        itemModel.setOldFilepath(cursor.getString(cursor.getColumnIndex(KEY_OLD_PATH)));
                        itemModel.setNewFilepath(cursor.getString(cursor.getColumnIndex(KEY_NEW_PATH)));
                        itemModel.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                    } while (cursor.moveToNext());
                }
            }
        }
        cursor.close();
        db.close();
        return itemModel;
    }

    public itemModel getSingleFileData(String fileNewName) {
        SQLiteDatabase db = this.getReadableDatabase();
        itemModel itemModel = new itemModel();
        String selectQuery = "SELECT * FROM " + OTHER_FILES_TABLE + " WHERE " + KEY_NEW_NAME + " ='" + fileNewName + "'";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (db.isOpen()) {
                if (cursor != null && cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            itemModel.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
                            itemModel.setOldFilename(cursor.getString(cursor.getColumnIndex(KEY_OLD_NAME)));
                            itemModel.setNewFilename(cursor.getString(cursor.getColumnIndex(KEY_NEW_NAME)));
                            itemModel.setOldFilepath(cursor.getString(cursor.getColumnIndex(KEY_OLD_PATH)));
                            itemModel.setNewFilepath(cursor.getString(cursor.getColumnIndex(KEY_NEW_PATH)));
                            itemModel.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                        } while (cursor.moveToNext());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

        return itemModel;
    }

    public itemModel getOldSingleData(String oldFilename) {
        SQLiteDatabase db = this.getReadableDatabase();
        itemModel itemModel = new itemModel();
        String selectQuery = "SELECT * FROM " + IMAGES_VIDEOS_TABLE + " WHERE " + KEY_OLD_NAME + " ='" + oldFilename + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (db.isOpen()) {
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        itemModel.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
                        itemModel.setOldFilename(cursor.getString(cursor.getColumnIndex(KEY_OLD_NAME)));
                        itemModel.setNewFilename(cursor.getString(cursor.getColumnIndex(KEY_NEW_NAME)));
                        itemModel.setOldFilepath(cursor.getString(cursor.getColumnIndex(KEY_OLD_PATH)));
                        itemModel.setNewFilepath(cursor.getString(cursor.getColumnIndex(KEY_NEW_PATH)));
                        itemModel.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                    } while (cursor.moveToNext());
                }
            }
        }
        cursor.close();
        db.close();
        return itemModel;

    }

    public itemModel getOldSingleDataVideo(String oldFilename) {
        SQLiteDatabase db = this.getReadableDatabase();
        itemModel itemModel = new itemModel();
        String selectQuery = "SELECT * FROM " + VIDEOS_TABLE + " WHERE " + KEY_OLD_NAME + " ='" + oldFilename + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    itemModel.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
                    itemModel.setOldFilename(cursor.getString(cursor.getColumnIndex(KEY_OLD_NAME)));
                    itemModel.setNewFilename(cursor.getString(cursor.getColumnIndex(KEY_NEW_NAME)));
                    itemModel.setOldFilepath(cursor.getString(cursor.getColumnIndex(KEY_OLD_PATH)));
                    itemModel.setNewFilepath(cursor.getString(cursor.getColumnIndex(KEY_NEW_PATH)));
                    itemModel.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return itemModel;

    }

    public void removeSingleData(String fileNewName) {
        Log.e(TAG, "removeSingleData: filename:: " + fileNewName);
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            itemModel itemModel = new itemModel();
            String selectQuery = "DELETE FROM " + IMAGES_VIDEOS_TABLE + " WHERE " + KEY_NEW_NAME + " ='" + fileNewName + "'";
            cursor = db.rawQuery(selectQuery, null);
            if (db.isOpen()) {
                if (cursor != null && cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        do {
                        } while (cursor.moveToNext());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (db != null)
                db.close();
        }

    }

    public void removeSingleDataVideo(String fileNewName) {
        Log.e(TAG, "removeSingleData: filename:: " + fileNewName);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            itemModel itemModel = new itemModel();
            String selectQuery = "DELETE FROM " + VIDEOS_TABLE + " WHERE " + KEY_NEW_NAME + " ='" + fileNewName + "'";
            cursor = db.rawQuery(selectQuery, null);
            if (db.isOpen()) {
                if (cursor != null && cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        do {
                        } while (cursor.moveToNext());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }

    }

    public void removeSingleFileData(String fileNewName) {
        Log.e(TAG, "removeSingleData: filename:: " + fileNewName);
        SQLiteDatabase db = this.getReadableDatabase();
        itemModel itemModel = new itemModel();
        String selectQuery = "DELETE FROM " + OTHER_FILES_TABLE + " WHERE " + KEY_NEW_NAME + " ='" + fileNewName + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (db.isOpen()) {
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                    } while (cursor.moveToNext());
                }
            }
        }
        cursor.close();
        Log.e(TAG, "removeSingleData: " + cursor.getCount());
        Log.e(TAG, "removeSingleData: data Removed");
        cursor.close();
        db.close();
    }

    public Cursor retrieve(CharSequence searchTerm) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_ID,
                KEY_FirstName,
                KEY_LastNAme,
                KEY_PhoneNumber,
                KEY_Email,
                KEY_Birthdate,
                KEY_FilePAth,
                KEY_DisplayFullname,
                KEY_color,
        };
        Cursor c = null;

        if (searchTerm != null && searchTerm.length() > 0) {
            String sql = "SELECT * FROM " + Contact_Table + " WHERE " + KEY_DisplayFullname + " LIKE '%" + searchTerm + "%'";
            c = db.rawQuery(sql, null);
            return c;

        }

        c = db.query(Contact_Table, columns, null, null, null, null, null);
        return c;
    }
}
