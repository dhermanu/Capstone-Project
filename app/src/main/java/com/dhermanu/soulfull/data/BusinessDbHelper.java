package com.dhermanu.soulfull.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.dhermanu.soulfull.data.BusinessContract.BusinessEntry;
import com.yelp.clientlib.entities.Business;

import okio.BufferedSink;

/**
 * Created by dean on 9/28/16.
 */


public class BusinessDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "business.db";

    public BusinessDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_BUSINESS_TABLE = "CREATE TABLE " + BusinessEntry.TABLE_NAME + " (" +
                // the ID of the location entry associated with this weather data
                BusinessEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BusinessEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                BusinessEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                BusinessEntry.COLUMN_BACKGROUNDPATH + " TEXT NOT NULL," +

                BusinessEntry.COLUMN_CATEGORY + " TEXT NOT NULL, " +
                BusinessEntry.COLUMN_PHONE + " TEXT NOT NULL, " +

                BusinessEntry.COLUMN_DISPLAYPHONE + " TEXT NOT NULL, " +
                BusinessEntry.COLUMN_REVIEW + " TEXT NOT NULL, " +
                BusinessEntry.COLUMN_RATING + " REAL NOT NULL, " +
                BusinessEntry.COLUMN_ADDRESS + " TEXT NOT NULL, " +

                BusinessEntry.COLUMN_CITY + " TEXT NOT NULL, " +
                BusinessEntry.COLUMN_ZIPCODE + " TEXT NOT NULL, " +
                BusinessEntry.COLUMN_STATE + " TEXT NOT NULL, " +
                BusinessEntry.COLUMN_COORD_LAT + " REAL NOT NULL, " +
                BusinessEntry.COLUMN_COORD_LONG + " REAL NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_BUSINESS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BusinessEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
