package com.calculator.vault.gallery.locker.hide.data.smartkit.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.calculator.vault.gallery.locker.hide.data.smartkit.Database.DataBaseHelper;
import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.Customlangmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TranslatorAdapter {
    protected static final String TAG = "DataAdapter";
    private String locale = Locale.getDefault().getLanguage();
    private Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public TranslatorAdapter(Context context) {
        this.mContext = context;
        this.mDbHelper = new DataBaseHelper(context);
    }

    public TranslatorAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
            return this;
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
    }

    public TranslatorAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
            return this;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public void close() {
        mDbHelper.close();
    }

    public List<Customlangmodel> getAllLanguages() {
        List<Customlangmodel> languagesList = new ArrayList();
        Cursor cursor = mDb.rawQuery("SELECT _id, language FROM `languages`", null);
        Log.e(TAG, "getAllLanguages: => cursor => "+cursor.getCount() );
        if (cursor.moveToFirst()) {
            do {
                String ilang = cursor.getString(1);
                String tlang = cursor.getString(1);
                if (ilang.equals("zh-TW")) {
                    ilang = "zh";
                    tlang = "zh_TW";
                }
                int imageid = mContext.getResources().getIdentifier("flag_" + ilang, "drawable", mContext.getPackageName());
                Customlangmodel languages = new Customlangmodel(Integer.parseInt(cursor.getString(0)),imageid,cursor.getString(1),getStringResourceByName(tlang));
                languages.setId(Integer.parseInt(cursor.getString(0)));
                languages.setImageId(imageid);
                languages.setLang(cursor.getString(1));
                languages.setName(getStringResourceByName(tlang));
                languagesList.add(languages);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return languagesList;
    }

    public String getStringResourceByName(String aString) {
        Log.e(TAG, "getStringResourceByName: => aString => "+aString );
        Log.e(TAG, "getStringResourceByName: => packgname => " +mContext.getPackageName());
        Log.e(TAG, "getStringResourceByName: => mContext => " +mContext);
        Log.e(TAG, "getStringResourceByName: => string => "+ String.valueOf(mContext.getResources().getIdentifier(aString, "string", mContext.getPackageName())));
        return mContext.getResources().getString(mContext.getResources().getIdentifier(aString, "string", mContext.getPackageName()));
    }

    public int[] getPositions() {
        int[] res = new int[2];
        Cursor cursor = mDb.rawQuery("SELECT frompos, topos FROM positions", null);
        if (cursor.moveToFirst()) {
            do {
                res[0] = Integer.parseInt(cursor.getString(0));
                res[1] = Integer.parseInt(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return res;
    }

    public int[] getDefaultPositions() {
        int[] res = new int[]{15, 17};
        String str = locale;
        int i = -1;
        switch (str.hashCode()) {
            case 3201:
                if (str.equals("de")) {
                    i = 3;
                    break;
                }
                break;
            case 3241:
                if (str.equals("en")) {
                    i = 1;
                    break;
                }
                break;
            case 3246:
                if (str.equals("es")) {
                    i = 0;
                    break;
                }
                break;
            case 3276:
                if (str.equals("fr")) {
                    i = 2;
                    break;
                }
                break;
            case 3371:
                if (str.equals("it")) {
                    i = 4;
                    break;
                }
                break;
            case 3588:
                if (str.equals("pt")) {
                    i = 5;
                    break;
                }
                break;
            case 3651:
                if (str.equals("ru")) {
                    i = 6;
                    break;
                }
                break;
        }
        switch (i) {
            case 0:
                res[0] = 17;
                res[1] = 15;
                break;
            case 1:
                res[1] = 17;
                break;
            case 2:
                res[1] = 22;
                break;
            case 3:
                res[1] = 13;
                break;
            case 4:
                res[1] = 36;
                break;
            case 5:
                res[1] = 64;
                break;
            case 6:
                res[1] = 66;
                break;
        }
        return res;
    }

    public void UpdatePositions(int frompos, int topos) {
        try {
            mDb.execSQL("UPDATE positions SET frompos = " + frompos + ", topos = " + topos + "");
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }
}