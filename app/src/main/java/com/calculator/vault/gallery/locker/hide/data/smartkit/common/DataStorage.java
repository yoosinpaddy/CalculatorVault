package com.calculator.vault.gallery.locker.hide.data.smartkit.common;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.WorldClock;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class DataStorage {
    public static final int BUFFER_SIZE = 512;
    public static final String DATABASE = "worldclock.db";
    public static final String DATA_STORAGE_SETTING = "settings";
    public static final String TAG = "DataStorage";

    public static int getDateTimeFormat(Context context) {
        int dateTimeFormat = 0;
        try {
            DataInputStream in = new DataInputStream(new BufferedInputStream(context.openFileInput(DATA_STORAGE_SETTING), 512));
            dateTimeFormat = in.readInt();
            in.close();
            return dateTimeFormat;
        } catch (FileNotFoundException e) {
            setDateTimeFormat(context, dateTimeFormat);
            return dateTimeFormat;
        } catch (IOException e2) {
            e2.printStackTrace();
            return dateTimeFormat;
        }
    }

    public static void setDateTimeFormat(Context context, int dateTimeFormat) {
        try {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(context.openFileOutput(DATA_STORAGE_SETTING, 0), 512));
            out.writeInt(dateTimeFormat);
            out.close();
        } catch (Exception e) {
        }
    }

    public static int addWorldClock(Context context, WorldClock worldClock) {
        SQLiteDatabase db = context.openOrCreateDatabase(DATABASE, 0, null);
        db.execSQL("insert into world_clock values (null, '" + worldClock.getTimeZoneId() + "', " + worldClock.getDisplayFormat() + ")");
        int lastInsertRowId = 0;
        Cursor cursor = db.rawQuery("select last_insert_rowid()", null);
        while (cursor.moveToNext()) {
            lastInsertRowId = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return lastInsertRowId;
    }

    public static void updateWorldClock(Context context, WorldClock worldClock) {
        SQLiteDatabase db = context.openOrCreateDatabase(DATABASE, 0, null);
        db.execSQL("update world_clock set time_zone_id = '" + worldClock.getTimeZoneId() + "', display_format = '" + worldClock.getDisplayFormat() + "' where id = " + worldClock.getId());
        db.close();
    }

    public static void removeWorldClock(Context context, WorldClock worldClock) {
        SQLiteDatabase db = context.openOrCreateDatabase(DATABASE, 0, null);
        db.execSQL("delete from world_clock where id = " + worldClock.getId());
        db.close();
    }

    public static List<WorldClock> getWorldClockList(Context context) {
        SQLiteDatabase db = context.openOrCreateDatabase(DATABASE, 0, null);
        Cursor cursor = db.rawQuery("select name from sqlite_master where type = 'table'", null);
        if (cursor.getCount() <= 1) {
            db.execSQL("create table world_clock (id integer primary key autoincrement, time_zone_id text, display_format integer)");
        }
        /*if (cursor.getCount() <= 1) {

            //String[] defaultWorldClockList = context.getResources().getStringArray(R.array.default_world_clock);
            int[] defaultDisplayFormatList = context.getResources().getIntArray(R.array.default_display_format);
            for (int i = 0; i < defaultWorldClockList.length; i++) {
                db.execSQL("insert into world_clock values (null, '" + defaultWorldClockList[i] + "', " + defaultDisplayFormatList[i] + ")");
            }
        }*/
        cursor.close();
        List<WorldClock> worldClockList = new Vector();
        cursor = db.rawQuery("select * from world_clock order by id", null);
        while (cursor.moveToNext()) {
            WorldClock worldClock = new WorldClock();
            worldClock.setId(cursor.getInt(0));
            worldClock.setTimeZoneId(cursor.getString(1));
            worldClock.setDisplayFormat(cursor.getInt(2));
            worldClockList.add(worldClock);
        }
        cursor.close();
        db.close();
        return worldClockList;
    }
}
