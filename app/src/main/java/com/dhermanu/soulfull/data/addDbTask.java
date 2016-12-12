package com.dhermanu.soulfull.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;

import com.dhermanu.soulfull.data.HalalContract.HalalEntry;
import com.dhermanu.soulfull.model.Halal;

/**
 * Created by dhermanu on 10/2/16.
 */

public class addDbTask extends AsyncTask<Void, Void, Uri> {

    private static final String FAV_PREF = "favorite_halal";
    private final Context context;
    private final Halal halal;

    public addDbTask(Context context, Halal halal) {
        this.context = context;
        this.halal = halal;
    }

    @Override
    protected Uri doInBackground(Void... voids) {

        SharedPreferences pref
                = context.getSharedPreferences(FAV_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editList = pref.edit();
        editList.putBoolean(halal.getId(), true);
        editList.apply();

        ContentValues halalValues = new ContentValues();

        halalValues.put(HalalEntry.COLUMN_ID, halal.getId());
        halalValues.put(HalalEntry.COLUMN_NAME, halal.getName());
        halalValues.put(HalalEntry.COLUMN_BACKGROUNDPATH, halal.getBackgroundPath());
        halalValues.put(HalalEntry.COLUMN_CATEGORY, halal.getCategory());
        halalValues.put(HalalEntry.COLUMN_PHONE, halal.getPhone());
        halalValues.put(HalalEntry.COLUMN_DISPLAYPHONE, halal.getPhoneDisplay());
        halalValues.put(HalalEntry.COLUMN_REVIEW, halal.getReview());
        halalValues.put(HalalEntry.COLUMN_RATING, halal.getRating());
        halalValues.put(HalalEntry.COLUMN_DISPLAYADDRESS, halal.getDisplayAddress());
        halalValues.put(HalalEntry.COLUMN_ADDRESS, halal.getAddress());
        halalValues.put(HalalEntry.COLUMN_CITY, halal.getCity());
        halalValues.put(HalalEntry.COLUMN_COORD_LAT, halal.getCoord_lat());
        halalValues.put(HalalEntry.COLUMN_COORD_LONG, halal.getCoord_long());

//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
//        ComponentName thisWidget = new ComponentName( context, appWidget.class );
//        AppWidgetManager.getInstance(context).updateAppWidget( thisWidget, remoteViews );


        return this.context
                .getContentResolver()
                .insert(HalalEntry.CONTENT_URI, halalValues);
    }
}
