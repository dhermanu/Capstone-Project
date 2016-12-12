package com.dhermanu.soulfull.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dhermanu.soulfull.data.HalalContract.HalalEntry;

/**
 * Created by dean on 9/28/16.
 */


public class HalalDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "halal.db";

    public HalalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_BUSINESS_TABLE = "CREATE TABLE " + HalalEntry.TABLE_NAME + " (" +
                // the ID of the location entry associated with this weather data
                HalalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HalalEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                HalalEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                HalalEntry.COLUMN_BACKGROUNDPATH + " TEXT NOT NULL," +

                HalalEntry.COLUMN_CATEGORY + " TEXT NOT NULL, " +
                HalalEntry.COLUMN_PHONE + " TEXT NOT NULL, " +

                HalalEntry.COLUMN_DISPLAYPHONE + " TEXT NOT NULL, " +
                HalalEntry.COLUMN_REVIEW + " TEXT NOT NULL, " +
                HalalEntry.COLUMN_RATING + " REAL NOT NULL, " +
                HalalEntry.COLUMN_ADDRESS + " TEXT NOT NULL, " +
                HalalEntry.COLUMN_DISPLAYADDRESS + " TEXT NOT NULL, " +

                HalalEntry.COLUMN_CITY + " TEXT NOT NULL, " +
                HalalEntry.COLUMN_COORD_LAT + " REAL NOT NULL, " +
                HalalEntry.COLUMN_COORD_LONG + " REAL NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_BUSINESS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HalalEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
