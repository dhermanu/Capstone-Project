package com.dhermanu.soulfull.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.dhermanu.soulfull.data.BusinessContract.BusinessEntry;

import android.support.annotation.Nullable;

/**
 * Created by dean on 9/28/16.
 */

public class BusinessProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private BusinessDbHelper mOpenHelper;

    static final int BUSINESS = 100;
    static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BusinessContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, BusinessContract.PATH_BUSINESS, BUSINESS);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new BusinessDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs
            , String sortOrder) {
        Cursor retCursor;
        switch(sUriMatcher.match(uri)){

            case BUSINESS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        BusinessEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch(match){
            case BUSINESS:
                return BusinessEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch(match){
            case BUSINESS:{
                long _id = db.insert(BusinessEntry.TABLE_NAME, null, contentValues);
                if(_id > 0 )
                    returnUri = BusinessEntry.buildBusinessUri(_id);
                else
                    throw new android.database.SQLException("Faile to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if ( null == selection ) selection = "1";
        switch(match){
            case BUSINESS:{
                rowsDeleted = db.delete(
                        BusinessEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues,
                      String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch(match){
            case BUSINESS:{
                rowsUpdated = db.update(
                        BusinessEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
