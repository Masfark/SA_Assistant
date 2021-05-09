package com.example.sa_assistant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ShopsDB";
    public static final String TABLE_SHOPS = "Shops";
    public static final String TABLE_USERS = "Users";
    public static final String TABLE_DATE_TIME = "Time";
    public static final String TABLE_KBSA_REPORTS = "Kbsa_reports";

    public static final String KEY_ID = "_id";
    public static final String KEY_ID_USER = "_id";
    public static final String KEY_ID_TIME = "_id";
    public static final String KEY_ID_REPORT = "_id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_NUMBER = "number";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CITY = "city";
    public static final String KEY_RES_NUM = "reserve_number";
    public static final String KEY_OOO = "ooo";
    public static final String KEY_NOTE = "note";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_TIME_START = "start_time";
    public static final String KEY_TIME_END = "end_time";
    public static final String KEY_REPORT = "report";
    public static final String KEY_REPORT_CHECK = "report_check";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_SHOPS + "(" + KEY_ID + " integer primary key,"
                + KEY_NUMBER + " integer,"
                + KEY_CITY + " text,"
                + KEY_ADDRESS + " text,"
                + KEY_OOO + " text,"
                + KEY_RES_NUM + " text,"
                + KEY_NOTE + " text,"
                + KEY_TIME_START + " text,"
                + KEY_TIME_END + " text" + ")");

        db.execSQL("create table " + TABLE_USERS + "(" + KEY_ID_USER + " integer primary key,"
                + KEY_USERNAME + " text" + ")");

        db.execSQL("create table " + TABLE_DATE_TIME + "(" + KEY_ID_TIME + " integer primary key,"
                + KEY_DATE + " text,"
                + KEY_TIME + " text" + ")");

        db.execSQL("create table " + TABLE_KBSA_REPORTS + "(" + KEY_ID_REPORT + " integer primary key,"
                + KEY_REPORT + " text," + KEY_REPORT_CHECK + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (DATABASE_VERSION < 11) {
            db.execSQL("create table " + TABLE_KBSA_REPORTS + "(" + KEY_ID_REPORT + " integer primary key,"
                    + KEY_REPORT + " text," + KEY_REPORT_CHECK + " text" + ")");
            db.execSQL("alter table " + TABLE_SHOPS + " add " + KEY_NOTE + " text");
        }

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);

    }


}
