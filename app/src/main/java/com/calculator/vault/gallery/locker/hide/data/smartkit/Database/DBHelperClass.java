package com.calculator.vault.gallery.locker.hide.data.smartkit.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.calculator.vault.gallery.locker.hide.data.smartkit.modle.HistoryModel;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Vasundhara on 4/10/2018.
 */

public class DBHelperClass {

    private static String DATABASE_NAME = "DB_Smart_Kit";    // Database Name
    private static  int DATABASE_Version = 1;    // Database Version

    public  myDbHelper myhelper;

    public static DBHelperClass DBHelper;

    public DBHelperClass(Context context) {
        myhelper = new myDbHelper(context);
    }


    public class myDbHelper extends SQLiteOpenHelper {
        //private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context = context;
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL("create table calculation_histroy(id INTEGER PRIMARY KEY AUTOINCREMENT,calculator text,expression text,answer text,speak text); ");
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {

                db.execSQL("DROP TABLE IF EXISTS calculation_histroy ");
                onCreate(db);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void insertData(String calculator, String expression, String answer, String speak) {

          update();
            SQLiteDatabase dbb = myhelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("calculator", calculator);
            contentValues.put("expression", expression);
            contentValues.put("answer", answer);
            contentValues.put("speak", speak);

            dbb.insert("calculation_histroy", null, contentValues);

            Log.e("insert",""+contentValues);
        }


        void  update(){

        }
        public ArrayList<HistoryModel> getAllData() {
            ArrayList<HistoryModel> historyModels = new ArrayList<>();


            try {
                SQLiteDatabase dbb = myhelper.getWritableDatabase();
                String sql = "SELECT * FROM calculation_histroy";
                Cursor cursor = dbb.rawQuery(sql, null);

                if(cursor.getCount()!=0){
                    if(cursor.moveToFirst()){
                        do{
                            HistoryModel historyModel=new HistoryModel();


                            historyModel.setId(String.valueOf(cursor.getInt(cursor.getColumnIndex("id"))));
                            historyModel.setCalculator(cursor.getString(cursor.getColumnIndex("calculator")));
                            historyModel.setExpression(cursor.getString(cursor.getColumnIndex("expression")));
                            historyModel.setAnswer(cursor.getString(cursor.getColumnIndex("answer")));
                            historyModel.setSpeak(cursor.getString(cursor.getColumnIndex("speak")));

                            historyModels.add(historyModel);
                        }while(cursor.moveToNext());
                    }
                    cursor.close();
                }


            }catch (Exception e){
                e.printStackTrace();
            }
            Collections.reverse(historyModels);

            return historyModels;
        }


        public void allDelete(){
            try {
                SQLiteDatabase dbb = myhelper.getWritableDatabase();
                String sql = " DELETE FROM calculation_histroy";
                dbb.execSQL(sql);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public void Delete(String id){
            try {
                SQLiteDatabase dbb = myhelper.getWritableDatabase();
                String sql = " DELETE FROM calculation_histroy where id="+id;
                dbb.execSQL(sql);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}


