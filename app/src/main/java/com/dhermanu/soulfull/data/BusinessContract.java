package com.dhermanu.soulfull.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dean on 9/27/16.
 */

public class BusinessContract {

    public static final String CONTENT_AUTHORITY = "com.dhermanu.soulfull";
    public static final Uri BASE_CONTENT_Uri = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BUSINESS = "business";

    public static final class BusinessEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_Uri.buildUpon().appendPath(PATH_BUSINESS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BUSINESS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BUSINESS;

        public static final String TABLE_NAME = "business";

        public static final String COLUMN_ID = "id";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_BACKGROUNDPATH = "background_path";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_PHONE = "phone_number";
        public static final String COLUMN_DISPLAYPHONE = "phone_display";
        public static final String COLUMN_REVIEW = "review";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_ZIPCODE = "zip_code";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_COORD_LAT = "coord_lat";
        public static final String COLUMN_COORD_LONG = "coord_long";


        public static Uri buildBusinessUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}

