package com.calculator.vault.gallery.locker.hide.data.smartkit.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.calculator.vault.gallery.locker.hide.data.smartkit.adapter.TranslatorAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper
{
    private static String DB_NAME = "translator.sqlite";
    private static String DB_PATH = "/data/data/com.calculator.vault.gallery.locker.hide.data/databases/";
    private static int DB_VERSION = 1;
    private static String TAG = "DataBaseHelper";
    private final Context mContext;
    private SQLiteDatabase mDataBase;

    public DataBaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        Log.e(TAG, "DataBaseHelper: PATH"+DB_PATH );
        this.mContext = context;
    }

    @SuppressLint("WrongConstant")
    public void createDataBase() throws IOException
    {
        if (checkDataBase())
        {
            this.mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
            int oldVersion = this.mDataBase.getVersion();
            this.mDataBase.close();

            if (DB_VERSION > oldVersion)
            {
                prepareCopyDataBase();
                return;
            }
            return;
        }
        prepareCopyDataBase();
    }

    private void prepareCopyDataBase()
    {
        getReadableDatabase();
        close();
        try {
            copyDataBase();
            Log.e(TAG, "createDatabase database created");
        } catch (IOException e) {
            throw new Error("ErrorCopyingDataBase");
        }
    }

    private boolean checkDataBase() {
        return new File(DB_PATH + DB_NAME).exists();
    }

    private void copyDataBase() throws IOException
    {
        InputStream mInput = this.mContext.getAssets().open(DB_NAME);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        while (true)
        {
            int mLength = mInput.read(mBuffer);
            if (mLength > 0)
            {
                mOutput.write(mBuffer, 0, mLength);
            }
            else {
                mOutput.flush();
                mOutput.close();
                mInput.close();
                TranslatorAdapter mDbHelper = new TranslatorAdapter(this.mContext);
                mDbHelper.open();
                int[] pos = mDbHelper.getDefaultPositions();
                mDbHelper.UpdatePositions(pos[0], pos[1]);
                mDbHelper.close();
                return;
            }
        }
    }

    @SuppressLint("WrongConstant")
    public boolean openDataBase() throws SQLException
    {
        this.mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
        return this.mDataBase != null;
    }

    public synchronized void close()
    {
        if (this.mDataBase != null)
        {
            this.mDataBase.close();
        }
        super.close();
    }

    public void onCreate(SQLiteDatabase arg0) {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
